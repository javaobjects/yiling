package com.yiling.sjms.sale.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SaleTargetDepartmentCurCalForm extends BaseForm {

    private Long id ;

    @ApiModelProperty("部门ID")
    private Long departId;

    /**
     * 部门名称
     */
    @ApiModelProperty("部门名称")
    private String departName;
    /**
     * 上一年目标单位元
     */
    @ApiModelProperty("上一年目标单位元")
    private BigDecimal lastTarget;

    /**
     * 上一年目标比例
     */
    @ApiModelProperty("上一年目标比例")
    private BigDecimal lastTargetRatio;

    /**
     * 本年目标单位元
     */
    @ApiModelProperty("本年目标单位元")
    private BigDecimal currentTarget;

    /**
     * 本年一年目标比例
     */
    @ApiModelProperty("本年一年目标比例")
    private BigDecimal currentTargetRatio;
    /**
     * 本年度增加单位元
     */
    @ApiModelProperty("本年度增加单位元")
    private BigDecimal currentIncrease;

    public BigDecimal getLastTarget() {
        return lastTarget==null?BigDecimal.ZERO:lastTarget;
    }

    public BigDecimal getLastTargetRatio() {
        return lastTargetRatio==null?BigDecimal.ZERO:lastTargetRatio;
    }

    public BigDecimal getCurrentTarget() {
        return currentTarget==null?BigDecimal.ZERO:currentTarget;
    }

    public BigDecimal getCurrentTargetRatio() {
        return currentTargetRatio==null?BigDecimal.ZERO:currentTargetRatio;
    }

    public BigDecimal getCurrentIncrease() {
        return currentIncrease==null?BigDecimal.ZERO:currentIncrease;
    }
}
