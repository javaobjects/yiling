package com.yiling.export.export;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.yiling.dataflow.agency.dto.request.QueryCrmAgencyPageListRequest;
import com.yiling.dataflow.crm.dto.request.QueryCrmEnterpriseRelationPinchRunnerPageListRequest;
import com.yiling.dataflow.flow.dto.request.FlowMonthBiTaskRequest;
import com.yiling.dataflow.gb.dto.request.QueryGbAppealFormListPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsBatchListPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseListPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowShopSaleListPageRequest;
import com.yiling.dataflow.report.dto.request.FlowWashSaleReportPageRequest;
import com.yiling.dataflow.sale.dto.request.QuerySaleDeptSubTargetDetailRequest;
import com.yiling.dataflow.wash.dto.request.QueryFlowGoodsBatchTransitPageRequest;
import com.yiling.export.BaseTest;
import com.yiling.export.excel.service.ExcelTaskRecordService;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.ExportTaskRecordService;
import com.yiling.export.export.service.impl.B2bSettlementExportServiceImpl;
import com.yiling.export.export.service.impl.B2bSettlementOrderExportServiceImpl;
import com.yiling.export.export.service.impl.B2bSettlementSimpleInfoExportServiceImpl;
import com.yiling.export.export.service.impl.CouponActivityAutoHasGetExportServiceImpl;
import com.yiling.export.export.service.impl.CouponActivityAutoHasGiveExportServiceImpl;
import com.yiling.export.export.service.impl.CouponActivityHasGiveExportServiceImpl;
import com.yiling.export.export.service.impl.CouponActivityHasUsedExportServiceImpl;
import com.yiling.export.export.service.impl.CrmEnterpriseRelationPinchRunnerServiceImpl;
import com.yiling.export.export.service.impl.CrmSupplierExportServivceImpl;
import com.yiling.export.export.service.impl.ExportB2BYilingGoodsServiceImpl;
import com.yiling.export.export.service.impl.FlowCollectEnterprisePageExportServiceImpl;
import com.yiling.export.export.service.impl.FlowGoodsBatchInTransitExportServiceImpl;
import com.yiling.export.export.service.impl.FlowGoodsBatchPageListExportServiceImpl;
import com.yiling.export.export.service.impl.FlowGoodsBatchTerminalExportServiceImpl;
import com.yiling.export.export.service.impl.FlowMonthBiTaskExportServiceImpl;
import com.yiling.export.export.service.impl.FlowPurchasePageListExportServiceImpl;
import com.yiling.export.export.service.impl.FlowSaleDayExportServiceImpl;
import com.yiling.export.export.service.impl.FlowSalePageListExportServiceImpl;
import com.yiling.export.export.service.impl.FlowShopSalePageListExportServiceImpl;
import com.yiling.export.export.service.impl.FlowWashSaleReportExportServiceImpl;
import com.yiling.export.export.service.impl.GbAppealFormServiceImpl;
import com.yiling.export.export.service.impl.GbFormListExportServiceImpl;
import com.yiling.export.export.service.impl.HmcActivityDoctorExportServiceImpl;
import com.yiling.export.export.service.impl.HmcActivityPatientExportServiceImpl;
import com.yiling.export.export.service.impl.OrderB2BCenterDetailAdminExportServiceImpl;
import com.yiling.export.export.service.impl.OrderEnterpriseSellExportServiceImpl;
import com.yiling.export.export.service.impl.OrderInvoiceExportServiceImpl;
import com.yiling.export.export.service.impl.OrderRecordReportExportServiceImpl;
import com.yiling.export.export.service.impl.PromotionGoodsGiftUsedExportServiceImpl;
import com.yiling.export.export.service.impl.SaleTargetDetailExportServiceImpl;
import com.yiling.export.imports.handler.ImportCouponActivityDataHandler;
import com.yiling.export.imports.handler.ImportFlowGoodsBatchTerminalHandler;
import com.yiling.export.imports.model.ImportFlowGoodsBatchTerminalModel;
import com.yiling.framework.oss.bo.FileInfo;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.ih.user.dto.request.QueryActivityDocPatientListRequest;
import com.yiling.ih.user.dto.request.QueryActivityDoctorListRequest;
import com.yiling.marketing.common.enums.CouponGetTypeEnum;
import com.yiling.marketing.couponactivity.api.CouponActivityApi;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityGivePageRequest;
import com.yiling.marketing.promotion.dto.request.PromotionGoodsGiftUsedRequest;
import com.yiling.open.erp.dto.request.ErpClientQuerySjmsRequest;
import com.yiling.order.order.dto.request.QueryBackOrderInfoRequest;
import com.yiling.order.order.dto.request.QueryInvoicePageRequest;
import com.yiling.order.order.dto.request.QueryOrderExportReportPageRequest;
import com.yiling.order.order.dto.request.QueryOrderPageRequest;
import com.yiling.sales.assistant.task.dto.request.QueryGoodsPageRequest;
import com.yiling.search.flow.dto.request.EsFlowSaleSearchRequest;
import com.yiling.settlement.b2b.dto.request.ExportSettlementOrderPageListRequest;
import com.yiling.settlement.b2b.dto.request.ExportSettlementPageListRequest;
import com.yiling.settlement.b2b.dto.request.ExportSettlementSimpleInfoPageListRequest;
import com.yiling.sjms.gb.dto.request.QueryGBFormListPageRequest;
import com.yiling.user.enterprise.api.EnterpriseApi;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;

