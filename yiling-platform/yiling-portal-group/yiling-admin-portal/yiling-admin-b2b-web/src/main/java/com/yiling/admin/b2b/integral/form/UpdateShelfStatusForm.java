package com.yiling.admin.b2b.integral.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 更新积分兑换商品上下架状态 Form
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateShelfStatusForm extends BaseForm {

    /**
     * ID
     */
    @NotNull
    @ApiModelProperty(value = "ID", required = true)
    private Long id;

    /**
     * 上架状态：1-已上架 2-已下架
     */
    @NotNull
    @ApiModelProperty(value = "上架状态：1-已上架 2-已下架", required = true)
    private Integer status;

}
