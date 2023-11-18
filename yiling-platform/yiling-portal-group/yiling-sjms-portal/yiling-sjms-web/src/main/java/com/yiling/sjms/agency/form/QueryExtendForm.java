package com.yiling.sjms.agency.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: gxl
 * @date: 2023/2/23
 */
@Data
public class QueryExtendForm extends BaseForm {

    /**
     * 主表主键
     */
    @ApiModelProperty(value = "机构eid")
    @NotNull
    private Long crmEnterpriseId;

    /**
     * erp供应链角色：1-经销商 2-终端医院 3-终端药店
     */
    @ApiModelProperty(value = "供应链角色(选择的机构的)")
    private Integer supplyChainRole;
}