package com.yiling.cms.impl;

import org.apache.dubbo.config.annotation.DubboService;

import com.yiling.cms.DemoApi;

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
