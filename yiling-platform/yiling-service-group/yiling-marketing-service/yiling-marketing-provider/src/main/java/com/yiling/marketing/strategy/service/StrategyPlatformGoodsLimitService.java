package com.yiling.marketing.strategy.service;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.payPromotion.entity.MarketingPayPlatformGoodsLimitDO;
import com.yiling.marketing.strategy.dto.request.AddStrategyPlatformGoodsLimitRequest;
import com.yiling.marketing.strategy.dto.request.DeleteStrategyPlatformGoodsLimitRequest;
import com.yiling.marketing.strategy.dto.request.QueryStrategyPlatformGoodsLimitPageRequest;
import com.yiling.marketing.strategy.entity.StrategyActivityDO;
import com.yiling.marketing.strategy.entity.StrategyPlatformGoodsLimitDO;

/**
 * <p>
 * 策略满赠平台SKU 服务类
 * </p>
 *
 * @author zhangy
 * @date 2022-08-22
 */
public interface StrategyPlatformGoodsLimitService extends BaseService<StrategyPlatformGoodsLimitDO> {

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
     * 复制指定平台SKU
     *
     * @param strategyActivityDO 营销活动主信息
     * @param oldId 被拷贝的营销活动id
     * @param opUserId 操作人
     * @param opTime 操作时间
     */
    void copy(StrategyActivityDO strategyActivityDO, Long oldId, Long opUserId, Date opTime);

    /**
     * 策略满赠指定平台SKU-已添加指定平台SKU列表查询
     *
     * @param request 查询条件
     * @return 已添加指定平台SKU列表
     */
    Page<StrategyPlatformGoodsLimitDO> pageList(QueryStrategyPlatformGoodsLimitPageRequest request);

    /**
     * 策略满赠指定平台SKU-已添加指定平台SKU列表查询
     *
     * @param request 查询条件
     * @return 已添加指定平台SKU列表
     */
    Page<MarketingPayPlatformGoodsLimitDO> pageListFroPayPromotion(QueryStrategyPlatformGoodsLimitPageRequest request);

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
    List<StrategyPlatformGoodsLimitDO> listPlatformGoodsByActivityId(Long strategyActivityId);

    /**
     * 根据活动id和商品规格id查询指定平台SKU
     *
     * @param strategyActivityId 活动id
     * @param sellSpecificationsId 规格id
     * @return 指定平台SKU
     */
    StrategyPlatformGoodsLimitDO queryByActivityIdAndSellSpecificationsId(Long strategyActivityId, Long sellSpecificationsId);

    /**
     * 根据活动id和商品规格id集合查询指定平台SKU
     *
     * @param strategyActivityId 活动id
     * @param sellSpecificationsIdList 规格id集合
     * @return 指定平台SKU
     */
    List<StrategyPlatformGoodsLimitDO> listByActivityIdAndSellSpecificationsIdList(Long strategyActivityId, List<Long> sellSpecificationsIdList);

    /**
     * 根据活动id和商品标准库ID集合查询指定平台SKU
     *
     * @param strategyActivityId 活动id
     * @param standardIdList 标准库ID集合
     * @return 指定平台SKU
     */
    List<StrategyPlatformGoodsLimitDO> listByActivityIdAndStandardIdList(Long strategyActivityId, List<Long> standardIdList);

    /**
     * 删除指定平台SKU
     *
     * @param request 删除指定平台SKU
     * @return 成功/失败
     */
    boolean deleteForPayPromotionPlatformSku(DeleteStrategyPlatformGoodsLimitRequest request);

    /**
     * 根据活动id查询指定平台SKU数量
     *
     * @param strategyActivityId 活动id
     * @return 指定平台SKU数量
     */
    Integer countPlatformGoodsByActivityIdForPayPromotion(Long strategyActivityId);

}
