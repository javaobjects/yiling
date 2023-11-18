package com.yiling.job.executor;

import com.yiling.framework.common.util.DubboUtils;
import com.yiling.job.BaseTest;
import com.yiling.open.third.api.FlowInterfaceConfigApi;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;

/**
 * @author: shuang.zhang
 * @date: 2022/8/8
 */
public class ErpInterfaceFlowJobHandlerTest extends BaseTest {

    @DubboReference(async = true,timeout = 1000*60*60)
    private FlowInterfaceConfigApi flowInterfaceConfigApi;

    @Test
    public void executeFlowInterfaceControl() {
        flowInterfaceConfigApi.executeFlowInterface();
        DubboUtils.quickAsyncCall("flowInterfaceConfigApi","executeFlowInterface");
    }
}
