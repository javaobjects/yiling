package com.yiling.b2b.admin.paymentdays.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询采购商列表 Form
 *
 * @author lun.yu
 * @date 2021/11/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCustomerListForm extends QueryPageListForm {

    @ApiModelProperty("客户名称")
    private String name;

}
