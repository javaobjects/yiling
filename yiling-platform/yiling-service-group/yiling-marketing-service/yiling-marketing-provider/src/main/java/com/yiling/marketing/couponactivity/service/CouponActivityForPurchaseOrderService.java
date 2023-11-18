package com.yiling.marketing.couponactivity.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.coupon.dto.request.QueryCouponCanUseListRequest;
import com.yiling.marketing.coupon.dto.request.UseMemberCouponRequest;
import com.yiling.marketing.couponactivity.dto.CouponActivityCanUseDTO;
import com.yiling.marketing.couponactivity.dto.OrderUseCouponBudgetDTO;
import com.yiling.marketing.couponactivity.dto.request.OrderUseCouponBudgetRequest;
import com.yiling.marketing.couponactivity.dto.request.OrderUseCouponRequest;
import com.yiling.marketing.couponactivity.entity.CouponActivityDO;

/**
 * @author: houjie.sun
 * @date: 2021/11/11
 */
public interface CouponActivityForPurchaseOrderService extends BaseService<CouponActivityDO> {

    /**
     * 获取支持使用优惠券的支付方式
     * @return
     */
    List<Integer> suportCouponPayMethodList();

    /**
     * 根据企业id、购买商品id、小计金额 查询已领取可使用优惠券
     * @param request
     * @return
     */
    CouponActivityCanUseDTO getCouponCanUseList(QueryCouponCanUseListRequest request);

    /**
     * 根据企业id、优惠券ID、商品明细 计算商品分摊优惠金额
     * 分摊维度到商品SKU
     * @param request
     * @return
     */
    OrderUseCouponBudgetDTO orderUseCouponShareAmountBudget(OrderUseCouponBudgetRequest request);

    /**
     * 进货单结算 使用优惠券
     * @param request
     * @return
     */
    Boolean orderUseCoupon(OrderUseCouponRequest request);

    /**
     * 购买会员，使用会员优惠券
     * @param request
     * @return
     */
    Boolean useMemberCoupon(UseMemberCouponRequest request);
}
