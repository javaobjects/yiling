package com.yiling.activity.web.wx.context;

import com.yiling.activity.web.wx.enums.WxQrCodeTypeEnum;
import lombok.Data;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;

/**
 * 微信处理消息上下文
 */
@Data
public class WxMsgHandlerContext {

    /**
     * 微信客服消息
     */
    WxMpKefuMessage message;

    /**
     * 活动id
     */
    private Long activityId;

    /**
     * 分享人用户id
     */
    private Long shareUserId;

    /**
     * 推送消息
     */
    private String pushMsg;

    /**
     * 场景值
     */
    private String sceneValue;

    /**
     * openId
     */
    private String openId;

    /**
     * 微信二维码类型
     */
    private WxQrCodeTypeEnum wxQrCodeTypeEnum;
}
