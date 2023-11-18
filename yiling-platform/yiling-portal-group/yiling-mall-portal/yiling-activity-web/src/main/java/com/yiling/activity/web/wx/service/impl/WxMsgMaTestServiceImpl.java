package com.yiling.activity.web.wx.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaMessage;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMsgEvent;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.yiling.activity.web.wx.enums.WxTypeEnum;
import com.yiling.activity.web.wx.service.WxMsgService;
import com.yiling.framework.common.pojo.vo.WxConstant;
import com.yiling.hmc.remind.api.MedsRemindApi;
import com.yiling.hmc.remind.dto.request.MedsRemindSubscribeRequest;
import com.yiling.hmc.remind.enums.HmcRemindSubscribeEnum;
import com.yiling.user.system.api.HmcUserApi;
import com.yiling.user.system.bo.HmcUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 微信消息服务 - 补方测试小程序 - wx4015099f1fca8328
 *
 * @Author fan.shen
 * @Date 2022/7/22
 */
@Service
@Slf4j
public class WxMsgMaTestServiceImpl implements WxMsgService {

    @DubboReference
    MedsRemindApi medsRemindApi;

    @DubboReference
    HmcUserApi hmcUserApi;

    /**
     * 小程序服务类
     */
    @Autowired
    WxMaService wxMaService;

    @Override
    public WxTypeEnum getWxType() {
        return WxTypeEnum.HMC_MA_TEST;
    }

    @Override
    public void doMsg(String msgBody) {

        if (StrUtil.isEmpty(msgBody)) {
            log.info("消息体为空，跳过处理");
            return;
        }

        WxMaMessage msg = WxMaMessage.fromXml(msgBody);

        if (!StrUtil.equalsIgnoreCase(msg.getMsgType(), "event")) {
            log.info("当前消息非事件消息，跳过处理");
            return;
        }

        List<MedsRemindSubscribeRequest> result = null;
        switch (msg.getEvent()) {

            case WxConstant.SUBSCRIBE_MSG_POPUP_EVENT:
                result = buildSubscribe(msg);
                break;

            case WxConstant.SUBSCRIBE_MSG_CHANGE_EVENT:
                result = buildSubscribeChange(msg);
                break;

            default:
                log.info("非订阅消息事件，跳过处理");
                break;
        }

        if (CollUtil.isEmpty(result)) {
            log.info("无满足条件的结果集，跳过处理");
            return;
        }

        log.info("准备保存消息订阅：{}", JSONUtil.toJsonStr(result));

        medsRemindApi.saveOrUpdateRemindSub(result);

        log.info("处理微信服务器通知完成");

    }


    /**
     * 构建订阅请求
     *
     * @param msg
     * @return
     */
    private List<MedsRemindSubscribeRequest> buildSubscribe(WxMaMessage msg) {
        List<WxMaSubscribeMsgEvent.PopupEvent> list = msg.getSubscribeMsgPopupEvent().getList();
        if (CollUtil.isEmpty(list)) {
            return Lists.newArrayList();
        }
        List<MedsRemindSubscribeRequest> requestList = list.stream().map(item -> {

            MedsRemindSubscribeRequest request = new MedsRemindSubscribeRequest();
            request.setAppId(wxMaService.getWxMaConfig().getAppid());
            request.setOpenId(msg.getFromUser());
            request.setTemplateId(item.getTemplateId());
            request.setSubscribeStatus(HmcRemindSubscribeEnum.getByName(item.getSubscribeStatusString()).getType());

            request.setUserId(Optional.ofNullable(hmcUserApi.getByOpenId(msg.getFromUser())).map(HmcUser::getUserId).orElse(null));

            return request;
        }).collect(Collectors.toList());

        return requestList.stream().filter(item -> Objects.nonNull(item.getUserId())).collect(Collectors.toList());

    }

    /**
     * 构建订阅请求
     *
     * @param msg
     * @return
     */
    private List<MedsRemindSubscribeRequest> buildSubscribeChange(WxMaMessage msg) {
        List<WxMaSubscribeMsgEvent.ChangeEvent> list = msg.getSubscribeMsgChangeEvent().getList();
        if (CollUtil.isEmpty(list)) {
            return Lists.newArrayList();
        }
        List<MedsRemindSubscribeRequest> requestList = list.stream().map(item -> {

            MedsRemindSubscribeRequest request = new MedsRemindSubscribeRequest();
            request.setAppId(wxMaService.getWxMaConfig().getAppid());
            request.setOpenId(msg.getFromUser());
            request.setTemplateId(item.getTemplateId());
            request.setSubscribeStatus(HmcRemindSubscribeEnum.getByName(item.getSubscribeStatusString()).getType());
            request.setUserId(Optional.ofNullable(hmcUserApi.getByOpenId(msg.getFromUser())).map(HmcUser::getUserId).orElse(null));

            return request;
        }).collect(Collectors.toList());

        return requestList.stream().filter(item -> Objects.nonNull(item.getUserId())).collect(Collectors.toList());

    }

}
