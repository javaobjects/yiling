package com.yiling.hmc.order.api;

import java.util.List;

import com.yiling.hmc.order.dto.MarketOrderDetailDTO;

/**
 * @author: hongyang.zhang
 * @data: 2023/02/20
 */
public interface MarketOrderDetailApi {
    /**
     * 根据订单id集合查询订单明细
     *
     * @param orderIds
     * @return
     */
    List<MarketOrderDetailDTO> queryByOrderIdList(List<Long> orderIds);

    /**
     * 根据商品名称模糊查询订单明细
     *
     * @param goodsName
     * @return
     */
    List<MarketOrderDetailDTO> queryByGoodsNameList(String goodsName);
}
