package com.yiling.order.order.dto.request;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.order.order.dto.CashDiscountAgreementInfoDTO;

import lombok.Data;

/**
 * 订单明细现折信息 DTO
 *
 * @author: xuan.zhou
 * @date: 2021/7/13
 */
@Data
public class OrderDetailCashDiscountInfoDTO implements java.io.Serializable {

    /**
     * 订单明细ID
     */
    private Long orderDetailId;

    /**
     * 商品Id
     */
    private Long goodsId;

    /**
     * 商品小计
     */
    private BigDecimal goodsAmount;

    /**
     * 现折金额
     */
    private BigDecimal cashDiscountAmount;

    /**
     * 现折协议信息集合
     */
    private List<CashDiscountAgreementInfoDTO> cashDiscountAgreementInfoList;
}
