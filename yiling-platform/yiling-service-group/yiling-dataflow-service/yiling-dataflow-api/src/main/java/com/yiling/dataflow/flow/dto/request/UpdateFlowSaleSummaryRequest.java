package com.yiling.dataflow.flow.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2022/11/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateFlowSaleSummaryRequest extends BaseRequest {
    private Date startTime;
    private Date endTime;
    private Long eid;
    private Long crmId;
}
