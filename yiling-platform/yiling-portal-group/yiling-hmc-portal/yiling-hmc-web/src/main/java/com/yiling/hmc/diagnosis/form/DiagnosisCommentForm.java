package com.yiling.hmc.diagnosis.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 诊后评价 form
 *
 * @author: fan.shen
 * @date: 2024/5/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DiagnosisCommentForm extends BaseForm {

    @NotNull(message = "不能为空")
    @ApiModelProperty(value = "问诊单id")
    private Integer diagnosisRecordId;

    @NotNull(message = "不能为空")
    @ApiModelProperty(value = "整体满意度_星级")
    private Integer starLevel;

    @NotBlank(message = "不能为空")
    @ApiModelProperty(value = "整体满意度_星级满意说明")
    private String starLevelExplan;

    @ApiModelProperty(value = "用户评价描述")
    private String userDescribe;

    @ApiModelProperty(value = "选中标签项")
    private String selectLabelItem;


}