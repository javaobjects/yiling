package com.yiling.order.order.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.order.order.dto.OrderDetailTicketDiscountDTO;
import com.yiling.order.order.dto.request.RefundOrderDetailTicketDiscountRequest;
import com.yiling.order.order.entity.OrderDetailTicketDiscountDO;

/**
 * <p>
 * 订单明细票折信息 服务类
 * </p>
 *
 * @author wei.wang
 * @date 2021-07-02
 */
public interface OrderDetailTicketDiscountService extends BaseService<OrderDetailTicketDiscountDO> {


    /**
     * 根据详情id
     * @param detailIds
     * @return
     */
    List<OrderDetailTicketDiscountDTO> getListOrderDetailTicketDiscount(List<Long> detailIds);


    /**
     *
     * @param orderId 订单id
     * @return
     */
    List<OrderDetailTicketDiscountDO> listByOrderId(Long orderId);

    /**
     * 根据订单id和订单明细id查询出所有的订单明细票折信息
     *
     * @param orderId
     * @param detailId
     * @return
     */
    List<OrderDetailTicketDiscountDO> getByOrderIdAndDetailId(Long orderId, Long detailId);

    /**
     * 退还订单明细票折信息
     *
     * @param request   退还票折请求数据
     * @return  成功/失败
     */
    boolean refundOrderDetailTicketDiscount(RefundOrderDetailTicketDiscountRequest request);

    /**
     * 根据申请id获取OrderDetailTicketDiscountDO
     * @param applyId
     * @return
     */
    List<OrderDetailTicketDiscountDO> listByApplyId(Long applyId);
}
