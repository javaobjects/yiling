package com.yiling.hmc.order.mq.listener;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.yiling.basic.mq.api.MqMessageConsumeFailureApi;
import com.yiling.basic.mq.handler.IConsumeFailureHandler;
import com.yiling.basic.mq.listener.AbstractMessageListener;
import com.yiling.basic.wx.api.GzhUserApi;
import com.yiling.basic.wx.dto.GzhUserDTO;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.redis.service.RedisService;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.hmc.config.H5DomainProperties;
import com.yiling.user.system.api.HmcUserApi;
import com.yiling.user.system.bo.HmcUser;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.Collections;
import java.util.Objects;

/**
 * HMC 关注公众号延迟消息监听器
 *
 * @author: fan.shen
 * @date: 2023/3/29
 */
@Slf4j
@RefreshScope
@RocketMqListener(topic = Constants.TOPIC_HMC_GZH_SUBSCRIBE_DELAY, consumerGroup = Constants.TOPIC_HMC_GZH_SUBSCRIBE_DELAY)
public class HmcGzhSubscribeDelayListener extends AbstractMessageListener {

    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;

    @DubboReference
    GzhUserApi gzhUserApi;

    @DubboReference
    HmcUserApi hmcUserApi;

    // @DubboReference
    // SysOperLogApi sysOperLogApi;


    @Autowired
    RedisService redisService;

    // C端域名
    @Autowired
    private H5DomainProperties h5DomainProperties;

    // C端抽奖
    @Value("${C_draw_activity_url}")
    private String drawActivityUrl;

    // 关注推送抽奖消息图片
    @Value("${C_gzh_subscribe_lottery_img}")
    private String gzhSubscribeLotteryImgUrl;

    // 关注推送健康测评消息图片
    @Value("${C_gzh_subscribe_healthEvaluate_img}")
    private String gzhSubscribeHealthEvaluateImgUrl;

    @Autowired
    WxMpService wxMpService;

