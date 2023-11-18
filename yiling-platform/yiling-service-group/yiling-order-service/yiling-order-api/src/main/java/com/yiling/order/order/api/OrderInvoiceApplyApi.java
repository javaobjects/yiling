package com.yiling.order.order.api;

import java.util.List;

import com.yiling.order.order.dto.OrderInvoiceApplyDTO;
import com.yiling.order.order.dto.request.UpdateOrderInvoiceApplyRequest;

/**
 * 订单发票申请Api
 * @author:wei.wang
 * @date:2021/7/2
 */
public interface OrderInvoiceApplyApi {
    /**
     * 根据orderIds获取订单开票信息
     * @param orderIds
     * @return
     */
    List<OrderInvoiceApplyDTO> getOrderInvoiceApplyByList(List<Long> orderIds);



    /**
     * 根据orderId获取所有开票申请
     * @param orderId
     * @return
     */
   List<OrderInvoiceApplyDTO> listByOrderId(Long orderId);

    /**
     * 保存或新增发票申请
     * @param orderInvoiceApply
     * @return
     */
    OrderInvoiceApplyDTO saveOrUpdateById(UpdateOrderInvoiceApplyRequest orderInvoiceApply);

    /**
     * 获取申请信息
     * @param id
     * @return
     */
    OrderInvoiceApplyDTO getOneById(Long id);

}
