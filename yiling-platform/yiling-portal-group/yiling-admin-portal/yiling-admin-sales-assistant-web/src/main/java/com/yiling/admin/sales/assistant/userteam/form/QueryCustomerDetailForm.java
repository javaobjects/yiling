package com.yiling.admin.sales.assistant.userteam.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 销售助手后台-查询订单信息/客户信息/拉人信息 列表 Form
 * 
 * @author lun.yu
 * @date 2022/1/25
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCustomerDetailForm extends QueryPageListForm {

    /**
     * 用户ID
     */
    @NotNull
    @ApiModelProperty(value = "用户ID")
    private Long userId;

}
