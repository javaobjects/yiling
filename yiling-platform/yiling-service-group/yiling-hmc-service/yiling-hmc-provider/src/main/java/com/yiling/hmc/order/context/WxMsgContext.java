package com.yiling.hmc.order.context;

import lombok.Data;

/**
 * 微信消息上下文
 */
@Data
public class WxMsgContext {

    /**
     * redisKey
     */
    private String key;

    /**
     * 页面路径
     */
    private String page;

    /**
     * scene值
     */
    private String scene;


}
