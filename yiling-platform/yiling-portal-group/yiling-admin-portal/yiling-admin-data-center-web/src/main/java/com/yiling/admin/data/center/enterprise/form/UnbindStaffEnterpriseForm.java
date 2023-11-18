package com.yiling.admin.data.center.enterprise.form;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 解除账号与企业的绑定关系 Form
 *
 * @author: xuan.zhou
 * @date: 2021/6/11
 */
@Data
public class UnbindStaffEnterpriseForm {

    /**
     * 用户ID
     */
    @NotNull
    @ApiModelProperty(value = "用户ID", required = true)
    private Long userId;

    /**
     * 企业ID
     */
    @NotNull
    @ApiModelProperty(value = "企业ID", required = true)
    private Long eid;
}
