package com.yiling.f2b.admin.enterprise.form;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 移除客户商务联系人 Form
 *
 * @author: xuan.zhou
 * @date: 2021/6/4
 */
@Data
public class RemoveCustomerContactForm {

    /**
     * 客户ID
     */
    @NotNull
    @ApiModelProperty(value = "客户ID", required = true)
    private Long customerEid;

    /**
     * 商务联系人ID
     */
    @NotNull
    @ApiModelProperty(value = "商务联系人ID", required = true)
    private Long contactUserId;
}
