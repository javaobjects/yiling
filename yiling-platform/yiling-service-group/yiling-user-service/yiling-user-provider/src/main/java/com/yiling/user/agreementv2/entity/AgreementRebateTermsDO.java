package com.yiling.user.agreementv2.entity;

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
 * 协议返利条款表
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("agreement_rebate_terms")
public class AgreementRebateTermsDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 协议ID
     */
    private Long agreementId;

    /**
     * 是否底价供货：0-否 1-是
     */
    private Integer reserveSupplyFlag;

    /**
     * 商品返利规则设置方式：1-全品设置 2-分类设置
     */
    private Integer goodsRebateRuleType;

    /**
     * 返利支付方：1-甲方 2-指定商业公司
     */
    private Integer rebatePay;

    /**
     * 返利上限(元)
     */
    private BigDecimal maxRebate;

    /**
     * 返利兑付方式：1-电汇 2-冲红 3-票折 4-易货 5-3个月承兑 6-6个月承兑 7-其他 8-支票
     */
    private Integer rebateCashType;

    /**
     * 返利兑付时间：1-协议生效月起 2-协议完结月起
     */
    private Integer rebateCashTime;

    /**
     * 返利兑付时段
     */
    private Integer rebateCashSegment;

    /**
     * 返利兑付单位：1-月 2-天
     */
    private Integer rebateCashUnit;

    /**
     * 其他备注
     */
    private String otherRemark;

    /**
     * 是否有任务量：0-否 1-是
     */
    private Integer taskFlag;

    /**
     * 任务量标准：1-销售 2-购进 3-付款金额
     */
    private Integer taskStandard;

    /**
     * 返利标准：1-销售 2-购进 3-付款金额
     */
    private Integer rebateStandard;

    /**
     * 返利计算单价：1-销售 2-购进 3-付款金额
     */
    private Integer rebateCalculatePrice;

    /**
     * 返利阶梯条件计算方法：1-覆盖计算 2-叠加计算
     */
    private Integer rebateStageMethod;

    /**
     * 返利计算规则：1-按单计算 2-汇总计算
     */
    private Integer rebateCalculateRule;

    /**
     * 返利计算规则类型：1-起返 2-倍返
     */
    private Integer rebateRuleType;

    /**
     * 时段类型设置：1-全时段统一配置 2-多时段分别配置 3-全时段+多时段组合配置
     */
    private Integer timeSegmentTypeSet;

    /**
     * 是否兑付子时段：0-否 1-是（有未达成任务量的子时段时）
     */
    private Integer cashChildSegmentFlag;

    /**
     * 是否兑付全时段：0-否 1-是（有未达成任务量的子时段时）
     */
    private Integer cashAllSegmentFlag;

    /**
     * 核心商品组关联性：1-每个核心商品组相互独立 2-视为核心组合
     */
    private Integer coreCommodityGroupRelevance;

    /**
     * 核心商品组任务量未完成时：1-仅当前核心商品组不兑付 2-全商品主任务不兑付 3-全协议均不兑付
     */
    private Integer coreCommodityGroupFail;

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
