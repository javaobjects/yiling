package com.yiling.hmc.diagnosis.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 我的就诊人详情
 *
 * @author: fan.shen
 * @date: 2023-12-27
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "我的就诊人详情")
public class MyPatientDetailVO {

    private static final long serialVersionUID = -333710312121608L;

    @ApiModelProperty("ID")
    private Integer id;

    /**
     * 患者名称
     */
    @ApiModelProperty(value = "患者名称")
    private String patientName;


    /**
     * 身份证号码
     */
    @ApiModelProperty(value = "身份证")
    private String idCard;

    /**
     * 患者年龄
     */
    @ApiModelProperty(value = "患者年龄")
    private Integer patientAge;

    /**
     * 患者性别
     */
    @ApiModelProperty(value = "性别1 : 男  0 : 女")
    private Integer gender;

    /**
     * 省code
     */
    @ApiModelProperty(value = "省code")
    private String provinceCode;

    /**
     * 市code
     */
    @ApiModelProperty(value = "市code")
    private String cityCode;

    /**
     * 区code
     */
    @ApiModelProperty(value = "区code")
    private String regionCode;

    @ApiModelProperty(value = "省Name", required = true)
    private String provinceName;

    @ApiModelProperty(value = "市Name", required = true)
    private String cityName;

    @ApiModelProperty(value = "区Name", required = true)
    private String regionName;

    /**
     * 详细地址
     */
    @ApiModelProperty(value = "详细地址")
    private String address;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "地址类型代码")
    private String addressTypeCode;

    @ApiModelProperty(value = "家庭关系")
    private String relation;

    @ApiModelProperty(value = "民族")
    private String nation;

    @ApiModelProperty(value = "民族代码")
    private String nationCode;

    @ApiModelProperty(value = "过往病史")
    private List<String> historyDisease;

    @ApiModelProperty(value = "家族病史")
    private List<String> familyDisease;

    @ApiModelProperty(value = "过敏史")
    private List<String> allergyHistory;

    @ApiModelProperty(value = "实名认证标志 1-是，0-否")
    private Integer realNameFlag;

    @ApiModelProperty(value = "是否完善信息 1：已完善 2：未完善")
    private Integer completeInfo;

}
