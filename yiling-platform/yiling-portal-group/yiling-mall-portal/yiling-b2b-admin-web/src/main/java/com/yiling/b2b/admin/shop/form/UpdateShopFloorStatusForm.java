package com.yiling.b2b.admin.shop.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * B2B-更新店铺楼层状态 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-20
 */
@Data
public class UpdateShopFloorStatusForm extends BaseForm {

    /**
     * ID
     */
    @NotNull
    @ApiModelProperty(value = "ID", required = true)
    private Long id;

    /**
     * 楼层状态：1-启用 2-停用
     */
    @NotNull
    @ApiModelProperty(value = "楼层状态：1-启用 2-停用", required = true)
    private Integer status;

}
