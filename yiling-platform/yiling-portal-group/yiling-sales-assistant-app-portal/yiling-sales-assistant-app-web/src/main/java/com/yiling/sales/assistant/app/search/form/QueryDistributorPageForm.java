package com.yiling.sales.assistant.app.search.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询配送商信息
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.sales.assistant.app.order.form
 * @date: 2021/9/14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryDistributorPageForm extends QueryPageListForm {


    @NotNull
    @ApiModelProperty(value = "客户ID",required = true)
    private Long purchaseEid;
}
