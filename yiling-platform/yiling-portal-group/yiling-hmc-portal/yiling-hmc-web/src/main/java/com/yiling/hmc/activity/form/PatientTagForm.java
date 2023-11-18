package com.yiling.hmc.activity.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;


/**
 * 患者标签 Form
 *
 * @author: fan.shen
 * @date: 2022-09-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PatientTagForm extends BaseForm {

    @NotNull
    @ApiModelProperty(value = "标签 ，号分隔")
    private String tag;

    @NotNull
    @ApiModelProperty(value = "标签类型 选药品时传0 疾病传1 门诊/疾病 2")
    private Integer tagType;
}
