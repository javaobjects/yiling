package com.yiling.hmc.activity.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author: fan.shen
 * @date: 2022-09-07
 */
@Data
public class SavePatientDoctorRelForm {

    @ApiModelProperty(value = "患者id")
    @NotNull
    private Integer patientId;

    @NotNull
    @ApiModelProperty(value = "医生id")
    private Integer doctorId;
//
//    @ApiModelProperty(value = "标签:所患疾病，所用药品")
//    private List<PatientTagForm> patientTagForms;


    @ApiModelProperty(value = "用户id")
    @NotNull
    private Integer userId;

    @ApiModelProperty("描述")
    private String desc;

    @ApiModelProperty("就诊日期")
    private Date visitDate;


    @ApiModelProperty(value = "活动id")
    @NotNull
    private Long activityId;

    @ApiModelProperty(hidden = true)
    private Integer createUser;
}