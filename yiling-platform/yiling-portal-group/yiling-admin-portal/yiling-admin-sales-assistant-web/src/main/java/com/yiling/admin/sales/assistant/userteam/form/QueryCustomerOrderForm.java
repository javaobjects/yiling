package com.yiling.admin.sales.assistant.userteam.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 销售助手后台-查询企业客户对应的订单列表 Form
 * 
 * @author lun.yu
 * @date 2021/9/29
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCustomerOrderForm extends QueryPageListForm {

    /**
     * 用户ID
     */
    @NotNull
    @ApiModelProperty(value = "用户ID")
    private Long userId;

    /**
     * 客户企业ID
     */
    @NotNull
    @ApiModelProperty("客户企业ID")
    private Long customerEid;



}
