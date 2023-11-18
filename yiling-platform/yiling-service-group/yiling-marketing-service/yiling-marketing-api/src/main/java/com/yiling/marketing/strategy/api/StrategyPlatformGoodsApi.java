package com.yiling.marketing.strategy.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.marketing.paypromotion.dto.PayPromotionPlatformGoodsLimitDTO;
import com.yiling.marketing.strategy.dto.StrategyPlatformGoodsLimitDTO;
import com.yiling.marketing.strategy.dto.request.AddStrategyPlatformGoodsLimitRequest;
import com.yiling.marketing.strategy.dto.request.DeleteStrategyPlatformGoodsLimitRequest;
import com.yiling.marketing.strategy.dto.request.QueryStrategyPlatformGoodsLimitPageRequest;

/**
 * 策略满赠-平台SKUApi
 *
 * @author: yong.zhang
 * @date: 2022/8/24
 */
public interface StrategyPlatformGoodsApi {

    /**
     * 根据活动id和规格id查询满赠这个活动绑定的平台sku
     *
     * @param strategyActivityId 活动id
     * @param sellSpecificationsIdList 规格id集合
     * @return 规格id集合
     */
    List<Long> listSellSpecificationsIdByStrategyId(Long strategyActivityId, List<Long> sellSpecificationsIdList);

    /**
     * 添加指定平台SKU
     *
     * @param request 添加指定平台SKU
     * @return 成功/失败
     */
    boolean add(AddStrategyPlatformGoodsLimitRequest request);

    /**
     * 添加指定平台SKU
     *
     * @param request 添加指定平台SKU
     * @return 成功/失败
     */
    boolean addForPayPromotion(AddStrategyPlatformGoodsLimitRequest request);

    /**
     * 删除指定平台SKU
     *
     * @param request 删除指定平台SKU
     * @return 成功/失败
     */
    boolean delete(DeleteStrategyPlatformGoodsLimitRequest request);

    /**
     * 删除指定平台SKU
     *
     * @param request 删除指定平台SKU
     * @return 成功/失败
     */
    boolean deleteForPayPromotionPlatformSku(DeleteStrategyPlatformGoodsLimitRequest request);

    /**
     * 策略满赠指定平台SKU-已添加指定平台SKU列表查询
     *
     * @param request 查询条件
     * @return 已添加指定平台SKU列表
     */
    Page<StrategyPlatformGoodsLimitDTO> pageList(QueryStrategyPlatformGoodsLimitPageRequest request);

    /**
     * 策略满赠指定平台SKU-已添加指定平台SKU列表查询
     *
     * @param request 查询条件
     * @return 已添加指定平台SKU列表
     */
    Page<PayPromotionPlatformGoodsLimitDTO> pageListFroPayPromotion(QueryStrategyPlatformGoodsLimitPageRequest request);

    /**
     * 根据活动id查询指定平台SKU数量
     *
     * @param strategyActivityId 活动id
     * @return 指定平台SKU数量
     */
    Integer countPlatformGoodsByActivityId(Long strategyActivityId);

    /**
     * 根据活动id查询指定平台SKU
     *
     * @param strategyActivityId 活动id
     * @return 指定平台SKU
     */
    List<StrategyPlatformGoodsLimitDTO> listPlatformGoodsByActivityId(Long strategyActivityId);

    /**
     * 根据活动id和商品标准库ID集合查询指定平台SKU
     *
     * @param strategyActivityId 活动id
     * @param sellSpecificationsIdList 规格ID集合
     * @return 指定平台SKU
     */
    List<StrategyPlatformGoodsLimitDTO> listByActivityIdAndSellSpecificationsIdList(Long strategyActivityId, List<Long> sellSpecificationsIdList);

    /**
     * 根据活动id和商品标准库ID集合查询指定平台SKU
     *
     * @param strategyActivityId 活动id
     * @param standardIdList 标准库ID集合
     * @return 指定平台SKU
     */
    List<StrategyPlatformGoodsLimitDTO> listByActivityIdAndStandardIdList(Long strategyActivityId, List<Long> standardIdList);


    /**
     * 根据活动id查询指定平台SKU数量
     *
     * @param id 活动id
     * @return 指定平台SKU数量
     */
    Integer countPlatformGoodsByActivityIdForPayPromotion(Long id);
}
