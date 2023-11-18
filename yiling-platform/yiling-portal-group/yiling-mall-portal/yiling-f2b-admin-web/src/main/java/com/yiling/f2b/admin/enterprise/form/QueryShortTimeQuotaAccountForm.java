package com.yiling.f2b.admin.enterprise.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 账户临时额度 Form
 *
 * @author: tingwei.chen
 * @date: 2021/7/5
 */
@Data
public class QueryShortTimeQuotaAccountForm extends QueryPageListForm {

    /**
     * 采购商名称
     */
    @NotNull
    @ApiModelProperty(value = "采购商名称", required = true)
    private String customerName;
    /**
     * 审核
     */
    @NotNull
    @ApiModelProperty(value = "状态", required = true)
    private int status;


}
