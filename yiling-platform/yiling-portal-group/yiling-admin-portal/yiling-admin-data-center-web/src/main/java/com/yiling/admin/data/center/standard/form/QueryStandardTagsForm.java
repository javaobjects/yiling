package com.yiling.admin.data.center.standard.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author shichen
 * @类名 QueryStandardTagsForm
 * @描述
 * @创建时间 2022/10/20
 * @修改人 shichen
 * @修改时间 2022/10/20
 **/
@Data
public class QueryStandardTagsForm extends QueryPageListForm {
    @ApiModelProperty("名称")
    private String name;
}
