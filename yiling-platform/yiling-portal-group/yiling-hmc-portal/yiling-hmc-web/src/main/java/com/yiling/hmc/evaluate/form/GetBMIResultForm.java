package com.yiling.hmc.evaluate.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 获取BMI计算结果
 *
 * @author: fan.shen
 * @date: 2022-12-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GetBMIResultForm extends BaseForm {

    private static final long serialVersionUID = -333710304281212221L;

    /**
     * cms_health_evaluate主键
     */
    @ApiModelProperty(value = "cms_health_evaluate主键")
    @NotNull
    private Long healthEvaluateId;

    /**
     * cms_health_evaluate_question主键
     */
    @ApiModelProperty(value = "cms_health_evaluate_question主键")
    @NotNull
    private Long questionId;

    /**
     * 身高
     */
    @ApiModelProperty(value = "身高")
    @NotNull
    private BigDecimal height;

    /**
     * 体重
     */
    @ApiModelProperty(value = "体重")
    @NotNull
    private BigDecimal weight;

}
