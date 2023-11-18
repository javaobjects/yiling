package com.yiling.dataflow.crm.api.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.yiling.dataflow.utils.BackupUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.dataflow.agency.service.CrmDepartmentAreaRelationService;
import com.yiling.dataflow.backup.dto.request.AgencyBackRequest;
import com.yiling.dataflow.crm.api.CrmEnterpriseRelationPinchRunnerApi;
import com.yiling.dataflow.crm.dto.CrmDepartmentAreaRelationDTO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseRelationPinchRunnerDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmEnterpriseRelationPinchRunnerPageListRequest;
import com.yiling.dataflow.crm.dto.request.RemoveCrmEnterpriseRelationPinchRunnerRequest;
import com.yiling.dataflow.crm.dto.request.SaveOrUpdateCrmEnterpriseRelationPinchRunnerRequest;
import com.yiling.dataflow.crm.dto.request.UpdateCrmEnterpriseRelationShipPinchRunnerRequest;
import com.yiling.dataflow.crm.service.CrmEnterpriseRelationPinchRunnerService;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.dataflow.utils.BackupUtil;
import com.yiling.framework.common.exception.ServiceException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.user.esb.dto.EsbOrganizationDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: houjie.sun
 * @date: 2023/4/19
 */
@Slf4j
@DubboService
public class CrmEnterpriseRelationPinchRunnerApiImpl implements CrmEnterpriseRelationPinchRunnerApi {

    @Autowired
    private CrmEnterpriseRelationPinchRunnerService crmEnterpriseRelationPinchRunnerService;

    @Autowired
    private CrmDepartmentAreaRelationService crmDepartmentAreaRelationService;

    @DubboReference
    EsbEmployeeApi esbEmployeeApi;

    @Override
    public Page<CrmEnterpriseRelationPinchRunnerDTO> page(QueryCrmEnterpriseRelationPinchRunnerPageListRequest request) {
        return PojoUtils.map(crmEnterpriseRelationPinchRunnerService.page(request), CrmEnterpriseRelationPinchRunnerDTO.class);
    }

    @Override
    public CrmEnterpriseRelationPinchRunnerDTO getById(SjmsUserDatascopeBO userDatascopeBO, Long id) {
        if (ObjectUtil.isNull(id) || 0 == id.intValue()) {
            return null;
        }
        return PojoUtils.map(crmEnterpriseRelationPinchRunnerService.getById(userDatascopeBO, id), CrmEnterpriseRelationPinchRunnerDTO.class);
    }

    @Override
    public CrmEnterpriseRelationPinchRunnerDTO getByCrmEnterpriseIdAndCrmRelationShipId(Long crmEnterpriseId, Long crmRelationShipId) {
        Assert.notNull(crmEnterpriseId, "参数 crmEnterpriseId 不能为空");
        Assert.notNull(crmRelationShipId, "参数 crmRelationShipId 不能为空");
        return PojoUtils.map(crmEnterpriseRelationPinchRunnerService.getByCrmEnterpriseIdAndCrmRelationShipId(crmEnterpriseId, crmRelationShipId), CrmEnterpriseRelationPinchRunnerDTO.class);
    }

    @Override
    public List<CrmEnterpriseRelationPinchRunnerDTO> getByCrmEnterpriseIdAndRelationShipIds(Long crmEnterpriseId, List<Long> crmRelationShipIds, String tableSuffix) {
        if (ObjectUtil.isNull(crmEnterpriseId) && CollUtil.isEmpty(crmRelationShipIds)) {
            throw new ServiceException("crmEnterpriseId、crmRelationShipIds, 不能同时为空");
        }
        return PojoUtils.map(crmEnterpriseRelationPinchRunnerService.getByCrmEnterpriseIdAndRelationShipIds(crmEnterpriseId, crmRelationShipIds, tableSuffix), CrmEnterpriseRelationPinchRunnerDTO.class);
    }

    @Override
    public Long add(SaveOrUpdateCrmEnterpriseRelationPinchRunnerRequest request) {
        Assert.notNull(request, "参数 request 不能为空");
        Assert.notNull(request.getCrmEnterpriseId(), "参数 crmEnterpriseId 不能为空");
        Assert.notNull(request.getCrmSupplyChainRole(), "参数 crmSupplyChainRole 不能为空");
        Assert.notNull(request.getCrmProvinceCode(), "参数 crmProvinceCode 不能为空");
        Assert.notNull(request.getEnterpriseCersId(), "参数 enterpriseCersId 不能为空");
        Assert.notNull(request.getBusinessSuperiorPostCode(), "参数 superiorPostCode 不能为空");
        return crmEnterpriseRelationPinchRunnerService.add(request);
    }

