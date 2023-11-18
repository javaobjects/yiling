package com.yiling.hmc.activity.form;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.util.Constants;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 获取医生信息 Form
 *
 * @author: fan.shen
 * @date: 2022-09-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GetDoctorInfoForm extends BaseForm {

    /**
     * 医生id
     */
    @NotNull
    @ApiModelProperty(value = "医生id", required = true)
    private Integer doctorId;

}
