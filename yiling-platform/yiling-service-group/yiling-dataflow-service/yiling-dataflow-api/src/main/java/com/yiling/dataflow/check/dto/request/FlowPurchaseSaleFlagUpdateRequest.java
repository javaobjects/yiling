package com.yiling.dataflow.check.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/9/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FlowPurchaseSaleFlagUpdateRequest extends BaseRequest {

    /**
     * ID
     */
    private Long id;

    /**
     * 销售流向是否存在，0不存在 1存在
     */
    private Integer flowSaleFlag;

}
