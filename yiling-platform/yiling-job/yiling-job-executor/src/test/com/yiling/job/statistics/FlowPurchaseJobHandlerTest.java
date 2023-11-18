package com.yiling.job.statistics;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.job.BaseTest;
import com.yiling.job.executor.service.jobhandler.FlowPurchaseJobHandler;

/**
 * @author: houjie.sun
 * @date: 2022/9/21
 */
public class FlowPurchaseJobHandlerTest extends BaseTest {

    @Autowired
    private FlowPurchaseJobHandler flowPurchaseJobHandler;

    @Test
    public void flowPurchaseCheckJobHandlerTest() {
        try {
            flowPurchaseJobHandler.flowPurchaseCheckJobHandler(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void flowPurchaseCheckTaskHandlerTest() {
        try {
            flowPurchaseJobHandler.flowPurchaseCheckTaskHandler(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
