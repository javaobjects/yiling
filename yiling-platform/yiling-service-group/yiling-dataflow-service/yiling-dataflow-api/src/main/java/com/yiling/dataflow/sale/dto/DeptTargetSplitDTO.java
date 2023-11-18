package com.yiling.dataflow.sale.dto;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2023-04-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DeptTargetSplitDTO extends BaseDTO {

    /**
     * 部门ID
     */
    private Long departId;

    /**
     * 部门名称
     */
    private String departName;

    /**
     * 指标配置类型 0-部门 1-省区 2-月份 3-品种 4-区办
     */
    private Integer type;

    /**
     * 类型关联ID:部门ID,商品品类ID
     */
    private Long relId;

    /**
     * 类型关联名称：部门名称,商品品类名称
     */
    private String relName;

    /**
     * 本年一年目标比例
     */
    private BigDecimal currentTargetRatio;
}
