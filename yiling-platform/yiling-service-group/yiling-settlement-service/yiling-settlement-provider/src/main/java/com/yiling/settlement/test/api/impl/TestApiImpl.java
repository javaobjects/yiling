package com.yiling.settlement.test.api.impl;

import org.apache.dubbo.config.annotation.DubboService;

import com.yiling.settlement.test.api.TestApi;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: xuan.zhou
 * @date: 2021/10/13
 */
@Slf4j
@DubboService
public class TestApiImpl implements TestApi {

    @Override
    public void test() {
        log.info("This is a test.");
    }
}
