package com.yiling.open.cms.diagnosis.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * IH 后台退诊入参
 *
 * @author: fan.shen
 * @data: 2023/05/15
 */
@Data
@Accessors(chain = true)
public class HmcCancelDiagnosisRecordForm extends BaseForm {

    @NotNull(message = "不能为空")
    @ApiModelProperty(value = "问诊单id")
    private Integer diagnosisRecordId;

    @ApiModelProperty(value = "单号")
    private String diagnosisRecordOrderNo;

    @NotBlank(message = "不能为空")
    @ApiModelProperty(value = "商户交易编号")
    private String merTranNo;

    @NotNull(message = "不能为空")
    @ApiModelProperty(value = "退款金额")
    private BigDecimal refundAmount;

    @NotNull
    @ApiModelProperty(value = "退款记录id")
    private Integer diagnosisRecordRefundId;


}
