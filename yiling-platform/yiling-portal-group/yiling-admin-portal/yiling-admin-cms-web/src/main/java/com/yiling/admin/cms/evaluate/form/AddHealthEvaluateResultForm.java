package com.yiling.admin.cms.evaluate.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 健康测评结果
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddHealthEvaluateResultForm extends BaseForm {

    /**
     * 是否有结果 0-否，1-是
     */
    @ApiModelProperty(value = "是否有结果 0-否，1-是")
    private Integer ifResult;

    /**
     * 健康测评结果 list
     */
    @ApiModelProperty(value = "健康测评结果 list")
    private List<HealthEvaluateResultDetailForm> HealthEvaluateResultList;
}
