package com.yiling.export.export.model;

import java.math.BigDecimal;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 退货单导出-中台端
 *
 * @author: yong.zhang
 * @date: 2021/11/16
 */
@Data
@EqualsAndHashCode
@ContentRowHeight(15)
@HeadRowHeight(20)
@ColumnWidth(20)
public class ExportOrderReturnEnterpriseBuyerCenterModel {

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
     * 采购商所在省
     */
    @ExcelProperty(value = "供应商所在省")
    private String buyerProvince;

    /**
     * 采购商所在市
     */
    @ExcelProperty(value = "供应商所在市")
    private String buyerCity;

    /**
     * 采购商所在区
     */
    @ExcelProperty(value = "供应商所在区")
    private String buyerRegion;

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
     * 退货单单据类型
     */
    @ExcelProperty(value = "退货单单据类型")
    private String returnType;

    /**
     * 退货单单据状态
     */
    @ExcelProperty(value = "退货单单据状态")
    private String returnStatus;

    /**
     * 单据提交时间
     */
    @ExcelProperty(value = "单据提交时间")
    private String createdTime;

    /**
     * 支付方式
     */
    @ExcelProperty(value = "支付方式")
    private String paymentMethod;

    /**
     * 退货总金额
     */
    @ExcelProperty(value = "退款总金额(元)")
    private BigDecimal returnAmount;

    /**
     * 优惠总金额
     */
    @ExcelProperty(value = "优惠总金额")
    private BigDecimal discountAmount;

    /**
     * 实退总金额
     */
    @ExcelProperty(value = "实退总金额")
    private BigDecimal realReturnAmount;
}
