package com.yiling.order.order.api;

import java.util.List;

import com.yiling.order.order.dto.OrderReturnDetailDTO;

/**
 * 退货单明细 API
 *
 * @author: yong.zhang
 * @date: 2021/8/5
 */
public interface OrderReturnDetailApi {
    /**
     * 根据退货单id批量获取明细
     *
     * @param returnIds
     * @return
     */
    List<OrderReturnDetailDTO> getOrderReturnDetailByReturnIds(List<Long> returnIds);

    /**
     * 根据退货单编号和订单明细id查询退货单明细信息
     *
     * @param returnId
     * @param detailId
     * @return
     */
    OrderReturnDetailDTO queryByReturnIdAndDetailId(Long returnId, Long detailId);

    /**
     * 根据退货单id获取退货单明细
     *
     * @param returnId  退货单id
     * @return  退货单明细
     */
    List<OrderReturnDetailDTO> getOrderReturnDetailByReturnId(Long returnId);

}
