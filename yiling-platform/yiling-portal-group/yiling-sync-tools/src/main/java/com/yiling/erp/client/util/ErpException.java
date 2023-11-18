package com.yiling.erp.client.util;


/**
 * @author shuan
 */
public class ErpException extends Exception
{

    public ErpException()
    {
    }

    public ErpException(String message) {
        super(message);
//        log.error(message);
    }

    public ErpException(String message, Throwable cause) {
        super(message, cause);
//        log.error(message, cause);
    }
}