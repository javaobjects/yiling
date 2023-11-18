package com.yiling.admin.erp.redis.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import com.yiling.admin.erp.redis.service.RedisService;
import com.yiling.admin.erp.redis.util.JavaSerializer;
import com.yiling.admin.erp.redis.util.JedisUtils;

import redis.clients.jedis.Jedis;

/**
 * @Description:
 * @Author: qi.xiong
 * @Date: 2018/12/5
 */
public class RedisSentinelServiceImpl<T> implements RedisService<T> {

    abstract class Executor<T> {
        public abstract T action(Jedis connection);

        public T execute() {
            T result = null;

            Jedis connection = null;
            try {
                connection = JedisUtils.getJedis();
                result = this.action(connection);
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }

            return result;
        }
    }

    @Override
    public String get(final String key) {
        return new Executor<String>() {
            @Override
            public String action(Jedis connection) {
                return connection.get(key);
            }
        }.execute();
    }

    @Override
    public String set(final String key, final String value) {
        return new Executor<String>() {
            @Override
            public String action(Jedis connection) {
                return connection.set(key, value);
            }
        }.execute();
    }

    @Override
    public String setex(final String key, final String value, final int seconds) {
        return new Executor<String>() {
            @Override
            public String action(Jedis connection) {
                return connection.setex(key, seconds, value);
            }
        }.execute();
    }

    @Override
    public T getT(final String key) {
        return new Executor<T>() {
            @Override
            public T action(Jedis connection) {
                byte[] data = connection.get(key.getBytes());
                if (data == null) {
                    return null;
                }
                return (T) JavaSerializer.deserialize(data);
            }
        }.execute();
    }

    @Override
    public String setT(final String key, final T value, final int seconds) {
        return new Executor<String>() {
            @Override
            public String action(Jedis connection) {
                byte[] data = JavaSerializer.serialize(value);
                return connection.setex(key.getBytes(), seconds, data);
            }
        }.execute();
    }

    @Override
    public Long lpush(String key, String value) {
        return new Executor<Long>() {
            @Override
            public Long action(Jedis connection) {
                return connection.lpush(key, value);
            }
        }.execute();
    }

    @Override
    public String rpop(String key) {
        return new Executor<String>() {
            @Override
            public String action(Jedis connection) {
                return connection.rpop(key);
            }
        }.execute();
    }

    @Override
    public Object getObj(String key) {
        return getT(key);
    }

    @Override
    public String setObj(String key, Object value, int seconds) {
        return setT(key, (T) value, seconds);
    }

    @Override
    public Long remove(final String... keys) {
        return new Executor<Long>() {
            @Override
            public Long action(Jedis connection) {
                return connection.del(keys);
            }
        }.execute();
    }

    @Override
    public String set(final String key, final String value, final String nxxx, final String expx, final long time) {
        return new Executor<String>() {
            @Override
            public String action(Jedis connection) {
                return connection.set(key, value, nxxx, expx, time);
            }
        }.execute();
    }

    @Override
    public String hmset(final String key, final Map<String, String> hash) {
        return new Executor<String>() {
            @Override
            public String action(Jedis connection) {
                return connection.hmset(key, hash);
            }
        }.execute();
    }

    @Override
    public Map<String, String> hgetAll(final String key) {
        return new Executor<Map<String, String>>() {
            @Override
            public Map<String, String> action(Jedis connection) {
                return connection.hgetAll(key);
            }
        }.execute();
    }

    @Override
    public Long expire(final String key, final int seconds) {

        return new Executor<Long>() {
            @Override
            public Long action(Jedis connection) {
                return connection.expire(key, seconds);
            }
        }.execute();
    }

    @Override
    public Boolean exists(final String key) {
        return new Executor<Boolean>() {
            @Override
            public Boolean action(Jedis connection) {
                return connection.exists(key);
            }
        }.execute();
    }

}
