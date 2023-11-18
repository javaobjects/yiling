package com.yiling.order.order.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;

/**
 * B2B移动端订单账期还款和待还款列表
 * @author:wei.wang
 * @date:2021/10/22
 */
@Data
public class OrderB2BPaymentDTO extends BaseDTO {
    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 还款状态
     */
    private Integer repaymentStatus;

    /**
     * 供应商名称
     */
    private String sellerEname;

    /**
     * 供应商Id
     */
    private Long sellerEid;

    /**
     * 采购商名称
     */
    private String buyerEname;

    /**
     * 采购商Eid
     */
    private Long buyerEid;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 支付金额
     */
    private BigDecimal payAmount;

    /**
     * 还款金额
     */
    private BigDecimal stayPaymentAmount;

    /**
     * 订单账期流水Id
     */
    private Long paymentId;

    /**
     * 还款时间
     */
    private Date repaymentTime;

}
