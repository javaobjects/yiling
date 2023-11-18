package com.yiling.marketing.couponactivity.api.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.common.enums.CouponActivityGiveRecordStatusEnum;
import com.yiling.marketing.common.enums.CouponErrorCode;
import com.yiling.marketing.common.enums.CouponGetTypeEnum;
import com.yiling.marketing.common.enums.CouponStatusEnum;
import com.yiling.marketing.common.enums.CouponUsedStatusEnum;
import com.yiling.marketing.common.util.CouponUtil;
import com.yiling.marketing.coupon.dto.CouponDTO;
import com.yiling.marketing.coupon.dto.request.QueryCouponCanUseListRequest;
import com.yiling.marketing.coupon.dto.request.SaveCouponRequest;
import com.yiling.marketing.coupon.dto.request.UseMemberCouponRequest;
import com.yiling.marketing.coupon.entity.CouponDO;
import com.yiling.marketing.coupon.service.CouponService;
import com.yiling.marketing.couponactivity.api.CouponActivityApi;
import com.yiling.marketing.couponactivity.bo.CouponActivityRulesBO;
import com.yiling.marketing.couponactivity.dto.CouponActivityCanAndOwnDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityCanUseDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityCanUseDetailDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityDetailDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityEidOrGoodsIdDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityEnterpDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityEnterpriseDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityEnterpriseGiveDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityEnterpriseLimitDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityGiveDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityGiveEnterpriseInfoDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityGoodsDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityHasGetDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityListFiveByGoodsIdDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityResidueCountDTO;
import com.yiling.marketing.couponactivity.dto.CouponMemberActivityLimitDTO;
import com.yiling.marketing.couponactivity.dto.CouponMemberActivityRuleDTO;
import com.yiling.marketing.couponactivity.dto.GetCouponActivityResultDTO;
import com.yiling.marketing.couponactivity.dto.OrderUseCouponBudgetDTO;
import com.yiling.marketing.couponactivity.dto.request.CouponActivityExistFlagRequest;
import com.yiling.marketing.couponactivity.dto.request.CouponActivityIncreaseRequest;
import com.yiling.marketing.couponactivity.dto.request.CouponActivityOperateRequest;
import com.yiling.marketing.couponactivity.dto.request.CouponActivityReceiveRequest;
import com.yiling.marketing.couponactivity.dto.request.DeleteCouponActivityEnterpriseGiveRecordRequest;
import com.yiling.marketing.couponactivity.dto.request.DeleteCouponActivityEnterpriseLimitRequest;
import com.yiling.marketing.couponactivity.dto.request.DeleteCouponActivityGoodsLimitRequest;
import com.yiling.marketing.couponactivity.dto.request.OrderUseCouponBudgetRequest;
import com.yiling.marketing.couponactivity.dto.request.OrderUseCouponRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryActivityDetailRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityCanReceivePageRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityCanReceiveRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityDetailRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityEnterpriseGiveRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityEnterpriseLimitRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityEnterpriseRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityGiveEnterpriseInfoRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityGivePageRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityGoodsRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponMemberActivityPageRequest;
import com.yiling.marketing.couponactivity.dto.request.SaveCouponActivityBasicRequest;
import com.yiling.marketing.couponactivity.dto.request.SaveCouponActivityEnterpriseLimitRequest;
import com.yiling.marketing.couponactivity.dto.request.SaveCouponActivityGiveEnterpriseInfoRequest;
import com.yiling.marketing.couponactivity.dto.request.SaveCouponActivityGoodsLimitRequest;
import com.yiling.marketing.couponactivity.dto.request.SaveCouponActivityRulesRequest;
import com.yiling.marketing.couponactivity.entity.CouponActivityDO;
import com.yiling.marketing.couponactivity.entity.CouponActivityEnterpriseLimitDO;
import com.yiling.marketing.couponactivity.entity.CouponActivityMemberLimitDO;
import com.yiling.marketing.couponactivity.service.CouponActivityEnterpriseLimitService;
import com.yiling.marketing.couponactivity.service.CouponActivityForEnterpriseService;
import com.yiling.marketing.couponactivity.service.CouponActivityForGoodsService;
import com.yiling.marketing.couponactivity.service.CouponActivityForPurchaseOrderService;
import com.yiling.marketing.couponactivity.service.CouponActivityGoodsLimitService;
import com.yiling.marketing.couponactivity.service.CouponActivityLogService;
import com.yiling.marketing.couponactivity.service.CouponActivityMemberLimitService;
import com.yiling.marketing.couponactivityautoget.service.CouponActivityAutoGetMemberLimitService;
import com.yiling.marketing.couponactivity.service.CouponActivityService;
import com.yiling.marketing.couponactivityautoget.entity.CouponActivityAutoGetMemberLimitDO;
import com.yiling.marketing.couponactivityautogive.entity.CouponActivityAutoGiveEnterpriseInfoDO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;


