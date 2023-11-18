package com.yiling.order.order.dto.request;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 修改订单现折金额 Request
 *
 * @author: xuan.zhou
 * @date: 2021/7/12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateOrderCashDiscountAmountRequest extends BaseRequest {

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 现折金额
     */
    private BigDecimal cashDiscountAmount;

    /**
     * 订单明细现折信息列表
     */
    private List<OrderDetailCashDiscountInfoDTO> orderDetailCashDiscountInfoList;

}
