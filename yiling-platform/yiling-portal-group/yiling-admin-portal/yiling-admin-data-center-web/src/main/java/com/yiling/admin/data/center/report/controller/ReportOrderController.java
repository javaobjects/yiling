package com.yiling.admin.data.center.report.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.admin.data.center.report.vo.ReportOrderVO;
import com.yiling.basic.dict.api.DictApi;
import com.yiling.basic.dict.bo.DictBO;
import com.yiling.dataflow.order.api.FlowSaleApi;
import com.yiling.dataflow.order.dto.FlowOrderExportReportDetailDTO;
import com.yiling.dataflow.order.dto.request.QueryFlowOrderExportReportPageRequest;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
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

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: ww
 * @date: 2022/03/09
 */
@RestController
@RequestMapping("/report/order")
@Api(tags = "统计报表")
@Slf4j
public class ReportOrderController extends BaseController {

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

    @ApiOperation(value = "数据统计")
    @GetMapping("/statistics")
    public Result<ReportOrderVO> getOrderStatistics() {

        ReportOrderVO reportOrderVO = new ReportOrderVO();

        DictBO categoryDict = dictApi.getDictByName("order_category_report_export");
        List<String> categoryNameList = categoryDict.getDataList().stream().map(DictBO.DictData::getLabel).collect(Collectors.toList());

        //工业直属id
        List<Long> eidList = enterpriseApi.listEidsByChannel(EnterpriseChannelEnum.INDUSTRY_DIRECT);

        //POP购进总金额
        QueryOrderExportReportPageRequest request = new QueryOrderExportReportPageRequest();
        request.setCategoryList(categoryNameList);
        request.setOrderType(OrderTypeEnum.POP.getCode());
        request.setPaymentMethodList(new ArrayList<Long>() {{
            add(PaymentMethodEnum.PAYMENT_DAYS.getCode());
            add(PaymentMethodEnum.PREPAYMENT.getCode());
        }});
        request.setType(1);
        request.setEidList(eidList);

        request.setStartCreatTime(DateUtil.beginOfYear(new Date()));
        request.setEndCreatTime(DateUtil.endOfDay(DateUtil.date().offset(DateField.DAY_OF_MONTH,-1)));
        OrderExportReportDTO orderExportReportDTO = orderExportApi.orderExportReport(request);
        BigDecimal amount = getAmount(orderExportReportDTO.getOrderQuantityReportList(),3,BigDecimal.ONE);
        reportOrderVO.setBuyPOPYearAmount(amount);

        request.setStartCreatTime(DateUtil.beginOfMonth(new Date()));

        OrderExportReportDTO orderExportReportMonth = orderExportApi.orderExportReport(request);
        BigDecimal amountMonth = getAmount(orderExportReportMonth.getOrderQuantityReportList(),3,BigDecimal.ONE);
        reportOrderVO.setBuyPOPMonthAmount(amountMonth);

        request.setStartCreatTime(DateUtil.beginOfDay(DateUtil.date().offset(DateField.DAY_OF_MONTH,-1)));
        OrderExportReportDTO orderExportReportDay = orderExportApi.orderExportReport(request);
        BigDecimal amountDay = getAmount(orderExportReportDay.getOrderQuantityReportList(),3,BigDecimal.ONE);
        reportOrderVO.setBuyPOPDayAmount(amountDay);

        //POP回款总金额
        QueryOrderExportReportPageRequest requestBack = new QueryOrderExportReportPageRequest();
        requestBack.setCategoryList(categoryNameList);
        requestBack.setEidList(eidList);
        requestBack.setOrderType(OrderTypeEnum.POP.getCode());
        requestBack.setPaymentMethodList(new ArrayList<Long>(){{add(PaymentMethodEnum.PREPAYMENT.getCode());}});
        requestBack.setType(1);

        requestBack.setStartCreatTime(DateUtil.beginOfYear(new Date()));
        requestBack.setEndCreatTime(DateUtil.endOfDay(DateUtil.date().offset(DateField.DAY_OF_MONTH,-1)));
        OrderExportReportDTO orderBackYearReport = orderExportApi.orderExportReport(requestBack);
        BigDecimal amountBackYear = getAmount(orderBackYearReport.getOrderQuantityReportList(),2,BigDecimal.valueOf(0.975));
        reportOrderVO.setBackPOPYearAmount(amountBackYear);

        requestBack.setStartCreatTime(DateUtil.beginOfMonth(new Date()));

        OrderExportReportDTO orderBackMonthReport = orderExportApi.orderExportReport(requestBack);
        BigDecimal amountBackMonth = getAmount(orderBackMonthReport.getOrderQuantityReportList(),2,BigDecimal.valueOf(0.975));
        reportOrderVO.setBackPOPMonthAmount(amountBackMonth);

        requestBack.setStartCreatTime(DateUtil.beginOfDay(DateUtil.date().offset(DateField.DAY_OF_MONTH,-1)));
        OrderExportReportDTO orderBackDayReport = orderExportApi.orderExportReport(requestBack);
        BigDecimal amountBackDay = getAmount(orderBackDayReport.getOrderQuantityReportList(),2,BigDecimal.valueOf(0.975));
        reportOrderVO.setBackPOPDayAmount(amountBackDay);

        //B2B动销总金额
        QueryOrderExportReportPageRequest requestSell = new QueryOrderExportReportPageRequest();
        requestSell.setCategoryList(categoryNameList);
        requestSell.setOrderType(OrderTypeEnum.B2B.getCode());
        requestSell.setPaymentMethodList(new ArrayList<Long>(){{add(PaymentMethodEnum.OFFLINE.getCode());add(PaymentMethodEnum.PAYMENT_DAYS.getCode());add(PaymentMethodEnum.ONLINE.getCode());}});
        requestSell.setType(2);

        requestSell.setStartCreatTime(DateUtil.beginOfYear(new Date()));
        requestSell.setEndCreatTime(DateUtil.endOfDay(DateUtil.date().offset(DateField.DAY_OF_MONTH,-1)));
        OrderExportReportDTO orderSellReport= orderExportApi.orderExportReport(requestSell);
        BigDecimal amountSellYear = getAmount(orderSellReport.getOrderQuantityReportList(),3,BigDecimal.ONE);
        reportOrderVO.setSellB2BYearAmount(amountSellYear);

        requestSell.setStartCreatTime(DateUtil.beginOfMonth(new Date()));

        OrderExportReportDTO orderSellMonthReport= orderExportApi.orderExportReport(requestSell);
        BigDecimal amountSellMonth = getAmount(orderSellMonthReport.getOrderQuantityReportList(),3,BigDecimal.ONE);
        reportOrderVO.setSellB2BMonthAmount(amountSellMonth);

        requestSell.setStartCreatTime(DateUtil.beginOfDay(DateUtil.date().offset(DateField.DAY_OF_MONTH,-1)));

        OrderExportReportDTO orderSellDayReport= orderExportApi.orderExportReport(requestSell);
        BigDecimal amountSellDay = getAmount(orderSellDayReport.getOrderQuantityReportList(),3,BigDecimal.ONE);
        reportOrderVO.setSellB2BDayAmount(amountSellDay);

        //自建平台
        QueryFlowOrderExportReportPageRequest ownSetPlatformRequest = new QueryFlowOrderExportReportPageRequest();
        ownSetPlatformRequest.setCategoryList(categoryNameList);

        ownSetPlatformRequest.setStartCreatTime(DateUtil.beginOfYear(new Date()));
        ownSetPlatformRequest.setEndCreatTime(DateUtil.endOfDay(DateUtil.date().offset(DateField.DAY_OF_MONTH,-1)));
        List<FlowOrderExportReportDetailDTO> orderFlowReport = flowSaleApi.getOrderFlowReport(ownSetPlatformRequest);
        BigDecimal amountFlowYear = getAmount(PojoUtils.map(orderFlowReport,OrderExportReportDetailDTO.class),3,BigDecimal.ONE);
        reportOrderVO.setOwnSetPlatformYearAmount(amountFlowYear);

        ownSetPlatformRequest.setStartCreatTime(DateUtil.beginOfMonth(new Date()));
        List<FlowOrderExportReportDetailDTO> orderFlowMonthReport = flowSaleApi.getOrderFlowReport(ownSetPlatformRequest);
        BigDecimal amountFlowMonth = getAmount(PojoUtils.map(orderFlowMonthReport,OrderExportReportDetailDTO.class),3,BigDecimal.ONE);
        reportOrderVO.setOwnSetPlatformMonthAmount(amountFlowMonth);

        ownSetPlatformRequest.setStartCreatTime(DateUtil.beginOfDay(DateUtil.date().offset(DateField.DAY_OF_MONTH,-1)));

        List<FlowOrderExportReportDetailDTO> orderFlowDayReport = flowSaleApi.getOrderFlowReport(ownSetPlatformRequest);
        BigDecimal amountFlowDay = getAmount(PojoUtils.map(orderFlowDayReport,OrderExportReportDetailDTO.class),3,BigDecimal.ONE);
        reportOrderVO.setOwnSetPlatformDayAmount(amountFlowDay);

        return Result.success(reportOrderVO);
    }

