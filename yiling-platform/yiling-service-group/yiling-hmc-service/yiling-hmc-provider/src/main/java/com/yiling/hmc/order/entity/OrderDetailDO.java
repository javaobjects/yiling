package com.yiling.hmc.order.entity;

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
 * 订单明细表
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("hmc_order_detail")
public class OrderDetailDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 保险id
     */
    private Long insuranceRecordId;

    /**
     * 药品id
     */
    private Long hmcGoodsId;

    /**
     * 药品id
     */
    private Long goodsId;

    /**
     * 售卖规格id
     */
    private Long sellSpecificationsId;

    /**
     * 药品名称
     */
    private String goodsName;

    /**
     * 参保价
     */
    private BigDecimal insurancePrice;

    /**
     * 保司跟以岭结算单价
     */
    private BigDecimal settlePrice;

    /**
     * 保司跟以岭结算额
     */
    private BigDecimal settleAmount;

    /**
     * 以岭跟终端结算单价
     */
    private BigDecimal terminalSettlePrice;

    /**
     * 以岭跟终端结算额
     */
    private BigDecimal terminalSettleAmount;

    /**
     * 药品规格
     */
    private String goodsSpecifications;

    /**
     * 购买数量
     */
    private Long goodsQuantity;

    /**
     * 商品小计 = 参保价 * 数量
     */
    private BigDecimal goodsAmount;

    /**
     * 保司药品编码
     */
    private String insuranceGoodsCode;

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
