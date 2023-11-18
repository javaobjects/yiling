package com.yiling.framework.common.util;

import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;

/**
 * @author: fei.wu <br>
 * @date: 2021/7/12 <br>
 */

public class JsonUtil {

    /**
     * 提取 url 中的参数 格式化为 JSON
     *
     * @param url
     * @return params json
     */
    public static String url2json(String url) {
        String[] params = url.split("&");
        JSONObject obj = new JSONObject();
        for (int i = 0; i < params.length; i++) {
            String[] param = params[i].split("=");
            if (param.length >= 2) {
                String key = param[0];
                String value = param[1];
                for (int j = 2; j < param.length; j++) {
                    value += "=" + param[j];
                }
                try {
                    obj.putOpt(key, value);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return obj.toString();
    }
}
