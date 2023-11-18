package com.yiling.sales.assistant.app.paymentdays.form;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 查询客户 Form
 *
 * @author: lun.yu
 * @date: 2021/9/27
 */
@Data
@Accessors(chain = true)
public class QueryCustomerForm extends BaseForm {

    /**
     * 客户名称或联系人姓名
     */
    @ApiModelProperty(value = "客户名称或联系人姓名", required = true)
    private String name;

}
