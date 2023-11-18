package com.yiling.marketing.lotteryactivity.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.thread.SpringAsyncConfig;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.lotteryactivity.bo.EnterpriseSimpleBO;
import com.yiling.marketing.lotteryactivity.dto.request.AddCustomerPageRequest;
import com.yiling.marketing.lotteryactivity.dto.request.DeleteCustomerPageRequest;
import com.yiling.marketing.lotteryactivity.dto.request.QueryCustomerPageRequest;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityGiveEnterpriseDO;
import com.yiling.marketing.lotteryactivity.dao.LotteryActivityGiveEnterpriseMapper;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityErrorCode;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityGiveEnterpriseService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.marketing.strategy.enums.StrategyErrorCode;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterpriseByNameRequest;
import com.yiling.user.enterprise.dto.request.QueryEnterprisePageListRequest;
import com.yiling.user.enterprise.enums.EnterpriseAuthStatusEnum;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;

import org.apache.dubbo.config.annotation.DubboReference;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;

/**
 * <p>
 * 抽奖活动-赠送指定客户表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-08-29
 */
@Service
public class LotteryActivityGiveEnterpriseServiceImpl extends BaseServiceImpl<LotteryActivityGiveEnterpriseMapper, LotteryActivityGiveEnterpriseDO> implements LotteryActivityGiveEnterpriseService {

    @DubboReference
    EnterpriseApi enterpriseApi;

    @Autowired
    SpringAsyncConfig springAsyncConfig;

    @Override
    public Page<EnterpriseSimpleBO> queryHadAddCustomerPage(QueryCustomerPageRequest request) {
        return baseMapper.queryHadAddCustomerPage(request.getPage(), request);
    }

