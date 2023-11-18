package com.yiling.basic.sms.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 短信验证码类型枚举
 *
 * @author: xuan.zhou
 * @date: 2021/6/8
 */
@Getter
@AllArgsConstructor
public enum SmsVerifyCodeTypeEnum {

    // POP-PC
    LOGIN("login", "登录验证码", "登录验证码：{}", SmsSignatureEnum.YILING_PHARMACEUTICAL),
    RESET_PASSWORD("reset_password", "重置密码短信验证码", "重置密码短信验证码：{}", SmsSignatureEnum.YILING_PHARMACEUTICAL),
    VERIFY_ORIGINAL_MOBILE_NUMBER("verify_original_mobile_number", "验证原手机号短信验证码", "验证原手机号短信验证码：{}", SmsSignatureEnum.YILING_PHARMACEUTICAL),
    CHANGE_MOBILE_NUMBER("change_mobile_number", "更换手机号短信验证", "更换手机号短信验证码：{}", SmsSignatureEnum.YILING_PHARMACEUTICAL),

    // B2B-PC
    B2B_PC_LOGIN("b2b_pc_login", "登录验证码", "登录验证码：{}", SmsSignatureEnum.YILING_PHARMACEUTICAL),

    // B2B-APP
    B2B_LOGIN("b2b_login", "登录验证码", "登录验证码：{}", SmsSignatureEnum.YILING_PHARMACEUTICAL),
    B2B_RESET_PASSWORD("b2b_reset_password", "重置密码短信验证码", "重置密码短信验证码：{}", SmsSignatureEnum.YILING_PHARMACEUTICAL),
    B2B_VERIFY_ORIGINAL_MOBILE_NUMBER("b2b_verify_original_mobile_number", "验证原手机号短信验证码", "验证原手机号短信验证码：{}", SmsSignatureEnum.YILING_PHARMACEUTICAL),
    B2B_CHANGE_MOBILE_NUMBER("b2b_change_mobile_number", "更换手机号短信验证", "更换手机号短信验证码：{}", SmsSignatureEnum.YILING_PHARMACEUTICAL),
    B2B_REGIST("b2b_regist", "企业入驻注册手机号短信验证码", "企业入驻注册手机号短信验证码：{}", SmsSignatureEnum.YILING_PHARMACEUTICAL),
    B2B_CHANGE_SPECIAL_MOBILE_NUMBER("b2b_change_special_mobile_number", "更换特殊号码手机号短信验证", "更换特殊号码手机号短信验证码：{}", SmsSignatureEnum.YILING_PHARMACEUTICAL),
    B2B_DEREGISTER_ACCOUNT("b2b_deregister_account", "注销账号短信验证码", "注销账号短信验证码：{}", SmsSignatureEnum.YILING_PHARMACEUTICAL),

    // 销售助手模块
    SALES_ASSISTANT_LOGIN("sales_assistant_login", "销售助手登录验证码", "登录验证码{}，用于验证您的手机，如非本人操作，可致电客服", SmsSignatureEnum.YILING_PHARMACEUTICAL),
    SALES_ASSISTANT_RESET_PASSWORD("sales_assistant_reset_password", "销售助手重置密码短信验证码", "尊敬的用户，您正在进行忘记密码操作，请输入验证码{}，如非本人操作，可致电客服", SmsSignatureEnum.YILING_PHARMACEUTICAL),
    SALES_ASSISTANT_CHANGE_MOBILE("sales_assistant_change_mobile", "销售助手换绑手机号短信验证码", "尊敬的用户，您正在进行换绑手机号操作，请输入验证码{}，如非本人操作，可致电客服", SmsSignatureEnum.YILING_PHARMACEUTICAL),
    SALES_ASSISTANT_ACCEPT_INVITE("sales_assistant_accept_invite", "销售助手接受团队邀请短信验证码", "尊敬的用户，您正在进行接受团队邀请操作，请输入验证码{}，如非本人操作，可致电客服", SmsSignatureEnum.YILING_PHARMACEUTICAL),
    SALES_ASSISTANT_ORDER_CONFIRM("sales_assistant_order_confirm", "销售助手确认订单验证码", "确认订单验证码：{}，用于验证您的手机号，如非被人操作，可致电客服", SmsSignatureEnum.YILING_PHARMACEUTICAL),

    // HMC 健康管理中心
    HMC_ACTIVITY_PATIENT_EDUCATE("hmc_activity_patient_educate", "添加就诊人短信验证码", "您的验证码为：{}，5分钟内有效，请及时使用，过期您将重新获取", SmsSignatureEnum.YILING_HEALTH),

    // 医药代表APP
    MR_LOGIN("mr_login", "医药代表APP登录验证码", "登录验证码{}，用于验证您的手机，如非本人操作，可致电客服", SmsSignatureEnum.YILING_PHARMACEUTICAL),
    MR_RESET_PASSWORD("mr_reset_password", "医药代表APP重置密码短信验证码", "尊敬的用户，您正在进行忘记密码操作，请输入验证码{}，如非本人操作，可致电客服", SmsSignatureEnum.YILING_PHARMACEUTICAL),
    MR_CHANGE_MOBILE("mr_change_mobile", "医药代表APP换绑手机号短信验证码", "尊敬的用户，您正在进行换绑手机号操作，请输入验证码{}，如非本人操作，可致电客服", SmsSignatureEnum.YILING_PHARMACEUTICAL),
    ;

    private String code;
    private String name;
    private String templateContent;
    private SmsSignatureEnum signatureEnum;

    public static SmsVerifyCodeTypeEnum getByCode(String code) {
        for (SmsVerifyCodeTypeEnum e : SmsVerifyCodeTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
