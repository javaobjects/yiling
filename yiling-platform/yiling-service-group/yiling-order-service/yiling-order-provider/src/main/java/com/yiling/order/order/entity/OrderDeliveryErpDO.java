package com.yiling.order.order.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单ERP出库单信息
 * </p>
 *
 * @author wei.wang
 * @date 2021-09-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("order_delivery_erp")
public class OrderDeliveryErpDO extends BaseDO {

    private static final long serialVersionUID = 1L;

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
     * 现折金额
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
    @TableLogic
    private Integer delFlag;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;


}
