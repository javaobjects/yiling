package com.yiling.sales.assistant.app.invoice.form;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 保存票折信息
 * @author:wei.wang
 * @date:2021/7/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveOrderDetailTicketDiscountForm extends BaseForm {
    @NotNull(message = "不能为空")
    @ApiModelProperty(value = "订单id")
    private Long id;

    @ApiModelProperty(value = "票折单号")
    private List<String> ticketDiscountNoList;

    @NotNull(message = "不能为空")
    @ApiModelProperty(value = "是否使用票折：0-否 1-是")
    private Integer ticketDiscountFlag;

    @NotBlank(message = "不能为空")
    @ApiModelProperty(value = "转换规则编码")
    private String transitionRuleCode;

    @NotEmpty(message = "不能为空")
    @ApiModelProperty(value = "票折信息")
    private List<@Valid OrderDetailTicketDiscountForm> ticketDiscount;
}
