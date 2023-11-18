package com.yiling.dataflow.wash;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.BaseTest;
import com.yiling.dataflow.report.api.impl.FlowWashSaleReportApiImpl;
import com.yiling.dataflow.report.dto.FlowWashSaleReportDTO;
import com.yiling.dataflow.report.dto.request.CreateFlowWashReportRequest;
import com.yiling.dataflow.report.dto.request.FlowWashSaleReportPageRequest;
import com.yiling.dataflow.report.handler.FlowWashPharmacyPurchaseReportHandler;
import com.yiling.dataflow.report.service.FlowWashSaleReportService;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhigang.guo
 * @date: 2023/3/13
 */
@Slf4j
public class FlowWashReportTest extends BaseTest {

    @Autowired
    FlowWashPharmacyPurchaseReportHandler handler;
    @Autowired
    FlowWashSaleReportApiImpl flowWashSaleReportApi;
    @Autowired
    FlowWashSaleReportService saleReportService;


    @Test
    public void testFlowWashSupplyStockReportHandlerCreate() {

        CreateFlowWashReportRequest reportRequest = new CreateFlowWashReportRequest();
        reportRequest.setCrmId(4482l);
        reportRequest.setYear(2023);
        reportRequest.setMonth(4);
        reportRequest.setReportType(CreateFlowWashReportRequest.ReportTypeEnum.HOSPITAL);

        handler.generator(reportRequest);

    }



    @Test
    public void testPageList() {
        FlowWashSaleReportPageRequest request = new FlowWashSaleReportPageRequest();
        request.setYear("2023");
        request.setMonth("4");
        request.setCurrent(5000);
        request.setSize(10);

        Page<FlowWashSaleReportDTO> reportDTOPage = saleReportService.pageList(request);

        System.out.println(JSONUtil.toJsonStr(reportDTOPage));

    }
}
