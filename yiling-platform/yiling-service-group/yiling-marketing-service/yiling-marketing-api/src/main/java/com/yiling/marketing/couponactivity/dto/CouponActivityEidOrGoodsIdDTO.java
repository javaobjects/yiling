package com.yiling.marketing.couponactivity.dto;

import java.util.List;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/9
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CouponActivityEidOrGoodsIdDTO extends BaseDTO {

    // 企业id、商品id 为或的关系,查询商品库商品

    /**
     * 是否所有企业商品（true-是 false-否）
     */
    private Boolean allEidFlag = false;

    /**
     * 企业id
     */
    private List<Long> eidList;

    /**
     * 商品id
     */
    private List<Long> goodsIdList;

    /**
     * 优惠券不可用原因
     */
    private String notAvailableMessage;
}
