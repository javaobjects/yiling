package com.yiling.open.web.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 请求参数处理类
 *                       
 * @Filename: RequestParametersHandler.java
 * @Version: 1.0
 * @Author: fenghu
 * @Email: fenghu.liu@rograndec.com
 *
 */
public class RequestParametersHandler {

    private YilingHashMap protocalMustParams; //必选参数
    private YilingHashMap protocalOptParams; //可选参数
    private YilingHashMap applicationParams; //应用参数

    public YilingHashMap getProtocalMustParams() {
        return protocalMustParams;
    }

    public void setProtocalMustParams(YilingHashMap protocalMustParams) {
        this.protocalMustParams = protocalMustParams;
    }

    public YilingHashMap getProtocalOptParams() {
        return protocalOptParams;
    }

    public void setProtocalOptParams(YilingHashMap protocalOptParams) {
        this.protocalOptParams = protocalOptParams;
    }

    public YilingHashMap getApplicationParams() {
        return applicationParams;
    }

    public void setApplicationParams(YilingHashMap applicationParams) {
        this.applicationParams = applicationParams;
    }

    public Map<String, String> getAllParams() {
        Map<String, String> params = new HashMap<String, String>();
        if (protocalMustParams != null && !protocalMustParams.isEmpty()) {
            params.putAll(protocalMustParams);
        }
        if (protocalOptParams != null && !protocalOptParams.isEmpty()) {
            params.putAll(protocalOptParams);
        }
        if (applicationParams != null && !applicationParams.isEmpty()) {
            params.putAll(applicationParams);
        }
        return params;
    }

}
