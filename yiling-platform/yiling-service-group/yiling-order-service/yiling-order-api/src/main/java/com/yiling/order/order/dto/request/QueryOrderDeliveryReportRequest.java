package com.yiling.order.order.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author:wei.wang
 * @date:2021/7/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryOrderDeliveryReportRequest extends BaseRequest {

    /**
     * 发货开始时间
     */
    private Date startDeliverTime;

    /**
     * 发货结束时间
     */
    private Date endDeliverTime;

    /**
     * 企业eid
     */
    private List<Long> eidList;
}
