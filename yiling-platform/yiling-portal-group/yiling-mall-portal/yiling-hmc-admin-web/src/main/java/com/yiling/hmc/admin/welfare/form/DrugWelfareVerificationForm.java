package com.yiling.hmc.admin.welfare.form;

import javax.validation.constraints.NotBlank;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: benben.jia
 * @data: 2022/10/08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DrugWelfareVerificationForm  extends BaseForm {

    @NotBlank(message = "福利券码不能为空")
    @ApiModelProperty("福利券码")
    private String couponCode;

}
