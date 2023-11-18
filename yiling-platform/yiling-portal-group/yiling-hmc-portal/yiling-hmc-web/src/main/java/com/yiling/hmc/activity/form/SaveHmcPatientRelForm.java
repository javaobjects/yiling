package com.yiling.hmc.activity.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 添加就诊人 form
 * @author: fan.shen
 * @date: 2022/9/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveHmcPatientRelForm extends BaseForm {

    @NotBlank
    @ApiModelProperty(value = "患者名称", required = true)
    private String patientName;

    @NotNull
    @ApiModelProperty(value = "用户id", required = true)
    private Integer fromUserId;

    @ApiModelProperty(value = "身份证", required = true)
    private String idCard;

    @NotBlank
    @ApiModelProperty(value = "手机号", required = true)
    private String mobile;

    @ApiModelProperty(value = "省code", required = true)
    private String provinceCode;

    @ApiModelProperty(value = "市code", required = true)
    private String cityCode;

    @ApiModelProperty(value = "区code", required = true)
    private String regionCode;

    @ApiModelProperty(value = "详细地址")
    private String address;

    @ApiModelProperty(value = "地址类型代码")
    private String addressTypeCode;

    @ApiModelProperty(value = "民族")
    private String nation;

    @ApiModelProperty(value = "民族代码")
    private String nationCode;

    @ApiModelProperty(value = "家庭关系,传code码（例：本人传01）")
    private String relation;

    @NotNull
    @ApiModelProperty(value = "医生id")
    private Integer doctorId;

    @ApiModelProperty(value = "标签:所患疾病，所用药品")
    private List<PatientTagForm> patientTagForms;

    @ApiModelProperty("描述")
    private String desc;

    @ApiModelProperty("就诊日期")
    private Date visitDate;

    @ApiModelProperty(value = "活动id")
    @NotNull
    private Long activityId;

    @ApiModelProperty(value = "实名认证标志 1-是，0-否")
    private Integer realNameFlag;

}