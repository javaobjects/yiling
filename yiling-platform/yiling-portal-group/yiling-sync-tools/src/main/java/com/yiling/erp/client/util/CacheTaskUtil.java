package com.yiling.erp.client.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: shuang.zhang
 * @date: 2022/3/7
 */
public class CacheTaskUtil {
    private static CacheTaskUtil cacheTaskUtil;
    private static Map<String, Integer> cacheMap;

    private CacheTaskUtil() {
        cacheMap = new ConcurrentHashMap<>();
    }

    public static CacheTaskUtil getInstance() {
        if (cacheTaskUtil == null) {
            cacheTaskUtil = new CacheTaskUtil();
        }
        return cacheTaskUtil;
    }

    /**
     * 添加缓存
     *
     * @param key
     */
    public void addCacheData(String key) {
        cacheMap.put(key, 1);
    }

    /**
     * 取出缓存
     *
     * @param key
     * @return
     */
    public Integer getCacheData(String key) {
        if (cacheMap.get(key) == null) {
            return 0;
        }
        return cacheMap.get(key);
    }

    /**
     * 清楚缓存
     *
     * @param key
     */
    public void removeCacheData(String key) {
        cacheMap.put(key, 0);
    }

    /**
     * 获取正在运行的任务标号
     * @return
     */
    public String getExecuting(){
        StringBuffer sb=new StringBuffer();
        for(Map.Entry<String,Integer> entry:cacheMap.entrySet()){
            if(entry.getValue()==1){
                sb.append(entry.getKey()).append(",");
            }
        }
        return sb.toString();
    }
}
