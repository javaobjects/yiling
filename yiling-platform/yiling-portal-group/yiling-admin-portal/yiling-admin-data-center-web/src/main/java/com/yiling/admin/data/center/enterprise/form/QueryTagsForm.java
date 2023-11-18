package com.yiling.admin.data.center.enterprise.form;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 查询标签 Form
 *
 * @author: lun.yu
 * @date: 2021/10/27
 */
@Data
public class QueryTagsForm extends QueryPageListForm {

    @Length(max = 10)
    @ApiModelProperty("名称")
    private String name;

}
