package com.yiling.sjms.sale.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Setter;

import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.util.List;

/**
 * 销售指标计算Form
 */
@Data
public class SaleTargetCurCalForm extends BaseForm {
    List<SaleTargetDepartmentCurCalForm> list;
    /**
     * 上一年目标单位元
     */
    @ApiModelProperty("合计上一年目标单位元")
    private BigDecimal lastTargetSum;

    /**
     * 上一年目标比例
     */
    @ApiModelProperty("合计上一年目标比例")
    @Setter
    private BigDecimal lastTargetRatioSum;

    /**
     * 本年目标单位元
     */
   // @Digits(integer = 10, fraction = 0, message = "")
    @ApiModelProperty("合计本年目标单位元")
    private BigDecimal currentTargetSum;

    /**
     * 本年一年目标比例
     */
    @ApiModelProperty("合计本年一年目标比例")
    @Digits(integer = 10, fraction = 0, message = "")
    private BigDecimal currentTargetRatioSum;
    /**
     * 本年度增加单位元
     */
    @ApiModelProperty("合计本年度增加单位元")
   // @Digits(integer = 10, fraction = 0, message = "")
    private BigDecimal currentIncreaseSum;

    public BigDecimal getLastTargetSum() {
        return lastTargetSum==null?BigDecimal.ZERO:lastTargetSum.setScale(0,BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getLastTargetRatioSum() {
        return lastTargetRatioSum==null?BigDecimal.ZERO:lastTargetRatioSum;
    }

    public BigDecimal getCurrentTargetSum() {
        return currentTargetSum==null?BigDecimal.ZERO:currentTargetSum.setScale(0,BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getCurrentTargetRatioSum() {
        return currentTargetRatioSum==null?BigDecimal.ZERO:currentTargetRatioSum;
    }

    public BigDecimal getCurrentIncreaseSum() {
        return currentIncreaseSum==null?BigDecimal.ZERO:currentIncreaseSum.setScale(0,BigDecimal.ROUND_HALF_UP);
    }
}
