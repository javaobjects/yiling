package com.yiling.marketing.couponactivityautogive.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CouponActivityAutoGiveRequest extends BaseRequest {

    /**
     * id
     */
    private Long id;

    /**
     * 操作人姓名
     */
    private String userName;

}