    @MdcLog
    @Override
    @GlobalTransactional
    protected MqAction consume(String gzhOpenId, MessageExt messageExt, ConsumeConcurrentlyContext context) {
        log.info("[HmcGzhSubscribeDelayListener]HMC监听器收到消息：{}", gzhOpenId);


        GzhUserDTO gzhUserDTO = gzhUserApi.getByGzhOpenId(gzhOpenId);
        if (Objects.isNull(gzhUserDTO)) {
            log.warn("根据openId:{}未获取到用户", gzhOpenId);
            return MqAction.CommitMessage;
        }

        // 1、获取用户
        HmcUser hmcUser = hmcUserApi.getByOpenId(gzhOpenId);

        // 2、查询操作日志
        // 最近访问抽奖、测评标记
        boolean lotteryFlag = false;
        boolean evaluateFlag = false;
        if (Objects.nonNull(hmcUser)) {
            // QuerySysOperLogPageListRequest request = new QuerySysOperLogPageListRequest();
            // request.setOperId(hmcUser.getUserId());
            // request.setRequestUrl("/lotteryActivity/getActivityPage");
            // Page<SysOperLogDTO> lotteryPage = sysOperLogApi.queryListPage(request);
            // request.setRequestUrl("/healthEvaluate/indexPage");
            // Page<SysOperLogDTO> evaluatePage = sysOperLogApi.queryListPage(request);


            String health_evaluate_access = RedisKey.generate("hmc", "health_evaluate_access", String.valueOf(hmcUser.getUserId()));
            if (!redisService.hasKey(health_evaluate_access)) {
                log.warn("根据redis key 未获取到测评访问记录，跳过处理,key:{}", health_evaluate_access);
            } else {
                DateTime accessDate = DateUtil.date(Long.parseLong(redisService.get(health_evaluate_access).toString()));
                if (DateUtil.compare(accessDate, gzhUserDTO.getUpdateTime()) > 0) {
                    log.info("根据redis key 获取到访问记录，key:{},访问时间:{}", health_evaluate_access, accessDate);
                    lotteryFlag = true;
                }
            }

            String lottery_activity_access = RedisKey.generate("hmc", "lottery_activity_access", String.valueOf(hmcUser.getUserId()));
            if (!redisService.hasKey(lottery_activity_access)) {
                log.warn("根据redis key 未获取到抽奖访问记录，跳过处理,key:{}", lottery_activity_access);
            } else {
                DateTime accessDate = DateUtil.date(Long.parseLong(redisService.get(lottery_activity_access).toString()));
                if (DateUtil.compare(accessDate, gzhUserDTO.getUpdateTime()) > 0) {
                    log.info("根据redis key 获取到抽奖访问记录，key:{},访问时间:{}", lottery_activity_access, accessDate);
                    evaluateFlag = true;
                }
            }

            // if (Objects.nonNull(lotteryPage) && CollUtil.isNotEmpty(lotteryPage.getRecords()) && DateUtil.compare(lotteryPage.getRecords().get(0).getOpTime(), gzhUserDTO.getUpdateTime()) > 0) {
            //     lotteryFlag = true;
            // }
            // if (Objects.nonNull(evaluatePage) && CollUtil.isNotEmpty(evaluatePage.getRecords()) && DateUtil.compare(evaluatePage.getRecords().get(0).getOpTime(), gzhUserDTO.getUpdateTime()) > 0) {
            //     evaluateFlag = true;
            // }
        }

        if (lotteryFlag && evaluateFlag) {
            log.warn("用户自关注之后最近已访问过抽奖页面和健康测评，跳过处理...");
            return MqAction.CommitMessage;
        }

        // 3、如果未访问过抽奖，则发送抽奖消息
        if (!lotteryFlag) {
            WxMpKefuMessage message = new WxMpKefuMessage();
            message.setMsgType(WxConsts.KefuMsgType.NEWS);
            message.setToUser(gzhOpenId);
            WxMpKefuMessage.WxArticle wxArticle = new WxMpKefuMessage.WxArticle();
            wxArticle.setTitle("@" + (Objects.nonNull(hmcUser) ? hmcUser.getNickName() : "亲爱的岭粉") + "，您有1次免费抽奖机会待领取，点击领取");
            wxArticle.setDescription("每天抽⼤奖，好运伴相随，健康转出来");
            wxArticle.setUrl(drawActivityUrl);
            wxArticle.setPicUrl(gzhSubscribeLotteryImgUrl);
            message.setArticles(Collections.singletonList(wxArticle));
            try {
                String pushResult = wxMpService.getKefuService().sendKefuMessageWithResponse(message);
                log.info("发送抽奖消息结果pushResult：{}, 参数：{}, token:{}", pushResult, JSONUtil.toJsonStr(message), wxMpService.getAccessToken());

            } catch (WxErrorException e) {
                log.warn("发送抽奖消息失败：{}", e.getMessage(), e);
            }
        }

        // 4、如果未访问过健康测评，则发送健康测评消息
        if (!evaluateFlag) {
            WxMpKefuMessage message = new WxMpKefuMessage();
            message.setMsgType(WxConsts.KefuMsgType.NEWS);
            message.setToUser(gzhOpenId);
            WxMpKefuMessage.WxArticle wxArticle = new WxMpKefuMessage.WxArticle();
            wxArticle.setTitle("@" + (Objects.nonNull(hmcUser) ? hmcUser.getNickName() : "亲爱的岭粉") + "，您有⼀份健康评估结果，点击领取");
            wxArticle.setDescription("健康⾃测，健康⻛险先掌握");
            wxArticle.setUrl(h5DomainProperties.getDomainUrl() + "/active/healthAssessment/");
            wxArticle.setPicUrl(gzhSubscribeHealthEvaluateImgUrl);
            message.setArticles(Collections.singletonList(wxArticle));
            try {
                String pushResult = wxMpService.getKefuService().sendKefuMessageWithResponse(message);
                log.info("发送健康测评消息结果pushResult：{}, 参数：{}, token:{}", pushResult, JSONUtil.toJsonStr(message), wxMpService.getAccessToken());

            } catch (WxErrorException e) {
                log.warn("发送健康测评消息失败：{}", e.getMessage(), e);
            }
        }

        return MqAction.CommitMessage;
    }

    @Override
    protected int getMaxReconsumeTimes() {
        return 3;
    }

    @Override
    protected IConsumeFailureHandler getConsumeFailureHandler() {
        return (body, message, context, e) -> {
            mqMessageConsumeFailureApi.handleConsumeFailure(body, message, e);
        };
    }
}
