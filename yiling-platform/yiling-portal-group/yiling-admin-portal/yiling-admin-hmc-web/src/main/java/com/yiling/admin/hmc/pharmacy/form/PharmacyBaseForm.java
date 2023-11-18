package com.yiling.admin.hmc.pharmacy.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * 终端药店 form
 *
 * @author: fan.shen
 * @date: 2024/5/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PharmacyBaseForm extends BaseForm {

    @NotNull
    @ApiModelProperty(value = "id")
    private Long id;

}