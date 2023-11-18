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
public class DrugWelfareCouponVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    /**
     * 药品福利id
     */
    @ApiModelProperty("药品福利id")
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
