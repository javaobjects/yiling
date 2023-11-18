package com.yiling.goods.medicine.api;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 商品最低折扣比率api
 * @author: lun.yu
 * @date: 2021/7/21
 */
public interface GoodsDiscountRateApi {

    /**
     * 根据客户id和商品id集合查询商品id对应的最低折扣比率
     * @param customerEid      	客户ID
     * @param goodsIdList 商品id集合
     * @return 商品id对应的最低折扣比率
     */
    Map<Long,BigDecimal> queryGoodsDiscountRateMap(Long customerEid, List<Long> goodsIdList);

}
