package com.yiling.user.web.auth.enums;

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

    VERIFY_CODE_ERROR(100001,"验证码错误或失效，请重新填写"),
    IMAGE_CAPTCHA_ERROR(100002, "图形验证码输入错误"),

    ACCOUNT_UNREGISTERED(100101,"您的账号尚未注册，请注册后登录"),
    ACCOUNT_DISABLED(100102,"您的账号已被停用，请联系客服或企业管理员"),
    ACCOUNT_OR_PASSWORD_ERROR(100103,"您填写的手机号或密码有误，请重新填写"),
    ACCOUNT_HAD_DEREGISTER(100104,"您的账号已注销"),
    ACCOUNT_DEREGISTERING(100105,"账号注销中，如有特殊情况请联系平台客服处理。"),

    ENTERPRISE_EMPTY(100201,"您的账号尚未加入企业，暂时无法登录"),
    ENTERPRISE_DISABLED(100202, "您所在的企业已停用，暂不能使用"),
    ENTERPRISE_UNAUDITED(100203, "您所在的企业认证通过之后，可登录"),
    B2B_HOSPITAL_NOT_ALLOWED(100204, "医疗机构类型不允许登录"),

    PLEASE_LOGIN_AT_DYH_APP(100301,"请登录大运河APP进行采购"),
    PLEASE_LOGIN_AT_POP(100302, "工业、商业用户请登录POP采购"),
    ;

    private Integer code;
    private String message;
}
