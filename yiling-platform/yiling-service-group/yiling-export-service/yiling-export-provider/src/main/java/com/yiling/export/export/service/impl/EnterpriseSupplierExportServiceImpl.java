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
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.enterprise.api.EnterpriseSupplierApi;
import com.yiling.user.enterprise.bo.EnterpriseSupplierBO;
import com.yiling.user.enterprise.dto.request.QuerySupplierPageRequest;

import cn.hutool.core.bean.BeanUtil;

/**
 * 商家后台中台-供应商列表导出
 *
 * @author: lun.yu
 * @date: 2023-06-16
 */
@Service("enterpriseSupplierExportService")
public class EnterpriseSupplierExportServiceImpl implements BaseExportQueryDataService<QuerySupplierPageRequest> {

    @DubboReference
    EnterpriseSupplierApi enterpriseSupplierApi;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();

    static {
        FIELD.put("ename", "供应商名称");
        FIELD.put("licenseNumber", "社会统一信用代码");
        FIELD.put("erpName", "ERP供应商名称");
        FIELD.put("erpCode", "ERP编码");
        FIELD.put("erpInnerCode", "ERP内码");
        FIELD.put("buyerCode", "采购员编码");
        FIELD.put("buyerName", "采购员名称");
    }

    @Override
    public QueryExportDataDTO queryData(QuerySupplierPageRequest request) {
        //需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();

        //需要循环调用
        List<Map<String, Object>> data = new ArrayList<>();
        Page<EnterpriseSupplierBO> page;
        int current = 1;

        do {
            request.setCurrent(current);
            request.setSize(500);
            page = enterpriseSupplierApi.queryListPage(request);
            if (Objects.isNull(page) || CollectionUtils.isEmpty(page.getRecords())) {
                break;
            }

            page.getRecords().forEach(enterpriseSupplierBO -> data.add(BeanUtil.beanToMap(enterpriseSupplierBO)));
            current = current + 1;

        } while (CollectionUtils.isNotEmpty(page.getRecords()));


        ExportDataDTO exportDataDTO = new ExportDataDTO();
        exportDataDTO.setSheetName("供应商列表导出");
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
    public QuerySupplierPageRequest getParam(Map<String, Object> map) {
        QuerySupplierPageRequest request = PojoUtils.map(map, QuerySupplierPageRequest.class);
        if (map.get("eid") != null) {
            request.setEid(null);
            request.setCustomerEid(Long.parseLong(map.get("eid").toString()));
        }

        return request;
    }

}

