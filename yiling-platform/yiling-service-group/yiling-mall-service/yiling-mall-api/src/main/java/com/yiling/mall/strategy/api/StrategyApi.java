package com.yiling.mall.strategy.api;

import java.util.Date;

/**
 * @author: yong.zhang
 * @date: 2022/9/22
 */
public interface StrategyApi {

    void strategyEndAutoJobHandler(Date startTime, Date stopTime);
}