/**
 * @author: houjie.sun
 * @date: 2021/10/25
 */
@DubboService
public class CouponActivityApiImpl implements CouponActivityApi {

    @Autowired
    private CouponActivityService                       couponActivityService;
    @Autowired
    private CouponActivityLogService                    couponActivityLogService;
    @Autowired
    private CouponActivityEnterpriseLimitService        couponActivityEnterpriseLimitService;
    @Autowired
    private CouponActivityGoodsLimitService             couponActivityGoodsLimitService;
    @Autowired
    private CouponService                               couponService;
    @Autowired
    private CouponActivityForGoodsService               couponActivityForGoodsService;
    @Autowired
    private CouponActivityForEnterpriseService          couponActivityForEnterpriseService;
    @Autowired
    private CouponActivityForPurchaseOrderService       couponActivityForPurchaseOrderService;
    @Autowired
    private CouponActivityMemberLimitService couponActivityMemberLimitService;
    @Autowired
    private CouponActivityAutoGetMemberLimitService couponActivityAutoGetMemberLimitService;

    @Override
    public Page<CouponActivityDTO> queryListPage(QueryCouponActivityRequest request) {
        return couponActivityService.queryListPage(request);
    }

    @Override
    public Page<CouponActivityDTO> queryListForMarketing(QueryCouponActivityRequest request) {
        return couponActivityService.queryListForMarketing(request);
    }

    @Override
    public CouponActivityDetailDTO getCouponActivityById(QueryCouponActivityDetailRequest request) {
        return couponActivityService.getCouponActivityById(request);
    }

    @Override
    public CouponActivityDetailDTO copy(CouponActivityOperateRequest request) {
        return couponActivityService.copy(request);
    }

    @Override
    public Boolean stop(CouponActivityOperateRequest request) {
        return couponActivityService.stop(request);
    }

    @Override
    public Boolean scrap(CouponActivityOperateRequest request) {
        return couponActivityService.scrap(request);
    }

    @Override
    public List<Long> scrapAndReturnCoupon(CouponActivityOperateRequest request) {
        return couponActivityService.scrapAndReturnCoupon(request);
    }

    @Override
    public Boolean increase(CouponActivityIncreaseRequest request) {
        return couponActivityService.increase(request);
    }

    @Override
    public Long saveOrUpdateBasic(SaveCouponActivityBasicRequest request) {
        return couponActivityService.saveOrUpdateBasic(request);
    }

    @Override
    public Long saveOrUpdateBasicForMember(SaveCouponActivityBasicRequest request) {
        return couponActivityService.saveOrUpdateBasicForMember(request);
    }

    @Override
    public Long saveOrUpdateRules(SaveCouponActivityRulesRequest request) {
        return couponActivityService.saveOrUpdateRules(request);
    }

    @Override
    public Boolean saveCouponActivityLog(Long CouponActivityId, Integer type, String content, Integer status, String faileReason, Long createUser) {
        return couponActivityLogService.insertCouponActivityLog(CouponActivityId, type, content, status, faileReason, createUser);
    }

