package com.yiling.user.member.api.impl;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseDO;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.member.api.MemberApi;
import com.yiling.user.member.bo.MemberBuyRecordBO;
import com.yiling.user.member.bo.MemberEnterpriseBO;
import com.yiling.user.member.bo.MemberExpiredBO;
import com.yiling.user.member.dto.CurrentMemberDTO;
import com.yiling.user.member.dto.EnterpriseMemberDTO;
import com.yiling.user.member.dto.MemberBuyRecordDTO;
import com.yiling.user.member.dto.MemberDTO;
import com.yiling.user.member.dto.MemberDetailDTO;
import com.yiling.user.member.dto.MemberOrderCouponDTO;
import com.yiling.user.member.dto.MemberOrderDTO;
import com.yiling.user.member.dto.MemberSimpleDTO;
import com.yiling.user.member.dto.request.CreateMemberRequest;
import com.yiling.user.member.dto.request.CurrentMemberForMarketingDTO;
import com.yiling.user.member.dto.request.ImportOpenMemberRequest;
import com.yiling.user.member.dto.request.OpenMemberRequest;
import com.yiling.user.member.dto.request.QueryBuyRecordRequest;
import com.yiling.user.member.dto.request.QueryEnterpriseMemberRequest;
import com.yiling.user.member.dto.request.QueryMemberBuyRecordRequest;
import com.yiling.user.member.dto.request.QueryMemberBuyStagePageRequest;
import com.yiling.user.member.dto.request.QueryMemberCouponPageRequest;
import com.yiling.user.member.dto.request.QueryMemberOrderPageRequest;
import com.yiling.user.member.dto.request.QueryMemberRequest;
import com.yiling.user.member.dto.request.UpdateMemberPromoterRequest;
import com.yiling.user.member.dto.request.UpdateMemberRequest;
import com.yiling.user.member.dto.request.UpdateMemberReturnRequest;
import com.yiling.user.member.dto.request.UpdateMemberSortRequest;
import com.yiling.user.member.entity.MemberBuyStageDO;
import com.yiling.user.member.entity.MemberDO;
import com.yiling.user.member.entity.MemberOrderCouponDO;
import com.yiling.user.member.service.EnterpriseMemberService;
import com.yiling.user.member.service.MemberBuyRecordService;
import com.yiling.user.member.service.MemberBuyStageService;
import com.yiling.user.member.service.MemberImportOpenService;
import com.yiling.user.member.service.MemberOrderCouponService;
import com.yiling.user.member.service.MemberOrderService;
import com.yiling.user.member.service.MemberReturnService;
import com.yiling.user.member.service.MemberService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 会员 API 实现
 *
 * @author: lun.yu
 * @date: 2021/10/25
 */
@Slf4j
@DubboService
public class MemberApiImpl implements MemberApi {

    @Autowired
    MemberService memberService;
    @Autowired
    EnterpriseMemberService enterpriseMemberService;
    @Autowired
    MemberBuyRecordService memberBuyRecordService;
    @Autowired
    MemberReturnService memberReturnService;
    @Autowired
    MemberOrderService memberOrderService;
    @Autowired
    MemberBuyStageService memberBuyStageService;
    @Autowired
    MemberImportOpenService memberImportOpenService;
    @Autowired
    MemberOrderCouponService memberOrderCouponService;

    @Override
    public Page<MemberDTO> queryListPage(QueryMemberRequest request) {
        return memberService.queryListPage(request);
    }

    @Override
    public List<MemberSimpleDTO> queryAllList() {
        return memberService.queryAllList();
    }

    @Override
    public List<Long> listIdsByName(String name) {
        LambdaQueryWrapper<MemberDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(MemberDO::getName, name);
        return memberService.list(wrapper).stream().map(MemberDO::getId).distinct().collect(Collectors.toList());
    }

    @Override
    public List<MemberDTO> listByIds(List<Long> ids) {
        return PojoUtils.map(memberService.listByIds(ids), MemberDTO.class);
    }

    @Override
    public List<Long> getEnterpriseByMemberId(Long memberId) {
        return enterpriseMemberService.getEnterpriseByMemberId(memberId);
    }

    @Override
    public Boolean stopGet(Long id,Long opUserId) {
        return memberService.stopGet(id,opUserId);
    }

    @Override
    public Boolean createMember(CreateMemberRequest request) {
        return memberService.createMember(request);
    }

    @Override
    public MemberDetailDTO getMember(Long id) {
        return memberService.getMember(id);
    }

    @Override
    public MemberDTO getById(Long id) {
        return PojoUtils.map(memberService.getById(id), MemberDTO.class);
    }

    @Override
    public Boolean updateMember(UpdateMemberRequest request) {
        return memberService.updateMember(request);
    }

    @Override
    public CurrentMemberDTO getCurrentMember(Long currentEid, Long memberId) {
        return memberService.getCurrentMember(currentEid, memberId);
    }

    @Override
    public CurrentMemberDTO getCurrentMember(Long currentEid) {
        List<MemberSimpleDTO> simpleDTOList = this.queryAllList();
        return memberService.getCurrentMember(currentEid, simpleDTOList.get(0).getId());
    }

    @Override
    public Boolean openMember(OpenMemberRequest request) {
        return memberService.openMember(request);
    }

    @Override
    public Boolean updateBuyMemberPromoter(UpdateMemberPromoterRequest request) {
        return memberBuyRecordService.updateBuyMemberPromoter(request);
    }

    @Override
    public boolean importBuyMember(ImportOpenMemberRequest request) {
        return memberImportOpenService.importBuyMember(request);
    }

    @Override
    public List<MemberEnterpriseBO> getMemberListByEid(Long eid) {
        return enterpriseMemberService.getMemberListByEid(eid);
    }

    @Override
    public Map<Long, List<Long>> getEidListByMemberId(List<Long> memberIdList) {
        return enterpriseMemberService.getEidListByMemberId(memberIdList);
    }

    @Override
    public Map<Long, List<Long>> getEidListByBuyStageId(List<Long> buyStageIdList) {
        return memberBuyStageService.getEidListByBuyStageId(buyStageIdList);
    }

    @Override
    public Map<Long, List<Long>> getEidByPromoterId(List<Long> promoterIdList) {
        return memberBuyRecordService.getEidByPromoterId(promoterIdList);
    }

    @Override
    public boolean updateSort(UpdateMemberSortRequest request) {
        return memberService.updateSort(request);
    }

    @Override
    public CurrentMemberForMarketingDTO getCurrentMemberForMarketing(Long currentEid) {
        return memberService.getCurrentMemberForMarketing(currentEid);
    }

}
