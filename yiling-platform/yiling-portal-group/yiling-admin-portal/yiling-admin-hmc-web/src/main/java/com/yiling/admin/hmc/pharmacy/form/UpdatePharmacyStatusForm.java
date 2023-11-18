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
public class UpdatePharmacyStatusForm extends PharmacyBaseForm {

    @NotNull
    @ApiModelProperty(value = "状态：1-启用，2-停用")
    private Integer status;

}