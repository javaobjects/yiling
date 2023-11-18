package com.yiling.mall.order.service;

import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.request.B2BOrderReturnApplyRequest;
import com.yiling.order.order.dto.request.OrderReturnApplyRequest;
import com.yiling.order.order.dto.request.OrderReturnVerifyRequest;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author tingwei.chen
 * @date 2021-07-19
 */
public interface ReturnOrderService {

    /**
     * 录入破损单
     *
     * @param
     */
    Boolean damageOrderReturn(OrderReturnApplyRequest orderReturnApplyRequest, Integer fromWhere, OrderDTO orderDTO);

    /**
     * 采购商退货单申请
     *
     * @param orderReturnApplyRequest
     * @return
     */
    Boolean purchaseApplyReturnOrder(OrderReturnApplyRequest orderReturnApplyRequest);

    /**
     * 供应商退货单申请
     *
     * @param orderReturnApplyRequest
     * @return
     */
    Boolean supplierApplyOrderReturn(OrderReturnApplyRequest orderReturnApplyRequest, OrderDTO orderDTO);

    /**
     * 销售订单退货单审核
     *
     * @param orderReturnApplyRequest
     */
    Boolean checkOrderReturn(OrderReturnApplyRequest orderReturnApplyRequest);

    /**
     * 申请退货-部分发货
     *
     * @param request 申请退货请求参数
     * @return 成功/失败
     */
    Boolean deliverOrderReturn(OrderReturnApplyRequest request);

    /**
     * 申请退货--采购商申请
     *
     * @param request 申请退货请求参数
     * @return 成功/失败
     */
    Boolean applyOrderReturn(OrderReturnApplyRequest request);

    /**
     * 退货单审核
     *
     * @param request
     * @return
     */
    Boolean verifyOrderReturn(OrderReturnVerifyRequest request);

    /**
     * 销售助手退货单申请
     *
     * @param request 请求参数
     * @return 成功/失败
     */
    Boolean applySaOrderReturn(B2BOrderReturnApplyRequest request);
}
