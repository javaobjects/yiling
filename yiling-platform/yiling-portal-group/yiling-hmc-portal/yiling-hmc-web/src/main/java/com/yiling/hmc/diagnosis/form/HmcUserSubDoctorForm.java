package com.yiling.hmc.diagnosis.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * 关注医生
 * @author: fan.shen
 * @data: 2023/05/09
 */
@Data
@Accessors(chain = true)
public class HmcUserSubDoctorForm {

    @NotBlank(message = "不能为空")
    @ApiModelProperty(value = "医生id")
    private Integer doctorId;

}
