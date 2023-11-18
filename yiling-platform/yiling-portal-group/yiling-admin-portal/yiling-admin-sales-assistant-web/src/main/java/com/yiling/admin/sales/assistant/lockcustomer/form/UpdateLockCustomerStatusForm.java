package com.yiling.admin.sales.assistant.lockcustomer.form;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 更新销售助手-锁定用户状态 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-04-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateLockCustomerStatusForm extends BaseForm {

    /**
     * ID集合
     */
    @NotEmpty
    @ApiModelProperty("ID集合")
    private List<Long> idList;

    /**
     * 状态：1-启用 2-停用
     */
    @NotNull
    @ApiModelProperty("状态：1-启用 2-停用")
    private Integer status;

}
