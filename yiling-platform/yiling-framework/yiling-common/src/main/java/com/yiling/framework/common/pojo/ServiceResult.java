package com.yiling.framework.common.pojo;

import com.yiling.framework.common.enums.IErrorCode;
import com.yiling.framework.common.enums.ResultCode;

import lombok.Data;

/**
 * SOA服务返回结果
 *
 * @author: xuan.zhou
 * @date: 2021/5/25
 */
@Data
public class ServiceResult<T> implements java.io.Serializable {

    /**
     * 返回码
     */
    private Integer code;

    /**
     * 返回信息
     */
    private String  message;

    /**
     * 返回数据
     */
    private T data;

    protected ServiceResult(IErrorCode errorCode, T data) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.data = data;
    }

    protected ServiceResult(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ServiceResult<T> success() {
        return new ServiceResult(ResultCode.SUCCESS, null);
    }

    public static <T> ServiceResult<T> success(T data) {
        return new ServiceResult(ResultCode.SUCCESS, data);
    }

    public static <T> ServiceResult<T> failed(IErrorCode errorCode) {
        return new ServiceResult(errorCode, null);
    }

    public static <T> ServiceResult<T> failed(String message) {
        return new ServiceResult(ResultCode.FAILED.getCode(), message, null);
    }

    public boolean isSuccess() {
        return ResultCode.SUCCESS.getCode().equals(this.code);
    }
}
