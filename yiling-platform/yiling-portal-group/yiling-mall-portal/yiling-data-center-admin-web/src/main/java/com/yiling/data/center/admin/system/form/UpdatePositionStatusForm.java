package com.yiling.data.center.admin.system.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 修改职位状态 Form
 *
 * @author: xuan.zhou
 * @date: 2021/11/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdatePositionStatusForm extends BaseForm {

    @NotNull
    @ApiModelProperty(value = "职位ID", required = true)
    private Long id;

    @NotNull
    @ApiModelProperty(value = "状态：1-启用 2-停用", required = true)
    private Integer status;
}
