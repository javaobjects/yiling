package com.yiling.hmc.wechat.entity;

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
 * C端参保记录支付流水表
 * </p>
 *
 * @author fan.shen
 * @date 2022-03-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("hmc_insurance_record_pay")
public class InsuranceRecordPayDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 参保记录id
     */
    private Long insuranceRecordId;

    /**
     * 交易单号
     */
    private String orderNo;

    /**
     * 保单号
     */
    private String policyNo;

    /**
     * 支付方式
     */
    private String payWayId;

    /**
     * 泰康支付订单号
     */
    private String billNo;

    /**
     * 支付流水号
     */
    private String transactionId;

    /**
     * 主动请求支付编号
     */
    private String activePayId;

    /**
     * 实收金额
     */
    private Long amount;

    /**
     * 签约编号 月交的方案，会有值
     */
    private String signPkSubId;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 支付状态 1-支付成功；2-支付失败
     */
    private Integer payStatus;

    /**
     * 安全验证
     */
    private String token;

    /**
     * 最小期次
     */
    private String minSequence;

    /**
     * 最大期次
     */
    private String maxSequence;

    /**
     * 缴费期次列表
     */
    private String premiumPlanList;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 是否删除 0-否，1-是
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
