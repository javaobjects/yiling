package com.yiling.hmc.welfare.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 *
 * @author fan.shen
 * @date 2022-09-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DrugWelfareCouponDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

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
