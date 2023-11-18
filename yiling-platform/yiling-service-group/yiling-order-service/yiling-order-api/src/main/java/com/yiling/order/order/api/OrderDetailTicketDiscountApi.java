package com.yiling.order.order.api;

import java.util.List;

import com.yiling.order.order.dto.OrderDetailTicketDiscountDTO;
import com.yiling.order.order.dto.request.SaveOrderDetailTicketDiscountRequest;

/**
 * 票折使用明细api
 * @author:wei.wang
 * @date:2021/7/5
 */
public interface OrderDetailTicketDiscountApi {
    /**
     * 根据详情id
     * @param detailIds
     * @return
     */
    List<OrderDetailTicketDiscountDTO> getListOrderDetailTicketDiscount(List<Long> detailIds);



    /**
     * 批量保存
     * @param list
     * @return
     */
    Boolean saveBatch(List<SaveOrderDetailTicketDiscountRequest> list);



}