/**
 * @author: shuang.zhang
 * @date: 2021/9/29
 */
public class ExprotTest extends BaseTest {

    @Autowired
    ExportTaskRecordService exportTaskRecordService;
    @Autowired
    B2bSettlementExportServiceImpl b2bSettlementExportService;
    @Autowired
    B2bSettlementOrderExportServiceImpl b2bSettlementOrderExportService;
    @Autowired
    CouponActivityHasGiveExportServiceImpl couponActivityHasGiveExportService;
    @Autowired
    CouponActivityHasUsedExportServiceImpl couponActivityHasUsedExportService;
    @Autowired
    CouponActivityAutoHasGiveExportServiceImpl couponActivityAutoHasGiveExportService;
    @Autowired
    CouponActivityAutoHasGetExportServiceImpl couponActivityAutoHasGetExportService;
    @Autowired
    OrderRecordReportExportServiceImpl orderRecordReportExportService;
    @Autowired
    OrderInvoiceExportServiceImpl orderInvoiceExportService;
    @Autowired
    PromotionGoodsGiftUsedExportServiceImpl promotionGoodsGiftUsedExportService;
    @Autowired
    OrderB2BCenterDetailAdminExportServiceImpl orderB2BCenterDetailAdminExportService;
    @Autowired
    GbFormListExportServiceImpl gbFormListExportService;
    @Autowired
    HmcActivityDoctorExportServiceImpl hmcActivityDoctorExportService;
    @Autowired
    ExportB2BYilingGoodsServiceImpl exportB2BYilingGoodsService;
    @Autowired
    ImportCouponActivityDataHandler importCouponActivityDataHandler;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    CouponActivityApi couponActivityApi;


    @Autowired
    ExcelTaskRecordService excelTaskRecordService;
    @Autowired
    FlowMonthBiTaskExportServiceImpl flowMonthBiTaskExportService;
    @Autowired
    FlowGoodsBatchPageListExportServiceImpl flowGoodsBatchPageListExportService;
    @Autowired
    FlowPurchasePageListExportServiceImpl flowPurchasePageListExportService;
    @Autowired
    FlowSalePageListExportServiceImpl flowSalePageListExportService;

    @Autowired
    CrmSupplierExportServivceImpl crmSupplierExportServivce;
    @Autowired
    HmcActivityPatientExportServiceImpl patientExportService;
    @Autowired
    FlowCollectEnterprisePageExportServiceImpl flowCollectEnterprisePageExportService;
    @Autowired
    FlowGoodsBatchInTransitExportServiceImpl flowGoodsBatchInTransitExportService;
    @Autowired
    FlowGoodsBatchTerminalExportServiceImpl flowGoodsBatchTerminalExportService;
    @Autowired
    ImportFlowGoodsBatchTerminalHandler importFlowGoodsBatchTerminalHandler;
    @Autowired
    FlowWashSaleReportExportServiceImpl flowWashSaleReportExportService;
    @Autowired
    FlowShopSalePageListExportServiceImpl FlowShopSalePageListExportService;

    @Autowired
    FlowSaleDayExportServiceImpl flowSaleDayExportService;
    @Autowired
    OrderEnterpriseSellExportServiceImpl orderEnterpriseSellExportService;
    @Autowired
    CrmEnterpriseRelationPinchRunnerServiceImpl crmEnterpriseRelationPinchRunnerService;
    @Autowired
    SaleTargetDetailExportServiceImpl saleTargetDetailExportService;
    @Autowired
    GbAppealFormServiceImpl gbAppealFormService;
    @Autowired
    B2bSettlementSimpleInfoExportServiceImpl b2bSettlementSimpleInfoExportService;

    @Autowired
    FileService fileService;

