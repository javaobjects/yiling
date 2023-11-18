package com.yiling.f2b.admin.procrelation.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2021/6/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryProcRelationDetailPageForm extends BaseForm {

    /**
     * 采购关系id
     */
    @Min(1)
    @NotNull
    @ApiModelProperty(value = "采购关系id")
    private Long relationId;

}
