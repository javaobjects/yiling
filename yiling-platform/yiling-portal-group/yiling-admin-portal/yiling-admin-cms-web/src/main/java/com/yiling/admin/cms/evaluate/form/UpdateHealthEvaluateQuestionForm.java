package com.yiling.admin.cms.evaluate.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 添加健康测评题目
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateHealthEvaluateQuestionForm extends BaseForm {

    /**
     * 测评id
     */
    @ApiModelProperty(value = "测评id")
    private Long healthEvaluateId;

    /**
     * 题目
     */
    @ApiModelProperty(value = "题目")
    private HealthEvaluateQuestionDetailForm question;

}
