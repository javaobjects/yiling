package com.yiling.sjms.goodsplans.form;

import com.yiling.framework.common.base.form.QueryPageListForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *经销商
 */
@Data
public class FlowAnalyseEnterpriseForm extends QueryPageListForm {
    @ApiModelProperty(value = "经销商名称")
    private String ename;
}
