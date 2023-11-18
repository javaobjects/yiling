package com.yiling.order.order.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 订单开票明细
 * @author:wei.wang
 * @date:2021/8/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderInvoiceDetailDTO extends BaseDTO {

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单明细ID
     */
    private Long detailId;

    /**
     * 关联分组号
     */
    private String groupNo;

    /**
     * ERP出库单号
     */
    private String erpDeliveryNo;

    /**
     * ERP出库录入id
     */
    private String erpSendOrderId;


    /**
     * 商品标准库ID
     */
    private Long standardId;

    /**
     * 批次号
     */
    private String batchNo;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 商品ERP编码
     */
    private String goodsErpCode;

    /**
     * 商品单价
     */
    private BigDecimal goodsPrice;

    /**
     * 商品原始价格
     */
    private BigDecimal originalPrice;

    /**
     * 发货商品数量
     */
    private Integer goodsQuantity;

    /**
     * 发货商品小计
     */
    private BigDecimal goodsAmount;

    /**
     * 现折金额
     */
    private BigDecimal cashDiscountAmount;

    /**
     * 票折方式：1-按比率 2-按金额
     */
    private Integer ticketDiscountType;

    /**
     * 票折比率
     */
    private BigDecimal ticketDiscountRate;

    /**
     * 票折单价
     */
    private BigDecimal ticketDiscountPrice;

    /**
     * 票折小计
     */
    private BigDecimal ticketDiscountAmount;

    /**
     * 开票金额
     */
    private BigDecimal invoiceAmount;

    /**
     * 开票数量
     */
    private Integer invoiceQuantity;

    /**
     * 是否删除：0-否 1-是
     */
    private Integer delFlag;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;


}
