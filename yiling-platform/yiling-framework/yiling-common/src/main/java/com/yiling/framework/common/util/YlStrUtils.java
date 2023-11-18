package com.yiling.framework.common.util;

import cn.hutool.core.util.StrUtil;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 字符串工具类
 *
 * @Description
 * @Author fan.shen
 * @Date 2022/4/18
 */
public class YlStrUtils {

    /**
     * 构建员工编号
     *
     * @param userId
     * @return
     */
    public static String buildSellerUserNo(Long userId) {
        if (Objects.isNull(userId) || Long.valueOf(0).equals(userId)) {
            return null;
        }
        if (String.valueOf(userId).length() >= 6) {
            return String.valueOf(userId);
        }
        String userNo = "";
        int length = 6 - String.valueOf(userId).length();
        for (int i = 0; i < length; i++) {
            userNo += 0;
        }
        return userNo + userId;
    }


    /**
     * 处理参数，转成map
     * eg :
     * 店员二维码：so:2_sEId:220_sUId:13
     * 药盒二维码：so:3
     * 患教活动码：activityId:11_doctorId:22
     *
     * @param param
     * @return
     */
    public static Map<String, String> dealParam(String param) {
        HashMap<String, String> result = new HashMap<>();
        if (StrUtil.isBlank(param)) {
            return result;
        }
        String[] keyValuePairs = param.split("_");
        for (int i = 0; i < keyValuePairs.length; i++) {
            String[] item = keyValuePairs[i].split(":");
            if (item.length == 2) {
                result.put(item[0], item[1]);
            }
        }
        return result;
    }

    /**
     * 处理参数，转成map
     * eg :key1=value1&key2=value2
     *
     * @param param
     * @return
     */
    public static Map<String, String> parseParam(String param) {
        HashMap<String, String> result = new HashMap<>();
        if (StrUtil.isBlank(param)) {
            return result;
        }
        String[] keyValuePairs = param.split("&");
        for (int i = 0; i < keyValuePairs.length; i++) {
            String[] item = keyValuePairs[i].split("=");
            if (item.length == 2) {
                result.put(item[0], item[1]);
            }
        }
        return result;
    }
}
