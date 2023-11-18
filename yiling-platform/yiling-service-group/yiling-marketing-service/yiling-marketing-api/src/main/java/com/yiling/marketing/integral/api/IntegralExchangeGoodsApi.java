package com.yiling.marketing.integral.api;

import com.yiling.marketing.integral.bo.ExchangeGoodsDetailBO;
import com.yiling.marketing.integral.bo.IntegralExchangeGoodsDetailBO;
import com.yiling.marketing.integral.dto.request.RecentExchangeGoodsRequest;

/**
 * 积分兑换商品 API
 *
 * @author: lun.yu
 * @date: 2023-01-09
 */
public interface IntegralExchangeGoodsApi {

    /**
     * 查看积分兑换商品详情
     *
     * @param id
     * @return
     */
    IntegralExchangeGoodsDetailBO getDetail(Long id);

    /**
     * 根据Id获取APP端积分兑换商品详情
     *
     * @param id
     * @return
     */
    ExchangeGoodsDetailBO getGoodsDetail(Long id);

    /**
     * 立即兑换
     *
     * @param request
     * @return
     */
    boolean exchange(RecentExchangeGoodsRequest request);
}