    @Override
    public Page<CouponActivityEnterpriseDTO> queryEnterpriseListPage(QueryCouponActivityEnterpriseRequest request) {
        return couponActivityEnterpriseLimitService.queryEnterpriseListPage(request);
    }

    @Override
    public Page<CouponActivityEnterpriseLimitDTO> queryEnterpriseLimitListPage(QueryCouponActivityEnterpriseLimitRequest request) {
        return couponActivityEnterpriseLimitService.pageList(request);
    }

    @Override
    public Boolean saveEnterpriseLimit(SaveCouponActivityEnterpriseLimitRequest request) {
        return couponActivityEnterpriseLimitService.saveEnterpriseLimit(request);
    }

    @Override
    public Boolean deleteEnterpriseLimit(DeleteCouponActivityEnterpriseLimitRequest request) {
        return couponActivityEnterpriseLimitService.deleteEnterpriseLimit(request);
    }

//    @Override
//    public Page<CouponActivityGoodsDTO> queryGoodsListPage(QueryCouponActivityGoodsRequest request) {
//        return couponActivityGoodsLimitService.queryGoodsListPage(request);
//    }

    @Override
    public Page<CouponActivityGoodsDTO> queryGoodsLimitListPage(QueryCouponActivityGoodsRequest request) {
        return couponActivityGoodsLimitService.pageList(request);
    }

    @Override
    public List<Long> queryGoodsLimitList(QueryCouponActivityGoodsRequest request) {
        return couponActivityGoodsLimitService.queryGoodsLimitList(request);
    }

    @Override
    public Boolean saveGoodsLimit(SaveCouponActivityGoodsLimitRequest request) {
        return couponActivityGoodsLimitService.saveGoodsLimit(request);
    }

    @Override
    public Boolean batchSaveGoodsLimit(SaveCouponActivityGoodsLimitRequest request) {
        return couponActivityGoodsLimitService.batchSaveGoodsLimit(request);
    }

    @Override
    public Boolean deleteGoodsLimit(DeleteCouponActivityGoodsLimitRequest request) {
        return couponActivityGoodsLimitService.deleteGoodsLimit(request);
    }

    @Override
    public Page<CouponActivityEnterpriseGiveDTO> queryEnterpriseGiveListPage(QueryCouponActivityEnterpriseGiveRequest request) {
        return couponActivityEnterpriseLimitService.queryEnterpriseGiveListPage(request);
    }

    @Override
    public Page<CouponActivityGiveEnterpriseInfoDTO> queryEnterpriseGiveRecordListPage(QueryCouponActivityGiveEnterpriseInfoRequest request) {
        Page<CouponActivityAutoGiveEnterpriseInfoDO> page = couponActivityEnterpriseLimitService.queryEnterpriseGiveRecordListPage(request);
        return PojoUtils.map(page, CouponActivityGiveEnterpriseInfoDTO.class);
    }

    @Override
    public Boolean addGiveEnterpriseInfo(SaveCouponActivityGiveEnterpriseInfoRequest request) {
        return couponActivityEnterpriseLimitService.addGiveEnterpriseInfo(request);
    }

    @Override
    public Boolean deleteEnterpriseGiveRecord(DeleteCouponActivityEnterpriseGiveRecordRequest request) {
        return couponActivityEnterpriseLimitService.deleteEnterpriseGiveRecord(request);
    }

    @Override
    public Page<CouponActivityGiveDTO> queryGiveListPage(QueryCouponActivityGivePageRequest request) {
        return couponActivityService.queryGiveListPage(request);
    }

//    @Override
//    public Page<CouponActivityUsedDTO> queryUsedListPage(QueryCouponActivityDetailRequest request) {
//        return couponActivityService.queryUsedListPage(request);
//    }

    @Override
    public CouponActivityResidueCountDTO getResidueCount(Long couponActivityId) {
        return couponActivityService.getResidueCount(couponActivityId);
    }

