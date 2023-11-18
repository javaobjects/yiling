package com.yiling.sjms.agency.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2023/4/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateCrmEnterpriseRelationPinchRunnerForm extends BaseForm {

    /**
     * ID
     */
    @NotNull
    @Min(value = 1)
    @ApiModelProperty(value = "ID")
    private Long id;

    /**
     * 机构id
     */
    @NotNull
    @Min(value = 1)
    @ApiModelProperty(value = "机构id")
    private Long crmEnterpriseId;

    /**
     * 三者关系ID
     */
    @NotNull
    @Min(value = 1)
    @ApiModelProperty(value = "三者关系ID")
    private Long enterpriseCersId;

    /**
     * 销量计入主管岗位代码
     */
    @NotNull
    @Min(value = 1)
    @ApiModelProperty(value = "销量计入主管岗位代码")
    private Long businessSuperiorPostCode;
}
