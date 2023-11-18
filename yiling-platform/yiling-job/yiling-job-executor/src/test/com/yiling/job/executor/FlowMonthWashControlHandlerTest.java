package com.yiling.job.executor;

import com.yiling.job.BaseTest;
import com.yiling.job.executor.service.jobhandler.AgencyBackupJobHandler;
import com.yiling.job.executor.service.jobhandler.FlowMonthWashControlHandler;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: shuang.zhang
 * @date: 2023/3/10
 */
public class FlowMonthWashControlHandlerTest extends BaseTest {

    @Autowired
    private FlowMonthWashControlHandler  flowMonthWashControlHandler;

    @Autowired
    private AgencyBackupJobHandler agencyBackupJobHandler;

    @Test
    public void test() throws Exception {
        flowMonthWashControlHandler.flowMonthWashControlJobHandler("");
    }

    @Test
    public void test1() throws Exception {
//        agencyBackupJobHandler.AgencyBackupJobHandler("");
    }

    @Test
    public void test2() throws Exception {
        flowMonthWashControlHandler.washSumNotifyJobHandler("");
    }

}
