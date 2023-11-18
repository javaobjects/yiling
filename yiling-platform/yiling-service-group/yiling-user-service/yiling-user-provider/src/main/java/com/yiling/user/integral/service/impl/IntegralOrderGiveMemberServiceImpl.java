package com.yiling.user.integral.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.integral.dto.request.AddIntegralGiveMemberRequest;
import com.yiling.user.integral.dto.request.DeleteIntegralGiveMemberRequest;
import com.yiling.user.integral.dto.request.QueryIntegralGiveMemberPageRequest;
import com.yiling.user.integral.entity.IntegralGiveRuleDO;
import com.yiling.user.integral.entity.IntegralOrderGiveMemberDO;
import com.yiling.user.integral.dao.IntegralOrderGiveMemberMapper;
import com.yiling.user.integral.service.IntegralOrderGiveMemberService;
import com.yiling.framework.common.base.BaseServiceImpl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 订单送积分-用户类型指定会员方案表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-29
 */
@Slf4j
@Service
public class IntegralOrderGiveMemberServiceImpl extends BaseServiceImpl<IntegralOrderGiveMemberMapper, IntegralOrderGiveMemberDO> implements IntegralOrderGiveMemberService {

    @Override
    public List<Long> getByGiveRuleId(Long giveRuleId) {
        LambdaQueryWrapper<IntegralOrderGiveMemberDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralOrderGiveMemberDO::getGiveRuleId, giveRuleId);
        return this.list(wrapper).stream().map(IntegralOrderGiveMemberDO::getMemberId).distinct().collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(AddIntegralGiveMemberRequest request) {
        IntegralOrderGiveMemberDO giveMemberDO = this.queryByRuleIdAndMemberId(request.getGiveRuleId(), request.getMemberId());
        if (Objects.nonNull(giveMemberDO)) {
            return true;
        }
        IntegralOrderGiveMemberDO orderGiveMemberDO = PojoUtils.map(request, IntegralOrderGiveMemberDO.class);
        return this.save(orderGiveMemberDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(DeleteIntegralGiveMemberRequest request) {
        IntegralOrderGiveMemberDO giveMemberDO = new IntegralOrderGiveMemberDO();
        giveMemberDO.setOpUserId(request.getOpUserId());

        LambdaQueryWrapper<IntegralOrderGiveMemberDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralOrderGiveMemberDO::getGiveRuleId, request.getGiveRuleId());
        wrapper.eq(IntegralOrderGiveMemberDO::getMemberId, request.getMemberId());
        this.batchDeleteWithFill(giveMemberDO, wrapper);

        return true;
    }

    @Override
    public Page<IntegralOrderGiveMemberDO> pageList(QueryIntegralGiveMemberPageRequest request) {
        QueryWrapper<IntegralOrderGiveMemberDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(IntegralOrderGiveMemberDO::getGiveRuleId, request.getGiveRuleId());
        wrapper.lambda().orderByDesc(IntegralOrderGiveMemberDO::getCreateTime);
        return this.page(request.getPage(), wrapper);
    }

    @Override
    public Integer countMemberByRuleId(Long giveRuleId) {
        QueryWrapper<IntegralOrderGiveMemberDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(IntegralOrderGiveMemberDO::getGiveRuleId, giveRuleId);
        return this.count(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void copy(IntegralGiveRuleDO giveRuleDO, Long oldId, Long opUserId) {
        List<IntegralOrderGiveMemberDO> orderGiveMemberDOS = this.listMemberByRuleId(oldId);
        if (CollUtil.isEmpty(orderGiveMemberDOS)) {
            return;
        }

        for (IntegralOrderGiveMemberDO memberLimitDO : orderGiveMemberDOS) {
            memberLimitDO.setId(null);
            memberLimitDO.setGiveRuleId(giveRuleDO.getId());
            memberLimitDO.setOpUserId(opUserId);
        }
        this.saveBatch(orderGiveMemberDOS);
    }

    @Override
    public List<IntegralOrderGiveMemberDO> listMemberByRuleId(Long giveRuleId) {
        LambdaQueryWrapper<IntegralOrderGiveMemberDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralOrderGiveMemberDO::getGiveRuleId, giveRuleId);
        return this.list(wrapper);
    }

    @Override
    public IntegralOrderGiveMemberDO queryByRuleIdAndMemberId(Long giveRuleId, Long memberId) {
        LambdaQueryWrapper<IntegralOrderGiveMemberDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralOrderGiveMemberDO::getGiveRuleId, giveRuleId);
        wrapper.eq(IntegralOrderGiveMemberDO::getMemberId, memberId);
        wrapper.last(" limit 1");
        return this.getOne(wrapper);
    }

}
