package com.yiling.export.export.bo;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 退货单导出
 *
 * @author: yong.zhang
 * @date: 2021/11/11
 */
@Data
public class ExportReturnPOPBO {

    /**
     * 退货单单据编号
     */
    private String     returnNo;

    /**
     * 订单编号
     */
    private String     orderNo;

    /**
     * 退货单单据类型
     */
    private String     returnType;

    /**
     * 退货单单据状态
     */
    private String     returnStatus;

    /**
     * 单据提交时间
     */
    private String     createdTime;

    /**
     * 支付方式
     */
    private String     paymentMethod;

    /**
     * 退款总金额(元)
     */
    private BigDecimal totalReturnAmount;

    /**
     * 商品ID
     */
    private Long       goodsId;

    /**
     * 商品名称
     */
    private String     goodsName;

    /**
     * 规格型号
     */
    private String     goodsSpecification;

    /**
     * 退货数量
     */
    private Integer    goodsReturnQuality;

    /**
     * 商品单价
     */
    private BigDecimal goodsPrice;

    /**
     * 退货金额
     */
    private BigDecimal goodsReturnAmount;

    /**
     * 折扣金额
     */
    private BigDecimal goodsDiscountAmount;

    /**
     * 退款金额
     */
    private BigDecimal returnAmounts;

    /**
     * 批次号/序列号
     */
    private String     batchNo;

    /**
     * 有效期至
     */
    private String     expiredDate;

    /**
     * 批次退货数量
     */
    private Integer    returnQuality;

    /**
     * 合同编号
     */
    private String     contractNumber;
}
