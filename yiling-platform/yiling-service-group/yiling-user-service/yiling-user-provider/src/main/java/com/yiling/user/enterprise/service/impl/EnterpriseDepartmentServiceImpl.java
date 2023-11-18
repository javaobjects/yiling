package com.yiling.user.enterprise.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.enterprise.dao.EnterpriseDepartmentMapper;
import com.yiling.user.enterprise.dto.request.AddDepartmentRequest;
import com.yiling.user.enterprise.dto.request.MoveDepartmentEmployeeRequest;
import com.yiling.user.enterprise.dto.request.QueryDepartmentPageListRequest;
import com.yiling.user.enterprise.dto.request.UpdateDepartmentRequest;
import com.yiling.user.enterprise.dto.request.UpdateDepartmentStatusRequest;
import com.yiling.user.enterprise.entity.EnterpriseDepartmentDO;
import com.yiling.user.enterprise.entity.EnterpriseEmployeeDepartmentDO;
import com.yiling.user.enterprise.service.EnterpriseDepartmentService;
import com.yiling.user.enterprise.service.EnterpriseEmployeeDepartmentService;
import com.yiling.user.enterprise.service.EnterpriseEmployeeService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 企业部门信息 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-05-21
 */
@Slf4j
@Service
public class EnterpriseDepartmentServiceImpl extends BaseServiceImpl<EnterpriseDepartmentMapper, EnterpriseDepartmentDO> implements EnterpriseDepartmentService {

    @Autowired
    private EnterpriseEmployeeService enterpriseEmployeeService;
    @Autowired
    private EnterpriseEmployeeDepartmentService enterpriseEmployeeDepartmentService;

    @Override
    public Boolean add(AddDepartmentRequest request) {
        EnterpriseDepartmentDO enterpriseDepartmentDO = this.getByName(request.getEid(), request.getName());
        if (enterpriseDepartmentDO != null) {
            throw new BusinessException(UserErrorCode.ENTERPRISE_DEPARTMENT_NAME_EXISTS);
        }

        if (StrUtil.isNotEmpty(request.getCode())) {
            enterpriseDepartmentDO = this.getByCode(request.getEid(), request.getCode());
            if (enterpriseDepartmentDO != null) {
                throw new BusinessException(UserErrorCode.ENTERPRISE_DEPARTMENT_CODE_EXISTS);
            }
        }

        EnterpriseDepartmentDO entity = PojoUtils.map(request, EnterpriseDepartmentDO.class);
        return this.save(entity);
    }

