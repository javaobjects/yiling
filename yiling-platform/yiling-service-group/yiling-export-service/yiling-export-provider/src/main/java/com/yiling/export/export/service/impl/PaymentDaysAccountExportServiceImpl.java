package com.yiling.export.export.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.payment.api.PaymentDaysAccountApi;
import com.yiling.user.payment.dto.PaymentDaysAccountDTO;
import com.yiling.user.payment.dto.request.QueryPaymentDaysAccountPageListRequest;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;

/**
 * @author: tingwei.chen
 * @date: 2021/7/2
 */
@Service("paymentDaysAccountService")
public class PaymentDaysAccountExportServiceImpl implements BaseExportQueryDataService<QueryPaymentDaysAccountPageListRequest> {

    @DubboReference
    PaymentDaysAccountApi paymentDaysAccountApi;
    @DubboReference
    EnterpriseApi         enterpriseApi;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();
    static {
        FIELD.put("eid", "授信主体ID");
        FIELD.put("ename", "授信主体名称");
        FIELD.put("customerEid","采购商ID");
        FIELD.put("customerName", "采购商名称");
        FIELD.put("startTime", "账期开始时间");
        FIELD.put("endTimeName", "账期结束时间");
        FIELD.put("totalAmount", "企业总额度(元)");
        FIELD.put("temporaryAmount", "临时额度(元)");
        FIELD.put("usedAmount", "已使用额度(元)");
        FIELD.put("repaymentAmount", "已还款额度(元)");
        FIELD.put("availableAmount", "可用额度(元)");
        FIELD.put("statusName", "状态");
        FIELD.put("period", "信用周期(天)");
    }

    @Override
    public QueryExportDataDTO queryData(QueryPaymentDaysAccountPageListRequest request) {
        //需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();

        //需要循环调用
        List<Map<String, Object>> data = new ArrayList<>();
        Page<PaymentDaysAccountDTO> page;
        int current = 1;

        do {
            request.setCurrent(current);
            request.setSize(500);
            page = paymentDaysAccountApi.pageList(request);
            if ( Objects.isNull(page) || CollectionUtils.isEmpty(page.getRecords())) {
                break;
            }

            page.getRecords().forEach(paymentDaysAccountDTO -> {
                paymentDaysAccountDTO.setUsedAmount(paymentDaysAccountDTO.getUsedAmount().add(paymentDaysAccountDTO.getHistoryUseAmount()));
                paymentDaysAccountDTO.setRepaymentAmount(paymentDaysAccountDTO.getRepaymentAmount().add(paymentDaysAccountDTO.getHistoryRepaymentAmount()));
                paymentDaysAccountDTO.setStatusName(Objects.requireNonNull(EnableStatusEnum.getByCode(paymentDaysAccountDTO.getStatus())).getName());
                paymentDaysAccountDTO.setEndTimeName(paymentDaysAccountDTO.getLongEffective() == 1 ? "长期有效" : DateUtil.formatDateTime(paymentDaysAccountDTO.getEndTime()));
                data.add(BeanUtil.beanToMap(paymentDaysAccountDTO));
            });
            current = current + 1;

        } while (CollectionUtils.isNotEmpty(page.getRecords()));


        ExportDataDTO exportDataDTO = new ExportDataDTO();
        exportDataDTO.setSheetName("账期列表导出");
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
    public QueryPaymentDaysAccountPageListRequest getParam(Map<String, Object> map) {
        //参数eid的转换为eidList
        QueryPaymentDaysAccountPageListRequest request = PojoUtils.map(map, QueryPaymentDaysAccountPageListRequest.class);
        List<Long> eidList = getEidList(map);
        request.setEidList(eidList);

        return request;
    }

    public List<Long> getEidList(Map<String, Object> map) {
        Long eid = Long.parseLong(map.getOrDefault("eid", 0L).toString());

        List<Long> eidList = new ArrayList<>();
        eidList.add(eid);
        if(Constants.YILING_EID.equals(eid)){
            List<Long> eidLists = enterpriseApi.listSubEids(eid);
            if (CollUtil.isNotEmpty(eidLists)) {
                eidList.addAll(eidLists);
            }
        }
        return eidList;
    }
}
