package com.yiling.order.order.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.swing.plaf.basic.BasicIconFactory;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 订单ERP出库单信息
 * @author:wei.wang
 * @date:2021/9/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderDeliveryErpDTO extends BaseDTO {

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单明细ID
     */
    private Long detailId;

    /**
     * 批次号
     */
    private String batchNo;

    /**
     * 有效期
     */
    private Date expiryDate;

    /**
     * 生产日期
     */
    private Date produceDate;

    /**
     * 发货数量
     */
    private Integer deliveryQuantity;

    /**
     * 現折金額
     */
    private BigDecimal cashDiscountAmount;

    /**
     * ERP出库单号
     */
    private String erpDeliveryNo;

    /**
     * ERP出库录入id
     */
    private String erpSendOrderId;

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
