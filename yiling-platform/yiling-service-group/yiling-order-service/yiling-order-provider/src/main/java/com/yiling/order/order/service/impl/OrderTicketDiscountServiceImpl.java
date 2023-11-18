package com.yiling.order.order.service.impl;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yiling.order.order.dto.request.RefundOrderTicketDiscountRequest;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.dao.OrderTicketDiscountMapper;
import com.yiling.order.order.dto.OrderTicketDiscountDTO;
import com.yiling.order.order.entity.OrderTicketDiscountDO;
import com.yiling.order.order.service.OrderTicketDiscountService;

/**
 * <p>
 * 订单票折记录 服务实现类
 * </p>
 *
 * @author wei.wang
 * @date 2021-07-02
 */
@Service
public class OrderTicketDiscountServiceImpl extends BaseServiceImpl<OrderTicketDiscountMapper, OrderTicketDiscountDO> implements OrderTicketDiscountService {

    /**
     * 根据票折单号获取票折信息
     *
     * @param ticketDiscountNo
     * @return
     */
    @Override
    public List<OrderTicketDiscountDTO> getOrderTicketDiscountByNo(String ticketDiscountNo) {
        QueryWrapper<OrderTicketDiscountDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderTicketDiscountDO :: getTicketDiscountNo,ticketDiscountNo);
        return PojoUtils.map(list(wrapper),OrderTicketDiscountDTO.class);
    }



    /**
     * 根据批量票折单号获取票折信息
     *
     * @param ticketDiscountNos 票折单号集合
     * @return
     */
    @Override
    public List<OrderTicketDiscountDTO> getOrderTicketDiscountByListNos(List<String> ticketDiscountNos) {
        QueryWrapper<OrderTicketDiscountDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(OrderTicketDiscountDO :: getTicketDiscountNo,ticketDiscountNos);
        return PojoUtils.map(list(wrapper),OrderTicketDiscountDTO.class);
    }

    /**
     * 获取订单票折记录根据订单id
     *
     * @param orderId 订单id
     * @return
     */
    @Override
    public List<OrderTicketDiscountDO> listByOrderId(Long orderId) {
        QueryWrapper<OrderTicketDiscountDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderTicketDiscountDO :: getOrderId,orderId);
        return list(wrapper);
    }

    @Override
    public boolean refundOrderTicketDiscount(RefundOrderTicketDiscountRequest request) {
        int count = this.getBaseMapper().refundOrderTicketDiscount(request.getId(), request.getRefundAmount());
        return count > 0;
    }

    /**
     * 获取订单票折记录根据申请id
     *
     * @param applyId
     * @return
     */
    @Override
    public List<OrderTicketDiscountDO> listByApplyId(Long applyId) {
        QueryWrapper<OrderTicketDiscountDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderTicketDiscountDO :: getApplyId,applyId);

        return list(wrapper);
    }
}
