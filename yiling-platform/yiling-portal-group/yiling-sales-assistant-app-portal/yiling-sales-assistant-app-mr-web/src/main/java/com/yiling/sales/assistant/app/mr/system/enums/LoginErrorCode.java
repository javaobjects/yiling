package com.yiling.sales.assistant.app.mr.system.enums;

import com.yiling.framework.common.enums.IErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 登录错误信息
 *
 * @author: xuan.zhou
 * @date: 2022/12/5
 */
@Getter
@AllArgsConstructor
public enum LoginErrorCode implements IErrorCode {

    // 该手机号没有绑定的医药代表，请联系企业管理员处理
    ACCOUNT_NOT_EXIST(100100, "手机号不存在"),
    ACCOUNT_DISABLED(100101, "您的账号已被停用，如有疑问请联系客服"),
    PASSWORD_ERROR(100102, "您填写的手机号或密码有误，请重新填写"),
    ENTERPRISE_EMPTY(100103, "您的账号尚未加入企业，暂时无法登录"),
    ENTERPRISE_UNAUDITED(100104, "您所在的企业认证通过之后，可登录"),
    ENTERPRISE_DISABLED(100105, "您所在的企业已停用，暂不能使用"),
    ENTERPRISE_EMPLOYEE_DISABLED(100106, "您的员工信息已被停用，如有疑问请于企业管理员联系"),
    TERMAINAL_USER(100107, "终端类型的企业账号，暂时无法登录"),
    ENTERPRISE_EMPLOYEE_IS_NOT_MR(100108, "您不是医药代表，暂时无法登录"),


    ACCOUNT_HAD_LOGOUT(100110, "您的账号已注销"),
    ACCOUNT_DEREGISTERING(100111,"账号注销中，如有特殊情况请联系平台客服处理。"),
    ACCOUNT_AUDITING(100112, "您的账号已申请注册，我们将在24小时内审核您的资料，请耐心等待"),

    VERIFY_CODE_ERROR(100200, "您填写的验证码错误，请重新填写"),
    ;

    private Integer code;
    private String message;
}
