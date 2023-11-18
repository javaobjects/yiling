package com.yiling.admin.hmc.activity.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2022/09/07
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class QueryActivityDoctorPageForm extends QueryPageListForm {

    @ApiModelProperty(value = "医生姓名")
    private String name;

    @ApiModelProperty(value = "第一职业医院id")
    private Integer hospitalId;

    @ApiModelProperty(value = "第一职业机构")
    private String hospitalName;

    @ApiModelProperty(value = "医生id")
    private Integer doctorId;

    @NotNull
    @ApiModelProperty(value = "活动id")
    private Integer activityId;
}
