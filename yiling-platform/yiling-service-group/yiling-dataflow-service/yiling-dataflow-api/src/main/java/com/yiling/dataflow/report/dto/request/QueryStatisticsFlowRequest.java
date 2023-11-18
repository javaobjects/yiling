package com.yiling.dataflow.report.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2022/6/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryStatisticsFlowRequest extends BaseRequest {
    /**
     * 商品品类:0连花1非连花
     */
    private Integer goodsCategory;

    private Date startTime;

    private Date endTime;

    private List<Long> eids;
}
