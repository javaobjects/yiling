package com.yiling.marketing.couponactivity.dto.request;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderUseCouponRequest extends BaseRequest {

    // 操作人ID opUserId 必传

    /**
     * 当前用户企业ID
     */
    @NotNull
    private Long eid;

    /**
     * 平台优惠券ID
     */
    private Long platformCouponId;

    /**
     * 店铺优惠券ID
     */
    private List<Long> couponIdList;

    /**
     * 操作人姓名
     */
    private String opUserName;

}
