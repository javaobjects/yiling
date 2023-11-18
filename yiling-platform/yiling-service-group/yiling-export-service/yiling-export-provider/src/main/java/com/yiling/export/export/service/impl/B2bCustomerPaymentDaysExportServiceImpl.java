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
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.payment.api.PaymentDaysAccountApi;
import com.yiling.user.payment.dto.PaymentDaysAccountDTO;
import com.yiling.user.payment.dto.request.QueryPaymentDaysAccountPageListRequest;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;

/**
 * 运营后台-中台 采购商账期列表导出
 * @author: lun.yu
 * @date: 2021/11/11
 */
@Service("b2bCustomerPaymentDaysExportService")
public class B2bCustomerPaymentDaysExportServiceImpl implements BaseExportQueryDataService<QueryPaymentDaysAccountPageListRequest> {

    @DubboReference
    PaymentDaysAccountApi paymentDaysAccountApi;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();
    static {
        FIELD.put("eid", "授信主体ID");
        FIELD.put("ename", "授信主体名称");
        FIELD.put("customerEid", "采购商ID");
        FIELD.put("customerName", "采购商名称");
        FIELD.put("startTime", "开始时间");
        FIELD.put("endTime", "结束时间");
        FIELD.put("totalAmount", "信用额度(元)");
        FIELD.put("temporaryAmount", "临时额度(元)");
        FIELD.put("usedAmount", "已使用额度(元)");
        FIELD.put("repaymentAmount", "已还款额度(元)");
        FIELD.put("availableAmount", "可用额度(元)");
        FIELD.put("statusName", "状态");
        FIELD.put("period", "信用周期(天)");
        FIELD.put("upPoint", "上浮点位(%)");
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

                //待还款额度
                paymentDaysAccountDTO.setStatusName(Objects.requireNonNull(EnableStatusEnum.getByCode(paymentDaysAccountDTO.getStatus())).getName());
                String date = "1970-01-01 00:00:00";
                if(paymentDaysAccountDTO.getStartTime().compareTo(DateUtil.parseDate(date)) == 0){
                    paymentDaysAccountDTO.setStartTime(null);
                }
                if(paymentDaysAccountDTO.getEndTime().compareTo(DateUtil.parseDate(date)) == 0){
                    paymentDaysAccountDTO.setEndTime(null);
                }
                data.add(BeanUtil.beanToMap(paymentDaysAccountDTO));
            });
            current = current + 1;

        } while (CollectionUtils.isNotEmpty(page.getRecords()));


        ExportDataDTO exportDataDTO = new ExportDataDTO();
        exportDataDTO.setSheetName("采购商账期列表导出");
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

        return PojoUtils.map(map, QueryPaymentDaysAccountPageListRequest.class);
    }

}
