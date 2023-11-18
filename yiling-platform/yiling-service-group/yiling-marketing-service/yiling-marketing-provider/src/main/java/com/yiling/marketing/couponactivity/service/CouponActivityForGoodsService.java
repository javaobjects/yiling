package com.yiling.marketing.couponactivity.service;

import java.util.List;
import java.util.Map;

import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.couponactivity.dto.CouponActivityCanAndOwnDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityEidOrGoodsIdDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityListFiveByGoodsIdDTO;
import com.yiling.marketing.couponactivity.dto.GetCouponActivityResultDTO;
import com.yiling.marketing.couponactivity.dto.request.CouponActivityExistFlagRequest;
import com.yiling.marketing.couponactivity.dto.request.CouponActivityReceiveRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityCanReceiveRequest;
import com.yiling.marketing.couponactivity.entity.CouponActivityDO;

/**
 * @author: houjie.sun
 * @date: 2021/11/8
 */

public interface CouponActivityForGoodsService extends BaseService<CouponActivityDO> {

    // ********************************************** 移动端-商品详情 ****************************************
    /**
     * 根据商品id、企业id 查询优惠券活动列表
     * 最多返回5个
     * @param goodsId 商品id
     * @param eid 店铺企业id
     * @param limit 查询数量0 <limit<= 100
     * @return
     */
    List<CouponActivityListFiveByGoodsIdDTO> getListFiveByGoodsIdAndEid(Long goodsId, Long eid, Integer limit, Integer platformType);


    /**
     * 根据商品id、企业id 查询可领取、已领取优惠券活动列表
     * @return
     */
    CouponActivityCanAndOwnDTO getCanAndOwnListByEid(QueryCouponActivityCanReceiveRequest request);


    /**
     * 根据商品id、企业id 查询所有参与活动的商品列表
     * @param goodsId 商品id
     * @param eid 店铺企业id
     * @return
     */
    CouponActivityEidOrGoodsIdDTO getGoodsListByGoodsIdAndEid(Long goodsId, Long eid);

    /**
     * 移动端-商品详情、店铺详情 自主领取
     * 根据优惠券活动id领取
     * @param request
     * @return
     */
    Boolean receiveByCouponActivityId(CouponActivityReceiveRequest request);

    /**
     * 根据企业ID、商品ID 查询是否存在优惠券活动
     * @param request
     * @return
     */
    Map<Long, List<Integer>> getCouponActivityExistFlag(CouponActivityExistFlagRequest request);

    /**
     * 移动端-商品详情、店铺详情 自主领取
     * 根据优惠券活动id领取
     * @param request
     * @return
     */
    GetCouponActivityResultDTO receiveByCouponActivityIdForApp(CouponActivityReceiveRequest request);

    /**
     * 一键领取某个商家下面的优惠券
     *
     * @param request
     * @return
     */
    Boolean oneKeyReceive(QueryCouponActivityCanReceiveRequest request);

    // ********************************************** 移动端-商品详情 ****************************************


}
