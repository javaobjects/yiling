package com.yiling.user.agreement.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议返利条件快照表
 * </p>
 *
 * @author dexi.yao
 * @date 2021-07-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("agreement_condition_snapshot")
public class AgreementConditionSnapshotDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 协议主键
     */
    private Long agreementId;

    /**
     * 版本号
     */
    @Version
    private Integer version;

    /**
     * 政策类型 1-购进额 2-回款额
     */
    private Integer policyType;

    /**
     * 协议政策
     */
    private BigDecimal policyValue;

    /**
     * 条件序号可为月,季度,梯度
     */
    private Integer rangeNo;

    /**
     * 条件总额
     */
    private BigDecimal totalAmount;

    /**
     * 拆解百分比
     */
    private Integer percentage;

    /**
     * 拆解数额
     */
    private BigDecimal amount;

    /**
     * 梯度天数起始值
     */
    private BigDecimal mixValue;

    /**
     * 梯度天数最大值
     */
    private BigDecimal maxValue;

    /**
     * 固定时间节点
     */
    private Integer timeNode;

    /**
     * 回款方式0全部1指定（回款形式集合 1-电汇 2-银行汇票 3-银行承兑）
     */
    private Integer backAmountType;

    /**
     * 支付方式0全部1指定 （条件支付方式 1-账期支付 2-预付款支付）
     */
    private Integer payType;

    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 备注
     */
    private String remark;


}
