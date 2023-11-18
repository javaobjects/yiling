package com.yiling.user.enterprise.service.impl;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yiling.framework.common.base.BaseDO;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.user.enterprise.dao.EnterpriseEmployeeDepartmentMapper;
import com.yiling.user.enterprise.dto.EnterpriseEmployeeDepartmentDTO;
import com.yiling.user.enterprise.entity.EnterpriseEmployeeDO;
import com.yiling.user.enterprise.entity.EnterpriseEmployeeDepartmentDO;
import com.yiling.user.enterprise.service.EnterpriseEmployeeDepartmentService;
import com.yiling.user.enterprise.service.EnterpriseEmployeeService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;

/**
 * <p>
 * 企业员工所属部门信息 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-11-05
 */
@Service
public class EnterpriseEmployeeDepartmentServiceImpl extends BaseServiceImpl<EnterpriseEmployeeDepartmentMapper, EnterpriseEmployeeDepartmentDO> implements EnterpriseEmployeeDepartmentService {

    @Autowired
    private EnterpriseEmployeeService enterpriseEmployeeService;

    @Override
    public List<EnterpriseEmployeeDepartmentDO> listByEmployeeId(Long employeeId) {
        LambdaQueryWrapper<EnterpriseEmployeeDepartmentDO> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper
                .eq(EnterpriseEmployeeDepartmentDO::getEmployeeId, employeeId)
                .orderByAsc(EnterpriseEmployeeDepartmentDO::getId);

        List<EnterpriseEmployeeDepartmentDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }

