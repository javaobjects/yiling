package com.yiling.hmc.diagnosis.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * 已读上报 form
 *
 * @author: fan.shen
 * @date: 2024/5/8
 */
@Data
@Accessors(chain = true)
public class ReportReadForm {

    @NotBlank
    @ApiModelProperty("群组id")
    private String groupId;


}