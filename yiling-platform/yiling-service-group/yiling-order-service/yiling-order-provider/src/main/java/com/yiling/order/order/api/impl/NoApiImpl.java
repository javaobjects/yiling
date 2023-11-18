package com.yiling.order.order.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.order.order.api.NoApi;
import com.yiling.order.order.enums.NoEnum;
import com.yiling.order.order.service.NoService;

/**
 * 单据号 API 实现
 *
 * @author: xuan.zhou
 * @date: 2021/6/25
 */
@DubboService
public class NoApiImpl implements NoApi {

    @Autowired
    NoService noService;

    @Override
    public String gen(NoEnum noEnum) {
        return noService.gen(noEnum);
    }
}
