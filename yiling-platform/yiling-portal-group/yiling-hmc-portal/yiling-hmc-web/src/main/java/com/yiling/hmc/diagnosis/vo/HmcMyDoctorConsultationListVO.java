package com.yiling.hmc.diagnosis.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 我咨询过的医生列表
 *
 * @author: fan.shen
 * @data: 2023/05/10
 */
@Data
@Accessors(chain = true)
public class HmcMyDoctorConsultationListVO {

    @ApiModelProperty(value = "医生id")
    private Integer doctorId;

    @ApiModelProperty(value = "头像")
    private String picture;

    @ApiModelProperty(value = "医生姓名")
    private String name;

    @ApiModelProperty(value = "职称")
    private String professionName;

    @ApiModelProperty(value = "科室名称")
    private String departmentName;

    @ApiModelProperty(value = "医院名称")
    private String hospitalName;

}
