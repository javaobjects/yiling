package com.yiling.user.integral.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.integral.api.IntegralGiveEnterpriseApi;
import com.yiling.user.integral.api.IntegralGiveMemberApi;
import com.yiling.user.integral.dto.IntegralOrderGiveEnterpriseDTO;
import com.yiling.user.integral.dto.IntegralOrderGiveMemberDTO;
import com.yiling.user.integral.dto.request.AddIntegralGiveEnterpriseRequest;
import com.yiling.user.integral.dto.request.AddIntegralGiveMemberRequest;
import com.yiling.user.integral.dto.request.DeleteIntegralGiveEnterpriseRequest;
import com.yiling.user.integral.dto.request.DeleteIntegralGiveMemberRequest;
import com.yiling.user.integral.dto.request.QueryIntegralGiveEnterprisePageRequest;
import com.yiling.user.integral.dto.request.QueryIntegralGiveMemberPageRequest;
import com.yiling.user.integral.entity.IntegralOrderGiveEnterpriseDO;
import com.yiling.user.integral.service.IntegralOrderGiveEnterpriseService;
import com.yiling.user.integral.service.IntegralOrderGiveMemberService;

/**
 * 订单送积分-指定客户 Api实现
 *
 * @author: lun.yu
 * @date: 2023-01-04
 */
@DubboService
public class IntegralGiveMemberApiImpl implements IntegralGiveMemberApi {

    @Autowired
    IntegralOrderGiveMemberService orderGiveMemberService;

    @Override
    public boolean add(AddIntegralGiveMemberRequest request) {
        return orderGiveMemberService.add(request);
    }

    @Override
    public boolean delete(DeleteIntegralGiveMemberRequest request) {
        return orderGiveMemberService.delete(request);
    }

    @Override
    public Page<IntegralOrderGiveMemberDTO> pageList(QueryIntegralGiveMemberPageRequest request) {
        return PojoUtils.map(orderGiveMemberService.pageList(request), IntegralOrderGiveMemberDTO.class);
    }

    @Override
    public Integer countMemberByRuleId(Long giveRuleId) {
        return orderGiveMemberService.countMemberByRuleId(giveRuleId);
    }
}
