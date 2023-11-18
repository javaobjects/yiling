package com.yiling.activity.web.wx.handler;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.yiling.activity.web.wx.enums.WordEnum;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.basic.wx.api.GzhUserApi;
import com.yiling.basic.wx.dto.GzhUserDTO;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.redis.service.RedisService;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.rocketmq.enums.MqDelayLevel;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * 微信文本消息处理器
 */
@Service
@Slf4j
public class WxTextMsgHandler {

    // 兔年大吉
    @Value("${rabbit_year_happy}")
    private String rabbitYearHappy;

    /**
     * 公众号服务类
     */
    @Autowired
    WxMpService wxMpService;

    @Autowired
    RedisService redisService;

    @DubboReference
    GzhUserApi gzhUserApi;

    @DubboReference
    MqMessageSendApi mqMessageSendApi;


    /**
     * 处理文本消息
     *
     * @param msg
     */
    public void handleTextMsg(WxMpXmlMessage msg) {
        try {
            // 文本消息处理器容器
            Map<WordEnum, Function<WxMpXmlMessage, Boolean>> textHandlerMap = new HashMap<>();
            textHandlerMap.put(WordEnum.RABBIT_YEAR_HAPPY, this::rabbitYearHappyHandler);

            WordEnum wordEnum = getMsgType(msg);
            if (Objects.isNull(wordEnum)) {
                log.info("根据场景二维码值未匹配到对应词条枚举，跳过处理");
                return;
            }
            textHandlerMap.get(wordEnum).apply(msg);
        } catch (Exception e) {
            log.error("发送客服消息失败：{}", e.getMessage(), e);
        }

    }

    /**
     * 设置key到redis
     *
     * @param msg
     */
    public void msgToRedis(WxMpXmlMessage msg) {

        GzhUserDTO gzhUserDTO = gzhUserApi.getByGzhOpenId(msg.getFromUser());

        if (Objects.isNull(gzhUserDTO)) {
            log.info("未获取到公众号用户，跳过处理....");
            return;
        }

        // long betweenMinute = DateUtil.between(DateUtil.date(), gzhUserDTO.getUpdateTime(), DateUnit.MINUTE);
        // if (betweenMinute < 3) {
        //     log.info("相差时间:{}",betweenMinute);
        //     log.info("当前时间和关注时间相差小于3分钟，跳过处理,当前时间:{},关注更新时间：{}....", DateUtil.date(), JSONUtil.toJsonStr(gzhUserDTO));
        //     return;
        // }

        // 如果当前时间和关注时间相差小于48小时 -> 跳过处理
        long betweenHour = DateUtil.between(DateUtil.date(), gzhUserDTO.getUpdateTime(), DateUnit.HOUR);
        if (betweenHour < 48) {
            log.info("相差时间:{}", betweenHour);
            log.info("当前时间和关注时间相差小于48小时，跳过处理,当前时间:{},关注更新时间：{}....", DateUtil.date(), JSONUtil.toJsonStr(gzhUserDTO));
            return;
        }


        String key15Day = RedisKey.generate("hmc", "gzh_15day", msg.getFromUser());
        if (redisService.hasKey(key15Day)) {
            log.info("未过上一次公众号互动15d有效期，跳过本次互动redis埋点....");
            return;
        }

        // 发送公众号延迟消息
        String TOPIC_HMC_GZH_INTERACT_DELAY = "topic_hmc_gzh_interact_delay";
        String TAG_HMC_GZH_INTERACT_DELAY = "tag_hmc_gzh_interact_delay";
        MqMessageBO gzhSubscribeDelay = new MqMessageBO(TOPIC_HMC_GZH_INTERACT_DELAY, TAG_HMC_GZH_INTERACT_DELAY, gzhUserDTO.getGzhOpenId(), MqDelayLevel.TEN_SECONDS);
        MqMessageBO prepare = mqMessageSendApi.prepare(gzhSubscribeDelay);
        mqMessageSendApi.send(prepare);
        log.info("互动之后发送延迟mq openId:{}", msg.getFromUser());

        // 互动之后设置redis key，20s之后发送提醒
        // String key = RedisKey.generate("hmc", "gzh_interact", msg.getFromUser());
        // redisService.set(key, msg.getFromUser(), 20);

        // log.info("互动之后设置redis key:{},value：{}", key, msg.getFromUser());
    }

    /**
     * 获取文本消息对应枚举类
     *
     * @param msg
     * @return
     */
    private WordEnum getMsgType(WxMpXmlMessage msg) {
        if (WordEnum.RABBIT_YEAR_HAPPY.getName().equals(msg.getContent())) {
            return WordEnum.RABBIT_YEAR_HAPPY;
        }
        return null;
    }

    /**
     * 处理[兔年大吉]消息
     *
     * @param msg
     */
    private Boolean rabbitYearHappyHandler(WxMpXmlMessage msg) {
        try {
            String content = "新年到，红包启，以岭药业2023限定微信红包封面，正在火热派送！\r\n \r\n";
            content += "\uD83D\uDC49 戳这里 <a href ='" + rabbitYearHappy + "'>「玩游戏抢兔年大吉限量版红包封面」</a>祝您年年岁岁常康健，平安喜乐，兔跃乾坤福满堂！";

            WxMpKefuMessage message = new WxMpKefuMessage();
            message.setToUser(msg.getFromUser());
            message.setMsgType(WxConsts.KefuMsgType.TEXT);
            message.setContent(content);

            log.info("发送客服消息参数：{}", JSONUtil.toJsonStr(message));
            String pushResult = wxMpService.getKefuService().sendKefuMessageWithResponse(message);
            log.info("发送客服消息结果：{}, ", pushResult);
        } catch (WxErrorException e) {
            log.error("发送客服消息失败：{}", e.getMessage(), e);
        }

        return true;
    }
}
