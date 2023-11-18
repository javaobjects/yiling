package com.yiling.order.order.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.redis.service.RedisService;
import com.yiling.order.order.enums.NoEnum;
import com.yiling.order.order.service.NoService;

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

    @Override
    public String gen(NoEnum noEnum) {
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

        // 校验单号是否重复
        String key = RedisKey.generate("NO", noEnum.name(), businessNo.toString());
        if (redisService.hasKey(key)) {
            return this.gen(noEnum);
        } else {
            redisService.set(key, "1", 5);
            return businessNo.toString();
        }
    }
}
