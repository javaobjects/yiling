package com.yiling.order.order.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.api.OrderTicketDiscountApi;
import com.yiling.order.order.dto.OrderTicketDiscountDTO;
import com.yiling.order.order.dto.request.SaveOrderTicketDiscountRequest;
import com.yiling.order.order.entity.OrderTicketDiscountDO;
import com.yiling.order.order.service.OrderTicketDiscountService;

/**
 * 订单票折记录api
 * @author:wei.wang
 * @date:2021/7/2
 */
@DubboService
public class OrderTicketDiscountApiImpl implements OrderTicketDiscountApi {

    @Autowired
    private OrderTicketDiscountService orderTicketDiscountService;
    /**
     * 根据票折单号获取票折信息
     *
     * @param ticketDiscountNo
     * @return
     */
    @Override
    public List<OrderTicketDiscountDTO> getOrderTicketDiscountByNo(String ticketDiscountNo) {

        return orderTicketDiscountService.getOrderTicketDiscountByNo(ticketDiscountNo);
    }

    /**
     * 根据批量票折单号获取票折信息
     *
     * @param ticketDiscountNos 票折单号集合
     * @return
     */
    @Override
    public List<OrderTicketDiscountDTO> getOrderTicketDiscountByListNos(List<String> ticketDiscountNos) {
        return orderTicketDiscountService.getOrderTicketDiscountByListNos(ticketDiscountNos);
    }


    /**
     * 保存数据
     *
     * @param list
     * @return
     */
    @Override
    public Boolean saveBatch(List<SaveOrderTicketDiscountRequest> list) {
        List<OrderTicketDiscountDO> result = PojoUtils.map(list, OrderTicketDiscountDO.class);
        return orderTicketDiscountService.saveBatch(result);
    }

    /**
     * 获取订单票折记录根据订单id
     *
     * @param orderId
     * @return
     */
    @Override
    public List<OrderTicketDiscountDTO> listByOrderId(Long orderId) {
        return PojoUtils.map(orderTicketDiscountService.listByOrderId(orderId),OrderTicketDiscountDTO.class);
    }

    /**
     * 根据applyId查询数据
     *
     * @param applyId
     * @return
     */
    @Override
    public List<OrderTicketDiscountDTO> listByApplyId(Long applyId) {
        QueryWrapper<OrderTicketDiscountDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderTicketDiscountDO :: getApplyId,applyId);
        List<OrderTicketDiscountDO> list = orderTicketDiscountService.list(wrapper);
        return PojoUtils.map(list,OrderTicketDiscountDTO.class);
    }
}
