package com.yiling.hmc.diagnosis.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 医生详情
 *
 * @author: fan.shen
 * @data: 2023/05/05
 */
@Data
@Accessors(chain = true)
public class HmcDiagnosisDoctorDetailVO {

    @ApiModelProperty(value = "医生id")
    private Integer doctorId;

    @ApiModelProperty(value = "头像")
    private String picture;

    @ApiModelProperty(value = "医生姓名")
    private String name;

    @ApiModelProperty(value = "执业证书编号")
    private String doctorsNoteCertNo;

    @ApiModelProperty(value = "医师资格证书编号")
    private String certificateCertNo;

    @ApiModelProperty(value = "科室名称")
    private String departmentName;

    @ApiModelProperty(value = "职称")
    private String professionName;

    @ApiModelProperty(value = "医院名称")
    private String hospitalName;

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

    @ApiModelProperty(value = "擅长")
    private String skilled;

    @ApiModelProperty(value = "从业经历")
    private String occupationalHistory;


    @ApiModelProperty(value = "是否关注 0未关注 1已关注")
    private Integer followStatus;

    @ApiModelProperty(value = "医院级别")
    private String hospitalLevel;

    @ApiModelProperty(value = "医院性质")
    private String hospitalType;

    @ApiModelProperty(value = "音频接诊时长")
    private Integer voiceMinutes;

    @ApiModelProperty(value = "视频接诊时长")
    private Integer videoMinutes;

    @ApiModelProperty(value = "医院级别type 1:一级甲等 2:一级乙等 3:一级丙等 4:二级甲等 5:二级乙等 6:二级丙等 7:三级甲等 8:三级乙等 9:三级丙等 ")
    private Integer hospitalLevelType;

    @ApiModelProperty(value = "医院性质type  1:儿童 2:妇产 3:中西医结合 4:中医 5:专科 6:综合")
    private Integer hospitalNatureType;

}
