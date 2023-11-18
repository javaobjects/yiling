package com.yiling.admin.data.center.enterprise.form;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 更新企业状态 Form
 *
 * @author: xuan.zhou
 * @date: 2021/5/26
 */
@Data
public class UpdateEnterpriseStatusForm {

    @NotNull
    @ApiModelProperty(value = "企业ID", required = true)
    private Long id;

    @NotNull
    @ApiModelProperty(value = "状态：1-启用 2-停用", required = true)
    private Integer status;
}
