package com.yiling.b2b.app.order.form;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 批量还款
 * @author:wei.wang
 * @date:2021/11/8
 */
@Data
public class OrderB2BAppPayAmountListForm {
    /**
     * 批量还款
     */
    @NotEmpty(message = "不能为空")
    @ApiModelProperty(value = "还款信息", required = true)
    List< @Valid  OrderB2BAppPayAmountForm> list;
}
