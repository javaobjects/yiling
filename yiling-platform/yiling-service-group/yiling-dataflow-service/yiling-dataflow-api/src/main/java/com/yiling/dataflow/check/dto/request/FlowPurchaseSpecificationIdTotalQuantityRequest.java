package com.yiling.dataflow.check.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/9/9
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FlowPurchaseSpecificationIdTotalQuantityRequest extends BaseRequest {

    /**
     * 采购时间
     */
    private Date startPoTime;

    /**
     * 采购时间
     */
    private Date endPoTime;

}
