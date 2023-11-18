package com.yiling.marketing.couponactivityautoget.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CouponActivityAutoGetOperateRequest extends BaseRequest {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 当前操作人ID
     */
    private Long userId;

}
