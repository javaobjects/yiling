package com.yiling.sjms.crm.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 SaveOrUpdateCrmDepartProductGroupForm
 * @描述
 * @创建时间 2023/4/12
 * @修改人 shichen
 * @修改时间 2023/4/12
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveOrUpdateCrmDepartProductGroupForm extends BaseForm {

    @ApiModelProperty(value = "id")
    private Long id;
    /**
     * 部门id
     */
    @NotNull(message = "部门id为空")
    @ApiModelProperty(value = "部门id")
    private Long departmentId;

    /**
     * 部门
     */
    @ApiModelProperty(value = "部门")
    private String department;

    /**
     * 产品组
     */
    @ApiModelProperty(value = "产品组")
    private String productGroup;

    /**
     * 产品组id
     */
    @NotNull(message = "产品组id为空")
    @ApiModelProperty(value = "产品组id")
    private Long productGroupId;
}
