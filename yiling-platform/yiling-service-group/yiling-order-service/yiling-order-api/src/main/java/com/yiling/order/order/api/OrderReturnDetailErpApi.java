package com.yiling.order.order.api;

import java.util.List;

import com.yiling.order.order.dto.OrderReturnDetailErpDTO;

/**
 * @author: yong.zhang
 * @date: 2021/9/23
 */
public interface OrderReturnDetailErpApi {

    /**
     * @param returnIds
     * @return
     */
    List<OrderReturnDetailErpDTO> listByReturnIds(List<Long> returnIds);
}
