package com.yiling.sjms.gb.form;


import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
public class QueryGBGoodsInfoPageForm extends QueryPageListForm {

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "名称")
    private String name;
}
