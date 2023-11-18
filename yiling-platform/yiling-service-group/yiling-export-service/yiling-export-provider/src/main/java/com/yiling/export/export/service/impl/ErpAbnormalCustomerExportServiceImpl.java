package com.yiling.export.export.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.dict.api.DictApi;
import com.yiling.basic.dict.bo.DictBO;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.erp.api.ErpCustomerApi;
import com.yiling.open.erp.dto.ErpCustomerDTO;
import com.yiling.open.erp.dto.request.ErpCustomerQueryRequest;
import com.yiling.open.erp.enums.SyncStatus;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 ErpAbnormalCustomerExportServiceImpl
 * @描述 异常客户导出
 * @创建时间 2022/12/14
 * @修改人 shichen
 * @修改时间 2022/12/14
 **/
@Slf4j
@Service("erpAbnormalCustomerExportService")
public class ErpAbnormalCustomerExportServiceImpl implements BaseExportQueryDataService<ErpCustomerQueryRequest> {
    @DubboReference
    private DictApi dictApi;

    @DubboReference(timeout = 60 * 1000)
    private ErpCustomerApi erpCustomerApi;

    @DubboReference
    private EnterpriseApi enterpriseApi;


    private static final String ERP_CUSTOMER_ERROR = "erp_customer_error";

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<String, String>() {
        {
            put("id", "*ID（不可修改）");
            put("name", "*企业名称");
            put("licenseNo", "*统一社会信用代码/医疗机构许可证");
            put("customerType", "*企业类型");
            put("contact", "*联系人");
            put("phone", "*联系电话");
            put("province", "*省");
            put("city", "*市");
            put("region", "*区");
            put("address", "*详细地址");
            put("syncMsg", "失败原因");
            put("suName", "供应商（不可修改）");
        }
    };


    @Override
    public QueryExportDataDTO queryData(ErpCustomerQueryRequest request) {
        QueryExportDataDTO result = new QueryExportDataDTO();
        List<Map<String, Object>> data = ListUtil.toLinkedList();
        List<ErpCustomerDTO> erpCustomerList = erpCustomerApi.getErpCustomerListBySyncStatus(request);
        if(CollectionUtil.isNotEmpty(erpCustomerList)){
            List<Long> suIdList = erpCustomerList.stream().map(ErpCustomerDTO::getSuId).distinct().collect(Collectors.toList());
            Map<Long, EnterpriseDTO> suMap = enterpriseApi.getMapByIds(suIdList);
            data = erpCustomerList.stream().map(e -> {
                Map<String, Object> bean = BeanUtil.beanToMap(e);
                EnterpriseDTO enterpriseDTO = suMap.get(e.getSuId());
                if(null != enterpriseDTO){
                    bean.put("suName",enterpriseDTO.getName());
                }
                return bean;
            }).collect(Collectors.toList());
        }
        ExportDataDTO exportDataDTO = new ExportDataDTO();
        exportDataDTO.setSheetName("客户异常导出");
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
    public ErpCustomerQueryRequest getParam(Map<String, Object> map) {
        ErpCustomerQueryRequest request = PojoUtils.map(map, ErpCustomerQueryRequest.class);
        Object errorCode = map.get("errorCode");
        if(null != errorCode){
            DictBO dictBO = dictApi.getDictByName(ERP_CUSTOMER_ERROR);
            DictBO.DictData dictData = dictBO.getDataList().stream().filter(e -> errorCode.toString().equals(e.getValue())).findFirst().orElse(null);
            if(null != dictData){
                request.setSyncMsg(dictData.getLabel());
            }
        }
        request.setSyncStatus(SyncStatus.FAIL.getCode());
        request.setQueryLimit(3000);
        return request;
    }
}