    @Override
    public String buildCouponRules(CouponActivityDTO couponActivity) {
        return couponActivityService.buildCouponRules(couponActivity);
    }

    @Override
    public List<CouponActivityDetailDTO> getCouponActivityById(List<Long> ids) {
        return couponActivityService.getCouponActivityById(ids);
    }

    @Override
    public List<CouponDTO> getCouponById(List<Long> ids) {
        return couponActivityService.getCouponById(ids);
    }

    @Override
    public List<CouponActivityDTO> getEffectiveCouponActivityByIdList(List<Long> ids, int totalCountFlag, int statusFlag) {
        List<CouponActivityDO> list = couponActivityService.getEffectiveCouponActivityByIdList(ids, totalCountFlag, statusFlag);
        return PojoUtils.map(list, CouponActivityDTO.class);
    }

    @Override
    public List<CouponActivityEnterpriseLimitDTO> getByCouponActivityIdList(List<Long> couponActivityIdList) {
        List<CouponActivityEnterpriseLimitDO> enterpriseLimitList = couponActivityEnterpriseLimitService.getByCouponActivityIdList(couponActivityIdList);
        return PojoUtils.map(enterpriseLimitList, CouponActivityEnterpriseLimitDTO.class);
    }

    @Override
    public List<CouponActivityHasGetDTO> getCouponActivityListByEid(QueryCouponActivityCanReceiveRequest request) {
        return couponActivityForEnterpriseService.getCouponActivityListByEid(request);
    }

    @Override
    public List<CouponActivityHasGetDTO> getCouponActivityListPageByEid(QueryCouponActivityCanReceivePageRequest request) {
        return couponActivityForEnterpriseService.getCouponActivityListPageByEid(request);
    }

    @Override
    public Page<CouponActivityEnterpDTO> getCouponsCenter(QueryCouponActivityCanReceivePageRequest request) {
        return couponActivityForEnterpriseService.getCouponsCenter(request);
    }

    @Override
    public List<CouponActivityHasGetDTO> getMemberCouponsCenter(QueryCouponActivityCanReceivePageRequest request) {
        return couponActivityForEnterpriseService.getMemberCouponsCenter(request);
    }

    @Override
    public Page<CouponActivityDTO> getPageForGoodsGift(QueryCouponActivityRequest request) {
        Page<CouponActivityDO> pageForGoodsGift = couponActivityService.getPageForGoodsGift(request);
        return PojoUtils.map(pageForGoodsGift, CouponActivityDTO.class);
    }

    @Override
    public String checkCouponActivity(CouponActivityDTO couponActivity, List<Integer> countList, List<Map<String, Long>> giveCountList) {
        return couponActivityService.checkCouponActivity(PojoUtils.map(couponActivity, CouponActivityDO.class), countList, giveCountList);
    }

    @Override
    public List<CouponActivityListFiveByGoodsIdDTO> getListFiveByGoodsIdAndEid(Long goodsId, Long eid, Integer limit, Integer platformType) {
        return couponActivityForGoodsService.getListFiveByGoodsIdAndEid(goodsId, eid, limit, platformType);
    }

    @Override
    public CouponActivityCanAndOwnDTO getCanAndOwnListByEid(QueryCouponActivityCanReceiveRequest request) {
        return couponActivityForGoodsService.getCanAndOwnListByEid(request);
    }

    @Override
    public CouponActivityEidOrGoodsIdDTO getGoodsListByGoodsIdAndEid(Long goodsId, Long eid) {
        return couponActivityForGoodsService.getGoodsListByGoodsIdAndEid(goodsId, eid);
    }

    @Override
    public Boolean receiveByCouponActivityId(CouponActivityReceiveRequest request) {
        return couponActivityForGoodsService.receiveByCouponActivityId(request);
    }

    @Override
    public GetCouponActivityResultDTO receiveByCouponActivityIdForApp(CouponActivityReceiveRequest request) {
        return couponActivityForGoodsService.receiveByCouponActivityIdForApp(request);
    }