    @Override
    public Boolean update(UpdateDepartmentRequest request) {
        EnterpriseDepartmentDO entity = this.getById(request.getId());
        EnterpriseDepartmentDO enterpriseDepartmentDO = this.getByName(entity.getEid(), request.getName());
        if (enterpriseDepartmentDO != null && !enterpriseDepartmentDO.getId().equals(request.getId())) {
            throw new BusinessException(UserErrorCode.ENTERPRISE_DEPARTMENT_NAME_EXISTS);
        }

        if (StrUtil.isNotEmpty(request.getCode())) {
            enterpriseDepartmentDO = this.getByCode(entity.getEid(), request.getCode());
            if (enterpriseDepartmentDO != null && !enterpriseDepartmentDO.getId().equals(request.getId())) {
                throw new BusinessException(UserErrorCode.ENTERPRISE_DEPARTMENT_CODE_EXISTS);
            }
        }

        Long parentId = request.getParentId();
        if (parentId != null && parentId != 0L) {
            List<EnterpriseDepartmentDO> sublist = this.sublistById(request.getId());
            List<Long> subIds = sublist.stream().map(EnterpriseDepartmentDO::getId).collect(Collectors.toList());
            subIds.add(request.getId());
            if (subIds.contains(parentId)) {
                throw new BusinessException(UserErrorCode.ENTERPRISE_DEPARTMENT_PARENT_IN_SUBLIST);
            }
        }

        entity = PojoUtils.map(request, EnterpriseDepartmentDO.class);
        return this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateStatus(UpdateDepartmentStatusRequest request) {
        EnterpriseDepartmentDO entity = this.getById(request.getId());

        // 状态未发生变化
        if (request.getStatus().equals(entity.getStatus())) {
            return true;
        }

        // 停用需要判断部门下是否有员工
        if (EnableStatusEnum.getByCode(request.getStatus()) == EnableStatusEnum.DISABLED) {
            this.disableSubDepartments(entity, request.getOpUserId());
        } else {
            this.enableParentDepartments(entity, request.getOpUserId());
        }

        return true;
    }

    /**
     * 递归启用当前部门及上级部门
     *
     * @param departmentDO
     * @param opUserId
     */
    private void enableParentDepartments(EnterpriseDepartmentDO departmentDO, Long opUserId) {
        // 启用当前部门
        if (EnableStatusEnum.getByCode(departmentDO.getStatus()) == EnableStatusEnum.DISABLED) {
            departmentDO.setStatus(EnableStatusEnum.ENABLED.getCode());
            departmentDO.setOpUserId(opUserId);
            this.updateById(departmentDO);
        }

        EnterpriseDepartmentDO parentDepartmentDO = this.getParent(departmentDO.getId());
        if (parentDepartmentDO != null) {
            this.enableParentDepartments(parentDepartmentDO, opUserId);
        }
    }

    private EnterpriseDepartmentDO getParent(Long id) {
        EnterpriseDepartmentDO departmentDO = this.getById(id);
        return this.getById(departmentDO.getParentId());
    }

    /**
     * 递归停用当前部门及下级部门
     *
     * @param departmentDO
     * @param opUserId
     * @return
     */
    private void disableSubDepartments(EnterpriseDepartmentDO departmentDO, Long opUserId) {
        // 当前部门下是否存在员工
        int departmentEmployeeNum = enterpriseEmployeeService.countByDepartmentId(departmentDO.getId());
        if (departmentEmployeeNum > 0) {
            log.warn("部门下存在员工信息，当前部门不可被停用。部门ID：{}", departmentDO.getId());
            throw new BusinessException(UserErrorCode.ENTERPRISE_DEPARTMENT_HAS_EMPLOYEE);
        }

        // 停用当前部门
        if (EnableStatusEnum.getByCode(departmentDO.getStatus()) == EnableStatusEnum.ENABLED) {
            departmentDO.setStatus(EnableStatusEnum.DISABLED.getCode());
            departmentDO.setOpUserId(opUserId);
            this.updateById(departmentDO);
        }

        List<EnterpriseDepartmentDO> departmentDOList = this.listByParentId(departmentDO.getEid(), departmentDO.getId());
        departmentDOList.forEach(e -> {
            // 停用下级部门
            this.disableSubDepartments(e, opUserId);
        });
    }

    @Override
    public Page<EnterpriseDepartmentDO> pageList(QueryDepartmentPageListRequest request) {
        LambdaQueryWrapper<EnterpriseDepartmentDO> queryWrapper = new LambdaQueryWrapper<>();

        Long eid = request.getEid();
        if (eid != null && eid != 0L) {
            queryWrapper.eq(EnterpriseDepartmentDO::getEid, eid);
        }

        String name = request.getName();
        if (StrUtil.isNotEmpty(name)) {
            queryWrapper.like(EnterpriseDepartmentDO::getName, name);
        }

        Integer status = request.getStatus();
        if (EnableStatusEnum.getByCode(status) != EnableStatusEnum.ALL) {
            queryWrapper.eq(EnterpriseDepartmentDO::getStatus, status);
        }

        queryWrapper.orderByAsc(EnterpriseDepartmentDO::getId);
        return this.page(request.getPage(), queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean moveEmployee(MoveDepartmentEmployeeRequest request) {
        if (request.getSourceId().equals(request.getTargetId())) {
            log.warn("来源部门ID与目标部门ID相同：sourceId={}, targetId={}", request.getSourceId(), request.getTargetId());
            return true;
        }

        EnterpriseDepartmentDO sourceDepartmentDO = this.getById(request.getSourceId());
        if (sourceDepartmentDO == null) {
            log.error("来源部门ID对应的部门信息未找到：sourceId={}", request.getSourceId());
            return false;
        }

        EnterpriseDepartmentDO targetDepartmentDO = this.getById(request.getTargetId());
        if (targetDepartmentDO == null) {
            log.error("目标部门ID对应的部门信息未找到：targetId={}", request.getTargetId());
            return false;
        } else if (!targetDepartmentDO.getEid().equals(sourceDepartmentDO.getEid())) {
            log.error("来源部门ID与目标部门ID不属于同一个主体：sourceId={}, targetId={}", request.getSourceId(), request.getTargetId());
            return false;
        }

        if (EnableStatusEnum.getByCode(targetDepartmentDO.getStatus()).equals(EnableStatusEnum.DISABLED)) {
            log.error("目标部门已停用：targetId={}", request.getTargetId());
            return false;
        }

        List<EnterpriseEmployeeDepartmentDO> sourceEmployeeDepartmentList = enterpriseEmployeeDepartmentService.listByDepartmentId(request.getSourceId());
        if (CollUtil.isEmpty(sourceEmployeeDepartmentList)) {
            log.warn("该部门下没有员工信息：sourceId={}, targetId={}", request.getSourceId(), request.getTargetId());
            return true;
        }

        List<EnterpriseEmployeeDepartmentDO> targetEmployeeDepartmentList = enterpriseEmployeeDepartmentService.listByDepartmentId(request.getTargetId());
        List<Long> targetEmployeeIds = targetEmployeeDepartmentList.stream().map(EnterpriseEmployeeDepartmentDO::getEmployeeId).distinct().collect(Collectors.toList());

        List<EnterpriseEmployeeDepartmentDO> moveList = sourceEmployeeDepartmentList.stream().filter(e -> !targetEmployeeIds.contains(e.getEmployeeId())).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(moveList)) {
            LambdaQueryWrapper<EnterpriseEmployeeDepartmentDO> queryWrapper = Wrappers.lambdaQuery();
            queryWrapper.in(EnterpriseEmployeeDepartmentDO::getId, moveList.stream().map(EnterpriseEmployeeDepartmentDO::getId).collect(Collectors.toList()));

            EnterpriseEmployeeDepartmentDO entity = new EnterpriseEmployeeDepartmentDO();
            entity.setDepartmentId(request.getTargetId());
            entity.setOpUserId(request.getOpUserId());

            enterpriseEmployeeDepartmentService.update(entity, queryWrapper);
        }

        // 已存在于目标部门的记录直接删除
        List<EnterpriseEmployeeDepartmentDO> repeatList = sourceEmployeeDepartmentList.stream().filter(e -> targetEmployeeIds.contains(e.getEmployeeId())).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(repeatList)) {
            LambdaQueryWrapper<EnterpriseEmployeeDepartmentDO> queryWrapper = Wrappers.lambdaQuery();
            queryWrapper.in(EnterpriseEmployeeDepartmentDO::getId, repeatList.stream().map(EnterpriseEmployeeDepartmentDO::getId).collect(Collectors.toList()));

            EnterpriseEmployeeDepartmentDO entity = new EnterpriseEmployeeDepartmentDO();
            entity.setOpUserId(request.getOpUserId());

            enterpriseEmployeeDepartmentService.batchDeleteWithFill(entity, queryWrapper);
        }

        return true;
    }

    @Override
    public List<EnterpriseDepartmentDO> sublistById(Long id) {
        List<EnterpriseDepartmentDO> departmentList = CollUtil.newArrayList();
        //递归方式取列表
        this.sublist(departmentList, id);
        return departmentList;
    }

    private void sublist(List<EnterpriseDepartmentDO> departmentList, Long id) {
        LambdaQueryWrapper<EnterpriseDepartmentDO> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper
                .eq(EnterpriseDepartmentDO::getParentId, id)
                .orderByAsc(EnterpriseDepartmentDO::getId);

        List<EnterpriseDepartmentDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return;
        }

        list.forEach(e -> {
            departmentList.add(e);
            // 获取下级部门列表
            this.sublist(departmentList, e.getId());
        });
    }

