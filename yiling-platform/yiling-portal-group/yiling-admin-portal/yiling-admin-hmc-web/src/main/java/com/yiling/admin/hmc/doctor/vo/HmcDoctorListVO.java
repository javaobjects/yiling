package com.yiling.admin.hmc.doctor.vo;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @data: 2022/09/06
 */
@Data
@Accessors(chain = true)
public class HmcDoctorListVO {

    @ApiModelProperty(value = "医生id")
    private Integer doctorId;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "第一执业医院")
    private String hospitalName;

    @ApiModelProperty(value = "来源：数据来源 0用户自主完善 1销售助手APP 2运营人员导入或创建")
    private Integer source;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "医生是否已参加活动 1：是 2：否")
    private Integer activityState;
}
