package com.yiling.dataflow;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.dataflow.order.service.FlowSettlementEnterpriseTagService;

/**
 * @author: houjie.sun
 * @date: 2022/5/24
 */
public class FlowSettlementEnterpriseTagTest extends BaseTest  {

    @Autowired
    private FlowSettlementEnterpriseTagService flowSettlementEnterpriseTagService;

    @Test
    public void getFlowSettlementEnterpriseTagNameListTest() {
        List<Long> eidList = flowSettlementEnterpriseTagService.getFlowSettlementEnterpriseTagNameList();
        System.out.println(">>>>> enterpriseTagNameList:" + eidList.toString());
    }


}
