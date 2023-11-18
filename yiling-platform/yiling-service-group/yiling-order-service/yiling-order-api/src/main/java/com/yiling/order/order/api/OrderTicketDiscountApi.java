package com.yiling.order.order.api;

import java.util.List;

import com.yiling.order.order.dto.OrderTicketDiscountDTO;
import com.yiling.order.order.dto.request.SaveOrderTicketDiscountRequest;

/**
 * 订单票折记录api
 * @author:wei.wang
 * @date:2021/7/2
 */
public interface OrderTicketDiscountApi {
    /**
     * 根据票折单号获取票折信息
     * @param ticketDiscountNo
     * @return
     */
    List<OrderTicketDiscountDTO> getOrderTicketDiscountByNo(String ticketDiscountNo);

    /**
     * 根据批量票折单号获取票折信息
     * @param ticketDiscountNos 票折单号集合
     * @return
     */
    List<OrderTicketDiscountDTO> getOrderTicketDiscountByListNos(List<String> ticketDiscountNos);

  

    /**
     * 保存数据
     * @param list
     * @return
     */
    Boolean saveBatch(List<SaveOrderTicketDiscountRequest> list);

    /**
     * 获取订单票折记录根据订单id
     * @param orderId
     * @return
     */
    List<OrderTicketDiscountDTO> listByOrderId(Long orderId);

    /**
     * 根据applyId查询数据
     * @param applyId
     * @return
     */
    List<OrderTicketDiscountDTO> listByApplyId(Long applyId);

}
