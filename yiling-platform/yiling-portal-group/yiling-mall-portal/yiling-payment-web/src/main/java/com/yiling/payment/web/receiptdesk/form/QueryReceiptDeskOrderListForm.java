package com.yiling.payment.web.receiptdesk.form;

import javax.validation.constraints.NotEmpty;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询收营台订单列表 Form
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryReceiptDeskOrderListForm extends BaseForm {

    @NotEmpty
    @ApiModelProperty(value = "支付交易ID", required = true)
    private String payId;
}
