package com.yiling.hmc.wechat.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * C端参保缴费计划表
 * </p>
 *
 * @author fan.shen
 * @date 2022-03-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("hmc_insurance_record_pay_plan")
public class InsuranceRecordPayPlanDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 参保记录表id
     */
    private Integer insuranceRecordId;

    /**
     * 支付状态 1-已支付，2-未支付
     * @see com.yiling.hmc.wechat.enums.PayPlanPayStatusEnum
     */
    private Integer payStatus;

    /**
     * 缴费金额
     */
    private BigDecimal premium;

    /**
     * 保单号
     */
    private String policyNo;

    /**
     * 开始时间
     */
    @TableField("startTime")
    private Date startTime;

    /**
     * 结束时间
     */
    @TableField("endTime")
    private Date endTime;

    /**
     * 缴费期次
     */
    private Integer paySequence;

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
