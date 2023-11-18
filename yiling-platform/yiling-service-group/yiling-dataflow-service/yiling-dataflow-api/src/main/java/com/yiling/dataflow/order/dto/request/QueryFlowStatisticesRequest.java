package com.yiling.dataflow.order.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 QueryCrmGoodsRequest
 * @描述
 * @创建时间 2023/2/23
 * @修改人 shichen
 * @修改时间 2023/2/23
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryFlowStatisticesRequest extends BaseRequest {
    private List<Long> crmEnterpriseIds;
}
