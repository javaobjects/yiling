package com.yiling.admin.b2b.common.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 更新B2B-开屏位状态 Form
 * </p>
 *
 * @author lun.yu
 * @date 2023-05-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateOpenPositionStatusForm extends BaseForm {

    /**
     * ID
     */
    @NotNull
    @ApiModelProperty(value = "ID", required = true)
    private Long id;

    /**
     * 状态：1-未发布 2-已发布
     */
    @NotNull
    @ApiModelProperty(value = "状态：1-未发布 2-已发布", required = true)
    private Integer status;

}
