package com.yiling.order.settlement.entity;

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
 * 结算单明细
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-08-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("settlement_detail")
public class SettlementDetailDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 结算单ID
     */
    private Long settlementId;

    /**
     * 订单ID
     */
    private Long sellerEid;

    /**
     * 订单ID
     */
    private Long buyerEid;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单明细
     */
    private Long orderDetailId;

    /**
     * 以岭商品Id
     */
    private Long goodsId;

    /**
     * 回款核销金额
     */
    private BigDecimal backAmount;

    /**
     * 红票核销金额
     */
    private BigDecimal discountsAmount;

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
