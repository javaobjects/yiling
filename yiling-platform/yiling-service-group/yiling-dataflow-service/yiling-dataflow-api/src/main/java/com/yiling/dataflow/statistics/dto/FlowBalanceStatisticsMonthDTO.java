package com.yiling.dataflow.statistics.dto;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2022/7/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FlowBalanceStatisticsMonthDTO extends BaseDTO {

    /**
     * 年月份
     */
    private String time;

    /**
     * 企业id
     */
    private Long eid;

    /**
     * 企业名称
     */
    private String ename;

    /**
     * 实施负责人
     */
    private String installEmployee;

    /**
     * 采购数量
     */
    private Long poQuantity;

    /**
     * 未匹配采购数量
     */
    private Long noMatchPoQuantity;

    /**
     * 采购上月增长率
     */
    private String poGrowthRate;

    /**
     * 销售数量
     */
    private Long soQuantity;

    /**
     * 未匹配销售数量
     */
    private Long noMatchSoQuantity;

    /**
     * 销售上月增长率
     */
    private String soGrowthRate;

    /**
     * 当前库存量(月末库存量)
     */
    private Long gbQuantity;

    /**
     * 未匹配库存数量
     */
    private Long noMatchGbQuantity;

    /**
     * 月初库存量
     */
    private Long beginMonthQuantity;

    /**
     * 平衡相差数
     */
    private Long differQuantity;
}
