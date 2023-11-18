package com.yiling.user.integral.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.integral.dto.IntegralOrderGiveMemberDTO;
import com.yiling.user.integral.dto.request.AddIntegralGiveMemberRequest;
import com.yiling.user.integral.dto.request.DeleteIntegralGiveMemberRequest;
import com.yiling.user.integral.dto.request.QueryIntegralGiveMemberPageRequest;

/**
 * 订单送积分-指定方案会员 Api
 *
 * @author: lun.yu
 * @date: 2023-01-05
 */
public interface IntegralGiveMemberApi {

    /**
     * 订单送积分-添加会员方案
     *
     * @param request 添加会员方案
     * @return 成功/失败
     */
    boolean add(AddIntegralGiveMemberRequest request);

    /**
     * 订单送积分-删除会员方案
     *
     * @param request 删除会员方案
     * @return 成功/失败
     */
    boolean delete(DeleteIntegralGiveMemberRequest request);

    /**
     * 订单送积分-查询指定方案会员分页列表
     *
     * @param request 查询条件
     * @return 已添加会员方案列表
     */
    Page<IntegralOrderGiveMemberDTO> pageList(QueryIntegralGiveMemberPageRequest request);

    /**
     * 订单送积分-根据发放规则id查询指定方案会员数量
     *
     * @param giveRuleId 发放规则id
     * @return 指定方案会员数量
     */
    Integer countMemberByRuleId(Long giveRuleId);

}
