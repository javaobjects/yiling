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
 * 保司结账表
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("hmc_insurance_settlement")
public class InsuranceSettlementDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 客户姓名(被保人名称)
     */
    private String issueName;

    /**
     * 保险提供服务商id
     */
    private Long insuranceCompanyId;

    /**
     * 保险提供服务商名称
     */
    private String insuranceCompanyName;

    /**
     * 保单id
     */
    private Long insuranceRecordId;

    /**
     * 保险单号
     */
    private String policyNo;

    /**
     * 第三方打款单号
     */
    private String thirdPayNo;

    /**
     * 保司结算状态:1-待结算/2-已结算/3-无需结算失效单/4-预付款抵扣已结
     */
    private Integer insuranceSettleStatus;

    /**
     * 收款账号
     */
    private String accountNo;

    /**
     * 打款时间
     */
    private Date payTime;

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
