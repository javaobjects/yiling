package com.yiling.sjms.sale.vo;

import com.yiling.framework.common.base.BaseVO;
import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaleTargetDepartmentCalVO extends BaseVO{

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
        return lastTarget!=null?lastTarget.setScale(0,BigDecimal.ROUND_HALF_UP):BigDecimal.ZERO;
    }

    public BigDecimal getLastTargetRatio() {
        return lastTargetRatio!=null?lastTargetRatio.setScale(2,BigDecimal.ROUND_HALF_UP):BigDecimal.ZERO;
    }

    public BigDecimal getCurrentTarget() {
        return currentTarget!=null?currentTarget.setScale(0,BigDecimal.ROUND_HALF_UP):BigDecimal.ZERO;
    }

    public BigDecimal getCurrentTargetRatio() {
        return currentTargetRatio!=null?currentTargetRatio.setScale(2,BigDecimal.ROUND_HALF_UP):BigDecimal.ZERO;
    }

    public BigDecimal getCurrentIncrease() {
        return currentIncrease!=null?currentIncrease.setScale(0,BigDecimal.ROUND_HALF_UP):BigDecimal.ZERO;
    }
}
