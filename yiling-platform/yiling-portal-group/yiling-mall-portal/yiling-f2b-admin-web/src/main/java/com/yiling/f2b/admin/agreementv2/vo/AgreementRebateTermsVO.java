package com.yiling.f2b.admin.agreementv2.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.yiling.f2b.admin.agreementv2.form.AddAgreementOtherRebateForm;
import com.yiling.f2b.admin.agreementv2.form.AddAgreementRebatePayEnterpriseForm;
import com.yiling.f2b.admin.agreementv2.form.AddAgreementRebateTimeSegmentForm;
import com.yiling.framework.common.base.BaseVO;
import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议返利条款 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-10
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementRebateTermsVO extends BaseVO {

    /**
     * 是否底价供货：0-否 1-是
     */
    @ApiModelProperty(value = "是否底价供货：0-否 1-是")
    private Integer reserveSupplyFlag;

    /**
     * 商品返利规则设置方式：1-全品设置 2-分类设置
     */
    @ApiModelProperty(value = "商品返利规则设置方式：1-全品设置 2-分类设置")
    private Integer goodsRebateRuleType;

    /**
     * 返利支付方：1-甲方 2-指定商业公司
     */
    @ApiModelProperty("返利支付方：1-甲方 2-指定商业公司（见字典：agreement_rebate_pay）")
    private Integer rebatePay;

    /**
     * 返利上限(元)
     */
    @ApiModelProperty("返利上限(元)")
    private BigDecimal maxRebate;

    /**
     * 返利兑付方式：1-电汇 2-冲红 3-票折 4-易货 5-3个月承兑 6-6个月承兑 7-其他 8-支票
     */
    @ApiModelProperty("返利兑付方式：1-电汇 2-冲红 3-票折 4-易货 5-3个月承兑 6-6个月承兑 7-其他 8-支票（见字典：agreement_rebate_cash_type）")
    private Integer rebateCashType;

    /**
     * 返利兑付时间：1-协议生效月起 2-协议完结月起
     */
    @ApiModelProperty("返利兑付时间：1-协议生效月起 2-协议完结月起（见字典：agreement_rebate_cash_time）")
    private Integer rebateCashTime;

    /**
     * 返利兑付时段
     */
    @ApiModelProperty("返利兑付时段")
    private Integer rebateCashSegment;

    /**
     * 返利兑付单位：1-月 2-天
     */
    @ApiModelProperty("返利兑付单位：1-月 2-天")
    private Integer rebateCashUnit;

    /**
     * 其他备注
     */
    @ApiModelProperty("其他备注")
    private String otherRemark;

    /**
     * 是否有任务量：0-否 1-是
     */
    @ApiModelProperty(value = "是否有任务量：0-否 1-是")
    private Integer taskFlag;

    /**
     * 任务量标准：1-销售 2-购进 3-付款金额
     */
    @ApiModelProperty("任务量标准：1-销售 2-购进 3-付款金额（见字典：agreement_rebate_task_standard）")
    private Integer taskStandard;

    /**
     * 返利标准：1-销售 2-购进 3-付款金额
     */
    @ApiModelProperty(value = "返利标准：1-销售 2-购进 3-付款金额（见字典：agreement_rebate_task_standard）")
    private Integer rebateStandard;

    /**
     * 返利计算单价：1-销售 2-购进 3-付款金额
     */
    @ApiModelProperty(value = "返利计算单价：1-销售 2-购进 3-付款金额（见字典：agreement_rebate_task_standard）")
    private Integer rebateCalculatePrice;

    /**
     * 返利阶梯条件计算方法：1-覆盖计算 2-叠加计算
     */
    @ApiModelProperty(value = "返利阶梯条件计算方法：1-覆盖计算 2-叠加计算（见字典：agreement_rebate_stage_method）")
    private Integer rebateStageMethod;

    /**
     * 返利计算规则：1-按单计算 2-汇总计算
     */
    @ApiModelProperty(value = "返利计算规则：1-按单计算 2-汇总计算（见字典：agreement_rebate_calculate_rule）")
    private Integer rebateCalculateRule;

    /**
     * 返利计算规则类型：1-起返 2-倍返
     */
    @ApiModelProperty(value = "返利计算规则类型：1-起返 2-倍返（见字典：agreement_rebate_rule_type）")
    private Integer rebateRuleType;

    /**
     * 时段类型设置：1-全时段统一配置 2-多时段分别配置 3-全时段+多时段组合配置
     */
    @ApiModelProperty("时段类型设置：1-全时段统一配置 2-多时段分别配置 3-全时段+多时段组合配置（见字典：agreement_time_segment_type_set）")
    private Integer timeSegmentTypeSet;

    /**
     * 是否兑付子时段（有未达成任务量的子时段时）
     */
    @ApiModelProperty("是否兑付子时段（有未达成任务量的子时段时）")
    private Boolean cashChildSegmentFlag;

    /**
     * 是否兑付全时段（有未达成任务量的子时段时）
     */
    @ApiModelProperty("是否兑付全时段（有未达成任务量的子时段时）")
    private Boolean cashAllSegmentFlag;

    /**
     * 核心商品组关联性：1-每个核心商品组相互独立 2-视为核心组合
     */
    @ApiModelProperty("核心商品组关联性：1-每个核心商品组相互独立 2-视为核心组合（见字典：agreement_core_commodity_group_relevance）")
    private Integer coreCommodityGroupRelevance;

    /**
     * 核心商品组任务量未完成时：1-仅当前核心商品组不兑付 2-全商品主任务不兑付 3-全协议均不兑付
     */
    @ApiModelProperty("核心商品组任务量未完成时：1-仅当前核心商品组不兑付 2-全商品主任务不兑付 3-全协议均不兑付（见字典：agreement_core_commodity_group_fail）")
    private Integer coreCommodityGroupFail;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 协议返利支付方指定商业公司集合
     */
    @ApiModelProperty("协议返利支付方指定商业公司集合")
    private List<AgreementRebatePayEnterpriseVO> agreementRebatePayEnterpriseList;

    /**
     * 非商品返利集合（最多6个阶梯）
     */
    @ApiModelProperty("非商品返利集合（最多6个阶梯）")
    private List<AgreementOtherRebateVO> agreementOtherRebateList;

    /**
     * 协议返利时段集合
     */
    @ApiModelProperty("协议返利时段集合")
    private List<AgreementRebateTimeSegmentVO> agreementRebateTimeSegmentList;

}