    @Override
    public Boolean saveMemberCouponRelation(SaveCouponActivityBasicRequest request) {
        if (CollectionUtils.isEmpty(request.getIds())) {
            return false;
        }
        QueryWrapper<CouponActivityMemberLimitDO> lambdaQueryWrapper = new  QueryWrapper<>();
        lambdaQueryWrapper.lambda().eq(CouponActivityMemberLimitDO::getCouponActivityId,request.getId());
        lambdaQueryWrapper.lambda().eq(CouponActivityMemberLimitDO::getMemberId,request.getIds().get(0));
        List<CouponActivityMemberLimitDO> result = couponActivityMemberLimitService.list(lambdaQueryWrapper);
        if(CollectionUtils.isNotEmpty(result)){
            throw new BusinessException(CouponErrorCode.MEMBER_ACTIVITY_ADD_REPEAT);
        }
        List<CouponActivityMemberLimitDO> couponActivityMemberLimitDOS = new ArrayList<>();
        request.getIds().forEach(item -> {
            CouponActivityMemberLimitDO couponActivityMemberLimitDO = new CouponActivityMemberLimitDO();
            couponActivityMemberLimitDO.setCouponActivityId(request.getId());
            couponActivityMemberLimitDO.setMemberId(item);
            couponActivityMemberLimitDO.setCreateTime(new Date());
            couponActivityMemberLimitDO.setCreateUser(request.getOpUserId());
            couponActivityMemberLimitDO.setUpdateTime(new Date());
            couponActivityMemberLimitDO.setUpdateUser(request.getOpUserId());
            couponActivityMemberLimitDO.setDelFlag(0);
            couponActivityMemberLimitDOS.add(couponActivityMemberLimitDO);
        });
        return couponActivityMemberLimitService.saveBatch(couponActivityMemberLimitDOS);
    }

    @Override
    public Boolean saveAutoGetMemberCouponRelation(SaveCouponActivityBasicRequest request) {
        if (CollectionUtils.isEmpty(request.getIds())) {
            return false;
        }
        List<CouponActivityAutoGetMemberLimitDO> couponActivityMemberLimitDOS = new ArrayList<>();
        request.getIds().forEach(item -> {
            CouponActivityAutoGetMemberLimitDO couponActivityMemberLimitDO = new CouponActivityAutoGetMemberLimitDO();
            couponActivityMemberLimitDO.setCouponActivityAutoGetId(request.getId());
            couponActivityMemberLimitDO.setMemberId(item);
            couponActivityMemberLimitDO.setCreateTime(new Date());
            couponActivityMemberLimitDO.setCreateUser(request.getOpUserId());
            couponActivityMemberLimitDO.setUpdateTime(new Date());
            couponActivityMemberLimitDO.setUpdateUser(request.getOpUserId());
            couponActivityMemberLimitDO.setDelFlag(0);
            couponActivityMemberLimitDOS.add(couponActivityMemberLimitDO);
        });
        return couponActivityAutoGetMemberLimitService.saveBatch(couponActivityMemberLimitDOS);
    }

    @Override
    public Map<Long, Integer> getRemainByActivityIds(List<Long> request) {
        return couponActivityService.getRemainByActivityIds(request);
    }

    @Override
    public Map<Long, CouponActivityDetailDTO> getRemainDtoByActivityIds(List<Long> request) {
        return  couponActivityService.getRemainDtoByActivityIds(request);
    }

    @Override
    public Boolean deleteMemberCouponRelation(SaveCouponActivityBasicRequest request) {
        List<CouponActivityMemberLimitDO> couponActivityMemberLimitDOS = new ArrayList<>();
        if (CollectionUtils.isEmpty(request.getIds())) {
            return false;
        }
        QueryWrapper<CouponActivityMemberLimitDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(CouponActivityMemberLimitDO::getId,request.getIds());
        CouponActivityMemberLimitDO activityMemberLimitDO=new CouponActivityMemberLimitDO();
        activityMemberLimitDO.setOpUserId(request.getOpUserId());
        return couponActivityMemberLimitService.batchDeleteWithFill(activityMemberLimitDO,queryWrapper)>0;
    }

