package com.yiling.f2b.admin.agreementv2.form;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 添加协议时段 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-28
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddAgreementRebateTimeSegmentForm extends BaseForm {

    /**
     * 时段类型：1-全时段 2-子时段
     */
    @Range(min = 1, max = 2)
    @ApiModelProperty(value = "时段类型：1-全时段 2-子时段",required = true)
    private Integer type;

    /**
     * 时段开始时间
     */
    @ApiModelProperty("时段开始时间（时段类型为全时段时不需传入）")
    private Date startTime;

    /**
     * 时段结束时间
     */
    @ApiModelProperty("时段结束时间（时段类型为全时段时不需传入）")
    private Date endTime;

    /**
     * 排序
     */
    @Min(0)
    @ApiModelProperty("排序")
    private Integer sort;

    /**
     * 是否规模返利（ka协议时才存在）
     */
    @ApiModelProperty("是否规模返利（ka协议时才可能存在）")
    private Boolean scaleRebateFlag;

    /**
     * 是否基础服务奖励（ka协议时才存在）
     */
    @ApiModelProperty("是否基础服务奖励（ka协议时才可能存在）")
    private Boolean basicServiceRewardFlag;

    /**
     * 是否项目服务奖励（ka协议时才存在）
     */
    @ApiModelProperty("是否项目服务奖励（ka协议时才可能存在）")
    private Boolean projectServiceRewardFlag;

    /**
     * 协议返利商品组集合
     */
    @ApiModelProperty("协议返利商品组集合（最多6个商品组）")
    private List<AddAgreementRebateGoodsGroupForm> agreementRebateGoodsGroupList;

    /**
     * 规模返利阶梯集合
     */
    @ApiModelProperty("规模返利阶梯集合（ka协议时才可能存在）")
    private List<AddAgreementRebateScaleRebateForm> agreementScaleRebateList;

    /**
     * 基础服务奖励阶梯集合
     */
    @ApiModelProperty("基础服务奖励阶梯集合（ka协议时才可能存在）")
    private List<AddAgreementRebateBasicServiceRewardForm> agreementRebateBasicServiceRewardList;

    /**
     * 项目服务奖励阶梯集合
     */
    @ApiModelProperty("项目服务奖励阶梯集合（ka协议时才可能存在）")
    private List<AddAgreementRebateProjectServiceRewardForm> agreementRebateProjectServiceRewardList;

}
