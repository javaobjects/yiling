package com.yiling.admin.data.center.report.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2022-05-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryEntInfoPageListForm extends QueryPageListForm {

    /**
     * 企业名称（全模糊查询）
     */
    @ApiModelProperty(value = "企业名称（全模糊查询）")
    private String name;
}
