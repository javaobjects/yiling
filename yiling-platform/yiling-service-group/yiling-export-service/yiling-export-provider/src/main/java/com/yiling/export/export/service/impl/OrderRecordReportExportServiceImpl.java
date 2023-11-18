package com.yiling.export.export.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.yiling.basic.dict.api.DictApi;
import com.yiling.basic.dict.bo.DictBO;
import com.yiling.dataflow.order.api.FlowSaleApi;
import com.yiling.dataflow.order.dto.FlowOrderExportReportDetailDTO;
import com.yiling.dataflow.order.dto.request.QueryFlowOrderExportReportPageRequest;
import com.yiling.export.export.bo.ExportOrderRecordReportBO;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.ylprice.api.GoodsYilingPriceApi;
import com.yiling.order.order.api.OrderExportApi;
import com.yiling.order.order.dto.OrderExportReportDTO;
import com.yiling.order.order.dto.OrderExportReportDetailDTO;
import com.yiling.order.order.dto.request.QueryOrderExportReportPageRequest;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.order.order.enums.PaymentMethodEnum;
import com.yiling.settlement.report.api.ReportParamApi;
import com.yiling.settlement.report.dto.ReportPriceParamNameDTO;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.enums.EnterpriseChannelEnum;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;

/**
 * @author:wei.wang
 * @date:2021/8/23
 */
@Service("orderDataReportExportService")
public class OrderRecordReportExportServiceImpl implements BaseExportQueryDataService<QueryOrderExportReportPageRequest> {

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();

    static {
        FIELD.put("provinceName", "省份");
    }

    @DubboReference
    DictApi dictApi;
    @DubboReference
    OrderExportApi orderExportApi;
    @DubboReference
    ReportParamApi reportApi;
    @DubboReference
    FlowSaleApi flowSaleApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    GoodsYilingPriceApi goodsYilingPriceApi;


