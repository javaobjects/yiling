package com.yiling.sjms.gb.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.yiling.dataflow.agency.dto.request.QueryCrmEnterpriseRelationShipPageListRequest;
import com.yiling.dataflow.agency.enums.AgencySupplyChainRoleEnum;
import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.api.CrmEnterpriseRelationShipApi;
import com.yiling.dataflow.crm.dto.CrmDepartmentAreaRelationDTO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseRelationShipDTO;
import com.yiling.sjms.gb.api.GbOrgManagerApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.user.esb.dto.EsbOrganizationDTO;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: shixing.sun
 * @date: 2021/12/7
 */
@Slf4j
@Service
public class RelationShipNotificationHandler {

    @DubboReference(timeout = 20 * 1000)
    CrmEnterpriseRelationShipApi crmEnterpriseRelationShipApi;

    @DubboReference(timeout = 10 * 1000)
    CrmEnterpriseApi crmEnterpriseApi;

    @DubboReference(timeout = 10 * 1000)
    EsbEmployeeApi esbEmployeeApi;

    @DubboReference(timeout = 10 * 1000)
    GbOrgManagerApi gbOrgManagerApi;

    /**
     * 开始更新三者关系备份表
     *
     * @param offset
     * @return
     */
    public boolean crmEnterpriseRelationShipBackup(Integer offset) {
        log.info("开始更新三者关系备份表" + offset);
        //备份月计算
        DateTime lastMonth = DateUtil.offsetMonth(new Date(), offset);
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
            page = crmEnterpriseRelationShipApi.getCrmRelationPage(shipPageListRequest);
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
            List<CrmEnterpriseDTO> crmEnterpriseList = crmEnterpriseApi.getCrmEnterpriseListById(new ArrayList<>(crmEnterpriseIds));
            // 获取员工信息
            List<EsbEmployeeDTO> employeeDTOS = esbEmployeeApi.listByJobIds(new ArrayList<>(jobIds));
            // 获取部门字段信息
            Map<Long, EsbOrganizationDTO> longEsbOrganizationDTOMap = gbOrgManagerApi.listByOrgIds(new ArrayList<>(jobIds));
            Map<Long, CrmEnterpriseDTO> crmEnterpriseDTOMap = new HashMap<>();
            Map<Long, EsbEmployeeDTO> esbEmployeeDTOMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(crmEnterpriseList)) {
                crmEnterpriseDTOMap = crmEnterpriseList.stream().collect(Collectors.toMap(CrmEnterpriseDTO::getId, one -> one, (k1, k2) -> k1));
            }
            if (CollectionUtils.isNotEmpty(employeeDTOS)) {
                esbEmployeeDTOMap = employeeDTOS.stream().collect(Collectors.toMap(EsbEmployeeDTO::getJobId, one -> one, (k1, k2) -> k1));
            }
            for (CrmEnterpriseRelationShipDTO relationShipDTO : enterpriseRelationShipDTOList) {
                CrmEnterpriseDTO crmEnterpriseDTO = crmEnterpriseDTOMap.get(relationShipDTO.getCrmEnterpriseId());
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
                }
                EsbEmployeeDTO esbEmployeeDTO = esbEmployeeDTOMap.get(relationShipDTO.getPostCode());
                if (ObjectUtil.isNotNull(esbEmployeeDTO)) {
                    relationShipDTO.setBusinessDepartment(esbEmployeeDTO.getYxDept());
                    relationShipDTO.setBusinessProvince(esbEmployeeDTO.getYxProvince());
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
            crmEnterpriseRelationShipApi.batchUpdateById(enterpriseRelationShipDTOList, tableSuffix);
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
}
