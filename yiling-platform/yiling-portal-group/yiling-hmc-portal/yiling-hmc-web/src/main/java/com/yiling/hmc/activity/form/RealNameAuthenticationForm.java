package com.yiling.hmc.activity.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * 获取医生信息 Form
 *
 * @author: fan.shen
 * @date: 2022-09-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RealNameAuthenticationForm extends BaseForm {

    /**
     * 身份证号码
     */
    @NotNull
    @ApiModelProperty(value = "身份证号码", required = true)
    private String idcard;

    /**
     * 姓名
     */
    @NotNull
    @ApiModelProperty(value = "姓名", required = true)
    private String name;

}
