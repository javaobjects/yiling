package com.yiling.sjms.sale.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QuerySaleTargetCheckForm extends BaseForm {
    @ApiModelProperty("指标名称")
    private String name;
}
