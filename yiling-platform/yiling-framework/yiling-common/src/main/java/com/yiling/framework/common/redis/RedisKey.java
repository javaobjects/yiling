package com.yiling.framework.common.redis;

import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.ServiceException;

import cn.hutool.core.util.StrUtil;

/**
 * description:分布式锁实现类 <br>
 * date: 2020/4/16 13:02 <br>
 * author: fei.wu <br>
 */
public class RedisKey {

    public static final String SYMBOL = ":";
    public static final String ONE_MINUTE_PREFIX = "one_minute_prefix_";
    public static final String FIVE_MINUTE_PREFIX = "five_minute_prefix_";
    public static final String TEN_MINUTE_PREFIX = "ten_minute_prefix_";
    public static final String THIRTY_MINUTE_PREFIX = "thirth_minute_prefix_";
    public static final String ONE_HOUR_PREFIX = "one_hour_prefix_";
    public static final String FOUR_HOUR_PREFIX = "four_hour_prefix_";
    public static final String SIX_HOUR_PREFIX = "six_hour_prefix_";
    public static final String EIGHT_HOUR_PREFIX = "eight_hour_prefix_";
    public static final String TWELVE_HOUR_PREFIX = "twelve_hour_prefix_";
    public static final String EIGHTEEN_HOUR_PREFIX = "eighteen_hour_prefix_";
    public static final String ONE_DAY_PREFIX = "one_day_prefix_";

    /**
     * Redis key生成方法
     *
     * @param moduleName 模块名称               eg：redis，user
     * @param desc       业务说明（不要使用中文）  eg：test.key，user.info
     *                   注意：
     *                   1.1 标识和描述尽量简单，单个长度不要超过 5
     *                   1.2 描述标识只允许数字和小写英文字母
     * @return key
     */
    public static String generate(String moduleName, String... desc) {

        if (StrUtil.isBlank(moduleName) || null == desc || desc.length == 0) {
            throw new ServiceException(ResultCode.PARAM_MISS, "Redis generate key exception");
        }
        StringBuffer key = new StringBuffer();
        key.append(moduleName);
        for (String s : desc) {
            if (StrUtil.isBlank(s)) {
                throw new ServiceException(ResultCode.PARAM_MISS, "Redis generate key exception");
            }
            key.append(SYMBOL).append(s.trim());
        }
        return key.toString();
    }

}
