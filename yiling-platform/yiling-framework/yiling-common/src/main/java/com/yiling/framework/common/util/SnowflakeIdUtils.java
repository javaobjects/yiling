package com.yiling.framework.common.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2023/6/29
 */
@Slf4j
public class SnowflakeIdUtils {

    private static long workerId = 0;
    private static long datacenterId = 1;
    private final static Snowflake snowflake = IdUtil.getSnowflake(workerId, datacenterId);

    static {
        try {
            workerId = NetUtil.ipv4ToLong(NetUtil.getLocalhostStr());
            log.info("当前机器的workerId:{}", workerId);
        } catch (Exception e) {
            log.warn("当前机器的workerId获取失败" + e);
            workerId = NetUtil.getLocalhostStr().hashCode();
            log.info("当前机器workerId:{}", workerId);
        }
    }

    public static synchronized long snowflakeId() {
        return snowflake.nextId();
    }

    public static void main(String[] args) throws Exception {
        long workerId = 0;
        try {
            workerId = NetUtil.ipv4ToLong(NetUtil.getLocalhostStr());
            System.out.println("当前机器的workerId:{}" + workerId);

            workerId = NetUtil.getLocalhostStr().hashCode();
            System.out.println("当前机器 workId:{}" + workerId);
        } catch (Exception e) {
            System.out.println("当前机器的workerId获取失败" + e);

        }

        System.out.println(snowflakeId());
        Thread.sleep(3000L);
        System.out.println(snowflakeId());
        Thread.sleep(3000L);
        System.out.println(snowflakeId());
    }

}
