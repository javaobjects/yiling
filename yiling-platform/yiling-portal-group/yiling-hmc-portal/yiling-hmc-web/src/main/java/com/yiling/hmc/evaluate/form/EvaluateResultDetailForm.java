package com.yiling.hmc.evaluate.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * 测评结果明细
 *
 * @author: fan.shen
 * @date: 2022-12-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EvaluateResultDetailForm extends BaseForm {

    private static final long serialVersionUID = -333710304281212221L;

    /**
     * 测评题目id
     */
    @ApiModelProperty(value = "测评题目id")
    private Long healthEvaluateQuestionId;

    /**
     * 单/多选 id 列表
     */
    @ApiModelProperty(value = "单/多选 id 列表")
    private List<Long> selectIdList;

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
