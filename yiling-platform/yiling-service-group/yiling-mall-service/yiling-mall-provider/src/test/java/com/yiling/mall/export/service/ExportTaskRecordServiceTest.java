package com.yiling.mall.export.service;

import com.yiling.mall.BaseTest;

/**
 * @Author：xingjian.lan
 * @Email：xingjian.lan@rograndec.com
 * @CreateDate：2020/9/17
 * @Version：1.0
 */
public class ExportTaskRecordServiceTest extends BaseTest {

//    @Autowired
//    private ExportTaskRecordService exportTaskRecordService;
//
//    @Autowired
//    private FileService fileService;
//
//    @Autowired
//    private GoodsBiddingPricePageListExportServiceImpl goodsBiddingPricePageListExportService;
//    @Autowired
//    RebateSuccessOrderDetailPageListExportServiceImpl rebateSuccessOrderDetailPageListExportService;
//    @Autowired
//    AgreementApplyDetailExportServiceImpl agreementApplyDetailExportService;
//    @Autowired
//    AgreementApplyBatchExportServiceImpl  agreementApplyBatchExportService;
//    @Autowired
//	AgreementUseExportServiceImpl         agreementUseExportService;
//    @Autowired
//    OrderPurchaseExportServiceImpl        orderPurchaseExportService;
//    @Autowired
//    OrderSaleExportServiceImpl            orderSaleExportService;
//    @Autowired
//    OrderInvoiceExportServiceImpl         orderInvoiceExportService;
//    @Autowired
//    OrderAdminExportServiceImpl           orderAdminExportService;
//
//
//    @Test
//    public void createExportTaskRecord() {
//        SaveExportTaskRequest request = JSON.parseObject("{\"className\":\"userExportService\",\"fileName\":\"commissionRetail\",\"menuName\":\"管理后台 - 用户信息\",\"groupName\":\"用户信息\",\"searchConditionList\":[],\"maxCount\":10000}", SaveExportTaskRequest.class);
//        request.setOpUserId(11111L);
//        exportTaskRecordService.saveExportTaskRecord(request);
//        try {
//            Thread.sleep(1000 * 60);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void exportTaskRecord() {
//        exportTaskRecordService.syncUploadExportTask(233L);
//    }
//
//    @Test
//    public void fileUpload() {
//        try {
//            FileInfo fileInfo = fileService.upload(FileUtil.getInputStream("D:\\workspace\\yiling-platform\\upload\\excelUpload\\pic61611646532.JPG"), "pic61611646532.JPG", FileTypeEnum.GOODS_PICTURE);
//            System.out.println(fileInfo.getUrl());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void exporyGoodsBiddingPrice() {
//        QueryGoodsPageListRequest request = new QueryGoodsPageListRequest();
////		request.setName("阿");
//
//        QueryExportDataDTO queryExportDataDTO = goodsBiddingPricePageListExportService.queryData(request);
//        System.err.println(queryExportDataDTO);
//    }
//
//    @Test
//    public void exporyApply() {
//        ExportAgreementApplyRequest request = new ExportAgreementApplyRequest();
//
//        request.setApplyId(5L);
//        QueryExportDataDTO queryExportDataDTO = agreementApplyDetailExportService.queryData(request);
//        System.err.println(JSON.toJSON(queryExportDataDTO));
//    }
//
//    @Test
//    public void exporyApplyBatch() {
//        ExportAgreementBatchApplyRequest request = new ExportAgreementBatchApplyRequest();
//
////		request.setEid(43L);
////		request.setEasCode("010.JXFX");
//		request.setQueryType(1);
//		request.setOpUserId(1L);
//        QueryExportDataDTO queryExportDataDTO = agreementApplyBatchExportService.queryData(request);
//        System.err.println(JSON.toJSON(queryExportDataDTO));
//    }
//    @Test
//    public void exporyUseBatch() {
//		ExportUseListRequest request = new ExportUseListRequest();
//		request.setEnterpriseId(41L);
//		request.setEasCode("0531.YTKN");
//        request.setQueryType(2);
//        QueryExportDataDTO queryExportDataDTO = agreementUseExportService.queryData(request);
//        System.err.println(JSON.toJSON(queryExportDataDTO));
//    }
//
//    @Test
//    public void rebateSuccessOrderDetail() {
//        QueryRebateOrderPageListRequest request = new QueryRebateOrderPageListRequest();
//        request.setAgreementId(58L);
//
//        QueryExportDataDTO queryExportDataDTO = rebateSuccessOrderDetailPageListExportService.queryData(request);
//        System.err.println(queryExportDataDTO);
//    }
//
//    @Test
//    public void orderPurchaseExportServiceTest() {
//        List<Long> list = new ArrayList<>();
//        list.add(36L);
//        QueryOrderPageRequest request = new QueryOrderPageRequest();
//        request.setEidList(list);
//        request.setType(2);
//        QueryExportDataDTO queryExportDataDTO = orderPurchaseExportService.queryData(request);
//        System.out.println(queryExportDataDTO);
//    }
//
//    @Test
//    public void orderSaleExportServiceTest() {
//        List<Long> list = new ArrayList<>();
//        list.add(2L);
//        list.add(3L);
//        list.add(4L);
//        list.add(5L);
//        QueryOrderPageRequest request = new QueryOrderPageRequest();
//        request.setEidList(list);
//        request.setType(1);
//        request.setYiLingOrdinary(false);
//        QueryExportDataDTO queryExportDataDTO = orderSaleExportService.queryData(request);
//        System.out.println(queryExportDataDTO);
//    }
//
//    @Test
//    public void orderInvoiceExportServiceTest() {
//        List<Long> list = new ArrayList<>();
//        list.add(2L);
//        list.add(3L);
//        list.add(4L);
//        list.add(5L);
//        QueryInvoicePageRequest request = new QueryInvoicePageRequest();
//        request.setEidLists(list);
//        QueryExportDataDTO queryExportDataDTO = orderInvoiceExportService.queryData(request);
//        System.out.println(queryExportDataDTO);
//    }
//
//    @Test
//    public void orderAdminExportServiceTest() {
//
//        QueryBackOrderInfoRequest request = new QueryBackOrderInfoRequest();
//        QueryExportDataDTO queryExportDataDTO = orderAdminExportService.queryData(request);
//        System.out.println(queryExportDataDTO);
//    }
}