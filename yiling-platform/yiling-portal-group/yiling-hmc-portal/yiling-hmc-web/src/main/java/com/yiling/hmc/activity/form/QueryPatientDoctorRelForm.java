package com.yiling.hmc.activity.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * 查询是否入组 form
 * @author: fan.shen
 * @date: 2022/9/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryPatientDoctorRelForm extends BaseForm {

    @ApiModelProperty(value = "活动id")
    @NotNull
    private Long activityId;

    @ApiModelProperty(value = "医生id")
    @NotNull
    private Integer doctorId;

    @ApiModelProperty(value = "用户id")
    @NotNull
    private Integer userId;

}