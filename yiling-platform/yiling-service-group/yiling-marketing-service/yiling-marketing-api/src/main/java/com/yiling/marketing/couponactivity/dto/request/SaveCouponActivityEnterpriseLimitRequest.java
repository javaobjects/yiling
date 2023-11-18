package com.yiling.marketing.couponactivity.dto.request;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/10/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveCouponActivityEnterpriseLimitRequest extends BaseRequest {

    /**
     * 企业信息列表
     */
    @NotNull
    private List<SaveCouponActivityEnterpriseLimitDetailRequest> enterpriseLimitList;

}
