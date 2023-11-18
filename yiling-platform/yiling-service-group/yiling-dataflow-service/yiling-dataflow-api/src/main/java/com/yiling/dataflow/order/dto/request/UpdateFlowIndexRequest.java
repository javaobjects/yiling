package com.yiling.dataflow.order.dto.request;

import java.util.Date;

import com.yiling.dataflow.flow.enums.ErpTopicName;
import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2023/2/13
 */
@Data
public class UpdateFlowIndexRequest extends BaseRequest {

    private Long eid;

    private Date startUpdateTime;

    private Date endUpdateTime;

    private Integer year;

    private String taskCode;
}
