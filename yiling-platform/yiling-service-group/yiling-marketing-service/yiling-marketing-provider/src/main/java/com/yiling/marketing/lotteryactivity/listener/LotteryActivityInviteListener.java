package com.yiling.marketing.lotteryactivity.listener;

import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.YlStrUtils;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.mq.MessageListener;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityJoinRuleDTO;
import com.yiling.marketing.lotteryactivity.dto.request.AddLotteryTimesRequest;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityDO;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityGetTypeEnum;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityJoinRuleService;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityService;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityTimesService;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * C端抽奖活动邀请成功 监听类
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-23
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_HMC_GZH_FIRST_SUBSCRIBE, consumerGroup = Constants.TOPIC_HMC_GZH_FIRST_SUBSCRIBE)
public class LotteryActivityInviteListener implements MessageListener {

    @Autowired
    LotteryActivityTimesService lotteryActivityTimesService;
    @Autowired
    LotteryActivityService lotteryActivityService;
    @Autowired
    LotteryActivityJoinRuleService lotteryActivityJoinRuleService;

    @DubboReference
    UserApi userApi;

    @MdcLog
    @Override
    public MqAction consume(MessageExt message, ConsumeConcurrentlyContext context) {
        String msg = null;
        try {
            msg = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
            if (StrUtil.isBlank(msg)){
                log.error("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 原因:{OrderCode为空}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
                return MqAction.CommitMessage;
            }
            log.info("C端抽奖活动邀请成功获取参数={}", msg);
            // 字符串格式：qt:20_actId:61_uId:17426
            Map<String, String> map = YlStrUtils.dealParam(msg);
            String activityId = map.get("actId");
            String uid = map.get("uId");

            LotteryActivityDO activityDO = lotteryActivityService.getById(activityId);
            if (Objects.isNull(activityDO)) {
                log.info("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{} 原因是抽奖活动为空", message.getMsgId(), message.getTopic(), message.getTags(), msg);
                return MqAction.CommitMessage;
            }
            Date now = new Date();
            if (activityDO.getStartTime().after(now) || activityDO.getEndTime().before(now) || !activityDO.getStatus().equals(EnableStatusEnum.ENABLED.getCode())) {
                log.info("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{} 原因是抽奖活动状态异常", message.getMsgId(), message.getTopic(), message.getTags(), msg);
                return MqAction.CommitMessage;
            }

            // 获取C端邀请规则，获取赠送次数
            LotteryActivityJoinRuleDTO joinRuleDTO = lotteryActivityJoinRuleService.getByLotteryActivityId(Long.parseLong(activityId));
            if (Objects.isNull(joinRuleDTO) || joinRuleDTO.getInviteGive() <= 0) {
                log.info("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{} 原因是抽奖活动C端规则为空或邀请新粉丝赠送抽奖次数为0", message.getMsgId(), message.getTopic(), message.getTags(), msg);
                return MqAction.CommitMessage;
            }

            // 增加抽奖次数
            AddLotteryTimesRequest request = new AddLotteryTimesRequest();
            request.setLotteryActivityId(Long.parseLong(activityId));
            request.setActivityName(activityDO.getActivityName());
            request.setPlatformType(2);
            request.setUid(Long.parseLong(uid));
            request.setUname(Optional.ofNullable(userApi.getById(Long.parseLong(uid))).orElse(new UserDTO()).getNickName());
            request.setTimes(joinRuleDTO.getInviteGive());
            request.setGetType(LotteryActivityGetTypeEnum.SHARE.getCode());
            request.setOpUserId(Long.parseLong(uid));
            Integer times = lotteryActivityTimesService.addLotteryTimes(request);
            log.info("C端抽奖活动邀请成功 抽奖活动ID={} 邀请人={} 赠送抽奖次数={} 赠送后剩余抽奖次数={}", activityId, uid, joinRuleDTO.getInviteGive(), times);
            if (times <= 0){
                log.error("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 异常信息:{isCreate=False}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
                return MqAction.ReconsumeLater;
            }
            log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
        } catch (Exception e) {
            log.error("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 异常信息:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg, e);
            return MqAction.ReconsumeLater;
        }
        return MqAction.CommitMessage;
    }
}
