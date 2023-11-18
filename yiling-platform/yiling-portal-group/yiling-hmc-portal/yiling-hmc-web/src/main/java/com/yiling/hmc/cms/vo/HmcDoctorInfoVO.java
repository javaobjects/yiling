package com.yiling.hmc.cms.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: fan.shen
 * @data: 2022-10-24
 */
@Data
public class HmcDoctorInfoVO {

    @ApiModelProperty(value = "医生id")
    private Integer id;

    @ApiModelProperty(value = "医生名称")
    private String doctorName;

    @ApiModelProperty(value = "职称")
    private String profession;

    @ApiModelProperty(value = "所在医疗机构")
    private String hospitalName;

    @ApiModelProperty(value = "所在医疗机构科室")
    private String hospitalDepartment;

    @ApiModelProperty(value = "医生头像")
    private String picture;

    @ApiModelProperty(value = "科室id")
    private Integer departmentId;
}
