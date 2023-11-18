package com.yiling.marketing.strategy.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.marketing.strategy.entity.StrategyEnterpriseGoodsLimitDO;

/**
 * <p>
 * 策略满赠店铺SKU Dao 接口
 * </p>
 *
 * @author zhangy
 * @date 2022-08-22
 */
@Repository
public interface StrategyEnterpriseGoodsLimitMapper extends BaseMapper<StrategyEnterpriseGoodsLimitDO> {

    /**
     * 根据活动id和商品id查询满赠这个活动绑定的店铺sku
     *
     * @param strategyActivityId 活动id
     * @param goodsIdList 商品id集合
     * @return 商品id集合
     */
    List<Long> listGoodsIdByStrategyId(@Param("strategyActivityId") Long strategyActivityId, @Param("goodsIdList") List<Long> goodsIdList);
}
