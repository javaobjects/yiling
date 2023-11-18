package com.yiling.sjms.gb.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.yiling.basic.no.enums.INoEnum;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.redis.service.RedisService;
import com.yiling.sjms.gb.service.NoService;
import com.yiling.user.common.enums.NoEnum;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;

/**
 * 生成团购单号 Service 实现
 * 
 * @author: wei.wang
 * @date: 2022/12/06
 */
@Service
@Primary
public class NoServiceImpl implements NoService {

    public static final String DATE_FORMAT = "yyyyMMddHHmmss";
    public static final String DATE_YEAR_MONTH_FORMAT = "yyyyMM";
    public static final String DATE_YEAR_FORMAT = "yyyy";

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

    @Override
    public String genNo(INoEnum noEnum) {
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
            case DATE_YYYY_MM:
                String dateStrYearMonth = DateUtil.format(new Date(), DATE_YEAR_MONTH_FORMAT);
                businessNo.append(dateStrYearMonth).append(randomStr).toString();
                break;
            case DATE_YYYY:
                String dateStrYear = DateUtil.format(new Date(), DATE_YEAR_FORMAT);
                businessNo.append(dateStrYear).append(randomStr).toString();
                break;
        }

        // 校验单号是否重复
        String key = RedisKey.generate("NO", noEnum.name(), businessNo.toString());
        if (redisService.hasKey(key)) {
            return this.genNo(noEnum);
        } else {
            redisService.set(key, "1", 5);
            return businessNo.toString();
        }
    }
}