    private BigDecimal getAmount( List<OrderExportReportDetailDTO> orderQuantityReportList,
                                 Integer priceType,
                                 BigDecimal ratio) {

        BigDecimal amount = BigDecimal.ZERO;
        if (CollectionUtil.isNotEmpty(orderQuantityReportList)) {

            List<Long> goodsIds = orderQuantityReportList.stream().map(OrderExportReportDetailDTO::getGoodsId).collect(Collectors.toList());
            List<ReportPriceParamNameDTO> priceParamNameList = PojoUtils.map(goodsYilingPriceApi.getPriceParamNameList(goodsIds, DateUtil.date()), ReportPriceParamNameDTO.class);
            Map<String, BigDecimal> reportPriceMap = new HashMap<>();
            if(CollectionUtil.isNotEmpty(priceParamNameList)){
                reportPriceMap = priceParamNameList.stream().collect(Collectors.toMap(k -> k.getParamId() + "_" + k.getGoodsId(), ReportPriceParamNameDTO::getPrice, (k1, k2) -> k1));
            }

            for (OrderExportReportDetailDTO one : orderQuantityReportList) {
                BigDecimal price = reportPriceMap.get(priceType + "_" + one.getGoodsId()) == null ? BigDecimal.ZERO : reportPriceMap.get(priceType + "_" + one.getGoodsId());
                BigDecimal paymentAmount = price.multiply(BigDecimal.valueOf(one.getQuantity())).multiply(ratio).divide(new BigDecimal(10000),2,BigDecimal.ROUND_HALF_UP);
                amount = amount.add(paymentAmount);
            }
        }
        return amount;
    }
}
