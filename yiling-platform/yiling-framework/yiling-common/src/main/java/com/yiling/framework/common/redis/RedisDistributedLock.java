package com.yiling.framework.common.redis;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.exception.ServiceException;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;


/**
 * description:分布式锁实现类 <br>
 * date: 2020/4/16 13:02 <br>
 * author: fei.wu <br>
 */
@Component
public class RedisDistributedLock {

    private static final Logger logger = LoggerFactory.getLogger(RedisDistributedLock.class);

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final String LOCK_OK              = "OK";
    private static final String SET_IF_NOT_EXIST     = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";

    /**
     * 获得锁
     *
     * @param lockName       锁name
     * @param acquireTimeout 获取锁的超时时间(如果获得锁失败,会在这个时间内重试获得锁)
     * @param timeout        锁的超时时间
     * @return 成功返回lockId，失败返回null
     */
    @Deprecated
    public String lock(String lockName, int acquireTimeout, int timeout, TimeUnit unit) throws InterruptedException {
        String lockKey = RedisKey.generate("redis", "lock", lockName);
        String lockId = UUID.randomUUID().toString();
        long end = System.currentTimeMillis() + unit.toMillis(acquireTimeout);
        while (System.currentTimeMillis() < end) {
            boolean result = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, lockId, timeout, unit);
            if (result) {
                return lockId;
            }
            TimeUnit.MILLISECONDS.sleep(10);
        }

        logger.info("获得锁超时: " + (end - System.currentTimeMillis()));
        return null;
    }

    /**
     * 获取锁方法（v2.0）
     *
     * @param lockName 锁name
     * @param acquireTimeout 获取锁的超时时间(如果获得锁失败,会在这个时间内重试获得锁)
     * @param timeout 锁的超时时间
     * @param unit 超时时间单位
     * @return java.lang.String
     * @author xuan.zhou
     * @date 2022/7/6
     **/
    public String lock2(String lockName, int acquireTimeout, int timeout, TimeUnit unit) {
        String lockKey = RedisKey.generate("redis", "lock", lockName);
        String lockId = UUID.randomUUID().toString();
        long end = System.currentTimeMillis() + unit.toMillis(acquireTimeout);
        while (System.currentTimeMillis() < end) {
            boolean result = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, lockId, timeout, unit);
            if (result) {
                return lockId;
            }
            ThreadUtil.sleep(10);
        }

        logger.warn("获取锁超时：lockName={}, acquireTimeout={}, timeout={}, unit={}", lockName, acquireTimeout, timeout, unit.toString());
        throw new BusinessException(ResultCode.FAILED, "系统繁忙，请稍后再试");
    }

    /**
     * 释放锁
     *
     * @param lockName 锁name
     * @param lockId   锁id
     * @return true/false
     */
    public boolean releaseLock(String lockName, String lockId) {
        if (StrUtil.isBlank(lockName) || lockId == null) {
            return false;
        }
        boolean releaseFlag = false;

        String lockKey = RedisKey.generate("redis", "lock", lockName);

        long end = System.currentTimeMillis() + 1000 * 60;

        while (true) {
            try {
                // 判断是不是可以释放锁
                if (lockId.equals(stringRedisTemplate.opsForValue().get(lockKey))) {
                    boolean del = stringRedisTemplate.delete(lockKey);
                    if (del) {
                        TimeUnit.MILLISECONDS.sleep(200);
                        continue;
                    }
                    releaseFlag = true;
                }
                break;
            } catch (Exception e) {
                //删除锁失败或者
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e1) {
                    Thread.currentThread().interrupt();
                }
                //长时间抛这个异常，需要人工介入。。
                logger.error("[RedisDistributedLock][releaseLock] 异常！请人工检查对应锁业务,锁参数: " + lockName + "\n" + e.getMessage(), e);
                //如果一分钟以内没有重试成功则退出
                if (System.currentTimeMillis() > end) {
                    break;
                }
            }
        }
        return releaseFlag;
    }

}
