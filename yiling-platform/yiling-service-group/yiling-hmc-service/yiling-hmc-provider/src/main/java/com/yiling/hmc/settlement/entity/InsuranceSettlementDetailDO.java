package com.yiling.hmc.settlement.entity;

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
 * 保司结账明细表
 * </p>
 *
 * @author yong.zhang
 * @date 2022-05-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("hmc_insurance_settlement_detail")
public class InsuranceSettlementDetailDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 保司结账主表id
     */
    private Long insuranceSettlementId;

    /**
     * 保险id
     */
    private Long insuranceRecordId;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 平台订单编号
     */
    private String orderNo;

    /**
     * 打款金额
     */
    private BigDecimal payAmount;

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
