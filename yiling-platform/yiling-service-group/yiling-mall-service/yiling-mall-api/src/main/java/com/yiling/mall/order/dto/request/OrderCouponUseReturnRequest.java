package com.yiling.mall.order.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderCouponUseReturnRequest extends BaseRequest {

    // opUserId 必传

    /**
     * 优惠券ID，必传（最多两个，一个商家券一个平台券）
     */
    private List<Long> couponIdList;

}
