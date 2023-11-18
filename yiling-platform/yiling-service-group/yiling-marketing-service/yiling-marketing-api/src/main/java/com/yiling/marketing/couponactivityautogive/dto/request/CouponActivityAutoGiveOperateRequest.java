package com.yiling.marketing.couponactivityautogive.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CouponActivityAutoGiveOperateRequest extends BaseRequest {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 当前操作人ID
     */
    private Long userId;

}
