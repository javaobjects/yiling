package com.yiling.hmc.meeting.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 获取会议签到信息 form
 *
 * @author: fan.shen
 * @date: 2024/3/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GetMeetingSignInfoForm extends BaseForm {

    @ApiModelProperty(value = "客户姓名")
    @NotBlank
    private String customerName;

}