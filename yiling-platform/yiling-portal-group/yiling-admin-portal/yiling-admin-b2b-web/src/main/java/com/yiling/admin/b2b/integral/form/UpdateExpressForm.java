package com.yiling.admin.b2b.integral.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 修改快递信息立即兑付 Form
 * </p>
 *
 * @author lun.yu
 * @date 2023-03-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateExpressForm extends BaseForm {

    /**
     * 订单ID
     */
    @NotNull
    @ApiModelProperty(value = "订单ID", required = true)
    private Long id;

    /**
     * 快递公司（见字典）
     */
    @NotEmpty
    @ApiModelProperty(value = "快递公司（见字典）", required = true)
    private String expressCompany;

    /**
     * 快递单号
     */
    @NotEmpty
    @ApiModelProperty(value = "快递单号", required = true)
    private String expressOrderNo;

}