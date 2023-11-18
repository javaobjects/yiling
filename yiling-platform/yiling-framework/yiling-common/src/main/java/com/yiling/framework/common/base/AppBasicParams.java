package com.yiling.framework.common.base;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * APP 请求基础参数对象
 *
 * @author: xuan.zhou
 * @date: 2020/11/24
 */
@Getter
@Setter
public class AppBasicParams implements java.io.Serializable {

    private static final long serialVersionUID = 1763572525613123786L;

    /**
     * APP_ID
     */
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private Long appId;

    /**
     * APP_CODE
     */
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private String appCode;

    /**
     * APP_KEY
     */
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private String appKey;

    /**
     * APP客户端类型：android、ios
     */
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private String appClient;

    /**
     * APP版本名称，例如：1.0.0
     */
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private String appVersion;

    /**
     * APP版本号，例如：100
     */
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private String versionCode;

    /**
     * APP渠道号
     */
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private String channelCode;

    /**
     * 请求设备IMEI
     */
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private String imei;

    /**
     * 请求设备网络类型
     */
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private String netType;

    /**
     * 站点ID
     */
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private Long siteId;

    /**
     * 请求时间（当前时间毫秒数）
     */
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private Long timestamp;
}
