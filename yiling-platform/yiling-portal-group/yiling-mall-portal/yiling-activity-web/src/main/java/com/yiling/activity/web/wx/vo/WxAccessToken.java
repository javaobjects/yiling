package com.yiling.activity.web.wx.vo;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Data;

/**
 * 微信 access_token
 *
 * @Author fan.shen
 * @Date 2022/3/26
 */
@Data
public class WxAccessToken {
    private static final long serialVersionUID = 1L;

    /**
     * token
     */
    @JsonAlias("access_token")
    private String accessToken;

    /**
     * 有效时间
     */
    @JsonAlias("expires_in")
    private Long expiresIn;
}
