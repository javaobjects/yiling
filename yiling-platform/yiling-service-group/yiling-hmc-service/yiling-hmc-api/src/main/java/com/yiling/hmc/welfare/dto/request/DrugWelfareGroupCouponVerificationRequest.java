package com.yiling.hmc.welfare.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: benben.jia
 * @data: 2022/10/14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DrugWelfareGroupCouponVerificationRequest extends BaseRequest {

    private static final long serialVersionUID = 1L;

    /**
     * 福利券码
     */
    private String couponCode;

    /**
     * 当前用户id
     */
    private Long userId;

    /**
     * 企业ID
     */
    private Long eid;

}
