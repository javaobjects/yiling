package com.yiling.data.center.admin.enterprisecustomer.form;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 更新客户信息 Form
 *
 * @author: lun.yu
 * @date: 2021/11/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class UpdateCustomerForm extends BaseForm {

    /**
     * 客户ID
     */
    @ApiModelProperty("客户ID")
    @NotNull
    private Long customerEid;

    /**
     * 支付方式ID列表
     */
    @NotEmpty
    @ApiModelProperty("支付方式ID列表")
    private List<Long> paymentMethodIds;

    /**
     * 客户分组id
     */
    @NotNull
    @ApiModelProperty("客户分组id")
    private Long customerGroupId;

}
