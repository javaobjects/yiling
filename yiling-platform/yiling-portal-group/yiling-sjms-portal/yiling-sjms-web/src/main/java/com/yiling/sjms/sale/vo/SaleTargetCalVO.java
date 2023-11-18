package com.yiling.sjms.sale.vo;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 销售指标计算Form
 */
@Data
public class SaleTargetCalVO extends BaseVO {
    List<SaleTargetDepartmentCalVO> list;
    /**
     * 上一年目标单位元
     */
    @ApiModelProperty("上一年目标单位元")
    private BigDecimal lastTargetSum;

    /**
     * 上一年目标比例
     */
    @ApiModelProperty("上一年目标比例")
    private BigDecimal lastTargetRatioSum;

    /**
     * 本年目标单位元
     */
    @ApiModelProperty("本年目标单位元")
    private BigDecimal currentTargetSum;

    /**
     * 本年一年目标比例
     */
    @ApiModelProperty("本年一年目标比例")
    private BigDecimal currentTargetRatioSum;
    /**
     * 本年度增加单位元
     */
    @ApiModelProperty("本年度增加单位元")
    private BigDecimal currentIncreaseSum;

    public BigDecimal getLastTargetSum() {
        return lastTargetSum!=null?lastTargetSum.setScale(0):BigDecimal.ZERO;
    }

    public BigDecimal getLastTargetRatioSum() {
        return lastTargetRatioSum!=null?lastTargetRatioSum.setScale(0):BigDecimal.ZERO;
    }

    public BigDecimal getCurrentTargetSum() {
        return currentTargetSum!=null?currentTargetSum.setScale(0):BigDecimal.ZERO;
    }

    public BigDecimal getCurrentTargetRatioSum() {
        return currentTargetRatioSum!=null?currentTargetRatioSum.setScale(0):BigDecimal.ZERO;
    }

    public BigDecimal getCurrentIncreaseSum() {
        return currentIncreaseSum!=null?currentIncreaseSum.setScale(0):BigDecimal.ZERO;
    }
}
