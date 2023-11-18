package com.yiling.basic.captcha.api.impl;

import javax.annotation.Resource;

import org.apache.dubbo.config.annotation.DubboService;

import com.yiling.basic.captcha.api.ImageCaptchaApi;
import com.yiling.basic.captcha.service.impl.ImageCaptchaService;

import lombok.extern.slf4j.Slf4j;

/**
 * @description: <br>
 * @date: 2020/7/7 <br>
 * @author: fei.wu <br>
 */
@Slf4j
@DubboService
public class ImageCaptchaApiImpl implements ImageCaptchaApi {

    @Resource
    ImageCaptchaService imageCaptchaService;

    @Override
    public String cacheCaptcha(String captcha, String captchaToken) {
        log.debug("captcha={}, captchaToken={}", captcha, captchaToken);
        String s = imageCaptchaService.cacheCaptcha(captcha, captchaToken);
        return s;
    }

    @Override
    public Boolean checkCaptcha(String captcha, String captchaToken) {
        Boolean b = imageCaptchaService.checkCaptcha(captcha, captchaToken);
        return b;
    }
}
