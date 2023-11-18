package com.yiling.admin.hmc.activity.form;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2022/09/07
 */
@Data
@Accessors(chain = true)
public class DeleteActivityDoctorForm {

    @NotNull
    @ApiModelProperty(value = "活动id")
    private Integer activityId;

    @NotNull
    @ApiModelProperty(value = "医生id")
    private Integer doctorId;

    @ApiModelProperty(value = "医生资格 1恢复 2取消")
    private Integer doctorStatus;
}
