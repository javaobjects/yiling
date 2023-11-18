package com.yiling.user.member.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.member.api.MemberEquityApi;
import com.yiling.user.member.dto.MemberEquityDTO;
import com.yiling.user.member.dto.request.CreateMemberEquityRequest;
import com.yiling.user.member.dto.request.QueryMemberEquityRequest;
import com.yiling.user.member.dto.request.UpdateMemberEquityRequest;
import com.yiling.user.member.service.MemberEquityService;

import lombok.extern.slf4j.Slf4j;

/**
 * 权益 API 实现
 *
 * @author: lun.yu
 * @date: 2021/10/26
 */
@Slf4j
@DubboService
public class MemberEquityApiImpl implements MemberEquityApi {

    @Autowired
    MemberEquityService memberEquityService;

    @Override
    public Page<MemberEquityDTO> queryListPage(QueryMemberEquityRequest request) {

        return memberEquityService.queryListPage(request);
    }

    @Override
    public Boolean updateStatus(Long id, Long opUserId) {

        return memberEquityService.updateStatus(id,opUserId);
    }

    @Override
    public Boolean createEquity(CreateMemberEquityRequest request) {

        return memberEquityService.createEquity(request);
    }

    @Override
    public Boolean updateEquity(UpdateMemberEquityRequest request) {

        return memberEquityService.updateEquity(request);
    }

    @Override
    public Boolean deleteEquity(Long id, Long opUserId) {

        return memberEquityService.deleteEquity(id,opUserId);
    }

    @Override
    public List<MemberEquityDTO> queryList(QueryMemberEquityRequest request) {

        return memberEquityService.queryList(request);
    }

    @Override
    public MemberEquityDTO getEquity(Long id) {

        return memberEquityService.getEquity(id);
    }
}
