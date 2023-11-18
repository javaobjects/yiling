package com.yiling.hmc.welfare.dto;

import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 入组券包DTO
 * @author fan.shen
 * @date 2022-09-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DrugWelfareGroupCouponDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 药品福利入组id
     */
    private Long groupId;

    /**
     * 药品福利id
     */
    private Long drugWelfareId;

    /**
     * 福利券id
     */
    private Long drugWelfareCouponId;

    /**
     * 福利券码
     */
    private String couponCode;

    /**
     * 福利券状态 1-待激活，2-已激活，3-已使用
     */
    private Integer couponStatus;

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

    /**
     * 激活时间
     */
    private Date activeTime;

    /**
     * 核销时间
     */
    private Date verifyTime;
}
