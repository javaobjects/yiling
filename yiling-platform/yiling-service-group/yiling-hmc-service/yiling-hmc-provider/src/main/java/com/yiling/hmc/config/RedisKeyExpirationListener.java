package com.yiling.hmc.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import com.yiling.basic.log.api.SysOperLogApi;
import com.yiling.basic.log.dto.SysOperLogDTO;
import com.yiling.basic.log.dto.request.QuerySysOperLogPageListRequest;
import com.yiling.basic.wx.api.GzhUserApi;
import com.yiling.basic.wx.dto.GzhUserDTO;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.redis.RedisDistributedLock;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.redis.service.RedisService;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.TraceIdUtil;
import com.yiling.hmc.remind.dto.request.MedsRemindSubscribeRequest;
import com.yiling.hmc.remind.entity.MedsRemindDO;
import com.yiling.hmc.remind.entity.MedsRemindSubscribeDO;
import com.yiling.hmc.remind.entity.MedsRemindTaskDetailDO;
import com.yiling.hmc.remind.entity.MedsRemindUserDO;
import com.yiling.hmc.remind.enums.HmcRemindSubscribeEnum;
import com.yiling.hmc.remind.enums.HmcSendStatusEnum;
import com.yiling.hmc.remind.service.MedsRemindService;
import com.yiling.hmc.remind.service.MedsRemindSubscribeService;
import com.yiling.hmc.remind.service.MedsRemindTaskDetailService;
import com.yiling.hmc.remind.service.MedsRemindUserService;
import com.yiling.user.system.api.HmcUserApi;
import com.yiling.user.system.bo.HmcUser;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * redis key 过期监听
 */
