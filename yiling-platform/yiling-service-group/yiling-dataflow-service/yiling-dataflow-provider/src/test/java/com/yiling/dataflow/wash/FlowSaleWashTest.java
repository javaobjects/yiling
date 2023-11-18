package com.yiling.dataflow.wash;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.dataflow.BaseTest;
import com.yiling.dataflow.report.dto.request.SumFlowWashReportRequest;
import com.yiling.dataflow.report.service.FlowWashSaleReportService;
import com.yiling.dataflow.wash.api.FlowSaleWashApi;
import com.yiling.dataflow.wash.dto.request.QueryFlowSaleWashPageRequest;
import com.yiling.dataflow.wash.enums.WashMappingStatusEnum;
import com.yiling.dataflow.wash.handler.FlowSaleWashHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2023/3/9
 */
@Slf4j
public class FlowSaleWashTest extends BaseTest {

    @Autowired
    private FlowSaleWashApi flowSaleWashApi;

    @Autowired
    private FlowSaleWashHandler flowSaleWashHandler;

    @Autowired
    FlowWashSaleReportService saleReportService;

    @Test
    public void listPageTest() {
        QueryFlowSaleWashPageRequest request = new QueryFlowSaleWashPageRequest();
        request.setFmwtId(3L);
        request.setCurrent(1);
        request.setSize(10);
        flowSaleWashApi.listPage(request);
    }

    @Test
    public void washTest() {
        flowSaleWashHandler.wash(71L);
    }


    @Test
    public void reportTest() {

        SumFlowWashReportRequest reportRequest = new SumFlowWashReportRequest();
        reportRequest.setYear("2023");
        reportRequest.setMonth("4");
        reportRequest.setCustomerCrmId(8068l);
        reportRequest.setMappingStatus(WashMappingStatusEnum.MATCH_SUCCESS.getCode());

        BigDecimal result = saleReportService.sumTotalMoney(reportRequest);


        System.out.println(result + "================");

    }

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.size());
            System.out.println("i = " + i + ", value = " + list.get(i));
            if ("1".equals(list.get(i))){
                list.addAll(i + 1, new ArrayList<>());
            }
        }
    }
}
