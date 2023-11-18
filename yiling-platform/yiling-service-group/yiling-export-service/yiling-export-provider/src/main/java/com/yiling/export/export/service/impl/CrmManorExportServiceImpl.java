package com.yiling.export.export.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;
import com.yiling.dataflow.agency.api.CrmDepartProductGroupApi;
import com.yiling.dataflow.agency.api.CrmSupplierApi;
import com.yiling.dataflow.agency.dto.CrmDepartmentProductRelationDTO;
import com.yiling.dataflow.agency.dto.CrmEnterpriseNameDTO;
import com.yiling.dataflow.agency.dto.CrmSupplierDTO;
import com.yiling.dataflow.agency.dto.request.QueryCrmAgencyPageListRequest;
import com.yiling.dataflow.agency.dto.request.QueryDepartProductGroupByUploadNameRequest;
import com.yiling.dataflow.agency.enums.CrmBaseSupplierInfoEnum;
import com.yiling.dataflow.agency.enums.CrmGeneralMedicineLevelEnum;
import com.yiling.dataflow.agency.enums.CrmGeneralMedicineSaleTypeEnum;
import com.yiling.dataflow.agency.enums.CrmPatentAgreementTypeEnum;
import com.yiling.dataflow.agency.enums.CrmSupplierAttributeEnum;
import com.yiling.dataflow.agency.enums.CrmSupplierSaleTypeEnum;
import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.api.CrmEnterpriseRelationManorApi;
import com.yiling.dataflow.crm.api.CrmEnterpriseRelationShipApi;
import com.yiling.dataflow.crm.api.CrmGoodsCategoryApi;
import com.yiling.dataflow.crm.api.CrmManorApi;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseRelationPostDTO;
import com.yiling.dataflow.crm.dto.CrmManorDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmManorPageRequest;
import com.yiling.dataflow.crm.dto.request.QueryCrmManorRepresentativePageRequest;
import com.yiling.dataflow.sjms.api.SjmsUserDatascopeApi;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.dataflow.sjms.enums.OrgDatascopeEnum;
import com.yiling.export.export.bo.AgencyDepartGrroupResolve;
import com.yiling.export.export.bo.ExportCrmManorBO;
import com.yiling.export.export.bo.ExportCrmSupplierInfoBO;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * DIH商业档案导出基于模版
 */
@Service("crmManorExportService")
@Slf4j
public class CrmManorExportServiceImpl implements BaseExportQueryDataService<QueryCrmManorPageRequest> {
    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();
    @DubboReference
    private CrmManorApi crmManorApi;
    @DubboReference
    UserApi userApi;

    static {
        FIELD.put("manorNo", "辖区编码");//辖区编码
        FIELD.put("name", "辖区名称");//辖区名称
        FIELD.put("agencyNum", "机构数量");//机构数量
        FIELD.put("categoryNum", "品种数量");//品种数量（去重）
        FIELD.put("updateTime", "最后操作时间");//最后操作时间
        FIELD.put("updateUserName", "操作人");//操作人
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
        Page<CrmManorDTO> page = null;
        int current = 1;
        do {
            request.setCurrent(current);
            request.setSize(500);
            //数据处理部分
            //  查询导出的数据填入data
            //供应商角色
            page = crmManorApi.pageList(request);
            List<CrmManorDTO> records = page.getRecords();
            if (CollUtil.isEmpty(records)) {
                continue;
            }
            List<Long> userIds = page.getRecords().stream().map(CrmManorDTO::getUpdateUser).collect(Collectors.toList());
            List<UserDTO> userDTOS = userApi.listByIds(userIds);
            //处理操作人名称处理
            Map<Long, UserDTO> userDTOMap = userDTOS.stream().collect(Collectors.toMap(UserDTO::getId, Function.identity()));

            //组装数据
            page.getRecords().forEach(item -> {
                UserDTO crmHospitalDTO = userDTOMap.get(item.getUpdateUser());
                if (ObjectUtil.isNotEmpty(crmHospitalDTO)) {
                    item.setUpdateUserName(crmHospitalDTO.getName());
                }
                //转化基本信息
                data.add(BeanUtil.beanToMap(item));
            });
            current = current + 1;
        } while (CollectionUtils.isNotEmpty(page.getRecords()));
    }

    private QueryExportDataDTO setResultParam(QueryExportDataDTO result, List<Map<String, Object>> data) {
        ExportDataDTO exportDataDTO = new ExportDataDTO();
        exportDataDTO.setSheetName("辖区");
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
