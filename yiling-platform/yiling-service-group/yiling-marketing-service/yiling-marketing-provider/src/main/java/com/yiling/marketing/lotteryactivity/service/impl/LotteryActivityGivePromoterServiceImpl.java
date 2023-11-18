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
import com.yiling.marketing.lotteryactivity.bo.EnterpriseSimpleBO;
import com.yiling.marketing.lotteryactivity.dto.request.AddCustomerPageRequest;
import com.yiling.marketing.lotteryactivity.dto.request.DeleteCustomerPageRequest;
import com.yiling.marketing.lotteryactivity.dto.request.QueryCustomerPageRequest;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityGiveEnterpriseDO;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityGiveMemberDO;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityGivePromoterDO;
import com.yiling.marketing.lotteryactivity.dao.LotteryActivityGivePromoterMapper;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityErrorCode;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityGiveEnterpriseService;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityGivePromoterService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.marketing.strategy.enums.StrategyErrorCode;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterpriseByNameRequest;
import com.yiling.user.enterprise.dto.request.QueryEnterprisePageListRequest;
import com.yiling.user.enterprise.enums.EnterpriseAuthStatusEnum;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;

/**
 * <p>
 * 抽奖活动-赠送范围用户类型指定推广方表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-08-29
 */
@Service
public class LotteryActivityGivePromoterServiceImpl extends BaseServiceImpl<LotteryActivityGivePromoterMapper, LotteryActivityGivePromoterDO> implements LotteryActivityGivePromoterService {

    @Autowired
    LotteryActivityGiveEnterpriseService lotteryActivityGiveEnterpriseService;

    @Autowired
    SpringAsyncConfig springAsyncConfig;

    @Override
    public Page<EnterpriseSimpleBO> queryHadAddPromoterPage(QueryCustomerPageRequest request) {
        return baseMapper.queryHadAddPromoterPage(request.getPage(), request);
    }