    @Test
    public void testPatientExport() {
        QueryActivityDocPatientListRequest request = new QueryActivityDocPatientListRequest();
        request.setActivityId(2);
        QueryExportDataDTO res = patientExportService.queryData(request);
        System.out.println(JSONUtil.toJsonStr(res));
    }

    @Test
    public void testPromotion() {
        PromotionGoodsGiftUsedRequest request = new PromotionGoodsGiftUsedRequest();
        request.setPromotionActivityId(265L);
        QueryExportDataDTO result = promotionGoodsGiftUsedExportService.queryData(request);
        System.out.println(result);
    }

    @Test
    public void exportAdminOrder() {
        exportTaskRecordService.syncUploadExportTask(3364L);
    }

    @Test
    public void test1() {
        long start = System.currentTimeMillis();
        exportTaskRecordService.syncUploadExportTask(1226L);
        System.out.println("导出耗时：" + (System.currentTimeMillis() - start));
    }

    @Test
    public void test2() {
        ExportSettlementPageListRequest request = new ExportSettlementPageListRequest();
        request.setCode("S20220224162724749688");
        QueryExportDataDTO dataDTO = b2bSettlementExportService.queryData(request);
        System.err.println(dataDTO);
    }

    @Test
    public void test3() {
        ExportSettlementOrderPageListRequest request = new ExportSettlementOrderPageListRequest();
        QueryExportDataDTO dataDTO = b2bSettlementOrderExportService.queryData(request);
        System.err.println(dataDTO);
    }

    @Test
    public void test4() {

        exportTaskRecordService.syncUploadExportTask(4358L);

    }

    @Test
    public void test5() {
        QueryCouponActivityGivePageRequest request = new QueryCouponActivityGivePageRequest();
        request.setCouponActivityId(92L);
        request.setGetType(CouponGetTypeEnum.GIVE.getCode());
        QueryExportDataDTO result = couponActivityHasGiveExportService.queryData(request);
        System.out.println(">>>>> result: " + JSON.toJSONString(result));
    }

    @Test
    public void test6() {
        QueryCouponActivityGivePageRequest request = new QueryCouponActivityGivePageRequest();
        request.setCouponActivityId(12L);
        QueryExportDataDTO result = couponActivityHasUsedExportService.queryData(request);
        System.out.println(">>>>> result: " + JSON.toJSONString(result));
    }

    @Test
    public void test7() {
        QueryCouponActivityGivePageRequest request = new QueryCouponActivityGivePageRequest();
        request.setCouponActivityAutoId(6L);
        request.setGetType(CouponGetTypeEnum.AUTO_GIVE.getCode());
        QueryExportDataDTO result = couponActivityAutoHasGiveExportService.queryData(request);
        System.out.println(">>>>> result: " + JSON.toJSONString(result));
    }

    @Test
    public void test8() {
        QueryCouponActivityGivePageRequest request = new QueryCouponActivityGivePageRequest();
        request.setCouponActivityAutoId(2L);
        request.setGetType(CouponGetTypeEnum.AUTO_GET.getCode());
        QueryExportDataDTO result = couponActivityAutoHasGetExportService.queryData(request);
        System.out.println(">>>>> result: " + JSON.toJSONString(result));
    }

    @Test
    public void test9() {
        QueryInvoicePageRequest request = new QueryInvoicePageRequest();
        request.setEndInvoiceTime(DateUtil.parse("2022-05-14 00:00:00"));
        request.setStartInvoiceTime(DateUtil.parse("2022-05-14 00:00:00"));
        QueryExportDataDTO queryExportDataDTO = orderInvoiceExportService.queryData(request);
        System.out.println(queryExportDataDTO);
    }

    @Test
    public void test10() {

        QueryOrderExportReportPageRequest request = new QueryOrderExportReportPageRequest();
        request.setStartCreatTime(DateUtil.parse("2019-08-13 13:33:18"));
        request.setEndCreatTime(DateUtil.parse("2022-08-13 13:33:18"));
        request.setRecordType(3);
        request.setCategoryType(1);
        QueryExportDataDTO dataDTO = orderRecordReportExportService.queryData(request);
        System.out.println(dataDTO);
    }

    @Test
    public void test11() {

        QueryBackOrderInfoRequest request = new QueryBackOrderInfoRequest();
        request.setOrderNo("D20220825143538334124");
        orderB2BCenterDetailAdminExportService.queryData(request);
    }

    @Test
    public void test12() {
        QueryActivityDoctorListRequest request = new QueryActivityDoctorListRequest();
        request.setActivityId(9);
        hmcActivityDoctorExportService.queryData(request);
    }

