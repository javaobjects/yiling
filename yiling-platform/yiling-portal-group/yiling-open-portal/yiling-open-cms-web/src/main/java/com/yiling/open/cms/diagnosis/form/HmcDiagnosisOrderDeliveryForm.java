package com.yiling.open.cms.diagnosis.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * 市场订单发货form
 *
 * @author: fan.shen
 * @date: 2023/05/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class HmcDiagnosisOrderDeliveryForm extends BaseForm {

    /**
     * IH 订单ID
     */
    @ApiModelProperty(value = "IH 订单ID", required = true)
    @NotNull(message = "不能为空")
    private Long ihOrderId;

    /**
     * 快递公司
     */
    @ApiModelProperty(value = "快递公司")
    private String deliveryCompany;

    /**
     * 快递单号
     */
    @ApiModelProperty(value = "快递单号")
    private String deliverNo;

}
