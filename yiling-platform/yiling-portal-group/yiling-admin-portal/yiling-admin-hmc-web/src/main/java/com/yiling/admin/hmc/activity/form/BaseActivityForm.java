package com.yiling.admin.hmc.activity.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * 查询患教活动 Form
 *
 * @author: fan.shen
 * @date: 2022/9/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class BaseActivityForm extends BaseForm {

    /**
     * 活动ID
     */
    @ApiModelProperty("活动ID")
    @NotNull(message = "活动ID不能为空")
    private Long id;

}
