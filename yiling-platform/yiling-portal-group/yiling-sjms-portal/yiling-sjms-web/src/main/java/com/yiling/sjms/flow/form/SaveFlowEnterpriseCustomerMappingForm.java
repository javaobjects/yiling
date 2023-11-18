package com.yiling.sjms.flow.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 SaveFlowEnterpriseCustomerMappingForm
 * @描述
 * @创建时间 2023/3/2
 * @修改人 shichen
 * @修改时间 2023/3/2
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveFlowEnterpriseCustomerMappingForm extends BaseForm {

    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 标准机构编码
     */
    @NotNull
    @ApiModelProperty(value = "标准机构编码")
    private Long crmOrgId;

    /**
     * 标准机构名称
     */
    @NotEmpty
    @ApiModelProperty(value = "标准机构名称")
    private String orgName;
}
