package com.yiling.order.order.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单优惠劵使用记录表
 * </p>
 *
 * @author zhigang.guo
 * @date 2021-10-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("order_coupon_use")
public class OrderCouponUseDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 活动ID
     */
    private Long couponActivityId;

    /**
     * 优惠劵ID
     */
    private Long couponId;

    /**
     * 优惠劵名称
     */
    private String couponName;

    /**
     * 实际使用金额
     */
    private BigDecimal amount;

    /**
     * 平台承担比例
     */
    private BigDecimal platformRatio;

    /**
     * 商家承担比例
     */
    private BigDecimal businessRatio;

    /**
     * 优惠劵类型(1,平台劵,2,商家劵)
     */
    private Integer couponType;

    /**
     * 是否归还(1,未归还,2,已归还)
     */
    private Integer isReturn;

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


}
