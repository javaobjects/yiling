package com.yiling.dataflow.statistics.dto.request;

import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.base.request.QueryPageListRequest;

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
public class QueryBalanceStatisticsMonthRequest extends QueryPageListRequest {

    /**
     * 年月份
     */
    private String time;

    /**
     * 企业id
     */
    private Long eid;

    /**
     * 实施负责人
     */
    private String installEmployee;

    /**
     * 范围查询类型(1-采购 2-销售 3-当前库存 4-平衡相差数)
     */
    private Integer quantityType;

    /**
     * 开始数量
     */
    private Integer minQuantity;

    /**
     * 结束数量
     */
    private Integer maxQuantity;

    /**
     * 月末库存查询时间
     */
    private String gbTime;

}
