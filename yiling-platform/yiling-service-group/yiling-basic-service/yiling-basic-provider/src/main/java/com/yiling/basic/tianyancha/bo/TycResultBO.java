package com.yiling.basic.tianyancha.bo;

import java.io.Serializable;

import lombok.Data;

/**
 * @author shichen
 * @类名 TycResultDTO
 * @描述 天眼查结果集对象
 * @创建时间 2022/1/11
 * @修改人 shichen
 * @修改时间 2022/1/11
 **/
@Data
public class TycResultBO<T> implements Serializable {

    /**
     * 返回结果
     */
    private T result;

    /**
     * 原因
     */
    private String reason;

    /**
     * 错误代码
     */
    private Integer error_code;
}
