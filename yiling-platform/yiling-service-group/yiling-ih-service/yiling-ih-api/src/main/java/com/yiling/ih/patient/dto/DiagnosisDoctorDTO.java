package com.yiling.ih.patient.dto;

import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 问诊医生
 */
@Data
@Accessors(chain = true)
public class DiagnosisDoctorDTO implements java.io.Serializable {

    private static final long serialVersionUID = 3768586786173659462L;

    /**
     * 医生id
     */
    private Integer doctorId;

    /**
     * 头像
     */
    private String picture;

    /**
     * 医生姓名
     */
    private String name;

    /**
     * 职称
     */
    private String professionName;

    /**
     * 科室名称
     */
    private String departmentName;

    /**
     * 可处方 0不显示 1显示
     */
    private Integer prescriptionStatus;

    /**
     * 医院名称
     */
    private String hospitalName;

    /**
     * 医院级别
     */
    private String hospitalLevel;

    /**
     * 医院性质
     */
    private String hospitalType;

    /**
     * 擅长
     */
    private String skilled;

    /**
     * 接诊量
     */
    private Integer diagnosticCount;

    /**
     * 好评率
     */
    private String evaluateRate;

    /**
     * 图文问诊状态0关闭 1开启
     */
    private Integer imagetextStatus;

    /**
     * 音频问诊状态0关闭 1开启
     */
    private Integer voiceStatus;

    /**
     * 视频问诊状态0关闭 1开启
     */
    private Integer videoStatus;

    /**
     * 图文问诊价格  单位分
     */
    private Integer imagetextPrice;

    /**
     * 视频问诊价格  单位分
     */
    private Integer videoPrice;

    /**
     * 音频问诊价格  单位分
     */
    private Integer voicePrice;

    // @ApiModelProperty(value = "医院级别type 1:一级甲等 2:一级乙等 3:一级丙等 4:二级甲等 5:二级乙等 6:二级丙等 7:三级甲等 8:三级乙等 9:三级丙等 ")
    private Integer hospitalLevelType;

    // @ApiModelProperty(value = "医院性质type  1:儿童 2:妇产 3:中西医结合 4:中医 5:专科 6:综合")
    private Integer hospitalNatureType;

}