    @Override
    public List<EnterpriseDepartmentDO> listByParentId(Long eid, Long parentId) {
        LambdaQueryWrapper<EnterpriseDepartmentDO> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper
                .eq(EnterpriseDepartmentDO::getEid, eid)
                .eq(EnterpriseDepartmentDO::getParentId, parentId)
                .orderByAsc(EnterpriseDepartmentDO::getId);
        List<EnterpriseDepartmentDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return list;
    }

    /**
     * 根据部门编码获取部门下的员工信息
     * @param code 部门编码
     * @return
     */
    @Override
    public List<EnterpriseEmployeeDepartmentDO> getEmployeeListByCode(String code) {
        LambdaQueryWrapper<EnterpriseDepartmentDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EnterpriseDepartmentDO::getCode,code);
        wrapper.last("limit 1");
        EnterpriseDepartmentDO enterpriseDepartmentDo = this.getOne(wrapper);

        if (Objects.isNull(enterpriseDepartmentDo)) {
            return ListUtil.toList();
        }

        LambdaQueryWrapper<EnterpriseEmployeeDepartmentDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EnterpriseEmployeeDepartmentDO::getEid,enterpriseDepartmentDo.getEid());
        queryWrapper.eq(EnterpriseEmployeeDepartmentDO::getDepartmentId,enterpriseDepartmentDo.getId());
        return enterpriseEmployeeDepartmentService.list(queryWrapper);

    }

    @Override
    public EnterpriseDepartmentDO getByEidCode(Long eid, String code) {
        QueryWrapper<EnterpriseDepartmentDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(EnterpriseDepartmentDO::getEid, eid)
                .eq(EnterpriseDepartmentDO::getCode, code)
                .last("limit 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public List<Tree<Long>> listTreeByEid(Long eid, EnableStatusEnum statusEnum) {
        List<EnterpriseDepartmentDO> list = this.listByEid(eid, statusEnum);

        TreeNodeConfig config = new TreeNodeConfig();
        config.setIdKey("id");
        config.setParentIdKey("parentId");
        config.setNameKey("name");
        config.setWeightKey("id");

        List<Tree<Long>> treeNodes = TreeUtil.build(list, 0L, config, (treeNode, tree) -> {
            tree.setId(treeNode.getId());
            tree.setParentId(treeNode.getParentId());
            tree.setName(treeNode.getName());
            // 扩展属性
            tree.putExtra("code", treeNode.getCode());
            tree.putExtra("description", treeNode.getDescription());
            tree.putExtra("managerId", treeNode.getManagerId());
            tree.putExtra("status", treeNode.getStatus());
        });

        return treeNodes;
    }

    /**
     * 获取企业部门列表
     *
     * @param eid 企业ID
     * @param statusEnum 状态枚举
     * @return java.util.List<com.yiling.user.enterprise.entity.EnterpriseDepartmentDO>
     * @author xuan.zhou
     * @date 2023/1/9
     **/
    private List<EnterpriseDepartmentDO> listByEid(Long eid, EnableStatusEnum statusEnum) {
        LambdaQueryWrapper<EnterpriseDepartmentDO> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(EnterpriseDepartmentDO::getEid, eid);
        if (statusEnum != EnableStatusEnum.ALL) {
            lambdaQueryWrapper.eq(EnterpriseDepartmentDO::getStatus, statusEnum.getCode());
        }

        List<EnterpriseDepartmentDO> list = this.list(lambdaQueryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return list;
    }

    /**
     * 根据部门名称获取部门信息
     *
     * @param eid 企业ID
     * @param name 部门名称
     * @return
     */
    private EnterpriseDepartmentDO getByName(Long eid, String name) {
        LambdaQueryWrapper<EnterpriseDepartmentDO> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper
                .eq(EnterpriseDepartmentDO::getEid, eid)
                .eq(EnterpriseDepartmentDO::getName, name)
                .last("limit 1");
        return this.getOne(queryWrapper);
    }

    private EnterpriseDepartmentDO getByCode(Long eid, String code) {
        LambdaQueryWrapper<EnterpriseDepartmentDO> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper
                .eq(EnterpriseDepartmentDO::getEid, eid)
                .eq(EnterpriseDepartmentDO::getCode, code)
                .last("limit 1");
        return this.getOne(queryWrapper);
    }
}
