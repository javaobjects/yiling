package com.yiling.admin.b2b.promotion.form;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 促销活动基本信息
 * @author: yong.zhang
 * @date: 2021/11/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "PromotionActivitySaveForm", description = "促销活动基本信息")
public class PromotionActivitySaveForm extends BaseForm {

    @ApiModelProperty(value = "促销名称")
    @NotNull(message = "促销名称不能为空")
    private String        name;

    @ApiModelProperty(value = "活动分类（1-平台活动；2-商家活动；）")
    @NotNull(message = "活动分类不能为空")
    private Integer       sponsorType;

    @ApiModelProperty(value = "开始时间")
    @NotNull(message = "开始时间不能为空")
    private Date          beginTime;

    @ApiModelProperty(value = "结束时间")
    @NotNull(message = "结束时间不能为空")
    private Date          endTime;

    @ApiModelProperty(value = "促销范围-选择平台（1-B2B；2-销售助手）")
    @NotEmpty(message = "促销范围里渠道分类不能为空")
    private List<Integer> platformSelected;

    @ApiModelProperty(value = "活动类型（1-满赠,2-特价,3-秒杀）")
    @NotNull(message = "活动类型不能为空")
    private Integer       type;

    @ApiModelProperty(value = "生效类型 1-立即生效，2-固定生效时间")
    private Integer       effectType;

    @ApiModelProperty(value = "立即生效-持续时间（h）")
    private Integer       lastTime;

    @ApiModelProperty(value = "促销预算金额")
    @NotNull(message = "促销预算金额不能为空")
    private BigDecimal    budgetAmount;

    @ApiModelProperty(value = "费用承担方（1-平台；2-商家;3-分摊）")
    @NotNull(message = "费用承担方不能为空")
    private Integer       bear;

    @ApiModelProperty(value = "分摊-平台百分比")
    private BigDecimal    platformPercent;

    @ApiModelProperty(value = "分摊-商户百分比")
    private BigDecimal    merchantPercent;

    @ApiModelProperty(value = "促销编码")
    private String        promotionCode;

    @ApiModelProperty(value = "商家类型 1-以岭，2-非以岭")
    private Integer       merchantType;

    @ApiModelProperty(value = "备注")
    private String       remark;
}
