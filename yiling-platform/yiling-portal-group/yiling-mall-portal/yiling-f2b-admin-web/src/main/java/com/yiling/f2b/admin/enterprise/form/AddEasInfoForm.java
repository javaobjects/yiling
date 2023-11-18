package com.yiling.f2b.admin.enterprise.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 添加EAS信息 Form
 *
 * @author: xuan.zhou
 * @date: 2021/7/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddEasInfoForm extends BaseForm {

    @NotNull
    @ApiModelProperty(value = "客户ID", required = true)
    private Long customerEid;

    @NotEmpty
    @ApiModelProperty(value = "EAS客户名称", required = true)
    private String easName;

    @NotEmpty
    @ApiModelProperty(value = "EAS编码", required = true)
    private String easCode;
}
