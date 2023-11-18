package com.yiling.hmc.welfare.dto.request;


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
public class DrugWelfareCouponUpdateRequest extends BaseRequest {


    /**
     * 药品福利券包id
     */
    private Long id;

    /**
     * 药品福利id
     */
    private Long drugWelfareId;

    /**
     * 要求达到的数量,满几盒
     */
    private Long requirementNumber;

    /**
     * 赠送数量,赠几盒
     */
    private Long giveNumber;

    /**
     * b2b优惠券id
     */
    private Long couponId;


}
