package com.yiling.dataflow.order;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.dataflow.BaseTest;
import com.yiling.dataflow.order.service.FlowGoodsBatchService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: houjie.sun
 * @date: 2022/6/22
 */
@Slf4j
public class FlowGoodsBatchServiceTest extends BaseTest {

    @Autowired
    private FlowGoodsBatchService flowGoodsBatchService;

    @Test
    public void statisticsFlowGoodsBatchTotalNumberTest() {
        flowGoodsBatchService.statisticsFlowGoodsBatchTotalNumber();
    }

}
