package com.yiling.open.web.redis.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @Author: fei.wu
 * @Email: fei.wu@rograndec.com
 * @CreateDate: 2018/4/23
 * @Version: 1.0
 */
public class RedisKey {

    public static final String SYMBOL = ":";

    /**
     * Redis key生成方法
     * @param moduleName 模块名称               eg：redis，user
     * @param desc       业务说明（不要使用中文）  eg：test.key，user.info
     * 注意：
     * 1.1 标识和描述尽量简单，单个长度不要超过 5
     * 1.2 描述标识只允许数字和小写英文字母
     * @return key
     */
    public static String generate(String moduleName, String... desc){
        if (StringUtils.isBlank(moduleName) || null == desc || desc.length == 0){
            throw new RuntimeException("Redis generate key exception, key is null");
        }
        StringBuffer key = new StringBuffer();
        key.append(moduleName);
        for (String s : desc) {
            if (StringUtils.isBlank(s)){
                throw new RuntimeException("Redis generate key exception, key is null");
            }
            key.append(SYMBOL).append(s.trim());
        }
        return key.toString();
    }

}
