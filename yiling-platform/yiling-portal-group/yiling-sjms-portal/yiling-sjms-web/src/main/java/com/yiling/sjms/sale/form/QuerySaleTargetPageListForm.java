package com.yiling.sjms.sale.form;

import com.yiling.framework.common.base.form.QueryPageListForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QuerySaleTargetPageListForm extends QueryPageListForm {
    @ApiModelProperty("指标ID")
    private Long id;
}
