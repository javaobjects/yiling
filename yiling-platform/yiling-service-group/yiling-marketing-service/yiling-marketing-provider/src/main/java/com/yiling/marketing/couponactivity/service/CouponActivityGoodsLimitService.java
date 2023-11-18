package com.yiling.marketing.couponactivity.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.couponactivity.dto.CouponActivityGoodsDTO;
import com.yiling.marketing.couponactivity.dto.request.DeleteCouponActivityGoodsLimitRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityGoodsRequest;
import com.yiling.marketing.couponactivity.dto.request.SaveCouponActivityGoodsLimitRequest;
import com.yiling.marketing.couponactivity.entity.CouponActivityGoodsLimitDO;

/**
 * <p>
 * 优惠券活动商品限制表 服务类
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
public interface CouponActivityGoodsLimitService extends BaseService<CouponActivityGoodsLimitDO> {

    Boolean insertBatch(List<CouponActivityGoodsLimitDO> list);

    /**
     * 查询商品信息列表
     * @param request
     * @return
     */
//    Page<CouponActivityGoodsDTO> queryGoodsListPage(QueryCouponActivityGoodsRequest request);

    /**
     * 查询已添加商品信息列表
     * @param request
     * @return
     */
    Page<CouponActivityGoodsDTO> pageList(QueryCouponActivityGoodsRequest request);

    /**
     * 组装已添加商品信息列表分页
     * @param page
     * @return
     */
//    void buildGoodsLimitPage(Page<CouponActivityGoodsDTO> page);

    /**
     * 添加商品信息
     * @param request
     * @return
     */
    Boolean saveGoodsLimit(SaveCouponActivityGoodsLimitRequest request);

    /**
     * 删除已添加商品信息
     * @param request
     * @return
     */
    Boolean deleteGoodsLimit(DeleteCouponActivityGoodsLimitRequest request);

    /**
     * 根据企业id、商品id查询优惠券活动关系列表
     * @return
     */
    List<CouponActivityGoodsLimitDO> getListByGoodsIdAndEid(Long goodsId, Long eid);

    /**
     * 根据企业id、商品id列表 查询优惠券活动关系列表
     * @return
     */
    List<CouponActivityGoodsLimitDO> getListByGoodsIdAndEidList(List<Long> eidList,List<Long> goodsIdList);

    /**
     * 根据优惠券活动ids查询可使用商品关系列表
     * @param couponActivityIds 优惠券活动id
     * @return
     */
    List<CouponActivityGoodsLimitDO> getListByCouponActivityIdList(List<Long> couponActivityIds);

    /**
     * 根据优惠券活动ids查询可使用商品关系列表
     * @param couponActivityIds 优惠券活动id和商品id
     * @return
     */
    List<CouponActivityGoodsLimitDO> getListByCouponActivityIdAndGoodsId(List<Long> couponActivityIds,List<Long>goodsIds);

    /**
     * 根据优惠券活动id查询可使用商品关系列表
     * @param couponActivityId 优惠券活动id
     * @return
     */
    List<CouponActivityGoodsLimitDO> getByCouponActivityId(Long couponActivityId);

    /**
     * 根据优惠券活动id删除
     * @param request
     * @return
     */
    Integer deleteByCouponActivityId(DeleteCouponActivityGoodsLimitRequest request);

    /**
     * 根据优惠券活动id批量保存
     * @param request
     * @return
     */
    Boolean batchSaveGoodsLimit(SaveCouponActivityGoodsLimitRequest request);

    /**
     * 根据优惠券活动id查询上坪限制id
     * @param request
     * @return
     */
    List<Long> queryGoodsLimitList(QueryCouponActivityGoodsRequest request);
}
