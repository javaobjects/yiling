package com.yiling.f2b.admin.enterprise.form;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 保存企业渠道商信息 Form
 *
 * @author: yuecheng.chen
 * @date: 2021/6/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class SaveChannelCustomerForm extends BaseForm {

    /**
     * 企业ID
     */
    @NotNull
    @NotNull(message = "不能为空！")
    private Long eid;

    /**
     * 客户ID
     */
    @ApiModelProperty("客户ID")
    @NotNull(message = "不能为空！")
    private Long customerEid;

    /**
     * 客户联系人ID列表
     */
    @ApiModelProperty("客户联系人ID列表")
    private List<Long> contactUserIds;

    /**
     * 支付方式ID列表
     */
    @ApiModelProperty("支付方式ID列表")
    private List<Long> paymentMethodIds;

    /**
     * 客户分组id
     */
    @ApiModelProperty("客户分组id")
    private Long customerGroupId;

}