    /**
     * 查询excel中的数据
     *
     * @param request
     * @return
     */
    @Override
    public QueryExportDataDTO queryData(QueryOrderExportReportPageRequest request) {
        //需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();

        //报表数据
        List<Map<String, Object>> data = new ArrayList<>();

        DictBO recordDict = dictApi.getDictByName("order_record_report_export");
        List<DictBO.DictData> dataList = recordDict.getDataList();
        Map<String, String> dictRecordList = dataList.stream().collect(Collectors.toMap(DictBO.DictData::getValue, DictBO.DictData::getLabel, (key1, key2) -> key2));
        String recordName = dictRecordList.get(String.valueOf(request.getRecordType()));

        DictBO categoryDict = dictApi.getDictByName("order_category_report_export");
        Map<String, String> dictCategoryList = categoryDict.getDataList().stream().collect(Collectors.toMap(DictBO.DictData::getValue, DictBO.DictData::getLabel, (key1, key2) -> key2));
        String categoryName = dictCategoryList.get(String.valueOf(request.getCategoryType()));
        List<String> categoryNameList = categoryDict.getDataList().stream().map(DictBO.DictData::getLabel).collect(Collectors.toList());
        //工业直属id
        List<Long> eidList = enterpriseApi.listEidsByChannel(EnterpriseChannelEnum.INDUSTRY_DIRECT);
        request.setEidList(eidList);
        StringBuilder name = new StringBuilder("累计");
        if (request.getRecordType().equals(1)) {
            name.append("购进交易额(万元)");
            request.setCategoryList(new ArrayList<String>() {{
                add(categoryName);
            }});
            request.setOrderType(OrderTypeEnum.POP.getCode());
            request.setPaymentMethodList(new ArrayList<Long>() {{
                add(PaymentMethodEnum.PAYMENT_DAYS.getCode());
                add(PaymentMethodEnum.PREPAYMENT.getCode());
            }});
            request.setType(1);

            getReportExport(request, data, categoryNameList, 3, BigDecimal.ONE);

        } else if (request.getRecordType().equals(2)) {
            name.append("回款交易额(万元)");
            request.setCategoryList(new ArrayList<String>() {{
                add(categoryName);
            }});
            request.setOrderType(OrderTypeEnum.POP.getCode());
            request.setPaymentMethodList(new ArrayList<Long>() {{
                add(PaymentMethodEnum.PREPAYMENT.getCode());
            }});
            request.setType(1);
            getReportExport(request, data, categoryNameList, 2, BigDecimal.valueOf(0.975));

        } else if (request.getRecordType().equals(3)) {
            name.append("自建平台动销交易额(万元)");
            request.setCategoryList(new ArrayList<String>() {{
                add(categoryName);
            }});
            QueryFlowOrderExportReportPageRequest flowRequest = PojoUtils.map(request, QueryFlowOrderExportReportPageRequest.class);
            List<FlowOrderExportReportDetailDTO> orderFlowReport = flowSaleApi.getOrderFlowReport(flowRequest);
            if (CollectionUtil.isNotEmpty(orderFlowReport)) {
                List<Long> goodsIds = orderFlowReport.stream().map(FlowOrderExportReportDetailDTO::getGoodsId).collect(Collectors.toList());
                List<ReportPriceParamNameDTO> priceParamNameList = PojoUtils.map(goodsYilingPriceApi.getPriceParamNameList(goodsIds, DateUtil.date()), ReportPriceParamNameDTO.class);
                Map<String, BigDecimal> reportPriceMap = new HashMap<>();
                if (CollectionUtil.isNotEmpty(priceParamNameList)) {
                    reportPriceMap = priceParamNameList.stream().collect(Collectors.toMap(k -> k.getParamId() + "_" + k.getGoodsId(), ReportPriceParamNameDTO::getPrice, (k1, k2) -> k1));
                }
                Map<String, BigDecimal> mapAmount = new HashMap<>();
                Map<String, BigDecimal> mapPyaAmount = new HashMap<>();
                for (FlowOrderExportReportDetailDTO one : orderFlowReport) {
                    BigDecimal price = reportPriceMap.get(3 + "_" + one.getGoodsId()) == null ? BigDecimal.ZERO : reportPriceMap.get(3 + "_" + one.getGoodsId());
                    BigDecimal amount = price.multiply(BigDecimal.valueOf(one.getQuantity())).divide(new BigDecimal(10000), 2, BigDecimal.ROUND_HALF_UP);
                    if (mapAmount.containsKey(one.getProvinceName())) {
                        BigDecimal allAmount = mapAmount.get(one.getProvinceName()).add(amount);
                        mapAmount.put(one.getProvinceName(), allAmount);
                    } else {
                        mapAmount.put(one.getProvinceName(), amount);
                    }

                    if (mapPyaAmount.containsKey(one.getProvinceName())) {
                        BigDecimal allAmount = mapPyaAmount.get(one.getProvinceName()).add(one.getPaymentAmount());
                        mapPyaAmount.put(one.getProvinceName(), allAmount);
                    } else {
                        mapPyaAmount.put(one.getProvinceName(), one.getPaymentAmount());
                    }
                }
                BigDecimal totalAmount = BigDecimal.ZERO;
                for (Map.Entry<String, BigDecimal> entry : mapPyaAmount.entrySet()) {
                    ExportOrderRecordReportBO recordReportBO = new ExportOrderRecordReportBO();
                    recordReportBO.setProvinceName(entry.getKey());
                    recordReportBO.setBuyAmount(mapAmount.get(entry.getKey()));
                    recordReportBO.setPaymentAmount(entry.getValue().divide(new BigDecimal(10000), 2, BigDecimal.ROUND_HALF_UP));
                    totalAmount = totalAmount.add(recordReportBO.getBuyAmount());
                    Map<String, Object> dataPojo = BeanUtil.beanToMap(recordReportBO);
                    data.add(dataPojo);
                }
                ExportOrderRecordReportBO recordReportBO = new ExportOrderRecordReportBO();
                recordReportBO.setProvinceName("合计");
                recordReportBO.setBuyAmount(totalAmount);
                Map<String, Object> dataPojo = BeanUtil.beanToMap(recordReportBO);
                data.add(dataPojo);

            }
            flowRequest.setCategoryList(categoryNameList);
            List<FlowOrderExportReportDetailDTO> orderFlowReportAll = flowSaleApi.getOrderFlowReport(flowRequest);
            if (CollectionUtil.isNotEmpty(orderFlowReportAll)) {
                List<Long> goodsIds = orderFlowReportAll.stream().map(FlowOrderExportReportDetailDTO::getGoodsId).collect(Collectors.toList());
                List<ReportPriceParamNameDTO> priceParamNameList = PojoUtils.map(goodsYilingPriceApi.getPriceParamNameList(goodsIds, DateUtil.date()), ReportPriceParamNameDTO.class);
                Map<String, BigDecimal> reportPriceMap = new HashMap<>();
                if (CollectionUtil.isNotEmpty(priceParamNameList)) {
                    reportPriceMap = priceParamNameList.stream().collect(Collectors.toMap(k -> k.getParamId() + "_" + k.getGoodsId(), ReportPriceParamNameDTO::getPrice, (k1, k2) -> k1));
                }
                BigDecimal allAmount = BigDecimal.ZERO;
                for (FlowOrderExportReportDetailDTO one : orderFlowReportAll) {
                    BigDecimal price = reportPriceMap.get(3 + "_" + one.getGoodsId()) == null ? BigDecimal.ZERO : reportPriceMap.get(3 + "_" + one.getGoodsId());
                    BigDecimal amount = price.multiply(BigDecimal.valueOf(one.getQuantity())).divide(new BigDecimal(10000), 2, BigDecimal.ROUND_HALF_UP);
                    allAmount = allAmount.add(amount);
                }
                ExportOrderRecordReportBO recordReportOne = new ExportOrderRecordReportBO();
                recordReportOne.setProvinceName("总额");
                recordReportOne.setBuyAmount(allAmount);
                Map<String, Object> dataOne = BeanUtil.beanToMap(recordReportOne);
                data.add(dataOne);
            }

        } else if (request.getRecordType().equals(4)) {
            name.append("大运河动销交易额(万元)");
            request.setEidList(null);
            request.setCategoryList(new ArrayList<String>() {{
                add(categoryName);
            }});
            request.setOrderType(OrderTypeEnum.B2B.getCode());
            request.setPaymentMethodList(new ArrayList<Long>() {{
                add(PaymentMethodEnum.OFFLINE.getCode());
                add(PaymentMethodEnum.PAYMENT_DAYS.getCode());
                add(PaymentMethodEnum.ONLINE.getCode());
            }});
            request.setType(2);
            getReportExport(request, data, categoryNameList, 3, BigDecimal.ONE);
        }

        FIELD.put("buyAmount", name.toString());
        FIELD.put("paymentAmount", "已付款金额(万元)");

        ExportDataDTO exportDataDTO = new ExportDataDTO();
        exportDataDTO.setSheetName(recordName + "(" + categoryName + ")");
        // 页签字段
        exportDataDTO.setFieldMap(FIELD);
        // 页签数据
        exportDataDTO.setData(data);

        List<ExportDataDTO> sheets = new ArrayList<>();
        sheets.add(exportDataDTO);
        result.setSheets(sheets);
        return result;
    }

