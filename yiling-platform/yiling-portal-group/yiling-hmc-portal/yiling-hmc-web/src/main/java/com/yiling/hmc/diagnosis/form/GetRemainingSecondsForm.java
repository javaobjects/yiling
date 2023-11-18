package com.yiling.hmc.diagnosis.form;

import javax.validation.constraints.NotBlank;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2023/6/7 0007
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GetRemainingSecondsForm extends BaseForm {

    @ApiModelProperty("房间号")
    @NotBlank(message = "房间号不能为空")
    private String roomId;
}
