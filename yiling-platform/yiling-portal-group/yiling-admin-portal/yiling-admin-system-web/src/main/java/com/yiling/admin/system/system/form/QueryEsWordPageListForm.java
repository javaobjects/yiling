package com.yiling.admin.system.system.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 QueryEsWordPageListForm
 * @描述
 * @创建时间 2022/4/26
 * @修改人 shichen
 * @修改时间 2022/4/26
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class QueryEsWordPageListForm extends QueryPageListForm {

    @ApiModelProperty(value = "类型：1-扩展词，2-停止词，3-单向同义词，4-双向同义词")
    private Integer type;

    @ApiModelProperty(value = "词语")
    private String word;
}
