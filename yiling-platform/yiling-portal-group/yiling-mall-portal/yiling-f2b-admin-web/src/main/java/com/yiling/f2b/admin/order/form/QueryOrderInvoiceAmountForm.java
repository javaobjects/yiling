package com.yiling.f2b.admin.order.form;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 订单金额计算
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryOrderInvoiceAmountForm extends QueryPageListForm  {


    @NotEmpty
    @ApiModelProperty(value = "开票金额数据")
    private List<@Valid QueryOrderInvoiceAmountErpDeliveryNoForm> list;

}
