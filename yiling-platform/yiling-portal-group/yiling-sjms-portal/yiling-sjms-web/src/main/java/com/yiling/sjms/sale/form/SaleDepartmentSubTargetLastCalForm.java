package com.yiling.sjms.sale.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 上一年度计算列
 */
@Data
public class SaleDepartmentSubTargetLastCalForm extends BaseForm {

    private Long id;
    /**
     * 指标ID
     */
    @ApiModelProperty("指标ID")
    private Long saleTargetId;

    /**
     * 部门ID
     */
    @ApiModelProperty("部门ID")
    private Long departId;

    /**
     * 指标配置类型1-省区2-月份3-品种4区办
     */
    @ApiModelProperty("指标配置类型1-省区2-月份3-品种4区办")
    private Integer type;

    /**
     * 类型关联ID:部门ID,商品品类ID
     */
    @ApiModelProperty("类型关联ID:部门ID,商品品类ID")
    private Long relId;

    /**
     * 类型关联名称：部门名称,商品品类名称
     */
    @ApiModelProperty("类型关联名称")
    private String relName;
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
     * 本年度增加单位元
     */
    @ApiModelProperty("本年度增加单位元")
    private BigDecimal currentIncrease;
    @ApiModelProperty("本年目标单位元")
    private BigDecimal currentTarget;
    @ApiModelProperty("本年一年目标比例")
    private BigDecimal currentTargetRatio;

    public BigDecimal getLastTarget() {
        return lastTarget == null ? BigDecimal.ZERO : lastTarget;
    }
    public BigDecimal getCurrentTarget() {
        return currentTarget==null?BigDecimal.ZERO:currentTarget;
    }

    public BigDecimal getCurrentTargetRatio() {
        return currentTargetRatio==null?BigDecimal.ZERO:currentTargetRatio;
    }

    public BigDecimal getLastTargetRatio() {
         return lastTargetRatio==null?BigDecimal.ZERO:lastTargetRatio;
    }

    public BigDecimal getCurrentIncrease() {
        return currentIncrease==null?BigDecimal.ZERO:currentIncrease;
    }
}
