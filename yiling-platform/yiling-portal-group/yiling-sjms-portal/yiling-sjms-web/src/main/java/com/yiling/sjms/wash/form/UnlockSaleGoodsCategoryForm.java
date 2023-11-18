package com.yiling.sjms.wash.form;

import com.yiling.framework.common.base.form.QueryPageListForm;
import com.yiling.sjms.crm.form.QueryCrmGoodsCategoryForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2023/5/18
 */
@Data
public class UnlockSaleGoodsCategoryForm extends QueryPageListForm {

    @ApiModelProperty("规则ID")
    private Long ruleId;

    /**
     * 品类名称
     */
    @ApiModelProperty(value = "品类名称")
    private String name;
}
