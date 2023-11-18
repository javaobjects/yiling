package com.yiling.open.erp.validation.entity;

import java.io.Serializable;
import java.util.Map;

import cn.hutool.core.util.StrUtil;

/**
 * @Description:
 * @Author: qi.xiong
 * @Date: 2019/5/8
 */
public class ValidationResult implements Serializable {
    private static final long serialVersionUID = 7030351887721569637L;

    private boolean error;
    private Map<String, String> errorMap;

    public boolean hasError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public Map<String, String> getErrorMap() {
        return errorMap;
    }

    public void setErrorMap(Map<String, String> errorMap) {
        this.errorMap = errorMap;
    }

    public String getErrorString() {
        if (errorMap == null || errorMap.size() == 0) {
            return StrUtil.EMPTY;
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : errorMap.entrySet()) {
            sb.append(entry.getKey() + entry.getValue()).append(",");
        }
        return sb.substring(0, sb.length() - 1);
    }
}
