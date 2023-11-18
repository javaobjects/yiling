package com.yiling.hmc.insurance.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * 泰康返回参数对象
 * @param <T>
 */
@Data
public class TkResult<T> implements Serializable {

    private static final long serialVersionUID = 1905775611728626568L;

    /**
     * 返回的业务code
     */
    private String code;

    /**
     * 返回提示语
     */
    private String message;

    /**
     * success代表调用成功
     */
    private String result;

    /**
     * 返回时间
     */
    private String timestamp;

    /**
     * 业务对象
     */
    private T data;

}
