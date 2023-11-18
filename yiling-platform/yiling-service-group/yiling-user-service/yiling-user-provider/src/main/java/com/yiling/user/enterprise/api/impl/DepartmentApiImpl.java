package com.yiling.user.enterprise.api.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.enterprise.api.DepartmentApi;
import com.yiling.user.enterprise.dto.EnterpriseDepartmentDTO;
import com.yiling.user.enterprise.dto.EnterpriseEmployeeDepartmentDTO;
import com.yiling.user.enterprise.dto.request.AddDepartmentRequest;
import com.yiling.user.enterprise.dto.request.MoveDepartmentEmployeeRequest;
import com.yiling.user.enterprise.dto.request.QueryDepartmentPageListRequest;
import com.yiling.user.enterprise.dto.request.UpdateDepartmentRequest;
import com.yiling.user.enterprise.dto.request.UpdateDepartmentStatusRequest;
import com.yiling.user.enterprise.entity.EnterpriseDepartmentDO;
import com.yiling.user.enterprise.entity.EnterpriseEmployeeDepartmentDO;
import com.yiling.user.enterprise.service.EnterpriseDepartmentService;
import com.yiling.user.enterprise.service.EnterpriseEmployeeDepartmentService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.map.MapUtil;

/**
 * 部门
 * @author: ray
 * @date: 2021/5/27
 */
@DubboService
public class DepartmentApiImpl implements DepartmentApi {

    @Autowired
    private EnterpriseDepartmentService enterpriseDepartmentService;
    @Autowired
    private EnterpriseEmployeeDepartmentService enterpriseEmployeeDepartmentService;

    @Override
    public EnterpriseDepartmentDTO getById(Long id) {
        return PojoUtils.map(enterpriseDepartmentService.getById(id), EnterpriseDepartmentDTO.class);
    }

    @Override
    public List<EnterpriseDepartmentDTO> listByIds(List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return ListUtil.empty();
        }

        List<EnterpriseDepartmentDO> list = enterpriseDepartmentService.listByIds(ids);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }

        return PojoUtils.map(list, EnterpriseDepartmentDTO.class);
    }

    @Override
    public Boolean add(AddDepartmentRequest request) {
        return enterpriseDepartmentService.add(request);
    }

    @Override
    public Boolean update(UpdateDepartmentRequest request) {
        return enterpriseDepartmentService.update(request);
    }

    @Override
    public Boolean updateStatus(UpdateDepartmentStatusRequest request) {
        return enterpriseDepartmentService.updateStatus(request);
    }

    @Override
    public Page<EnterpriseDepartmentDTO> pageList(QueryDepartmentPageListRequest request) {
        Page<EnterpriseDepartmentDO> page = enterpriseDepartmentService.pageList(request);
        return PojoUtils.map(page, EnterpriseDepartmentDTO.class);
    }

    @Override
    public Boolean moveEmployee(MoveDepartmentEmployeeRequest request) {
        return enterpriseDepartmentService.moveEmployee(request);
    }

    @Override
    public List<EnterpriseDepartmentDTO> listByParentId(Long eid, Long parentId) {
        return PojoUtils.map(enterpriseDepartmentService.listByParentId(eid, parentId), EnterpriseDepartmentDTO.class);
    }

    @Override
    public List<EnterpriseEmployeeDepartmentDTO> listByEmployeeId(Long employeeId) {
        return PojoUtils.map(enterpriseEmployeeDepartmentService.listByEmployeeId(employeeId),EnterpriseEmployeeDepartmentDTO.class);
    }

    @Override
    public List<EnterpriseEmployeeDepartmentDTO> getEmployeeListByCode(String code) {
        return PojoUtils.map(enterpriseDepartmentService.getEmployeeListByCode(code),EnterpriseEmployeeDepartmentDTO.class);
    }

    @Override
    public Map<Long, List<EnterpriseDepartmentDTO>> getDepartmentByEidUser(Long eid, List<Long> userIdList) {
        Map<Long, List<EnterpriseDepartmentDTO>> map = MapUtil.newHashMap();

        Map<Long, List<EnterpriseEmployeeDepartmentDO>> employeeDepartmentDoMap = enterpriseEmployeeDepartmentService.getDepartmentByEidUser(eid, userIdList);
        employeeDepartmentDoMap.forEach((userId, list) -> {
            List<Long> departmentIdList = list.stream().map(EnterpriseEmployeeDepartmentDO::getDepartmentId).collect(Collectors.toList());
            map.put(userId, PojoUtils.map(enterpriseDepartmentService.listByIds(departmentIdList), EnterpriseDepartmentDTO.class));
        });

        return map;
    }

    @Override
    public EnterpriseDepartmentDTO getByEidCode(Long eid, String code) {
        return PojoUtils.map(enterpriseDepartmentService.getByEidCode(eid, code), EnterpriseDepartmentDTO.class);
    }

    @Override
    public List<Tree<Long>> listTreeByEid(Long eid, EnableStatusEnum statusEnum) {
        Assert.notNull(eid, "参数eid不能为空");
        Assert.notNull(statusEnum, "参数statusEnum不能为空");
        return enterpriseDepartmentService.listTreeByEid(eid, statusEnum);
    }
}