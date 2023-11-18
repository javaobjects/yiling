package com.yiling.admin.hmc.activity.form;

import java.util.List;

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
public class SaveActivityDoctorForm {

    @NotNull
    @ApiModelProperty(value = "活动id")
    private Integer activityId;

    @NotNull
    @ApiModelProperty(value = "医生活动码集合")
    private List<HmcActivityDoctorQrcodeUrlForm> hmcActivityDoctorQrcodeUrlFormList;


}
