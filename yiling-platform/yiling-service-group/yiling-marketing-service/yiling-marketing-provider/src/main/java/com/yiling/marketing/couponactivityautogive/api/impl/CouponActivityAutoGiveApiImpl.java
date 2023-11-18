package com.yiling.marketing.couponactivityautogive.api.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.coupon.service.CouponService;
import com.yiling.marketing.couponactivity.dto.CouponActivityGiveDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityGoodsDTO;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityAutoGiveGoodsRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityGivePageRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryGiveEnterpriseInfoListRequest;
import com.yiling.marketing.couponactivity.dto.request.SaveCouponActivityGiveEnterpriseInfoRequest;
import com.yiling.marketing.couponactivity.service.CouponActivityService;
import com.yiling.marketing.couponactivityautogive.api.CouponActivityAutoGiveApi;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveCouponDTO;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveCouponDetailDTO;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveDTO;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveDetailDTO;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveEnterpriseInfoDTO;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveEnterpriseLimitDTO;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveGoodsLimitDTO;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveMemberDTO;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGivePageDTO;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveRecordDTO;
import com.yiling.marketing.couponactivityautogive.dto.request.CouponActivityAutoGiveOperateRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.CouponActivityAutoGiveRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.DeleteCouponActivityAutoGiveGoodsLimitRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.DeleteCouponActivityAutoGiveMemberLimitRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.QueryCouponActivityAutoGiveMemberLimitRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.QueryCouponActivityAutoGiveRecordRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.QueryCouponActivityAutoGiveRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.QueryCouponActivityGiveDetailRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.QueryCouponActivityGiveFailRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.SaveCouponActivityAutoGiveBasicRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.SaveCouponActivityAutoGiveGoodsLimitRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.SaveCouponActivityAutoGiveMemberLimitRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.SaveCouponActivityAutoGiveRulesRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.UpdateAutoGiveCountRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.UpdateAutoGiveRecordStatusRequest;
import com.yiling.marketing.couponactivityautogive.entity.CouponActivityAutoGiveCouponDO;
import com.yiling.marketing.couponactivityautogive.entity.CouponActivityAutoGiveEnterpriseInfoDO;
import com.yiling.marketing.couponactivityautogive.entity.CouponActivityAutoGiveEnterpriseLimitDO;
import com.yiling.marketing.couponactivityautogive.entity.CouponActivityAutoGiveGoodsLimitDO;
import com.yiling.marketing.couponactivityautogive.entity.CouponActivityAutoGiveRecordDO;
import com.yiling.marketing.couponactivityautogive.service.CouponActivityAutoGiveCouponService;
import com.yiling.marketing.couponactivityautogive.service.CouponActivityAutoGiveEnterpriseInfoService;
import com.yiling.marketing.couponactivityautogive.service.CouponActivityAutoGiveEnterpriseLimitService;
import com.yiling.marketing.couponactivityautogive.service.CouponActivityAutoGiveGoodsLimitService;
import com.yiling.marketing.couponactivityautogive.service.CouponActivityAutoGiveRecordService;
import com.yiling.marketing.couponactivityautogive.service.CouponActivityAutoGiveService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;

/**
 * @author: houjie.sun
 * @date: 2021/10/26
 */
@DubboService
public class CouponActivityAutoGiveApiImpl implements CouponActivityAutoGiveApi {

    @Autowired
    private CouponActivityAutoGiveService                autoGiveService;
    @Autowired
    private CouponActivityAutoGiveCouponService          autoGiveCouponService;
    @Autowired
    private CouponService                                couponService;
    @Autowired
    private CouponActivityAutoGiveRecordService          activityAutoGiveRecordService;
    @Autowired
    private CouponActivityAutoGiveGoodsLimitService      autoGiveGoodsLimitService;
    @Autowired
    private CouponActivityAutoGiveEnterpriseLimitService autoGiveEnterpriseLimitService;
    @Autowired
    private CouponActivityService                        couponActivityService;
    @Autowired
    private CouponActivityAutoGiveEnterpriseInfoService  autoGiveEnterpriseInfoService;

    @Override
    public Page<CouponActivityAutoGivePageDTO> queryListPage(QueryCouponActivityAutoGiveRequest request) {
        return PojoUtils.map(autoGiveService.queryListPage(request), CouponActivityAutoGivePageDTO.class);
    }

    @Override
    public Long saveOrUpdateBasic(SaveCouponActivityAutoGiveBasicRequest request) {
        return autoGiveService.saveOrUpdateBasic(request);
    }

    @Override
    public Long saveOrUpdateRules(SaveCouponActivityAutoGiveRulesRequest request) {
        return autoGiveService.saveOrUpdateRules(request);
    }

    @Override
    public CouponActivityAutoGiveDetailDTO getDetailById(Long id) {
        return autoGiveService.getDetailById(id);
    }

    @Override
    public Boolean stop(CouponActivityAutoGiveOperateRequest request) {
        return autoGiveService.stop(request);
    }

    @Override
    public Boolean enable(CouponActivityAutoGiveOperateRequest request) {
        return autoGiveService.enable(request);
    }

