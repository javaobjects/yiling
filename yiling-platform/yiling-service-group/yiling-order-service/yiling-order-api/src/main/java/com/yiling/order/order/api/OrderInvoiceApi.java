package com.yiling.order.order.api;

import java.util.List;

import com.yiling.order.order.dto.OrderInvoiceDTO;

/**
 * 订单发票号信息Api
 * @author:wei.wang
 * @date:2021/7/2
 */
public interface OrderInvoiceApi {

    /**
     * 根据发票id获取发票明细
     * @param applyId
     * @return
     */
    List<OrderInvoiceDTO> getOrderInvoiceByApplyId(Long applyId);

    /**
     * 根据发票ids获取发票明细
     * @param applyIds
     * @return
     */
    List<OrderInvoiceDTO> getOrderInvoiceByApplyIdList(List<Long> applyIds);


}
