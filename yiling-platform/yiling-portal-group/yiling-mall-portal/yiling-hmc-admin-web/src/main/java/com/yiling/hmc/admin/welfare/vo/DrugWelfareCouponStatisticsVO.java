package com.yiling.hmc.admin.welfare.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: benben.jia
 * @data: 2022/10/08
 */
@Data
public class DrugWelfareCouponStatisticsVO extends BaseVO {


    @ApiModelProperty("用药福利计划id")
    private Long drugWelfareId;

    @ApiModelProperty("用药福利计划名称")
    private String drugWelfareName;

    @ApiModelProperty("要求达到的数量,满几盒")
    private Long requirementNumber;

    @ApiModelProperty("赠送数量,赠几盒")
    private Long giveNumber;

    @ApiModelProperty("福利券码")
    private String couponCode;

    @ApiModelProperty("使用状态 1-待激活，2-已激活，3-已核销")
    private Integer couponStatus;

    @ApiModelProperty("激活时间")
    private Date activeTime;

    @ApiModelProperty("核销人")
    private String verificationName;

    @ApiModelProperty("核销时间")
    private Date verifyTime;
}
