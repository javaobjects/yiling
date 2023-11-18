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
 * 提交测评结果
 *
 * @author: fan.shen
 * @date: 2022-12-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SubmitEvaluateResultForm extends BaseForm {

    private static final long serialVersionUID = -333710304281212221L;

    /**
     * cms_health_evaluate主键
     */
    @ApiModelProperty(value = "cms_health_evaluate主键")
    private Long healthEvaluateId;

    /**
     * 开始测评id
     */
    @ApiModelProperty(value = "开始测评id")
    @NotNull
    private Long startEvaluateId;

    /**
     * 测评答案
     */
    @ApiModelProperty(value = "测评答案")
    @NotNull
    private List<EvaluateResultDetailForm> evaluateResultDetailList;

}
