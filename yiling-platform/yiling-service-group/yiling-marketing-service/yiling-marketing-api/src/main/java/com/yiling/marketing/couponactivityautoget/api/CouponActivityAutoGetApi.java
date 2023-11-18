package com.yiling.marketing.couponactivityautoget.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.marketing.couponactivity.dto.CouponActivityGiveDTO;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityGivePageRequest;
import com.yiling.marketing.couponactivityautoget.dto.CouponActivityAutoGetDTO;
import com.yiling.marketing.couponactivityautoget.dto.CouponActivityAutoGetDetailDTO;
import com.yiling.marketing.couponactivityautoget.dto.CouponActivityAutoGetPageDTO;
import com.yiling.marketing.couponactivityautoget.dto.request.CouponActivityAutoGetOperateRequest;
import com.yiling.marketing.couponactivityautoget.dto.request.QueryCouponActivityAutoGetRequest;
import com.yiling.marketing.couponactivityautoget.dto.request.SaveCouponActivityAutoGetBasicRequest;
import com.yiling.marketing.couponactivityautoget.dto.request.SaveCouponActivityAutoGetRulesRequest;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGetCouponDTO;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveMemberDTO;
import com.yiling.marketing.couponactivityautogive.dto.request.DeleteCouponActivityAutoGetMemberLimitRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.QueryCouponActivityAutoGetMemberLimitRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.SaveCouponActivityAutoGetMemberLimitRequest;

/**
 * 自动领取活动 Api
 *
 * @author: houjie.sun
 * @date: 2021/10/26
 */
public interface CouponActivityAutoGetApi {

    /**
     * 自动发放优惠券活动分页
     *
     * @param request
     * @return
     */
    Page<CouponActivityAutoGetPageDTO> queryListPage(QueryCouponActivityAutoGetRequest request);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    CouponActivityAutoGetDetailDTO getDetailById(Long id);

    /**
     * 保存/修改自主领券基本信息
     *
     * @param request
     * @return
     */
    Long saveOrUpdateBasic(SaveCouponActivityAutoGetBasicRequest request);

    /**
     * 保存/修改自动发券基本信息
     *
     * @param request
     * @return
     */
    Long saveOrUpdateRules(SaveCouponActivityAutoGetRulesRequest request);

    /**
     * 根据ID停用自动发券活动
     *
     * @param request
     * @return
     */
    Boolean stop(CouponActivityAutoGetOperateRequest request);

    /**
     * 根据ID启用自动发券活动
     *
     * @param request
     * @return
     */
    Boolean enable(CouponActivityAutoGetOperateRequest request);

    /**
     * 根据ID废弃自动发券活动
     *
     * @param request
     * @return
     */
    Boolean scrap(CouponActivityAutoGetOperateRequest request);

    /**
     * 已领取优惠券分页列表
     *
     * @param request
     * @return
     */
    Page<CouponActivityGiveDTO> queryGiveListPage(QueryCouponActivityGivePageRequest request);

    /**
     * 根据ID查询 优惠券自动领取活动
     *
     * @param id
     * @return
     */
    CouponActivityAutoGetDTO getAutoGetById(Long id);

    /**
     * 根据ID查列表询 优惠券自动领取活动列表
     *
     * @param idList
     * @return
     */
    List<CouponActivityAutoGetDTO> getAutoGetByIdList(List<Long> idList);

    /**
     * 根据优惠券活动ID查询 优惠券自动发放活动关联信息
     *
     * @param couponActivityId
     * @return
     */
    CouponActivityAutoGetCouponDTO getAutoGetCouponByCouponActivityId(Long couponActivityId);

    /**
     * 根据优惠券活动ID列表查询 优惠券自动发放活动关联信息列表
     *
     * @param couponActivityIdList
     * @return
     */
    List<CouponActivityAutoGetCouponDTO> getAutoGetCouponByCouponActivityIdList(List<Long> couponActivityIdList);

    /**
     * 根据领券活动ID查询 优惠券自动发放活动关联信息
     *
     * @param couponActivityGetId
     * @return
     */
    List<CouponActivityAutoGetCouponDTO> getByCouponActivityGetId(Long couponActivityGetId);

    /**
     * 查询会员信息列表
     *
     * @param request
     * @return
     */
    Page<CouponActivityAutoGiveMemberDTO> queryEnterpriseLimitListPage(QueryCouponActivityAutoGetMemberLimitRequest request);

    /**
     * 查询会员信息列表
     *
     * @param request
     * @return
     */
    Page<CouponActivityAutoGiveMemberDTO> queryPromotionListPage(QueryCouponActivityAutoGetMemberLimitRequest request);

    /**
     * 添加企业信息
     *
     * @param request
     * @return
     */
    Boolean saveEnterpriseLimit(SaveCouponActivityAutoGetMemberLimitRequest request);

    /**
     * 添加企业信息
     *
     * @param request
     * @return
     */
    Boolean savePromotionEnterpriseLimit(SaveCouponActivityAutoGetMemberLimitRequest request);

    /**
     * 删除已添加会员企业
     *
     * @param request
     * @return
     */
    Boolean deleteEnterpriseLimit(DeleteCouponActivityAutoGetMemberLimitRequest request);

    /**
     * 删除已添加会员企业
     *
     * @param request
     * @return
     */
    Boolean deletePromotionEnterpriseLimit(DeleteCouponActivityAutoGetMemberLimitRequest request);

    /**
     * 根据领券活动IDs查询 优惠券自动发放活动关联信息
     *
     * @param couponActivityGetIds
     * @return
     */
    List<CouponActivityAutoGetCouponDTO> getByCouponActivityGetIdList(List<Long> couponActivityGetIds);

}
