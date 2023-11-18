package com.yiling.hmc.activity.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * 查看用户是否和医生绑定-医带患
 * @author: fan.shen
 * @date: 2022/9/5
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CheckDocPatientRelForm extends BaseForm {

    @ApiModelProperty(value = "医生id")
    @NotNull
    private Integer doctorId;

}