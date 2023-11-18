package com.yiling.dataflow.statistics.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2022/7/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsSpecStatisticsRequest extends QueryPageListRequest {

    /**
     * 企业id
     */
    private Long eid;

    /**
     * 查询月份 yyyy-MM
     */
    private String monthTime;

    /**
     * 商品+规格id
     */
    private Long specificationId;

    private String beginMonthDateTime;

    private String endMonthDateTime;
}
