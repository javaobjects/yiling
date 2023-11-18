package com.yiling.marketing.couponactivity.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CouponActivityExistFlagDetailRequest extends BaseRequest {

    /**
     * 企业ID
     */
    @NotNull
    private Long eid;

    /**
     * 商品ID
     */
    @NotNull
    private Long goodsId;

}
