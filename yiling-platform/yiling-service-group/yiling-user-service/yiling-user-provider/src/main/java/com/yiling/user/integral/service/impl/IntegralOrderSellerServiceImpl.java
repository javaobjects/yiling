package com.yiling.user.integral.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.common.util.WrapperUtils;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.entity.EnterpriseDO;
import com.yiling.user.enterprise.service.EnterpriseService;
import com.yiling.user.integral.dao.IntegralOrderSellerMapper;
import com.yiling.user.integral.dto.IntegralOrderSellerDTO;
import com.yiling.user.integral.dto.request.AddIntegralOrderSellerRequest;
import com.yiling.user.integral.dto.request.DeleteIntegralGiveSellerRequest;
import com.yiling.user.integral.dto.request.QueryGiveIntegralSellerPageRequest;
import com.yiling.user.integral.entity.IntegralGiveRuleDO;
import com.yiling.user.integral.entity.IntegralOrderSellerDO;
import com.yiling.user.integral.service.IntegralOrderSellerService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 订单送积分指定商家 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-03
 */
@Slf4j
@Service
public class IntegralOrderSellerServiceImpl extends BaseServiceImpl<IntegralOrderSellerMapper, IntegralOrderSellerDO> implements IntegralOrderSellerService {

    @Override
    public Page<IntegralOrderSellerDTO> pageList(QueryGiveIntegralSellerPageRequest request) {
        QueryWrapper<IntegralOrderSellerDO> wrapper = WrapperUtils.getWrapper(request);
        return PojoUtils.map(this.page(request.getPage(), wrapper), IntegralOrderSellerDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(AddIntegralOrderSellerRequest request) {
        if (Objects.nonNull(request.getEid())) {

            List<IntegralOrderSellerDO> sellerDOList = this.listByRuleIdAndEidList(request.getGiveRuleId(), ListUtil.toList(request.getEid()));
            if (CollUtil.isNotEmpty(sellerDOList)) {
                return true;
            }

            IntegralOrderSellerDO sellerDO = PojoUtils.map(request, IntegralOrderSellerDO.class);
            return this.save(sellerDO);

        } else if (CollUtil.isNotEmpty(request.getEidList())) {
            List<IntegralOrderSellerDO> sellerDOList = new ArrayList<>();

            List<Long> existEidList = this.listByRuleIdAndEidList(request.getGiveRuleId(), request.getEidList()).stream().map(IntegralOrderSellerDO::getEid).distinct().collect(Collectors.toList());

            request.getEidList().forEach(eid -> {
                if (existEidList.contains(eid)) {
                    return;
                }
                IntegralOrderSellerDO sellerDO = new IntegralOrderSellerDO();
                sellerDO.setGiveRuleId(request.getGiveRuleId());
                sellerDO.setEid(eid);
                sellerDO.setOpUserId(request.getOpUserId());
                sellerDOList.add(sellerDO);
            });

            if (CollUtil.isNotEmpty(sellerDOList)) {
                this.saveBatch(sellerDOList);
            }
        }

        return true;
    }

    @Override
    public List<IntegralOrderSellerDO> listByRuleIdAndEidList(Long giveRuleId, List<Long> eidList) {
        if (CollUtil.isEmpty(eidList)) {
            return ListUtil.toList();
        }

        LambdaQueryWrapper<IntegralOrderSellerDO> wrapper = new LambdaQueryWrapper<>();
        if (Objects.nonNull(giveRuleId) && giveRuleId != 0) {
            wrapper.eq(IntegralOrderSellerDO::getGiveRuleId, giveRuleId);
        }
        wrapper.in(IntegralOrderSellerDO::getEid, eidList);
        return this.list(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(DeleteIntegralGiveSellerRequest request) {
        IntegralOrderSellerDO sellerDO = new IntegralOrderSellerDO();
        sellerDO.setOpUserId(request.getOpUserId());
        LambdaQueryWrapper<IntegralOrderSellerDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralOrderSellerDO::getGiveRuleId, request.getGiveRuleId());
        if (Objects.nonNull(request.getEid())) {
            wrapper.eq(IntegralOrderSellerDO::getEid, request.getEid());
        } else if (CollUtil.isNotEmpty(request.getEidList())) {
            wrapper.in(IntegralOrderSellerDO::getEid, request.getEidList());
        }

        return this.batchDeleteWithFill(sellerDO, wrapper) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void copy(IntegralGiveRuleDO giveRuleDO, Long oldId, Long opUserId) {
        List<IntegralOrderSellerDO> sellerDOList = this.listSellerByGiveRuleId(oldId);
        if (CollUtil.isEmpty(sellerDOList)) {
            return;
        }

        for (IntegralOrderSellerDO sellerLimitDO : sellerDOList) {
            sellerLimitDO.setId(null);
            sellerLimitDO.setGiveRuleId(giveRuleDO.getId());
            sellerLimitDO.setOpUserId(opUserId);
        }
        this.saveBatch(sellerDOList);
    }

    @Override
    public Integer countSellerByGiveRuleId(Long giveRuleId) {
        LambdaQueryWrapper<IntegralOrderSellerDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralOrderSellerDO::getGiveRuleId, giveRuleId);
        return this.count(wrapper);
    }

    @Override
    public List<IntegralOrderSellerDO> listSellerByGiveRuleId(Long giveRuleId) {
        LambdaQueryWrapper<IntegralOrderSellerDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralOrderSellerDO::getGiveRuleId, giveRuleId);
        return this.list(wrapper);
    }

    @Override
    public List<IntegralOrderSellerDO> listSellerByGiveRuleIdList(List<Long> giveRuleIdList) {
        LambdaQueryWrapper<IntegralOrderSellerDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(IntegralOrderSellerDO::getGiveRuleId, giveRuleIdList);
        return this.list(wrapper);
    }

}
