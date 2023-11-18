package com.yiling.sjms.crm.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/6/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryHosDruRelPageListForm extends QueryPageListForm {

    /**
     * form表主键
     */
    @ApiModelProperty(value = "formId")
    private Long formId;
}
