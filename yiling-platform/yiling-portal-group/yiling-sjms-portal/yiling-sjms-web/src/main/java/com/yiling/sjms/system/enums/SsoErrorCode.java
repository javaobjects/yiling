package com.yiling.sjms.system.enums;

import com.yiling.framework.common.enums.IErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * SSO错误信息
 *
 * @author: xuan.zhou
 * @date: 2022/11/23
 */
@Getter
@AllArgsConstructor
public enum SsoErrorCode implements IErrorCode {

    SIGN_ERROR(100101, "参数签名错误"),
    REQUEST_EXPIRED(100102, "请求已过期"),
    ESB_EMPLOYEE_NOT_EXIST(100103, "未找到对应的ESB人员信息，请于管理员联系"),
    // 异常
    EXCEPTION(100200, "发生异常，请于管理员联系"),
    ;

    private Integer code;
    private String message;
}
