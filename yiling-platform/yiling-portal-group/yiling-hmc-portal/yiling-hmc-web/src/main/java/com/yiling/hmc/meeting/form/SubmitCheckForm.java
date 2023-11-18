package com.yiling.hmc.meeting.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * 会议核销 form
 *
 * @author: fan.shen
 * @date: 2024/3/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SubmitCheckForm extends BaseForm {

    private Long id;

    @ApiModelProperty(value = "客户姓名")
    private String customerName;

    @ApiModelProperty("工作单位")
    private String hospitalName;

}