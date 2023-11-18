package com.yiling.marketing.couponactivityautoget.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.coupon.service.CouponService;
import com.yiling.marketing.couponactivity.dto.CouponActivityGiveDTO;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityGivePageRequest;
import com.yiling.marketing.couponactivity.service.CouponActivityService;
import com.yiling.marketing.couponactivityautoget.api.CouponActivityAutoGetApi;
import com.yiling.marketing.couponactivityautoget.dto.CouponActivityAutoGetDTO;
import com.yiling.marketing.couponactivityautoget.dto.CouponActivityAutoGetDetailDTO;
import com.yiling.marketing.couponactivityautoget.dto.CouponActivityAutoGetPageDTO;
import com.yiling.marketing.couponactivityautoget.dto.request.CouponActivityAutoGetOperateRequest;
import com.yiling.marketing.couponactivityautoget.dto.request.QueryCouponActivityAutoGetRequest;
import com.yiling.marketing.couponactivityautoget.dto.request.SaveCouponActivityAutoGetBasicRequest;
import com.yiling.marketing.couponactivityautoget.dto.request.SaveCouponActivityAutoGetRulesRequest;
import com.yiling.marketing.couponactivityautoget.entity.CouponActivityAutoGetCouponDO;
import com.yiling.marketing.couponactivityautoget.entity.CouponActivityAutoGetEnterpriseLimitDO;
import com.yiling.marketing.couponactivityautoget.entity.CouponActivityAutoGetPromotionLimitDO;
import com.yiling.marketing.couponactivityautoget.service.CouponActivityAutoGetCouponService;
import com.yiling.marketing.couponactivityautoget.service.CouponActivityAutoGetEnterpriseLimitService;
import com.yiling.marketing.couponactivityautoget.service.CouponActivityAutoGetPromotionLimitService;
import com.yiling.marketing.couponactivityautoget.service.CouponActivityAutoGetService;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGetCouponDTO;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveMemberDTO;
import com.yiling.marketing.couponactivityautogive.dto.request.DeleteCouponActivityAutoGetMemberLimitRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.QueryCouponActivityAutoGetMemberLimitRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.SaveCouponActivityAutoGetMemberLimitRequest;

/**
 * @author: houjie.sun
 * @date: 2021/10/26
 */
@DubboService
public class CouponActivityAutoGetApiImpl implements CouponActivityAutoGetApi {

    @Autowired
    private CouponActivityAutoGetService                autoGetService;
    @Autowired
    private CouponActivityAutoGetCouponService          autoGetCouponService;
    @Autowired
    private CouponService                               couponService;
    @Autowired
    private CouponActivityService                       couponActivityService;
    @Autowired
    private CouponActivityAutoGetEnterpriseLimitService autoGetEnterpriseLimitService;
    @Autowired
    private CouponActivityAutoGetPromotionLimitService couponActivityAutoGetPromotionLimitService;

    @Override
    public Page<CouponActivityAutoGetPageDTO> queryListPage(QueryCouponActivityAutoGetRequest request) {
        return PojoUtils.map(autoGetService.queryListPage(request), CouponActivityAutoGetPageDTO.class);
    }

    @Override
    public CouponActivityAutoGetDetailDTO getDetailById(Long id) {
        return autoGetService.getDetailById(id);
    }

    @Override
    public Long saveOrUpdateBasic(SaveCouponActivityAutoGetBasicRequest request) {
        return autoGetService.saveOrUpdateBasic(request);
    }

    @Override
    public Long saveOrUpdateRules(SaveCouponActivityAutoGetRulesRequest request) {
        return autoGetService.saveOrUpdateRules(request);
    }

    @Override
    public Boolean stop(CouponActivityAutoGetOperateRequest request) {
        return autoGetService.stop(request);
    }

    @Override
    public Boolean enable(CouponActivityAutoGetOperateRequest request) {
        return autoGetService.enable(request);
    }

    @Override
    public Boolean scrap(CouponActivityAutoGetOperateRequest request) {
        return autoGetService.scrap(request);
    }

    @Override
    public Page<CouponActivityGiveDTO> queryGiveListPage(QueryCouponActivityGivePageRequest request) {
        return couponActivityService.queryGiveListPage(request);
    }

    @Override
    public CouponActivityAutoGetDTO getAutoGetById(Long couponActivityId) {
        return autoGetService.getAutoGetById(couponActivityId);
    }

    @Override
    public List<CouponActivityAutoGetDTO> getAutoGetByIdList(List<Long> idList) {
        return autoGetService.getAutoGetByIdList(idList);
    }

    @Override
    public CouponActivityAutoGetCouponDTO getAutoGetCouponByCouponActivityId(Long couponActivityId) {
        return autoGetCouponService.getByCouponActivityId(couponActivityId);
    }

    @Override
    public List<CouponActivityAutoGetCouponDTO> getAutoGetCouponByCouponActivityIdList(List<Long> couponActivityIdList) {
        return autoGetCouponService.getByCouponActivityIdList(couponActivityIdList);
    }

    @Override
    public List<CouponActivityAutoGetCouponDTO> getByCouponActivityGetId(Long couponActivityGetId) {
        List<CouponActivityAutoGetCouponDO> list = autoGetCouponService.getByCouponActivityGetId(couponActivityGetId);
        return PojoUtils.map(list, CouponActivityAutoGetCouponDTO.class);
    }

    @Override
    public Page<CouponActivityAutoGiveMemberDTO> queryEnterpriseLimitListPage(QueryCouponActivityAutoGetMemberLimitRequest request) {
        Page<CouponActivityAutoGetEnterpriseLimitDO> page = autoGetEnterpriseLimitService.queryEnterpriseLimitListPage(request);
        return PojoUtils.map(page, CouponActivityAutoGiveMemberDTO.class);
    }
    @Override
    public Page<CouponActivityAutoGiveMemberDTO> queryPromotionListPage(QueryCouponActivityAutoGetMemberLimitRequest request) {
        Page<CouponActivityAutoGetPromotionLimitDO> page = autoGetEnterpriseLimitService.queryPromotionListPage(request);
        return PojoUtils.map(page, CouponActivityAutoGiveMemberDTO.class);
    }

    @Override
    public Boolean saveEnterpriseLimit(SaveCouponActivityAutoGetMemberLimitRequest request) {
        return autoGetEnterpriseLimitService.saveEnterpriseLimit(request);
    }

    @Override
    public Boolean savePromotionEnterpriseLimit(SaveCouponActivityAutoGetMemberLimitRequest request) {
        return autoGetEnterpriseLimitService.savePromotionEnterpriseLimit(request);
    }

    @Override
    public Boolean deleteEnterpriseLimit(DeleteCouponActivityAutoGetMemberLimitRequest request) {
        return autoGetEnterpriseLimitService.deleteEnterpriseLimit(request);
    }

    @Override
    public Boolean deletePromotionEnterpriseLimit(DeleteCouponActivityAutoGetMemberLimitRequest request) {
        return autoGetEnterpriseLimitService.deletePromotionEnterpriseLimit(request);
    }

    @Override
    public List<CouponActivityAutoGetCouponDTO> getByCouponActivityGetIdList(List<Long> couponActivityGetIds) {
        List<CouponActivityAutoGetCouponDO> list = autoGetCouponService.getByCouponActivityGetIdList(couponActivityGetIds);
        return PojoUtils.map(list, CouponActivityAutoGetCouponDTO.class);
    }

}
