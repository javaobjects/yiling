package com.yiling.f2b.admin.procrelation.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2023/6/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryTemplateGoodsListForm extends BaseForm {

    /**
     * id
     */
    @NotNull
    @ApiModelProperty(value = "模板id")
    private Long templateId;

    /**
     * id
     */
    @NotNull
    @ApiModelProperty(value = "工业eid")
    private Long factoryEid;
}
