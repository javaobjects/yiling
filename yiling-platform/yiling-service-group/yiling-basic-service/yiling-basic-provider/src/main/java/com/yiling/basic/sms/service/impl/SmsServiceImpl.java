package com.yiling.basic.sms.service.impl;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.basic.mq.service.MqMessageSendService;
import com.yiling.basic.sms.config.SmsConfig;
import com.yiling.basic.sms.entity.SmsRecordDO;
import com.yiling.basic.sms.enums.SmsSignatureEnum;
import com.yiling.basic.sms.enums.SmsStatusEnum;
import com.yiling.basic.sms.enums.SmsTypeEnum;
import com.yiling.basic.sms.enums.SmsVerifyCodeTypeEnum;
import com.yiling.basic.sms.service.SmsRecordService;
import com.yiling.basic.sms.service.SmsService;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.redis.service.RedisService;
import com.yiling.framework.common.util.Constants;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 短信 Service 实现
 *
 * @author: xuan.zhou
 * @date: 2021/6/8
 */
@Slf4j
@Service
public class SmsServiceImpl implements SmsService {

    @Lazy
    @Autowired
    SmsServiceImpl _this;
    
    @Autowired
    SmsConfig               smsConfig;
    @Autowired
    SmsRecordService        smsRecordService;
    @Autowired
    MqMessageSendService    mqMessageSendService;
    @Autowired
    RedisService            redisService;

    @Transactional(rollbackFor = Exception.class)
    public MqMessageBO sendPrepare(String mobile, String content, SmsTypeEnum typeEnum, SmsSignatureEnum signatureEnum) {
        SmsRecordDO entity = new SmsRecordDO();
        entity.setMobile(mobile);
        entity.setContent(content);
        entity.setType(typeEnum.getCode());
        entity.setSignature(signatureEnum.getName());
        entity.setStatus(SmsStatusEnum.UNSENT.getCode());
        smsRecordService.save(entity);

        MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_SMS_CREATED, "", entity.getId().toString());
        mqMessageBO = mqMessageSendService.prepare(mqMessageBO);

