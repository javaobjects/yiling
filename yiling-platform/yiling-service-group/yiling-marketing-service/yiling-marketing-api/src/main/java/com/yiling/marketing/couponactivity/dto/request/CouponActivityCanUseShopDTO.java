package com.yiling.marketing.couponactivity.dto.request;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/10/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CouponActivityCanUseShopDTO extends BaseDTO {

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 优惠券产品表关联的eid
     */
    private Long caglEid;

    /**
     * 优惠券企业表关联的eid
     */
    private Long caelEid;

    /**
     * 产品限制1 全部产品，2部分产品
     */
    private Integer goodsLimit;

    /**
     * 企业限制1 全部，2部分企业目前都是部分企业
     */
    private Integer enterpriseLimit;

    /**
     * -1 商家后台 1运营后台
     */
    private Integer platform;

    /**
     *  指定用户类型（1-全部用户；2-仅普通用户；3-全部会员用户；4-部分指定会员;5-新客
     */
    private Integer userType;

    /**
     * 优惠券一共发放多少张券
     */
    private Integer totalCount;

    /**
     *  指定用户类型（1-全部用户；2-指定用户；3-按照条件
     */
    private Integer enterpriseRang;
}
