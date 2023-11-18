package com.yiling.sjms.sale.vo;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 销售指标部门配置
 * </p>
 *
 * @author gxl
 * @date 2023-04-12
 */
@Data
public class QuerySaleDepartmentTargetConfigVO extends BaseVO {

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

    private List<QuerySaleDepartmentSubTargetVO> list;
    @ApiModelProperty("指标年份")
    private Long targetYear;
    /**
     * 指标名称
     */
    @ApiModelProperty("指标名称")
    private String name;

    /**
     * 指标编号
     */
    @ApiModelProperty("指标编号")
    private String targetNo;

    @ApiModelProperty("销售目标金额")
    private BigDecimal saleTarget;

}
