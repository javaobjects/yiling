package com.yiling.admin.data.center.enterprise.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 修改账号姓名 Form
 *
 * @author: xuan.zhou
 * @date: 2021/6/11
 */
@Data
public class UpdateStaffNameForm {

    /**
     * 用户ID
     */
    @NotNull
    @ApiModelProperty(value = "用户ID", required = true)
    private Long id;

    /**
     * 姓名
     */
    @NotEmpty(message = "^姓名不能为空")
    @Length(max = 10)
    @ApiModelProperty(value = "姓名", required = true)
    private String name;
}
