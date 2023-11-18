package com.yiling.data.center.admin.enterprisesupplier.form;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 企业分页列表查询 Form
 * </p>
 *
 * @author lun.yu
 * @date 2023-06-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryEnterpriseListPageForm extends QueryPageListForm {

    /**
     * 企业名称
     */
    @NotEmpty
    @ApiModelProperty(value = "企业名称", required = true)
    private String name;


}
