package com.yiling.basic.captcha.api;

/**
 * @description: <br>
 * @date: 2020/7/7 <br>
 * @author: fei.wu <br>
 */

public interface ImageCaptchaApi {

    /**
     *
     * @param captcha
     * @param captchaToken
     * @return
     */
    String cacheCaptcha(String captcha, String captchaToken);

    /**
     *
     * @param captcha
     * @param captchaToken
     * @return
     */
    Boolean checkCaptcha(String captcha, String captchaToken);
}
