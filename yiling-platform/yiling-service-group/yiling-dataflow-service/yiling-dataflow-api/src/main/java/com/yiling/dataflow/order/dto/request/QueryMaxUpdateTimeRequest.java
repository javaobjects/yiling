package com.yiling.dataflow.order.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2023/2/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryMaxUpdateTimeRequest extends BaseRequest {

    /**
     * 商业公司ID
     */
    private Long eid;

    private Date startTime;

    private Date endTime;
}
