package com.yiling.sjms.agency.form;

import com.yiling.framework.common.base.form.QueryPageListForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCrmManorEnterprisePageListForm extends QueryPageListForm {
    @ApiModelProperty(value = "辖区ID")
    private Long manorId;
}
