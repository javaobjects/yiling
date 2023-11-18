package com.yiling.open.cms.diagnosis.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * IH 处方订单自动收货
 *
 * @author: fan.shen
 * @data: 2023/02/20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class HmcDiagnosisOrderReceiveForm extends BaseForm {

    /**
     * IH 订单ID
     */
    @ApiModelProperty(value = "IH 订单ID", required = true)
    @NotNull(message = "不能为空")
    private Long ihOrderId;

}
