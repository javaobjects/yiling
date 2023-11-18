package com.yiling.admin.system.system.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 字典类型列表 Form
 *
 * @author lun.yu
 * @date 2021-08-31
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel
public class QueryDictTypeForm extends QueryPageListForm {

    /**
     * 字典名称/描述
     */
    @ApiModelProperty(value = "字典名称/描述")
    private String name;
}