    @Override
    public List<Long> getGivePromoterByActivityId(Long lotteryActivityId) {
        LambdaQueryWrapper<LotteryActivityGivePromoterDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LotteryActivityGivePromoterDO::getLotteryActivityId, lotteryActivityId);
        return this.list(wrapper).stream().map(LotteryActivityGivePromoterDO::getPromoterId).distinct().collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateGivePromoterByLotteryActivityId(Long lotteryActivityId, List<Long> promoterIdList, Long opUserId) {
        List<Long> promoterIdDoList = this.getGivePromoterByActivityId(lotteryActivityId);

        List<Long> addList = promoterIdList.stream().filter(promoterId -> !promoterIdDoList.contains(promoterId)).collect(Collectors.toList());
        List<Long> removeList = promoterIdDoList.stream().filter(promoterIdDo -> !promoterIdList.contains(promoterIdDo)).collect(Collectors.toList());

        // 添加会员方案
        if (CollUtil.isNotEmpty(addList)) {
            this.batchSavePromoter(lotteryActivityId, opUserId, addList);
        }

        if (CollUtil.isNotEmpty(removeList)) {
            // 删除赠送指定会员方案数据
            this.batchDeletePromoter(lotteryActivityId, opUserId, removeList);
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addPromoter(AddCustomerPageRequest request) {
        Long lotteryActivityId = request.getLotteryActivityId();
        Long opUserId = request.getOpUserId();

        if (request.getType() == 1 || request.getType() == 2) {
            List<Long> eidList = request.getIdList();
            if (CollUtil.isEmpty(eidList)) {
                return true;
            }
            if (eidList.size() == 1) {
                LambdaQueryWrapper<LotteryActivityGivePromoterDO> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(LotteryActivityGivePromoterDO::getLotteryActivityId, lotteryActivityId);
                wrapper.eq(LotteryActivityGivePromoterDO::getPromoterId, eidList.get(0));
                wrapper.last("limit 1");
                LotteryActivityGivePromoterDO promoterDO = this.getOne(wrapper);
                if (Objects.nonNull(promoterDO)) {
                    throw new BusinessException(LotteryActivityErrorCode.DATA_HAD_ADD);
                }
            }

            // 批量保存推广方
            this.batchSavePromoter(lotteryActivityId, opUserId, eidList);

        } else if (request.getType() == 3) {
            // 校验数量是否大于500
            List<EnterpriseDTO> enterpriseDTOList = lotteryActivityGiveEnterpriseService.getEnterpriseDTOList(request, ListUtil.toList(EnterpriseTypeEnum.BUSINESS.getCode()));

            if (CollUtil.isNotEmpty(enterpriseDTOList)) {
                // 将要添加的
                List<Long> eidList = enterpriseDTOList.stream().map(BaseDTO::getId).collect(Collectors.toList());
                // 异步保存添加的推广方
                CompletableFuture.runAsync(() -> this.batchSavePromoter(lotteryActivityId, opUserId, eidList), springAsyncConfig.getAsyncExecutor());
            }

        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deletePromoter(DeleteCustomerPageRequest request) {
        Long lotteryActivityId = request.getLotteryActivityId();
        Long opUserId = request.getOpUserId();

        if (request.getType() == 1 || request.getType() == 2) {
            if (CollUtil.isEmpty(request.getIdList())) {
                return true;
            }

            // 删除赠送指定客户表数据
            this.batchDeletePromoter(lotteryActivityId, opUserId, request.getIdList());

        } else if (request.getType() == 3) {

            QueryCustomerPageRequest pageRequest = new QueryCustomerPageRequest();
            pageRequest.setLotteryActivityId(lotteryActivityId);
            pageRequest.setId(request.getId());
            pageRequest.setName(request.getName());
            List<EnterpriseSimpleBO> hadAddCustomerList = baseMapper.queryHadAddPromoterPage(pageRequest);
            if (CollUtil.isNotEmpty(hadAddCustomerList)) {
                List<Long> eidList = hadAddCustomerList.stream().map(EnterpriseSimpleBO::getId).distinct().collect(Collectors.toList());
                CompletableFuture.runAsync(() -> this.batchDeletePromoter(lotteryActivityId, opUserId, eidList), springAsyncConfig.getAsyncExecutor());
            }

        }
        return true;
    }

    public void batchSavePromoter(Long lotteryActivityId, Long opUserId, List<Long> eidList) {
        // 已经添加的
        List<Long> eidDoList = this.getGivePromoterByActivityId(lotteryActivityId);
        List<Long> newEidList = eidList.stream().filter(eid -> !eidDoList.contains(eid)).collect(Collectors.toList());

        if (CollUtil.isNotEmpty(newEidList)) {
            List<LotteryActivityGivePromoterDO> givePromoterDOList = newEidList.stream().map(promoterId -> {
                LotteryActivityGivePromoterDO givePromoterDO = new LotteryActivityGivePromoterDO();
                givePromoterDO.setLotteryActivityId(lotteryActivityId);
                givePromoterDO.setPromoterId(promoterId);
                givePromoterDO.setOpUserId(opUserId);
                return givePromoterDO;
            }).collect(Collectors.toList());

            this.saveBatch(givePromoterDOList);
        }
    }

    public void batchDeletePromoter(Long lotteryActivityId, Long opUserId, List<Long> removeList) {
        LotteryActivityGivePromoterDO givePromoterDO = new LotteryActivityGivePromoterDO();
        givePromoterDO.setOpUserId(opUserId);
        LambdaQueryWrapper<LotteryActivityGivePromoterDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LotteryActivityGivePromoterDO::getLotteryActivityId, lotteryActivityId);
        queryWrapper.in(LotteryActivityGivePromoterDO::getPromoterId, removeList);
        this.batchDeleteWithFill(givePromoterDO, queryWrapper);
    }

}
