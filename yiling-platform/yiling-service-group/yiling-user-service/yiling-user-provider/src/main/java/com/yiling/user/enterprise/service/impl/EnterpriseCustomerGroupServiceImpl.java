package com.yiling.user.enterprise.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.enterprise.dao.EnterpriseCustomerGroupMapper;
import com.yiling.user.enterprise.dto.EnterpriseCustomerGroupDTO;
import com.yiling.user.enterprise.dto.request.AddCustomerGroupRequest;
import com.yiling.user.enterprise.dto.request.QueryCustomerGroupPageListRequest;
import com.yiling.user.enterprise.dto.request.UpdateCustomerGroupRequest;
import com.yiling.user.enterprise.entity.EnterpriseCustomerGroupDO;
import com.yiling.user.enterprise.service.EnterpriseCustomerGroupService;
import com.yiling.user.enterprise.service.EnterpriseCustomerService;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * 企业客户分组 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-05-21
 */
@Service
public class EnterpriseCustomerGroupServiceImpl extends BaseServiceImpl<EnterpriseCustomerGroupMapper, EnterpriseCustomerGroupDO> implements EnterpriseCustomerGroupService {

    @Autowired
    EnterpriseCustomerService enterpriseCustomerService;

    @Override
    public Page<EnterpriseCustomerGroupDO> pageList(QueryCustomerGroupPageListRequest request) {
        LambdaQueryWrapper<EnterpriseCustomerGroupDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        Long eid = request.getEid();
        if (eid != null && eid != 0L) {
            lambdaQueryWrapper.eq(EnterpriseCustomerGroupDO::getEid, eid);
        }

        String name = request.getName();
        if (StrUtil.isNotEmpty(name)) {
            lambdaQueryWrapper.like(EnterpriseCustomerGroupDO::getName, name);
        }

        Integer type = request.getType();
        if (type != null && type != 0) {
            lambdaQueryWrapper.eq(EnterpriseCustomerGroupDO::getType, type);
        }

        Integer status = request.getStatus();
        if (status != null && status != 0) {
            lambdaQueryWrapper.eq(EnterpriseCustomerGroupDO::getStatus, status);
        }

        return this.page(request.getPage(), lambdaQueryWrapper);
    }

    @Override
    public Long add(AddCustomerGroupRequest request) {
        if (this.getByName(request.getEid(), request.getName()) != null) {
            throw new BusinessException(UserErrorCode.CUSTOMER_GROUP_NAME_EXISTS);
        }

        EnterpriseCustomerGroupDO entity = new EnterpriseCustomerGroupDO();
        entity.setEid(request.getEid());
        entity.setName(request.getName());
        entity.setType(request.getType());
        entity.setStatus(1);
        entity.setOpUserId(request.getOpUserId());
        this.save(entity);

        return entity.getId();
    }

    @Override
    public boolean update(UpdateCustomerGroupRequest request) {
        EnterpriseCustomerGroupDO entity = this.getById(request.getId());

        EnterpriseCustomerGroupDO group = this.getByName(entity.getEid(), request.getName());
        if (group != null && !group.getId().equals(request.getId())) {
            throw new BusinessException(UserErrorCode.CUSTOMER_GROUP_NAME_EXISTS);
        }

        entity.setName(request.getName());
        entity.setOpUserId(request.getOpUserId());
        return this.updateById(entity);
    }

    private EnterpriseCustomerGroupDO getByName(Long eid, String name) {
        QueryWrapper<EnterpriseCustomerGroupDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(EnterpriseCustomerGroupDO::getEid, eid)
                .eq(EnterpriseCustomerGroupDO::getName, name)
                .last("limit 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public Map<Long, Long> countCustomerGroupNumByEids(List<Long> eids) {
        List<EnterpriseCustomerGroupDO> list = this.baseMapper.selectList(new LambdaQueryWrapper<EnterpriseCustomerGroupDO>().in(EnterpriseCustomerGroupDO::getEid, eids));
        return list.stream().collect(Collectors.groupingBy(EnterpriseCustomerGroupDO::getEid, Collectors.counting()));
    }

    @Override
    public EnterpriseCustomerGroupDTO getByEidAndName(Long eid, String name) {
        if (ObjectUtil.isNull(eid) || StringUtils.isBlank(name)) {
            return null;
        }
        LambdaQueryWrapper<EnterpriseCustomerGroupDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(EnterpriseCustomerGroupDO::getEid, eid).eq(EnterpriseCustomerGroupDO::getName, name);
        EnterpriseCustomerGroupDO one = this.getOne(lambdaQueryWrapper);
        return PojoUtils.map(one, EnterpriseCustomerGroupDTO.class);
    }

    @Override
    public List<EnterpriseCustomerGroupDO> listByEid(Long eid) {
        if (Objects.isNull(eid)) {
            return ListUtil.toList();
        }

        LambdaQueryWrapper<EnterpriseCustomerGroupDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EnterpriseCustomerGroupDO::getEid, eid);
        return this.list(queryWrapper);
    }
}
