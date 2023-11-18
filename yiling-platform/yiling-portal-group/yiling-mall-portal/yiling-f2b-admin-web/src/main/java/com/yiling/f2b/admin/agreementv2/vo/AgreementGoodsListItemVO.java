package com.yiling.f2b.admin.agreementv2.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议商品列表项 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementGoodsListItemVO extends BaseVO {

    /**
     * 商品ID
     */
    @ApiModelProperty("商品ID")
    private Long goodsId;

    /**
     * 规格商品ID
     */
    @ApiModelProperty("规格商品ID")
    private Long specificationGoodsId;

    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")
    private String goodsName;

    /**
     * 规格
     */
    @ApiModelProperty("规格")
    private String specifications;

    /**
     * 生产厂家名称
     */
    @ApiModelProperty("生产厂家名称")
    private String manufacturerName;

    /**
     * 品牌厂家名称
     */
    @ApiModelProperty("品牌厂家名称")
    private String brandManufacturerName;

    /**
     * 返利计算单价：1-销售 2-购进 3-付款金额
     */
    @ApiModelProperty(value = "返利计算单价：1-销售 2-购进 3-付款金额（见字典：agreement_rebate_task_standard）")
    private Integer rebateCalculatePrice;

    /**
     * 返利标准：1-销售 2-购进 3-付款金额
     */
    @ApiModelProperty(value = "返利标准：1-销售 2-购进 3-付款金额（见字典：agreement_rebate_task_standard）")
    private Integer rebateStandard;

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
     * 购进渠道：1-直供 2-所有商业公司购进 3-指定商业公司购进
     */
    @ApiModelProperty(value = "购进渠道：1-直供 2-所有商业公司购进 3-指定商业公司购进")
    private Integer buyChannel;

}
