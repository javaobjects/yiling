package com.yiling.dataflow.gb;

import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.yiling.dataflow.BaseTest;
import com.yiling.dataflow.gb.api.GbAppealFormApi;
import com.yiling.dataflow.gb.dto.request.GbAppealFormExecuteEditDetailRequest;
import com.yiling.dataflow.gb.dto.request.SaveGbAppealFormSaleReportMatchRequest;
import com.yiling.dataflow.gb.service.GbOrderService;

import cn.hutool.core.collection.ListUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhigang.guo
 * @date: 2023/3/13
 */
@Slf4j
public class GbAppealFormTest extends BaseTest {

    @DubboReference
    GbAppealFormApi gbAppealFormApi;
    @DubboReference
    GbOrderService gbOrderService;


    @Test
    public void testFlowWashSupplyStockReportHandlerCreate() {
        String requestStr = "{\"districtCountyCode\":\"2050\",\"quantity\":-15,\"superiorSupervisorCode\":\"YX07489\",\"opUserId\":1,\"appealAllocationId\":5,\"orgId\":2051,\"businessOrgId\":10658,\"opTime\":1686209058638,\"superiorSupervisorName\":\"张志祥\",\"postName\":\"动销专员02\",\"businessProvince\":\"湖北\",\"appealFormId\":1,\"businessDepartment\":\"事业三部（呼吸）武汉一区\",\"representativeCode\":\"29409\",\"postCode\":37300,\"districtCounty\":\"零售一部武汉二区\",\"execType\":2,\"department\":\"零售一部武汉二区\",\"representativeName\":\"左滢滢\",\"provincialArea\":\"\"}";
        GbAppealFormExecuteEditDetailRequest request = JSON.parseObject(requestStr, GbAppealFormExecuteEditDetailRequest.class);
        gbAppealFormApi.editGbAppealAllocation(request);

    }

    @Test
    public void selectFlowForMatchTest() {
        SaveGbAppealFormSaleReportMatchRequest request = new SaveGbAppealFormSaleReportMatchRequest();
        request.setAppealFormId(1L);
        request.setFlowWashIdList(ListUtil.toList(17459720L, 17459721L));
        gbAppealFormApi.selectFlowForMatch(request);
    }

    @Test
    public void mateFlowTest() {
        Long gdOrderId = 940L;
        gbOrderService.mateFlow(gdOrderId);
    }

}
