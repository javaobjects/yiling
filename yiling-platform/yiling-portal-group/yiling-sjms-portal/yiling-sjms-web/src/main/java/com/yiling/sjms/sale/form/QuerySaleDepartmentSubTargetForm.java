package com.yiling.sjms.sale.form;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.user.common.util.bean.Not;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class QuerySaleDepartmentSubTargetForm extends BaseForm {
    @ApiModelProperty("指标ID")
    @NotNull
    private Long saleTargetId;
    /**
     * 部门ID
     */
    @ApiModelProperty("部门ID")
    @NotNull
    private Long departId;
    @ApiModelProperty("指标配置类型1-省区2-月份-品种4-区办")
    @NotNull
    private Integer type;
}
