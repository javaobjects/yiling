package com.yiling.mall.member.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.mall.member.api.MemberApi;
import com.yiling.mall.member.service.MemberOrderService;
import com.yiling.mall.member.service.MemberService;
import com.yiling.user.member.dto.MemberOrderDTO;
import com.yiling.user.member.dto.request.MemberOrderRequest;

import lombok.extern.slf4j.Slf4j;

/**
 * 会员 API 实现
 *
 * @author: lun.yu
 * @date: 2021/12/13
 */
@Slf4j
@DubboService
public class MemberApiImpl implements MemberApi {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberOrderService memberOrderService;

    @Override
    public MemberOrderDTO createMemberOrder(MemberOrderRequest request) {
        return PojoUtils.map(memberOrderService.createMemberOrder(request), MemberOrderDTO.class);
    }

    @Override
    public Boolean memberExpirationReminderHandler() {
        return memberService.memberExpirationReminderHandler();
    }
}
