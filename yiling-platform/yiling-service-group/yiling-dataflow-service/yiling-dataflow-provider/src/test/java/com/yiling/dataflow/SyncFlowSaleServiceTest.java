package com.yiling.dataflow;

import java.util.Arrays;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.flow.service.SyncFlowSaleService;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseListPageRequest;
import com.yiling.dataflow.order.entity.FlowSaleDO;
import com.yiling.dataflow.order.service.FlowSaleService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: houjie.sun
 * @date: 2022/5/24
 */
public class SyncFlowSaleServiceTest extends BaseTest  {

    @Autowired
    private SyncFlowSaleService syncFlowSaleService;

    @Test
    public void Test() {
        syncFlowSaleService.syncFlowSaleSummary();
    }

}
