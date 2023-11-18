package com.yiling.sjms.agency.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2023/3/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CheckAgencyLockFormDataForm extends BaseForm {

    /**
     * 表单id-修改时需要
     */
    @ApiModelProperty("表单id-修改时校验需要")
    private Long id;

    /**
     * 校验的机构的id
     */
    @NotNull
    @ApiModelProperty("校验的机构的id")
    private Long crmEnterpriseId;

    /**
     * 主流程表单主表id
     */
    @ApiModelProperty("主流程表单主表id")
    private Long formId;
    
    /**
     * 供应链角色：1-商业公司 2-医疗机构 3-零售机构
     */
    @NotNull
    @ApiModelProperty("供应链角色：1-商业公司 2-医疗机构 3-零售机构")
    private Integer supplyChainRole;
}
