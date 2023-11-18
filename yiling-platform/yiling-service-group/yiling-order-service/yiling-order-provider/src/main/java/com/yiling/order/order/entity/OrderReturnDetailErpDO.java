package com.yiling.order.order.entity;

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
 * 退货单明细（erp出库单维度）
 * </p>
 *
 * @author yong.zhang
 * @date 2021-09-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("order_return_detail_erp")
public class OrderReturnDetailErpDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 退货单ID
     */
    private Long returnId;

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
     * 退货数量
     */
    private Integer returnQuantity;

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
