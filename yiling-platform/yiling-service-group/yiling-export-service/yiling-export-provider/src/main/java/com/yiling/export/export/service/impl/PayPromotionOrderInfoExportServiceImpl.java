package com.yiling.export.export.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.dto.request.QueryGoodsPageListRequest;
import com.yiling.marketing.paypromotion.api.PayPromotionActivityApi;
import com.yiling.marketing.paypromotion.dto.PayPromotionActivityDTO;
import com.yiling.marketing.paypromotion.dto.PayPromotionParticipateDTO;
import com.yiling.marketing.paypromotion.dto.request.QueryPayPromotionActivityPageRequest;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.enums.OrderStatusEnum;
import com.yiling.user.enterprise.api.EmployeeApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.payment.api.PaymentDaysAccountApi;
import com.yiling.user.payment.dto.PaymentDaysOrderDTO;
import com.yiling.user.payment.dto.request.QueryExpireDayOrderRequest;
import com.yiling.user.payment.enums.PaymentOrderRepaymentStatusEnum;
import com.yiling.user.system.api.AdminApi;
import com.yiling.user.system.bo.Admin;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import io.swagger.annotations.ApiModelProperty;

/**
 * 账期到期提醒列表导出
 * @author: lun.yu
 * @date: 2021/8/18
 */
@Service("payPromotionOrderInfoExportService")
public class PayPromotionOrderInfoExportServiceImpl implements BaseExportQueryDataService<QueryPayPromotionActivityPageRequest> {


    @DubboReference
    PaymentDaysAccountApi paymentDaysAccountApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    AdminApi adminApi;
    @DubboReference
    EmployeeApi employeeApi;

    @DubboReference
    PayPromotionActivityApi payPromotionActivityApi;

    @DubboReference
    OrderApi orderApi;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();

    static {
        FIELD.put("participateTime", "时间");
        FIELD.put("marketingPayId","活动ID");
        FIELD.put("name","活动名称");
        FIELD.put("eid", "买家企业ID");
        FIELD.put("ename", "买家企业名称");
        FIELD.put("orderNo", "订单号");
        FIELD.put("status", "订单状态");
        FIELD.put("discountAmount", "优惠金额");
        FIELD.put("payment", "订单实付金额");
    }

    @Override
    public QueryExportDataDTO queryData(QueryPayPromotionActivityPageRequest request) {
        //需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();

        //需要循环调用
        List<Map<String, Object>> data = new ArrayList<>();
        Page<PayPromotionParticipateDTO> participate;
        int current = 1;
        do {
            request.setCurrent(current);
            request.setSize(500);
            participate = payPromotionActivityApi.getPayPromotionParticipateById(request);
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(participate.getRecords())) {
                List<Long> orderIds = participate.getRecords().stream().map(PayPromotionParticipateDTO::getOrderId).collect(Collectors.toList());
                List<Long> ids = participate.getRecords().stream().map(PayPromotionParticipateDTO::getMarketingPayId).collect(Collectors.toList());
                List<OrderDTO> orderPromotionActivityDTOS = orderApi.listByIds(orderIds);
                PayPromotionActivityDTO payPromotionActivityDTO = payPromotionActivityApi.getById(request.getId());
                Map<Long, OrderDTO> orderPromotionActivityDTOMap = new HashMap<>();
                if (org.apache.commons.collections.CollectionUtils.isNotEmpty(orderPromotionActivityDTOS)) {
                    orderPromotionActivityDTOMap = orderPromotionActivityDTOS.stream().collect(Collectors.toMap(OrderDTO::getId, Function.identity()));
                }
                Map<Long, OrderDTO> finalOrderPromotionActivityDTOMap = orderPromotionActivityDTOMap;
                participate.getRecords().forEach(item -> {
                    OrderDTO orderDTO = finalOrderPromotionActivityDTOMap.get(item.getOrderId());
                    item.setPayment(orderDTO.getPaymentAmount());
                    item.setName(payPromotionActivityDTO.getName());
                    item.setDiscountAmount(payPromotionActivityDTO.getSponsorType() == 1 ? orderDTO.getPlatformPaymentDiscountAmount() : orderDTO.getShopPaymentDiscountAmount());
                    item.setOrderNo(orderDTO.getOrderNo());
                    item.setStatus(OrderStatusEnum.getByCode(orderDTO.getOrderStatus()).getName());
                });
                participate.getRecords().forEach(paymentDaysOrderDTO -> data.add(BeanUtil.beanToMap(paymentDaysOrderDTO)));
            }
            current = current + 1;
        } while (CollUtil.isNotEmpty(participate.getRecords()));

        ExportDataDTO exportDataDTO = new ExportDataDTO();
        exportDataDTO.setSheetName("参与次数明细");
        // 页签字段
        exportDataDTO.setFieldMap(FIELD);
        // 页签数据
        exportDataDTO.setData(data);

        List<ExportDataDTO> sheets = new ArrayList<>();
        sheets.add(exportDataDTO);
        result.setSheets(sheets);
        return result;
    }

    @Override
    public QueryPayPromotionActivityPageRequest getParam(Map<String, Object> map) {
        return PojoUtils.map(map, QueryPayPromotionActivityPageRequest.class);
    }
}
