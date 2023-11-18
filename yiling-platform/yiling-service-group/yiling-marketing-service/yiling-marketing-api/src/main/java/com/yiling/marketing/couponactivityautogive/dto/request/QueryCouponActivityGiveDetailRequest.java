package com.yiling.marketing.couponactivityautogive.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueryCouponActivityGiveDetailRequest extends BaseRequest {

    /**
     *
     */
    private Integer type;

}
