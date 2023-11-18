package com.yiling.dataflow.order.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2023/3/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryFlowGoodsBatchDetailExistRequest extends BaseRequest {

    /**
     * 商业公司ID
     */
    private Long eid;

    /**
     * 当前库存时间
     */
    private Date gbDetailTime;
}
