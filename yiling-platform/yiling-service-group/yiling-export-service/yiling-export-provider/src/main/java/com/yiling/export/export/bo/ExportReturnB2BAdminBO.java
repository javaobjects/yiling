package com.yiling.export.export.bo;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 退货单导出-商家端
 *
 * @author: yong.zhang
 * @date: 2021/11/11
 */
@Data
public class ExportReturnB2BAdminBO {

    /**
     * 退货单单据编号
     */
    private String returnNo;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 采购商ID
     */
    private Long buyerEid;

    /**
     * 采购商名称
     */
    private String buyerEname;

    /**
     * 采购商所在省
     */
    private String buyerProvince;

    /**
     * 采购商所在市
     */
    private String buyerCity;

    /**
     * 采购商所在区
     */
    private String buyerRegion;

    /**
     * 退货单单据类型
     */
    private String returnType;

    /**
     * 退货单单据状态
     */
    private String returnStatus;

    /**
     * 单据提交时间
     */
    private String createdTime;

    /**
     * 支付方式
     */
    private String paymentMethod;

    /**
     * 退货总金额
     */
    private BigDecimal returnAmount;

    /**
     * 优惠总金额
     */
    private BigDecimal discountAmount;

    /**
     * 实退总金额
     */
    private BigDecimal realReturnAmount;
}
