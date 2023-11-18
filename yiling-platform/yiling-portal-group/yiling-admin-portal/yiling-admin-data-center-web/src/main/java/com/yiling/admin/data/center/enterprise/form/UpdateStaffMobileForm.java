package com.yiling.admin.data.center.enterprise.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.yiling.framework.common.util.Constants;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 修改账号手机号 Form
 *
 * @author: xuan.zhou
 * @date: 2021/8/17
 */
@Data
public class UpdateStaffMobileForm {

    /**
     * 用户ID
     */
    @NotNull
    @ApiModelProperty(value = "用户ID", required = true)
    private Long id;

    /**
     * 手机号
     */
    @NotEmpty(message = "^请填写手机号")
    @Pattern(regexp = Constants.REGEXP_MOBILE, message = "^手机号格式不正确，请重新填写")
    @ApiModelProperty(value = "手机号", required = true)
    private String mobile;
}
