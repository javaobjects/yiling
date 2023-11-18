package com.yiling.dataflow.sale.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2023-04-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaleDepartmentSubTargetDTO extends BaseDTO {

    private static final long serialVersionUID = -8957872667792681102L;

    /**
     * 指标ID
     */
    private Long saleTargetId;

    /**
     * 部门ID
     */
    private Long departId;

    /**
     * 指标配置类型1-省区2-月份3-品种4区办
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
     * 上一年目标单位元
     */
    private BigDecimal lastTarget;

    /**
     * 上一年目标比例
     */
    private BigDecimal lastTargetRatio;

    /**
     * 本年目标单位元
     */
    private BigDecimal currentTarget;

    /**
     * 本年一年目标比例
     */
    private BigDecimal currentTargetRatio;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建日期
     */
    private Date createTime;

    /**
     * 更新人
     */
    private Long updateUser;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 本年度增加单位元
     */
    private BigDecimal currentIncrease;

}
