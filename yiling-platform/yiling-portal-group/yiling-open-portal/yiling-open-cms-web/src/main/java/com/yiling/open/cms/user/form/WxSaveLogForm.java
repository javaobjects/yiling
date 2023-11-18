package com.yiling.open.cms.user.form;

import com.yiling.framework.common.pojo.bo.TerminalInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 保存登录日志入参
 *
 * @Description
 * @Author fan.shen
 * @Date 2022/8/26
 */
@Data
@ToString
@ApiModel(value = "WxSaveLogForm", description = "保存登录日志")
@Slf4j
public class WxSaveLogForm implements Serializable {

    private static final long serialVersionUID = -7722430332896313642L;

    @ApiModelProperty(value = "用户id")
    @NotNull
    private Long userId;

    @ApiModelProperty(value = "小程序id")
    @NotBlank
    private String appId;

    @ApiModelProperty(value = "终端设备信息")
    private TerminalInfo terminalInfo;


}
