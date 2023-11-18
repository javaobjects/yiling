package com.yiling.dataflow.flow;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.yiling.dataflow.BaseTest;
import com.yiling.dataflow.flow.bo.FlowEnterpriseConnectStatisticBO;
import com.yiling.dataflow.flow.dto.request.SaveFlowEnterpriseConnectMonitorRequest;
import com.yiling.dataflow.flow.service.FlowEnterpriseConnectMonitorService;

/**
 * @author shichen
 * @类名 FlowEnterpriseConnectMonitorTest
 * @描述
 * @创建时间 2023/3/27
 * @修改人 shichen
 * @修改时间 2023/3/27
 **/
public class FlowEnterpriseConnectMonitorTest  extends BaseTest {

    @Autowired
    private FlowEnterpriseConnectMonitorService flowEnterpriseConnectMonitorService;

    @Test
    public void test1(){

        flowEnterpriseConnectMonitorService.findByCrmEid(1L);
    }

    @Test
    public void test2(){
        SaveFlowEnterpriseConnectMonitorRequest request = new SaveFlowEnterpriseConnectMonitorRequest();
        request.setCrmEnterpriseId(219249L);
        request.setCrmEnterpriseName("广州市振康医药有限公司");
        request.setDockingTime(new Date());
        request.setFlowCollectionTime(new Date());
        request.setInstallEmployee("张三");
        request.setSupplierLevel(1);
        request.setProvinceCode("440000");
        request.setProvinceName("广东省");
        request.setFlowMode(2);
        request.setReturnFlowDayCount(22);
        flowEnterpriseConnectMonitorService.saveOrUpdate(request);
    }

    @Test
    public void test3(){
        List<FlowEnterpriseConnectStatisticBO> list = flowEnterpriseConnectMonitorService.countConnectionStatusByProvince(1);
        System.out.println(JSON.toJSONString(list));
    }
}
