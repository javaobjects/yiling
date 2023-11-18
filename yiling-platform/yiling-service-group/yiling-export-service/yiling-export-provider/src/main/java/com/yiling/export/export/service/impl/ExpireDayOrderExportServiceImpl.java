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
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.payment.api.PaymentDaysAccountApi;
import com.yiling.user.payment.dto.PaymentDaysOrderDTO;
import com.yiling.user.payment.dto.request.QueryExpireDayOrderRequest;

import cn.hutool.core.bean.BeanUtil;

/**
 * @author: tingwei.chen
 * @date: 2021/7/2
 */
@Service("expireDayOrderExportService")
public class ExpireDayOrderExportServiceImpl implements BaseExportQueryDataService<QueryExpireDayOrderRequest> {


    @DubboReference
    PaymentDaysAccountApi paymentDaysAccountApi;
    @DubboReference
    EnterpriseApi enterpriseApi;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();
    static {
        FIELD.put("orderId", "订单id");
        FIELD.put("orderNo", "订单编号");
        FIELD.put("ename","供应商名称");
        FIELD.put("customerName", "采购商名称");
        FIELD.put("period", "账期（天）");
        FIELD.put("usedAmount", "使用金额");
        FIELD.put("returnAmount", "退款金额");
        FIELD.put("expirationTime", "到期时间");
        FIELD.put("repaymentStatus", "还款状态：1-未还款 2-部分还款 3-全部还款");
        FIELD.put("repaymentAmount", "已还款金额");
        FIELD.put("repaymentTime", "还款时间");

    }

    @Override
    public QueryExportDataDTO queryData(QueryExpireDayOrderRequest request) {
        //需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();

        //需要循环调用
        List<Map<String, Object>> data = new ArrayList<>();
        Page<PaymentDaysOrderDTO>  page;
        int current=1;
        do {
            request.setCurrent(current);
            request.setSize(500);
            page = paymentDaysAccountApi.expireDayOrderPage(request);
            if (page == null || CollectionUtils.isEmpty(page.getRecords())) {
                break;
            }
            for (PaymentDaysOrderDTO e : page.getRecords()) {
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
    public QueryExpireDayOrderRequest getParam(Map<String, Object> map) {
        //参数eid的转换为eidList
        QueryExpireDayOrderRequest request = PojoUtils.map(map, QueryExpireDayOrderRequest.class);
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