    @Override
    public Boolean deleteAutoGetMemberCouponRelation(SaveCouponActivityBasicRequest request) {
        if (CollectionUtils.isEmpty(request.getIds())) {
            return false;
        }
        QueryWrapper<CouponActivityAutoGetMemberLimitDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(CouponActivityAutoGetMemberLimitDO::getId,request.getIds());
        CouponActivityAutoGetMemberLimitDO activityMemberLimitDO=new CouponActivityAutoGetMemberLimitDO();
        activityMemberLimitDO.setOpUserId(request.getOpUserId());
        return couponActivityAutoGetMemberLimitService.batchDeleteWithFill(activityMemberLimitDO,queryWrapper)>0;
    }

    @Override
    public Boolean oneKeyReceive(QueryCouponActivityCanReceiveRequest request) {
        return couponActivityForGoodsService.oneKeyReceive(request);
    }

    @Override
    public Boolean giveCoupon(List<SaveCouponRequest> requests){
        return couponActivityService.giveCoupon(requests);
    }

    @Override
    public Boolean giveCouponBySingal(SaveCouponRequest requests,CouponActivityDetailDTO couponActivityDetailDTO){
        return couponActivityService.giveCouponBySingal(requests,couponActivityDetailDTO);
    }

    @Override
    public Boolean scrapActivity(CouponActivityOperateRequest request) {
        return couponActivityService.scrapActivity(request);

    }

    @Override
    public Boolean updateCoupon(List<Long> partIds) {
        return couponActivityService.updateCoupon(partIds);
    }

    @Override
    public CouponActivityDetailDTO getActivityCouponById(Long id) {
        return couponActivityService.getDetailById(id);
    }

    @Override
    public Page<CouponMemberActivityLimitDTO> PageActivityMeberLimit(QueryCouponMemberActivityPageRequest request) {
        QueryWrapper<CouponActivityMemberLimitDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CouponActivityMemberLimitDO::getCouponActivityId,request.getId());
        if(CollectionUtils.isNotEmpty(request.getMemberIds())){
            queryWrapper.lambda().in(CouponActivityMemberLimitDO::getMemberId,request.getMemberIds());
        }
        Page<CouponActivityMemberLimitDO> objectPage = new Page<>();
        Page<CouponActivityMemberLimitDO> page = couponActivityMemberLimitService.page(objectPage, queryWrapper);
        return PojoUtils.map(page, CouponMemberActivityLimitDTO.class);
    }

    @Override
    public Page<CouponMemberActivityLimitDTO> PageActivityAutoGetMeberLimit(QueryCouponMemberActivityPageRequest request) {
        QueryWrapper<CouponActivityAutoGetMemberLimitDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CouponActivityAutoGetMemberLimitDO::getCouponActivityAutoGetId,request.getId());
        Page<CouponActivityAutoGetMemberLimitDO> objectPage = new Page<>();
        Page<CouponActivityAutoGetMemberLimitDO> page = couponActivityAutoGetMemberLimitService.page(objectPage, queryWrapper);
        return PojoUtils.map(page, CouponMemberActivityLimitDTO.class);
    }
    @Override
    public Map<Long, List<Integer>> getCouponActivityExistFlag(CouponActivityExistFlagRequest request) {
        return couponActivityForGoodsService.getCouponActivityExistFlag(request);
    }

    @Override
    public  List<Integer> suportCouponPayMethodList(){
        return couponActivityForPurchaseOrderService.suportCouponPayMethodList();
    }

    @Override
    public CouponActivityCanUseDTO getCouponCanUseList(QueryCouponCanUseListRequest request) {
        return couponActivityForPurchaseOrderService.getCouponCanUseList(request);
    }

    @Override
    public OrderUseCouponBudgetDTO orderUseCouponShareAmountBudget(OrderUseCouponBudgetRequest request) {
        return couponActivityForPurchaseOrderService.orderUseCouponShareAmountBudget(request);
    }

    @Override
    public Boolean orderUseCoupon(OrderUseCouponRequest request) {
        return couponActivityForPurchaseOrderService.orderUseCoupon(request);
    }

    @Override
    public Boolean useMemberCoupon(UseMemberCouponRequest request) {
        return couponActivityForPurchaseOrderService.useMemberCoupon(request);
    }

    @Override
    public List<CouponActivityEnterpriseLimitDTO> getByCouponActivityId(Long couponActivityId) {
        List<CouponActivityEnterpriseLimitDO> list = couponActivityEnterpriseLimitService.getByCouponActivityId(couponActivityId);
        return PojoUtils.map(list, CouponActivityEnterpriseLimitDTO.class);
    }


