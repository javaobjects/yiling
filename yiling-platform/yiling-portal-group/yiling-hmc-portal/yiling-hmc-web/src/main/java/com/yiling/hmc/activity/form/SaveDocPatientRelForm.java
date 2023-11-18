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
 * 保存医带患 form
 * @author: fan.shen
 * @date: 2022/9/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveDocPatientRelForm extends BaseForm {

    @ApiModelProperty(value = "身份证", required = true)
    private String idCard;

    @NotBlank
    @ApiModelProperty(value = "患者名称", required = true)
    private String patientName;

    @NotBlank
    @ApiModelProperty(value = "手机号", required = true)
    private String mobile;

    @ApiModelProperty(value = "性别1 : 男 ， 0 : 女")
    private Integer gender;

    @ApiModelProperty(value = "出生日期 19511120")
    private String birthday;

    @NotNull
    @ApiModelProperty(value = "医生id")
    private Integer doctorId;

    @ApiModelProperty(value = "标签:所患疾病，所用药品")
    private List<PatientTagForm> patientTagForms;

    @ApiModelProperty(value = "活动id")
    private Long activityId;

    @ApiModelProperty(value = "实名认证标志 1-是，0-否")
    private Integer realNameFlag;

}