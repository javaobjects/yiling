package com.yiling.marketing.strategy.service;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.paypromotion.dto.PayPromotionEnterpriseGoodsLimitDTO;
import com.yiling.marketing.strategy.dto.request.AddStrategyEnterpriseGoodsLimitRequest;
import com.yiling.marketing.strategy.dto.request.DeleteStrategyEnterpriseGoodsLimitRequest;
import com.yiling.marketing.strategy.dto.request.QueryStrategyEnterpriseGoodsLimitPageRequest;
import com.yiling.marketing.strategy.entity.StrategyActivityDO;
import com.yiling.marketing.strategy.entity.StrategyEnterpriseGoodsLimitDO;

/**
 * <p>
 * 策略满赠店铺SKU 服务类
 * </p>
 *
 * @author zhangy
 * @date 2022-08-22
 */
public interface StrategyEnterpriseGoodsLimitService extends BaseService<StrategyEnterpriseGoodsLimitDO> {

    /**
     * 根据活动id和商品id查询满赠这个活动绑定的店铺sku
     *
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
     * 删除指定店铺SKU
     *
     * @param request 删除指定店铺SKU
     * @return 成功/失败
     */
    boolean delete(DeleteStrategyEnterpriseGoodsLimitRequest request);

    /**
     * 复制指定店铺SKU
     *
     * @param strategyActivityDO 营销活动主信息
     * @param oldId 被拷贝的营销活动id
     * @param opUserId 操作人
     * @param opTime 操作时间
     */
    void copy(StrategyActivityDO strategyActivityDO, Long oldId, Long opUserId, Date opTime);

    /**
     * 策略满赠指定店铺SKU-已添加指定店铺SKU列表查询
     *
     * @param request 查询条件
     * @return 已添加指定店铺SKU列表
     */
    Page<StrategyEnterpriseGoodsLimitDO> pageList(QueryStrategyEnterpriseGoodsLimitPageRequest request);

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
    List<StrategyEnterpriseGoodsLimitDO> listEnterpriseGoodsByActivityId(Long strategyActivityId);

    /**
     * 根据活动id和商品id查询指定店铺SKU
     *
     * @param strategyActivityId 活动id
     * @param goodsId 商品id
     * @return 指定店铺SKU
     */
    StrategyEnterpriseGoodsLimitDO queryByActivityIdAndGoodsId(Long strategyActivityId, Long goodsId);

    /**
     * 根据活动id和商品id集合查询指定店铺SKU
     *
     * @param strategyActivityId 活动id
     * @param goodsIdList 商品id集合
     * @return 指定店铺SKU
     */
    List<StrategyEnterpriseGoodsLimitDO> listByActivityIdAndGoodsIdList(Long strategyActivityId, List<Long> goodsIdList);

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
    boolean deleteForPromotion(DeleteStrategyEnterpriseGoodsLimitRequest request);

    /**
     * 策略满赠指定店铺SKU-已添加指定店铺SKU列表查询
     *
     * @param request 查询条件
     * @return 已添加指定店铺SKU列表
     */
    Page<PayPromotionEnterpriseGoodsLimitDTO> pageListForPayPromotion(QueryStrategyEnterpriseGoodsLimitPageRequest request);

    /**
     * 根据活动id查询指定店铺SKU数量
     *
     * @param strategyActivityId 活动id
     * @return 指定店铺SKU数量
     */
    Integer countEnterpriseGoodsByActivityIdForPayPromotion(Long strategyActivityId);

}
