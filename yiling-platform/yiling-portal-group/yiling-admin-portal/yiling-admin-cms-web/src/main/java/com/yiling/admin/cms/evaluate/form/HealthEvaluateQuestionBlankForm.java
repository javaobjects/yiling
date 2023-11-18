package com.yiling.admin.cms.evaluate.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * 添加健康测评题目 填空题
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class HealthEvaluateQuestionBlankForm extends BaseForm {

    /**
     * 填空类型 1-文本，2-数字，3-数字型（区间），4-日期，5-上传图片
     */
    @ApiModelProperty(value = "填空类型 1-文本，2-数字，3-数字型（区间），4-日期，5-上传图片")
    private Integer blankType;

    /**
     * 是否可输入小数 0-否，1-是
     */
    @ApiModelProperty(value = "是否可输入小数 0-否，1-是")
    private Integer ifDecimal;

    /**
     * 是否有单位 0-否，1-是
     */
    @ApiModelProperty(value = "是否有单位 0-否，1-是")
    private Integer ifUnit;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位")
    private String unit;
}
