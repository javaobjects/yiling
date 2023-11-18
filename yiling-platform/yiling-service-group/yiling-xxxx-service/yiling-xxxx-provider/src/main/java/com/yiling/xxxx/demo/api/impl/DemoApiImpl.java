package com.yiling.xxxx.demo.api.impl;

import org.apache.dubbo.config.annotation.DubboService;

import com.yiling.xxxx.demo.api.DemoApi;

/**
 * DEMO API
 *
 * @author xuan.zhou
 * @date 2022/5/12
 */
@DubboService
public class DemoApiImpl implements DemoApi {

    @Override
    public String echo(String name) {
        return "Hello " + name;
    }
}
