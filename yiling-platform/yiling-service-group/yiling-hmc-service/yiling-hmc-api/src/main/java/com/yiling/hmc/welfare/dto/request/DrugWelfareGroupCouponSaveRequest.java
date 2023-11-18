package com.yiling.hmc.welfare.dto.request;


import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: benben.jia
 * @date:  2022-09-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DrugWelfareGroupCouponSaveRequest extends BaseRequest {


    /**
     * 药品福利id
     */
    private Long drugWelfareId;


    /**
     * 入组福利券表id
     */
    private Long drugWelfareGroupCouponId;

    /**
     * b2b优惠券id
     */
    private Long couponId;


    /**
     * 核销时间
     */
    private Date verificationTime;





}
