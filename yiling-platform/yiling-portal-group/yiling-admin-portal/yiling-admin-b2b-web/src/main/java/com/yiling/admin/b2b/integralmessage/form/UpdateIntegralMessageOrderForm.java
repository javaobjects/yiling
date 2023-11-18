package com.yiling.admin.b2b.integralmessage.form;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 更新积分消息排序 Form
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateIntegralMessageOrderForm extends BaseForm {

    /**
     * ID
     */
    @NotNull
    @ApiModelProperty(value = "ID", required = true)
    private Long id;

    /**
     * 排序
     */
    @NotNull
    @Range(min = 1, max = 200)
    @ApiModelProperty(value = "排序", required = true)
    private Integer sort;

}
