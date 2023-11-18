package com.yiling.framework.common.pojo;

import org.slf4j.MDC;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yiling.framework.common.enums.IErrorCode;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.util.Constants;

import cn.hutool.core.text.StrFormatter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * REST API 返回结果
 *
 * @author xuan.zhou
 * @date 2019/10/15
 */
@Data
@ApiModel(description = "接口统一返回类")
public class Result<T> implements java.io.Serializable {

    @ApiModelProperty("返回码")
    private Integer code;
    @ApiModelProperty("返回信息")
    private String  message;
    @ApiModelProperty("返回数据")
    private T       data;
    @ApiModelProperty("日志跟踪标识")
    private String traceId;

    protected Result() {
    }

    protected Result(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    protected Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Result<T> success() {
        return new Result(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage());
    }

    public static <T> Result<T> success(T data) {
        return new Result(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    public static <T> Result<T> failed(String message) {
        return new Result(ResultCode.FAILED.getCode(), message);
    }

    public static <T> Result<T> failed(IErrorCode errorCode) {
        return new Result(errorCode.getCode(), errorCode.getMessage());
    }

    public static <T> Result<T> failed(Integer code, String message) {
        return new Result(code, message);
    }

    public static <T> Result<T> failed(Integer code, String message, T data) {
        return new Result(code, message, data);
    }

    /**
     * 参数验证失败返回结果
     */
    public static <T> Result<T> validateFailed() {
        return failed(ResultCode.PARAM_VALID_ERROR);
    }

    /**
     * 参数验证失败返回结果
     * @param message 提示信息
     */
    public static <T> Result<T> validateFailed(String message) {
        return new Result(ResultCode.PARAM_VALID_ERROR.getCode(), message, null);
    }

    /**
     * 参数验证失败返回结果
     * @param message 提示信息
     */
    public static <T> Result<T> validateFailed(String fieldName, String message) {
        return new Result(ResultCode.PARAM_VALID_ERROR.getCode(), StrFormatter.format("[{}]{}", fieldName, message), null);
    }

    /**
     * 未登录返回结果
     */
    public static <T> Result<T> unauthorized() {
        return new Result(ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getMessage(), null);
    }

    /**
     * 未登录返回结果
     */
    public static <T> Result<T> unauthorized(String message) {
        return new Result(ResultCode.UNAUTHORIZED.getCode(), message, null);
    }

    /**
     * 未授权返回结果
     */
    public static <T> Result<T> forbidden() {
        return new Result(ResultCode.FORBIDDEN.getCode(), ResultCode.FORBIDDEN.getMessage());
    }

    public String getTraceId() {
        return MDC.get(Constants.TRACE_ID);
    }

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    public boolean isSuccessful() {
        return ResultCode.SUCCESS.getCode().equals(this.getCode());
    }
}
