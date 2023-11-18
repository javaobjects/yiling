package com.yiling.hmc.wechat.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

import com.yiling.framework.common.pojo.bo.TerminalInfo;

/**
 * 微信公众号登录入参
 *
 * @Description
 * @Author fan.shen
 * @Date 2022/3/23
 */
@Data
@ToString
@ApiModel(value = "WxMpLoginForm", description = "微信公众号登录入参")
@Slf4j
public class WxMpLoginForm implements Serializable {

    private static final long serialVersionUID = -7722430332896313642L;

    @NotEmpty
    @ApiModelProperty(value = "临时登录凭证code", required = true)
    private String code;

    @ApiModelProperty(value = "终端设备信息")
    private TerminalInfo terminalInfo;

    @ApiModelProperty(value = "C端活动id")
    private Long activityId;

    @ApiModelProperty(value = "店员或销售id、医生id")
    private Long doctorId;

    @ApiModelProperty(value = "C端活动来源 1-患教活动，2-医带患，3-八子补肾")
    private Integer activitySource;


}
