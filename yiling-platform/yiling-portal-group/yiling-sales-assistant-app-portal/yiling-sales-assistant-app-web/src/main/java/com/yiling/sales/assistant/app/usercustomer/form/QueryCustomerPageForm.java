package com.yiling.sales.assistant.app.usercustomer.form;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * 查询我的客户
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCustomerPageForm extends QueryPageListForm {

    /**
     * 企业名称或联系电话
     */
    @Length(max = 30)
    @ApiModelProperty(value = "企业名称或联系电话")
    private String nameOrPhone;

    /**
     * 是否查看禁用客户
     */
    @ApiModelProperty(value = "是否查看禁用客户:0是，1，否")
    private Integer customerStatus;

    /**
     * 排序，1:按照订单下单时间排序 2:按照下单数量进行排序
     */
    @ApiModelProperty(value = "排序，1:按照订单下单时间排序 2:按照下单数量进行排序")
    @NotNull
    private Integer orderSort;
}