    /**
     * @param request
     * @param data
     * @param categoryNameList 字典配置莲花非莲花
     * @param priceType 2:表示供货价 3:表示商销价
     * @param ratio 比率
     */
    private void getReportExport(QueryOrderExportReportPageRequest request, List<Map<String, Object>> data, List<String> categoryNameList, Integer priceType, BigDecimal ratio) {
        OrderExportReportDTO orderExportReportDTO = orderExportApi.orderExportReport(request);

        List<OrderExportReportDetailDTO> orderQuantityReportList = orderExportReportDTO.getOrderQuantityReportList();
        List<OrderExportReportDetailDTO> orderPaymentReportList = orderExportReportDTO.getOrderPaymentReportList();

        Map<String, BigDecimal> amountMap = getResultMap(priceType, ratio, orderQuantityReportList);
        if(CollectionUtil.isNotEmpty(orderPaymentReportList)){
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (OrderExportReportDetailDTO one : orderPaymentReportList) {
                ExportOrderRecordReportBO recordReportBO = new ExportOrderRecordReportBO();
                recordReportBO.setProvinceName(one.getProvinceName());
                recordReportBO.setBuyAmount(amountMap.get(one.getProvinceName()));
                recordReportBO.setPaymentAmount(one.getPaymentAmount().divide(new BigDecimal(10000), 2, BigDecimal.ROUND_HALF_UP));
                totalAmount = totalAmount.add(recordReportBO.getBuyAmount());
                Map<String, Object> dataPojo = BeanUtil.beanToMap(recordReportBO);
                data.add(dataPojo);
            }
            ExportOrderRecordReportBO recordReportBO = new ExportOrderRecordReportBO();
            recordReportBO.setProvinceName("合计");
            recordReportBO.setBuyAmount(totalAmount);
            Map<String, Object> dataPojo = BeanUtil.beanToMap(recordReportBO);
            data.add(dataPojo);
        }
        request.setCategoryList(categoryNameList);

        OrderExportReportDTO orderExportReportAmount = orderExportApi.orderExportReport(request);
        Map<String, BigDecimal> amountAllMap = getResultMap(priceType, ratio, orderExportReportAmount.getOrderQuantityReportList());
        if(CollectionUtil.isNotEmpty(orderExportReportAmount.getOrderPaymentReportList())){
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (OrderExportReportDetailDTO one : orderExportReportAmount.getOrderPaymentReportList()) {
                totalAmount = totalAmount.add(amountAllMap.get(one.getProvinceName()));
            }
            ExportOrderRecordReportBO recordReportOne = new ExportOrderRecordReportBO();
            recordReportOne.setProvinceName("总额");
            recordReportOne.setBuyAmount(totalAmount);
            Map<String, Object> dataOne = BeanUtil.beanToMap(recordReportOne);
            data.add(dataOne);
        }
    }

