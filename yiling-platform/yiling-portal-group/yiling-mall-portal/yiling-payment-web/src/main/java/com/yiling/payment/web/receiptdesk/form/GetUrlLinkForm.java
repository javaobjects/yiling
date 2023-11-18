package com.yiling.payment.web.receiptdesk.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 批量支付订单参数
 * @author:wei.wang
 * @date:2021/11/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GetUrlLinkForm extends BaseForm {

    /**
     * payId
     */
    @NotBlank()
    @ApiModelProperty(value = "payId", required = true)
    private String payId;

}
