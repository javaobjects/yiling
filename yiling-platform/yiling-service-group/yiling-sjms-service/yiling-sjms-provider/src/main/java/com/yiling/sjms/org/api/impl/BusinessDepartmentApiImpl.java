package com.yiling.sjms.org.api.impl;

import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.yiling.sjms.gb.service.BusinessDepartmentService;
import com.yiling.sjms.org.api.BusinessDepartmentApi;
import com.yiling.user.esb.dto.EsbOrganizationDTO;

/**
 * 事业部 API 实现
 *
 * @author: xuan.zhou
 * @date: 2023/3/3
 */
@DubboService
public class BusinessDepartmentApiImpl implements BusinessDepartmentApi {

    @Autowired
    BusinessDepartmentService businessDepartmentService;

    @Override
    public EsbOrganizationDTO getByEmpId(String empId) {
        Assert.notNull(empId, "参数empId不能为空");
        return businessDepartmentService.getByEmpId(empId);
    }

    @Override
    public Map<String, EsbOrganizationDTO> listByEmpIds(List<String> empIds) {
        return businessDepartmentService.listByEmpIds(empIds);
    }

    @Override
    public EsbOrganizationDTO getByJobId(Long jobId) {
        Assert.notNull(jobId, "参数jobId不能为空");
        return businessDepartmentService.getByJobId(jobId);
    }

    @Override
    public Map<Long, EsbOrganizationDTO> listByJobIds(List<Long> jobIds) {
        return businessDepartmentService.listByJobIds(jobIds);
    }

    @Override
    public EsbOrganizationDTO getByOrgId(Long orgId) {
        Assert.notNull(orgId, "参数orgId不能为空");
        return businessDepartmentService.getByOrgId(orgId);
    }

    @Override
    public Map<Long, EsbOrganizationDTO> listByOrgIds(List<Long> orgIds) {
        return businessDepartmentService.listByOrgIds(orgIds);
    }
}
