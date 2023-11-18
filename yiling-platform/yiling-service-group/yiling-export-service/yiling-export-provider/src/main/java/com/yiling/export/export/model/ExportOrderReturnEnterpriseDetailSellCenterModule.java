package com.yiling.export.export.model;

import java.math.BigDecimal;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 退货单明细导出-中台端
 *
 * @author: yong.zhang
 * @date: 2021/11/16
 */
@Data
@EqualsAndHashCode
@ContentRowHeight(15)
@HeadRowHeight(20)
@ColumnWidth(20)
public class ExportOrderReturnEnterpriseDetailSellCenterModule {

    /**
     * 退货单单据编号
     */
    @ExcelProperty(value = "退货单单据编号")
    private String returnNo;

    /**
     * 订单编号
     */
    @ExcelProperty(value = "订单编号")
    private String orderNo;

    /**
     * 采购商ID
     */
    @ExcelProperty(value = "采购商ID")
    private Long buyerEid;

    /**
     * 采购商名称
     */
    @ExcelProperty(value = "采购商名称")
    private String buyerEname;

    /**
     * 采购商所在省
     */
    @ExcelProperty(value = "采购商所在省")
    private String buyerProvince;

    /**
     * 采购商所在市
     */
    @ExcelProperty(value = "采购商所在市")
    private String buyerCity;

    /**
     * 采购商所在区
     */
    @ExcelProperty(value = "采购商所在区")
    private String buyerRegion;

    /**
     * 供应商ID
     */
    @ExcelProperty(value = "供应商ID")
    private Long sellerEid;

    /**
     * 供应商名称
     */
    @ExcelProperty(value = "供应商名称")
    private String sellerEname;

    /**
     * 商品ID
     */
    @ExcelProperty(value = "商品ID")
    private Long goodsId;

    /**
     * 商品名称
     */
    @ExcelProperty(value = "商品名称")
    private String goodsName;

    /**
     * 规格型号
     */
    @ExcelProperty(value = "规格型号")
    private String goodsSpecification;

    /**
     * 批次号/序列号
     */
    @ExcelProperty(value = "批次号/序列号")
    private String batchNo;

    /**
     * 有效期至
     */
    @ExcelProperty(value = "有效期至")
    private String expiredDate;

    /**
     * 批次退货数量
     */
    @ExcelProperty(value = "批次退货数量")
    private Integer returnQuality;

    /**
     * 商品单价
     */
    @ExcelProperty(value = "商品单价")
    private BigDecimal goodsPrice;

    /**
     * 退货金额
     */
    @ExcelProperty(value = "退货金额")
    private BigDecimal goodsReturnAmount;

    /**
     * 折扣金额
     */
    @ExcelProperty(value = "折扣金额")
    private BigDecimal goodsDiscountAmount;

    /**
     * 退款金额
     */
    @ExcelProperty(value = "退款金额")
    private BigDecimal returnAmount;

}
