package com.yiling.marketing.integral.dao;

import java.util.List;

import com.yiling.marketing.integral.entity.IntegralOrderEnterpriseGoodsDO;
import com.yiling.framework.common.base.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 订单送积分店铺SKU表 Dao 接口
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-29
 */
@Repository
public interface IntegralOrderEnterpriseGoodsMapper extends BaseMapper<IntegralOrderEnterpriseGoodsDO> {

    /**
     * 根据发放规则id和商品id，查询发放规则绑定的店铺sku
     *
     * @param giveRuleId 发放规则id
     * @param goodsIdList 商品id集合
     * @return 商品id集合
     */
    List<Long> listGoodsIdByRuleId(@Param("giveRuleId") Long giveRuleId, @Param("goodsIdList") List<Long> goodsIdList);

}
