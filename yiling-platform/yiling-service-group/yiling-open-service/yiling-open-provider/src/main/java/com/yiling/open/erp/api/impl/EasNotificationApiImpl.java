package com.yiling.open.erp.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.open.erp.api.EasNotificationApi;
import com.yiling.open.erp.handler.EasNotificationHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuang.zhang
 * @date: 2021/12/7
 */
@DubboService
@Slf4j
public class EasNotificationApiImpl implements EasNotificationApi {

    @Autowired
    private EasNotificationHandler easNotificationHandler;

    @Override
    public boolean executeEas(String msg) {
        try {
            return easNotificationHandler.executeEas(msg);
        } catch (Exception e) {
            log.error("远程执行eas通知接口错误", e);
        }
        return false;
    }
}
