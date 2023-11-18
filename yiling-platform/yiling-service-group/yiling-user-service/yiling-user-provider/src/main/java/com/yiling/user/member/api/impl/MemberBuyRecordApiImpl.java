package com.yiling.user.member.api.impl;

import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.member.api.MemberBuyRecordApi;
import com.yiling.user.member.bo.MemberBuyRecordBO;
import com.yiling.user.member.dto.MemberBuyRecordDTO;
import com.yiling.user.member.dto.request.CancelBuyRecordRequest;
import com.yiling.user.member.dto.request.QueryBuyRecordRequest;
import com.yiling.user.member.dto.request.QueryMemberBuyRecordRequest;
import com.yiling.user.member.dto.request.QueryMemberListRecordRequest;
import com.yiling.user.member.service.MemberBuyRecordService;

import lombok.extern.slf4j.Slf4j;

/**
 * 会员购买记录 API 实现
 *
 * @author: lun.yu
 * @date: 2022-10-09
 */
@Slf4j
@DubboService
public class MemberBuyRecordApiImpl implements MemberBuyRecordApi {

    @Autowired
    MemberBuyRecordService memberBuyRecordService;

    @Override
    public Page<MemberBuyRecordDTO> queryBuyRecordListPage(QueryBuyRecordRequest request) {
        return memberBuyRecordService.queryBuyRecordListPage(request);
    }

    @Override
    public MemberBuyRecordDTO getBuyRecodeDetail(Long id) {
        return memberBuyRecordService.getBuyRecordDetail(id);
    }

    @Override
    public List<MemberBuyRecordDTO> getCurrentValidMemberRecord(Long eid) {
        return memberBuyRecordService.getCurrentValidMemberRecord(eid);
    }

    @Override
    public MemberBuyRecordDTO getBuyRecodeByOrderNo(String orderNo) {
        return memberBuyRecordService.getBuyRecordByOrderNo(orderNo);
    }

    @Override
    public List<MemberBuyRecordBO> getMemberBuyRecordByDate(QueryMemberBuyRecordRequest request) {
        return memberBuyRecordService.getMemberBuyRecordByDate(request);
    }

    @Override
    public Map<Long, Long> getPromoterByEid(Long eid) {
        return memberBuyRecordService.getPromoterByEid(eid);
    }

    @Override
    public List<MemberBuyRecordDTO> getMemberRecordListByEid(Long eid, Long memberId) {
        return memberBuyRecordService.getMemberRecordListByEid(eid, memberId);
    }

    @Override
    public boolean cancelBuyRecord(CancelBuyRecordRequest request) {
        return memberBuyRecordService.cancelBuyRecord(request);
    }

    @Override
    public List<MemberBuyRecordDTO> getBuyRecordListByCond(QueryMemberListRecordRequest request) {
        return memberBuyRecordService.getBuyRecordListByCond(request);
    }

}
