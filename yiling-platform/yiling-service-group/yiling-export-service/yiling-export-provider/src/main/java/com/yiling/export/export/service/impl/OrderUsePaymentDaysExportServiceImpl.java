package com.yiling.export.export.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.api.OrderApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.payment.api.PaymentDaysAccountApi;
import com.yiling.user.payment.dto.QuotaOrderDTO;
import com.yiling.user.payment.dto.request.QueryQuotaOrderRequest;

import cn.hutool.core.bean.BeanUtil;

/**
 * @author: tingwei.chen
 * @date: 2021/7/2
 */
@Service("orderUsePaymentDaysExportService")
public class OrderUsePaymentDaysExportServiceImpl implements BaseExportQueryDataService<QueryQuotaOrderRequest> {


    @DubboReference
    PaymentDaysAccountApi paymentDaysAccountApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    OrderApi orderApi;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();
    static {
        FIELD.put("orderNo", "订单编号");
        FIELD.put("createTime","下单时间");
        FIELD.put("orderStatus", "订单状态");
        FIELD.put("usedAmount", "订单金额");
        FIELD.put("temporaryAmount", "临时额度");
        FIELD.put("returnAmount", "退款金额");
    }

    @Override
    public QueryExportDataDTO queryData(QueryQuotaOrderRequest request) {
        //需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();

        //需要循环调用
        List<Map<String, Object>> data = new ArrayList<>();
        Page<QuotaOrderDTO>  page;
        int current=1;
        do {
            request.setCurrent(current);
            request.setSize(500);
            page = paymentDaysAccountApi.quotaOrderPage(request);
            if (page == null || CollectionUtils.isEmpty(page.getRecords())) {
                break;
            }
            for (QuotaOrderDTO e : page.getRecords()) {
                e.setOrderStatus(orderApi.selectByOrderNo(e.getOrderNo()).getOrderStatus());
                data.add(BeanUtil.beanToMap(e));
            }
            current=current+1;
        } while (page != null && CollectionUtils.isNotEmpty(page.getRecords()));


        ExportDataDTO exportDataDTO = new ExportDataDTO();
        exportDataDTO.setSheetName("用户账期导出");
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
    public QueryQuotaOrderRequest getParam(Map<String, Object> map) {
        //参数eid的转换为eidList
        QueryQuotaOrderRequest request = PojoUtils.map(map, QueryQuotaOrderRequest.class);
        Long eid = Long.parseLong(map.getOrDefault("eid", 0L).toString());
        List<Long> eidList = enterpriseApi.listSubEids(eid);
        if (CollectionUtils.isNotEmpty(eidList)) {
            request.setEidList(eidList);
        }else{
            request.setOpUserId(eid);
        }
        return request;
    }
}
