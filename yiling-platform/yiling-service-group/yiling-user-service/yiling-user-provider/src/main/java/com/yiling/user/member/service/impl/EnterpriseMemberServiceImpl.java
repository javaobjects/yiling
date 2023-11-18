package com.yiling.user.member.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.entity.EnterpriseDO;
import com.yiling.user.enterprise.service.EnterpriseService;
import com.yiling.user.member.bo.EnterpriseMemberBO;
import com.yiling.user.member.bo.MemberEnterpriseBO;
import com.yiling.user.member.bo.MemberExpiredBO;
import com.yiling.user.member.dao.EnterpriseMemberMapper;
import com.yiling.user.member.dto.EnterpriseMemberDTO;
import com.yiling.user.member.dto.MemberBuyRecordDTO;
import com.yiling.user.member.dto.request.QueryEnterpriseMemberRequest;
import com.yiling.user.member.dto.request.UpdateReturnEnterpriseMemberRequest;
import com.yiling.user.member.entity.EnterpriseMemberDO;
import com.yiling.user.member.entity.MemberDO;
import com.yiling.user.member.entity.MemberExpiredWarnRecordDO;
import com.yiling.user.member.service.EnterpriseMemberService;
import com.yiling.user.member.service.MemberBuyRecordService;
import com.yiling.user.member.service.MemberExpiredWarnRecordService;
import com.yiling.user.member.service.MemberService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 企业会员 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/25
 */
@Slf4j
@Service
public class EnterpriseMemberServiceImpl extends BaseServiceImpl<EnterpriseMemberMapper, EnterpriseMemberDO> implements EnterpriseMemberService {

    @Autowired
    MemberBuyRecordService memberBuyRecordService;
    @Autowired
    EnterpriseService enterpriseService;
    @Autowired
    MemberService memberService;
    @Autowired
    MemberExpiredWarnRecordService memberExpiredWarnRecordService;

    @Override
    public EnterpriseMemberDO getEnterpriseMember(Long eid, Long memberId) {
        LambdaQueryWrapper<EnterpriseMemberDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EnterpriseMemberDO::getEid, eid);
        queryWrapper.eq(EnterpriseMemberDO::getMemberId, memberId);
        queryWrapper.last("limit 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public List<Long> getEnterpriseByMemberId(Long memberId) {
        LambdaQueryWrapper<EnterpriseMemberDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EnterpriseMemberDO::getMemberId,memberId);
        queryWrapper.gt(EnterpriseMemberDO::getEndTime,new Date());

