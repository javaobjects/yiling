package com.yiling.marketing.couponactivity.dto.request;

import java.util.List;

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
public class CouponActivityExistFlagRequest extends BaseRequest {

    /**
     * 当前登录企业ID
     */
    @NotNull
    private Long currentEid;

    /**
     * 企业id、商品id列表
     */
    @NotNull
    private List<CouponActivityExistFlagDetailRequest> detailList;

}
