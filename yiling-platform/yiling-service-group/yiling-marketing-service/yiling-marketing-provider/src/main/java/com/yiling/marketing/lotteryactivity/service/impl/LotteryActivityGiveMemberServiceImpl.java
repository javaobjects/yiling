package com.yiling.marketing.lotteryactivity.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.lotteryactivity.bo.MemberSimpleBO;
import com.yiling.marketing.lotteryactivity.dto.request.AddOrDeleteMemberRequest;
import com.yiling.marketing.lotteryactivity.dto.request.QueryMemberPageRequest;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityGiveMemberDO;
import com.yiling.marketing.lotteryactivity.dao.LotteryActivityGiveMemberMapper;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityErrorCode;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityGiveMemberService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.user.member.api.MemberApi;
import com.yiling.user.member.dto.MemberDTO;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.collection.CollUtil;

/**
 * <p>
 * 抽奖活动-赠送范围用户类型指定会员表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-08-29
 */
@Service
public class LotteryActivityGiveMemberServiceImpl extends BaseServiceImpl<LotteryActivityGiveMemberMapper, LotteryActivityGiveMemberDO> implements LotteryActivityGiveMemberService {

    @DubboReference
    MemberApi memberApi;

    @Override
    public Page<MemberSimpleBO> queryHadAddMemberPage(QueryMemberPageRequest request) {
        LambdaQueryWrapper<LotteryActivityGiveMemberDO> wrapper = new LambdaQueryWrapper<>();
        if (Objects.nonNull(request.getLotteryActivityId()) && request.getLotteryActivityId() != 0) {
            wrapper.eq(LotteryActivityGiveMemberDO::getLotteryActivityId, request.getLotteryActivityId());
        }
        Page<LotteryActivityGiveMemberDO> giveMemberDOPage = this.page(request.getPage(), wrapper);
        Page<MemberSimpleBO> simpleBOPage = PojoUtils.map(giveMemberDOPage, MemberSimpleBO.class);
        if (CollUtil.isEmpty(giveMemberDOPage.getRecords())) {
            return simpleBOPage;
        }

        List<Long> idList = giveMemberDOPage.getRecords().stream().map(LotteryActivityGiveMemberDO::getMemberId).collect(Collectors.toList());
        Map<Long, String> nameMap = memberApi.listByIds(idList).stream().collect(Collectors.toMap(BaseDTO::getId, MemberDTO::getName));

        List<MemberSimpleBO> memberSimpleBOS = giveMemberDOPage.getRecords().stream().map(lotteryActivityGiveMemberDO -> {
            MemberSimpleBO memberSimpleBO = new MemberSimpleBO();
            memberSimpleBO.setId(lotteryActivityGiveMemberDO.getMemberId());
            memberSimpleBO.setName(nameMap.get(memberSimpleBO.getId()));
            return memberSimpleBO;
        }).collect(Collectors.toList());
        simpleBOPage.setRecords(memberSimpleBOS);

        return simpleBOPage;
    }

    @Override
    public List<Long> getGiveMemberByActivityId(Long lotteryActivityId) {
        LambdaQueryWrapper<LotteryActivityGiveMemberDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LotteryActivityGiveMemberDO::getLotteryActivityId, lotteryActivityId);
        return this.list(wrapper).stream().map(LotteryActivityGiveMemberDO::getMemberId).distinct().collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateGiveMemberByLotteryActivityId(Long lotteryActivityId, List<Long> memberIdList, Long opUserId) {
        List<Long> memberIdDoList = this.getGiveMemberByActivityId(lotteryActivityId);

        List<Long> addList = memberIdList.stream().filter(eid -> !memberIdDoList.contains(eid)).collect(Collectors.toList());
        List<Long> removeList = memberIdDoList.stream().filter(eid -> !memberIdList.contains(eid)).collect(Collectors.toList());

        // 添加会员方案
        if (CollUtil.isNotEmpty(addList)) {
            List<LotteryActivityGiveMemberDO> giveMemberDOList = addList.stream().map(memberId -> {
                LotteryActivityGiveMemberDO giveMemberDO = new LotteryActivityGiveMemberDO();
                giveMemberDO.setLotteryActivityId(lotteryActivityId);
                giveMemberDO.setMemberId(memberId);
                giveMemberDO.setOpUserId(opUserId);
                return giveMemberDO;
            }).collect(Collectors.toList());
            this.saveBatch(giveMemberDOList);
        }

        if (CollUtil.isNotEmpty(removeList)) {
            // 删除赠送指定会员方案数据
            LotteryActivityGiveMemberDO giveMemberDO = new LotteryActivityGiveMemberDO();
            giveMemberDO.setOpUserId(opUserId);
            LambdaQueryWrapper<LotteryActivityGiveMemberDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(LotteryActivityGiveMemberDO::getLotteryActivityId, lotteryActivityId);
            queryWrapper.in(LotteryActivityGiveMemberDO::getMemberId, removeList);
            this.batchDeleteWithFill(giveMemberDO, queryWrapper);
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addMember(AddOrDeleteMemberRequest request) {
        LambdaQueryWrapper<LotteryActivityGiveMemberDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LotteryActivityGiveMemberDO::getLotteryActivityId, request.getLotteryActivityId());
        wrapper.eq(LotteryActivityGiveMemberDO::getMemberId, request.getMemberId());
        wrapper.last("limit 1");
        LotteryActivityGiveMemberDO memberDO = this.getOne(wrapper);
        if (Objects.nonNull(memberDO)) {
            throw new BusinessException(LotteryActivityErrorCode.DATA_HAD_ADD);
        }

        LotteryActivityGiveMemberDO giveMemberDO = PojoUtils.map(request, LotteryActivityGiveMemberDO.class);
        return this.save(giveMemberDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteMember(AddOrDeleteMemberRequest request) {
        LotteryActivityGiveMemberDO giveMemberDO = new LotteryActivityGiveMemberDO();
        giveMemberDO.setOpUserId(request.getOpUserId());
        LambdaQueryWrapper<LotteryActivityGiveMemberDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LotteryActivityGiveMemberDO::getLotteryActivityId, request.getLotteryActivityId());
        queryWrapper.eq(LotteryActivityGiveMemberDO::getMemberId, request.getMemberId());

        return this.batchDeleteWithFill(giveMemberDO, queryWrapper) > 0;
    }


}