//    @Override
//    public Page<CouponActivityHasGetDTO> queryListPageByEid(QueryCouponActivityByEidRequest request) {
//        Page<CouponActivityHasGetDTO> page = new Page();
//        if(ObjectUtil.isNull(request)){
//            return page;
//        }
//        // 查询所有未结束、未废弃的优惠券活动
//        QueryCouponActivityByEidRequest couponActivityRequest = new QueryCouponActivityByEidRequest();
//        request.setEid(request.getEid());
//        page = PojoUtils.map(couponActivityService.getCountByEid(couponActivityRequest), CouponActivityHasGetDTO.class);
//        // 已领取优惠券
//        List<CouponDO> couponList = couponService.getByEid(request.getEid());
//        if(CollUtil.isEmpty(couponList)){
//            return page;
//        }
//        Map<Long, List<CouponDO>> couponMap = couponList.stream().collect(Collectors.groupingBy(CouponDO::getCouponActivityId));
//        page.getRecords().forEach(c -> {
//            couponMap.get(c.getCouponActivityId())
//        });
//
//        return null;
//    }


    @Override
    public List<CouponActivityCanUseDetailDTO> queryByCouponActivityIdList(List<Long> idList) {
        return couponActivityService.queryByCouponActivityIdList(idList);
    }

    @Override
    public CouponActivityRulesBO getCouponActivityRulesById(Long couponActivityId) {
        CouponActivityRulesBO bo = new CouponActivityRulesBO();
        List<CouponActivityDetailDTO> list = getCouponActivityById(new ArrayList(){{add(couponActivityId);}});
        if(CollUtil.isEmpty(list)){
            return bo;
        }
        CouponActivityDetailDTO couponActivity = list.get(0);
        CouponActivityDTO dto = new CouponActivityDTO();
        dto.setType(couponActivity.getType());
        dto.setThresholdValue(couponActivity.getThresholdValue());
        dto.setDiscountValue(couponActivity.getDiscountValue());
        dto.setDiscountMax(couponActivity.getDiscountMax());
        String couponRules = couponActivityService.buildCouponRules(dto);
        bo.setCouponRules(couponRules);
        return bo;
    }

    @Override
    public CouponMemberActivityRuleDTO getMemberCouponActivityRulesById(Long couponActivityId) {
        // 参数是卡包id
        CouponDO couponDO = couponService.getById(couponActivityId);
        CouponActivityDO couponActivityDO = couponActivityService.getById(couponDO.getCouponActivityId());
        return PojoUtils.map(couponActivityDO,CouponMemberActivityRuleDTO.class);
    }

    @Override
    public Long getActivityIdByCouponId(QueryActivityDetailRequest request) {
        return couponActivityService.getActivityIdByCouponId(request);
    }

    @Override
    public List<CouponDTO>  getHasGiveCountByEids(Long couponActivityId, List<Long> eidList){
        return couponActivityService.getHasGiveCountByEids(couponActivityId,eidList);
    }

    @Override
    public void SyncCouponGiveNum() {
        couponActivityService.SyncCouponGiveNum();
    }
}
