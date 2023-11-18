package com.yiling.export.export.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.api.CrmEnterpriseRelationShipApi;
import com.yiling.dataflow.crm.api.CrmManorApi;
import com.yiling.dataflow.crm.api.CrmManorRepresentativeApi;
import com.yiling.dataflow.crm.dto.CrmManorRepresentativeDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmManorRepresentativePageRequest;
import com.yiling.export.export.bo.ExportCrmManorBO;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.org.api.BusinessDepartmentApi;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.api.EsbOrganizationApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.user.esb.dto.EsbOrganizationDTO;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
@Service("crmManorRepresentativeExportService")
@Slf4j
public class CrmManorRepresentativeExportServiceImpl implements BaseExportQueryDataService<QueryCrmManorRepresentativePageRequest> {
    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();
    @DubboReference(timeout = 10 * 1000)
    private CrmManorRepresentativeApi crmManorRepresentativeApi;
    @DubboReference
    private UserApi userApi;
    @DubboReference(timeout = 10 * 1000)
    private CrmEnterpriseRelationShipApi crmEnterpriseRelationShipApi ;
    @DubboReference(timeout = 10 * 1000)
    EsbEmployeeApi esbEmployeeApi;

    @DubboReference(timeout = 10 * 1000)
    EsbOrganizationApi esbOrganizationApi;
    @DubboReference
    BusinessDepartmentApi businessDepartmentApi;

    static {
        FIELD.put("manorNo", "辖区编码");//辖区编码
        FIELD.put("name", "辖区名称");//辖区名称
        FIELD.put("representativePostCode", "岗位编码");//机构数量
        FIELD.put("representativePostName", "岗位名称");//品种数量（去重）
        FIELD.put("representativeCode", "业务代表工号");//最后操作时间
        FIELD.put("representativeName", "业务代表姓名");//操作人
        FIELD.put("businessDepartment", "业务部门");//操作人
        FIELD.put("businessProvince", "业务省区");//操作人
        FIELD.put("businessArea", "业务区域");//操作人
        FIELD.put("updateTime", "最后操作时间");//操作人
        FIELD.put("updateUserName", "操作人");//操作人
    }


    @Override
    public QueryExportDataDTO queryData(QueryCrmManorRepresentativePageRequest request) {
        // 需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();
        List<Map<String, Object>> data = new ArrayList<>();
        //操作数据
        buildExportData(request, data);
        //设置结果
        return setResultParam(result, data);
    }

    @Override
    public QueryCrmManorRepresentativePageRequest getParam(Map<String, Object> map) {
        QueryCrmManorRepresentativePageRequest request = PojoUtils.map(map, QueryCrmManorRepresentativePageRequest.class);
        return request;
    }

    // 校验数据权限、查询数据
    void buildExportData(QueryCrmManorRepresentativePageRequest request, List<Map<String, Object>> data) {
        Page<CrmManorRepresentativeDTO> page = null;
        int current = 1;
        do {
            page=null;
            request.setCurrent(current);
            request.setSize(500);
            //数据处理部分
            //  查询导出的数据填入data
            //供应商角色
            page = crmManorRepresentativeApi.pageList(request);
            List<CrmManorRepresentativeDTO> records = page.getRecords();
            if (CollUtil.isEmpty(records)) {
                continue;
            }
            List<Long> userIds = page.getRecords().stream().map(CrmManorRepresentativeDTO::getUpdateUser).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
            List<UserDTO> userDTOS = userApi.listByIds(userIds);
            //处理操作人名称处理
            Map<Long, UserDTO> userDTOMap = userDTOS.stream().collect(Collectors.toMap(UserDTO::getId, Function.identity()));
            buildEsbEmployInfo(page.getRecords(),false);
            //组装数据
            page.getRecords().forEach(item -> {
                //转化基本信息
                UserDTO crmHospitalDTO = userDTOMap.get(item.getUpdateUser());
                if (ObjectUtil.isNotEmpty(crmHospitalDTO)) {
                    item.setUpdateUserName(crmHospitalDTO.getName());
                }
                data.add(BeanUtil.beanToMap(item));
            });
            current = current + 1;
        } while (CollectionUtils.isNotEmpty(page.getRecords()));
    }

