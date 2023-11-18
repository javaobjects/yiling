package com.yiling.marketing.lotteryactivity.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.marketing.lotteryactivity.api.LotteryActivityGiveScopeApi;
import com.yiling.marketing.lotteryactivity.bo.EnterpriseSimpleBO;
import com.yiling.marketing.lotteryactivity.bo.MemberSimpleBO;
import com.yiling.marketing.lotteryactivity.dto.request.AddCustomerPageRequest;
import com.yiling.marketing.lotteryactivity.dto.request.AddOrDeleteMemberRequest;
import com.yiling.marketing.lotteryactivity.dto.request.DeleteCustomerPageRequest;
import com.yiling.marketing.lotteryactivity.dto.request.QueryCustomerPageRequest;
import com.yiling.marketing.lotteryactivity.dto.request.QueryMemberPageRequest;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityGiveEnterpriseService;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityGiveMemberService;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityGivePromoterService;

/**
 * <p>
 * 抽奖活动赠送范围 API实现
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-20
 */
@DubboService
public class LotteryActivityGiveScopeApiImpl implements LotteryActivityGiveScopeApi {

    @Autowired
    LotteryActivityGiveEnterpriseService lotteryActivityGiveEnterpriseService;
    @Autowired
    LotteryActivityGiveMemberService lotteryActivityGiveMemberService;
    @Autowired
    LotteryActivityGivePromoterService lotteryActivityGivePromoterService;

    @Override
    public Page<EnterpriseSimpleBO> queryHadAddCustomerPage(QueryCustomerPageRequest request) {
        return lotteryActivityGiveEnterpriseService.queryHadAddCustomerPage(request);
    }

    @Override
    public Page<MemberSimpleBO> queryHadAddMemberPage(QueryMemberPageRequest request) {
        return lotteryActivityGiveMemberService.queryHadAddMemberPage(request);
    }

    @Override
    public Page<EnterpriseSimpleBO> queryHadAddPromoterPage(QueryCustomerPageRequest request) {
        return lotteryActivityGivePromoterService.queryHadAddPromoterPage(request);
    }

    @Override
    public boolean addCustomer(AddCustomerPageRequest request) {
        return lotteryActivityGiveEnterpriseService.addCustomer(request);
    }

    @Override
    public boolean deleteCustomer(DeleteCustomerPageRequest request) {
        return lotteryActivityGiveEnterpriseService.deleteCustomer(request);
    }

    @Override
    public boolean addMember(AddOrDeleteMemberRequest request) {
        return lotteryActivityGiveMemberService.addMember(request);
    }

    @Override
    public boolean deleteMember(AddOrDeleteMemberRequest request) {
        return lotteryActivityGiveMemberService.deleteMember(request);
    }

    @Override
    public boolean addPromoter(AddCustomerPageRequest request) {
        return lotteryActivityGivePromoterService.addPromoter(request);
    }

    @Override
    public boolean deletePromoter(DeleteCustomerPageRequest request) {
        return lotteryActivityGivePromoterService.deletePromoter(request);
    }
}
