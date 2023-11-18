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
public class B2bSettOrderInfoExcelModel {


    /**
     * 结算单号
     */
    @ExcelProperty(value = "结算单号")
    private String code;

    /**
     * 订单号
     */
    @ExcelProperty(value = "订单号")
    private String orderNo;

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
     * 订单支付时间
     */
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(value = "订单支付时间")
    private Date paymentTime;

    /**
     * 结算时间
     */
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(value = "结算时间")
    private Date settlementTime;

    /**
     * 结算类型
     */
    @ExcelProperty(value = "结算类型")
    private String typeStr;

    /**
     * 商品ID
     */
    @ExcelProperty(value = "商品ID")
    private Long goodsId;

    /**
     * ERP商品内码
     */
    @ExcelProperty(value = "ERP商品内码")
    private String inSn;

    /**
     * ERP商品编码
     */
    @ExcelProperty(value = "ERP商品编码")
    private String goodsErpCode;

    /**
     * 商品名称
     */
    @ExcelProperty(value = "商品名称")
    private String goodsName;

    /**
     * 规格
     */
    @ExcelProperty(value = "规格")
    private String goodsSpecification;

    /**
     * 生产厂家
     */
    @ExcelProperty(value = "生产厂家")
    private String goodsManufacturer;

    /**
     * 购买数量
     */
    @ExcelProperty(value = "购买数量")
    private Integer goodsQuantity;

    /**
     * 发货数量
     */
    @ExcelProperty(value = "发货数量")
    private Integer deliveryQuantity;

    /**
     * 卖家退货数量
     */
    @ExcelProperty(value = "卖家退货数量")
    private Integer sellerReturnQuantity;

    /**
     * 收货数量
     */
    @ExcelProperty(value = "收货数量")
    private Integer receiveQuantity;

    /**
     * 买家退货数量
     */
    @ExcelProperty(value = "买家退货数量")
    private Integer returnQuantity;

    /**
     * 商品单价
     */
    @ExcelProperty(value = "商品单价")
    private BigDecimal goodsPrice;

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
     * 货款结算金额
     */
    @ExcelProperty(value = "货款结算金额")
    private BigDecimal goodsSettleAmount;

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
     * 平台承担券结算金额
     */
    @ExcelProperty(value = "平台承担券结算金额")
    private BigDecimal settleCouponAmount;

    /**
     * 平台承担秒杀特价金额
     */
    @ExcelProperty(value = "平台承担秒杀特价金额")
    private BigDecimal promotionAmount;

    /**
     * 平台承担秒杀特价退款金额
     */
    @ExcelProperty(value = "平台承担秒杀特价退款金额")
    private BigDecimal refundPromotionAmount;

    /**
     * 平台承担秒杀特价结算金额
     */
    @ExcelProperty(value = "平台承担秒杀特价结算金额")
    private BigDecimal settPromotionAmount;

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
     * 平台承担套装结算金额
     */
    @ExcelProperty(value = "平台承担套装结算金额")
    private BigDecimal settComPacAmount;

    /**
     * 商品单价
     */
    @ExcelProperty(value = "平台承担预售优惠金额")
    private BigDecimal presaleDiscountAmount;

    /**
     * 平台承担预售优惠退款金额
     */
    @ExcelProperty(value = "平台承担预售优惠退款金额")
    private BigDecimal returnPresaleDiscountAmount;

    /**
     * 平台承担预售优惠结算金额
     */
    @ExcelProperty(value = "平台承担预售优惠结算金额")
    private BigDecimal settPreSaleAmount;

    /**
     * 平台承担支付促销金额
     */
    @ExcelProperty(value = "平台承担支付促销金额")
    private BigDecimal paymentPlatformDiscountAmount;

    /**
     * 平台承担支付促销退款金额
     */
    @ExcelProperty(value = "平台承担支付促销退款金额")
    private BigDecimal refundPayAmount;

    /**
     * 平台承担支付促销结算金额
     */
    @ExcelProperty(value = "平台承担支付促销结算金额")
    private BigDecimal settPaySaleAmount;

    /**
     * 预售违约金额
     */
    @ExcelProperty(value = "预售违约金额")
    private BigDecimal presaleDefaultAmount;

    /**
     * 预售违约退款金额
     */
    private BigDecimal refundPresaleDefaultAmount;

    /**
     * 预售违约结算金额
     */
    private BigDecimal preDefSettlementAmount;

    /**
     * 结算总金额
     */
    @ExcelProperty(value = "结算总金额")
    private BigDecimal amount;

}
