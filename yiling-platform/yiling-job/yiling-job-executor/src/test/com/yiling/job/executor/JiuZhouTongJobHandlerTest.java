package com.yiling.job.executor;

import com.yiling.framework.common.util.DubboUtils;
import com.yiling.job.BaseTest;
import com.yiling.job.executor.service.jobhandler.JiuZhouTongJobHandler;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: shuang.zhang
 * @date: 2023/3/30
 */
public class JiuZhouTongJobHandlerTest extends BaseTest {

    @Autowired
    private JiuZhouTongJobHandler jiuZhouTongJobHandler;

    @Test
    public void executeFlowInterfaceControl() throws Exception {
        jiuZhouTongJobHandler.pushPurchaseOrderHandleer("");
    }
}
