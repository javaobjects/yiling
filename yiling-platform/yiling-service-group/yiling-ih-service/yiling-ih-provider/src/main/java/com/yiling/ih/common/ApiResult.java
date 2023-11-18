package com.yiling.ih.common;

import com.yiling.framework.common.exception.ServiceException;

import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * API Result
 *
 * @author: xuan.zhou
 * @date: 2022/6/8
 */
@Data
@Slf4j
public class ApiResult<T> implements java.io.Serializable {

    private static final long serialVersionUID = -58332726271204187L;

    private Long code;

    private String msg;

    private T data;

    public boolean success() {
        if (this.code != null && this.code == 1) {
            return true;
        } else {
            log.error("API调用失败：code={}, msg={}, data={}", this.code, this.msg, JSONUtil.toJsonStr(this.data));
            throw new ServiceException("API调用失败：" + this.getMsg());
        }
    }
}
