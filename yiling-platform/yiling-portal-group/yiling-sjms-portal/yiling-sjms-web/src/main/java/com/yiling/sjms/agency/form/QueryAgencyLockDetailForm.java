package com.yiling.sjms.agency.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2023/2/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryAgencyLockDetailForm extends BaseForm {

    /**
     * 主表主键
     */
    @ApiModelProperty(value = "机构eid")
    @NotNull
    private Long crmEnterpriseId;

    /**
     * 表单id
     */
    @NotNull
    @ApiModelProperty(value = "表单id")
    private Long formId;
}