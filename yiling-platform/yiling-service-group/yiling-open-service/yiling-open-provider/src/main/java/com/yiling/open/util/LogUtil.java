package com.yiling.open.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuang.zhang
 * @date: 2022/6/8
 */
@Slf4j
@RefreshScope
@Component
public class LogUtil {

    private static Boolean status;

    @Value("${log.out.status:true}")
    public void setStatus(Boolean status) {
        LogUtil.status = status;
    }

    public static void info(String msg) {
        if(status){
            log.info(msg);
        }
    }

    public static void info(String format, Object arg) {
        if(status){
            log.info(format,arg);
        }
    }


    public static void info(String format, Object arg1, Object arg2) {
        if(status){
            log.info(format,arg1,arg2);
        }
    }


    public static void info(String format, Object... arguments) {
        if(status){
            log.info(format,arguments);
        }
    }
}
