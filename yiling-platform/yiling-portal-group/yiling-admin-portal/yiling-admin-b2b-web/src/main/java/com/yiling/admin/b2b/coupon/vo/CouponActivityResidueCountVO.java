package com.yiling.admin.b2b.coupon.vo;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/8
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CouponActivityResidueCountVO extends BaseVO {

    /**
     * 剩余数量
     */
    @ApiModelProperty(value = "剩余数量")
    private Integer residueCount;

    /**
     * 供应商限制（1-全部供应商；2-部分供应商）
     */
    @ApiModelProperty(value = "供应商限制（1-全部供应商；2-部分供应商）")
    @NotNull
    private Integer enterpriseLimit;

}
