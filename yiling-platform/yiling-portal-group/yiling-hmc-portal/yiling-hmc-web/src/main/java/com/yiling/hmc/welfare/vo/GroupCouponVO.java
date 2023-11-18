package com.yiling.hmc.welfare.vo;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 药品福利券包
 * @author fan.shen
 * @date 2022-09-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GroupCouponVO extends BaseVO {

    private static final long serialVersionUID = 1L;


    /**
     * 药品福利入组id
     */
    private Long groupId;

    /**
     * 药品福利id
     */
    @ApiModelProperty("药品福利id")
    private Long drugWelfareId;

    /**
     * 福利券码
     */
    @ApiModelProperty("福利券码")
    private String couponCode;

    /**
     * 福利券状态 1-待激活，2-已激活，3-已使用
     */
    @ApiModelProperty("福利券状态 1-未激活，2-已激活，3-已核销")
    private Integer couponStatus;

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
     * 激活时间
     */
    @ApiModelProperty("激活时间")
    private Date activeTime;

    /**
     * 核销时间
     */
    @ApiModelProperty("核销时间")
    private Date verifyTime;

}