    @Test
    public void test13() {
        QueryGoodsPageRequest request = new QueryGoodsPageRequest();
        QueryExportDataDTO queryExportDataDTO = exportB2BYilingGoodsService.queryData(request);
        System.out.println(queryExportDataDTO.toString());
    }

    @Test
    public void test14() {
        DateTime monthDate = DateUtil.parse("2022-10", "yyyy-MM");
        DateTime queryDate = DateUtil.beginOfMonth(monthDate);
        FlowMonthBiTaskRequest request = new FlowMonthBiTaskRequest();
        request.setEid(62L);
        request.setQueryDate(queryDate);
        request.setParentFlag(2);

        byte[] flowMonth = flowMonthBiTaskExportService.getExportByte(request, "test.zip");
    }

    @Test
    public void test15() {
        long start = System.currentTimeMillis();
        excelTaskRecordService.syncImportExcelTask(39L);
        System.out.println("导出耗时：" + (System.currentTimeMillis() - start));
    }

    @Test
    public void test16() {
        QueryGBFormListPageRequest request = new QueryGBFormListPageRequest();
        QueryExportDataDTO queryExportDataDTO = gbFormListExportService.queryData(request);
        System.out.println(queryExportDataDTO);
    }

    @Test
    public void test17(){
        Map<String, Object> map = new HashMap<>();
        map.put("currentUserId", 1);
        map.put("menuSource", 1);
        map.put("enterpriseTagId", "28,29,30,27,2");
        QueryFlowGoodsBatchListPageRequest request = flowGoodsBatchPageListExportService.getParam(map);
        flowGoodsBatchPageListExportService.queryData(request);
    }

    @Test
    public void test18(){
        Map<String, Object> map = new HashMap<>();
        map.put("currentUserId", 1);
        map.put("timeType", 0);
        map.put("menuSource", 1);
        map.put("enterpriseTagId", "28,29,30,27,2");
        QueryFlowPurchaseListPageRequest request = flowPurchasePageListExportService.getParam(map);
        flowPurchasePageListExportService.queryData(request);
    }

    @Test
    public void test19(){
        Map<String, Object> map = new HashMap<>();
        map.put("currentUserId", 1);
        map.put("timeType", 0);
        map.put("menuSource", 1);
        map.put("enterpriseTagId", "28,29,30,27,2");
        QueryFlowPurchaseListPageRequest request = flowSalePageListExportService.getParam(map);
        flowSalePageListExportService.queryData(request);
    }

    @Test
    public void test20(){
        Map<String, Object> map = new HashMap<>();
        map.put("ylCode", "22222");
        QueryCrmAgencyPageListRequest request=crmSupplierExportServivce.getParam(map);
        crmSupplierExportServivce.queryData(request);
    }

    @Test
    public void test21() {
        Map<String, Object> map = new HashMap<>();
        map.put("currentUserCode", "24154");
        ErpClientQuerySjmsRequest request = flowCollectEnterprisePageExportService.getParam(map);
        flowCollectEnterprisePageExportService.queryData(request);
    }

    @Test
    public void flowGoodsBatchInTransitExportTest() {
        Map<String, Object> map = new HashMap<>();
        map.put("currentUserCode", "24154");
        map.put("crmGoodsSpecifications", "0.26g*30粒*400");
        map.put("gbDetailMonth", "2023-03");
        QueryFlowGoodsBatchTransitPageRequest request = flowGoodsBatchInTransitExportService.getParam(map);
        flowGoodsBatchInTransitExportService.queryData(request);
    }

    @Test
    public void flowGoodsBatchTerminalExportTest() {
        Map<String, Object> map = new HashMap<>();
        map.put("currentUserCode", "24154");
        map.put("crmGoodsCode", 13L);
        map.put("gbDetailMonth", "2023-03");
        QueryFlowGoodsBatchTransitPageRequest request = flowGoodsBatchTerminalExportService.getParam(map);
        flowGoodsBatchTerminalExportService.queryData(request);
    }

    @Test
    public void importFlowGoodsBatchTerminalHandlerTest() {
        ImportFlowGoodsBatchTerminalModel model = new ImportFlowGoodsBatchTerminalModel();
        model.setCrmEnterpriseId(2L);
        model.setGbName("连花清瘟片");
        model.setGbSpecifications("0.35g*24片*400盒");
//        model.setCrmGoodsCode(11L);
        model.setGbBatchNo("100121");
        model.setGbNumber(new BigDecimal("50"));

        ExcelVerifyHandlerResult excelVerifyHandlerResult = importFlowGoodsBatchTerminalHandler.verifyHandler(model);

        List<ImportFlowGoodsBatchTerminalModel> list = new ArrayList<>();
//        ImportFlowGoodsBatchTerminalModel importModel = new ImportFlowGoodsBatchTerminalModel();
        list.add(model);

        Map<String, Object> paramMap = new HashMap<>();
        Map<String, Object> param = new HashMap<>();
        param.put("gbDetailMonth", "2023-03");
        param.put("currentUserId", "1L");
        String paramStr = JSONUtil.toJsonStr(param);
        paramMap.put("param", paramStr);
        List<ImportFlowGoodsBatchTerminalModel> object = importFlowGoodsBatchTerminalHandler.execute(list, paramMap);
    }


