package com.yiling.payment.exception;

import lombok.Getter;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.payment.exception
 * @date: 2021/10/19
 */
@Getter
public class PayException extends RuntimeException {

    private static final long serialVersionUID = -6630208990131777375L;

    private Integer code;

    public PayException( String msg) {
        super(msg);
    }

    public PayException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }
}
