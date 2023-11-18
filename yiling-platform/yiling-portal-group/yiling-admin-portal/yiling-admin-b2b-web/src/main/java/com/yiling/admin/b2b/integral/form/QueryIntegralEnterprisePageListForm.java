package com.yiling.admin.b2b.integral.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询企业分页列表 Form
 *
 * @author: lun.yu
 * @date: 2023-02-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryIntegralEnterprisePageListForm extends QueryPageListForm {

    /**
     * 企业ID
     */
    @ApiModelProperty("企业ID")
    private Long id;

    /**
     * 企业名称
     */
    @ApiModelProperty("企业名称")
    private String name;

}