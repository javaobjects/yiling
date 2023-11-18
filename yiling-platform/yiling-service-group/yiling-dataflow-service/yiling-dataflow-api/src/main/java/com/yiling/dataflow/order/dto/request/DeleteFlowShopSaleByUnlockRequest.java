package com.yiling.dataflow.order.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2023/3/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DeleteFlowShopSaleByUnlockRequest extends BaseRequest {

    /**
     * 商业公司ID
     */
    private Long eid;

    /**
     * potime 开始
     */
    private String startMonth;

    /**
     * potime 结束
     */
    private String endMonth;
}
