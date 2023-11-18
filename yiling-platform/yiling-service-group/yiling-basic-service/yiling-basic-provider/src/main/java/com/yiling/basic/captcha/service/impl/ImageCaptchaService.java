package com.yiling.basic.captcha.service.impl;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.yiling.basic.captcha.enums.CaptchaResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.redis.RedisKey;

import lombok.extern.slf4j.Slf4j;

/**
 * @description: <br>
 * @date: 2020/7/7 <br>
 * @author: fei.wu <br>
 */
@Slf4j
@Service
public class ImageCaptchaService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public static final String CAPTCHA_REDIS_KEY_PREFIX = "image:captcha";

    public String cacheCaptcha(String captcha, String captchaToken) {
        if (StringUtils.isBlank(captcha) || StringUtils.isBlank(captchaToken)) {
            throw new BusinessException(CaptchaResultCode.CAPTCHA_NOT_EMPTY);
        }
        String key = RedisKey.generate(CAPTCHA_REDIS_KEY_PREFIX, captchaToken);
        stringRedisTemplate.opsForValue().set(key, captcha, 5, TimeUnit.MINUTES);
        return captcha;
    }

    public Boolean checkCaptcha(String captcha, String captchaToken) {
        if (StringUtils.isBlank(captcha) || StringUtils.isBlank(captchaToken)) {
            throw new BusinessException(CaptchaResultCode.CAPTCHA_NOT_EMPTY);
        }
        String key = RedisKey.generate(CAPTCHA_REDIS_KEY_PREFIX, captchaToken);
        try {
            String s = stringRedisTemplate.opsForValue().get(key);
            log.debug("key : {} ,cache captcha : {} , input captcha : {}", key, s, captcha);
            if (null != s && captcha.equalsIgnoreCase(s)) {
                return true;
            } else {
                return false;
            }
        } finally {
            stringRedisTemplate.delete(key);
        }


    }
}
