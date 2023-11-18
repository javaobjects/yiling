package com.yiling.hmc.meeting.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * 会议签到 form
 *
 * @author: fan.shen
 * @date: 2024/3/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SubmitSignInfoForm extends BaseForm {

    private Long id;

    @ApiModelProperty(value = "客户姓名")
    @NotBlank
    private String customerName;

    @ApiModelProperty("电话")
    private String mobile;

    @ApiModelProperty("工作单位")
    private String hospitalName;

    @ApiModelProperty("机构编码")
    private String sysCode;

    @ApiModelProperty("科室")
    private String departmentName;

    @ApiModelProperty("职务")
    private String jobTitle;

    @ApiModelProperty("会场来源id 1、2、3...")
    private Integer meetingSourceId;


}