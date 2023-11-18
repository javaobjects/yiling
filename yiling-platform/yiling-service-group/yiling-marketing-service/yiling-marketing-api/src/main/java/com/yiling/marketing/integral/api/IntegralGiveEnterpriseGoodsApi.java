package com.yiling.marketing.integral.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.marketing.integral.dto.request.AddIntegralGiveEnterpriseGoodsRequest;
import com.yiling.marketing.integral.dto.request.DeleteIntegralGiveEnterpriseGoodsRequest;
import com.yiling.marketing.integral.dto.request.QueryIntegralEnterpriseGoodsPageRequest;
import com.yiling.user.integral.dto.IntegralOrderEnterpriseGoodsDTO;

/**
 * 订单送积分-店铺SKU Api
 *
 * @author: lun.yu
 * @date: 2023-01-04
 */
public interface IntegralGiveEnterpriseGoodsApi {

    /**
     * 根据发放规则id和商品id，查询发放规则绑定的店铺sku
     *
     * @param giveRuleId 发放规则id
     * @param goodsIdList 商品id集合
     * @return 商品id集合
     */
    List<Long> listGoodsIdByRuleId(Long giveRuleId, List<Long> goodsIdList);

    /**
     * 添加指定店铺SKU
     *
     * @param request 添加指定店铺SKU
     * @return 成功/失败
     */
    boolean add(AddIntegralGiveEnterpriseGoodsRequest request);

    /**
     * 删除指定店铺SKU
     *
     * @param request 删除指定店铺SKU
     * @return 成功/失败
     */
    boolean delete(DeleteIntegralGiveEnterpriseGoodsRequest request);

    /**
     * 订单送积分-指定店铺SKU-已添加指定店铺SKU分页列表查询
     *
     * @param request 查询条件
     * @return 已添加指定店铺SKU列表
     */
    Page<IntegralOrderEnterpriseGoodsDTO> pageList(QueryIntegralEnterpriseGoodsPageRequest request);

    /**
     * 根据发放规则id查询指定店铺SKU数量
     *
     * @param giveRuleId 发放规则id
     * @return 指定店铺SKU数量
     */
    Integer countEnterpriseGoodsByRuleId(Long giveRuleId);

    /**
     * 根据发放规则id查询指定店铺SKU
     *
     * @param giveRuleId 发放规则id
     * @return 指定店铺SKU
     */
    List<IntegralOrderEnterpriseGoodsDTO> listEnterpriseGoodsByRuleId(Long giveRuleId);

    /**
     * 根据发放规则id和商品id集合查询指定店铺SKU
     *
     * @param giveRuleId 发放规则id
     * @param goodsIdList 商品id集合
     * @return 指定店铺SKU
     */
    List<IntegralOrderEnterpriseGoodsDTO> listByRuleIdAndGoodsIdList(Long giveRuleId, List<Long> goodsIdList);
}
