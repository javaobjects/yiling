package com.yiling.f2b.admin.enterprise.form;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 修改客户信息 Form
 *
 * @author: xuan.zhou
 * @date: 2021/6/30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateCustomerInfoForm extends BaseForm {

    /**
     * 客户企业ID
     */
    @NotNull
    @ApiModelProperty(value = "客户企业ID", required = true)
    private Long customerEid;

    /**
     * 客户支付方式ID列表
     */
    @ApiModelProperty(value = "客户支付方式ID列表", required = true)
    private List<Long> paymentMethodIds;

    /**
     * 客户分组ID
     */
    @ApiModelProperty(value = "客户分组ID", required = true)
    private Long customerGroupId;
}
