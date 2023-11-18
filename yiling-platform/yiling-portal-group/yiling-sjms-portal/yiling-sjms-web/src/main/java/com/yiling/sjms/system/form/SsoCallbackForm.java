package com.yiling.sjms.system.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * SSO 回调接口参数
 *
 * @author: xuan.zhou
 * @date: 2022/11/23
 */
@Data
public class SsoCallbackForm {

    @NotEmpty
    @ApiModelProperty(value = "员工工号", required = true)
    private String userCode;

    @NotNull
    @ApiModelProperty(value = "时间戳", required = true)
    private Long timestamp;

    @NotEmpty
    @ApiModelProperty(value = "数据包签名", required = true)
    private String md5;

    @ApiModelProperty(value = "重定向URL。如果为空，则重定向到系统首页")
    private String redirectUrl;

    @ApiModelProperty(value = "类型：1-pc 2-app，默认为1")
    private Integer type = 1;
}