        return this.list(queryWrapper).stream().map(EnterpriseMemberDO::getEid).collect(Collectors.toList());
    }

    @Override
    public Page<EnterpriseMemberBO> queryEnterpriseMemberPage(QueryEnterpriseMemberRequest request) {
        return baseMapper.queryEnterpriseMemberPage(request.getPage(), request);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateReturnEnterpriseMember(UpdateReturnEnterpriseMemberRequest request) {
        LambdaQueryWrapper<EnterpriseMemberDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EnterpriseMemberDO::getEid, request.getEid());
        queryWrapper.eq(EnterpriseMemberDO::getMemberId, request.getMemberId());
        queryWrapper.gt(EnterpriseMemberDO::getEndTime, new Date());
        queryWrapper.last("limit 1");
        EnterpriseMemberDO enterpriseMemberDO = this.getOne(queryWrapper);

        if (Objects.isNull(enterpriseMemberDO)) {
            return true;
        }

        MemberBuyRecordDTO memberBuyRecordDTO = memberBuyRecordService.getBuyRecordByOrderNo(request.getOrderNo());
        // 当前退款订单所购买的会员时长
        Integer validDays = memberBuyRecordDTO.getValidDays();

        EnterpriseMemberDO memberDO = new EnterpriseMemberDO();
        memberDO.setId(enterpriseMemberDO.getId());
        // 到期时间 减去 退款订单的时长 等于 退款后企业会员的时长
        memberDO.setEndTime(DateUtil.offsetDay(enterpriseMemberDO.getEndTime(), -validDays));
        memberDO.setOpUserId(request.getOpUserId());
        log.info("企业会员退款，企业ID：{}，会员订单号：{}，当前退款订单所购买的会员时长：{}", request.getEid(), request.getOrderNo(), validDays);

        return this.updateById(memberDO);
    }

    @Override
    public List<EnterpriseDTO> getMemberEnterprise() {
        LambdaQueryWrapper<EnterpriseMemberDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.gt(EnterpriseMemberDO::getEndTime,new Date());
        List<Long> eidList = this.list(queryWrapper).stream().map(EnterpriseMemberDO::getEid).distinct().collect(Collectors.toList());
        if (CollUtil.isEmpty(eidList)) {
            return ListUtil.toList();
        }

        List<EnterpriseDO> enterpriseDOList = enterpriseService.listByIds(eidList);
        return PojoUtils.map(enterpriseDOList, EnterpriseDTO.class);
    }

    @Override
    public Map<Long, List<Long>> getEidListByMemberId(List<Long> memberIdList) {
        LambdaQueryWrapper<EnterpriseMemberDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(EnterpriseMemberDO::getMemberId, memberIdList);
        queryWrapper.gt(EnterpriseMemberDO::getEndTime,new Date());
        List<EnterpriseMemberDO> memberDOList = this.list(queryWrapper);

        Map<Long, List<EnterpriseMemberDO>> memberEnterpriseMap = memberDOList.stream().collect(Collectors.groupingBy(EnterpriseMemberDO::getMemberId));
        Map<Long, List<Long>> map = MapUtil.newHashMap();
        memberEnterpriseMap.forEach((memberId, list) -> {
            if (CollUtil.isNotEmpty(list)) {
                map.put(memberId, list.stream().map(EnterpriseMemberDO::getEid).collect(Collectors.toList()));
            }
        });

        return map;
    }

    @Override
    public EnterpriseMemberBO getDetail(Long id) {
        EnterpriseMemberDO enterpriseMemberDO = Optional.ofNullable(this.getById(id)).orElseThrow(() -> new BusinessException(UserErrorCode.ENTERPRISE_MEMBER_NOT_EXIST));
        EnterpriseMemberBO enterpriseMemberBO = PojoUtils.map(enterpriseMemberDO, EnterpriseMemberBO.class);

        EnterpriseDO enterpriseDO = Optional.ofNullable(enterpriseService.getById(enterpriseMemberDO.getEid())).orElseThrow(() -> new BusinessException(UserErrorCode.ENTERPRISE_NOT_EXISTS));
        PojoUtils.map(enterpriseDO, enterpriseMemberBO);
        enterpriseMemberBO.setEname(enterpriseDO.getName());
        enterpriseMemberBO.setEnterpriseStatus(enterpriseDO.getStatus());

        MemberDO memberDO = Optional.ofNullable(memberService.getById(enterpriseMemberDO.getMemberId())).orElseThrow(() -> new BusinessException(UserErrorCode.MEMBER_NOT_EXIST));
        enterpriseMemberBO.setMemberName(memberDO.getName());
        enterpriseMemberBO.setExtinguishPicture(memberDO.getExtinguishPicture());
        enterpriseMemberBO.setLightPicture(memberDO.getLightPicture());

        return enterpriseMemberBO;
    }

    @Override
    public List<MemberEnterpriseBO> getMemberListByEid(Long eid) {
        // 获取所有会员
        LambdaQueryWrapper<MemberDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MemberDO::getStopGet, 0);
        List<MemberDO> memberDOList = memberService.list(queryWrapper);

        return memberDOList.stream().map(memberDO -> {
            MemberEnterpriseBO memberEnterpriseBO = new MemberEnterpriseBO();

            EnterpriseMemberDO enterpriseMember = this.getEnterpriseMember(eid, memberDO.getId());
            if (Objects.nonNull(enterpriseMember) && enterpriseMember.getEndTime().after(new Date())) {
                memberEnterpriseBO.setMemberFlag(true);
                memberEnterpriseBO.setStartTime(enterpriseMember.getStartTime());
                memberEnterpriseBO.setEndTime(enterpriseMember.getEndTime());
            } else {
                memberEnterpriseBO.setMemberFlag(false);
            }
            memberEnterpriseBO.setMemberId(memberDO.getId());
            memberEnterpriseBO.setMemberName(memberDO.getName());
            memberEnterpriseBO.setBgPicture(memberDO.getBgPicture());
            memberEnterpriseBO.setLightPicture(memberDO.getLightPicture());
            memberEnterpriseBO.setExtinguishPicture(memberDO.getExtinguishPicture());
            return memberEnterpriseBO;

        }).collect(Collectors.toList());
    }

    @Override
    public List<MemberExpiredBO> getMemberExpiredList(Long currentEid, Long currentUserId) {
        LambdaQueryWrapper<EnterpriseMemberDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EnterpriseMemberDO::getEid, currentEid);
        List<EnterpriseMemberDO> memberDOList = this.list(wrapper);

        Date now = new Date();
        List<MemberExpiredBO> list = ListUtil.toList();

        memberDOList.forEach(enterpriseMemberDO -> {
            MemberDO memberDO = memberService.getById(enterpriseMemberDO.getMemberId());
            if (memberDO.getRenewalWarn() == 0) {
                return;
            }
            // 到期前提醒天数
            Integer warnDays = memberDO.getWarnDays();
            // 到期时间
            Date endTime = enterpriseMemberDO.getEndTime();

            // 校验是否已经提醒
            LambdaQueryWrapper<MemberExpiredWarnRecordDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(MemberExpiredWarnRecordDO::getEid, enterpriseMemberDO.getEid());
            queryWrapper.eq(MemberExpiredWarnRecordDO::getMemberId, enterpriseMemberDO.getMemberId());
            queryWrapper.eq(MemberExpiredWarnRecordDO::getEndTime, endTime);
            int count = memberExpiredWarnRecordService.count(queryWrapper);
            if (count > 0) {
                log.info("会员到期提醒企业ID={} 会员ID={} 查询到已经提醒", enterpriseMemberDO.getEid(), enterpriseMemberDO.getMemberId());
                return;
            }

            if (DateUtil.offsetDay(endTime, -warnDays).before(now)) {
                MemberExpiredBO memberExpiredBO = new MemberExpiredBO();
                memberExpiredBO.setMemberId(enterpriseMemberDO.getMemberId());
                memberExpiredBO.setMemberName(memberDO.getName());
                memberExpiredBO.setExpiredDate(endTime);
                memberExpiredBO.setEid(currentEid);
                memberExpiredBO.setStartTime(enterpriseMemberDO.getStartTime());
                memberExpiredBO.setEndTime(endTime);
                memberExpiredBO.setWarnDays(warnDays);
                list.add(memberExpiredBO);
            }
        });

        if (CollUtil.isNotEmpty(list)) {
            // 记录入库
            List<MemberExpiredWarnRecordDO> expiredWarnRecordDOList = PojoUtils.map(list, MemberExpiredWarnRecordDO.class);
            expiredWarnRecordDOList.forEach(memberExpiredWarnRecordDO -> memberExpiredWarnRecordDO.setOpUserId(currentUserId));
            memberExpiredWarnRecordService.saveBatch(expiredWarnRecordDOList);
        }

        return list;
    }

    @Override
    public boolean getEnterpriseMemberStatus(Long eid) {
        LambdaQueryWrapper<EnterpriseMemberDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EnterpriseMemberDO::getEid, eid);
        queryWrapper.gt(EnterpriseMemberDO::getEndTime, new Date());
        return this.count(queryWrapper) > 0;
    }

    @Override
    public List<Long> getEnterpriseByMemberList(List<Long> memberIdList) {
        if (CollUtil.isEmpty(memberIdList)) {
            return ListUtil.toList();
        }

        LambdaQueryWrapper<EnterpriseMemberDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(EnterpriseMemberDO::getMemberId, memberIdList);
        wrapper.gt(EnterpriseMemberDO::getEndTime, new Date());
        return this.list(wrapper).stream().map(EnterpriseMemberDO::getEid).distinct().collect(Collectors.toList());
    }

    @Override
    public List<Long> getMemberByEid(Long eid) {
        LambdaQueryWrapper<EnterpriseMemberDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EnterpriseMemberDO::getEid, eid);
        wrapper.gt(EnterpriseMemberDO::getEndTime, new Date());
        return this.list(wrapper).stream().map(EnterpriseMemberDO::getMemberId).distinct().collect(Collectors.toList());
    }


}
