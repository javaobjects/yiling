package com.yiling.marketing.couponactivityautoget.service.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.common.enums.CouponActivityCanGetEnum;
import com.yiling.marketing.common.enums.CouponActivityLogTypeEnum;
import com.yiling.marketing.common.enums.CouponActivityResultTypeEnum;
import com.yiling.marketing.common.enums.CouponActivityStatusEnum;
import com.yiling.marketing.common.enums.CouponAutoGetRangeEnum;
import com.yiling.marketing.common.enums.CouponErrorCode;
import com.yiling.marketing.common.enums.CouponGetEnterpriseLimitTypeEnum;
import com.yiling.marketing.common.enums.CouponGiveEnterpriseLimitTypeEnum;
import com.yiling.marketing.common.enums.CouponLimitTypeEnum;
import com.yiling.marketing.coupon.dto.CouponDTO;
import com.yiling.marketing.coupon.service.CouponService;
import com.yiling.marketing.couponactivity.dto.CouponActivityDetailDTO;
import com.yiling.marketing.couponactivity.dto.request.DeleteCouponActivityEnterpriseLimitRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityRepeatNameRequest;
import com.yiling.marketing.couponactivity.entity.CouponActivityDO;
import com.yiling.marketing.couponactivity.entity.CouponActivityEnterpriseGetRulesDO;
import com.yiling.marketing.couponactivity.entity.CouponActivityEnterpriseLimitDO;
import com.yiling.marketing.couponactivity.enums.CouponActivityErrorCode;
import com.yiling.marketing.couponactivity.service.CouponActivityEnterpriseGetRulesService;
import com.yiling.marketing.couponactivity.service.CouponActivityEnterpriseLimitService;
import com.yiling.marketing.couponactivity.service.CouponActivityGoodsLimitService;
import com.yiling.marketing.couponactivity.service.CouponActivityLogService;
import com.yiling.marketing.couponactivity.service.CouponActivityService;
import com.yiling.marketing.couponactivityautoget.dao.CouponActivityAutoGetMapper;
import com.yiling.marketing.couponactivityautoget.dto.CouponActivityAutoGetDTO;
import com.yiling.marketing.couponactivityautoget.dto.CouponActivityAutoGetDetailDTO;
import com.yiling.marketing.couponactivityautoget.dto.request.CouponActivityAutoGetOperateRequest;
import com.yiling.marketing.couponactivityautoget.dto.request.QueryCouponActivityAutoGetRequest;
import com.yiling.marketing.couponactivityautoget.dto.request.SaveCouponActivityAutoGetBasicRequest;
import com.yiling.marketing.couponactivityautoget.dto.request.SaveCouponActivityAutoGetCouponRequest;
import com.yiling.marketing.couponactivityautoget.dto.request.SaveCouponActivityAutoGetRulesRequest;
import com.yiling.marketing.couponactivityautoget.entity.CouponActivityAutoGetDO;
import com.yiling.marketing.couponactivityautoget.entity.CouponActivityAutoGetEnterpriseLimitDO;
import com.yiling.marketing.couponactivityautoget.entity.CouponActivityAutoGetMemberLimitDO;
import com.yiling.marketing.couponactivityautoget.entity.CouponActivityAutoGetPromotionLimitDO;
import com.yiling.marketing.couponactivityautoget.service.CouponActivityAutoGetCouponService;
import com.yiling.marketing.couponactivityautoget.service.CouponActivityAutoGetEnterpriseLimitService;
import com.yiling.marketing.couponactivityautoget.service.CouponActivityAutoGetMemberLimitService;
import com.yiling.marketing.couponactivityautoget.service.CouponActivityAutoGetPromotionLimitService;
import com.yiling.marketing.couponactivityautoget.service.CouponActivityAutoGetRecordService;
import com.yiling.marketing.couponactivityautoget.service.CouponActivityAutoGetService;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGetCouponDTO;
import com.yiling.marketing.couponactivityautogive.enums.CouponActivityAutoGiceErrorCode;
import com.yiling.order.order.api.OrderFirstInfoApi;
import com.yiling.order.order.enums.OrderTypeEnum;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 自主领券活动表 服务实现类
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
@Slf4j
@Service
public class CouponActivityAutoGetServiceImpl extends BaseServiceImpl<CouponActivityAutoGetMapper, CouponActivityAutoGetDO>
                                              implements CouponActivityAutoGetService {

    @DubboReference
    OrderFirstInfoApi                                   orderFirstInfoApi;
    @Autowired
    private CouponService                               couponService;
    @Autowired
    private CouponActivityAutoGetCouponService          autoGetCouponService;
    @Autowired
    private CouponActivityService                       couponActivityService;
    @Autowired
    private CouponActivityAutoGetEnterpriseLimitService autoGetEnterpriseLimitService;
    @Autowired
    private CouponActivityLogService                    couponActivityLogService;
    @Autowired
    private CouponActivityGoodsLimitService             couponActivityGoodsLimitService;
    @Autowired
    private CouponActivityEnterpriseLimitService        couponActivityEnterpriseLimitService;
    @Autowired
    private CouponActivityAutoGetRecordService          autoGetRecordService;
    @Autowired
    private CouponActivityAutoGetService                autoGetService;
    @Autowired
    private CouponActivityEnterpriseGetRulesService     enterpriseGetRulesService;
    @Autowired
    private CouponActivityAutoGetMemberLimitService     couponActivityAutoGetMemberLimitService;
    @Autowired
    private CouponActivityAutoGetEnterpriseLimitService     couponActivityAutoGetEnterpriseLimitService;
    @Autowired
    private CouponActivityAutoGetPromotionLimitService couponActivityAutoGetPromotionLimitService;


    @Override
    public Page<CouponActivityAutoGetDO> queryListPage(QueryCouponActivityAutoGetRequest request) {
        LambdaQueryWrapper<CouponActivityAutoGetDO> queryWrapper = getCouponActivityWrapper(request);
        return page(request.getPage(), queryWrapper);
    }

    @Override
    public CouponActivityAutoGetDetailDTO getDetailById(Long id) {
        if (ObjectUtil.isNull(id)) {
            return null;
        }
        return PojoUtils.map(this.getDetail(id), CouponActivityAutoGetDetailDTO.class);
    }

    @Override
    @GlobalTransactional
    public Long saveOrUpdateBasic(SaveCouponActivityAutoGetBasicRequest request) {
        String name = request.getName();
        if (StrUtil.isBlank(name)) {
            throw new BusinessException(CouponActivityErrorCode.NAME_NULL_ERROR);
        }
        // 时间校验
        long nowTime = System.currentTimeMillis();
        Date beginTime = request.getBeginTime();
        Date endTime = request.getEndTime();
        if(ObjectUtil.isNull(beginTime)){
            throw new BusinessException(CouponActivityErrorCode.BEGIN_TIME_NULL_ERROR);
        }
        if(ObjectUtil.isNull(endTime)){
            throw new BusinessException(CouponActivityErrorCode.END_TIME_NULL_ERROR);
        }
        // 保存校验开始时间
        if (ObjectUtil.isNull(request.getId()) && beginTime.getTime() < nowTime) {
            throw new BusinessException(CouponErrorCode.COUPON_BEGIN_TIME_ERROR);
        }
        // 保存、修改校验开始时间
        if (endTime.getTime() <= beginTime.getTime()) {
            throw new BusinessException(CouponErrorCode.COUPON_END_TIME_ERROR);
        }



        // 修改校验
        String checkMsg = updateCouponActivityAutoGetCheck(request.getId(), name, nowTime, beginTime, endTime);
        if (StrUtil.isNotBlank(checkMsg)) {
            throw new BusinessException(CouponActivityAutoGiceErrorCode.UPDATE_ALLOW_CHECK_ERROR, checkMsg);
        }

        // 保存校验
        checkMsg = saveCouponActivityAutoGiveCheck(request);
        if (StrUtil.isNotBlank(checkMsg)) {
            throw new BusinessException(CouponActivityAutoGiceErrorCode.UPDATE_ALLOW_CHECK_ERROR, checkMsg);
        }

        // 关联优惠券空值校验
        List<SaveCouponActivityAutoGetCouponRequest> couponActivityAutoGetList = request.getCouponActivityAutoGetList();
        if (CollUtil.isEmpty(couponActivityAutoGetList)) {
            throw new BusinessException(CouponActivityAutoGiceErrorCode.AUTO_GET_RELATION_COUPON_ACTIVITY_NULL);
        }
        couponActivityAutoGetList.forEach(couponActivity -> {
            if (ObjectUtil.isNull(couponActivity.getCouponActivityId()) || couponActivity.getCouponActivityId() == 0) {
                throw new BusinessException(CouponActivityAutoGiceErrorCode.AUTO_GET_RELATION_COUPON_ACTIVITY_NULL);
            }
            if (ObjectUtil.isNull(couponActivity.getGiveNum()) || couponActivity.getGiveNum() == 0) {
                throw new BusinessException(CouponActivityAutoGiceErrorCode.AUTO_GET_RELATION_COUPON_ACTIVITY_NUM_NULL);
            }
        });

        // 校验重复值
        List<Long> couponActivityIdList = couponActivityAutoGetList.stream().map(SaveCouponActivityAutoGetCouponRequest::getCouponActivityId)
            .collect(Collectors.toList());
        String format = "";
        Set<Long> set = new HashSet<>(couponActivityIdList);
        Collection difference = CollectionUtils.disjunction(couponActivityIdList, set);
        List<Long> differenceList = new ArrayList<>(difference);
        if (CollUtil.isNotEmpty(differenceList)) {
            format = MessageFormat.format(CouponActivityAutoGiceErrorCode.COUPON_ACTIVITY_REPEAT_ERROR.getMessage(), differenceList.toString());
            throw new BusinessException(CouponActivityAutoGiceErrorCode.COUPON_ACTIVITY_REPEAT_ERROR, format);
        }

        // 查询优惠券活动信息
        List<CouponActivityDetailDTO> couponActivityDbList = couponActivityService.getCouponActivityById(couponActivityIdList);
        // 优惠券活动全部不存在
        if (CollUtil.isEmpty(couponActivityDbList)) {
            format = MessageFormat.format(CouponActivityAutoGiceErrorCode.COUPON_ACTIVITY_NULL.getMessage(), couponActivityIdList.toString());
            throw new BusinessException(CouponActivityAutoGiceErrorCode.COUPON_ACTIVITY_NULL, format);
        }


        // 优惠券活动有部分不存在
        Map<Long, CouponActivityDetailDTO> couponActivityDbMap = couponActivityDbList.stream()
            .collect(Collectors.toMap(c -> c.getId(), c -> c, (v1, v2) -> v1));
        //校验领券活动开始结束时间和优惠券时间的比较

        if (CollectionUtils.isNotEmpty(request.getCouponActivityAutoGetList())) {
            List<CouponActivityDetailDTO> gudingActivity = couponActivityDbList.stream().filter(item -> item.getUseDateType() == 1).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(gudingActivity)) {
                gudingActivity.forEach(item->{
                    CouponActivityDetailDTO couponActivityDetailDTO = couponActivityDbMap.get(item.getId());
                    if(request.getEndTime().after(couponActivityDetailDTO.getEndTime())){
                        throw new BusinessException(CouponErrorCode.COUPON_GET_ENDTIME_AFTER_ACTIVITY_ENDTIME);
                    }
                });
            }
        }
        // 优惠券活动已发放数量
        List<Map<String, Long>> giveCountList = couponService.getGiveCountByCouponActivityId(couponActivityIdList);
        for (SaveCouponActivityAutoGetCouponRequest autoGetCouponRequest : couponActivityAutoGetList) {
            CouponActivityDetailDTO couponActivity = couponActivityDbMap.get(autoGetCouponRequest.getCouponActivityId());
            if (ObjectUtil.isNull(couponActivity)) {
                format = MessageFormat.format(CouponActivityAutoGiceErrorCode.COUPON_ACTIVITY_NULL.getMessage(), couponActivity.getId());
                throw new BusinessException(CouponActivityAutoGiceErrorCode.COUPON_ACTIVITY_NULL, format);
            }
            // 优惠券活动校验
            List<Integer> countList = new ArrayList<>();
            CouponActivityDO couponActivityDo = PojoUtils.map(couponActivity, CouponActivityDO.class);
            String couponActivityErrorMsg = couponActivityService.checkCouponActivity(couponActivityDo, countList, giveCountList);
            if (StrUtil.isNotBlank(couponActivityErrorMsg)) {
                throw new BusinessException(CouponErrorCode.COUPON_RELEVANCE_ERROR, couponActivityErrorMsg);
            }
        }

        // 是否已被其他发放活动关联
        String errorMsg = this.getByAutoGetIdAndCouponActivityId(request.getId(), couponActivityIdList);
        if (StrUtil.isNotBlank(errorMsg)) {
            throw new BusinessException(CouponErrorCode.COUPON_RELEVANCE_ERROR, errorMsg);
        }

        CouponActivityAutoGetDO couponActivityAutoGetDO = PojoUtils.map(request, CouponActivityAutoGetDO.class);
        Long autoGetId = saveOrUpdate(request.getId(), request.getOpUserId(), couponActivityAutoGetDO);
        // 保存关联优惠券
        autoGetCouponService.saveGiveCoupon(request.getOpUserId(), autoGetId, couponActivityAutoGetList);
        return autoGetId;
    }

    public String getByAutoGetIdAndCouponActivityId(Long autoGetId, List<Long> couponActivityIds) {
        if (CollUtil.isEmpty(couponActivityIds)) {
            throw new BusinessException(ResultCode.PARAM_MISS);
        }
        List<CouponActivityAutoGetCouponDTO> autoGetCouponList = autoGetCouponService.getByCouponActivityIdList(couponActivityIds);
        if (CollUtil.isEmpty(autoGetCouponList)) {
            return null;
        }
        List<Long> autoGetIdDbList = autoGetCouponList.stream().map(CouponActivityAutoGetCouponDTO::getCouponActivityAutoGetId).distinct()
            .collect(Collectors.toList());
        List<CouponActivityAutoGetDTO> autoGetDbList = this.getByIdList(autoGetIdDbList);
        if (CollUtil.isEmpty(autoGetDbList)) {
            return null;
        }
        if (autoGetDbList.size() == 1 && ObjectUtil.equal(autoGetDbList.get(0).getId(), autoGetId)) {
            return null;
        }

        Map<Long, List<CouponActivityAutoGetCouponDTO>> autoGetCouponMap = autoGetCouponList.stream()
            .collect(Collectors.groupingBy(CouponActivityAutoGetCouponDTO::getCouponActivityAutoGetId));
        for (CouponActivityAutoGetDTO autoGetDb : autoGetDbList) {
            if (ObjectUtil.equal(autoGetDb.getId(), autoGetId)) {
                continue;
            }
            List<CouponActivityAutoGetCouponDTO> autoGiveCoupon = autoGetCouponMap.get(autoGetDb.getId());
            if (ObjectUtil.isNull(autoGiveCoupon)) {
                continue;
            }

            boolean timeFlag = false;
            boolean statusFlag = false;
            if (autoGetDb.getEndTime().getTime() >= System.currentTimeMillis()) {
                timeFlag = true;
            }
            if (!ObjectUtil.equal(CouponActivityStatusEnum.SCRAP.getCode(), autoGetDb.getStatus())) {
                statusFlag = true;
            }
            if (timeFlag && statusFlag) {
                List<Long> list = autoGiveCoupon.stream().map(CouponActivityAutoGetCouponDTO::getCouponActivityId).distinct()
                    .collect(Collectors.toList());
                return "此优惠券活动id:" + list.toString() + "已被其他自动领取活动关联[id:" + autoGetDb.getId() + "]";
            }
        }
        return null;
    }

    @Override
    public Long saveOrUpdateRules(SaveCouponActivityAutoGetRulesRequest request) {
        if (ObjectUtil.isNull(request.getId())) {
            log.info("自主领取活动保存或修改使用规则，ID不能为空");
            throw new BusinessException(CouponActivityErrorCode.UPDATE_ID_NULL_ERROR);
        }

        // 修改校验
        String checkMsg = updateCouponActivityAutoGetCheck(request.getId(), null, 0, null, null);
        if (StrUtil.isNotBlank(checkMsg)) {
            throw new BusinessException(CouponActivityAutoGiceErrorCode.UPDATE_ALLOW_CHECK_ERROR, checkMsg);
        }
        // 部分会员可用
        if (ObjectUtil.equal(CouponAutoGetRangeEnum.PART.getCode(), request.getConditionEnterpriseRange())) {
            QueryWrapper<CouponActivityAutoGetEnterpriseLimitDO> objectQueryWrapper = new QueryWrapper<>();
            objectQueryWrapper.lambda().eq(CouponActivityAutoGetEnterpriseLimitDO::getCouponActivityAutoGetId,request.getId());
            List<CouponActivityAutoGetEnterpriseLimitDO> activityAutoGetMemberLimitDOS = couponActivityAutoGetEnterpriseLimitService.list(objectQueryWrapper);
            if(CollectionUtils.isEmpty(activityAutoGetMemberLimitDOS)){
                throw new BusinessException(CouponActivityAutoGiceErrorCode.AUTO_GET_RELATION_COUPON_ENTERPRISE_EMPTY);
            }
        }
        // 会员方案
        if (ObjectUtil.equal(7, request.getConditionUserType())) {
            QueryWrapper<CouponActivityAutoGetMemberLimitDO> objectQueryWrapper = new QueryWrapper<>();
            objectQueryWrapper.lambda().eq(CouponActivityAutoGetMemberLimitDO::getCouponActivityAutoGetId,request.getId());
            List<CouponActivityAutoGetMemberLimitDO> activityAutoGetMemberLimitDOS = couponActivityAutoGetMemberLimitService.list(objectQueryWrapper);
            if(CollectionUtils.isEmpty(activityAutoGetMemberLimitDOS)){
                throw new BusinessException(CouponActivityAutoGiceErrorCode.AUTO_GET_RELATION_COUPON_MEMBER_EMPTY);
            }
        }
       // 推广人
        if (ObjectUtil.equal(8, request.getConditionUserType())) {
            QueryWrapper<CouponActivityAutoGetPromotionLimitDO> objectQueryWrapper = new QueryWrapper<>();
            objectQueryWrapper.lambda().eq(CouponActivityAutoGetPromotionLimitDO::getCouponActivityAutoGetId,request.getId());
            List<CouponActivityAutoGetPromotionLimitDO> activityAutoGetMemberLimitDOS = couponActivityAutoGetPromotionLimitService.list(objectQueryWrapper);
            if(CollectionUtils.isEmpty(activityAutoGetMemberLimitDOS)){
                throw new BusinessException(CouponActivityAutoGiceErrorCode.AUTO_GET_RELATION_COUPON_PROMOTION_USER_EMPTY);
            }
        }

        /* 企业类型限制（1-全部企业类型；2-部分企业类型） */
       /* if (ObjectUtil.isNull(request.getConditionEnterpriseType()) || request.getConditionEnterpriseType() == 0) {
            throw new BusinessException(CouponErrorCode.ENTERPRISE_TYPE_LIMIT_ERROR);
        }*/
        // 2
        String enterpriseTypeSelected = "";
        if (ObjectUtil.equal(CouponLimitTypeEnum.PART.getCode(), request.getConditionEnterpriseType())) {
            if (CollUtil.isEmpty(request.getConditionEnterpriseTypeValueList())) {
                throw new BusinessException(CouponErrorCode.ENTERPRISE_TYPE_VALUE_ERROR);
            }
            enterpriseTypeSelected = String.join(",", request.getConditionEnterpriseTypeValueList());
        }

        /* 用户类型 */
        /*if (ObjectUtil.isNull(request.getConditionUserType()) || request.getConditionUserType() == 0) {
            throw new BusinessException(CouponErrorCode.USER_TYPE_LIMIT_ERROR);
        }*/
        if (ObjectUtil.equal(CouponGetEnterpriseLimitTypeEnum.PART_MEMBER.getCode(), request.getConditionUserType())) {
            // 部分会员
            List<CouponActivityAutoGetEnterpriseLimitDO> autoGetEnterpriseLimitList = autoGetEnterpriseLimitService
                .getByAutoGetId(request.getId());
            if (CollUtil.isEmpty(autoGetEnterpriseLimitList)) {
                throw new BusinessException(CouponErrorCode.MEMBER_LIMIT_NULL_ERROR);
            }
        }

        CouponActivityAutoGetDO couponActivityutoGetDO = PojoUtils.map(request, CouponActivityAutoGetDO.class);
        // 企业类型
        couponActivityutoGetDO.setConditionEnterpriseTypeValue(enterpriseTypeSelected);

        return saveOrUpdate(request.getId(), request.getCurrentUserId(), couponActivityutoGetDO);
    }

    @Override
    @GlobalTransactional
    public Boolean stop(CouponActivityAutoGetOperateRequest request) {
        Long id = request.getId();
        Long userId = request.getUserId();
        // 优惠券活动操作日志构建
        Integer logType = CouponActivityLogTypeEnum.STOP.getCode();
        int logStatus = CouponActivityResultTypeEnum.SUCCESS.getCode();
        String faileReason = "";
        CouponActivityAutoGetDO couponActivityAutoGet = this.getById(id);
        if (ObjectUtil.isNull(couponActivityAutoGet)) {
            throw new BusinessException(CouponErrorCode.DATA_NOT_EXIST);
        }

        Integer status = couponActivityAutoGet.getStatus();
        if (!ObjectUtil.equal(CouponActivityStatusEnum.ENABLED.getCode(), status)) {
//                logStatus = CouponActivityResultTypeEnum.FAIL.getCode();
//                faileReason = "停用失败：自主领券活动状态不是“启用”，不能操作“停用”id=" + id;
//                log.error("自主领券活动状态不是“启用”，不能操作“停用”, id -> {}", id);
            throw new BusinessException(CouponActivityErrorCode.STOP_ERROR);
        }
        LambdaQueryWrapper<CouponActivityAutoGetDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CouponActivityAutoGetDO::getId, id);
        CouponActivityAutoGetDO entity = new CouponActivityAutoGetDO();
        entity.setStatus(CouponActivityStatusEnum.DISABLED.getCode());
        this.update(entity, queryWrapper);
        // 保存操作日志
        couponActivityLogService.insertCouponActivityLog(id, logType, JSONObject.toJSONString(request), logStatus, faileReason, userId);
        return true;
    }

    @Override
    @GlobalTransactional
    public Boolean enable(CouponActivityAutoGetOperateRequest request) {
        Long id = request.getId();
        Long userId = request.getUserId();
        // 优惠券活动操作日志构建
        Integer logType = CouponActivityLogTypeEnum.ENABLE.getCode();
        int logStatus = CouponActivityResultTypeEnum.SUCCESS.getCode();
        String faileReason = "";
        CouponActivityAutoGetDO couponActivityAutoGet = this.getById(id);
        if (ObjectUtil.isNull(couponActivityAutoGet)) {
            throw new BusinessException(CouponErrorCode.DATA_NOT_EXIST);
        }

        Integer status = couponActivityAutoGet.getStatus();
        if (!ObjectUtil.equal(CouponActivityStatusEnum.DISABLED.getCode(), status)) {
//                logStatus = CouponActivityResultTypeEnum.FAIL.getCode();
//                faileReason = "启用失败：自主领券活动状态不是“停用”，不能操作“启用”id=" + id;
//                log.error("自主领券活动状态不是“停用”，不能操作“启用”, id -> {}", id);
            throw new BusinessException(CouponActivityErrorCode.STOP_ERROR);
        }
        LambdaQueryWrapper<CouponActivityAutoGetDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CouponActivityAutoGetDO::getId, id);
        CouponActivityAutoGetDO entity = new CouponActivityAutoGetDO();
        entity.setStatus(CouponActivityStatusEnum.ENABLED.getCode());
        this.update(entity, queryWrapper);
        // 保存操作日志
        couponActivityLogService.insertCouponActivityLog(id, logType, JSONObject.toJSONString(request), logStatus, faileReason, userId);
        return true;
    }

    @Override
    @GlobalTransactional
    public Boolean scrap(CouponActivityAutoGetOperateRequest request) {
        Long id = request.getId();
        Long userId = request.getUserId();
        // 优惠券活动操作日志构建
        Integer logType = CouponActivityLogTypeEnum.SCRAP.getCode();
        int logStatus = CouponActivityResultTypeEnum.SUCCESS.getCode();
        String faileReason = "";
        CouponActivityAutoGetDO couponActivityAutoGet = this.getById(id);
        if (ObjectUtil.isNull(couponActivityAutoGet)) {
            throw new BusinessException(CouponErrorCode.DATA_NOT_EXIST);
        }

        Integer status = couponActivityAutoGet.getStatus();
        if (ObjectUtil.equal(CouponActivityStatusEnum.SCRAP.getCode(), status)) {
//                logStatus = CouponActivityResultTypeEnum.FAIL.getCode();
//                faileReason = "作废失败：自主领券活动状态是“废弃”，不能再操作“作废”，id=" + id;
//                log.error("自主领券活动状态是“废弃”，不能再操作“作废”, id -> {}", id);
            throw new BusinessException(CouponActivityErrorCode.SCRAP_ERROR);
        }
        LambdaQueryWrapper<CouponActivityAutoGetDO> activityQueryWrapper = new LambdaQueryWrapper<>();
        activityQueryWrapper.eq(CouponActivityAutoGetDO::getId, id);
        CouponActivityAutoGetDO activityEntity = new CouponActivityAutoGetDO();
        activityEntity.setStatus(CouponActivityStatusEnum.SCRAP.getCode());
        this.update(activityEntity, activityQueryWrapper);
        // 保存操作日志
        couponActivityLogService.insertCouponActivityLog(id, logType, JSONObject.toJSONString(request), logStatus, faileReason, userId);
        return true;
    }

    @Override
    public CouponActivityAutoGetDTO getAutoGetById(Long id) {
        if (ObjectUtil.isNull(id)) {
            return null;
        }
        LambdaQueryWrapper<CouponActivityAutoGetDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(CouponActivityAutoGetDO::getId, id);
        CouponActivityAutoGetDO one = this.getOne(lambdaQueryWrapper);
        return PojoUtils.map(one, CouponActivityAutoGetDTO.class);
    }

    @Override
    public List<CouponActivityAutoGetDTO> getAutoGetByIdList(List<Long> idList) {
        if (CollUtil.isEmpty(idList)) {
            return ListUtil.empty();
        }
        LambdaQueryWrapper<CouponActivityAutoGetDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(CouponActivityAutoGetDO::getId, idList);
        lambdaQueryWrapper.eq(CouponActivityAutoGetDO::getStatus, CouponActivityStatusEnum.ENABLED.getCode());
        lambdaQueryWrapper.gt(CouponActivityAutoGetDO::getEndTime, new Date());
        List<CouponActivityAutoGetDO> list = this.list(lambdaQueryWrapper);
        return PojoUtils.map(list, CouponActivityAutoGetDTO.class);
    }

    /**
     * 保存自主领取活动基本信息校验
     * @param request
     * @return
     */
    public String saveCouponActivityAutoGiveCheck(SaveCouponActivityAutoGetBasicRequest request) {
        if (ObjectUtil.isNotNull(request.getId())) {
            return null;
        }
        // 名称是否重复
       /* boolean isNameRepeat = checkNameRepeat(request.getName());
        if (isNameRepeat) {
            return "此自主领券活动名称已存在，请检查";
        }*/
        return null;
    }

    /**
     * 修改自主领取活动校验
     * @param id
     * @param name
     * @return
     */
    public String updateCouponActivityAutoGetCheck(Long id, String name, long nowTime, Date beginTime, Date endTime) {
        if (ObjectUtil.isNull(id)) {
            return null;
        }

        // id不为空，开始校验是否允许修改
        // 查询已保存信息
        CouponActivityAutoGetDO autoGetDb = this.getById(id);

        Integer status = autoGetDb.getStatus();
        // 状态：停用、废弃，不可修改
        if (!ObjectUtil.equal(CouponActivityStatusEnum.ENABLED.getCode(), status)) {
            log.info("updateCouponActivityAutoGetCheck false, reason -> {}", "自主领券活动状态：停用、废弃，不可修改");
            return "自主领券活动状态：停用、废弃，不可修改";
        }

        if(nowTime != 0 && ObjectUtil.isNotNull(beginTime) && ObjectUtil.isNotNull(endTime)){
            // 已开始，不能修改开始时间
            if(autoGetDb.getBeginTime().getTime() <= nowTime && autoGetDb.getBeginTime().getTime() != beginTime.getTime()){
                return "自动发放优惠券活动已开始，不能修改活动开始时间，请检查";
            }
            // 已结束，不能修改时间
            if (autoGetDb.getEndTime().getTime() <= nowTime
                    && (autoGetDb.getBeginTime().getTime() != beginTime.getTime() || autoGetDb.getEndTime().getTime() != endTime.getTime())) {
                return "自动发放优惠券活动已结束，不能修改活动起止时间，请检查";
            }
        }

        // 是否已领取
        List<CouponDTO> couponList = couponService.getByCouponActivityAutoId(id);
        if (CollUtil.isNotEmpty(couponList)) {
            return "此自主领券活动已经发放过优惠券，不能修改，请检查";
        }
        // 名称是否重复==>可以重复
        return null;
    }

    /**
     * 优惠券活动名称是否重复
     * @param name
     * @return true-是 false-否
     */
    private boolean checkNameRepeat(String name) {
        QueryCouponActivityRepeatNameRequest request = new QueryCouponActivityRepeatNameRequest();
        request.setName(name);
        Long repeatId = this.baseMapper.getIdByName(request);
        if (ObjectUtil.isNotNull(repeatId)) {
            return true;
        }
        return false;
    }

    @Override
    public String couponActivityAutoGetCheck(CouponActivityDetailDTO couponActivity, Long userId, Long eid, Integer etype, Integer currentMember, List<Long> autoGetIdList) {
        Integer sponsorType = couponActivity.getSponsorType();
        Long activityEid = couponActivity.getEid();
        // 平台创建的券校验
        if (ObjectUtil.isNotNull(activityEid) && activityEid.intValue() == 0) {
            return checkGetForPlatform(couponActivity, userId, eid, etype, currentMember, autoGetIdList);
        }
        // 商家创建的券校验
        if (ObjectUtil.isNotNull(activityEid) && activityEid.intValue() != 0) {
            return checkGetForBusiness(couponActivity, userId, eid, etype, autoGetIdList);
        }
        return null;
    }

    @Override
    public List<CouponActivityAutoGetDTO> getByIdList(List<Long> idList) {
        if (CollUtil.isEmpty(idList)) {
            return ListUtil.empty();
        }
        LambdaQueryWrapper<CouponActivityAutoGetDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(CouponActivityAutoGetDO::getId, idList);
        List<CouponActivityAutoGetDO> list = this.list(lambdaQueryWrapper);
        return PojoUtils.map(list, CouponActivityAutoGetDTO.class);
    }
    private String checkGetForBusiness(CouponActivityDetailDTO couponActivity, Long userId, Long eid, Integer etype, List<Long> autoGetIdList) {
        // 商家创建的优惠券活动是否可领取
        if(!ObjectUtil.equal(CouponActivityCanGetEnum.CAN_GET.getCode(), couponActivity.getCanGet())){
            log.info("此优惠券领取活动用户领取设置规则为不可领取，couponActivityId -> {}, userId -> {}, eid -> {}, couponActivityId -> {}", couponActivity.getId(), userId,
                    eid, couponActivity.getId());
            return CouponErrorCode.COUPON_ACTIVITY_CAN_GET_ERROR.getMessage();
        }

        List<CouponActivityEnterpriseGetRulesDO> enterpriseGetRulesList = enterpriseGetRulesService
            .getByCouponActivityIdList(ListUtil.toList(couponActivity.getId()));
        if (CollUtil.isEmpty(enterpriseGetRulesList)) {
            log.info("此商家优惠券领取规则设置为空，couponActivityId -> {}, userId -> {}, eid -> {}", couponActivity.getId(), userId, eid);
            return CouponErrorCode.MOBILE_AUTO_GET_ERROR.getMessage();
        }
        // 领取规则
        CouponActivityEnterpriseGetRulesDO enterpriseGetRules = enterpriseGetRulesList.get(0);
        // 时间
        Date date = new Date();
        if (enterpriseGetRules.getEndTime().getTime() <= date.getTime()) {
            log.info("此优惠券领取活动已结束，不能领取，couponActivityId -> {}, userId -> {}, eid -> {}, enterpriseGetRuleId -> {}", couponActivity.getId(), userId,
                eid, enterpriseGetRules.getId());
            return CouponErrorCode.COUPON_ACTIVITY_GET_END_ERROR.getMessage();
        }
        // 企业类型
        String enterpriseTypeCheckResult = enterpriseLimitTypeCheck(eid, userId, couponActivity.getId(), enterpriseGetRules.getId(),
            enterpriseGetRules.getConditionEnterpriseType(), enterpriseGetRules.getConditionEnterpriseTypeValue(), etype);
        if (StrUtil.isNotBlank(enterpriseTypeCheckResult)) {
            return enterpriseTypeCheckResult;
        }
        // 用户类型
        if (ObjectUtil.equal(CouponGetEnterpriseLimitTypeEnum.PART_USER.getCode(), enterpriseGetRules.getConditionUserType())) {
            // 查询可领取用户企业
            List<CouponActivityAutoGetEnterpriseLimitDO> autoGetEnterpriseList = autoGetEnterpriseLimitService
                .getByAutoGetId(enterpriseGetRules.getId());
            if (CollUtil.isNotEmpty(autoGetEnterpriseList)) {
                Map<Long, List<CouponActivityAutoGetEnterpriseLimitDO>> autoGetEnterpriseMap = autoGetEnterpriseList.stream()
                    .collect(Collectors.groupingBy(CouponActivityAutoGetEnterpriseLimitDO::getEid));
                if (ObjectUtil.isNull(autoGetEnterpriseMap.get(eid))) {
                    log.info("此商家优惠券仅部分用户可以领取，couponActivityId -> {}, userId -> {}, eid -> {}, enterpriseGetRuleId -> {}", couponActivity.getId(),
                        userId, eid, enterpriseGetRules.getId());
                    return CouponErrorCode.MOBILE_AUTO_GET_ENTERPRISE_LIMIT_ERROR.getMessage();
                }
            }
        }
        // 新客校验
        if ((ObjectUtil.equal(CouponGetEnterpriseLimitTypeEnum.NEW_CUSTOMER.getCode(), enterpriseGetRules.getConditionUserType()))) {
            if (!orderFirstInfoApi.checkNewVisitor(eid, OrderTypeEnum.B2B)) {
                return "此优惠券仅新客用户可以领取，您不能领取";
            }
        }
        // 领取数量
        int giveNum = ObjectUtil.isNull(enterpriseGetRules.getGiveNum()) ? 0 : enterpriseGetRules.getGiveNum().intValue();
        String giveNumCheckResult = giveNumCheck("checkGetForBusiness", userId, eid, giveNum, couponActivity.getId(), enterpriseGetRules.getId(),
            null, 2);
        if (StrUtil.isNotBlank(giveNumCheckResult)) {
            return giveNumCheckResult;
        }
        autoGetIdList.add(enterpriseGetRules.getId());
        return null;
    }

    private String checkGetForPlatform(CouponActivityDetailDTO couponActivity, Long userId, Long eid, Integer etype, Integer currentMember, List<Long> autoGetIdList) {
        CouponActivityAutoGetCouponDTO autoGetCoupon = autoGetCouponService.getByCouponActivityId(couponActivity.getId());
        if (ObjectUtil.isNull(autoGetCoupon)) {
            log.info("此平台优惠券领取规则设置为空，couponActivityId -> {}, userId -> {}, eid -> {}", couponActivity.getId(), userId, eid);
            return CouponErrorCode.MOBILE_AUTO_GET_ERROR.getMessage();
        }

        /* 自主领取规则 */
        CouponActivityAutoGetDTO autoGet = autoGetService.getAutoGetById(autoGetCoupon.getCouponActivityAutoGetId());
        if (ObjectUtil.isNull(autoGet)) {
            log.info("此平台优惠券领取规则设置为空，couponActivityId -> {}, userId -> {}, eid -> {}, autoGetCouponId -> {}", couponActivity.getId(), userId,
                autoGetCoupon.getId());
            return CouponErrorCode.MOBILE_AUTO_GET_ERROR.getMessage();
        }
        // 状态是否启用
        if(!ObjectUtil.equal(CouponActivityStatusEnum.ENABLED.getCode(), autoGet.getStatus())){
            log.info("此平台优惠券领取规则不是启用状态，couponActivityId -> {}, userId -> {}, eid -> {}, autoGetId -> {}", couponActivity.getId(), userId,
                autoGet.getId());
            return CouponErrorCode.COUPON_ACTIVITY_AUTO_GET_STATUS_ERROR.getMessage();
        }
        // 时间
        Date date = new Date();
        if (autoGet.getEndTime().getTime() <= date.getTime()) {
            log.info("此优惠券领取活动已结束，不能领取，couponActivityId -> {}, userId -> {}, eid -> {}, autoGetRuleId -> {}", couponActivity.getId(), userId, eid,
                autoGet.getId());
            return CouponErrorCode.COUPON_ACTIVITY_GET_END_ERROR.getMessage();
        }

        // 校验可领取企业
        String enterpriseTypeCheckResult = enterpriseLimitTypeCheck(eid, userId, couponActivity.getId(), autoGetCoupon.getId(),
            autoGet.getConditionEnterpriseType(), autoGet.getConditionEnterpriseTypeValue(), etype);
        if (StrUtil.isNotBlank(enterpriseTypeCheckResult)) {
            return enterpriseTypeCheckResult;
        }

        // 普通用户
        if (ObjectUtil.equal(CouponGetEnterpriseLimitTypeEnum.ENTERPRISE.getCode(), autoGet.getConditionUserType())) {
            if (ObjectUtil.isNotNull(currentMember) && currentMember == 1) {
                log.info("此优惠券仅普通用户可以领取，couponActivityId -> {}, userId -> {}, eid -> {}, autoGetCouponId -> {}", couponActivity.getId(), userId,
                    autoGetCoupon.getId());
                return CouponErrorCode.MOBILE_AUTO_GET_CURRENT_MEMBER_ONE_ERROR.getMessage();
            }
        }
        // 全部会员
        if (ObjectUtil.equal(CouponGetEnterpriseLimitTypeEnum.MEMBER.getCode(), autoGet.getConditionUserType())) {
            if (ObjectUtil.isNull(currentMember) || currentMember == 0) {
                log.info("此优惠券仅会员可以领取，couponActivityId -> {}, userId -> {}, eid -> {}, autoGetCouponId -> {}", couponActivity.getId(), userId,
                    autoGetCoupon.getId());
                return CouponErrorCode.MOBILE_AUTO_GET_CURRENT_MEMBER_ZERO_ERROR.getMessage();
            }
        }
        // 部分会员
        if (ObjectUtil.equal(CouponGetEnterpriseLimitTypeEnum.PART_MEMBER.getCode(), autoGet.getConditionUserType())) {
            if (ObjectUtil.isNull(currentMember) || currentMember == 0) {
                log.info("此优惠券仅会员可以领取，couponActivityId -> {}, userId -> {}, eid -> {}, autoGetCouponId -> {}", couponActivity.getId(), userId,
                    autoGetCoupon.getId());
                return CouponErrorCode.MOBILE_AUTO_GET_CURRENT_MEMBER_ZERO_ERROR.getMessage();
            }
            // 查询可领取会员企业
            List<CouponActivityAutoGetEnterpriseLimitDO> autoGetEnterpriseList = autoGetEnterpriseLimitService.getByAutoGetId(autoGet.getId());
            if (CollUtil.isNotEmpty(autoGetEnterpriseList)) {
                Map<Long, List<CouponActivityAutoGetEnterpriseLimitDO>> autoGetEnterpriseMap = autoGetEnterpriseList.stream()
                    .collect(Collectors.groupingBy(CouponActivityAutoGetEnterpriseLimitDO::getEid));
                if (ObjectUtil.isNull(autoGetEnterpriseMap.get(eid))) {
                    log.info("此优惠券仅部分会员可以领取，couponActivityId -> {}, userId -> {}, eid -> {}, autoGetCouponId -> {}", couponActivity.getId(), userId,
                        autoGetCoupon.getId());
                    return CouponErrorCode.MOBILE_AUTO_GET_ENTERPRISE_LIMIT_ERROR.getMessage();
                }
            }
        }

        // 新客校验
        if ((ObjectUtil.equal(CouponGetEnterpriseLimitTypeEnum.NEW_CUSTOMER.getCode(), autoGet.getConditionUserType()))) {
            if (!orderFirstInfoApi.checkNewVisitor(eid, OrderTypeEnum.B2B)) {
                return "此优惠券仅新客用户可以领取，您不能领取";
            }
        }
        /* 可领取数量 */
        int giveNum = ObjectUtil.isNull(autoGetCoupon.getGiveNum()) ? 0 : autoGetCoupon.getGiveNum().intValue();
        String giveNumCheckResult = giveNumCheck("checkGetForPlatform", userId, eid, giveNum, couponActivity.getId(), autoGet.getId(),
            autoGetCoupon.getId(), 1);
        if (StrUtil.isNotBlank(giveNumCheckResult)) {
            return giveNumCheckResult;
        }
        autoGetIdList.add(autoGet.getId());
        return null;
    }

    /**
     * 查询已领取优惠券数量
     * @param method
     * @param userId
     * @param eid
     * @param giveNum
     * @param couponActivityId
     * @param autoGetOrRulesId
     * @param relationId
     * @param businessType 1-平台创建的自动领券活动 2-商家创建的自动领券活动
     * @return
     */
    private String giveNumCheck(String method, Long userId, Long eid, int giveNum, Long couponActivityId, Long autoGetOrRulesId, Long relationId, Integer businessType) {
        // 查询已领取优惠券
        Integer getCount = couponService.getEffectiveCountWithoutScrapByEid(eid, couponActivityId, autoGetOrRulesId, businessType);
        if (giveNum <= getCount) {
            log.info(method + ", 此优惠券领取数量已达到上限，不能再领取，userId -> {}, eid -> {}, couponActivityId -> {}, autoGetId -> {}, autoGetCouponId -> {}",
                userId, eid, couponActivityId, autoGetOrRulesId, relationId);
            return CouponErrorCode.MOBILE_AUTO_GET_COUNT_ERROR.getMessage();
        }
        return null;
    }

    private String enterpriseLimitTypeCheck(Long eid, Long userId, Long couponActivityId, Long rulesId, Integer enterpriseType,
                                            String enterpriseTypeValue, Integer etype) {
        if (ObjectUtil.equal(CouponGiveEnterpriseLimitTypeEnum.PART_ENTERPRISE.getCode(), enterpriseType)) {
            List<String> conditionEnterpriseTypeList = Convert.toList(String.class, enterpriseTypeValue);
            if (!conditionEnterpriseTypeList.contains(etype.toString())) {
                log.info("此优惠券仅部分企业类型可以领取，couponActivityId -> {}, userId -> {}, eid -> {}, autoGetCouponId -> {}", couponActivityId, userId,
                    rulesId);
                return CouponErrorCode.MOBILE_AUTO_GET_ENTERPRISE_TYPE_ERROR.getMessage();
            }
        }
        return null;
    }

    public Map<String, Boolean> checkUpdateAutoGiveFixed(Date beginTime, Date endTime, long nowTime, long timeDifference) {
        Map<String, Boolean> map = new HashMap<>();
        boolean updateFlag = true;
        boolean giveFlag = true;
        // 开始之前5分钟不能修改
        if ((beginTime.getTime() - nowTime) <= timeDifference) {
            updateFlag = false;
        }
        // 结束之前5分钟不能发放
        if ((endTime.getTime() - nowTime) <= timeDifference) {
            giveFlag = false;
        }
        map.put("updateFlag", updateFlag);
        map.put("giveFlag", giveFlag);
        return map;
    }

    private Long saveOrUpdate(Long id, Long currentUserId, CouponActivityAutoGetDO couponActivityAutoGetDO) {
        Date date = new Date();
        // 运营后台没有企业信息，企业信息字段不插入、不更新
        if (Objects.isNull(id) || id == 0) {
            // 保存
            couponActivityAutoGetDO.setStatus(CouponActivityStatusEnum.ENABLED.getCode());
            couponActivityAutoGetDO.setCreateUser(currentUserId);
            couponActivityAutoGetDO.setCreateTime(date);
            this.save(couponActivityAutoGetDO);
            //            this.baseMapper.insertCouponActivity(couponActivityAutoGiveDO);
        } else {
            // 更新
            couponActivityAutoGetDO.setUpdateUser(currentUserId);
            couponActivityAutoGetDO.setUpdateTime(date);
            this.updateById(couponActivityAutoGetDO);
            //            this.baseMapper.updateCouponActivityById(couponActivityAutoGiveDO);
        }
        return couponActivityAutoGetDO.getId();
    }

    private CouponActivityAutoGetDetailDTO getDetail(Long id) {
        CouponActivityAutoGetDO couponActivityAutoGetDO;
        CouponActivityAutoGetDetailDTO couponActivityAutoGetDetailDTO;
        couponActivityAutoGetDO = this.getById(id);
        couponActivityAutoGetDetailDTO = Optional.ofNullable(PojoUtils.map(couponActivityAutoGetDO, CouponActivityAutoGetDetailDTO.class))
            .orElseThrow(() -> new BusinessException(CouponErrorCode.DATA_NOT_EXIST));
        List<String> emptyList = ListUtil.empty();
        // 企业类型
        String enterpriseTypeSelected = couponActivityAutoGetDO.getConditionEnterpriseTypeValue();
        List<String> enterpriseTypeSelectedList = StrUtil.isBlank(enterpriseTypeSelected) ? emptyList
            : Arrays.asList(enterpriseTypeSelected.split(","));
        couponActivityAutoGetDetailDTO.setConditionEnterpriseTypeValueList(enterpriseTypeSelectedList);
        return couponActivityAutoGetDetailDTO;
    }

    private LambdaQueryWrapper<CouponActivityAutoGetDO> getCouponActivityWrapper(QueryCouponActivityAutoGetRequest request) {
        LambdaQueryWrapper<CouponActivityAutoGetDO> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(request.getId()) && request.getId() != 0) {
            queryWrapper.eq(CouponActivityAutoGetDO::getId, request.getId());
        }
        if (StrUtil.isNotEmpty(request.getName())) {
            queryWrapper.like(CouponActivityAutoGetDO::getName, request.getName());
        }
        if (ObjectUtil.isNotNull(request.getStatus()) && request.getStatus() != 0) {
            queryWrapper.eq(CouponActivityAutoGetDO::getStatus, request.getStatus());
        }
        if (ObjectUtil.isNotNull(request.getCreateUser()) && request.getCreateUser() != 0) {
            queryWrapper.eq(CouponActivityAutoGetDO::getCreateUser, request.getCreateUser());
        }
        if (ObjectUtil.isNotNull(request.getActivityStatus()) && request.getActivityStatus() != 0) {
            // 活动状态：1-未开始 2-进行中 3-已结束
            Date date = new Date();
            if (request.getActivityStatus() == 1) {
                // 查开始时间大于当前时间的
                queryWrapper.gt(CouponActivityAutoGetDO::getBeginTime, DateUtil.beginOfDay(date));
            }
            if (request.getActivityStatus() == 2) {
                // 查开始时间小于等于当前时间的，且结束时间大于当前时间的
                queryWrapper.le(CouponActivityAutoGetDO::getBeginTime, DateUtil.beginOfDay(date));
                queryWrapper.gt(CouponActivityAutoGetDO::getEndTime, DateUtil.beginOfDay(date));
            }
            if (request.getActivityStatus() == 3) {
                // 查结束时间小于等于当前时间的
                queryWrapper.le(CouponActivityAutoGetDO::getEndTime, DateUtil.beginOfDay(date));
            }
        }
        if (ObjectUtil.isNotNull(request.getBeginCreateTime())) {
            queryWrapper.ge(CouponActivityAutoGetDO::getCreateTime, DateUtil.beginOfDay(request.getBeginCreateTime()));
        }
        if (ObjectUtil.isNotNull(request.getEndCreateTime())) {
            queryWrapper.le(CouponActivityAutoGetDO::getCreateTime, DateUtil.endOfDay(request.getEndCreateTime()));
        }
        queryWrapper.orderByDesc(CouponActivityAutoGetDO::getCreateTime);
        return queryWrapper;
    }
}
