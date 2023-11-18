package com.yiling.sales.assistant.app.search.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryEsGoodsSearchForm extends QueryPageListForm {
    /**
     * 搜索词
     */
    @ApiModelProperty(value = "搜索词")
    private String keyWord;

    /**
     * 企业ID(客户ID)
     */
    @NotNull
    @ApiModelProperty(value = "企业ID(客户ID)",required = true)
    private Long eid;

    /**
     * 配送商ids
     */
    @NotNull
    @ApiModelProperty(value = "配送商id",required = true)
    private Long distributorEid;
}
