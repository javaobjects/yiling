package com.yiling.sjms.sale.vo;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * 销售指标部门配置
 * </p>
 *
 * @author gxl
 * @date 2023-04-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QuerySaleDepartmentTargetVO extends BaseVO {

    private static final long serialVersionUID = 1L;
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


}
