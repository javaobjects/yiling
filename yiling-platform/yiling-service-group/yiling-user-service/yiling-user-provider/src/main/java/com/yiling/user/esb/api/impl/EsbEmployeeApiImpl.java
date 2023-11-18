package com.yiling.user.esb.api.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.user.esb.dto.EsbOrganizationDTO;
import com.yiling.user.esb.dto.request.QueryEsbEmployeePageListRequest;
import com.yiling.user.esb.dto.request.SaveOrUpdateEsbEmployeeRequest;
import com.yiling.user.esb.entity.EsbEmployeeDO;
import com.yiling.user.esb.service.EsbEmployeeService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;

/**
 * ESB人员 API 实现
 *
 * @author: xuan.zhou
 * @date: 2022/11/24
 */
@DubboService
public class EsbEmployeeApiImpl implements EsbEmployeeApi {

    @Autowired
    EsbEmployeeService esbEmployeeService;

    @Override
    public Page<EsbEmployeeDTO> pageList(QueryEsbEmployeePageListRequest request) {
        return PojoUtils.map(esbEmployeeService.pageList(request), EsbEmployeeDTO.class);
    }

    @Override
    public EsbEmployeeDTO getByEmpId(String empId) {
        return PojoUtils.map(esbEmployeeService.getByEmpId(empId), EsbEmployeeDTO.class);
    }

    @Override
    public List<EsbEmployeeDTO> listByEmpIds(List<String> empIds) {
        return PojoUtils.map(esbEmployeeService.listByEmpIds(empIds), EsbEmployeeDTO.class);
    }

    @Override
    public List<EsbEmployeeDTO> listByDeptId(Long deptId) {
        return PojoUtils.map(esbEmployeeService.listByDeptId(deptId), EsbEmployeeDTO.class);
    }

    @Override
    public EsbEmployeeDTO getByDeptIdAndJobName(Long deptId, String jobName) {
        return PojoUtils.map(esbEmployeeService.getByDeptIdAndJobName(deptId, jobName), EsbEmployeeDTO.class);
    }

    @Override
    public Boolean saveOrUpdate(SaveOrUpdateEsbEmployeeRequest request) {
        return esbEmployeeService.saveOrUpdate(request);
    }

    @Override
    public boolean isProvinceManager(String empId) {
        return esbEmployeeService.isProvinceManager(empId);
    }

    @Override
    public EsbEmployeeDTO getByEmpIdOrJobId(String empId, String JobId,String tableSuffix) {
        return PojoUtils.map(esbEmployeeService.getByEmpIdOrJobId(empId,JobId,tableSuffix), EsbEmployeeDTO.class);
    }

    @Override
    public List<EsbEmployeeDTO> listByJobIds(List<Long> jobIds) {
        return PojoUtils.map(esbEmployeeService.listByJobIds(jobIds), EsbEmployeeDTO.class);
    }


    @Override
    public List<EsbEmployeeDTO> getEmpInfoByName(String name) {
        return PojoUtils.map(esbEmployeeService.getEmpInfoByName(name), EsbEmployeeDTO.class);
    }

    @Override
    public List<EsbEmployeeDTO> listByJobIdsForAgency(List<Long> jobIds) {
        return PojoUtils.map(esbEmployeeService.listByJobIdsForAgency(jobIds), EsbEmployeeDTO.class);
    }

    @Override
    public Map<Long, EsbOrganizationDTO> listByOrgIds(List<Long> longs, List<Long> orgIds, String tableSuffix) {
        return esbEmployeeService.listByOrgIds(longs,orgIds,tableSuffix);
    }

    @Override
    public List<EsbEmployeeDTO> listSuffixByJobIds(List<Long> jobIds, String tableSuffix) {
        return PojoUtils.map(esbEmployeeService.listSuffixByJobIds(jobIds, tableSuffix), EsbEmployeeDTO.class);
    }

    @Override
    public List<EsbEmployeeDTO> getProvincialManagerByYxDeptAndYxProvinceAndJobNames(String yxDept, String yxProvince, String yxArea, List<String> jobNamesList) {
        if (StringUtils.isEmpty(yxDept) || StringUtils.isEmpty(yxProvince)) {
            return ListUtil.empty();
        }
        Assert.notEmpty(jobNamesList, "参数 jobNames 不能为空");
        jobNamesList.forEach(o -> {Assert.notBlank(o, "参数 jobName 不能为空");});
        // 查询省区经理
        List<EsbEmployeeDO> managers = esbEmployeeService.getProvincialManagerByYxDeptAndYxProvinceAndJobNames(yxDept, yxProvince, yxArea, jobNamesList.get(0));
        if (CollUtil.isNotEmpty(managers)) {
            return PojoUtils.map(managers, EsbEmployeeDTO.class);
        }
        // 没有省区经理，再查询省区主管
        List<EsbEmployeeDO> supervisors = esbEmployeeService.getProvincialManagerByYxDeptAndYxProvinceAndJobNames(yxDept, yxProvince, yxArea, jobNamesList.get(1));
        if (CollUtil.isNotEmpty(supervisors)) {
            return PojoUtils.map(supervisors, EsbEmployeeDTO.class);
        }
        return ListUtil.empty();
    }

    @Override
    public List<EsbEmployeeDTO> getEsbEmployeeDTOList() {
        return PojoUtils.map(esbEmployeeService.getEsbEmployeeDTOList(), EsbEmployeeDTO.class);
    }
}
