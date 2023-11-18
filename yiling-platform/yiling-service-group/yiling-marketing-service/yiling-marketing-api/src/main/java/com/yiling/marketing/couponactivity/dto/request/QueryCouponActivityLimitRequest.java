package com.yiling.marketing.couponactivity.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/12/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCouponActivityLimitRequest extends BaseRequest {

    /**
     * 商家可用类型(1-全部； 2-部分)
     */
    private Integer enterpriseLimit;

    /**
     * 商品可用类型(1-全部； 2-部分)
     */
    private Integer goodsLimit;

}
