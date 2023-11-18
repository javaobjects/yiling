package com.yiling.marketing.couponactivity.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/10/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CouponActivityOperateRequest extends BaseRequest {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 当前企业ID
     */
    private Long eid;

}
