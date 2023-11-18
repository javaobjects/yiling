package com.yiling.erp.client.util;



/**
 * @author shuan
 */
public class ErpRunException extends RuntimeException
{

    public ErpRunException()
    {
    }

    public ErpRunException(String message) {
        super(message);
//        log.error(message);
    }

    public ErpRunException(String message, Throwable cause) {
        super(message, cause);
//        log.error(message, cause);
    }
}