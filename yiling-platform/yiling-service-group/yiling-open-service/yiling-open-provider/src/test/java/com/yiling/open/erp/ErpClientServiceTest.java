package com.yiling.open.erp;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.yiling.open.BaseTest;
import com.yiling.open.erp.api.impl.ErpClientApiImpl;
import com.yiling.open.erp.bo.SjmsFlowCollectStatisticsCountBO;
import com.yiling.open.erp.dto.ErpClientDTO;
import com.yiling.open.erp.dto.request.ErpClientQuerySjmsRequest;
import com.yiling.open.erp.dto.request.UpdateClientStatusRequest;
import com.yiling.open.erp.service.ErpClientService;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;

/**
 * @author: shuang.zhang
 * @date: 2022/2/22
 */
public class ErpClientServiceTest extends BaseTest {

    @Autowired
    private ErpClientService erpClientService;
    @Autowired
    private ErpClientApiImpl ErpClientApiImpl;

    @Test
    public void Test1() {
        List<ErpClientDTO> erpClientDOList = erpClientService.selectBySuId(1L);
        System.out.println(JSON.toJSONString(erpClientDOList));
    }

    @Test
    public void Test3() {
        ErpClientDTO erpClientDO = erpClientService.getErpClientBySuIdAndSuDeptNo(1L, "01");
        System.out.println(JSON.toJSONString(erpClientDO));
    }

    @Test
    public void Test2() {
        UpdateClientStatusRequest request = new UpdateClientStatusRequest();
        request.setSuId(15L);
        request.setClientStatus(1);
        request.setRemark("qqqqq");
        erpClientService.updateClientStatus(request);
    }

    @Test
    public void handleErpClientsNoHeartBetween24hTest() {
        ErpClientApiImpl.handleErpClientsNoHeartBetween24h();
    }

    @Test
    public void erpClientDataInitStatusUpdateTest() {
        ErpClientApiImpl.erpClientDataInitStatusUpdate();
    }


    @Test
    public void getNoDatCountByRkSuIdListAndFlowDateTest() {
        List<String> licenseNumberList = ListUtil.toList("123456");

        Date yesterday = DateUtil.yesterday();
        Date noDataDayEnd3 = DateUtil.offsetDay(DateUtil.endOfDay(yesterday), Math.negateExact(4));
        Integer noDataMoreThan3DaysCount = erpClientService.getNoDatCountByRkSuIdListAndFlowDate(licenseNumberList, noDataDayEnd3);
    }

    @Test
    public void getSjmsFlowCollectStatisticsCountTest() {
        ErpClientQuerySjmsRequest request = new ErpClientQuerySjmsRequest();
        request.setLicenseNumberList(ListUtil.toList("91420000714675659X", "914206837905867111",
                "91421000066148489T", "9142010072612386XR",
                "913700000969569700", "91420112MA4KLGHW01",
                "91500107MA5YQ1DN2N", "91330110MA2CCJE32Y",
                "91410100MA45QPFQ74", "91430105MA4PD27G9N"));
        SjmsFlowCollectStatisticsCountBO countBO = ErpClientApiImpl.getSjmsFlowCollectStatisticsCount(request);
        System.out.println("********** countBO:" + JSONUtil.toJsonStr(countBO));
    }

}

