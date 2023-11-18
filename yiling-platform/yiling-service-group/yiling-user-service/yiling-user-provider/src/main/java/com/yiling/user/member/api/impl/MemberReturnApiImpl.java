package com.yiling.user.member.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.member.api.MemberApi;
import com.yiling.user.member.api.MemberReturnApi;
import com.yiling.user.member.dto.MemberReturnDTO;
import com.yiling.user.member.dto.request.QueryMemberReturnPageRequest;
import com.yiling.user.member.dto.request.UpdateMemberAuthReturnRequest;
import com.yiling.user.member.dto.request.UpdateMemberReturnRequest;
import com.yiling.user.member.dto.request.UpdateMemberReturnStatusRequest;
import com.yiling.user.member.service.EnterpriseMemberService;
import com.yiling.user.member.service.MemberBuyRecordService;
import com.yiling.user.member.service.MemberReturnService;
import com.yiling.user.member.service.MemberService;

import lombok.extern.slf4j.Slf4j;

/**
 * 会员退款 API 实现
 *
 * @author: lun.yu
 * @date: 2022-04-18
 */
@Slf4j
@DubboService
public class MemberReturnApiImpl implements MemberReturnApi {

    @Autowired
    MemberService memberService;
    @Autowired
    EnterpriseMemberService enterpriseMemberService;
    @Autowired
    MemberBuyRecordService memberBuyRecordService;
    @Autowired
    MemberReturnService memberReturnService;

    @Override
    public boolean submitReturn(UpdateMemberReturnRequest request) {
        return memberBuyRecordService.submitReturn(request);
    }

    @Override
    public Page<MemberReturnDTO> queryMemberReturnPage(QueryMemberReturnPageRequest request) {
        return memberReturnService.queryMemberReturnPage(request);
    }

    @Override
    public boolean updateAuthStatus(UpdateMemberAuthReturnRequest request) {
        return memberReturnService.updateAuthStatus(request);
    }

    @Override
    public boolean updateReturnStatus(UpdateMemberReturnStatusRequest request) {
        return memberReturnService.updateReturnStatus(request);
    }
}
