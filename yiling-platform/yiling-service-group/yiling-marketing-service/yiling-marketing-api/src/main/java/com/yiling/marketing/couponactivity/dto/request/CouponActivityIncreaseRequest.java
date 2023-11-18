package com.yiling.marketing.couponactivity.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/10/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CouponActivityIncreaseRequest extends BaseRequest {

    /**
     * 当前企业ID
     */
    private Long eid;

    /**
     * 主键ID
     */
    @NotNull
    private Long id;

    /**
     * 数量
     */
    @NotNull
    private Integer quantity;

    /**
     * 备注
     */
    private String remark;

}
