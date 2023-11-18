package com.yiling.admin.erp.redis.service;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: qi.xiong
 * @Date: 2018/12/5
 */
public interface RedisService<T> {

    String get(String key);

    String set(String key, String value);

    String setex(String key, String value, int seconds);

    T getT(String key);

    String setT(String key, T value, int seconds);

    Long lpush(String key, String value);

    String rpop(String key);

    Object getObj(String key);

    String setObj(String key, Object value, int seconds);

    Long remove(String... keys);

    String set(String key, String value, String nxxx, String expx, long time);

    String hmset(String key, Map<String, String> hash);

    Map<String, String> hgetAll(String key);

    Long expire(String key, int seconds);

    Boolean exists(String key);

}
