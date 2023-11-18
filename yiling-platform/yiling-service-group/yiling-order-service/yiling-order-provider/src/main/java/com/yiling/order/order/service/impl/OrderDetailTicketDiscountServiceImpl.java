package com.yiling.order.order.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.dao.OrderDetailTicketDiscountMapper;
import com.yiling.order.order.dto.OrderDetailTicketDiscountDTO;
import com.yiling.order.order.dto.request.RefundOrderDetailTicketDiscountRequest;
import com.yiling.order.order.entity.OrderDetailTicketDiscountDO;
import com.yiling.order.order.service.OrderDetailTicketDiscountService;

/**
 * <p>
 * 订单明细票折信息 服务实现类
 * </p>
 *
 * @author wei.wang
 * @date 2021-07-02
 */
@Service
public class OrderDetailTicketDiscountServiceImpl extends BaseServiceImpl<OrderDetailTicketDiscountMapper, OrderDetailTicketDiscountDO> implements OrderDetailTicketDiscountService {


    /**
     * 根据详情id
     *
     * @param detailIds
     * @return
     */
    @Override
    public List<OrderDetailTicketDiscountDTO> getListOrderDetailTicketDiscount(List<Long> detailIds) {
        QueryWrapper<OrderDetailTicketDiscountDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(OrderDetailTicketDiscountDO :: getDetailId,detailIds);
        return PojoUtils.map(list(wrapper),OrderDetailTicketDiscountDTO.class);
    }


    /**
     * @param orderId 订单id
     * @return
     */
    @Override
    public List<OrderDetailTicketDiscountDO> listByOrderId(Long orderId) {
        QueryWrapper<OrderDetailTicketDiscountDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderDetailTicketDiscountDO :: getOrderId,orderId);
        return list(wrapper);
    }

    /**
     * 根据订单id和订单明细id查询出所有的订单明细票折信息(一个订单明细可能使用了多张票折)
     *
     * @param orderId
     * @param detailId
     * @return
     */
    @Override
    public List<OrderDetailTicketDiscountDO> getByOrderIdAndDetailId(Long orderId, Long detailId) {
        QueryWrapper<OrderDetailTicketDiscountDO> orderDetailTicketDiscountDOWrapper = new QueryWrapper<>();
        orderDetailTicketDiscountDOWrapper.lambda().eq(OrderDetailTicketDiscountDO::getOrderId, orderId);
        orderDetailTicketDiscountDOWrapper.lambda().eq(OrderDetailTicketDiscountDO::getDetailId, detailId);
        return this.list(orderDetailTicketDiscountDOWrapper);
    }

    @Override
    public boolean refundOrderDetailTicketDiscount(RefundOrderDetailTicketDiscountRequest request) {
        int count = this.getBaseMapper().refundOrderDetailTicketDiscount(request.getId(), request.getRefundAmount());
        return count > 0;
    }

    /**
     * 根据申请id获取OrderDetailTicketDiscountDO
     *
     * @param applyId
     * @return
     */
    @Override
    public List<OrderDetailTicketDiscountDO> listByApplyId(Long applyId) {
        QueryWrapper<OrderDetailTicketDiscountDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderDetailTicketDiscountDO :: getApplyId,applyId);
        return list(wrapper);
    }

}
