package com.yiling.order.order.service;

import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderReturnDTO;
import com.yiling.order.order.dto.request.OrderReturnApplyRequest;
import com.yiling.order.order.dto.request.OrderReturnVerifyRequest;

/**
 * @author: yong.zhang
 * @date: 2022/3/10
 */
public interface ReturnService {

    /**
     * 供应商退货申请-发货时的退货单
     *
     * @param orderReturnApplyRequest 申请参数
     * @param orderDTO 订单信息
     * @return 退货单信息
     */
    OrderReturnDTO supplierApplyOrderReturn(OrderReturnApplyRequest orderReturnApplyRequest, OrderDTO orderDTO);

    /**
     * 破损退货单创建前置操作--主要是为了保持收货数量的一致性(发货表和订单明细修改表)
     *
     * @param orderReturnApplyRequest 退货单申请的请求参数
     * @return 成功/失败
     */
    Boolean beforeDamageOrderReturn(OrderReturnApplyRequest orderReturnApplyRequest);

    /**
     * 破损退货申请-用户收货时的退货申请
     *
     * @param orderReturnApplyRequest 申请参数
     * @param orderDTO 订单信息
     * @return 成功/失败
     */
    Boolean damageOrderReturn(OrderReturnApplyRequest orderReturnApplyRequest, OrderDTO orderDTO);

    /**
     * 采购退货申请-用户收货时的退货申请
     *
     * @param orderReturnApplyRequest 申请参数
     * @param orderDTO 订单信息
     * @return 成功/失败
     */
    Boolean purchaseApplyReturnOrder(OrderReturnApplyRequest orderReturnApplyRequest, OrderDTO orderDTO);

    /**
     * 退货单审核
     *
     * @param request 审核时的请求数据
     * @return 退货单信息
     */
    OrderReturnDTO verifyOrderReturn(OrderReturnVerifyRequest request);
}
