package com.yiling.user.member.bo;

import java.math.BigDecimal;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-会员收银台信息 BO
 * </p>
 *
 * @author lun.yu
 * @date 2022-10-28
 */
@Data
@Accessors(chain = true)
public class MemberCheckStandBO {

    /**
     * 推广方名称
     */
    private String promoterName;

    /**
     * 推广方人名称
     */
    private String promoterUserName;

    /**
     * 原价
     */
    private BigDecimal originalPrice;

    /**
     * 优惠金额
     */
    private BigDecimal discountAmount;

    /**
     * 支付金额
     */
    private BigDecimal payAmount;

}
