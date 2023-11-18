package com.yiling.job.statistics;

import com.yiling.framework.common.util.DubboUtils;
import com.yiling.job.BaseTest;
import com.yiling.job.executor.service.jobhandler.FlowBiJobHandler;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: shuang.zhang
 * @date: 2022/8/8
 */
public class FlowBiJobHandlerTest extends BaseTest {

    @Autowired
    private FlowBiJobHandler flowBiJobHandler;

    @Test
    public void test1() {
        try {
            flowBiJobHandler.flowBiJobControl(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2() {
        try {
            flowBiJobHandler.flowMonthBiJobControl(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
