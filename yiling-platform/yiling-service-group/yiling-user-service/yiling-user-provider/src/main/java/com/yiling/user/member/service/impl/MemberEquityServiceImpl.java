package com.yiling.user.member.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.member.dao.MemberEquityMapper;
import com.yiling.user.member.dao.MemberEquityRelationMapper;
import com.yiling.user.member.dto.MemberEquityDTO;
import com.yiling.user.member.dto.request.CreateMemberEquityRequest;
import com.yiling.user.member.dto.request.QueryMemberEquityRequest;
import com.yiling.user.member.dto.request.UpdateMemberEquityRequest;
import com.yiling.user.member.entity.MemberEquityDO;
import com.yiling.user.member.entity.MemberEquityRelationDO;
import com.yiling.user.member.service.MemberEquityService;

import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * 权益表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/26
 */
@Service
public class MemberEquityServiceImpl extends BaseServiceImpl<MemberEquityMapper, MemberEquityDO> implements MemberEquityService {

    @Autowired
    MemberEquityRelationMapper memberEquityRelationMapper;
    @Autowired
    MemberEquityMapper memberEquityMapper;

    @Override
    public Page<MemberEquityDTO> queryListPage(QueryMemberEquityRequest request) {
        LambdaQueryWrapper<MemberEquityDO> queryWrapper = getMemberEquityDoLambdaQueryWrapper(request);

        return PojoUtils.map(this.page(request.getPage(),queryWrapper),MemberEquityDTO.class);
    }

    private LambdaQueryWrapper<MemberEquityDO> getMemberEquityDoLambdaQueryWrapper(QueryMemberEquityRequest request) {
        LambdaQueryWrapper<MemberEquityDO> queryWrapper = new LambdaQueryWrapper<>();
        if (Objects.nonNull(request.getType()) && request.getType() != 0) {
            queryWrapper.eq(MemberEquityDO::getType, request.getType());
        }
        if (StrUtil.isNotEmpty(request.getName())) {
            queryWrapper.like(MemberEquityDO::getName, request.getName());
        }
        if (Objects.nonNull(request.getStatus())) {
            queryWrapper.eq(MemberEquityDO::getStatus,request.getStatus());
        }

        return queryWrapper;
    }

    @Override
    public Boolean updateStatus(Long id, Long opUserId) {
        MemberEquityDO memberEquityDO = Optional.ofNullable(getById(id)).orElseThrow(()->new BusinessException(UserErrorCode.EQUITY_NOT_EXIST));
        memberEquityDO.setStatus(memberEquityDO.getStatus() == 0 ? 1 : 0);
        memberEquityDO.setOpUserId(opUserId);
        memberEquityDO.setUpdateTime(new Date());

        // 校验如果有会员在使用此权益，则提示无法停用
        if (memberEquityDO.getStatus() == 0) {
            LambdaQueryWrapper<MemberEquityRelationDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(MemberEquityRelationDO::getEquityId, id);
            Integer count = memberEquityRelationMapper.selectCount(wrapper);
            if (count > 0) {
                throw new BusinessException(UserErrorCode.MEMBER_EQUITY_GOING);
            }
        }

        return updateById(memberEquityDO);
    }

    @Override
    public Boolean createEquity(CreateMemberEquityRequest request) {
        MemberEquityDO memberEquityDO = PojoUtils.map(request,MemberEquityDO.class);

        //权益名称重复校验
        LambdaQueryWrapper<MemberEquityDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MemberEquityDO::getName,request.getName());
        int count = this.count(queryWrapper);
        if(count > 0){
            throw new BusinessException(UserErrorCode.EQUITY_NAME_EXIST);
        }

        return this.save(memberEquityDO);
    }

    @Override
    public Boolean updateEquity(UpdateMemberEquityRequest request) {
        if (request.getStatus() == 0) {
            // 校验如果有会员在使用此权益，则提示无法关闭
            LambdaQueryWrapper<MemberEquityRelationDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(MemberEquityRelationDO::getEquityId, request.getId());
            Integer count = memberEquityRelationMapper.selectCount(wrapper);
            if (count > 0) {
                throw new BusinessException(UserErrorCode.MEMBER_EQUITY_GOING);
            }
        }

        //权益名称重复校验
        LambdaQueryWrapper<MemberEquityDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MemberEquityDO::getName, request.getName());
        queryWrapper.last("limit 1");
        MemberEquityDO equityDO = this.getOne(queryWrapper);
        if(Objects.nonNull(equityDO) && equityDO.getId().compareTo(request.getId()) != 0){
            throw new BusinessException(UserErrorCode.EQUITY_NAME_EXIST);
        }

        MemberEquityDO memberEquityDO = PojoUtils.map(request,MemberEquityDO.class);

        return this.updateById(memberEquityDO);
    }

    @Override
    public Boolean deleteEquity(Long id , Long opUserId) {
        // 校验如果有会员在使用此权益，则提示无法删除
        LambdaQueryWrapper<MemberEquityRelationDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemberEquityRelationDO::getEquityId, id);
        Integer count = memberEquityRelationMapper.selectCount(wrapper);
        if (count > 0) {
            throw new BusinessException(UserErrorCode.MEMBER_EQUITY_GOING);
        }

        MemberEquityDO memberEquityDO = Optional.ofNullable(getById(id)).orElseThrow(()->new BusinessException(UserErrorCode.EQUITY_NOT_EXIST));
        memberEquityDO.setOpUserId(opUserId);

        return this.deleteByIdWithFill(memberEquityDO) > 0;
    }

    @Override
    public List<MemberEquityDTO> queryList(QueryMemberEquityRequest request) {
        LambdaQueryWrapper<MemberEquityDO> queryWrapper = getMemberEquityDoLambdaQueryWrapper(request);

        return PojoUtils.map(this.list(queryWrapper),MemberEquityDTO.class);
    }

    @Override
    public MemberEquityDTO getEquity(Long id) {

        return PojoUtils.map(this.getById(id),MemberEquityDTO.class);
    }
}
