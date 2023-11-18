package com.yiling.hmc.wechat.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.wechat.dao.InsuranceFetchPlanMapper;
import com.yiling.hmc.wechat.dto.InsuranceFetchPlanDTO;
import com.yiling.hmc.wechat.dto.request.InsuranceFetchPlanPageRequest;
import com.yiling.hmc.wechat.dto.request.SaveInsuranceFetchPlanRequest;
import com.yiling.hmc.wechat.entity.InsuranceFetchPlanDO;
import com.yiling.hmc.wechat.enums.InsuranceFetchStatusEnum;
import com.yiling.hmc.wechat.service.InsuranceFetchPlanService;

import cn.hutool.core.collection.CollUtil;

/**
 * <p>
 * C端拿药计划表 服务实现类
 * </p>
 *
 * @author fan.shen
 * @date 2022-03-31
 */
@Service
public class InsuranceFetchPlanServiceImpl extends BaseServiceImpl<InsuranceFetchPlanMapper, InsuranceFetchPlanDO> implements InsuranceFetchPlanService {

    @Override
    public Boolean saveFetchPlan(List<SaveInsuranceFetchPlanRequest> requestList) {
        List<InsuranceFetchPlanDO> list = PojoUtils.map(requestList, InsuranceFetchPlanDO.class);
        return this.saveBatch(list);
    }

    @Override
    public List<InsuranceFetchPlanDTO> getByInsuranceRecordId(Long insuranceRecordId) {
        QueryWrapper<InsuranceFetchPlanDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(InsuranceFetchPlanDO::getInsuranceRecordId, insuranceRecordId);
        queryWrapper.lambda().orderByAsc(InsuranceFetchPlanDO::getInitFetchTime);
        List<InsuranceFetchPlanDO> list = this.list(queryWrapper);
        return PojoUtils.map(list, InsuranceFetchPlanDTO.class);
    }

    @Override
    public InsuranceFetchPlanDTO getLatestPlan(Long insuranceRecordId) {
        QueryWrapper<InsuranceFetchPlanDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(InsuranceFetchPlanDO::getInsuranceRecordId, insuranceRecordId);
        queryWrapper.lambda().eq(InsuranceFetchPlanDO::getFetchStatus, InsuranceFetchStatusEnum.WAIT.getType());
        queryWrapper.lambda().orderByAsc(InsuranceFetchPlanDO::getInitFetchTime);
        queryWrapper.last("limit 1");
        InsuranceFetchPlanDO planDO = this.getOne(queryWrapper);
        return PojoUtils.map(planDO, InsuranceFetchPlanDTO.class);
    }

    @Override
    public boolean updateFetchStatus(InsuranceFetchPlanDTO planDTO) {

        InsuranceFetchPlanDO fetchPlanDO = new InsuranceFetchPlanDO();
        fetchPlanDO.setFetchStatus(planDTO.getFetchStatus());
        fetchPlanDO.setActualFetchTime(new Date());
        fetchPlanDO.setOrderId(planDTO.getOrderId());

        UpdateWrapper<InsuranceFetchPlanDO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(InsuranceFetchPlanDO::getId, planDTO.getId());

        return this.update(fetchPlanDO, updateWrapper);
    }

    @Override
    public Map<Long, Long> getFetchPlanCountMap(List<Long> insuranceRecordIdList, List<Long> recordPayIdList) {
        LambdaQueryWrapper<InsuranceFetchPlanDO> fetchWrapper = Wrappers.lambdaQuery();
        fetchWrapper.in(CollUtil.isNotEmpty(insuranceRecordIdList), InsuranceFetchPlanDO::getInsuranceRecordId, insuranceRecordIdList);
        fetchWrapper.in(CollUtil.isNotEmpty(recordPayIdList), InsuranceFetchPlanDO::getRecordPayId, recordPayIdList);
        fetchWrapper.select(InsuranceFetchPlanDO::getId, InsuranceFetchPlanDO::getInsuranceRecordId);

        List<InsuranceFetchPlanDO> list = this.list(fetchWrapper);
        if (CollUtil.isEmpty(list)) {
            return Maps.newHashMap();
        }
        Map<Long, Long> longMap = list.stream().collect(Collectors.groupingBy(InsuranceFetchPlanDO::getInsuranceRecordId, Collectors.counting()));
        return longMap;
    }

    @Override
    public List<InsuranceFetchPlanDTO> getByRecordPayId(Long recordPayId) {
        LambdaQueryWrapper<InsuranceFetchPlanDO> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(InsuranceFetchPlanDO::getRecordPayId, recordPayId);
        queryWrapper.orderByAsc(InsuranceFetchPlanDO::getInitFetchTime);
        List<InsuranceFetchPlanDO> list = this.list(queryWrapper);
        return PojoUtils.map(list, InsuranceFetchPlanDTO.class);
    }

    @Override
    public Page<InsuranceFetchPlanDO> pageList(InsuranceFetchPlanPageRequest request) {
        QueryWrapper<InsuranceFetchPlanDO> wrapper = new QueryWrapper<>();
        if (null != request.getInitFetchStartTime()) {
            wrapper.lambda().ge(InsuranceFetchPlanDO::getInitFetchTime, request.getInitFetchStartTime());
        }
        if (null != request.getInitFetchStopTime()) {
            wrapper.lambda().le(InsuranceFetchPlanDO::getInitFetchTime, request.getInitFetchStopTime());
        }
        if (null != request.getFetchStatus() && 0 != request.getFetchStatus()) {
            wrapper.lambda().eq(InsuranceFetchPlanDO::getFetchStatus, request.getFetchStatus());
        }
        return this.page(new Page<>(request.getCurrent(), request.getSize()), wrapper);
    }

    @Override
    public InsuranceFetchPlanDTO getByOrderId(Long id) {
        LambdaQueryWrapper<InsuranceFetchPlanDO> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(InsuranceFetchPlanDO::getOrderId, id);
        InsuranceFetchPlanDO fetchPlanDO = this.getOne(queryWrapper);
        return PojoUtils.map(fetchPlanDO, InsuranceFetchPlanDTO.class);
    }
}