    @Override
    public Boolean scrap(CouponActivityAutoGiveOperateRequest request) {
        return autoGiveService.scrap(request);
    }

    @Override
    public Page<CouponActivityGoodsDTO> queryGoodsLimitListPage(QueryCouponActivityAutoGiveGoodsRequest request) {
        return autoGiveGoodsLimitService.pageList(request);
    }

    @Override
    public Boolean saveGoodsLimit(SaveCouponActivityAutoGiveGoodsLimitRequest request) {
        return autoGiveGoodsLimitService.saveGoodsLimit(request);
    }

    @Override
    public Boolean deleteGoodsLimit(DeleteCouponActivityAutoGiveGoodsLimitRequest request) {
        return autoGiveGoodsLimitService.deleteGoodsLimit(request);
    }

//    @Override
//    public Page<CouponActivityAutoGiveMemberDTO> queryEnterpriseListPage(QueryCouponActivityAutoGiveMemberRequest request) {
//        return autoGiveEnterpriseLimitService.queryEnterpriseListPage(request);
//    }

    @Override
    public Page<CouponActivityAutoGiveMemberDTO> queryEnterpriseLimitListPage(QueryCouponActivityAutoGiveMemberLimitRequest request) {
        Page<CouponActivityAutoGiveEnterpriseLimitDO> page = autoGiveEnterpriseLimitService.queryEnterpriseLimitListPage(request);
        return PojoUtils.map(page, CouponActivityAutoGiveMemberDTO.class);
    }

    @Override
    public Boolean saveEnterpriseLimit(SaveCouponActivityAutoGiveMemberLimitRequest request) {
        return autoGiveEnterpriseLimitService.saveEnterpriseLimit(request);
    }

    @Override
    public Boolean deleteEnterpriseLimit(DeleteCouponActivityAutoGiveMemberLimitRequest request) {
        return autoGiveEnterpriseLimitService.deleteEnterpriseLimit(request);
    }

    @Override
    public Page<CouponActivityGiveDTO> queryGiveListPage(QueryCouponActivityGivePageRequest request) {
        return couponActivityService.queryGiveListPage(request);
    }

    @Override
    public Page<CouponActivityAutoGiveRecordDTO> queryGiveFailListPage(QueryCouponActivityGiveFailRequest request) {
        QueryCouponActivityAutoGiveRecordRequest autoGiveRecordRequest = PojoUtils.map(request, QueryCouponActivityAutoGiveRecordRequest.class);
        return activityAutoGiveRecordService.queryEnterpriseGiveRecordListPage(autoGiveRecordRequest);
    }

    @Override
    public Boolean autoGive(CouponActivityAutoGiveRequest request) {
        return activityAutoGiveRecordService.autoGive(request);
    }

    @Override
    public CouponActivityAutoGiveDTO getAutoGiveById(Long id) {
        return autoGiveService.getAutoGiveById(id);
    }

    @Override
    public List<CouponActivityAutoGiveDTO> getAutoGiveByIdList(List<Long> idList) {
        return autoGiveService.getAutoGiveByIdList(idList);
    }

    @Override
    public CouponActivityAutoGiveCouponDTO getAutoGiveCouponByCouponActivityId(Long couponActivityId) {
        return autoGiveCouponService.getByCouponActivityId(couponActivityId);
    }

    @Override
    public List<CouponActivityAutoGiveCouponDTO> getByCouponByCouponActivityIdList(List<Long> couponActivityIdList) {
        return autoGiveCouponService.getByCouponByCouponActivityIdList(couponActivityIdList);
    }

    @Override
    public List<CouponActivityAutoGiveCouponDTO> getAutoGiveCouponByAutoGiveIdList(List<Long> couponActivityAutoGiveIds) {
        List<CouponActivityAutoGiveCouponDO> list = autoGiveCouponService.getAutoGiveCouponByAutoGiveIdList(couponActivityAutoGiveIds);
        return PojoUtils.map(list, CouponActivityAutoGiveCouponDTO.class);
    }

    @Override
    public List<CouponActivityAutoGiveDetailDTO> getAllByCondition(QueryCouponActivityGiveDetailRequest request) {
        List<CouponActivityAutoGiveDetailDTO> autoGiveDetailList = autoGiveService.getAllByCondition(request);
        if(CollUtil.isNotEmpty(autoGiveDetailList)){
            List<Long> autoGiveIdList = autoGiveDetailList.stream().map(CouponActivityAutoGiveDetailDTO::getId).distinct().collect(Collectors.toList());
            // 关联优惠券列表
            List<CouponActivityAutoGiveCouponDO> list = autoGiveCouponService.getAutoGiveCouponByAutoGiveIdList(autoGiveIdList);
            if(CollUtil.isEmpty(list)){
                autoGiveDetailList.stream().forEach(o -> {
                    o.setCouponActivityList(ListUtil.empty());
                });
            } else {
                Map<Long, List<CouponActivityAutoGiveCouponDO>> map = list.stream().collect(Collectors.groupingBy(CouponActivityAutoGiveCouponDO::getCouponActivityAutoGiveId));
                for (CouponActivityAutoGiveDetailDTO dto : autoGiveDetailList) {
                    List<CouponActivityAutoGiveCouponDO> activityAutoGiveList = map.get(dto.getId());
                    if(CollUtil.isEmpty(activityAutoGiveList)){
                        dto.setCouponActivityList(ListUtil.empty());
                        continue;
                    }
                    List<CouponActivityAutoGiveCouponDetailDTO> dtoList = PojoUtils.map(activityAutoGiveList, CouponActivityAutoGiveCouponDetailDTO.class);
                    dto.setCouponActivityList(dtoList);
                }
            }
        }
        return autoGiveDetailList;
    }

