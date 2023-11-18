package com.yiling.user.member.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.member.api.EnterpriseMemberApi;
import com.yiling.user.member.bo.EnterpriseMemberBO;
import com.yiling.user.member.bo.MemberEnterpriseBO;
import com.yiling.user.member.bo.MemberExpiredBO;
import com.yiling.user.member.dto.EnterpriseMemberDTO;
import com.yiling.user.member.dto.request.QueryEnterpriseMemberRequest;
import com.yiling.user.member.service.EnterpriseMemberService;

import lombok.extern.slf4j.Slf4j;

/**
 * 企业会员 API 实现
 *
 * @author: lun.yu
 * @date: 2022-09-28
 */
@Slf4j
@DubboService
public class EnterpriseMemberApiImpl implements EnterpriseMemberApi {

    @Autowired
    EnterpriseMemberService enterpriseMemberService;

    @Override
    public Page<EnterpriseMemberBO> queryEnterpriseMemberPage(QueryEnterpriseMemberRequest request) {
        return enterpriseMemberService.queryEnterpriseMemberPage(request);
    }

    @Override
    public EnterpriseMemberBO getDetail(Long id) {
        return enterpriseMemberService.getDetail(id);
    }

    @Override
    public List<MemberEnterpriseBO> getMemberListByEid(Long eid) {
        return enterpriseMemberService.getMemberListByEid(eid);
    }

    @Override
    public List<EnterpriseDTO> getMemberEnterprise() {
        return enterpriseMemberService.getMemberEnterprise();
    }

    @Override
    public boolean getEnterpriseMemberStatus(Long eid) {
        return enterpriseMemberService.getEnterpriseMemberStatus(eid);
    }

    @Override
    public EnterpriseMemberDTO getEnterpriseMember(Long eid, Long memberId) {
        return PojoUtils.map(enterpriseMemberService.getEnterpriseMember(eid, memberId), EnterpriseMemberDTO.class);
    }

    @Override
    public List<MemberExpiredBO> getMemberExpiredList(Long currentEid, Long currentUserId) {
        return enterpriseMemberService.getMemberExpiredList(currentEid, currentUserId);
    }

    @Override
    public List<Long> getEnterpriseByMemberList(List<Long> memberIdList) {
        return enterpriseMemberService.getEnterpriseByMemberList(memberIdList);
    }

    @Override
    public List<Long> getMemberByEid(Long eid) {
        return enterpriseMemberService.getMemberByEid(eid);
    }
}
