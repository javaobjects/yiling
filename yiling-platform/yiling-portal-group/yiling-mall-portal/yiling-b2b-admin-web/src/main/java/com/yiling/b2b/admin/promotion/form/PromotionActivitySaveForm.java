package com.yiling.b2b.admin.promotion.form;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/11/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PromotionActivitySaveForm extends BaseForm {

    @ApiModelProperty(value = "促销名称")
    @NotNull(message = "促销名称不能为空")
    private String name;

    @ApiModelProperty(value = "开始时间")
    @NotNull(message = "开始时间不能为空")
    private Date beginTime;

    @ApiModelProperty(value = "结束时间")
    @NotNull(message = "结束时间不能为空")
    private Date endTime;

    @ApiModelProperty(value = "活动类型（1-满赠）")
    @NotNull(message = "活动类型不能为空")
    private Integer type;

    @ApiModelProperty(value = "促销预算金额")
    @NotNull(message = "促销预算金额不能为空")
    private BigDecimal budgetAmount;

    @ApiModelProperty(value = "满赠金额")
    @NotNull(message = "满赠金额不能为空")
    private BigDecimal promotionAmount;

    @ApiModelProperty(value = "促销范围-选择平台（1-B2B；2-销售助手）")
    @NotEmpty(message = "促销范围里渠道分类不能为空")
    private List<Integer> platformSelected;

    @ApiModelProperty(value = "备注")
    private String remark;
}
