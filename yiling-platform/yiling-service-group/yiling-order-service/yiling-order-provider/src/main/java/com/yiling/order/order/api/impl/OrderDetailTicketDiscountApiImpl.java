package com.yiling.order.order.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.api.OrderDetailTicketDiscountApi;
import com.yiling.order.order.dto.OrderDetailTicketDiscountDTO;
import com.yiling.order.order.dto.request.SaveOrderDetailTicketDiscountRequest;
import com.yiling.order.order.entity.OrderDetailTicketDiscountDO;
import com.yiling.order.order.service.OrderDetailTicketDiscountService;

/**
 * 票折使用明细api
 * @author:wei.wang
 * @date:2021/7/5
 */
@DubboService
public class OrderDetailTicketDiscountApiImpl implements OrderDetailTicketDiscountApi {

    @Autowired
    private OrderDetailTicketDiscountService orderDetailTicketDiscountService;
    /**
     * 根据详情id
     *
     * @param detailIds
     * @return
     */
    @Override
    public List<OrderDetailTicketDiscountDTO> getListOrderDetailTicketDiscount(List<Long> detailIds) {
        return orderDetailTicketDiscountService.getListOrderDetailTicketDiscount(detailIds);
    }



    /**
     * 批量保存
     *
     * @param list
     * @return
     */
    @Override
    public Boolean saveBatch(List<SaveOrderDetailTicketDiscountRequest> list) {
        List<OrderDetailTicketDiscountDO> result = PojoUtils.map(list, OrderDetailTicketDiscountDO.class);
        return  orderDetailTicketDiscountService.saveBatch(result);
    }



}
