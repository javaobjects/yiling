package com.yiling.basic.captcha.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.basic.BaseTest;
import com.yiling.basic.captcha.service.impl.ImageCaptchaService;

/**
 * @author: xuan.zhou
 * @date: 2021/8/25
 */
public class ImageCaptchaServiceTest extends BaseTest {

    @Autowired
    ImageCaptchaService imageCaptchaService;

    @Test
    public void cacheCaptcha() {
        String str = imageCaptchaService.cacheCaptcha("123456", "abcd");
    }
}
