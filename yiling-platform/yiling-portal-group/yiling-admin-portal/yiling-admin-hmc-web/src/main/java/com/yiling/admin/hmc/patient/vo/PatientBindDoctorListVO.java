package com.yiling.admin.hmc.patient.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 患者绑定医生对象
 * @author: fan.shen
 * @date: 2023/02/03
 */
@Data
@Accessors(chain = true)
public class PatientBindDoctorListVO {

    /**
     * 医生ID
     */
    @ApiModelProperty(value = "医生ID")
    private Integer doctorId;

    /**
     * 医生姓名
     */
    @ApiModelProperty(value = "医生姓名")
    private String doctorName;

    /**
     * 第一执业医院
     */
    @ApiModelProperty(value = "第一执业医院")
    private String hospitalName;

    /**
     * 绑定来源
     */
    @ApiModelProperty(value = "绑定来源")
    private String sourceType;

    /**
     * 绑定业务线
     */
    @ApiModelProperty(value = "绑定业务线")
    private String businessType;

    /**
     * 绑定时间
     */
    @ApiModelProperty(value = "绑定时间")
    private Date createTime;

}