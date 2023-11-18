package com.yiling.sjms.sale.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * 销售指标计算上一年度列计算接口
 */
@Data
public class SaleDepartmentTargetLastCalForm extends BaseForm {
    @NotNull
    List<SaleDepartmentSubTargetLastCalForm> list;
    @ApiModelProperty("销售目标金额")
    private BigDecimal saleTarget;
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



    public BigDecimal getLastTargetSum() {
        return lastTargetSum==null?BigDecimal.ZERO:lastTargetSum.setScale(0,BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getLastTargetRatioSum() {
        return lastTargetRatioSum==null?BigDecimal.ZERO:lastTargetRatioSum;
    }

}
