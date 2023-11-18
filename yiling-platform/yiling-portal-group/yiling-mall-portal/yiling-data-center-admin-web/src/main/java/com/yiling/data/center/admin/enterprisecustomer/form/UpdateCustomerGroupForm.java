package com.yiling.data.center.admin.enterprisecustomer.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 修改客户分组 Form
 *
 * @author: xuan.zhou
 * @date: 2021/5/24
 */
@Data
public class UpdateCustomerGroupForm {

    /**
     * 客户分组ID
     */
    @NotNull
    @ApiModelProperty(value = "分组ID", required = true)
    private Long id;

    /**
     * 分组名称
     */
    @NotEmpty
    @Length(max = 50)
    @ApiModelProperty(value = "分组名称", required = true)
    private String name;
}
