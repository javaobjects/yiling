package com.yiling.admin.erp.enterprise.form;

import javax.validation.constraints.NotBlank;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2023/3/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCrmEnterpriseListForm extends BaseForm {

    /**
     * crm企业名称（模糊查询）
     */
    @ApiModelProperty(value = "crm企业名称（模糊查询）", example = "")
    private String name;
}
