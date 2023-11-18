package com.yiling.dataflow.backup.service.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.agency.dto.request.QueryCrmEnterpriseRelationShipPageListRequest;
import com.yiling.dataflow.agency.enums.AgencySupplyChainRoleEnum;
import com.yiling.dataflow.agency.service.CrmHospitalService;
import com.yiling.dataflow.backup.dto.request.AgencyBackRequest;
import com.yiling.dataflow.backup.service.RelationShipBackupService;
import com.yiling.dataflow.crm.api.CrmEnterpriseRelationShipApi;
import com.yiling.dataflow.crm.dto.CrmDepartmentAreaRelationDTO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseRelationShipDTO;
import com.yiling.dataflow.crm.dto.request.UpdateCrmEnterpriseRelationShipRequest;
import com.yiling.dataflow.crm.entity.CrmEnterpriseRelationShipDO;
import com.yiling.dataflow.crm.service.CrmEnterpriseRelationShipService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.user.esb.dto.EsbOrganizationDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RelationShipBackupServiceImpl implements RelationShipBackupService {

    @DubboReference(timeout = 20 * 1000)
    CrmEnterpriseRelationShipApi crmEnterpriseRelationShipApi;

    @DubboReference
    EsbEmployeeApi esbEmployeeApi;

    @Autowired
    private CrmHospitalService crmHospitalService;

    @Autowired
    private CrmEnterpriseRelationShipService crmEnterpriseRelationShipService;

    @Override
    public Boolean RelationShipBackupByPostcode(AgencyBackRequest request, List<Long> orgId) {
        log.info("开始更新三者关系备份表" + request.getOffsetMonth());
        //备份月计算
        DateTime lastMonth = DateUtil.offsetMonth(new Date(), request.getOffsetMonth());
        // yyyy-MM
        String monthBackupStr = (Objects.isNull(request.getYearMonthFormat())||request.getYearMonthFormat().intValue()<=0) ?DateUtil.format(lastMonth, "yyyyMM"):request.getYearMonthFormat()+"";
        // 日期后缀
        String tableSuffix = String.format("wash_%s", monthBackupStr);
         // 获取所有的岗位id，生产环境一共6263个。平均每个岗位id对应200条三者关系
        List<Long> postCodeList = crmEnterpriseRelationShipApi.getCrmEnterprisePostCode();
        if (CollectionUtil.isEmpty(postCodeList)) {
            return true;
        }
        List<CrmDepartmentAreaRelationDTO> crmDepartmentAreaRelationDTOS = crmEnterpriseRelationShipApi.getAllData();
        // 分成多个集合,方便批量查询
        List<List<Long>> lists = splitList(postCodeList, 10);
        for (List<Long> items : lists) {
            List<EsbEmployeeDTO> esbEmployeeDTOS = esbEmployeeApi.listSuffixByJobIds(items,tableSuffix);
            if(CollectionUtil.isEmpty(esbEmployeeDTOS)){
                continue;
            }
            log.info("机构信息"+esbEmployeeDTOS.get(0));
            List<Long> collect = esbEmployeeDTOS.stream().map(EsbEmployeeDTO::getDeptId).distinct().collect(Collectors.toList());
            //获取部门字段
            Map<Long, EsbOrganizationDTO> longEsbOrganizationDTOMap = esbEmployeeApi.listByOrgIds(collect, orgId,tableSuffix);
            Map<Long, EsbEmployeeDTO> esbEmployeeDTOMap = esbEmployeeDTOS.stream().collect(Collectors.toMap(EsbEmployeeDTO::getJobId, one -> one, (k1, k2) -> k1));
            for(Long postCode:items){
                UpdateCrmEnterpriseRelationShipRequest relationShipRequest = new UpdateCrmEnterpriseRelationShipRequest();
                relationShipRequest.setPostCode(postCode);
                EsbEmployeeDTO esbEmployeeDTO = esbEmployeeDTOMap.get(postCode);
                if(ObjectUtil.isNotNull(esbEmployeeDTO)){
                    relationShipRequest.setBusinessDepartment(esbEmployeeDTO.getYxDept());
                    relationShipRequest.setBusinessProvince(esbEmployeeDTO.getYxProvince());
                    // 添加业务区办代码字段
                    relationShipRequest.setBusinessAreaCode(esbEmployeeDTO.getDeptId() + "");
                    relationShipRequest.setBusinessArea(esbEmployeeDTO.getYxArea());
                    if(StringUtils.equals(esbEmployeeDTO.getDutyGredeId(),"4")){
                        relationShipRequest.setPostName(esbEmployeeDTO.getJobName());
                        relationShipRequest.setSuperiorSupervisorCode(esbEmployeeDTO.getSuperior());
                        relationShipRequest.setSuperiorSupervisorName(esbEmployeeDTO.getSuperiorName());
                        relationShipRequest.setRepresentativeCode(esbEmployeeDTO.getEmpId());
                        relationShipRequest.setRepresentativeName(esbEmployeeDTO.getEmpName());
                        relationShipRequest.setDutyGredeId("4");
                    }
                    // 业务代表区别 业务代表，销售主管
                    if(StringUtils.equals(esbEmployeeDTO.getDutyGredeId(),"1")){
                        relationShipRequest.setPostName(esbEmployeeDTO.getJobName());
                        relationShipRequest.setRepresentativeCode(esbEmployeeDTO.getEmpId());
                        relationShipRequest.setRepresentativeName(esbEmployeeDTO.getEmpName());
                        relationShipRequest.setDutyGredeId("1");
                        List<String> jobNamesList = ListUtil.toList("地区经理", "占位");
                        List<EsbEmployeeDTO> provincialManagerList = esbEmployeeApi.getProvincialManagerByYxDeptAndYxProvinceAndJobNames(esbEmployeeDTO.getYxDept(), esbEmployeeDTO.getYxProvince(), esbEmployeeDTO.getYxArea(),jobNamesList);
                        if (CollUtil.isNotEmpty(provincialManagerList)) {
                            EsbEmployeeDTO esbEmployeeDTO1 = provincialManagerList.get(0);
                            relationShipRequest.setSuperiorJob(esbEmployeeDTO1.getJobId());
                            relationShipRequest.setSuperiorJobName(esbEmployeeDTO1.getJobName());
                            relationShipRequest.setSuperiorSupervisorCode(esbEmployeeDTO1.getEmpId());
                            relationShipRequest.setSuperiorSupervisorName(esbEmployeeDTO1.getEmpName());
                        }else {
                            relationShipRequest.setSuperiorJob(0L);
                            relationShipRequest.setSuperiorJobName("");
                            relationShipRequest.setSuperiorSupervisorCode("");
                            relationShipRequest.setSuperiorSupervisorName("");
                        }
                    }
                    if(StringUtils.equals(esbEmployeeDTO.getDutyGredeId(),"2")){
                        relationShipRequest.setSuperiorSupervisorCode(esbEmployeeDTO.getEmpId());
                        relationShipRequest.setSuperiorSupervisorName(esbEmployeeDTO.getEmpName());
                        relationShipRequest.setSuperiorJobName(esbEmployeeDTO.getJobName());
                        relationShipRequest.setSuperiorJob(esbEmployeeDTO.getJobId());
                        relationShipRequest.setRepresentativeCode("");
                        relationShipRequest.setRepresentativeName("");
                        //relationShipRequest.setPostCode(null);
                        relationShipRequest.setPostName("");
                        relationShipRequest.setDutyGredeId("2");
                    }

                    //通过部门，业务部门，业务省区获取省区
                    Optional<CrmDepartmentAreaRelationDTO> first = crmDepartmentAreaRelationDTOS.stream().filter(c -> StringUtils.equals(c.getDepartmentBusiness(), esbEmployeeDTO.getYxDept()) && StringUtils.equals(c.getProvincialAreaBusiness(), esbEmployeeDTO.getYxProvince())).findFirst();
                    String provinceArea = first.isPresent() ? first.get().getProvincialArea() : "";
                    relationShipRequest.setProvincialArea(provinceArea);
                    //获取部门。
                    EsbOrganizationDTO esbOrganizationDTO = longEsbOrganizationDTOMap.get(esbEmployeeDTO.getDeptId());
                    if (ObjectUtil.isNotEmpty(esbOrganizationDTO)) {
                        relationShipRequest.setDepartment(esbOrganizationDTO.getOrgName());
                    }
                    // 获取省区经理信息
                    List<String> jobNamesList = ListUtil.toList("省区经理", "省区主管");
                    List<EsbEmployeeDTO> provincialManagerList = esbEmployeeApi.getProvincialManagerByYxDeptAndYxProvinceAndJobNames(esbEmployeeDTO.getYxDept(), esbEmployeeDTO.getYxProvince(), null,jobNamesList);
                    if (CollUtil.isNotEmpty(provincialManagerList)) {
                        EsbEmployeeDTO provincialManager = provincialManagerList.get(0);
                        relationShipRequest.setProvincialManagerPostCode(provincialManager.getJobId());
                        relationShipRequest.setProvincialManagerPostName(provincialManager.getJobName());
                        relationShipRequest.setProvincialManagerCode(provincialManager.getEmpId());
                        relationShipRequest.setProvincialManagerName(provincialManager.getEmpName());
                    }else {
                        relationShipRequest.setProvincialManagerPostCode(0L);
                        relationShipRequest.setProvincialManagerPostName("");
                        relationShipRequest.setProvincialManagerCode("");
                        relationShipRequest.setProvincialManagerName("");
                    }
                }
                if (ObjectUtil.isNotNull(relationShipRequest) && ObjectUtil.isNotNull(esbEmployeeDTO)) {
                    log.info("relationShipRequest"+relationShipRequest);
                    setObject(relationShipRequest);
                    crmEnterpriseRelationShipService.updateBackUpBatchByPostCode(relationShipRequest, "crm_enterprise_relation_ship_"+tableSuffix);
                }
            }
        }
        log.info("三者关系表备份完毕");
        return true;

    }

    @Override
    public Boolean RelationShipBackup(AgencyBackRequest request, List<Long> orgId) {
        log.info("开始更新三者关系备份表" + request.getOffsetMonth());
        //备份月计算
        DateTime lastMonth = DateUtil.offsetMonth(new Date(), request.getOffsetMonth());
        // yyyy-MM
        String monthBackupStr = DateUtil.format(lastMonth, "yyyyMM");
        // 日期后缀
        String tableSuffix = String.format("wash_%s", monthBackupStr);
        QueryCrmEnterpriseRelationShipPageListRequest shipPageListRequest = new QueryCrmEnterpriseRelationShipPageListRequest();
        Page<CrmEnterpriseRelationShipDTO> page;
        int current = 1;
        do {
            log.info("更新备份表第" + current + "页。开始查找数据");
            shipPageListRequest.setCurrent(current);
            shipPageListRequest.setSize(500);
            page = crmHospitalService.getCrmRelationPage(shipPageListRequest);
            List<CrmEnterpriseRelationShipDTO> enterpriseRelationShipDTOList = page.getRecords();
            if (CollectionUtils.isEmpty(enterpriseRelationShipDTOList)) {
                continue;
            }
            log.info("找到多少条数据:" + enterpriseRelationShipDTOList.size());
            Set<Long> jobIds = new HashSet<>();
            Set<Long> crmEnterpriseIds = new HashSet<>();
            for (CrmEnterpriseRelationShipDTO relationShipDTO : enterpriseRelationShipDTOList) {
                jobIds.add(relationShipDTO.getPostCode());
                crmEnterpriseIds.add(relationShipDTO.getCrmEnterpriseId());
            }
            // 一共200条数据，全部查出来
            List<CrmDepartmentAreaRelationDTO> crmDepartmentAreaRelationDTOS = crmEnterpriseRelationShipApi.getAllData();
            // 获取机构信息
            //List<CrmEnterpriseDTO> crmEnterpriseList = crmEnterpriseService.getCrmEnterpriseListById(new ArrayList<>(crmEnterpriseIds));
            // 获取员工信息
            List<EsbEmployeeDTO> employeeDTOS = esbEmployeeApi.listByJobIds(new ArrayList<>(jobIds));
            List<Long> collect = employeeDTOS.stream().map(c -> c.getDeptId()).collect(Collectors.toList());
            // 获取部门字段信息
            Map<Long, EsbOrganizationDTO> longEsbOrganizationDTOMap = esbEmployeeApi.listByOrgIds(new ArrayList<>(collect), orgId,null);
            Map<Long, CrmEnterpriseDTO> crmEnterpriseDTOMap = new HashMap<>();
            Map<Long, EsbEmployeeDTO> esbEmployeeDTOMap = new HashMap<>();
           /* if (CollectionUtils.isNotEmpty(crmEnterpriseList)) {
                crmEnterpriseDTOMap = crmEnterpriseList.stream().collect(Collectors.toMap(CrmEnterpriseDTO::getId, one -> one, (k1, k2) -> k1));
            }*/
            if (CollectionUtils.isNotEmpty(employeeDTOS)) {
                esbEmployeeDTOMap = employeeDTOS.stream().collect(Collectors.toMap(EsbEmployeeDTO::getJobId, one -> one, (k1, k2) -> k1));
            }
            for (CrmEnterpriseRelationShipDTO relationShipDTO : enterpriseRelationShipDTOList) {
               /* CrmEnterpriseDTO crmEnterpriseDTO = crmEnterpriseDTOMap.get(relationShipDTO.getCrmEnterpriseId());
                if (Objects.nonNull(crmEnterpriseDTO)) {
                    relationShipDTO.setSupplyChainRole(getSupplyChainRole(crmEnterpriseDTO.getSupplyChainRole()));
                    relationShipDTO.setSupplyChainRoleType(crmEnterpriseDTO.getSupplyChainRole());
                    relationShipDTO.setCustomerCode(crmEnterpriseDTO.getCode());
                    relationShipDTO.setCustomerName(crmEnterpriseDTO.getName());
                    relationShipDTO.setProvince(crmEnterpriseDTO.getProvinceName());
                    relationShipDTO.setCityCode(crmEnterpriseDTO.getCityName());
                    relationShipDTO.setCity(crmEnterpriseDTO.getCityCode());
                    relationShipDTO.setDistrictCountyCode(crmEnterpriseDTO.getRegionCode());
                    relationShipDTO.setDistrictCounty(crmEnterpriseDTO.getRegionName());
                }*/
                EsbEmployeeDTO esbEmployeeDTO = esbEmployeeDTOMap.get(relationShipDTO.getPostCode());
                if (ObjectUtil.isNotNull(esbEmployeeDTO)) {
                    relationShipDTO.setBusinessDepartment(esbEmployeeDTO.getYxDept());
                    relationShipDTO.setBusinessProvince(esbEmployeeDTO.getYxProvince());
                    // 添加业务区办代码字段
                    relationShipDTO.setBusinessAreaCode(esbEmployeeDTO.getDeptId() + "");
                    relationShipDTO.setBusinessArea(esbEmployeeDTO.getYxArea());
                    relationShipDTO.setSuperiorSupervisorCode(esbEmployeeDTO.getSuperior());
                    relationShipDTO.setSuperiorSupervisorName(esbEmployeeDTO.getSuperiorName());
                    relationShipDTO.setRepresentativeCode(esbEmployeeDTO.getEmpId());
                    relationShipDTO.setRepresentativeName(esbEmployeeDTO.getEmpName());
                    relationShipDTO.setPostName(esbEmployeeDTO.getJobName());
                    //通过部门，业务部门，业务省区获取省区
                    Optional<CrmDepartmentAreaRelationDTO> first = crmDepartmentAreaRelationDTOS.stream().filter(item -> StringUtils.equals(item.getDepartmentBusiness(), esbEmployeeDTO.getYxDept()) && StringUtils.equals(item.getProvincialAreaBusiness(), esbEmployeeDTO.getYxProvince())).findFirst();
                    String provinceArea = first.isPresent() ? first.get().getProvincialArea() : "";
                    relationShipDTO.setProvincialArea(provinceArea);
                }
                //获取部门。
                EsbOrganizationDTO esbOrganizationDTO = longEsbOrganizationDTOMap.get(relationShipDTO.getPostCode());
                if (ObjectUtil.isNotEmpty(esbOrganizationDTO)) {
                    relationShipDTO.setDepartment(esbOrganizationDTO.getOrgName());
                }
            }
            current = current + 1;
            log.info("更新备份表第" + current + "页。查找数据结束，开始更新");
            log.info("准备更新数据库,一共多少条数据:" + enterpriseRelationShipDTOList.size());
            if (CollectionUtil.isNotEmpty(enterpriseRelationShipDTOList)) {
                List<CrmEnterpriseRelationShipDO> relationShipDOS = PojoUtils.map(enterpriseRelationShipDTOList, CrmEnterpriseRelationShipDO.class);
                crmEnterpriseRelationShipService.updateBackUpBatchById(relationShipDOS, tableSuffix);
            }
        } while (CollectionUtils.isNotEmpty(page.getRecords()));
        log.info("三者关系表备份完毕");
        return true;
    }

    private String getSupplyChainRole(Integer supplyChainRole) {
        AgencySupplyChainRoleEnum supplyChainRoleEnum = AgencySupplyChainRoleEnum.getByCode(supplyChainRole);
        if (null == supplyChainRoleEnum) {
            return "";
        }
        return supplyChainRoleEnum.getName();
    }

    /**
     * 分割集合工具类
     */
    public static <T> List<List<T>> splitList(List<T> list, int splitSize) {
        //判断集合是否为空
        if (CollectionUtils.isEmpty(list)){
            return Collections.emptyList();
        }
        //计算分割后的大小
        int maxSize = (list.size() + splitSize - 1) / splitSize;
        //开始分割
        return Stream.iterate(0, n -> n + 1)
                .limit(maxSize)
                .parallel()
                .map(a -> list.parallelStream().skip(a * splitSize).limit(splitSize).collect(Collectors.toList()))
                .filter(b -> !b.isEmpty())
                .collect(Collectors.toList());
    }

    private static Object setObject(Object object)  {
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            String str = field.getName().substring(0, 1).toUpperCase();
            String get = "get" + str + field.getName().substring(1);
            Method method = null;
            try {
                method = object.getClass().getMethod(get);
                if (method.invoke(object) == null||method.invoke(object).equals("")) {
                    field.setAccessible(true);
                    field.set(object, setValue(field));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return object;
    }

    private static Object setValue(Field field) {
        if (field.getType().equals(Float.class) || field.getType().equals(float.class)) {
            return 0f;
        } else if (field.getType().equals(double.class) || field.getType().equals(Double.class)) {
            return 0d;
        } else if (field.getType().equals(String.class)) {
            return "";
        } else if (field.getType().equals(Long.class) || field.getType().equals(long.class)) {
            return 0l;
        } else if (field.getType().equals(BigDecimal.class)) {
            return new BigDecimal("0");
        }
        return null;
    }
}