    @Override
    public Boolean edit(SaveOrUpdateCrmEnterpriseRelationPinchRunnerRequest request) {
        Assert.notNull(request, "参数 request 不能为空");
        Assert.notNull(request.getId(), "参数 id 不能为空");
        Assert.notNull(request.getCrmEnterpriseId(), "参数 crmEnterpriseId 不能为空");
        Assert.notNull(request.getCrmSupplyChainRole(), "参数 crmSupplyChainRole 不能为空");
        Assert.notNull(request.getCrmProvinceCode(), "参数 crmProvinceCode 不能为空");
        Assert.notNull(request.getEnterpriseCersId(), "参数 enterpriseCersId 不能为空");
        Assert.notNull(request.getBusinessSuperiorPostCode(), "参数 superiorPostCode 不能为空");
        return crmEnterpriseRelationPinchRunnerService.edit(request);
    }

    @Override
    public Boolean remove(RemoveCrmEnterpriseRelationPinchRunnerRequest request) {
        Assert.notNull(request, "参数 request 不能为空");
        Assert.notNull(request.getId(), "参数 id 不能为空");
        return crmEnterpriseRelationPinchRunnerService.remove(request);
    }

    @Override
    public Integer removeByEnterpriseCersId(RemoveCrmEnterpriseRelationPinchRunnerRequest request) {
        Assert.notNull(request, "参数 request 不能为空");
        Assert.notEmpty(request.getEnterpriseCersIdList(), "参数 enterpriseCersIdList 不能为空");
        Assert.notNull(request.getOpUserId(), "参数 opUserId 不能为空");
        return crmEnterpriseRelationPinchRunnerService.removeByEnterpriseCersId(request);
    }

    @Override
    public Boolean relationShipPinchRunnerBackup(AgencyBackRequest request, List<Long> orgIds) {
        log.info("开始更新代跑三者关系备份表销量计入主管岗位信息");
        // 备份月计算
        DateTime lastMonth = DateUtil.offsetMonth(new Date(), request.getOffsetMonth());
        // 备份表后缀
        String tableSuffix = BackupUtil.generateTableSuffix(DateUtil.year(lastMonth), DateUtil.month(lastMonth));
        String tableName = "crm_enterprise_relation_pinch_runner_".concat(tableSuffix);
        log.info("开始更新代跑三者关系备份表销量计入主管岗位信息, 表名:{}", tableName);

        // 获取所有的岗位id，生产环境一共6263个。平均每个岗位id对应200条三者关系
        List<Long> businessSuperiorPostCodeList = crmEnterpriseRelationPinchRunnerService.getBusinessSuperiorPostCode();
        log.info("开始更新代跑三者关系备份表销量计入主管岗位信息, 代跑关系岗位id列表:{}", JSONUtil.toJsonStr(businessSuperiorPostCodeList));
        if (CollectionUtil.isEmpty(businessSuperiorPostCodeList)) {
            return true;
        }
        // 省区与业务省区对应关系
        List<CrmDepartmentAreaRelationDTO> crmDepartmentAreaRelationDTOS = PojoUtils.map(crmDepartmentAreaRelationService.list(), CrmDepartmentAreaRelationDTO.class);

        Date opTime = new Date();
        // 按照岗位id分批查询
        List<List<Long>> lists = Lists.partition(businessSuperiorPostCodeList, 10);
        for (List<Long> items : lists) {
            List<EsbEmployeeDTO> esbEmployeeDTOS = esbEmployeeApi.listSuffixByJobIds(items,tableSuffix);
            if (CollectionUtil.isEmpty(esbEmployeeDTOS)) {
                continue;
            }
            // ESB人员信息
            List<Long> deptIdList = esbEmployeeDTOS.stream().map(EsbEmployeeDTO::getDeptId).distinct().collect(Collectors.toList());
            Map<Long, EsbEmployeeDTO> esbEmployeeDTOMap = esbEmployeeDTOS.stream().collect(Collectors.toMap(EsbEmployeeDTO::getJobId, one -> one, (k1, k2) -> k1));
            // 部门
            Map<Long, EsbOrganizationDTO> esbOrganizationDTOMap = esbEmployeeApi.listByOrgIds(deptIdList, orgIds, tableSuffix);

            for (Long businessSuperiorPostCode : items) {
                EsbEmployeeDTO esbEmployeeDTO = esbEmployeeDTOMap.get(businessSuperiorPostCode);
                if (ObjectUtil.isNull(esbEmployeeDTO)) {
                    continue;
                }
                // 构建更新参数，根据销量计入主管岗位代码 更新岗位信息
                UpdateCrmEnterpriseRelationShipPinchRunnerRequest pinchRunnerRequest = buildUpdatePinchRunnerBackupRequest(businessSuperiorPostCode, esbEmployeeDTO, crmDepartmentAreaRelationDTOS, esbOrganizationDTOMap, opTime);
                if (ObjectUtil.isNotNull(pinchRunnerRequest)) {
                    log.info("开始更新代跑三者关系备份表销量计入主管岗位信息, 更新岗位参数:{}", JSONUtil.toJsonStr(pinchRunnerRequest));
                    crmEnterpriseRelationPinchRunnerService.updateBackUpBatchByBusinessSuperiorPostCode(pinchRunnerRequest, tableName);
                }
            }
        }
        log.info("代跑三者关系备份表更新主管岗位信息完毕");
        return true;
    }

