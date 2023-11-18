package com.yiling.settlement.report.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2022-10-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QuerySyncTaskPageListRequest extends QueryPageListRequest {

    /**
     * 同步类型：1-流向订单
     */
    private Integer type;

    /**
     * 同步状态：1-待同步 2-同步失败 3-同步成功
     */
    private List<Integer> status;
}
