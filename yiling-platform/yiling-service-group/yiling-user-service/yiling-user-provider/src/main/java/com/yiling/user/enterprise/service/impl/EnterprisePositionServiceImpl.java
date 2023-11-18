package com.yiling.user.enterprise.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.enterprise.dao.EnterprisePositionMapper;
import com.yiling.user.enterprise.dto.request.QueryPositionPageListRequest;
import com.yiling.user.enterprise.dto.request.SavePositionRequest;
import com.yiling.user.enterprise.dto.request.UpdatePositionStatusRequest;
import com.yiling.user.enterprise.entity.EnterprisePositionDO;
import com.yiling.user.enterprise.service.EnterpriseEmployeeService;
import com.yiling.user.enterprise.service.EnterprisePositionService;

import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * 企业职位信息 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-09-29
 */
@Service
public class EnterprisePositionServiceImpl extends BaseServiceImpl<EnterprisePositionMapper, EnterprisePositionDO> implements EnterprisePositionService {

    @Autowired
    private EnterpriseEmployeeService enterpriseEmployeeService;

    @Override
    public Page<EnterprisePositionDO> pageList(QueryPositionPageListRequest request) {
        LambdaQueryWrapper<EnterprisePositionDO> queryWrapper = Wrappers.lambdaQuery();

        // 企业ID
        queryWrapper.eq(EnterprisePositionDO::getEid, request.getEid());

        // 职位名称
        String name = request.getName();
        if (StrUtil.isNotEmpty(name)) {
            queryWrapper.like(EnterprisePositionDO::getName, name);
        }

        // 职位状态
        if (EnableStatusEnum.getByCode(request.getStatus()) != EnableStatusEnum.ALL) {
            queryWrapper.eq(EnterprisePositionDO::getStatus, request.getStatus());
        }

        return this.page(request.getPage(), queryWrapper);
    }

    @Override
    public boolean save(SavePositionRequest request) {
        // 检查名称是否重复
        this.checkName(request.getEid(), request.getName(), request.getId());

        EnterprisePositionDO entity = PojoUtils.map(request, EnterprisePositionDO.class);
        return this.saveOrUpdate(entity);
    }

    @Override
    public boolean updateStatus(UpdatePositionStatusRequest request) {
        EnterprisePositionDO entity = this.getById(request.getId());
        if (entity == null) {
            throw new BusinessException(UserErrorCode.NETERPRISE_POSITIOIN_NOT_EXISTS);
        }

        if (EnableStatusEnum.getByCode(request.getStatus()) == EnableStatusEnum.DISABLED) {
            int count = enterpriseEmployeeService.countByPositionId(request.getId());
            if (count > 0) {
                throw new BusinessException(UserErrorCode.NETERPRISE_POSITIOIN_HAS_EMPLOYEE);
            }
        }

        entity.setStatus(request.getStatus());
        entity.setOpUserId(request.getOpUserId());
        return this.updateById(entity);
    }

    /**
     * 检查职位名称是否重复
     *
     * @param eid 所属企业ID
     * @param name 职位名称
     * @param id 职位ID
     */
    private void checkName(Long eid, String name, Long id) {
        LambdaQueryWrapper<EnterprisePositionDO> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper
                .eq(EnterprisePositionDO::getEid, eid)
                .eq(EnterprisePositionDO::getName, name)
                .last("limit 1");
        EnterprisePositionDO entity = this.getOne(queryWrapper);

        if (id == null) {
            if (entity != null) {
                throw new BusinessException(UserErrorCode.ENTERPRISE_POSITION_NAME_EXISTS);
            }
        } else {
            if (entity != null && !entity.getId().equals(id)) {
                throw new BusinessException(UserErrorCode.ENTERPRISE_POSITION_NAME_EXISTS);
            }
        }
    }
}
