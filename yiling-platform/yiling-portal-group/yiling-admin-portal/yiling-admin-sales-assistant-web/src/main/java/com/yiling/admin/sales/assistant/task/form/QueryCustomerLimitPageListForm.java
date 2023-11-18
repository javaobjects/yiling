package com.yiling.admin.sales.assistant.task.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询企业客户分页列表 Form
 *
 * @author: xuan.zhou
 * @date: 2021/5/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class QueryCustomerLimitPageListForm extends QueryPageListForm {

    @ApiModelProperty("客户名称")
    private String name;

    @ApiModelProperty("是否控制价格：0否 1是")
    private Integer limitFlag;

}
