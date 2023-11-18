package com.yiling.b2b.admin.enterprisecustomer.form;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 添加客户分组 Form
 *
 * @author: xuan.zhou
 * @date: 2021/5/24
 */
@Data
public class AddCustomerGroupForm {

    /**
     * 分组名称
     */
    @NotEmpty
    @Length(max = 50)
    @ApiModelProperty(value = "分组名称", required = true)
    private String name;
}
