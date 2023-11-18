package com.yiling.marketing.strategy.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.marketing.paypromotion.dto.PayPromotionEnterpriseGoodsLimitDTO;
import com.yiling.marketing.strategy.dto.StrategyEnterpriseGoodsLimitDTO;
import com.yiling.marketing.strategy.dto.request.AddStrategyEnterpriseGoodsLimitRequest;
import com.yiling.marketing.strategy.dto.request.DeleteStrategyEnterpriseGoodsLimitRequest;
import com.yiling.marketing.strategy.dto.request.QueryStrategyEnterpriseGoodsLimitPageRequest;

/**
 * 策略满赠-店铺SKUApi
 *
 * @author: yong.zhang
 * @date: 2022/8/24
 */
public interface StrategyEnterpriseGoodsApi {

    /**
     * 根据活动id和商品id查询满赠这个活动绑定的店铺sku
     *frpageListForPayPromotion
     * @param strategyActivityId 活动id
     * @param goodsIdList 商品id集合
     * @return 商品id集合
     */
    List<Long> listGoodsIdByStrategyId(Long strategyActivityId, List<Long> goodsIdList);

    /**
     * 添加指定店铺SKU
     *
     * @param request 添加指定店铺SKU
     * @return 成功/失败
     */
    boolean add(AddStrategyEnterpriseGoodsLimitRequest request);

    /**
     * 添加指定店铺SKU
     *
     * @param request 添加指定店铺SKU
     * @return 成功/失败
     */
    boolean addForPayPromotion(AddStrategyEnterpriseGoodsLimitRequest request);
    /**
     * 删除指定店铺SKU
     *
     * @param request 删除指定店铺SKU
     * @return 成功/失败
     */
    boolean delete(DeleteStrategyEnterpriseGoodsLimitRequest request);

    /**
     * 删除指定店铺SKU
     *
     * @param request 删除指定店铺SKU
     * @return 成功/失败
     */
    boolean deleteForPromotion(DeleteStrategyEnterpriseGoodsLimitRequest request);

    /**
     * 策略满赠指定店铺SKU-已添加指定店铺SKU列表查询
     *
     * @param request 查询条件
     * @return 已添加指定店铺SKU列表
     */
    Page<PayPromotionEnterpriseGoodsLimitDTO> pageListForPayPromotion(QueryStrategyEnterpriseGoodsLimitPageRequest request);

    /**
     * 策略满赠指定店铺SKU-已添加指定店铺SKU列表查询
     *
     * @param request 查询条件
     * @return 已添加指定店铺SKU列表
     */
    Page<StrategyEnterpriseGoodsLimitDTO> pageList(QueryStrategyEnterpriseGoodsLimitPageRequest request);

    /**
     * 根据活动id查询指定店铺SKU数量
     *
     * @param strategyActivityId 活动id
     * @return 指定店铺SKU数量
     */
    Integer countEnterpriseGoodsByActivityId(Long strategyActivityId);

    /**
     * 根据活动id查询指定店铺SKU
     *
     * @param strategyActivityId 活动id
     * @return 指定店铺SKU
     */
    List<StrategyEnterpriseGoodsLimitDTO> listEnterpriseGoodsByActivityId(Long strategyActivityId);

    /**
     * 根据活动id和商品id集合查询指定店铺SKU
     *
     * @param strategyActivityId 活动id
     * @param goodsIdList 商品id集合
     * @return 指定店铺SKU
     */
    List<StrategyEnterpriseGoodsLimitDTO> listByActivityIdAndGoodsIdList(Long strategyActivityId, List<Long> goodsIdList);

    /**
     * 根据活动id查询指定店铺SKU数量
     *
     * @param id 活动id
     * @return 指定店铺SKU数量
     */
    Integer countEnterpriseGoodsByActivityIdForPayPromotion(Long id);
}
