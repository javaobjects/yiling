package com.yiling.basic.no.service.impl;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yiling.basic.no.enums.INoEnum;
import com.yiling.basic.no.service.NoService;
import com.yiling.framework.common.redis.RedisDistributedLock;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.redis.service.RedisService;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;

/**
 * 生成业务单号 Service 实现
 * 
 * @author: xuan.zhou
 * @date: 2021/6/25
 */
@Service
public class NoServiceImpl implements NoService {

    public static final String DATE_FORMAT = "yyyyMMddHHmmss";

    @Autowired
    RedisService redisService;
    @Autowired
    RedisDistributedLock redisDistributedLock;

    @Override
    public String gen(INoEnum noEnum) {
        String randomStr = RandomUtil.randomNumbers(noEnum.getRandomNum());

        StringBuilder businessNo = new StringBuilder();
        businessNo.append(noEnum.getPrefix());
        switch(noEnum.getMiddelPartMode()){
            case DATESTR:
                String dateStr = DateUtil.format(new Date(), DATE_FORMAT);
                businessNo.append(dateStr).append(randomStr).toString();
                break;
            case RANDOM:
                businessNo.append(randomStr).toString();
                break;
        }

        String lockName = RedisKey.generate("NO", noEnum.name());
        String lockId = "";
        try {
            lockId = redisDistributedLock.lock2(lockName, 3, 3, TimeUnit.SECONDS);
            // 校验单号是否重复
            String key = RedisKey.generate("NO", noEnum.name(), businessNo.toString());
            if (redisService.hasKey(key)) {
                return this.gen(noEnum);
            } else {
                redisService.set(key, "1", 5);
                return businessNo.toString();
            }
        } finally {
            redisDistributedLock.releaseLock(lockName, lockId);
        }
    }
}
