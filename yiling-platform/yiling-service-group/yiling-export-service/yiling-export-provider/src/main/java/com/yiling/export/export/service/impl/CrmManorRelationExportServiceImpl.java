package com.yiling.export.export.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.api.CrmEnterpriseRelationManorApi;
import com.yiling.dataflow.crm.api.CrmGoodsCategoryApi;
import com.yiling.dataflow.crm.api.CrmManorApi;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseRelationManorDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsCategoryDTO;
import com.yiling.dataflow.crm.dto.CrmManorDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmEnterpriseRelationManorPageRequest;
import com.yiling.dataflow.crm.dto.request.QueryCrmManorPageRequest;
import com.yiling.export.export.bo.ExportCrmManorBO;
import com.yiling.export.export.bo.ExportCrmManorRelationBO;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * DIH商业档案导出基于模版
 */
@Service("crmManorRelationExportService")
@Slf4j
public class CrmManorRelationExportServiceImpl implements BaseExportQueryDataService<QueryCrmManorPageRequest> {
    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();
    @DubboReference(timeout = 60 * 1000)
    private CrmManorApi crmManorApi;
    @DubboReference(timeout = 60 * 1000)
    private CrmEnterpriseRelationManorApi crmEnterpriseRelationManorApi;
    @DubboReference(timeout = 60 * 1000)
    private CrmEnterpriseApi crmEnterpriseApi;
    @DubboReference(timeout = 60 * 1000)
    private CrmGoodsCategoryApi crmGoodsCategoryApi;
    UserApi userApi;

    static {
        FIELD.put("manorNo", "辖区编码");//辖区编码
        FIELD.put("manorName", "辖区名称");//辖区名称
        FIELD.put("crmEnterpriseId", "机构编码");//机构编码
        FIELD.put("crmEnterpriseName", "机构名称");//机构名称
        FIELD.put("crmEnterpriseCode", "CRM编码");//CRM编码
        FIELD.put("categoryName", "品种");//品种
    }


    @Override
    public QueryExportDataDTO queryData(QueryCrmManorPageRequest request) {
        // 需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();
        List<Map<String, Object>> data = new ArrayList<>();
        //操作数据
        buildExportData(request, data);
        //设置结果
        return setResultParam(result, data);
    }

    @Override
    public QueryCrmManorPageRequest getParam(Map<String, Object> map) {
        QueryCrmManorPageRequest request = PojoUtils.map(map, QueryCrmManorPageRequest.class);
        return request;
    }

    // 校验数据权限、查询数据
    void buildExportData(QueryCrmManorPageRequest request, List<Map<String, Object>> data) {
        Page<CrmEnterpriseRelationManorDTO> page = null;
        int current = 1;
        do {
            request.setCurrent(current);
            request.setSize(500);
            //数据处理部分
            //  查询导出的数据填入data
            //供应商角色
            page = crmEnterpriseRelationManorApi.pageExportList(request);
            List<CrmEnterpriseRelationManorDTO> records = page.getRecords();
            if (CollUtil.isEmpty(records)) {
                continue;
            }
            List<Long> crmEnIds = Optional.ofNullable(page.getRecords().stream().map(CrmEnterpriseRelationManorDTO::getCrmEnterpriseId).collect(Collectors.toList())).orElse(ListUtil.empty());
            List<Long> categoryIds = Optional.ofNullable(page.getRecords().stream().map(CrmEnterpriseRelationManorDTO::getCategoryId).collect(Collectors.toList())).orElse(ListUtil.empty());
            //如果参数是empty列表，返回空List;
            List<CrmEnterpriseDTO> crmEnterpriseDTOS = crmEnterpriseApi.getCrmEnterpriseListById(crmEnIds);
            //如果参数是empty列表，返回空List;
            List<CrmGoodsCategoryDTO> categoryDTOS = crmGoodsCategoryApi.findByIds(categoryIds);
            Map<Long, CrmEnterpriseDTO> crmEnterpriseDTOMap = Optional.ofNullable(crmEnterpriseDTOS.stream().collect(Collectors.toMap(CrmEnterpriseDTO::getId, m -> m, (a, b) -> a))).orElse(Maps.newHashMap());
            Map<Long, CrmGoodsCategoryDTO> categoryDTOMap = Optional.ofNullable(categoryDTOS.stream().collect(Collectors.toMap(CrmGoodsCategoryDTO::getId, m -> m, (a, b) -> a))).orElse(Maps.newHashMap());

            //组装数据
            page.getRecords().forEach(item -> {
                if (Objects.nonNull(crmEnterpriseDTOMap.get(item.getCrmEnterpriseId()))) {
                    CrmEnterpriseDTO crmEnterpriseDTO = crmEnterpriseDTOMap.get(item.getCrmEnterpriseId());
                    item.setCrmEnterpriseId(crmEnterpriseDTO.getId());
                    item.setCrmEnterpriseName(crmEnterpriseDTO.getName());
                    item.setCrmEnterpriseCode(crmEnterpriseDTO.getCode());
                }
                if (Objects.nonNull(categoryDTOMap.get(item.getCategoryId()))) {
                    CrmGoodsCategoryDTO crmGoodsCategoryDTO = categoryDTOMap.get(item.getCategoryId());
                    item.setCategoryName(crmGoodsCategoryDTO.getName());
                }
                data.add(BeanUtil.beanToMap(item));
            });
            current = current + 1;
        } while (CollectionUtils.isNotEmpty(page.getRecords()));
    }

    private QueryExportDataDTO setResultParam(QueryExportDataDTO result, List<Map<String, Object>> data) {
        ExportDataDTO exportDataDTO = new ExportDataDTO();
        exportDataDTO.setSheetName("辖区详情");
        // 页签字段
        exportDataDTO.setFieldMap(FIELD);
        // 页签数据
        exportDataDTO.setData(data);
        List<ExportDataDTO> sheets = new ArrayList<>();
        sheets.add(exportDataDTO);
        result.setSheets(sheets);
        return result;
    }
}
