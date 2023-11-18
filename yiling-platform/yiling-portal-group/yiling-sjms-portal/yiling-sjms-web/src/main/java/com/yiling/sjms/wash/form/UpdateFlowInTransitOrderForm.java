package com.yiling.sjms.wash.form;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2023/3/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateFlowInTransitOrderForm extends BaseForm {

    /**
     * 主键id
     */
    @NotNull
    @Min(1)
    private Long id;

    /**
     * 商品批次号
     */
    @ApiModelProperty(value = "商品批次号")
    private String gbBatchNo;

    /**
     * 库存数量
     */
    @NotNull(message = "在途数量不能为空")
    @Min(1)
    @ApiModelProperty(value = "库存数量")
    private BigDecimal gbNumber;

    /**
     * 采购时间
     */
    @ApiModelProperty(value = "采购时间，天")
    private Date gbPurchaseTime;
}