    private QueryExportDataDTO setResultParam(QueryExportDataDTO result, List<Map<String, Object>> data) {
        ExportDataDTO exportDataDTO = new ExportDataDTO();
        exportDataDTO.setSheetName("辖区代表");
        // 页签字段
        exportDataDTO.setFieldMap(FIELD);
        // 页签数据
        exportDataDTO.setData(data);
        List<ExportDataDTO> sheets = new ArrayList<>();
        sheets.add(exportDataDTO);
        result.setSheets(sheets);
        return result;
    }
    public void buildEsbEmployInfo(List<CrmManorRepresentativeDTO> crmEnterpriseDTOS, boolean needProvinceInfo) {
        // 用岗位id名字postcode找到人员信息，业务部门，省区，业务区域等
        List<Long> postCodes = crmEnterpriseDTOS.stream().filter(item->(Objects.nonNull(item.getRepresentativePostCode())&&
                item.getRepresentativePostCode()!=0L)).map(CrmManorRepresentativeDTO::getRepresentativePostCode).collect(Collectors.toList());
        if(CollectionUtil.isEmpty(postCodes)){
            return;
        }
        List<EsbEmployeeDTO> esbEmployeeDTOS = esbEmployeeApi.listByJobIds(postCodes);
        Map<Long, EsbEmployeeDTO> esbEmployeeDTOMap=new HashMap<>();
        Map<Long, EsbOrganizationDTO> esbOrganizationDTOMap=new HashMap<>();
        if(CollectionUtil.isNotEmpty(esbEmployeeDTOS)){
            esbEmployeeDTOMap = esbEmployeeDTOS.stream().collect(Collectors.toMap(EsbEmployeeDTO::getJobId,o -> o, (k1, k2) -> k1));
            List<Long> deptIds = esbEmployeeDTOS.stream().map(EsbEmployeeDTO::getDeptId).collect(Collectors.toList());
            if(CollectionUtil.isNotEmpty(deptIds)){
                esbOrganizationDTOMap = businessDepartmentApi.listByOrgIds(deptIds);
            }
        }
        Map<Long, EsbEmployeeDTO> finalEsbEmployeeDTOMap = esbEmployeeDTOMap;
        Map<Long, EsbOrganizationDTO> finalEsbOrganizationDTOMap = esbOrganizationDTOMap;
        // 用岗位id获取esbEmploy信息
        crmEnterpriseDTOS.forEach(item -> {
            Long postCode = item.getRepresentativePostCode();
            if (ObjectUtil.isNull(postCode) || postCode.equals(0L)) {
                return;
            }
            EsbEmployeeDTO esbEmployeeDTO = finalEsbEmployeeDTOMap.get(postCode);
            if (ObjectUtil.isNull(esbEmployeeDTO)) {
                return;
            }
            changeField(item, esbEmployeeDTO);
            // 获取上级主管岗位id和名称
            if(StringUtils.equals(esbEmployeeDTO.getDutyGredeId(),"1")){
                item.setDutyGredeId("1");
                List<String> jobNamesList = ListUtil.toList("地区经理", "占位");
                List<EsbEmployeeDTO> provincialManagerList = esbEmployeeApi.getProvincialManagerByYxDeptAndYxProvinceAndJobNames(esbEmployeeDTO.getYxDept(), esbEmployeeDTO.getYxProvince(), esbEmployeeDTO.getYxArea(), jobNamesList);
                if (CollUtil.isNotEmpty(provincialManagerList)) {
                    EsbEmployeeDTO esbEmployeeDTO1 = provincialManagerList.get(0);
                    item.setSuperiorJob(esbEmployeeDTO1.getJobId());
                    item.setSuperiorJobName(esbEmployeeDTO1.getJobName());
                }else{
                    item.setSuperiorJob(null);
                    item.setSuperiorJobName(null);
                    item.setSuperiorSupervisorName(null);
                    item.setSuperiorSupervisorCode(null);
                }
            }
            if(StringUtils.equals(esbEmployeeDTO.getDutyGredeId(),"2")){
                item.setDutyGredeId("2");
                item.setSuperiorSupervisorCode(esbEmployeeDTO.getEmpId());
                item.setSuperiorSupervisorName(esbEmployeeDTO.getEmpName());
                item.setSuperiorJobName(esbEmployeeDTO.getJobName());
                item.setSuperiorJob(esbEmployeeDTO.getJobId());
                item.setRepresentativeCode(null);
                item.setRepresentativeName(null);
                item.setRepresentativePostCode(null);
                item.setRepresentativePostName(null);
            }
            //通过循环获取部门。
            EsbOrganizationDTO organizationDTO= finalEsbOrganizationDTOMap.get(esbEmployeeDTO.getDeptId());
            if (ObjectUtil.isNotEmpty(organizationDTO)) {
                item.setDepartment(organizationDTO.getOrgName());
            }
            //通过部门，业务部门，业务省区获取省区
            String provinceArea = crmEnterpriseRelationShipApi.getProvinceAreaByThreeParms(esbEmployeeDTO.getYxDept(), esbEmployeeDTO.getYxProvince());
            item.setProvincialArea(provinceArea);
        });
    }
    public static void changeField(CrmManorRepresentativeDTO item, EsbEmployeeDTO esbEmployeeDTO) {
        if(ObjectUtil.isNull(esbEmployeeDTO)){
            return;
        }
        item.setBusinessDepartment(esbEmployeeDTO.getYxDept());
        item.setBusinessProvince(esbEmployeeDTO.getYxProvince());
        item.setBusinessArea(esbEmployeeDTO.getYxArea());
        item.setSuperiorSupervisorCode(esbEmployeeDTO.getSuperior());
        item.setSuperiorSupervisorName(esbEmployeeDTO.getSuperiorName());
        item.setRepresentativeCode(esbEmployeeDTO.getEmpId());
        item.setRepresentativeName(esbEmployeeDTO.getEmpName());
        item.setRepresentativePostCode(esbEmployeeDTO.getJobId());
        item.setRepresentativePostName(esbEmployeeDTO.getJobName());
    }
}
