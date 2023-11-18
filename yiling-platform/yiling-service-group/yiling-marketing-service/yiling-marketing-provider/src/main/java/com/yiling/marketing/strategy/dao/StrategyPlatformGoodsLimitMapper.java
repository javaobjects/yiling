package com.yiling.marketing.strategy.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.marketing.strategy.entity.StrategyPlatformGoodsLimitDO;

/**
 * <p>
 * 策略满赠平台SKU Dao 接口
 * </p>
 *
 * @author zhangy
 * @date 2022-08-22
 */
@Repository
public interface StrategyPlatformGoodsLimitMapper extends BaseMapper<StrategyPlatformGoodsLimitDO> {

    /**
     * 根据活动id和规格id查询满赠这个活动绑定的平台sku
     *
     * @param strategyActivityId 活动id
     * @param sellSpecificationsIdList 规格id集合
     * @return 规格id集合
     */
    List<Long> listSellSpecificationsIdByStrategyId(@Param("strategyActivityId") Long strategyActivityId, @Param("sellSpecificationsIdList") List<Long> sellSpecificationsIdList);
}
