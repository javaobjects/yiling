package com.yiling.basic.captcha.enums;

import com.yiling.framework.common.enums.IErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: fei.wu <br>
 * @date: 2021/6/2 <br>
 */
@Getter
@AllArgsConstructor
public enum CaptchaResultCode implements IErrorCode {

    CAPTCHA_NOT_EMPTY(1080, "验证码不能为空");

    private final Integer code;
    private final String message;
}
