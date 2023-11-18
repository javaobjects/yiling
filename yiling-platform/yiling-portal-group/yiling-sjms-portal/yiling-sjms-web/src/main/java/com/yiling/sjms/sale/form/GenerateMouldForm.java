package com.yiling.sjms.sale.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2023-04-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GenerateMouldForm extends QueryPageListForm {

    /**
     * 指标ID
     */
    @ApiModelProperty("指标ID")
    @NotNull
    private Long saleTargetId;

    /**
     * 部门ID
     */
    @ApiModelProperty("部门ID")
    @NotNull
    private Long departId;
}
