package com.yiling.sales.assistant.app.system.enums;

import com.yiling.framework.common.enums.IErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 登录错误信息
 *
 * @author: xuan.zhou
 * @date: 2022/1/19
 */
@Getter
@AllArgsConstructor
public enum LoginErrorCode implements IErrorCode {

    ACCOUNT_NOT_EXIST(100100, "手机号不存在"),
    ACCOUNT_DISABLED(100101, "您的账号已被停用，如有疑问请联系客服"),
    PASSWORD_ERROR(100102, "您填写的手机号或密码有误，请重新填写"),
    ENTERPRISE_EMPTY(100103, "您的账号尚未加入企业，暂时无法登录"),
    ENTERPRISE_UNAUDITED(100104, "您所在的企业认证通过之后，可登录"),
    ENTERPRISE_DISABLED(100105, "您所在的企业已停用，暂不能使用"),
    ENTERPRISE_NO_SALESAREA(100106, "所在企业销售区域配置之后可使用"),
    TERMAINAL_USER(100107, "请联系您的商务代表进行采购"),
    PLATFORM_NOT_OPEN(100108, "企业未开使用权限，暂不能使用"),
    PERSONAL_INFO_AUDITING(100109, "您的证件24小时之内审核通过，请耐心等待"),
    ACCOUNT_HAD_LOGOUT(100110, "您的账号已注销"),
    ACCOUNT_DEREGISTERING(100111,"账号注销中，如有特殊情况请联系平台客服处理。"),

    VERIFY_CODE_ERROR(100200, "您填写的验证码错误，请重新填写"),
    VERIFY_CODE_TOKEN_ERROR(100201, "验证码token错误，请重新获取短信验证码"),
    ;

    private Integer code;
    private String message;
}
