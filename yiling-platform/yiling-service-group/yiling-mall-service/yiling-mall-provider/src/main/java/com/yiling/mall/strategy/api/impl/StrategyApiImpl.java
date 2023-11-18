package com.yiling.mall.strategy.api.impl;

import java.util.Date;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.mall.strategy.api.StrategyApi;
import com.yiling.mall.strategy.handler.StrategyEndAutoJobHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: yong.zhang
 * @date: 2022/9/22
 */
@Slf4j
@DubboService
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StrategyApiImpl implements StrategyApi {

    private final StrategyEndAutoJobHandler strategyEndAutoJobHandler;

    @Override
    public void strategyEndAutoJobHandler(Date startTime, Date stopTime) {
        strategyEndAutoJobHandler.strategyEndAutoJobHandler(startTime, stopTime);
    }
}