        return list;
    }

    @Override
    public List<EnterpriseEmployeeDepartmentDO> listByDepartmentId(Long departmentId) {
        LambdaQueryWrapper<EnterpriseEmployeeDepartmentDO> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper
                .eq(EnterpriseEmployeeDepartmentDO::getDepartmentId, departmentId)
                .orderByAsc(EnterpriseEmployeeDepartmentDO::getId);

        List<EnterpriseEmployeeDepartmentDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }

        return list;
    }

    @Override
    public List<Long> listUserIdsByDepartmentIds(List<Long> departmentIds) {
        if (CollUtil.isEmpty(departmentIds)) {
            return ListUtil.empty();
        }

        LambdaQueryWrapper<EnterpriseEmployeeDepartmentDO> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper
                .in(EnterpriseEmployeeDepartmentDO::getDepartmentId, departmentIds)
                .orderByAsc(EnterpriseEmployeeDepartmentDO::getId);

        List<EnterpriseEmployeeDepartmentDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }

        return list.stream().map(EnterpriseEmployeeDepartmentDO::getUserId).distinct().collect(Collectors.toList());
    }

    @Override
    public List<Long> listByUser(Long eid, Long userId) {
        LambdaQueryWrapper<EnterpriseEmployeeDepartmentDO> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper
                .eq(EnterpriseEmployeeDepartmentDO::getEid, eid)
                .eq(EnterpriseEmployeeDepartmentDO::getUserId, userId)
                .orderByAsc(EnterpriseEmployeeDepartmentDO::getId);
        List<EnterpriseEmployeeDepartmentDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return list.stream().map(EnterpriseEmployeeDepartmentDO::getDepartmentId).distinct().collect(Collectors.toList());
    }

    @Override
    public Map<Long, List<Long>> listByEmployeeIds(List<Long> employeeIds) {
        LambdaQueryWrapper<EnterpriseEmployeeDepartmentDO> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper
                .in(EnterpriseEmployeeDepartmentDO::getEmployeeId, employeeIds)
                .orderByAsc(EnterpriseEmployeeDepartmentDO::getId);

        List<EnterpriseEmployeeDepartmentDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return MapUtil.empty();
        }

        Map<Long, List<Long>> map = list.stream().collect(Collectors.groupingBy(EnterpriseEmployeeDepartmentDO::getEmployeeId,
            Collectors.mapping(EnterpriseEmployeeDepartmentDO::getDepartmentId, Collectors.toList())));
        return map;
    }

    public boolean bindEmployeeDepartments(Long employeeId, List<Long> departmentIds, Long opUserId) {
        if (CollUtil.isEmpty(departmentIds)) {
            return true;
        }

        EnterpriseEmployeeDO enterpriseEmployeeDO = enterpriseEmployeeService.getById(employeeId);

        List<EnterpriseEmployeeDepartmentDO> list = CollUtil.newArrayList();
        departmentIds.forEach(e -> {
            EnterpriseEmployeeDepartmentDO entity = new EnterpriseEmployeeDepartmentDO();
            entity.setEmployeeId(employeeId);
            entity.setEid(enterpriseEmployeeDO.getEid());
            entity.setUserId(enterpriseEmployeeDO.getUserId());
            entity.setDepartmentId(e);
            entity.setOpUserId(opUserId);
            list.add(entity);
        });

        int n = this.baseMapper.bindEmployeeDepartments(list);
        return n > 0;
    }

    public boolean unbindEmployeeDepartments(Long employeeId, List<Long> departmentIds, Long opUserId) {
        if (CollUtil.isEmpty(departmentIds)) {
            return true;
        }

        QueryWrapper<EnterpriseEmployeeDepartmentDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(EnterpriseEmployeeDepartmentDO::getEmployeeId, employeeId)
                .in(EnterpriseEmployeeDepartmentDO::getDepartmentId, departmentIds);

        EnterpriseEmployeeDepartmentDO entity = new EnterpriseEmployeeDepartmentDO();
        entity.setOpUserId(opUserId);

        return this.batchDeleteWithFill(entity, queryWrapper) > 0;
    }

    @Override
    public boolean saveEmployeeDepartments(Long employeeId, List<Long> departmentIds, Long opUserId) {
        List<EnterpriseEmployeeDepartmentDO> enterpriseEmployeeDepartmentDOList = this.listByEmployeeId(employeeId);
        if (CollUtil.isEmpty(departmentIds)) {
            if (CollUtil.isEmpty(enterpriseEmployeeDepartmentDOList)) {
                return true;
            }

            List<Long> employeeDepartmentIds = enterpriseEmployeeDepartmentDOList.stream().map(EnterpriseEmployeeDepartmentDO::getDepartmentId).distinct().collect(Collectors.toList());
            return this.unbindEmployeeDepartments(employeeId, employeeDepartmentIds, opUserId);
        }

        if (CollUtil.isEmpty(enterpriseEmployeeDepartmentDOList)) {
            return this.bindEmployeeDepartments(employeeId, departmentIds, opUserId);
        }

        List<Long> employeeDepartmentIds = enterpriseEmployeeDepartmentDOList.stream().map(EnterpriseEmployeeDepartmentDO::getDepartmentId).distinct().collect(Collectors.toList());

        // 移除
        List<Long> removeDepartmentIds = employeeDepartmentIds.stream().filter(e -> !departmentIds.contains(e)).distinct().collect(Collectors.toList());
        this.unbindEmployeeDepartments(employeeId, removeDepartmentIds, opUserId);

        // 新增
        List<Long> addDepartmentIds = departmentIds.stream().filter(e -> !employeeDepartmentIds.contains(e)).distinct().collect(Collectors.toList());
        this.bindEmployeeDepartments(employeeId, addDepartmentIds, opUserId);

        return true;
    }

    @Override
    public Map<Long, List<EnterpriseEmployeeDepartmentDO>> getDepartmentByEidUser(Long eid, List<Long> userIdList) {
        Map<Long, List<EnterpriseEmployeeDepartmentDO>> map = MapUtil.newHashMap();
        if (Objects.isNull(eid) || CollUtil.isEmpty(userIdList)) {
            return map;
        }

        LambdaQueryWrapper<EnterpriseEmployeeDepartmentDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EnterpriseEmployeeDepartmentDO::getEid, eid);
        wrapper.in(EnterpriseEmployeeDepartmentDO::getUserId, userIdList);
        wrapper.orderByDesc(EnterpriseEmployeeDepartmentDO::getId);
        List<EnterpriseEmployeeDepartmentDO> employeeDepartmentDOList = this.list(wrapper);
        map = employeeDepartmentDOList.stream().sorted(Comparator.comparing(EnterpriseEmployeeDepartmentDO::getId).reversed()).collect(Collectors.groupingBy(EnterpriseEmployeeDepartmentDO::getUserId));

        return map;
    }
}
