package com.yiling.hmc.order.api;

import java.util.List;

import com.yiling.hmc.order.dto.OrderOperateDTO;

/**
 * @author: yong.zhang
 * @date: 2022/4/21
 */
public interface OrderOperateApi {

    /**
     * 获取某订单的最新操作
     *
     * @param orderId 订单id
     * @return 操作记录
     */
    OrderOperateDTO getLastOperate(Long orderId);

    /**
     * 根据订单id和类型查询订单操作信息
     *
     * @param orderId 订单id
     * @param operateTypeList 操作功能:1-自提/2-发货/3-退货/4-收货
     * @return 订单操作信息
     */
    List<OrderOperateDTO> listByOrderIdAndTypeList(Long orderId, List<Integer> operateTypeList);
}
