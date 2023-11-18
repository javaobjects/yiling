package com.yiling.user.enterprise.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.user.enterprise.dto.request
 * @date: 2021/9/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryDeliveryAddressRequest extends BaseRequest {

    /**
     * 企业eid
     */
    private Long eid;
}