    private Map<String, BigDecimal> getResultMap(Integer priceType, BigDecimal ratio, List<OrderExportReportDetailDTO> orderQuantityReportList) {
        Map<String, BigDecimal> amountMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(orderQuantityReportList)) {

            List<Long> goodsIds = orderQuantityReportList.stream().map(OrderExportReportDetailDTO::getGoodsId).collect(Collectors.toList());
            List<ReportPriceParamNameDTO> priceParamNameList = PojoUtils.map(goodsYilingPriceApi.getPriceParamNameList(goodsIds, DateUtil.date()), ReportPriceParamNameDTO.class);
            Map<String, BigDecimal> reportPriceMap = new HashMap<>();
            if (CollectionUtil.isNotEmpty(priceParamNameList)) {
                reportPriceMap = priceParamNameList.stream().collect(Collectors.toMap(k -> k.getParamId() + "_" + k.getGoodsId(), ReportPriceParamNameDTO::getPrice, (k1, k2) -> k1));
            }
            for (OrderExportReportDetailDTO one : orderQuantityReportList) {
                BigDecimal price = reportPriceMap.get(priceType + "_" + one.getGoodsId()) == null ? BigDecimal.ZERO : reportPriceMap.get(priceType + "_" + one.getGoodsId());
                BigDecimal paymentAmount = price.multiply(BigDecimal.valueOf(one.getQuantity())).multiply(ratio).divide(new BigDecimal(10000), 2, BigDecimal.ROUND_HALF_UP);
                one.setPaymentAmount(paymentAmount);
                if (amountMap.containsKey(one.getProvinceName())) {
                    BigDecimal amount = amountMap.get(one.getProvinceName()).add(paymentAmount);
                    amountMap.put(one.getProvinceName(), amount);
                } else {
                    amountMap.put(one.getProvinceName(), paymentAmount);
                }
            }
        }
        return amountMap;
    }

    @Override
    public QueryOrderExportReportPageRequest getParam(Map<String, Object> map) {
        QueryOrderExportReportPageRequest request = PojoUtils.map(map, QueryOrderExportReportPageRequest.class);
        request.setEndCreatTime(DateUtil.endOfDay(request.getEndCreatTime()));
        request.setStartCreatTime(DateUtil.beginOfDay(request.getStartCreatTime()));
        return request;
    }
}
