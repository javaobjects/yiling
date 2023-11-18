package com.yiling.dataflow.wash.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.entity.CrmEnterpriseDO;
import com.yiling.dataflow.crm.service.CrmEnterpriseService;
import com.yiling.dataflow.wash.dao.UnlockThirdRecordMapper;
import com.yiling.dataflow.wash.dto.request.QueryUnlockThirdRecordPageRequest;
import com.yiling.dataflow.wash.dto.request.SaveOrUpdateUnlockThirdRecordRequest;
import com.yiling.dataflow.wash.entity.UnlockThirdRecordDO;
import com.yiling.dataflow.wash.service.UnlockThirdRecordService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;

/**
 * <p>
 * 小三批备案表 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-05-11
 */
@Service
public class UnlockThirdRecordServiceImpl extends BaseServiceImpl<UnlockThirdRecordMapper, UnlockThirdRecordDO> implements UnlockThirdRecordService {

    @Autowired
    private CrmEnterpriseService crmEnterpriseService;

    @Override
    public Page<UnlockThirdRecordDO> listPage(QueryUnlockThirdRecordPageRequest request) {
        Page<UnlockThirdRecordDO> page = new Page<>(request.getCurrent(), request.getSize());

        LambdaQueryWrapper<UnlockThirdRecordDO> wrapper = new LambdaQueryWrapper<>();
        if (request.getOrgCrmId() != null) {
            wrapper.eq(UnlockThirdRecordDO::getOrgCrmId, request.getOrgCrmId());
        }
        if (request.getOpStartTime() != null) {
            wrapper.ge(UnlockThirdRecordDO::getLastOpTime, DateUtil.beginOfDay(request.getOpStartTime())
            );
        }
        if (request.getOpEndTime() != null) {
            wrapper.le(UnlockThirdRecordDO::getLastOpTime, DateUtil.endOfDay(request.getOpEndTime()));
        }
        wrapper.orderByDesc(UnlockThirdRecordDO::getCreateTime);

        return baseMapper.selectPage(page, wrapper);
    }

    @Override
    public void add(SaveOrUpdateUnlockThirdRecordRequest request) {
        if (CollUtil.isNotEmpty(request.getDepartmentInfoList()) && request.getDepartmentInfoList().size() > 10) {
            throw new BusinessException(ResultCode.FAILED, "生效业务部门不可大于10个");
        }

        UnlockThirdRecordDO entity = getByOrgCrmId(request.getOrgCrmId());
        if (entity != null) {
            throw new BusinessException(ResultCode.FAILED, "当前机构已维护小三批备案！");
        }

        CrmEnterpriseDO crmEnterpriseDO = crmEnterpriseService.getById(request.getOrgCrmId());
        if (crmEnterpriseDO == null) {
            throw new BusinessException(ResultCode.FAILED);
        }
        UnlockThirdRecordDO unlockThirdRecordDO = PojoUtils.map(request, UnlockThirdRecordDO.class);
        unlockThirdRecordDO.setCustomerName(crmEnterpriseDO.getName());
        unlockThirdRecordDO.setEffectiveDepartment(JSONUtil.toJsonStr(request.getDepartmentInfoList()));
        unlockThirdRecordDO.setLastOpUser(request.getOpUserId());
        unlockThirdRecordDO.setLastOpTime(request.getOpTime());
        baseMapper.insert(unlockThirdRecordDO);
    }

    @Override
    public void update(SaveOrUpdateUnlockThirdRecordRequest request) {
        if (request.getId() == null) {
            throw new BusinessException(ResultCode.FAILED);
        }
        if (CollUtil.isNotEmpty(request.getDepartmentInfoList()) && request.getDepartmentInfoList().size() > 10) {
            throw new BusinessException(ResultCode.FAILED, "生效业务部门不可大于10个");
        }

        UnlockThirdRecordDO unlockThirdRecordDO = getByOrgCrmIdAndNeId(request.getOrgCrmId(), request.getId());
        if (unlockThirdRecordDO != null) {
            throw new BusinessException(ResultCode.FAILED, "当前机构已维护小三批备案！");
        }

        unlockThirdRecordDO = baseMapper.selectById(request.getId());
        if (unlockThirdRecordDO == null) {
            throw new BusinessException(ResultCode.FAILED);
        }

        unlockThirdRecordDO.setOrgCrmId(request.getOrgCrmId());
        unlockThirdRecordDO.setPurchaseQuota(request.getPurchaseQuota());
        unlockThirdRecordDO.setRemark(request.getRemark());
        unlockThirdRecordDO.setEffectiveDepartment(JSONUtil.toJsonStr(request.getDepartmentInfoList()));
        unlockThirdRecordDO.setLastOpUser(request.getOpUserId());
        unlockThirdRecordDO.setLastOpTime(request.getOpTime());
        baseMapper.updateById(unlockThirdRecordDO);
    }

    @Override
    public UnlockThirdRecordDO getByOrgCrmId(Long orgCrmId) {
        LambdaQueryWrapper<UnlockThirdRecordDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UnlockThirdRecordDO::getOrgCrmId, orgCrmId);
        List<UnlockThirdRecordDO> list = baseMapper.selectList(wrapper);
        if (CollUtil.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public void delete(Long id) {
        UnlockThirdRecordDO unlockThirdRecordDO = new UnlockThirdRecordDO();
        unlockThirdRecordDO.setId(id);
        this.deleteByIdWithFill(unlockThirdRecordDO);
    }

    private UnlockThirdRecordDO getByOrgCrmIdAndNeId(Long orgCrmId, Long id) {
        LambdaQueryWrapper<UnlockThirdRecordDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UnlockThirdRecordDO::getOrgCrmId, orgCrmId);
        wrapper.ne(UnlockThirdRecordDO::getId, id);
        List<UnlockThirdRecordDO> list = baseMapper.selectList(wrapper);
        if (CollUtil.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }
}
