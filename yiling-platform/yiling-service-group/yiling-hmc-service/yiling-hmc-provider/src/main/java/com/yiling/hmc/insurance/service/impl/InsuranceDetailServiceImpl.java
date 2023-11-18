package com.yiling.hmc.insurance.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.insurance.dao.InsuranceDetailMapper;
import com.yiling.hmc.insurance.dto.request.InsuranceDetailListRequest;
import com.yiling.hmc.insurance.dto.request.InsuranceDetailSaveRequest;
import com.yiling.hmc.insurance.dto.request.InsuranceSaveRequest;
import com.yiling.hmc.insurance.entity.InsuranceDetailDO;
import com.yiling.hmc.insurance.service.InsuranceDetailService;

import cn.hutool.core.collection.CollUtil;

/**
 * <p>
 * 保险商品明细表 服务实现类
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-23
 */
@Service
public class InsuranceDetailServiceImpl extends BaseServiceImpl<InsuranceDetailMapper, InsuranceDetailDO> implements InsuranceDetailService {

    @Override
    public boolean saveInsuranceDetail(InsuranceSaveRequest request) {
        List<InsuranceDetailSaveRequest> insuranceDetailSaveRequestList = request.getInsuranceDetailSaveList();
        List<InsuranceDetailDO> doList = PojoUtils.map(insuranceDetailSaveRequestList, InsuranceDetailDO.class);
        doList.forEach(e -> {
            e.setInsuranceId(request.getId());
            e.setOpUserId(request.getOpUserId());
            e.setOpTime(request.getOpTime());
        });
        return this.saveBatch(doList);
    }

    @Override
    public boolean saveEditInsuranceDetail(InsuranceSaveRequest request) {
        List<InsuranceDetailSaveRequest> insuranceDetailSaveRequestList = request.getInsuranceDetailSaveList();
        List<InsuranceDetailDO> newList = PojoUtils.map(insuranceDetailSaveRequestList, InsuranceDetailDO.class);
        List<InsuranceDetailDO> oldList = this.listByInsuranceId(request.getId());
        List<InsuranceDetailDO> insertList = new ArrayList<>();
        List<InsuranceDetailDO> updateList = new ArrayList<>();
        List<InsuranceDetailDO> deleteList = new ArrayList<>();
        // 1.新旧保险明细对比 => 新增，修改
        newList.forEach(item -> {
            Optional<InsuranceDetailDO> first = oldList.stream().filter(old -> old.getControlId().equals(item.getControlId())).findFirst();
            // 旧数据不包含新的数据  -> 新增
            if (!first.isPresent()) {
                item.setOpUserId(request.getOpUserId());
                item.setOpTime(request.getOpTime());
                insertList.add(item);
            } else {
                item.setId(first.get().getId());
                // 旧数据包含新的数据  -> 修改
                item.setOpUserId(request.getOpUserId());
                item.setOpTime(request.getOpTime());
                updateList.add(item);
            }
        });

        // 2.旧新保险明细对比 => 删除
        oldList.forEach(item -> {
            boolean exists = newList.stream().noneMatch(temp -> temp.getControlId().equals(item.getControlId()));
            if (exists) {
                deleteList.add(item);
            }
        });

        if (CollUtil.isNotEmpty(insertList)) {
            this.saveBatch(insertList);
        }

        if (CollUtil.isNotEmpty(updateList)) {
            this.updateBatchById(updateList);
        }

        if (CollUtil.isNotEmpty(deleteList)) {
            InsuranceDetailDO deleteEntity = new InsuranceDetailDO();
            deleteEntity.setOpUserId(request.getOpUserId());
            deleteEntity.setOpTime(request.getOpTime());
            QueryWrapper<InsuranceDetailDO> wrapper = new QueryWrapper<>();
            List<Long> idList = deleteList.stream().map(InsuranceDetailDO::getId).collect(Collectors.toList());
            wrapper.lambda().in(InsuranceDetailDO::getId, idList);
            this.batchDeleteWithFill(deleteEntity, wrapper);
        }

        return true;
    }

    @Override
    public List<InsuranceDetailDO> listByInsuranceId(Long insuranceId) {
        QueryWrapper<InsuranceDetailDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(InsuranceDetailDO::getInsuranceId, insuranceId);
        return this.list(wrapper);
    }

    @Override
    public List<InsuranceDetailDO> listByControlId(List<Long> controlIdList) {
        QueryWrapper<InsuranceDetailDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(InsuranceDetailDO::getControlId, controlIdList);
        return this.list(wrapper);
    }

    @Override
    public Page<InsuranceDetailDO> pageByInsuranceNameAndStatus(InsuranceDetailListRequest request) {
        Page<InsuranceDetailDO> objectPage = new Page<>(request.getCurrent(), request.getSize());
        return this.baseMapper.pageByInsuranceNameAndStatus(objectPage, request);
    }

    @Override
    public List<InsuranceDetailDO> listByControlIdAndCompanyAndInsuranceStatus(List<Long> controlIdList, Long insuranceCompanyId, Integer insuranceStatus) {
        return this.getBaseMapper().listByControlIdAndCompanyAndInsuranceStatus(controlIdList, insuranceCompanyId, insuranceStatus);
    }
}
