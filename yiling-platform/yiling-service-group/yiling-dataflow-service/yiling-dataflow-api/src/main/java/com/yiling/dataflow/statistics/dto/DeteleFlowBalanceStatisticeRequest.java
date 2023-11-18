package com.yiling.dataflow.statistics.dto;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2022/6/16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DeteleFlowBalanceStatisticeRequest extends BaseRequest {
    private Date startTime;
    private Date endTime;
    private List<Long> eidList;
}