    @Override
    public CouponActivityAutoGiveRecordDTO getAutoGiveRecordLastOneByEid(Long eid) {
        CouponActivityAutoGiveRecordDO record = activityAutoGiveRecordService.getAutoGiveRecordLastOneByEid(eid);
        return PojoUtils.map(record, CouponActivityAutoGiveRecordDTO.class);
    }

    @Override
    public List<CouponActivityAutoGiveGoodsLimitDTO> getGoodsLimitByAutoGiveIdList(List<Long> autoGiveIdList) {
        List<CouponActivityAutoGiveGoodsLimitDO> list = autoGiveGoodsLimitService.getGoodsLimitByAutoGiveIdList(autoGiveIdList);
        return PojoUtils.map(list, CouponActivityAutoGiveGoodsLimitDTO.class);
    }

    @Override
    public List<CouponActivityAutoGiveEnterpriseLimitDTO> getEnterpriseLimitByAutoGiveIdList(List<Long> autoGiveIdList) {
        List<CouponActivityAutoGiveEnterpriseLimitDO> list = autoGiveEnterpriseLimitService.getEnterpriseLimitByAutoGiveIdList(autoGiveIdList);
        return PojoUtils.map(list, CouponActivityAutoGiveEnterpriseLimitDTO.class);
    }

    @Override
    public List<CouponActivityAutoGiveRecordDTO> getRecordListByEidAndAutoGiveIds(Long eid, List<Long> autoGiveIdList) {
        List<CouponActivityAutoGiveRecordDO> list = activityAutoGiveRecordService.getRecordListByEidAndAutoGiveIds(eid, autoGiveIdList);
        return PojoUtils.map(list, CouponActivityAutoGiveRecordDTO.class);
    }

    @Override
    public List<CouponActivityAutoGiveRecordDTO> saveAutoGiveRecordWithWaitStatus(List<CouponActivityAutoGiveRecordDTO> autoGiveList) {
        List<CouponActivityAutoGiveRecordDO> list = PojoUtils.map(autoGiveList, CouponActivityAutoGiveRecordDO.class);
        activityAutoGiveRecordService.saveAutoGiveRecordWithWaitStatus(list);
        List<CouponActivityAutoGiveRecordDTO> resultList = PojoUtils.map(list, CouponActivityAutoGiveRecordDTO.class);
        return resultList;
    }

    @Override
    public Boolean updateRecordStatus(List<UpdateAutoGiveRecordStatusRequest> autoGiveList) {
        return activityAutoGiveRecordService.updateRecordStatus(autoGiveList);
    }

    @Override
    public Boolean updateGiveCountByIdList(UpdateAutoGiveCountRequest request) {
        return autoGiveService.updateGiveCountByIdList(request);
    }

    @Override
    public Boolean saveOrUpdate(SaveCouponActivityGiveEnterpriseInfoRequest request) {
        return autoGiveEnterpriseInfoService.saveOrUpdate(request);
    }

    @Override
    public List<CouponActivityAutoGiveEnterpriseInfoDTO> getByEidAndCouponActivityId(QueryGiveEnterpriseInfoListRequest request) {
        List<CouponActivityAutoGiveEnterpriseInfoDO> list = autoGiveEnterpriseInfoService.getByEidAndCouponActivityId(request);
        return PojoUtils.map(list, CouponActivityAutoGiveEnterpriseInfoDTO.class);
    }

    @Override
    public List<CouponActivityAutoGiveRecordDTO> getRecordListByAutoGiveIds(List<Long> autoGiveIdList) {
        if(CollUtil.isEmpty(autoGiveIdList)){
            return ListUtil.empty();
        }
        List<CouponActivityAutoGiveRecordDO> list = activityAutoGiveRecordService.getRecordListByAutoGiveIds(autoGiveIdList);
        return PojoUtils.map(list, CouponActivityAutoGiveRecordDTO.class);
    }

    @Override
    public List<CouponActivityAutoGiveGoodsLimitDTO> getGoodsLimitByAutoGiveId(Long autoGiveId) {
        return PojoUtils.map(autoGiveGoodsLimitService.getGoodsLimitByAutoGiveId(autoGiveId), CouponActivityAutoGiveGoodsLimitDTO.class);
    }

    @Override
    public List<CouponActivityAutoGiveEnterpriseLimitDTO> getByAutoGiveId(Long id) {
        return PojoUtils.map(autoGiveEnterpriseLimitService.getByAutoGiveId(id), CouponActivityAutoGiveEnterpriseLimitDTO.class);
    }
}
