package com.yiling.admin.hmc.welfare.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: benben.jia
 * @data: 2022/09/26
 */
@Data
public class DrugWelfareCouponVO extends BaseVO {

    /**
     * 药品福利券包id
     */
    @ApiModelProperty("药品福利券包id")
    private Long drugWelfareId;

    /**
     * 要求达到的数量,满几盒
     */
    @ApiModelProperty("要求达到的数量,满几盒")
    private Long requirementNumber;

    /**
     * 赠送数量,赠几盒
     */
    @ApiModelProperty("赠送数量,赠几盒")
    private Long giveNumber;

    /**
     * b2b优惠券id
     */
    @ApiModelProperty("b2b优惠券id")
    private Long couponId;


}
