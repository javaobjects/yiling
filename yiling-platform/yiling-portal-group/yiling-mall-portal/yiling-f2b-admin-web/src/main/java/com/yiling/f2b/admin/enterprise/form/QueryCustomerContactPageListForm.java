package com.yiling.f2b.admin.enterprise.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询客户商务联系人分页列表 Form
 *
 * @author: xuan.zhou
 * @date: 2021/6/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCustomerContactPageListForm extends QueryPageListForm {

    /**
     * 客户ID
     */
    @NotNull
    @ApiModelProperty(value = "客户ID", required = true)
    private Long customerEid;
}
