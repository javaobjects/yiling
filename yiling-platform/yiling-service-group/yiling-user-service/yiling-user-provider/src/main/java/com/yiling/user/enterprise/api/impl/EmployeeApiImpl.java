package com.yiling.user.enterprise.api.impl;

import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.enterprise.api.EmployeeApi;
import com.yiling.user.enterprise.dto.EnterpriseEmployeeDTO;
import com.yiling.user.enterprise.dto.request.CreateEmployeeRequest;
import com.yiling.user.enterprise.dto.request.QueryEmployeePageListRequest;
import com.yiling.user.enterprise.dto.request.RemoveEmployeeRequest;
import com.yiling.user.enterprise.dto.request.UpdateEmployeeRequest;
import com.yiling.user.enterprise.dto.request.UpdateEmployeeStatusRequest;
import com.yiling.user.enterprise.entity.EnterpriseEmployeeDO;
import com.yiling.user.enterprise.service.EnterpriseEmployeeDepartmentService;
import com.yiling.user.enterprise.service.EnterpriseEmployeeService;
import com.yiling.user.system.bo.Staff;
import com.yiling.user.system.service.StaffService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;

/**
 * 企业员工 API 实现
 *
 * @author: xuan.zhou
 * @date: 2021/5/26
 */
@DubboService
public class EmployeeApiImpl implements EmployeeApi {

    @Autowired
    EnterpriseEmployeeService enterpriseEmployeeService;
    @Autowired
    StaffService staffService;
    @Autowired
    EnterpriseEmployeeDepartmentService enterpriseEmployeeDepartmentService;

    @Override
    public Page<EnterpriseEmployeeDTO> pageList(QueryEmployeePageListRequest request) {
        return enterpriseEmployeeService.pageList(request);
    }

    @Override
    public EnterpriseEmployeeDTO getById(Long id) {
        return PojoUtils.map(enterpriseEmployeeService.getById(id), EnterpriseEmployeeDTO.class);
    }

    @Override
    public List<EnterpriseEmployeeDTO> listByIds(List<Long> ids) {
        return PojoUtils.map(enterpriseEmployeeService.listByIds(ids), EnterpriseEmployeeDTO.class);
    }

    @Override
    public long create(CreateEmployeeRequest request) {
        return enterpriseEmployeeService.create(request);
    }

    @Override
    public boolean update(UpdateEmployeeRequest request) {
        return enterpriseEmployeeService.update(request);
    }

    @Override
    public boolean updateStatus(UpdateEmployeeStatusRequest request) {
        return enterpriseEmployeeService.updateStatus(request);
    }

    @Override
    public boolean remove(RemoveEmployeeRequest request) {
        return enterpriseEmployeeService.remove(request.getEid(), request.getUserId(), request.getOpUserId());
    }

    @Override
    public boolean exists(Long eid, Long userId) {
        return enterpriseEmployeeService.exists(eid, userId);
    }

    @Override
    public List<Staff> listAdminsByEid(Long eid) {
        List<EnterpriseEmployeeDO> enterpriseEmployeeDOList = enterpriseEmployeeService.listAdminsByEid(eid);
        if (CollUtil.isEmpty(enterpriseEmployeeDOList)) {
            return null;
        }

        List<Staff> staffList = CollUtil.newArrayList();
        enterpriseEmployeeDOList.forEach(enterpriseEmployeeDO -> {
            Staff staff = staffService.getById(enterpriseEmployeeDO.getUserId());
            staffList.add(staff);
        });

        return staffList;
    }

    @Override
    public boolean isAdmin(Long eid, Long userId) {
        return enterpriseEmployeeService.isAdmin(eid, userId);
    }

    @Override
    public Map<Long, Long> countByEids(List<Long> eids) {
        return enterpriseEmployeeService.countByEids(eids);
    }

    @Override
    public Integer countByDepartmentId(Long departmentId) {
        return enterpriseEmployeeService.countByDepartmentId(departmentId);
    }

    @Override
    public Map<Long, Long> countByDepartmentIds(List<Long> departmentIds) {
        return enterpriseEmployeeService.countByDepartmentIds(departmentIds);
    }

    @Override
    public Integer countByPositionId(Long positionId) {
        return enterpriseEmployeeService.countByPositionId(positionId);
    }

    @Override
    public Map<Long, Long> countByPositionIds(List<Long> positionIds) {
        return enterpriseEmployeeService.countByPositionIds(positionIds);
    }

    @Override
    public List<EnterpriseEmployeeDTO> listByEidUserIds(Long eid, List<Long> userIds) {
        List<EnterpriseEmployeeDO> list = enterpriseEmployeeService.listByEidUserIds(eid, userIds);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }

        return PojoUtils.map(list, EnterpriseEmployeeDTO.class);
    }

    @Override
    public List<EnterpriseEmployeeDTO> listByUserId(Long userId, EnableStatusEnum statusEnum) {
        List<EnterpriseEmployeeDO> list = enterpriseEmployeeService.listByUserId(userId, statusEnum);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return PojoUtils.map(list, EnterpriseEmployeeDTO.class);
    }

    @Override
    public List<EnterpriseEmployeeDTO> listByUserIdEids(Long userId, List<Long> eids, EnableStatusEnum statusEnum) {
        List<EnterpriseEmployeeDO> list = enterpriseEmployeeService.listByUserIdEids(userId, eids, statusEnum);
        return PojoUtils.map(list, EnterpriseEmployeeDTO.class);
    }

    @Override
    public List<EnterpriseEmployeeDTO> listByEid(Long eid, EnableStatusEnum statusEnum) {
        List<EnterpriseEmployeeDO> list = enterpriseEmployeeService.listByEid(eid, statusEnum);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return PojoUtils.map(list, EnterpriseEmployeeDTO.class);
    }

    @Override
    public EnterpriseEmployeeDTO getByEidUserId(Long eid, Long userId) {
        return PojoUtils.map(enterpriseEmployeeService.getByEidUserId(eid, userId), EnterpriseEmployeeDTO.class);
    }

    @Override
    public EnterpriseEmployeeDTO getByCode(Long eid, String code) {
        return PojoUtils.map(enterpriseEmployeeService.getByCode(eid, code), EnterpriseEmployeeDTO.class);
    }

    @Override
    public EnterpriseEmployeeDTO getParentInfo(Long eid, Long userId) {
        return PojoUtils.map(enterpriseEmployeeService.getParentInfo(eid, userId), EnterpriseEmployeeDTO.class);
    }

    @Override
    public List<Long> listDepartmentIdsByUser(Long eid, Long userId) {
        return enterpriseEmployeeDepartmentService.listByUser(eid, userId);
    }

    @Override
    public Map<Long, List<Long>> listDepartmentIdsByEmployeeIds(List<Long> employeeIds) {
        return enterpriseEmployeeDepartmentService.listByEmployeeIds(employeeIds);
    }
}
