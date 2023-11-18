package com.yiling.marketing.couponactivity.dto;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * B2B-优惠券详情DTO
 *
 * @author: houjie.sun
 * @date: 2021/10/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CouponMemberActivityRuleDTO extends BaseDTO {

    /**
     * 类型（1-满减券；2-满折券）
     */
    private Integer type;


    /**
     * 优惠内容 满折95 九五折，满减95 减少95元
     */
    private BigDecimal discountValue;

    /**
     * 最高优惠额度（折扣券，保存不能输入0、可以输入空，表数据默认值0表示不限制最高优惠额度）
     */
    private BigDecimal discountMax;
}
