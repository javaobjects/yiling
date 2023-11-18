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
public class B2bSettUserExcelModel {

    /**
     * id
     */
    private Long id;


    /**
     * 结算单号
     */
    @ExcelProperty(value = "结算单号")
    private String code;


    /**
     * 创建时间
     */
    @DateTimeFormat("yyyy-MM-dd")
    @ExcelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 结算类型
     */
    @ExcelProperty(value = "结算类型")
    private String typeStr;

    /**
     * 结算状态
     */
    @ExcelProperty(value = "结算状态")
    private String statusStr;

    /**
     * 结算时间
     */
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(value = "结算时间")
    private Date settlementTime;

    /**
     * 供应商
     */
    @ExcelProperty(value = "供应商")
    private String ename;

    /**
     * 货款金额
     */
    @ExcelProperty(value = "货款金额")
    private BigDecimal goodsAmount;

    /**
     * 货款/退款金额
     */
    @ExcelProperty(value = "货款/退款金额")
    private BigDecimal refundGoodsAmount;

    /**
     * 平台承担促销金额
     */
    @ExcelProperty(value = "平台承担促销金额")
    private BigDecimal discountAmount;

    /**
     * 平台承担促销退款金额
     */
    @ExcelProperty(value = "平台承担促销退款金额")
    private BigDecimal refundDiscountAmount;

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