@Slf4j
@Service
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {

    @Autowired
    MedsRemindService medsRemindService;

    @Autowired
    MedsRemindTaskDetailService medsRemindTaskDetailService;

    @Autowired
    MedsRemindUserService medsRemindUserService;

    @Autowired
    MedsRemindSubscribeService subscribeService;

    @Autowired
    WxTemplateConfig templateConfig;

    @Autowired
    WxMaService wxMaService;

    @DubboReference
    HmcUserApi hmcUserApi;

    @Autowired
    protected RedisDistributedLock redisDistributedLock;

    @DubboReference
    SysOperLogApi sysOperLogApi;

    @Autowired
    RedisService redisService;

    @Autowired
    WxMpService wxMpService;

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

    @DubboReference
    private GzhUserApi gzhUserApi;


    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @MdcLog
    @Override
    public void onMessage(Message message, byte[] pattern) {

        MDC.put(Constants.TRACE_ID, TraceIdUtil.genTraceId());

        //获取过期的key
        String expireKey = message.toString();

        Map<String, Function<String, Boolean>> map = Maps.newHashMap();

        // 用药提醒
        map.put("hmc:meds_remind", this::medsRemind);

        // 订阅公众号推送消息
        // map.put("hmc:gzh_subscribe", this::gzhSubscribe);

        // 公众号互动-文本消息
        // map.put("hmc:gzh_interact", this::gzhInteract);

        map.entrySet().stream().filter(item -> expireKey.startsWith(item.getKey())).distinct().forEach(item -> item.getValue().apply(expireKey));

    }

    /**
     * 公众号互动
     *
     * @param key
     * @return
     */
    public Boolean gzhInteract(String key) {
        String lockId = null;
        String lockKey = "hmc_lock_" + key;
        try {
            lockId = redisDistributedLock.lock2(lockKey, 3, 3, TimeUnit.MILLISECONDS);
            log.info("[RedisKeyExpirationListener]准备发送公众号互动消息通知，lockKey : {},lockId:{}", lockKey, lockKey);

            String gzhOpenId = key.substring(key.lastIndexOf(":") + 1);

            GzhUserDTO gzhUserDTO = gzhUserApi.getByGzhOpenId(gzhOpenId);
            if (Objects.isNull(gzhUserDTO)) {
                log.warn("根据openId:{}未获取到用户", gzhOpenId);
                return Boolean.FALSE;
            }

            // 1、获取用户
            HmcUser hmcUser = hmcUserApi.getByOpenId(gzhOpenId);

            // 2、查询操作日志
            // 最近访问抽奖、测评标记
            boolean lotteryFlag = false;
            boolean evaluateFlag = false;
            if (Objects.nonNull(hmcUser)) {
                QuerySysOperLogPageListRequest request = new QuerySysOperLogPageListRequest();
                request.setOperId(hmcUser.getUserId());
                request.setRequestUrl("/lotteryActivity/getActivityPage");
                Page<SysOperLogDTO> lotteryPage = sysOperLogApi.queryListPage(request);
                request.setRequestUrl("/healthEvaluate/indexPage");
                Page<SysOperLogDTO> evaluatePage = sysOperLogApi.queryListPage(request);
                if (Objects.nonNull(lotteryPage) && CollUtil.isNotEmpty(lotteryPage.getRecords()) && DateUtil.compare(lotteryPage.getRecords().get(0).getOpTime(), gzhUserDTO.getUpdateTime()) > 0) {
                    lotteryFlag = true;
                }

                if (Objects.nonNull(evaluatePage) && CollUtil.isNotEmpty(evaluatePage.getRecords()) && DateUtil.compare(evaluatePage.getRecords().get(0).getOpTime(), gzhUserDTO.getUpdateTime()) > 0) {
                    evaluateFlag = true;
                }
            }

            if (lotteryFlag && evaluateFlag) {
                log.warn("用户自关注之后最近已访问过抽奖页面和健康测评，跳过处理...");
                return Boolean.TRUE;
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
                    log.error("发送抽奖消息失败：{}", e.getMessage(), e);
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
                    log.error("发送健康测评消息失败：{}", e.getMessage(), e);
                }
            }

            // 5、设置时间，保证15天之内不再推送消息
            String key15Day = RedisKey.generate("hmc", "gzh_15day", gzhOpenId);
            redisService.set(key15Day, gzhOpenId, 15 * 24 * 60 * 60);
            // redisService.set(key15Day, gzhOpenId, 10 * 60);
        } catch (Exception e) {
            log.error("[RedisKeyExpirationListener] 准备订阅公众号活动消息通知异常{}", ExceptionUtils.getStackTrace(e), e);
        } finally {
            // 释放锁
            redisDistributedLock.releaseLock(lockKey, lockId);
        }
        return Boolean.TRUE;
    }

    /**
     * 关注公众号
     *
     * @param key
     * @return
     */
    public Boolean gzhSubscribe(String key) {


        String lockId = null;
        String lockKey = "hmc_lock_" + key;
        try {
            lockId = redisDistributedLock.lock2(lockKey, 3, 3, TimeUnit.SECONDS);
            log.info("[RedisKeyExpirationListener]准备发送订阅公众号消息通知，lockKey : {},lockId:{}", lockKey, lockKey);

            String gzhOpenId = key.substring(key.lastIndexOf(":") + 1);

            GzhUserDTO gzhUserDTO = gzhUserApi.getByGzhOpenId(gzhOpenId);
            if (Objects.isNull(gzhUserDTO)) {
                log.warn("根据openId:{}未获取到用户", gzhOpenId);
                return Boolean.FALSE;
            }

            // 1、获取用户
            HmcUser hmcUser = hmcUserApi.getByOpenId(gzhOpenId);

            // 2、查询操作日志
            // 最近访问抽奖、测评标记
            boolean lotteryFlag = false;
            boolean evaluateFlag = false;
            if (Objects.nonNull(hmcUser)) {
                QuerySysOperLogPageListRequest request = new QuerySysOperLogPageListRequest();
                request.setOperId(hmcUser.getUserId());
                request.setRequestUrl("/lotteryActivity/getActivityPage");
                Page<SysOperLogDTO> lotteryPage = sysOperLogApi.queryListPage(request);
                request.setRequestUrl("/healthEvaluate/indexPage");
                Page<SysOperLogDTO> evaluatePage = sysOperLogApi.queryListPage(request);
                if (Objects.nonNull(lotteryPage) && CollUtil.isNotEmpty(lotteryPage.getRecords()) && DateUtil.compare(lotteryPage.getRecords().get(0).getOpTime(), gzhUserDTO.getUpdateTime()) > 0) {
                    lotteryFlag = true;
                }
                if (Objects.nonNull(evaluatePage) && CollUtil.isNotEmpty(evaluatePage.getRecords()) && DateUtil.compare(evaluatePage.getRecords().get(0).getOpTime(), gzhUserDTO.getUpdateTime()) > 0) {
                    evaluateFlag = true;
                }
            }

            if (lotteryFlag && evaluateFlag) {
                log.warn("用户自关注之后最近已访问过抽奖页面和健康测评，跳过处理...");
                return Boolean.TRUE;
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
                    log.error("发送抽奖消息失败：{}", e.getMessage(), e);
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
                    log.error("发送健康测评消息失败：{}", e.getMessage(), e);
                }
            }
        } catch (Exception e) {
            log.error("[RedisKeyExpirationListener] 准备订阅公众号活动消息通知异常{}", ExceptionUtils.getStackTrace(e), e);
        } finally {
            // 释放锁
            redisDistributedLock.releaseLock(lockKey, lockId);
        }

        return Boolean.TRUE;
    }

    /**
     * 用药提醒
     *
     * @param key
     * @return
     */
    public Boolean medsRemind(String key) {
        //信息打印
        log.info("[RedisKeyExpirationListener]准备发送用药提醒通知，key : {}", key);
        String lockId = null;
        String lockKey = "hmc_lock_" + key;
        try {
            lockId = redisDistributedLock.lock2(lockKey, 3, 3, TimeUnit.SECONDS);

            String value = key.substring(key.lastIndexOf(":") + 1);
            if (StrUtil.isBlank(value)) {
                log.info("未获取到key对应的值，key：{}", key);
                return Boolean.FALSE;
            }
            // 获取任务
            Long id = Long.valueOf(value);
            MedsRemindTaskDetailDO taskDetailDO = medsRemindTaskDetailService.getById(id);
            if (Objects.isNull(taskDetailDO)) {
                log.info("未获取到提醒对应的任务，key：{}", key);
                return Boolean.FALSE;
            }
            if (!HmcSendStatusEnum.WAIT.getType().equals(taskDetailDO.getSendStatus())) {
                log.info("当前任务非待发送状态，跳过处理，taskDetailDO：{}", taskDetailDO);
                return Boolean.FALSE;
            }

            // 获取订阅用户信息，如果已经取消订阅，则取消发送
            MedsRemindSubscribeDO subscribeDO = subscribeService.getByUserIdAndTemplateId(taskDetailDO.getReceiveUserId(), templateConfig.getMedsReminder());
            if (Objects.nonNull(subscribeDO) && HmcRemindSubscribeEnum.REJECT.getType().equals(subscribeDO.getSubscribeStatus())) {
                log.info("当前用户已取消订阅，跳过处理，用户id：{}，模板id：{}", taskDetailDO.getReceiveUserId(), templateConfig.getMedsReminder());
                return Boolean.FALSE;
            }

            HmcUser hmcUser = hmcUserApi.getByIdAndAppId(taskDetailDO.getReceiveUserId(), wxMaService.getWxMaConfig().getAppid());
            if (Objects.isNull(hmcUser)) {
                log.info("未获取到用户信息，跳过处理，用户id：{}", taskDetailDO.getReceiveUserId());
                return Boolean.FALSE;
            }

            // 获取所有同一时间提醒任务
            List<MedsRemindTaskDetailDO> taskList = medsRemindTaskDetailService.getAllSameTimeRemindTask(taskDetailDO);
            if (CollUtil.isEmpty(taskList)) {
                log.info("未获取到待提醒对应的任务，跳过处理，taskDetailDO：{}", taskDetailDO);
                return Boolean.FALSE;
            }


            List<Long> medsIdList = taskList.stream().map(MedsRemindTaskDetailDO::getMedsRemindId).collect(Collectors.toList());
            List<MedsRemindDO> medsRemindDOS = medsRemindService.listByIds(medsIdList);
            Map<Long, MedsRemindDO> medsRemindDOMap = medsRemindDOS.stream().collect(Collectors.toMap(MedsRemindDO::getId, o -> o, (k1, k2) -> k1));


            List<MedsRemindUserDO> remindUserList = medsRemindUserService.getAllValidateRemindByMedsIdList(taskDetailDO.getReceiveUserId(), medsIdList);
            if (CollUtil.isEmpty(remindUserList)) {
                log.info("未获取到有效的订阅用户信息，跳过处理，receiveUserId：{},medsIdList:{}", taskDetailDO.getReceiveUserId(), medsIdList);
                return Boolean.FALSE;
            }

            Map<Long, MedsRemindUserDO> medsRemindUserMap = remindUserList.stream().collect(Collectors.toMap(MedsRemindUserDO::getMedsRemindId, o -> o, (k1, k2) -> k1));
            List<MedsRemindTaskDetailDO> toSendTaskList = taskList.stream().filter(item -> medsRemindUserMap.containsKey(item.getMedsRemindId())).collect(Collectors.toList());


            // 构建消息，推送
            String goodsName = toSendTaskList.stream().map(item -> medsRemindDOMap.get(item.getMedsRemindId()).getGoodsName()).collect(Collectors.joining(","));
            if (StrUtil.length(goodsName) > 20) {
                goodsName = StrUtil.sub(goodsName, 0, 16) + "...";
            }

            String sendTime = DateUtil.format(taskDetailDO.getInitSendTime(), "yyyy年MM月dd日 HH:mm");

            try {
                WxMaSubscribeMessage msg = new WxMaSubscribeMessage();

                WxMaSubscribeMessage.MsgData time2 = new WxMaSubscribeMessage.MsgData();
                time2.setName("time2");
                time2.setValue(sendTime);

                WxMaSubscribeMessage.MsgData thing3 = new WxMaSubscribeMessage.MsgData();
                thing3.setName("thing3");
                thing3.setValue(goodsName);

                WxMaSubscribeMessage.MsgData thing4 = new WxMaSubscribeMessage.MsgData();
                thing4.setName("thing4");
                thing4.setValue("点击查看详情，标记为已用药");

                msg.setTemplateId(templateConfig.getMedsReminder());
                msg.addData(time2).addData(thing3).addData(thing4);

                msg.setToUser(hmcUser.getMiniProgramOpenId());
                msg.setMiniprogramState(templateConfig.getMiniProgramState());
                msg.setPage("pagesSub/remind/list");
                log.info("[RedisKeyExpirationListener]发送微信订阅消息:{}", JSONUtil.toJsonStr(msg));
                wxMaService.getMsgService().sendSubscribeMsg(msg);
            } catch (WxErrorException e) {
                log.error("[RedisKeyExpirationListener]推送用药提醒微信订阅消息报错：{}", ExceptionUtils.getStackTrace(e));
                // 错误代码：43101, 错误信息：用户拒绝接受消息，如果用户之前曾经订阅过，则表示用户取消了订阅关系
                if (e.getError().getErrorCode() == 43101) {
                    MedsRemindSubscribeRequest request = new MedsRemindSubscribeRequest();
                    request.setOpenId(hmcUser.getMiniProgramOpenId());
                    request.setAppId(this.wxMaService.getWxMaConfig().getAppid());
                    request.setTemplateId(templateConfig.getMedsReminder());
                    request.setSubscribeStatus(HmcRemindSubscribeEnum.REJECT.getType());
                    request.setUserId(hmcUser.getUserId());
                    log.info("[RedisKeyExpirationListener]推送消息返回43101，准备构建取消订阅记录,参数:{}", JSONUtil.toJsonStr(request));
                    this.subscribeService.saveOrUpdateRemindSub(Collections.singletonList(request));
                }
            }

            // 更新任务发送状态
            log.info("准备更新任务发送状态....");
            taskList.forEach(item -> {
                item.setSendStatus(HmcSendStatusEnum.FINISHED.getType());
                item.setActualSendTime(DateUtil.date());
                medsRemindTaskDetailService.updateById(item);
            });

            log.info("[RedisKeyExpirationListener]推送微信订阅消息完成....");
        } catch (Exception e) {
            log.error("[RedisKeyExpirationListener] 推送用药提醒异常{}", ExceptionUtils.getStackTrace(e), e);
        } finally {
            // 释放锁
            redisDistributedLock.releaseLock(lockKey, lockId);
        }

        return Boolean.TRUE;

    }

}
