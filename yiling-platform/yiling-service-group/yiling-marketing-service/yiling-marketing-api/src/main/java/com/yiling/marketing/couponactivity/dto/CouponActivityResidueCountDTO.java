package com.yiling.marketing.couponactivity.dto;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CouponActivityResidueCountDTO extends BaseDTO {

    /**
     * 剩余数量
     */
    private Integer residueCount;

    /**
     * 供应商限制（1-全部供应商；2-部分供应商）
     */
    @NotNull
    private Integer enterpriseLimit;

}
