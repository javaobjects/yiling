package com.yiling.user.web.auth.form;

import com.yiling.framework.common.pojo.bo.TerminalInfo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * APP基础登录 Form
 *
 * @author xuan.zhou
 * @date 2022/4/7
 */
@Data
public class BaseAppLoginForm {

    @ApiModelProperty(value = "终端设备信息")
    private TerminalInfo terminalInfo;
}