    private UpdateCrmEnterpriseRelationShipPinchRunnerRequest buildUpdatePinchRunnerBackupRequest(Long postCode, EsbEmployeeDTO esbEmployeeDTO, List<CrmDepartmentAreaRelationDTO> crmDepartmentAreaRelationDTOS, Map<Long, EsbOrganizationDTO> esbOrganizationDTOMap, Date opTime) {
        UpdateCrmEnterpriseRelationShipPinchRunnerRequest pinchRunnerRequest = new UpdateCrmEnterpriseRelationShipPinchRunnerRequest();
        pinchRunnerRequest.setOpTime(opTime);
        // 销量计入主管岗位代码
        pinchRunnerRequest.setBusinessSuperiorPostCode(postCode);
        // 销量计入主管岗位名称
        pinchRunnerRequest.setBusinessSuperiorPostName(StrUtil.isBlank(esbEmployeeDTO.getJobName()) ? "" : esbEmployeeDTO.getJobName());
        // 销量计入主管工号
        pinchRunnerRequest.setBusinessSuperiorCode(StrUtil.isBlank(esbEmployeeDTO.getSuperior()) ? "" : esbEmployeeDTO.getSuperior());
        // 销量计入主管姓名
        pinchRunnerRequest.setBusinessSuperiorName(StrUtil.isBlank(esbEmployeeDTO.getSuperiorName()) ? "" : esbEmployeeDTO.getSuperiorName());
        // 销量计入业务区域代码
        pinchRunnerRequest.setBusinessSuperiorAreaCode(ObjectUtil.isNull(esbEmployeeDTO.getDeptId()) ? "" : esbEmployeeDTO.getDeptId() + "");
        // 销量计入业务区域
        pinchRunnerRequest.setBusinessSuperiorArea(StrUtil.isBlank(esbEmployeeDTO.getYxArea()) ? "" : esbEmployeeDTO.getYxArea());
        // 销量计入业务部门
        pinchRunnerRequest.setBusinessSuperiorDepartment(StrUtil.isBlank(esbEmployeeDTO.getYxDept()) ? "" : esbEmployeeDTO.getYxDept());
        // 销量计入业务省区
        pinchRunnerRequest.setBusinessSuperiorProvince(StrUtil.isBlank(esbEmployeeDTO.getYxProvince()) ? "" : esbEmployeeDTO.getYxProvince());
        // 销量计入部门，通过 部门id 获取部门
        EsbOrganizationDTO esbOrganizationDTO = esbOrganizationDTOMap.get(esbEmployeeDTO.getDeptId());
        String superiorDepartment = "";
        if (ObjectUtil.isNotEmpty(esbOrganizationDTO)) {
            superiorDepartment = StrUtil.isBlank(esbOrganizationDTO.getOrgName()) ? "" : esbOrganizationDTO.getOrgName();
        }
        pinchRunnerRequest.setSuperiorDepartment(superiorDepartment);
        // 销量计入省区，通过 业务部门、业务省区 获取省区
        Optional<CrmDepartmentAreaRelationDTO> first = crmDepartmentAreaRelationDTOS.stream().filter(c -> StringUtils.equals(c.getDepartmentBusiness(), esbEmployeeDTO.getYxDept()) && StringUtils.equals(c.getProvincialAreaBusiness(), esbEmployeeDTO.getYxProvince())).findFirst();
        String provinceArea = first.isPresent() ? first.get().getProvincialArea() : "";
        pinchRunnerRequest.setSuperiorProvincial(provinceArea);
        // 省区经理，查不到则为空，查到多个则报错提示，这里进行跳过不更新
        List<String> jobNamesList = ListUtil.toList("省区经理", "省区主管");
        List<EsbEmployeeDTO> provincialManagerList = esbEmployeeApi.getProvincialManagerByYxDeptAndYxProvinceAndJobNames(esbEmployeeDTO.getYxDept(), esbEmployeeDTO.getYxProvince(), null, jobNamesList);
        if (CollUtil.isNotEmpty(provincialManagerList) && provincialManagerList.size() == 1) {
            EsbEmployeeDTO provincialManager = provincialManagerList.get(0);
            pinchRunnerRequest.setProvincialManagerPostCode(provincialManager.getJobId());
            pinchRunnerRequest.setProvincialManagerPostName(provincialManager.getJobName());
            pinchRunnerRequest.setProvincialManagerCode(provincialManager.getEmpId());
            pinchRunnerRequest.setProvincialManagerName(provincialManager.getEmpName());
        }
        return pinchRunnerRequest;
    }
}
