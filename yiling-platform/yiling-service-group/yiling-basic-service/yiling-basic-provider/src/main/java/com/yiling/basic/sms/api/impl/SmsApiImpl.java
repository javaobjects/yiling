package com.yiling.basic.sms.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.basic.sms.api.SmsApi;
import com.yiling.basic.sms.enums.SmsSignatureEnum;
import com.yiling.basic.sms.enums.SmsTypeEnum;
import com.yiling.basic.sms.enums.SmsVerifyCodeTypeEnum;
import com.yiling.basic.sms.service.SmsService;

/**
 * 短信 API 实现
 *
 * @author: xuan.zhou
 * @date: 2021/6/7
 */
@DubboService
public class SmsApiImpl implements SmsApi {

    @Autowired
    SmsService smsService;

    @Override
    public boolean send(String mobile, String content, SmsTypeEnum typeEnum) {
        return smsService.send(mobile, content, typeEnum);
    }

    @Override
    public boolean send(String mobile, String content, SmsTypeEnum typeEnum, SmsSignatureEnum signatureEnum) {
        return smsService.send(mobile, content, typeEnum, signatureEnum);
    }

    @Override
    public boolean sendVerifyCode(String mobile, SmsVerifyCodeTypeEnum verifyCodeTypeEnum) {
        return smsService.sendVerifyCode(mobile, verifyCodeTypeEnum);
    }

    @Override
    public boolean checkVerifyCode(String mobile, String verifyCode, SmsVerifyCodeTypeEnum verifyCodeTypeEnum) {
        return smsService.checkVerifyCode(mobile, verifyCode, verifyCodeTypeEnum);
    }

    @Override
    public boolean cleanVerifyCode(String mobile, SmsVerifyCodeTypeEnum verifyCodeTypeEnum) {
        return smsService.cleanVerifyCode(mobile, verifyCodeTypeEnum);
    }

    @Override
    public String getVerifyCodeToken(String mobile, String verifyCode, SmsVerifyCodeTypeEnum verifyCodeTypeEnum) {
        return smsService.getVerifyCodeToken(mobile, verifyCode, verifyCodeTypeEnum);
    }

    @Override
    public boolean checkVerifyCodeToken(String mobile, String token, SmsVerifyCodeTypeEnum verifyCodeTypeEnum) {
        return smsService.checkVerifyCodeToken(mobile, token, verifyCodeTypeEnum);
    }

    @Override
    public boolean cleanVerifyCodeToken(String mobile, SmsVerifyCodeTypeEnum verifyCodeTypeEnum) {
        return smsService.cleanVerifyCodeToken(mobile, verifyCodeTypeEnum);
    }
}
