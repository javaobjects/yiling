package com.yiling.hmc.diagnosis.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 问诊医生
 *
 * @author: fan.shen
 * @date: 2023-12-27
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "首页问诊医生")
public class DiagnosisDoctorVO {

    private static final long serialVersionUID = -333710312121608L;

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

    @ApiModelProperty(value = "可处方 0不显示 1显示")
    private Integer prescriptionStatus;

    @ApiModelProperty(value = "医院名称")
    private String hospitalName;

    @ApiModelProperty(value = "医院级别")
    private String hospitalLevel;

    @ApiModelProperty(value = "医院性质")
    private String hospitalType;

    @ApiModelProperty(value = "擅长")
    private String skilled;

    @ApiModelProperty(value = "接诊量")
    private Integer diagnosticCount;

    @ApiModelProperty(value = "好评率")
    private String evaluateRate;

    @ApiModelProperty(value = "图文问诊状态0关闭 1开启")
    private Integer imagetextStatus;

    @ApiModelProperty(value = "音频问诊状态0关闭 1开启")
    private Integer voiceStatus;

    @ApiModelProperty(value = "视频问诊状态0关闭 1开启")
    private Integer videoStatus;

    @ApiModelProperty(value = "图文问诊价格  单位分")
    private Integer imagetextPrice;

    @ApiModelProperty(value = "视频问诊价格  单位分")
    private Integer videoPrice;

    @ApiModelProperty(value = "音频问诊价格  单位分")
    private Integer voicePrice;

    @ApiModelProperty(value = "医院级别type 1:一级甲等 2:一级乙等 3:一级丙等 4:二级甲等 5:二级乙等 6:二级丙等 7:三级甲等 8:三级乙等 9:三级丙等 ")
    private Integer hospitalLevelType;

    @ApiModelProperty(value = "医院性质type  1:儿童 2:妇产 3:中西医结合 4:中医 5:专科 6:综合")
    private Integer hospitalNatureType;

}
