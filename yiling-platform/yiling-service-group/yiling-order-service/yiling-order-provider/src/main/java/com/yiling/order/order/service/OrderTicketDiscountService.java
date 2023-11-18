package com.yiling.order.order.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.order.order.dto.OrderTicketDiscountDTO;
import com.yiling.order.order.dto.request.RefundOrderTicketDiscountRequest;
import com.yiling.order.order.entity.OrderTicketDiscountDO;

/**
 * <p>
 * 订单票折记录 服务类
 * </p>
 *
 * @author wei.wang
 * @date 2021-07-02
 */
public interface OrderTicketDiscountService extends BaseService<OrderTicketDiscountDO> {

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
     * 获取订单票折记录根据订单id
     * @param orderId
     * @return
     */
    List<OrderTicketDiscountDO> listByOrderId(Long orderId);

    /**
     * 退还订单票折记录
     * @param request
     * @return
     */
    boolean refundOrderTicketDiscount(RefundOrderTicketDiscountRequest request);

    /**
     * 获取订单票折记录根据申请id
     * @param applyId
     * @return
     */
    List<OrderTicketDiscountDO> listByApplyId(Long applyId);
}