    @Override
    public List<Long> getGiveEnterpriseByActivityId(Long lotteryActivityId) {
        LambdaQueryWrapper<LotteryActivityGiveEnterpriseDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LotteryActivityGiveEnterpriseDO::getLotteryActivityId, lotteryActivityId);
        return this.list(wrapper).stream().map(LotteryActivityGiveEnterpriseDO::getEid).distinct().collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateGiveEnterpriseByLotteryActivityId(Long lotteryActivityId, List<Long> eidList, Long opUserId) {
        List<Long> eidDoList = this.getGiveEnterpriseByActivityId(lotteryActivityId);

        List<Long> addList = eidList.stream().filter(eid -> !eidDoList.contains(eid)).collect(Collectors.toList());
        List<Long> removeList = eidDoList.stream().filter(eid -> !eidList.contains(eid)).collect(Collectors.toList());

        // 添加客户
        if (CollUtil.isNotEmpty(addList)) {
            this.batchSaveGiveEnterprise(lotteryActivityId, opUserId, addList);
        }

        if (CollUtil.isNotEmpty(removeList)) {
            // 删除赠送指定客户表数据
            this.batchDeleteGiveEnterprise(lotteryActivityId, opUserId, removeList);
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addCustomer(AddCustomerPageRequest request) {
        Long lotteryActivityId = request.getLotteryActivityId();
        Long opUserId = request.getOpUserId();

        if (request.getType() == 1 || request.getType() == 2) {
            List<Long> eidList = request.getIdList();
            if (CollUtil.isEmpty(eidList)) {
                return true;
            }
            if (eidList.size() == 1) {
                LambdaQueryWrapper<LotteryActivityGiveEnterpriseDO> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(LotteryActivityGiveEnterpriseDO::getLotteryActivityId, lotteryActivityId);
                wrapper.eq(LotteryActivityGiveEnterpriseDO::getEid, eidList.get(0));
                wrapper.last("limit 1");
                LotteryActivityGiveEnterpriseDO enterpriseDO = this.getOne(wrapper);
                if (Objects.nonNull(enterpriseDO)) {
                    throw new BusinessException(LotteryActivityErrorCode.DATA_HAD_ADD);
                }
            }

            // 批量保存指定客户
            this.batchSaveGiveEnterprise(lotteryActivityId, opUserId, eidList);

        } else if (request.getType() == 3) {
            List<EnterpriseDTO> enterpriseDTOList = this.getEnterpriseDTOList(request,
                    ListUtil.toList(EnterpriseTypeEnum.CHAIN_BASE.getCode(), EnterpriseTypeEnum.CHAIN_DIRECT.getCode(), EnterpriseTypeEnum.CHAIN_JOIN.getCode(),
                            EnterpriseTypeEnum.PHARMACY.getCode(), EnterpriseTypeEnum.HOSPITAL.getCode(), EnterpriseTypeEnum.CLINIC.getCode()));

            if (CollUtil.isNotEmpty(enterpriseDTOList)) {
                // 将要添加的
                List<Long> eidList = enterpriseDTOList.stream().map(BaseDTO::getId).collect(Collectors.toList());
                // 异步保存添加的指定客户
                CompletableFuture.runAsync(() -> this.batchSaveGiveEnterprise(lotteryActivityId, opUserId, eidList), springAsyncConfig.getAsyncExecutor());
            }

        }

        return true;
    }

    @Override
    public List<EnterpriseDTO> getEnterpriseDTOList(AddCustomerPageRequest request, List<Integer> inTypeList) {
        // 校验数量是否大于500
        QueryEnterprisePageListRequest pageListRequest = new QueryEnterprisePageListRequest();
        pageListRequest.setCurrent(request.getCurrent());
        pageListRequest.setSize(request.getSize());
        pageListRequest.setId(request.getId());
        pageListRequest.setName(request.getName());
        pageListRequest.setAuthStatus(EnterpriseAuthStatusEnum.AUTH_SUCCESS.getCode());
        pageListRequest.setStatus(EnableStatusEnum.ENABLED.getCode());
        pageListRequest.setInTypeList(inTypeList);
        Page<EnterpriseDTO> enterpriseDTOPage = enterpriseApi.pageList(pageListRequest);
        if (enterpriseDTOPage.getTotal() > 500L) {
            throw new BusinessException(StrategyErrorCode.STRATEGY_BUYER_TO_MANY);
        }

        QueryEnterpriseByNameRequest enterpriseRequest = new QueryEnterpriseByNameRequest();
        enterpriseRequest.setId(request.getId());
        enterpriseRequest.setName(request.getName());
        enterpriseRequest.setAuthStatus(EnterpriseAuthStatusEnum.AUTH_SUCCESS.getCode());
        enterpriseRequest.setStatus(EnableStatusEnum.ENABLED.getCode());
        enterpriseRequest.setTypeList(inTypeList);

        return enterpriseApi.getEnterpriseListByName(enterpriseRequest);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteCustomer(DeleteCustomerPageRequest request) {
        Long lotteryActivityId = request.getLotteryActivityId();
        Long opUserId = request.getOpUserId();

        if (request.getType() == 1 || request.getType() == 2) {
            if (CollUtil.isEmpty(request.getIdList())) {
                return true;
            }

            // 删除赠送指定客户表数据
            this.batchDeleteGiveEnterprise(lotteryActivityId, opUserId, request.getIdList());

        } else if (request.getType() == 3) {

            QueryCustomerPageRequest pageRequest = new QueryCustomerPageRequest();
            pageRequest.setLotteryActivityId(lotteryActivityId);
            pageRequest.setId(request.getId());
            pageRequest.setName(request.getName());
            List<EnterpriseSimpleBO> hadAddCustomerList = baseMapper.queryHadAddCustomerPage(pageRequest);
            if (CollUtil.isNotEmpty(hadAddCustomerList)) {
                List<Long> eidList = hadAddCustomerList.stream().map(EnterpriseSimpleBO::getId).distinct().collect(Collectors.toList());
                CompletableFuture.runAsync(() -> this.batchDeleteGiveEnterprise(lotteryActivityId, opUserId, eidList), springAsyncConfig.getAsyncExecutor());
            }

        }

        return true;
    }

    /**
     * 批量删除赠送的指定客户
     *
     * @param lotteryActivityId
     * @param opUserId
     * @param idList
     */
    public void batchDeleteGiveEnterprise(Long lotteryActivityId, Long opUserId, List<Long> idList) {
        LotteryActivityGiveEnterpriseDO giveEnterpriseDO = new LotteryActivityGiveEnterpriseDO();
        giveEnterpriseDO.setOpUserId(opUserId);
        LambdaQueryWrapper<LotteryActivityGiveEnterpriseDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LotteryActivityGiveEnterpriseDO::getLotteryActivityId, lotteryActivityId);
        queryWrapper.in(LotteryActivityGiveEnterpriseDO::getEid, idList);
        this.batchDeleteWithFill(giveEnterpriseDO, queryWrapper);
    }

    /**
     * 批量添加赠送的指定客户
     *
     * @param lotteryActivityId
     * @param opUserId
     * @param eidList
     */
    public void batchSaveGiveEnterprise(Long lotteryActivityId, Long opUserId, List<Long> eidList) {
        // 已经添加的
        List<Long> eidDoList = this.getGiveEnterpriseByActivityId(lotteryActivityId);
        List<Long> newEidList = eidList.stream().filter(eid -> !eidDoList.contains(eid)).collect(Collectors.toList());

        if (CollUtil.isNotEmpty(newEidList)) {
            List<LotteryActivityGiveEnterpriseDO> enterpriseDOList = newEidList.stream().map(eid -> {
                LotteryActivityGiveEnterpriseDO giveEnterpriseDO = new LotteryActivityGiveEnterpriseDO();
                giveEnterpriseDO.setLotteryActivityId(lotteryActivityId);
                giveEnterpriseDO.setEid(eid);
                giveEnterpriseDO.setOpUserId(opUserId);
                return giveEnterpriseDO;
            }).collect(Collectors.toList());

            this.saveBatch(enterpriseDOList);
        }
    }

}
