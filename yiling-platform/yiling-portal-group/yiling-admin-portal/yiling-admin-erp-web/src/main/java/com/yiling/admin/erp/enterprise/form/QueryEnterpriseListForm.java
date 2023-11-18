package com.yiling.admin.erp.enterprise.form;

import javax.validation.constraints.NotBlank;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/1/14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryEnterpriseListForm  extends BaseForm {

    /**
     * 企业名称（模糊查询）
     */
    @NotBlank
    @ApiModelProperty(value = "企业名称（模糊查询）", example = "1")
    private String name;

}
