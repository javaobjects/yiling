package com.yiling.hmc.wechat.dto;

import java.io.Serializable;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2022/4/26
 */
@Data
@Builder
public class WxMsgDTO implements Serializable {

    /**
     * 接收者openid
     */
    private String touser;

    /**
     * 模板ID
     */
    private String template_id;

    /**
     * 模板跳转链接（海外帐号没有跳转能力）
     */
    private String url;

    /**
     * 模板数据
     */
    private Map<String, WxMssData> data;

    /**
     * 小程序
     */
    private MiniProgram miniprogram;
}
