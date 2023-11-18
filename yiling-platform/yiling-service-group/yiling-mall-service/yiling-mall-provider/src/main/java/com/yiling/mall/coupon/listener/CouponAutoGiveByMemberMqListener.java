package com.yiling.mall.coupon.listener;

import java.util.List;


import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.mq.MessageListener;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;
import com.yiling.mall.coupon.bo.CouponActivityAutoGiveContext;
import com.yiling.mall.coupon.handler.CouponAutoGiveByMemberHandler;
import com.yiling.marketing.coupon.api.CouponApi;
import com.yiling.marketing.couponactivity.api.CouponActivityApi;
import com.yiling.marketing.couponactivityautogive.api.CouponActivityAutoGiveApi;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveDetailDTO;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.member.api.MemberApi;
import com.yiling.user.member.dto.CurrentMemberDTO;
import com.yiling.user.member.dto.request.CurrentMemberForMarketingDTO;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: houjie.sun
 * @date: 2021/11/24
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_MEMBER_COUPON_AUTOMATIC_SEND, consumerGroup = Constants.TOPIC_MEMBER_COUPON_AUTOMATIC_SEND)
public class CouponAutoGiveByMemberMqListener implements MessageListener {

    @DubboReference
    EnterpriseApi                         enterpriseApi;
    @DubboReference
    MemberApi                             memberApi;
    @DubboReference
    CouponActivityAutoGiveApi             autoGiveApi;
    @DubboReference
    CouponActivityApi                     couponActivityApi;
    @DubboReference
    CouponApi                             couponApi;
    @Autowired
    private CouponAutoGiveByMemberHandler couponAutoGiveByMemberHandler;

    @MdcLog
    @Override
    public MqAction consume(MessageExt message, ConsumeConcurrentlyContext context) {
        String eidStr = null;
        try {
            //
            eidStr = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
            log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), eidStr);

            if (StrUtil.isBlank(eidStr)) {
                log.warn("CouponActivityAutoGiveMqListener，MsgId:{}, 企业id为空", message.getMsgId());
                return MqAction.CommitMessage;
            }

            // 1、企业信息
            long eid = Long.parseLong(eidStr);
            EnterpriseDTO enterprise = enterpriseApi.getById(eid);
            log.info("#consume# 获取企业信息eid:[{}],返回值：[{}]", eid, enterprise);
            if (ObjectUtil.isNull(enterprise)) {
                log.warn("CouponActivityAutoGiveMqListener, MsgId:{}, 企业信息不存在, eid:[{}]", message.getMsgId(), eid);
                return MqAction.CommitMessage;
            }

            // 2、会员信息
            CurrentMemberForMarketingDTO member = memberApi.getCurrentMemberForMarketing(eid);
            log.info("#consume# 获取会员信息eid:[{}],返回值：[{}]", eid, member);
            if (ObjectUtil.isNull(member)) {
                log.warn("CouponActivityAutoGiveMqListener, MsgId:{}, 会员信息不存在, eid:[{}]", message.getMsgId(), eid);
                return MqAction.CommitMessage;
            }
            if (!ObjectUtil.equal(1, member.getCurrentMember())) {
                log.warn("CouponActivityAutoGiveMqListener, MsgId:{}, 当前企业不是会员, eid:[{}]", message.getMsgId(), eid);
                return MqAction.CommitMessage;
            }
            CouponActivityAutoGiveContext autoGiveContext = CouponActivityAutoGiveContext.builder().enterpriseDTO(enterprise).currentMemberDTO(member).build();

            // 3、可发放活动
            List<CouponActivityAutoGiveDetailDTO> autoGiveDetailList = couponAutoGiveByMemberHandler.checkMemberGiveRules(autoGiveContext);
            if (CollectionUtils.isEmpty(autoGiveDetailList)) {
                log.warn("当前企业未匹配到活动信息，企业id:{}", eid);
                return MqAction.CommitMessage;
            }
            // 4、发放处理
            boolean isSuccess = couponAutoGiveByMemberHandler.memberHandler(enterprise, autoGiveDetailList);

            log.info("自动发放优惠券消息处理完毕");
            if (isSuccess) {
                return MqAction.CommitMessage;
            } else {
                return MqAction.ReconsumeLater;
            }
        } catch (Exception e) {
            log.error("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 异常信息:{}", message.getMsgId(), message.getTopic(), message.getTags(), eidStr, e);
            throw e;
        }
    }

}
