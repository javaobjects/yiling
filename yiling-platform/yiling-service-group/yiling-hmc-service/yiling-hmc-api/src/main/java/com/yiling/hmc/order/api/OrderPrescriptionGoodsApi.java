package com.yiling.hmc.order.api;

import com.yiling.hmc.order.dto.OrderPrescriptionGoodsDTO;
import com.yiling.hmc.wechat.dto.request.OrderPrescriptionGoodsRequest;

import java.util.List;

/**
 * 订单处方商品API
 *
 * @author: fan.shen
 * @date: 2022/4/11
 */
public interface OrderPrescriptionGoodsApi {

    /**
     * 根据订单id获取处方商品
     * @param id
     * @return
     */
    List<OrderPrescriptionGoodsDTO> getByOrderId(Long id);
}
