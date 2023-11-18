package com.yiling.export.export.bo;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 退货单明细导出-商家端
 *
 * @author: yong.zhang
 * @date: 2021/11/11
 */
@Data
public class ExportReturnDetailB2BAdminBO {

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
     * 商品ID
     */
    private Long goodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 规格型号
     */
    private String goodsSpecification;

    /**
     * 批次号/序列号
     */
    private String batchNo;

    /**
     * 有效期至
     */
    private String expiredDate;

    /**
     * 批次退货数量
     */
    private Integer returnQuality;

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
    private BigDecimal returnAmount;
    
}
