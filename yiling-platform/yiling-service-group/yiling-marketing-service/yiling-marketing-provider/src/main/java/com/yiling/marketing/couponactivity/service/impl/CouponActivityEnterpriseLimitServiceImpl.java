package com.yiling.marketing.couponactivity.service.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.redis.RedisDistributedLock;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.common.enums.CouponActivityGiveRecordStatusEnum;
import com.yiling.marketing.common.enums.CouponActivityLogTypeEnum;
import com.yiling.marketing.common.enums.CouponActivityResultTypeEnum;
import com.yiling.marketing.common.enums.CouponActivityStatusEnum;
import com.yiling.marketing.common.enums.CouponErrorCode;
import com.yiling.marketing.common.enums.CouponGetTypeEnum;
import com.yiling.marketing.common.enums.CouponStatusEnum;
import com.yiling.marketing.common.enums.CouponUsedStatusEnum;
import com.yiling.marketing.common.util.CouponUtil;
import com.yiling.marketing.coupon.dto.CouponDTO;
import com.yiling.marketing.coupon.dto.request.SaveCouponRequest;
import com.yiling.marketing.coupon.service.CouponService;
import com.yiling.marketing.couponactivity.dao.CouponActivityEnterpriseLimitMapper;
import com.yiling.marketing.couponactivity.dto.CouponActivityDetailDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityEnterpriseDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityEnterpriseGiveDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityEnterpriseLimitDTO;
import com.yiling.marketing.couponactivity.dto.request.DeleteCouponActivityEnterpriseGiveRecordRequest;
import com.yiling.marketing.couponactivity.dto.request.DeleteCouponActivityEnterpriseLimitRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityDetailRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityEnterpriseGiveRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityEnterpriseLimitRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityEnterpriseRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityGiveEnterpriseInfoRequest;
import com.yiling.marketing.couponactivity.dto.request.SaveCouponActivityEnterpriseLimitDetailRequest;
import com.yiling.marketing.couponactivity.dto.request.SaveCouponActivityEnterpriseLimitRequest;
import com.yiling.marketing.couponactivity.dto.request.SaveCouponActivityGiveEnterpriseInfoDetailRequest;
import com.yiling.marketing.couponactivity.dto.request.SaveCouponActivityGiveEnterpriseInfoRequest;
import com.yiling.marketing.couponactivity.entity.CouponActivityDO;
import com.yiling.marketing.couponactivity.entity.CouponActivityEnterpriseLimitDO;
import com.yiling.marketing.couponactivity.enums.CouponActivityErrorCode;
import com.yiling.marketing.couponactivity.enums.CouponActivityUseDateTypeEnum;
import com.yiling.marketing.couponactivity.service.CouponActivityEnterpriseLimitService;
import com.yiling.marketing.couponactivity.service.CouponActivityLogService;
import com.yiling.marketing.couponactivity.service.CouponActivityService;
import com.yiling.marketing.couponactivityautoget.dto.CouponActivityAutoGetDTO;
import com.yiling.marketing.couponactivityautoget.service.CouponActivityAutoGetCouponService;
import com.yiling.marketing.couponactivityautoget.service.CouponActivityAutoGetService;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGetCouponDTO;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveCouponDTO;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveDTO;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveRecordDTO;
import com.yiling.marketing.couponactivityautogive.dto.request.UpdateAutoGiveRecordStatusRequest;
import com.yiling.marketing.couponactivityautogive.entity.CouponActivityAutoGiveEnterpriseInfoDO;
import com.yiling.marketing.couponactivityautogive.entity.CouponActivityAutoGiveRecordDO;
import com.yiling.marketing.couponactivityautogive.service.CouponActivityAutoGiveCouponService;
import com.yiling.marketing.couponactivityautogive.service.CouponActivityAutoGiveEnterpriseInfoService;
import com.yiling.marketing.couponactivityautogive.service.CouponActivityAutoGiveRecordService;
import com.yiling.marketing.couponactivityautogive.service.CouponActivityAutoGiveService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 优惠券活动供应商限制表 服务实现类
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
@Slf4j
@Service
public class CouponActivityEnterpriseLimitServiceImpl extends BaseServiceImpl<CouponActivityEnterpriseLimitMapper, CouponActivityEnterpriseLimitDO>
                                                      implements CouponActivityEnterpriseLimitService {

    @Autowired
    protected RedisDistributedLock                      redisDistributedLock;
    @Autowired
    private CouponActivityAutoGiveRecordService         autoGiveRecordService;
    @Autowired
    private CouponService                               couponService;
    @Autowired
    private CouponActivityService                       couponActivityService;
    @Autowired
    private CouponActivityLogService                    couponActivityLogService;
    @Autowired
    private CouponActivityAutoGiveCouponService         autoGiveCouponService;
    @Autowired
    private CouponActivityAutoGetCouponService          autoGetCouponService;
    @Autowired
    private CouponActivityAutoGiveService               autoGiveService;
    @Autowired
    private CouponActivityAutoGetService                autoGetService;
    @Autowired
    private CouponActivityAutoGiveEnterpriseInfoService autoGiveEnterpriseInfoService;

    @Override
    public Boolean insertBatch(List<CouponActivityEnterpriseLimitDO> list) {
        if (CollUtil.isNotEmpty(list)) {
            return this.saveBatch(list);
        }
        return false;
    }

    @Override
    public Page<CouponActivityEnterpriseDTO> queryEnterpriseListPage(QueryCouponActivityEnterpriseRequest request) {
        Page<CouponActivityEnterpriseDTO> page = new Page<>();
        if (ObjectUtil.isNull(request)) {
            return page;
        }

        //        // 根据id、name查询企业信息
        //        QueryEnterprisePageListRequest enterprisePageListRequest = PojoUtils.map(request, QueryEnterprisePageListRequest.class);
        //        enterprisePageListRequest.setAuthStatus(EnterpriseAuthStatusEnum.AUTH_SUCCESS.getCode());
        //        enterprisePageListRequest.setStatus(EnableStatusEnum.ENABLED.getCode());
        //        enterprisePageListRequest.setMallFlag(1);
        //        // B2B不展示工业类型
        //        enterprisePageListRequest.setNotInTypeList(new ArrayList(){{add(1);}});
        //        if (ObjectUtil.isNotNull(request.getEid()) && request.getEid() != 0) {
        //            enterprisePageListRequest.setId(request.getEid());
        //        }
        //        if (StrUtil.isNotBlank(request.getEname())) {
        //            enterprisePageListRequest.setName(request.getEname());
        //        }
        //        Page<EnterpriseDTO> enterpriseDTOPage = enterpriseApi.pageList(enterprisePageListRequest);
        //        List<CouponActivityEnterpriseDTO> list = new ArrayList<>();
        //        if (CollUtil.isNotEmpty(enterpriseDTOPage.getRecords())) {
        //            CouponActivityEnterpriseDTO couponActivityEnterprise;
        //            int index = 0;
        //            for (EnterpriseDTO enterprise : enterpriseDTOPage.getRecords()) {
        //                couponActivityEnterprise = new CouponActivityEnterpriseDTO();
        //                couponActivityEnterprise.setEid(enterprise.getId());
        //                couponActivityEnterprise.setEname(enterprise.getName());
        //                list.add(index, couponActivityEnterprise);
        //                index++;
        //            }
        //        }
        //        page = PojoUtils.map(enterpriseDTOPage, CouponActivityEnterpriseDTO.class);
        //        page.setRecords(list);
        return page;
    }

    @Override
    public Page<CouponActivityEnterpriseLimitDTO> pageList(QueryCouponActivityEnterpriseLimitRequest request) {
        CouponActivityDO couponActivityDO = couponActivityService.getById(request.getCouponActivityId());
        if (ObjectUtil.isNotNull(couponActivityDO)) {
            if (couponActivityDO.getEid() != 0) {
                Page<CouponActivityEnterpriseLimitDTO> enterpriseLimitDTOPage = new Page<>();
                List<CouponActivityEnterpriseLimitDTO> couponActivityEnterpriseLimitDTOS = new ArrayList<>();
                CouponActivityEnterpriseLimitDTO couponActivityEnterpriseLimitDTO = new CouponActivityEnterpriseLimitDTO();
                couponActivityEnterpriseLimitDTO.setEid(couponActivityDO.getEid());
                couponActivityEnterpriseLimitDTOS.add(couponActivityEnterpriseLimitDTO);
                enterpriseLimitDTOPage.setRecords(couponActivityEnterpriseLimitDTOS);
                return enterpriseLimitDTOPage;
            }
        }
        Page<CouponActivityEnterpriseLimitDO> enterpriseLimitDOPage = this.baseMapper.pageList(request.getPage(), request);
        return PojoUtils.map(enterpriseLimitDOPage, CouponActivityEnterpriseLimitDTO.class);
    }

    @Override
    public Boolean saveEnterpriseLimit(SaveCouponActivityEnterpriseLimitRequest request) {
        if (ObjectUtil.isNull(request) || CollUtil.isEmpty(request.getEnterpriseLimitList())) {
            return false;
        }

        long couponActivityId = request.getEnterpriseLimitList().get(0).getCouponActivityId();
        String lockName = CouponUtil.getLockName("couponActivityEnterpriseLimit", "saveEnterpriseLimit");
        String lockId = "";
        try {
            lockId = redisDistributedLock.lock(lockName, 60, 60, TimeUnit.SECONDS);
            if (StringUtils.isEmpty(lockId)) {
                log.info("优惠券活动添加供应商：系统繁忙, 加锁失败，请稍等重试, couponActivityId -> {}, request -> {}", couponActivityId, JSONObject.toJSONString(request));
                throw new BusinessException(CouponErrorCode.REDIS_LOCK_SAVE_ENTERPRISE_LIMIT_DOING_ERROR);
            }
            // 查询此优惠券活动已添加企业，eid不重复则新增、否则更新
            LambdaQueryWrapper<CouponActivityEnterpriseLimitDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(CouponActivityEnterpriseLimitDO::getCouponActivityId, couponActivityId);
            List<CouponActivityEnterpriseLimitDO> dbList = this.list(lambdaQueryWrapper);
            Map<Long, CouponActivityEnterpriseLimitDO> dbMap = new HashMap<>();
            if (CollUtil.isNotEmpty(dbList)) {
                dbMap = dbList.stream().collect(Collectors.toMap(e -> e.getEid(), e -> e, (v1, v2) -> v1));
            }

            // 待新增数据
            List<SaveCouponActivityEnterpriseLimitDetailRequest> insertList = new ArrayList<>();

            Long userId = request.getOpUserId();
            Date date = new Date();
            for (SaveCouponActivityEnterpriseLimitDetailRequest entity : request.getEnterpriseLimitList()) {
                CouponActivityEnterpriseLimitDO enterpriseLimitDb = dbMap.get(entity.getEid());
                if (ObjectUtil.isNotNull(enterpriseLimitDb)) {
                    String errMsg = MessageFormat.format(CouponErrorCode.ENTERPRISE_LIMIT_EXIST_ERROR.getMessage(), enterpriseLimitDb.getEid());
                    throw new BusinessException(CouponErrorCode.ENTERPRISE_LIMIT_EXIST_ERROR, errMsg);
                }
                // 新增
                entity.setCreateUser(userId);
                entity.setCreateTime(date);
                insertList.add(entity);
            }

            if (CollUtil.isNotEmpty(insertList)) {
                List<CouponActivityEnterpriseLimitDO> doInsertList = PojoUtils.map(insertList, CouponActivityEnterpriseLimitDO.class);
                this.saveBatch(doInsertList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(CouponErrorCode.ENTERPRISE_LIMIT_EXIST_ERROR, e.getMessage());
        } finally {
            redisDistributedLock.releaseLock(lockName, lockId);
        }
        return true;
    }

    @Override
    public Boolean deleteEnterpriseLimit(DeleteCouponActivityEnterpriseLimitRequest request) {
        if (ObjectUtil.isNull(request) || CollUtil.isEmpty(request.getIds()) || ObjectUtil.isNull(request.getCouponActivityId())) {
            throw new BusinessException(ResultCode.PARAM_MISS);
        }

        Long userId = request.getOpUserId();
        Long couponActivityId = request.getCouponActivityId();
        List<Long> ids = request.getIds();
        Date date = new Date();
        try {
            // 运营后台修改优惠券活动，已开始不能删除商家设置
            if(ObjectUtil.equal(1, request.getPlatformType())){
                String errorMsg = couponActivityService.deleteCouponActivityLimitCheck(request.getCurrentEid(), couponActivityId);
                if(StrUtil.isNotBlank(errorMsg)){
                    throw new BusinessException(CouponErrorCode.ACTIVITY_RUNNING_DELETE_ENTERPRISE_LIMIT_ERROR, errorMsg + "，禁止删除商家");
                }
            }

            // 删除条件
            LambdaQueryWrapper<CouponActivityEnterpriseLimitDO> deleteWrapper = new LambdaQueryWrapper<>();
            deleteWrapper.eq(CouponActivityEnterpriseLimitDO::getCouponActivityId, couponActivityId);
            deleteWrapper.in(CouponActivityEnterpriseLimitDO::getId, ids);
            // 删除
            CouponActivityEnterpriseLimitDO couponActivityEnterpriseLimit = new CouponActivityEnterpriseLimitDO();
            couponActivityEnterpriseLimit.setOpUserId(userId);
            couponActivityEnterpriseLimit.setUpdateUser(userId);
            couponActivityEnterpriseLimit.setUpdateTime(date);
            this.batchDeleteWithFill(couponActivityEnterpriseLimit, deleteWrapper);
        } catch (Exception e) {
            log.error("删除优惠券活动已添加供应商失败，couponActivityId -> {}, ids -> {}, exception -> {}", couponActivityId, ids, e);
            e.printStackTrace();
            throw new BusinessException(CouponActivityErrorCode.ENTERPRISE_LIMIT_DELETE_ERROR, e.getMessage());
        }
        return true;
    }

    @Override
    public Page<CouponActivityEnterpriseGiveDTO> queryEnterpriseGiveListPage(QueryCouponActivityEnterpriseGiveRequest request) {
        Page<CouponActivityEnterpriseGiveDTO> page = new Page<>();
        if (ObjectUtil.isNull(request)) {
            return page;
        }

        try {
            //            // 查询企业信息
            //            QueryEnterprisePageListRequest enterprisePageListRequest = new QueryEnterprisePageListRequest();
            //            enterprisePageListRequest.setAuthStatus(EnterpriseAuthStatusEnum.AUTH_SUCCESS.getCode());
            //            enterprisePageListRequest.setStatus(EnableStatusEnum.ENABLED.getCode());
            //            enterprisePageListRequest.setMallFlag(1);
            //            // B2B不展示工业类型
            //            enterprisePageListRequest.setNotInTypeList(new ArrayList(){{add(1);}});
            //            if (ObjectUtil.isNotNull(request.getEid()) && request.getEid() != 0) {
            //                enterprisePageListRequest.setId(request.getEid());
            //            }
            //            if (StrUtil.isNotBlank(request.getEname())) {
            //                enterprisePageListRequest.setName(request.getEname());
            //            }
            //            if (ObjectUtil.isNotNull(request.getEtype()) && request.getEtype() != 0) {
            //                enterprisePageListRequest.setType(request.getEtype());
            //            }
            //            if (StrUtil.isNotBlank(request.getRegionCode())) {
            //                enterprisePageListRequest.setProvinceCode(request.getRegionCode());
            //            }
            //            Page<EnterpriseDTO> enterpriseDTOPage = enterpriseApi.pageList(enterprisePageListRequest);
            //            List<CouponActivityEnterpriseGiveDTO> list = new ArrayList<>();
            //            if (CollUtil.isNotEmpty(enterpriseDTOPage.getRecords())) {
            //                CouponActivityEnterpriseGiveDTO couponActivityEnterpriseGive;
            //                int index = 0;
            //                for (EnterpriseDTO enterprise : enterpriseDTOPage.getRecords()) {
            //                    couponActivityEnterpriseGive = new CouponActivityEnterpriseGiveDTO();
            //                    couponActivityEnterpriseGive.setEid(enterprise.getId());
            //                    couponActivityEnterpriseGive.setEname(enterprise.getName());
            //                    couponActivityEnterpriseGive.setEtype(enterprise.getType());
            //                    couponActivityEnterpriseGive.setRegionCode(enterprise.getProvinceCode());
            //                    couponActivityEnterpriseGive.setRegionName(enterprise.getProvinceName());
            //                    couponActivityEnterpriseGive.setAuthStatus(enterprise.getAuthStatus());
            //                    list.add(index, couponActivityEnterpriseGive);
            //                    index++;
            //                }
            //            }
            //
            //            page = PojoUtils.map(enterpriseDTOPage, CouponActivityEnterpriseGiveDTO.class);
            //            page.setRecords(list);
        } catch (Exception e) {
            log.error("查询待发放企业信息异常，request -> {}, exception -> {}", JSONObject.toJSONString(request), e);
            e.printStackTrace();
            throw new BusinessException(CouponActivityErrorCode.GIVE_ENTERPRISE_QUERY_ENTERPRISE_ERROR, e.getMessage());
        }
        return page;
    }

    @Override
    public Page<CouponActivityAutoGiveEnterpriseInfoDO> queryEnterpriseGiveRecordListPage(QueryCouponActivityGiveEnterpriseInfoRequest request) {
        Page<CouponActivityAutoGiveEnterpriseInfoDO> page = new Page();
        if (ObjectUtil.isNull(request)) {
            return page;
        }

        long couponActivityId = request.getCouponActivityId();
        try {
            // 查询已发放企业信息
            page = autoGiveEnterpriseInfoService.queryGiveEnterpriseInfoListPage(request);
        } catch (Exception e) {
            log.error("查询已发放企业信息列表异常，couponActivityId -> {}, request -> {}, exception -> {}", couponActivityId, JSONObject.toJSONString(request), e);
            e.printStackTrace();
            throw new BusinessException(CouponActivityErrorCode.GIVE_ENTERPRISE_QUERY_RECORD_ERROR, e.getMessage());
        }
        return page;
    }

    @Override
    public Boolean addGiveEnterpriseInfo(SaveCouponActivityGiveEnterpriseInfoRequest request) {
        if (ObjectUtil.isNull(request) || CollUtil.isEmpty(request.getGiveDetailList()) || ObjectUtil.isNull(request.getUnifyGiveNum())) {
            throw new BusinessException(ResultCode.PARAM_MISS);
        }

        // 登录人信息
        Long userId = request.getOpUserId();
        Long couponActivityId = null;
        for (SaveCouponActivityGiveEnterpriseInfoDetailRequest detail : request.getGiveDetailList()) {
            if(ObjectUtil.isNotNull(detail.getCouponActivityId())){
                couponActivityId = detail.getCouponActivityId();
                break;
            }
        }
        if(ObjectUtil.isNull(couponActivityId)){
            throw new BusinessException(CouponErrorCode.DATA_NOT_EXIST, "优惠券活动id不能为空");
        }

        try {
            // 发放
            handlerCouponActivityGive(request, userId, couponActivityId);
            return true;
        } catch (Exception e) {
            log.info("发放-添加供应商失败，couponActivityId -> {}, request -> {}, exception -> {}", couponActivityId, JSONObject.toJSONString(request), e);
            // 优惠券活动操作日志
            String faileReason = "优惠券发放失败：系统异常, "+ e.getMessage();
            faileReason = faileReason.length() < 200 ? faileReason:faileReason.substring(0, 200);
            couponActivityLogService.insertCouponActivityLog(couponActivityId, CouponActivityLogTypeEnum.GIVE.getCode(),
                JSONObject.toJSONString(""), CouponActivityResultTypeEnum.FAIL.getCode(), faileReason, userId);
            e.printStackTrace();
            throw new BusinessException(CouponActivityErrorCode.GIVE_ENTERPRISE_ERROR, e.getMessage());
        }
    }

    @GlobalTransactional
    private void handlerCouponActivityGive(SaveCouponActivityGiveEnterpriseInfoRequest request, Long userId, Long couponActivityId) {
        log.info("request -> {}, couponActivityId -> {}, userId -> {}", request, couponActivityId, userId);
        String userName = request.getOpUserName();
        Integer unifyGiveNum = request.getUnifyGiveNum();
        List<SaveCouponActivityGiveEnterpriseInfoDetailRequest> giveDetailList = request.getGiveDetailList();
        Date date = new Date();

        // 去重
        List<Long> eidAllList = giveDetailList.stream().map(SaveCouponActivityGiveEnterpriseInfoDetailRequest::getEid)
            .collect(Collectors.toList());
        Set<Long> set = new HashSet<>(eidAllList);
        Collection difference = CollectionUtils.disjunction(eidAllList, set);
        if (CollUtil.isNotEmpty(difference)) {
            List<Long> differenceList = new ArrayList<>(difference);
            String format = MessageFormat.format(CouponErrorCode.GIVE_ENTERPRISE_INFO_REPEAT_ERROR.getMessage(), differenceList.toString());
            throw new BusinessException(CouponErrorCode.GIVE_ENTERPRISE_INFO_REPEAT_ERROR, format);
        }

        // 查询优惠券活动信息
        QueryCouponActivityDetailRequest detailRequest = new QueryCouponActivityDetailRequest();
        detailRequest.setId(couponActivityId);
        detailRequest.setEid(request.getOwnEid());
        CouponActivityDetailDTO couponActivity = couponActivityService.getCouponActivityById(detailRequest);
        if (ObjectUtil.isNull(couponActivity)) {
            throw new BusinessException(CouponErrorCode.COUPON_ACTIVITY_NOT_EXIST_ERROR);
        }
        // 优惠券活动已发放数量
        List<Map<String, Long>> giveCountList = couponService.getGiveCountByCouponActivityId(Arrays.asList(couponActivityId));
        // 优惠券活动校验
        List<Integer> countList = new ArrayList<>();
        CouponActivityDO couponActivityDo = PojoUtils.map(couponActivity, CouponActivityDO.class);
        String couponActivityErrorMsg = couponActivityService.checkCouponActivity(couponActivityDo, countList, giveCountList);
        if (StrUtil.isNotBlank(couponActivityErrorMsg)) {
            throw new BusinessException(CouponErrorCode.AUTO_GIVE_ERROR, couponActivityErrorMsg);
        }

        // 关联自动发放、自动领取活动校验
        Integer useDateType = couponActivity.getUseDateType();
        String errorMsg = giveAndGetCheck(useDateType, date, couponActivity);
        if (StrUtil.isNotBlank(errorMsg)) {
            throw new BusinessException(CouponErrorCode.AUTO_GIVE_ERROR, errorMsg);
        }

        // 根据优惠券有效期类型设置时间
        Map<String, Date> timeMap = CouponUtil.buildCouponBeginEndTime(date, couponActivity.getBeginTime(), couponActivity.getEndTime(),
            useDateType, couponActivity.getExpiryDays());
        Date beginTime = timeMap.get("beginTime");
        Date endTime = timeMap.get("endTime");

        // 优惠券卡包
        List<SaveCouponRequest> couponRequestList = new ArrayList<>();
        // 待保存发放记录
        List<CouponActivityAutoGiveRecordDO> newAutoGiveRecordList = new ArrayList<>();

        for (SaveCouponActivityGiveEnterpriseInfoDetailRequest infoRequest : giveDetailList) {
            infoRequest.setOwnEid(request.getOwnEid());
            infoRequest.setOwnEname(request.getOwnEname());
            infoRequest.setGiveNum(unifyGiveNum);
            // 生成优惠券到用户卡包
            for (int i = 0; i < infoRequest.getGiveNum(); i++) {
                SaveCouponRequest saveCouponRequest = buildSaveCouponRequest(userId, userName, couponActivityId, couponActivity.getName(), date,
                    beginTime, endTime, infoRequest);
                couponRequestList.add(saveCouponRequest);
            }
            // 生成发放记录
            CouponActivityAutoGiveRecordDO newRecord = buildCouponActivityAutoGiveRecordDO(userId, couponActivityId, date, couponActivity,
                infoRequest);
            newAutoGiveRecordList.add(newRecord);
        }

        // 保存发放记录，状态待发放
        if (CollUtil.isNotEmpty(newAutoGiveRecordList)) {
            autoGiveRecordService.saveAutoGiveRecordWithWaitStatus(newAutoGiveRecordList);
        }
        // 保存/更新发放企业信息
        request.setCouponActivityFlag(true);
        request.setEnterpriseFlag(false);
        autoGiveEnterpriseInfoService.saveOrUpdate(request);

        // 优惠券卡包
        if (CollUtil.isNotEmpty(couponRequestList)) {
            couponService.insertBatch(couponRequestList);
        }
        // 更新发放记录，已发放
        List<UpdateAutoGiveRecordStatusRequest> updateRecordStatusList = new ArrayList<>();
        UpdateAutoGiveRecordStatusRequest recordStatusRequest;
        for (CouponActivityAutoGiveRecordDO record : newAutoGiveRecordList) {
            recordStatusRequest = new UpdateAutoGiveRecordStatusRequest();
            recordStatusRequest.setId(record.getId());
            recordStatusRequest.setStatus(CouponActivityGiveRecordStatusEnum.SUCCESS.getCode());
            updateRecordStatusList.add(recordStatusRequest);
        }
        autoGiveRecordService.updateRecordStatus(updateRecordStatusList);

        // 优惠券活动操作日志
        couponActivityLogService.insertCouponActivityLog(couponActivityId, CouponActivityLogTypeEnum.GIVE.getCode(),
                JSONObject.toJSONString(request.getGiveDetailList().get(0)), CouponActivityResultTypeEnum.SUCCESS.getCode(), "", userId);

        // 修改已经发放的数量
        couponActivityService.updateHasGiveNum(couponActivityId,couponRequestList.size());
    }

    private CouponActivityAutoGiveRecordDO buildCouponActivityAutoGiveRecordDO(Long userId, long couponActivityId, Date date,
                                                                               CouponActivityDetailDTO couponActivity,
                                                                               SaveCouponActivityGiveEnterpriseInfoDetailRequest infoRequest) {
        CouponActivityAutoGiveRecordDO newRecord = new CouponActivityAutoGiveRecordDO();
        newRecord.setCouponActivityId(couponActivityId);
        newRecord.setCouponActivityName(couponActivity.getName());
        newRecord.setEid(infoRequest.getEid());
        newRecord.setEname(infoRequest.getEname());
        newRecord.setGiveNum(infoRequest.getGiveNum());
        newRecord.setGetType(CouponGetTypeEnum.GIVE.getCode());
        newRecord.setStatus(CouponActivityGiveRecordStatusEnum.WAIT.getCode());
        newRecord.setCreateUser(userId);
        newRecord.setCreateTime(date);
        newRecord.setOwnEid(couponActivity.getEid());
        newRecord.setOwnEname(couponActivity.getEname());
        newRecord.setBatchNumber(CouponUtil.getMillisecondTime());
        return newRecord;
    }

    private SaveCouponRequest buildSaveCouponRequest(Long userId, String userName, long couponActivityId, String couponActivityName, Date date,
                                                     Date beginTime, Date endTime, SaveCouponActivityGiveEnterpriseInfoDetailRequest infoRequest) {
        SaveCouponRequest saveCouponRequest = new SaveCouponRequest();
        saveCouponRequest.setCouponActivityId(couponActivityId);
        saveCouponRequest.setCouponActivityName(couponActivityName);
        saveCouponRequest.setEid(infoRequest.getEid());
        saveCouponRequest.setEname(infoRequest.getEname());
        saveCouponRequest.setGetType(CouponGetTypeEnum.GIVE.getCode());
        saveCouponRequest.setGetUserId(userId);
        saveCouponRequest.setGetUserName(userName);
        saveCouponRequest.setGetTime(date);
        saveCouponRequest.setBeginTime(beginTime);
        saveCouponRequest.setEndTime(endTime);
        saveCouponRequest.setUsedStatus(CouponUsedStatusEnum.NOT_USED.getCode());
        saveCouponRequest.setStatus(CouponStatusEnum.NORMAL_COUPON.getCode());
        saveCouponRequest.setCreateUser(userId);
        saveCouponRequest.setCreateTime(date);
        return saveCouponRequest;
    }

    @Override
    public Boolean deleteEnterpriseGiveRecord(DeleteCouponActivityEnterpriseGiveRecordRequest request) {
        if (ObjectUtil.isNull(request) || CollUtil.isEmpty(request.getIds())) {
            return false;
        }

        Long currentUserId = request.getUserId();
        Long couponActivityId = request.getCouponActivityId();
        List<Long> ids = request.getIds();

        // todo coupon 加锁，与添加同一个key（发放、领取，优惠券活动需要相同的锁）
        try {
            // 查询优惠券活动信息
            QueryCouponActivityDetailRequest detailRequest = new QueryCouponActivityDetailRequest();
            detailRequest.setId(couponActivityId);
            detailRequest.setEid(request.getEid());
            CouponActivityDetailDTO couponActivity = couponActivityService.getCouponActivityById(detailRequest);
            if (ObjectUtil.isNull(couponActivity)) {
                throw new BusinessException(CouponErrorCode.COUPON_ACTIVITY_NOT_EXIST_ERROR);
            }
            if (!ObjectUtil.equal(CouponActivityStatusEnum.ENABLED.getCode(), couponActivity.getStatus())) {
                throw new BusinessException(CouponErrorCode.COUPON_ACTIVITY_STATUS_GIVE_ERROR);
            }

            // 根据id查询发放企业记录
            List<CouponActivityAutoGiveRecordDTO> giveRecordList = autoGiveRecordService.getByIdList(ids);
            List<Long> eidList = giveRecordList.stream().filter(r -> ObjectUtil.isNotNull(r.getEid())).map(CouponActivityAutoGiveRecordDTO::getEid)
                .collect(Collectors.toList());

            // 根据优惠券id查询用户卡包
            List<CouponDTO> couponList = couponService.getByCouponActivityIdForGiveDelete(couponActivityId, eidList);
            // 未使用优惠券
            Map<Long, List<CouponDTO>> couponNotUsedMap = new HashMap<>();
            // 已使用优惠券
            Map<Long, List<CouponDTO>> couponUsedMap = new HashMap<>();
            if (CollUtil.isNotEmpty(couponList)) {
                couponNotUsedMap = couponList.stream().filter(c -> ObjectUtil.equal(CouponUsedStatusEnum.NOT_USED.getCode(), c.getUsedStatus()))
                    .collect(Collectors.groupingBy(CouponDTO::getEid));
                couponUsedMap = couponList.stream().filter(c -> ObjectUtil.equal(CouponUsedStatusEnum.USED.getCode(), c.getUsedStatus()))
                    .collect(Collectors.groupingBy(CouponDTO::getEid));
            }

            // 待更新的发放企业记录
            List<CouponActivityAutoGiveRecordDO> updateRecordList = new ArrayList<>();
            CouponActivityAutoGiveRecordDO updateRecord;
            // 待删除的发放企业记录
            List<Long> deleteRecordList = new ArrayList<>();
            // 待删除的优惠券
            List<Long> deleteCouponList = new ArrayList<>();

            // 处理发放企业记录，更新/删除
            Date date = new Date();
            for (CouponActivityAutoGiveRecordDTO record : giveRecordList) {
                List<CouponDTO> couponUsedList = couponUsedMap.get(record.getEid());
                if (CollUtil.isNotEmpty(couponUsedList)) {
                    updateRecord = new CouponActivityAutoGiveRecordDO();
                    updateRecord.setId(record.getId());
                    updateRecord.setGiveNum(couponUsedList.size());
                    updateRecord.setUpdateUser(currentUserId);
                    updateRecord.setUpdateTime(date);
                    updateRecordList.add(updateRecord);
                } else {
                    deleteRecordList.add(record.getId());
                }
            }

            // 处理卡包优惠券，删除
            if (CollUtil.isNotEmpty(couponNotUsedMap)) {
                for (Map.Entry<Long, List<CouponDTO>> entry : couponNotUsedMap.entrySet()) {
                    List<CouponDTO> couponNotUsedList = entry.getValue();
                    List<Long> idList = couponNotUsedList.stream().map(CouponDTO::getId).collect(Collectors.toList());
                    deleteCouponList.addAll(idList);
                }
            }

            if (CollUtil.isNotEmpty(deleteCouponList)) {
                couponService.deleteByIds(deleteCouponList, currentUserId);
            }
            if (CollUtil.isNotEmpty(deleteRecordList)) {
                autoGiveRecordService.deleteByIds(deleteRecordList, currentUserId);
            }
            if (CollUtil.isNotEmpty(updateRecordList)) {
                autoGiveRecordService.updateBatch(updateRecordList);
            }
            // 操作日志
            couponActivityLogService.insertCouponActivityLog(couponActivityId, CouponActivityLogTypeEnum.GIVE.getCode(),
                JSONObject.toJSONString(request), CouponActivityResultTypeEnum.SUCCESS.getCode(), "", currentUserId);
        } catch (Exception e) {
            // 优惠券活动操作日志
            String faileReason = "发放-删除供应商失败：" + e.getMessage();
            faileReason = faileReason.length() < 200 ? faileReason:faileReason.substring(0, 200);
            couponActivityLogService.insertCouponActivityLog(couponActivityId, CouponActivityLogTypeEnum.GIVE.getCode(),
                JSONObject.toJSONString(request), CouponActivityResultTypeEnum.FAIL.getCode(), faileReason, currentUserId);
            log.info("发放-删除供应商失败，couponActivityId -> {}, request -> {}, exception -> {}", couponActivityId, JSONObject.toJSONString(request), e);
            e.printStackTrace();
            throw new BusinessException(CouponActivityErrorCode.GIVE_ENTERPRISE_ERROR, e.getMessage());
        }
        return true;
    }

    @Override
    public List<CouponActivityEnterpriseLimitDO> getByEid(Long eid) {
        if (ObjectUtil.isNull(eid)) {
            return ListUtil.empty();
        }
        LambdaQueryWrapper<CouponActivityEnterpriseLimitDO> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(CouponActivityEnterpriseLimitDO::getEid, eid);
        return this.list(queryWrapper);
    }

    @Override
    public List<CouponActivityEnterpriseLimitDO> getByEidList(List<Long> eidList) {
        if (CollUtil.isEmpty(eidList)) {
            return ListUtil.empty();
        }
        LambdaQueryWrapper<CouponActivityEnterpriseLimitDO> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(CouponActivityEnterpriseLimitDO::getEid, eidList);
        return this.list(queryWrapper);
    }

    @Override
    public List<CouponActivityEnterpriseLimitDO> getByCouponActivityIdList(List<Long> couponActivityIdList) {
        if (CollUtil.isEmpty(couponActivityIdList)) {
            return ListUtil.empty();
        }
        LambdaQueryWrapper<CouponActivityEnterpriseLimitDO> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(CouponActivityEnterpriseLimitDO::getCouponActivityId, couponActivityIdList);
        return this.list(queryWrapper);
    }

    @Override
    public List<CouponActivityEnterpriseLimitDO> getByCouponActivityId(Long couponActivityId) {
        if (ObjectUtil.isNull(couponActivityId)) {
            return ListUtil.empty();
        }
        LambdaQueryWrapper<CouponActivityEnterpriseLimitDO> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(CouponActivityEnterpriseLimitDO::getCouponActivityId, couponActivityId);
        return this.list(queryWrapper);
    }

    @Override
    public Integer deleteByCouponActivityId(DeleteCouponActivityEnterpriseLimitRequest request) {
        if(ObjectUtil.isNull(request) || ObjectUtil.isNull(request.getCouponActivityId())){
            return 0;
        }
        // 删除条件
        LambdaQueryWrapper<CouponActivityEnterpriseLimitDO> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(CouponActivityEnterpriseLimitDO::getCouponActivityId, request.getCouponActivityId());
        // 删除
        CouponActivityEnterpriseLimitDO couponActivityEnterpriseLimit = new CouponActivityEnterpriseLimitDO();
        couponActivityEnterpriseLimit.setUpdateUser(request.getOpUserId());
        couponActivityEnterpriseLimit.setUpdateTime(request.getOpTime());
        return this.batchDeleteWithFill(couponActivityEnterpriseLimit, deleteWrapper);
    }

    private String giveAndGetCheck(Integer useDateType, Date nowTime, CouponActivityDetailDTO couponActivity) {
        Long id = couponActivity.getId();
        if (ObjectUtil.equal(CouponActivityUseDateTypeEnum.AFTER_GIVE.getCode(), useDateType)) {
            // 关联自动发放、自动领取活动状态校验
            // 自动发放活动
            CouponActivityAutoGiveCouponDTO autoGiveCoupon = autoGiveCouponService.getByCouponActivityId(id);
            // 自动领取活动
            CouponActivityAutoGetCouponDTO autoGetCoupon = autoGetCouponService.getByCouponActivityId(id);
            if (ObjectUtil.isNull(autoGiveCoupon) && ObjectUtil.isNull(autoGetCoupon)) {
                return "此优惠券活动有效期类型为“按发放/领取时间设定”，未关联自动发放或自动领取活动，不能发放";
            }
            if (ObjectUtil.isNotNull(autoGiveCoupon)) {
                CouponActivityAutoGiveDTO autoGive = autoGiveService.getAutoGiveById(autoGiveCoupon.getCouponActivityAutoGiveId());
                Date autoGiveBeginTime = autoGive.getBeginTime();
                Date autoGiveEndTime = autoGive.getEndTime();
                if (autoGiveBeginTime.getTime() > nowTime.getTime()) {
                    return "此优惠券活动有效期类型为“按发放/领取时间设定”，关联的自动发放活动未开始，不能发放，[自动发放活动id：" + autoGive.getId() + "]";
                }
                if (autoGiveEndTime.getTime() <= nowTime.getTime()) {
                    return "此优惠券活动有效期类型为“按发放/领取时间设定”，关联的自动发放活动未开始，不能发放，[自动发放活动id：" + autoGive.getId() + "]";
                }
            }
            if (ObjectUtil.isNotNull(autoGetCoupon)) {
                CouponActivityAutoGetDTO autoGet = autoGetService.getAutoGetById(autoGetCoupon.getCouponActivityAutoGetId());
                Date autoGetBeginTime = autoGet.getBeginTime();
                Date autoGetEndTime = autoGet.getEndTime();
                if (autoGetBeginTime.getTime() > nowTime.getTime()) {
                    return "此优惠券活动有效期类型为“按发放/领取时间设定”，关联的自动领取活动未开始，不能发放，[自动领取活动id：" + autoGet.getId() + "]";
                }
                if (autoGetEndTime.getTime() <= nowTime.getTime()) {
                    return "此优惠券活动有效期类型为“按发放/领取时间设定”，关联的自动发放活动已结束，不能发放，[自动发放活动id：" + autoGet.getId() + "]";
                }
            }
        }
        return null;
    }

}
