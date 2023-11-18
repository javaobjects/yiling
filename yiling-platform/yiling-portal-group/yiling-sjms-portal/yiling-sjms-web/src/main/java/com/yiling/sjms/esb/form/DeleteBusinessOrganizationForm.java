package com.yiling.sjms.esb.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 删除ESB业务架构打标标签 Form
 *
 * @author: lun.yu
 * @date: 2023-04-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DeleteBusinessOrganizationForm extends BaseForm {

    /**
     * 部门ID
     */
    @NotNull
    @ApiModelProperty(value = "部门ID", required = true)
    private Long orgId;

}
