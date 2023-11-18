package com.yiling.sales.assistant.app.payment.form;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询收营台订单列表 Form
 *
 * @author: xuan.zhou
 * @date: 2021/6/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryReceiptDeskOrderListForm extends BaseForm {

    @NotEmpty
    @ApiModelProperty(value = "订单号列表", required = true)
    private List<String> orderNos;
}
