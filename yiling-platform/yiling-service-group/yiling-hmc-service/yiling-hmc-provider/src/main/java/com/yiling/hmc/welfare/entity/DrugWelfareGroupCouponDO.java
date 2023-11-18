package com.yiling.hmc.welfare.entity;

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
 * 入组福利券表
 * </p>
 *
 * @author hongyang.zhang
 * @date 2022-09-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("hmc_drug_welfare_group_coupon")
public class DrugWelfareGroupCouponDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 药品福利入组id
     */
    private Long groupId;

    /**
     * 药品福利id
     */
    private Long drugWelfareId;

    /**
     * 福利券id
     */
    private Long drugWelfareCouponId;

    /**
     * 福利券码
     */
    private String couponCode;

    /**
     * 福利券状态 1-待激活，2-已激活，3-已使用
     */
    private Integer couponStatus;

    /**
     * 要求达到的数量,满几盒
     */
    private Long requirementNumber;

    /**
     * 赠送数量,赠几盒
     */
    private Long giveNumber;

    /**
     * b2b优惠券id
     */
    private Long couponId;

    /**
     * 激活时间
     */
    private Date activeTime;

    /**
     * 核销时间
     */
    private Date verifyTime;

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