    @Test
    public void flowWashSaleReportExportTest() {

        FlowWashSaleReportPageRequest request = new FlowWashSaleReportPageRequest();
        request.setYear("2023");
        request.setMonth("2");


        byte[] exportByte = flowWashSaleReportExportService.getExportByte(request, "");


        System.out.println(exportByte);
    }


    @Test
    public void test22() {
        Map<String, Object> map = new HashMap<>();
        map.put("startTime", "2023-03-01");
        map.put("endTime", "2023-03-29");
        map.put("dataTags", Arrays.asList(1,2,3));
        map.put("opUserId", 24154L);
//        EsFlowSaleSearchRequest request = flowSaleDayExportService.getParam(map);
//        System.out.println("request=" + JSONUtil.toJsonStr(request));
    }

    @Test
    public void test23() {
        EsFlowSaleSearchRequest request = new EsFlowSaleSearchRequest();
        request.setStartTime(DateUtil.parse("2022-07-01"));
        request.setEndTime(DateUtil.parse("2022-08-31"));
        request.setDataTags(Arrays.asList(0, 1, 2));

        String fileName = "日销售流向202303301044.zip";

        byte[] result = flowSaleDayExportService.getExportByte(null, fileName);

        try {
            FileInfo fileInfo = fileService.upload( new ByteArrayInputStream(result), fileName, FileTypeEnum.FILE_EXPORT_CENTER);
            System.out.println("fileInfo = " + JSONUtil.toJsonStr(fileInfo));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    @Test
    public void test24() {

        QueryOrderPageRequest request = new QueryOrderPageRequest();

        request.setType(1);
        List<Long> eidList = new ArrayList<>();
        eidList.add(2L);
        request.setEidList(eidList);

        request.setOrderType(1);
        request.setSize(1);
        request.setCurrent(1);

        byte[] result = orderEnterpriseSellExportService.getExportByte(request,"销售订单");

        try {
            FileInfo fileInfo = fileService.upload( new ByteArrayInputStream(result), "销售订单", FileTypeEnum.FILE_EXPORT_CENTER);
            System.out.println("fileInfo = " + JSONUtil.toJsonStr(fileInfo));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void flowShopSalePageListExportTest() {
        Map<String, Object> map = new HashMap<>();
        //        map.put("menuSource", "1");
        map.put("menuSource", "1");
        map.put("currentUserId", 1);
        map.put("mainEid", 7);
        map.put("shopEid", 38);
        map.put("startTime", "2023-03-01");
        map.put("endTime", "2023-05-01");
        map.put("provinceCode", "420000");
        map.put("enterpriseTagId", "34");
        QueryFlowShopSaleListPageRequest request = FlowShopSalePageListExportService.getParam(map);
        FlowShopSalePageListExportService.queryData(request);
    }

    @Test
    public void test25(){
        QuerySaleDeptSubTargetDetailRequest request = new QuerySaleDeptSubTargetDetailRequest();
        request.setDepartId(1L).setSaleTargetId(1L);
        byte[] exportByte = saleTargetDetailExportService.getExportByte(request,"销售指标配置明细.zip");
    }
    @Test
    public void crmEnterpriseRelationPinchRunnerExportTest() {
        Map<String, Object> map = new HashMap<>();
        QueryCrmEnterpriseRelationPinchRunnerPageListRequest request = crmEnterpriseRelationPinchRunnerService.getParam(map);
        crmEnterpriseRelationPinchRunnerService.queryData(request);
    }
    @Test
    public void gbAppealFormExportTest() {
        Map<String, Object> map = new HashMap<>();
        map.put("matchMonth", "2023-05");

        QueryGbAppealFormListPageRequest request = gbAppealFormService.getParam(map);
        gbAppealFormService.queryData(request);
    }

    @Test
    public void test26(){
        ExportSettlementSimpleInfoPageListRequest request = new ExportSettlementSimpleInfoPageListRequest();
        byte[] exportByte = b2bSettlementSimpleInfoExportService.getExportByte(request,"结算单导出");
        System.err.println(exportByte);
    }
}
