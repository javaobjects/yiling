package com.yiling.sales.assistant.app.system.form;

import com.yiling.framework.common.pojo.bo.TerminalInfo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 基础登录 Form
 *
 * @author: xuan.zhou
 * @date: 2021/11/26
 */
@Data
public class BaseLoginForm {

    @ApiModelProperty(value = "终端设备信息")
    private TerminalInfo terminalInfo;
}
