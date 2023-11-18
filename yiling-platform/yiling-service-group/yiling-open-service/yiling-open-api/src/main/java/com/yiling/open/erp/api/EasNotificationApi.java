package com.yiling.open.erp.api;

/**
 * @author: shuang.zhang
 * @date: 2021/12/7
 */
public interface EasNotificationApi {

    /**
     * 远程执行eas接口
     * @param msg
     * @return
     */
    boolean executeEas(String msg);

}
