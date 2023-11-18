package com.yiling.hmc.welfare.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @author: fan.shen
 * @data: 2022/09/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryGroupCouponRequest extends BaseRequest {

    private static final long serialVersionUID = 1L;

    /**
     * 福利券码
     */
    private String couponCode;
}
