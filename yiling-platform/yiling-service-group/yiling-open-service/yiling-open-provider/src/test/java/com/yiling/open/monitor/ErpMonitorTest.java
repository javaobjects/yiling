package com.yiling.open.monitor;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.check.api.FlowPurchaseCheckApi;
import com.yiling.dataflow.check.dto.FlowPurchaseCheckTaskDTO;
import com.yiling.dataflow.check.dto.request.QueryFlowPurchaseCheckTaskPageRequest;
import com.yiling.open.BaseTest;
import com.yiling.open.erp.api.ErpClientApi;
import com.yiling.open.monitor.dto.request.QueryErpMonitorSaleExceptionPageRequest;
import com.yiling.open.monitor.entity.MonitorAbnormalDataDO;
import com.yiling.open.monitor.service.MonitorAbnormalDataService;

/**
 * @author: houjie.sun
 * @date: 2022/10/19
 */
public class ErpMonitorTest extends BaseTest {

    @DubboReference
    private ErpClientApi erpClientApi;
    @DubboReference
    private FlowPurchaseCheckApi flowPurchaseCheckApi;
    @Autowired
    private MonitorAbnormalDataService monitorAbnormalDataService;

    @Test
    public void getNoFlowSaleEidListTest(){
        erpClientApi.statisticsNoFlowSaleEidList();
    }

    @Test
    public void getSaleExceptionPageTest(){
        QueryErpMonitorSaleExceptionPageRequest request = new QueryErpMonitorSaleExceptionPageRequest();
        Page<MonitorAbnormalDataDO> page = monitorAbnormalDataService.page(request);
    }

    @Test
    public void getPurchaseExceptionPageTest(){
        QueryFlowPurchaseCheckTaskPageRequest request = new QueryFlowPurchaseCheckTaskPageRequest();
        Page<FlowPurchaseCheckTaskDTO> purchaseExceptionListPage = flowPurchaseCheckApi.getPurchaseExceptionListPage(request);
    }
}
