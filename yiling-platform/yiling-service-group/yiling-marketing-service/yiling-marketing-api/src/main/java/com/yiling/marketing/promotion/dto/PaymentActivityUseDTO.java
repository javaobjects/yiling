package com.yiling.marketing.promotion.dto;

import java.util.List;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * 支付促销活动
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PaymentActivityUseDTO extends BaseDTO {

    /**
     * 平台优惠列表
     */
    private List<PaymentActivityUseDetailDTO> platformList;

    /**
     * 商家优惠列表
     */
    private List<PaymentActivityUseDetailDTO> businessList;

}
