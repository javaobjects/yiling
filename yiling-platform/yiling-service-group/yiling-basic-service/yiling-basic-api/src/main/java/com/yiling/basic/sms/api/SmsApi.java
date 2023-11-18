package com.yiling.basic.sms.api;

import com.yiling.basic.sms.enums.SmsSignatureEnum;
import com.yiling.basic.sms.enums.SmsTypeEnum;
import com.yiling.basic.sms.enums.SmsVerifyCodeTypeEnum;

/**
 * 短信 API
 *
 * @author: xuan.zhou
 * @date: 2021/6/7
 */
public interface SmsApi {

    /**
     * 发送普通短信
     *
     * @param mobile 手机号
     * @param content 短信内容
     * @param typeEnum 短信类型枚举
     * @return
     */
    boolean send(String mobile, String content, SmsTypeEnum typeEnum);

    /**
     * 发送普通短信（自定义短信签名）
     *
     * @param mobile 手机号
     * @param content 短信内容
     * @param typeEnum 短信类型枚举
     * @param signatureEnum 短信签名枚举
     * @return
     */
    boolean send(String mobile, String content, SmsTypeEnum typeEnum, SmsSignatureEnum signatureEnum);

    /**
     * 发送短信验证码
     *
     * @param mobile 手机号
     * @param verifyCodeTypeEnum 验证码类型枚举
     * @return
     */
    boolean sendVerifyCode(String mobile, SmsVerifyCodeTypeEnum verifyCodeTypeEnum);

    /**
     * 校验短信验证码
     *
     * @param mobile 手机号
     * @param verifyCode 验证码
     * @param verifyCodeTypeEnum 验证码类型枚举
     * @return
     */
    boolean checkVerifyCode(String mobile, String verifyCode, SmsVerifyCodeTypeEnum verifyCodeTypeEnum);

    /**
     * 清理短信验证码
     *
     * @param mobile 手机号
     * @param verifyCodeTypeEnum 验证码类型枚举
     * @return
     */
    boolean cleanVerifyCode(String mobile, SmsVerifyCodeTypeEnum verifyCodeTypeEnum);

    /**
     * 校验短信验证码并生成token
     *
     * @param mobile 手机号
     * @param verifyCode 验证码
     * @param verifyCodeTypeEnum 验证码类型枚举
     * @return java.lang.String
     * @author xuan.zhou
     * @date 2022/3/9
     **/
    String getVerifyCodeToken(String mobile, String verifyCode, SmsVerifyCodeTypeEnum verifyCodeTypeEnum);

    /**
     * 校验短信验证码token
     *
     * @param mobile 手机号
     * @param token 验证码token
     * @param verifyCodeTypeEnum 验证码类型枚举
     * @return boolean
     * @author xuan.zhou
     * @date 2022/3/9
     **/
    boolean checkVerifyCodeToken(String mobile, String token, SmsVerifyCodeTypeEnum verifyCodeTypeEnum);

    /**
     * 清理短信验证码token
     *
     * @param mobile 手机号
     * @param verifyCodeTypeEnum 验证码类型枚举
     * @return
     */
    boolean cleanVerifyCodeToken(String mobile, SmsVerifyCodeTypeEnum verifyCodeTypeEnum);
}
