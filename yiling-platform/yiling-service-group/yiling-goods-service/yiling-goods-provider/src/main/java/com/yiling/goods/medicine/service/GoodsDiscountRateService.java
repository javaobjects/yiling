package com.yiling.goods.medicine.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.goods.medicine.entity.GoodsDiscountRateDO;

/**
 * <p>
 * 商品最低折扣比率 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2021-07-21
 */
public interface GoodsDiscountRateService extends BaseService<GoodsDiscountRateDO> {

    /**
     * 根据客户id和商品id集合查询商品id对应的最低折扣比率
     * @param customerEid 客户ID
     * @param goodsIdList 商品id集合
     * @return 商品最低折扣比率集合
     */
    List<GoodsDiscountRateDO> queryGoodsDiscountRateList(Long customerEid, List<Long> goodsIdList);
}
