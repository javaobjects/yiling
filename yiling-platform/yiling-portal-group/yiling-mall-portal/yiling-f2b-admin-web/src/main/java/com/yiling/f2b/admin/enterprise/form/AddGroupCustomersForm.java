package com.yiling.f2b.admin.enterprise.form;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 向分组中添加客户 Form
 *
 * @author: xuan.zhou
 * @date: 2021/5/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddGroupCustomersForm extends BaseForm {

    @NotNull
    @ApiModelProperty(value = "分组ID", required = true)
    private Long groupId;

    @NotEmpty
    @ApiModelProperty(value = "客户ID集合", required = true)
    private List<Long> customerEids;
}
