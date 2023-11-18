package com.yiling.marketing.integral.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.integral.dto.IntegralOrderPlatformGoodsDTO;
import com.yiling.user.integral.dto.request.AddIntegralGivePlatformGoodsRequest;
import com.yiling.user.integral.dto.request.DeleteIntegralGivePlatformGoodsRequest;
import com.yiling.user.integral.dto.request.QueryIntegralGivePlatformGoodsPageRequest;

/**
 * 订单送积分-平台SKU Api
 *
 * @author: lun.yu
 * @date: 2023-01-04
 */
public interface IntegralGivePlatformGoodsApi {

    /**
     * 添加指定平台SKU
     *
     * @param request 添加指定平台SKU
     * @return 成功/失败
     */
    boolean add(AddIntegralGivePlatformGoodsRequest request);

    /**
     * 删除指定平台SKU
     *
     * @param request 删除指定平台SKU
     * @return 成功/失败
     */
    boolean delete(DeleteIntegralGivePlatformGoodsRequest request);

    /**
     * 订单送积分-平台SKU-已添加指定平台SKU分页列表查询
     *
     * @param request 查询条件
     * @return 已添加指定平台SKU列表
     */
    Page<IntegralOrderPlatformGoodsDTO> pageList(QueryIntegralGivePlatformGoodsPageRequest request);

    /**
     * 根据发放规则ID查询指定平台SKU数量
     *
     * @param giveRuleId 发放规则ID
     * @return 指定平台SKU数量
     */
    Integer countPlatformGoodsByGiveRuleId(Long giveRuleId);

    /**
     * 根据发放规则ID和商品标准库ID集合查询指定平台SKU
     *
     * @param giveRuleId 发放规则ID
     * @param sellSpecificationsIdList 规格ID集合
     * @return 指定平台SKU
     */
    List<IntegralOrderPlatformGoodsDTO> listByRuleIdAndSellSpecificationsIdList(Long giveRuleId, List<Long> sellSpecificationsIdList);

}
