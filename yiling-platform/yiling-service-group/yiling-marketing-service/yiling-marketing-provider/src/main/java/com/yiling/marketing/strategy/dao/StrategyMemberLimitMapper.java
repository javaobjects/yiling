package com.yiling.marketing.strategy.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.marketing.strategy.entity.StrategyMemberLimitDO;

/**
 * <p>
 * 策略满赠会员方案 Dao 接口
 * </p>
 *
 * @author zhangy
 * @date 2022-08-22
 */
@Repository
public interface StrategyMemberLimitMapper extends BaseMapper<StrategyMemberLimitDO> {

    /**
     * 根据活动id查询这个活动绑定的指定方案会员
     *
     * @param strategyActivityId 活动id
     * @return 会员id集合
     */
    List<Long> listMemberIdByStrategyId(@Param("strategyActivityId") Long strategyActivityId);
}
