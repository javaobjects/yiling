package com.yiling.admin.system.system.form;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 启用/停用 Form
 *
 * @author: lun.yu
 * @date: 2021/7/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class UpdateAdminStatusForm extends BaseForm {

    /**
     * 用户ID
     */
    @NotNull
    @ApiModelProperty("用户ID")
    private Long userId;

    /**
     * 状态：1-启用 2-停用
     */
    @NotNull
    @Range(min = 1, max = 2)
    @ApiModelProperty("状态：1-启用 2-停用")
    private Integer status;

}
