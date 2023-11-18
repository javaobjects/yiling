package com.yiling.open.erp.util;


import lombok.extern.slf4j.Slf4j;

/**
 * @author shuan
 */
@Slf4j
public class ErpRunException extends RuntimeException
{

    public ErpRunException()
    {
    }

    public ErpRunException(String message) {
        super(message);
        log.error(message);
    }

    public ErpRunException(String message, Throwable cause) {
        super(message, cause);
        log.error(message, cause);
    }
}