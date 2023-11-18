package com.yiling.hmc.order.api;

import java.util.List;

import com.yiling.hmc.order.dto.OrderReturnDetailDTO;

/**
 * @author: yong.zhang
 * @date: 2022/3/25
 */
public interface OrderReturnDetailApi {

    /**
     * 根据退货单id查询退货单明细
     *
     * @param returnId 退货单id
     * @return 退货单明细
     */
    List<OrderReturnDetailDTO> listByReturnId(Long returnId);
}
