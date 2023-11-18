package com.yiling.framework.common.redis;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * description:限流-适用于小计数器 <br>
 * date: 2020/4/16 13:02 <br>
 * author: fei.wu <br>
 */
@Component
public class RateLimiterUtil {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 是否允许执行
     *
     * @param key   redisKey
     * @param count 执行限制次数
     * @param time  限制时间
     * @param unit  时间单位
     * @return
     */
    public boolean isAllowExecution(String key, int count, int time, TimeUnit unit) {
        key = "rate:" + key;
        long sm = unit.toMillis(time);
        long now = System.currentTimeMillis();
        Long zcard = stringRedisTemplate.opsForZSet().zCard(key);
        boolean b = zcard + 1 <= count;
        if (b) {
            stringRedisTemplate.opsForZSet().add(key, now + "", now);
        }
        stringRedisTemplate.opsForZSet().removeRangeByScore(key, 0, now - sm);
        return b;
    }

    /**
     * 由于限流工具加上了rate:前缀,避免 key 重复,所以提供单独的方法删除
     *
     * @param key
     */
    public void removeRateKey(String key) {
        stringRedisTemplate.delete("rate:" + key);
    }


}
