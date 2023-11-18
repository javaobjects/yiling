package com.yiling.user.integral.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.integral.dto.IntegralOrderSellerDTO;
import com.yiling.user.integral.dto.request.AddIntegralOrderSellerRequest;
import com.yiling.user.integral.dto.request.DeleteIntegralGiveSellerRequest;
import com.yiling.user.integral.dto.request.QueryGiveIntegralSellerPageRequest;

/**
 * 订单送积分-商家范围 Api
 *
 * @author: lun.yu
 * @date: 2023-01-03
 */
public interface IntegralGiveSellerApi {

    /**
     * 添加商家
     *
     * @param request 添加商家内容
     * @return 成功/失败
     */
    boolean add(AddIntegralOrderSellerRequest request);

    /**
     * 删除商家
     *
     * @param request 删除商家内容
     * @return 成功/失败
     */
    boolean delete(DeleteIntegralGiveSellerRequest request);

    /**
     * 订单送积分-已添加商家列表查询
     *
     * @param request 查询条件
     * @return 已添加商家列表
     */
    Page<IntegralOrderSellerDTO> pageList(QueryGiveIntegralSellerPageRequest request);

    /**
     * 根据发放规则id查询商家范围数量
     *
     * @param giveRuleId 发放规则id
     * @return 商家范围数量
     */
    Integer countSellerByGiveRuleId(Long giveRuleId);

    /**
     * 根据发放规则id查询商家范围
     *
     * @param giveRuleId 发放规则id
     * @return 商家范围
     */
    List<IntegralOrderSellerDTO> listSellerByGiveRuleId(Long giveRuleId);

    /**
     * 根据企业ID查询订单送积分指定商家
     *
     * @param giveRuleId 发放规则ID
     * @param eidList 企业ID
     * @return 商家信息
     */
    List<IntegralOrderSellerDTO> listByRuleIdAndEidList(Long giveRuleId, List<Long> eidList);
}
