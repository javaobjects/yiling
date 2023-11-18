package com.yiling.admin.cms.content.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 更新QA显示状态
 * </p>
 *
 * @author fan.shen
 * @date 2022-03-24
 */
@Data
@Accessors(chain = true)
public class SwitchQaStatusForm {

    @NotNull
    @ApiModelProperty(value = "id")
    private Long id;

    @NotNull
    @ApiModelProperty(value = "展示状态 1-展示，2-关闭")
    private Integer showStatus;
}
