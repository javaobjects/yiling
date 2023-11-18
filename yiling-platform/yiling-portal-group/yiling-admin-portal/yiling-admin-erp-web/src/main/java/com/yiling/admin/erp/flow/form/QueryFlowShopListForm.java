package com.yiling.admin.erp.flow.form;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2023/4/11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryFlowShopListForm extends BaseForm {

    /**
     * 类型：1-总店 2-门店
     */
    @ApiModelProperty(value = "类型：1-总店 2-门店")
    private Integer type;

    /**
     * 名称（模糊查询）
     */
    @ApiModelProperty(value = "名称（模糊查询）")
    private String name;

}
