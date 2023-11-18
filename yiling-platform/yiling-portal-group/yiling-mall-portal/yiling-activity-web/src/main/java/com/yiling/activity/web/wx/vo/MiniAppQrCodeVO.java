package com.yiling.activity.web.wx.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 小程序码对象
 */
@NoArgsConstructor
@Data
@ApiModel("小程序码对象")
public class MiniAppQrCodeVO {

    @ApiModelProperty(value = "图片base64")
    private String base64;

    @ApiModelProperty(value = "page")
    private String page;

    @ApiModelProperty(value = "scene")
    private String scene;

    @ApiModelProperty(value = "key")
    private String key;

    @ApiModelProperty(value = "envVersion")
    private String envVersion;
}
