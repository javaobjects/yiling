package com.yiling.export.export.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.common.enums.PaymentTemporaryAuditStatusEnum;
import com.yiling.user.payment.api.PaymentDaysAccountApi;
import com.yiling.user.payment.dto.ShortTimeQuotaDTO;
import com.yiling.user.payment.dto.request.QueryShortTimeQuotaAccountRequest;

import cn.hutool.core.bean.BeanUtil;

/**
 * @author: tingwei.chen
 * @date: 2021/7/2
 */
@Service("paymentDaysTemporaryExport")
public class PaymentDaysTemporaryExportServiceImpl implements BaseExportQueryDataService<QueryShortTimeQuotaAccountRequest> {


    @DubboReference
    PaymentDaysAccountApi paymentDaysAccountApi;

    @Resource(name = "paymentDaysAccountService")
    PaymentDaysAccountExportServiceImpl paymentDaysAccountExportService;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();
    static {
        FIELD.put("eid", "授信主体ID");
        FIELD.put("ename", "授信主体名称");
        FIELD.put("customerEid","采购商ID");
        FIELD.put("customerName","采购商名称");
        FIELD.put("shortTimeQuota", "申请的临时额度(元)");
        FIELD.put("createTime", "申请时间");
        FIELD.put("auditStatusName", "审核状态");
        FIELD.put("updateUserId", "审核人ID");
        FIELD.put("updateUser", "审核人姓名");
        FIELD.put("updateTime", "审核人时间");
    }

    @Override
    public QueryExportDataDTO queryData(QueryShortTimeQuotaAccountRequest request) {
        //需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();

        //需要循环调用
        List<Map<String, Object>> data = new ArrayList<>();
        Page<ShortTimeQuotaDTO>  page;
        int current=1;
        do {
            request.setCurrent(current);
            request.setSize(500);

            page = paymentDaysAccountApi.shortTimeQuotaPage(request);
            if (Objects.isNull(page) || CollectionUtils.isEmpty(page.getRecords())) {
                break;
            }

            //审核状态需要在excel里显示中文，故单独字段处理
            page.getRecords().forEach(shortTimeQuotaDTO -> {
                shortTimeQuotaDTO.setAuditStatusName(Objects.requireNonNull(PaymentTemporaryAuditStatusEnum.getByCode(shortTimeQuotaDTO.getAuditStatus())).getName());
                data.add(BeanUtil.beanToMap(shortTimeQuotaDTO));
            });
            current = current + 1;

        } while (CollectionUtils.isNotEmpty(page.getRecords()));


        ExportDataDTO exportDataDTO = new ExportDataDTO();
        exportDataDTO.setSheetName("临时额度审核导出");
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
    public QueryShortTimeQuotaAccountRequest getParam(Map<String, Object> map) {
        //参数eid的转换为eidList
        QueryShortTimeQuotaAccountRequest request = PojoUtils.map(map, QueryShortTimeQuotaAccountRequest.class);
        List<Long> eidList = paymentDaysAccountExportService.getEidList(map);
        request.setEidList(eidList);

        return request;
    }
}
