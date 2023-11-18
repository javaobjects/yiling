package com.yiling.export.export.model;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author dexi.yao
 * @date 2021-09-23
 */
@Data
@EqualsAndHashCode
@ContentRowHeight(15)
@HeadRowHeight(20)
@ColumnWidth(20)
@ExcelIgnoreUnannotated
public class B2bSettDetailExcelModel {

    /**
     * 结算单id
     */
    private Long settlementId;

    /**
     * 结算单号
     */
    @ExcelProperty(value = "结算单号")
    private String code;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 订单号
     */
    @ExcelProperty(value = "订单号")
    private String orderNo;

    /**
     * 买家id
     */
    private Long buyerEid;

    /**
     * 卖家企业id
     */
    private Long sellerEid;


    /**
     * 采购商名称
     */
    @ExcelProperty(value = "采购商名称")
    private String buyerName;

    /**
     * 客户ERP内码
     */
    @ExcelProperty(value = "客户ERP内码")
    private String customerErpCode;

    /**
     * 供应商
     */
    @ExcelProperty(value = "供应商")
    private String sellerName;

    /**
     * 订单创建时间
     */
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(value = "订单创建时间")
    private Date orderCreateTime;

    /**
     * 订单支付时间
     */
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(value = "订单支付时间")
    private Date paymentTime;

    /**
     * 结算类型
     */
    @ExcelProperty(value = "结算类型")
    private String typeStr;

    /**
     * 结算单创建时间
     */
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(value = "结算单创建时间")
    private Date createTime;

    /**
     * 货款金额
     */
    @ExcelProperty(value = "货款金额")
    private BigDecimal goodsAmount;

    /**
     * 货款退款金额
     */
    @ExcelProperty(value = "货款退款金额")
    private BigDecimal refundGoodsAmount;

    /**
     * 平台承担券金额
     */
    @ExcelProperty(value = "平台承担券金额")
    private BigDecimal couponAmount;

    /**
     * 平台承担券退款金额
     */
    @ExcelProperty(value = "平台承担券退款金额")
    private BigDecimal refundCouponAmount;

    /**
     * 平台承担秒杀/特价金额
     */
    @ExcelProperty(value = "平台承担秒杀/特价金额")
    private BigDecimal promotionAmount;

    /**
     * 平台承担秒杀/特价退款金额
     */
    @ExcelProperty(value = "平台承担秒杀/特价退款金额")
    private BigDecimal refundPromotionAmount;

    /**
     * 平台承担满赠金额
     */
    @ExcelProperty(value = "平台承担满赠金额")
    private BigDecimal giftAmount;

    /**
     * 平台承担满赠退款金额
     */
    @ExcelProperty(value = "平台承担满赠退款金额")
    private BigDecimal refundGiftAmount;

    /**
     * 平台承担套装金额
     */
    @ExcelProperty(value = "平台承担套装金额")
    private BigDecimal comPacAmount;

    /**
     * 平台承担套装退款金额
     */
    @ExcelProperty(value = "平台承担套装退款金额")
    private BigDecimal refundComPacAmount;

    /**
     * 平台承担预售优惠金额
     */
    @ExcelProperty(value = "平台承担预售优惠金额")
    private BigDecimal presaleDiscountAmount;

    /**
     * 平台承担预售优惠退款金额
     */
    @ExcelProperty(value = "平台承担预售优惠退款金额")
    private BigDecimal refundPreAmount;

    /**
     * 平台承担支付促销优惠金额
     */
    @ExcelProperty(value = "平台承担支付促销优惠金额")
    private BigDecimal payDiscountAmount;

    /**
     * 平台承担支付促销退款金额
     */
    @ExcelProperty(value = "平台承担支付促销退款金额")
    private BigDecimal refundPayAmount;

    /**
     * 预售违约金额
     */
    @ExcelProperty(value = "预售违约金额")
    private BigDecimal presaleDefaultAmount;

    /**
     * 结算总金额
     */
    @ExcelProperty(value = "结算总金额")
    private BigDecimal amount;


}
