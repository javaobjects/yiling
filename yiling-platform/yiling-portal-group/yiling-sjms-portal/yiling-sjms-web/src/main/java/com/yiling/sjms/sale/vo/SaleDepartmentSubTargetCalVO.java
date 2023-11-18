package com.yiling.sjms.sale.vo;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SaleDepartmentSubTargetCalVO extends BaseVO{

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
