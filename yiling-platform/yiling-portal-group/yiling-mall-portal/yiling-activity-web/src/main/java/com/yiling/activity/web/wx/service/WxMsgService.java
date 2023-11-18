package com.yiling.activity.web.wx.service;

import com.yiling.activity.web.wx.enums.WxTypeEnum;

/**
 * 微信消息服务
 *
 * @Author fan.shen
 * @Date 2022/7/22
 */
public interface WxMsgService {

    /**
     * 获取微信类型
     *
     * @return
     */
    WxTypeEnum getWxType();

    /**
     * 处理消息
     *
     * @param msgBody
     * @return
     */
    void doMsg(String msgBody);

}