        return mqMessageBO;
    }

    @Override
    public boolean send(String mobile, String content, SmsTypeEnum typeEnum) {
        return _this.send(mobile, content, typeEnum, SmsSignatureEnum.YILING_PHARMACEUTICAL);
    }

    @Override
    public boolean send(String mobile, String content, SmsTypeEnum typeEnum, SmsSignatureEnum signatureEnum) {
        MqMessageBO mqMessageBO = _this.sendPrepare(mobile, content, typeEnum, signatureEnum);

        mqMessageSendService.send(mqMessageBO);

        return true;
    }

    @Override
    public boolean sendVerifyCode(String mobile, SmsVerifyCodeTypeEnum verifyCodeTypeEnum) {
        String verifyCode = smsConfig.getVerifyCodeDefault();
        if (smsConfig.isVerifyCodeGeneraterEnabled()) {
            verifyCode = RandomUtil.randomNumbers(6);
        }

        String key = this.genVerifyCodeCacheKey(mobile, verifyCodeTypeEnum);
        if(Objects.nonNull(redisService.get(key))){
            return true;
        }

        // 检查是否触发风控
        Date now = new Date();
        String hashKey = null;
        if (smsConfig.isRiskControlEnabled()) {
            String lockKey = RedisKey.generate("sms", "verifyCode", verifyCodeTypeEnum.getCode(), "riskControlLock", mobile);
            if (Objects.nonNull(redisService.get(lockKey))) {
                throw new BusinessException(ResultCode.FAILED, "连续获取短信验证码已超过5次，请5分钟后再试~");
            }
            hashKey = this.checkRiskControl(mobile, verifyCodeTypeEnum, lockKey, now);
        }

        String content = StrFormatter.format(verifyCodeTypeEnum.getTemplateContent(), verifyCode);
        this.send(mobile, content, SmsTypeEnum.VERIFY_CODE, verifyCodeTypeEnum.getSignatureEnum());

        redisService.set(key, verifyCode, smsConfig.getVerifyCodeExpirationTime());

        // 风控机制
        if (smsConfig.isRiskControlEnabled()) {
            redisService.hSet(hashKey, DateUtil.format(now, "yyyy-MM-dd HH:mm:ss"), verifyCode, smsConfig.getVerifyCodeRiskControlCheckTime());
        }

        return true;
    }

    private String checkRiskControl(String mobile, SmsVerifyCodeTypeEnum verifyCodeTypeEnum, String lockKey, Date now) {
        String hashKey = RedisKey.generate("sms", "verifyCode", verifyCodeTypeEnum.getCode(), "riskControlCheck", mobile);
        Map<Object, Object> map = redisService.hGetAll(hashKey);
        if (CollUtil.isNotEmpty(map)) {
            List<String> list = ListUtil.toList();
            for (Object date : map.keySet()) {
                String time = (String) date;
                if (DateUtil.offsetSecond(DateUtil.parseDateTime(time), smsConfig.getVerifyCodeRiskControlCheckTime()).compareTo(now) > 0) {
                    list.add(time);
                }
            }
            if (list.size() >= 5) {
                // 5分钟之内达到5次，触发风控，等待5分钟才能发短信
                redisService.set(lockKey, DateUtil.format(now, "yyyy-MM-dd HH:mm:ss"), smsConfig.getVerifyCodeRiskControlLockTime());

                throw new BusinessException(ResultCode.FAILED, "连续获取短信验证码已超过5次，请5分钟后再试~");
            }
        }

        return hashKey;
    }

    @Override
    public boolean checkVerifyCode(String mobile, String verifyCode, SmsVerifyCodeTypeEnum verifyCodeTypeEnum) {
        String key = this.genVerifyCodeCacheKey(mobile, verifyCodeTypeEnum);
        if (redisService.hasKey(key)) {
            String value = (String) redisService.get(key);
            return verifyCode.equals(value);
        }
        return false;
    }

    @Override
    public boolean cleanVerifyCode(String mobile, SmsVerifyCodeTypeEnum verifyCodeTypeEnum) {
        String key = this.genVerifyCodeCacheKey(mobile, verifyCodeTypeEnum);
        if (redisService.hasKey(key)) {
            return redisService.del(key);
        }
        return true;
    }

    @Override
    public String getVerifyCodeToken(String mobile, String verifyCode, SmsVerifyCodeTypeEnum verifyCodeTypeEnum) {
        boolean result = this.checkVerifyCode(mobile, verifyCode, verifyCodeTypeEnum);
        if (!result) {
            return null;
        }

        String key = this.genVerifyCodeTokenCacheKey(mobile, verifyCodeTypeEnum);
        if (redisService.hasKey(key)) {
            return (String) redisService.get(key);
        }

        String token = IdUtil.fastSimpleUUID();
        redisService.set(key, token, smsConfig.getVerifyCodeTokenExpirationTime());

        return token;
    }

    @Override
    public boolean checkVerifyCodeToken(String mobile, String token, SmsVerifyCodeTypeEnum verifyCodeTypeEnum) {
        String key = this.genVerifyCodeTokenCacheKey(mobile, verifyCodeTypeEnum);
        if (redisService.hasKey(key)) {
            String value = (String) redisService.get(key);
            return token.equals(value);
        }
        return false;
    }

    @Override
    public boolean cleanVerifyCodeToken(String mobile, SmsVerifyCodeTypeEnum verifyCodeTypeEnum) {
        String key = this.genVerifyCodeTokenCacheKey(mobile, verifyCodeTypeEnum);
        if (redisService.hasKey(key)) {
            return redisService.del(key);
        }
        return true;
    }

    private String genVerifyCodeCacheKey(String mobile, SmsVerifyCodeTypeEnum verifyCodeTypeEnum) {
        return RedisKey.generate("sms", "verifyCode", verifyCodeTypeEnum.getCode(), mobile);
    }

    private String genVerifyCodeTokenCacheKey(String mobile, SmsVerifyCodeTypeEnum verifyCodeTypeEnum) {
        return RedisKey.generate("sms", "verifyCode", "token", verifyCodeTypeEnum.getCode(), mobile);
    }
}
