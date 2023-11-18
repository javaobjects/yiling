package com.yiling.hmc.wechat.form;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import com.yiling.framework.common.pojo.bo.TerminalInfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * 微信会话入参
 * @Description
 * @Author fan.shen
 * @Date 2022/3/25
 */
@Data
@ToString
@ApiModel(value = "WxSessionForm", description = "微信会话参数")
public class WxSessionForm implements Serializable {

	private static final long serialVersionUID = -7722430332896313642L;

    @NotEmpty
	@ApiModelProperty(value = "临时登录凭证code ")
	private String code;

    @ApiModelProperty(value = "终端设备信息")
    private TerminalInfo terminalInfo;

}
