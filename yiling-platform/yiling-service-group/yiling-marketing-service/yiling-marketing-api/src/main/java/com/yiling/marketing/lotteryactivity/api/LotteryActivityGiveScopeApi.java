package com.yiling.marketing.lotteryactivity.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.marketing.lotteryactivity.bo.EnterpriseSimpleBO;
import com.yiling.marketing.lotteryactivity.bo.MemberSimpleBO;
import com.yiling.marketing.lotteryactivity.dto.request.AddCustomerPageRequest;
import com.yiling.marketing.lotteryactivity.dto.request.AddOrDeleteMemberRequest;
import com.yiling.marketing.lotteryactivity.dto.request.DeleteCustomerPageRequest;
import com.yiling.marketing.lotteryactivity.dto.request.QueryCustomerPageRequest;
import com.yiling.marketing.lotteryactivity.dto.request.QueryMemberPageRequest;

/**
 * <p>
 * 抽奖活动赠送范围 API
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-20
 */
public interface LotteryActivityGiveScopeApi {

    /**
     * 查询赠送企业分页列表
     *
     * @param request
     * @return
     */
    Page<EnterpriseSimpleBO> queryHadAddCustomerPage(QueryCustomerPageRequest request);

    /**
     * 查询赠送会员分页列表
     *
     * @param request
     * @return
     */
    Page<MemberSimpleBO> queryHadAddMemberPage(QueryMemberPageRequest request);

    /**
     * 查询赠送推广方分页列表
     *
     * @param request
     * @return
     */
    Page<EnterpriseSimpleBO> queryHadAddPromoterPage(QueryCustomerPageRequest request);

    /**
     * 批量添加指定客户
     *
     * @param request
     * @return
     */
    boolean addCustomer(AddCustomerPageRequest request);

    /**
     * 批量删除指定客户
     *
     * @param request
     * @return
     */
    boolean deleteCustomer(DeleteCustomerPageRequest request);

    /**
     * 添加会员
     *
     * @param request
     * @return
     */
    boolean addMember(AddOrDeleteMemberRequest request);

    /**
     * 删除会员
     *
     * @param request
     * @return
     */
    boolean deleteMember(AddOrDeleteMemberRequest request);

    /**
     * 添加推广方
     *
     * @param request
     * @return
     */
    boolean addPromoter(AddCustomerPageRequest request);

    /**
     * 删除推广方
     *
     * @param request
     * @return
     */
    boolean deletePromoter(DeleteCustomerPageRequest request);
}
