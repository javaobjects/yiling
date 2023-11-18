package com.yiling.marketing.couponactivityautogive.service.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.common.enums.CouponActivityGiveRecordStatusEnum;
import com.yiling.marketing.common.enums.CouponActivityRepeatGiveTypeEnum;
import com.yiling.marketing.common.enums.CouponActivityResultTypeEnum;
import com.yiling.marketing.common.enums.CouponErrorCode;
import com.yiling.marketing.common.enums.CouponGetTypeEnum;
import com.yiling.marketing.coupon.dto.request.SaveCouponRequest;
import com.yiling.marketing.coupon.service.CouponService;
import com.yiling.marketing.couponactivity.entity.CouponActivityDO;
import com.yiling.marketing.couponactivity.service.CouponActivityService;
import com.yiling.marketing.couponactivityautogive.dao.CouponActivityAutoGiveRecordMapper;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveCouponDTO;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveRecordDTO;
import com.yiling.marketing.couponactivityautogive.dto.request.CouponActivityAutoGiveRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.QueryCouponActivityAutoGiveRecordRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.UpdateAutoGiveCountRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.UpdateAutoGiveRecordStatusRequest;
import com.yiling.marketing.couponactivityautogive.entity.CouponActivityAutoGiveDO;
import com.yiling.marketing.couponactivityautogive.entity.CouponActivityAutoGiveRecordDO;
import com.yiling.marketing.couponactivityautogive.service.CouponActivityAutoGiveCouponService;
import com.yiling.marketing.couponactivityautogive.service.CouponActivityAutoGiveRecordService;
import com.yiling.marketing.couponactivityautogive.service.CouponActivityAutoGiveService;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 自动发券企业参与记录表 服务实现类
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
@Slf4j
@Service
public class CouponActivityAutoGiveRecordServiceImpl extends BaseServiceImpl<CouponActivityAutoGiveRecordMapper, CouponActivityAutoGiveRecordDO>
                                                     implements CouponActivityAutoGiveRecordService {

    @Autowired
    private CouponActivityService               couponActivityService;
    @Autowired
    private CouponActivityAutoGiveService       autoGiveService;
    @Autowired
    private CouponActivityAutoGiveCouponService autoGiveCouponService;
    @Autowired
    private CouponService                       couponService;

    @Override
    public Page<CouponActivityAutoGiveRecordDTO> queryEnterpriseGiveRecordListPage(QueryCouponActivityAutoGiveRecordRequest request) {
        Page<CouponActivityAutoGiveRecordDTO> page;
        if (Objects.isNull(request)) {
            return new Page();
        }
        LambdaQueryWrapper<CouponActivityAutoGiveRecordDO> queryWrapper = getCouponActivityWrapper(request);
        return PojoUtils.map(page(request.getPage(), queryWrapper), CouponActivityAutoGiveRecordDTO.class);
    }

    @Override
    public Boolean autoGive(CouponActivityAutoGiveRequest request) {
        Long id = request.getId();
        Long userId = request.getOpUserId();
        String userName = request.getUserName();
        // 查询发放记录
        CouponActivityAutoGiveRecordDO autoGiveRecord = this.getById(id);
        if (ObjectUtil.isNull(autoGiveRecord)) {
            throw new BusinessException(CouponErrorCode.DATA_NOT_EXIST);
        }
        if (ObjectUtil.equal(CouponActivityResultTypeEnum.SUCCESS.getCode(), autoGiveRecord.getStatus())) {
            throw new BusinessException(CouponErrorCode.AUTO_GIVE_RESULT_TYPE_ERROR);
        }

        String failMsg = "";
        Date date = new Date();

        // 查询自动发放活动
        Long autoGiveId = autoGiveRecord.getCouponActivityAutoGiveId();
        Long couponActivityId = autoGiveRecord.getCouponActivityId();
        CouponActivityAutoGiveDO autoGive = autoGiveService.getById(autoGiveId);
        if (ObjectUtil.isNull(autoGive)) {
            failMsg = MessageFormat.format(CouponErrorCode.AUTO_GIVE_NOT_EXIST.getMessage(), autoGiveId);
            throw new BusinessException(CouponErrorCode.AUTO_GIVE_NOT_EXIST, failMsg);
        }
        // 查询优惠券活动
        CouponActivityDO couponActivity = couponActivityService.getById(couponActivityId);
        if (ObjectUtil.isNull(couponActivity)) {
            failMsg = MessageFormat.format(CouponErrorCode.COUPON_ACTIVITY_NOT_EXIST.getMessage(), couponActivityId);
            // 更新当前记录
            buildUpdateRecordStatusRequest(userId, id, CouponActivityGiveRecordStatusEnum.FAIL.getCode(), date, failMsg);
            throw new BusinessException(CouponErrorCode.COUPON_ACTIVITY_NOT_EXIST, failMsg);
        }
        // 查询自动发放的优惠券数量
        CouponActivityAutoGiveCouponDTO autoGiveCoupon = autoGiveCouponService.getByCouponActivityId(couponActivityId);
        if (ObjectUtil.isNull(autoGiveCoupon)) {
            failMsg = MessageFormat.format(CouponErrorCode.AUTO_GIVE_RELATION_NOT_EXIST.getMessage(), autoGiveId, couponActivityId);
            throw new BusinessException(CouponErrorCode.AUTO_GIVE_RELATION_NOT_EXIST, failMsg);
        }
        // 优惠券活动已发放数量
        List<Map<String, Long>> giveCountList = couponService.getGiveCountByCouponActivityId(Arrays.asList(couponActivity.getId()));
        // 优惠券活动校验
        List<Integer> countList = new ArrayList<>();
        String couponActivityErrorMsg = couponActivityService.checkCouponActivity(couponActivity, countList, giveCountList);
        if (StrUtil.isNotBlank(couponActivityErrorMsg)) {
            // 更新当前记录
            buildUpdateRecordStatusRequest(userId, id, CouponActivityGiveRecordStatusEnum.FAIL.getCode(), date, couponActivityErrorMsg);
            throw new BusinessException(CouponErrorCode.AUTO_GIVE_ERROR, couponActivityErrorMsg);
        }

        // 自动发放活动校验
        List<Integer> canGiveNumList = new ArrayList<>();
        String autoGiveErrorMsg = checkAutoGive(autoGive, autoGiveCoupon, couponActivity, autoGiveRecord, countList.get(0), canGiveNumList);
        if (StrUtil.isNotBlank(autoGiveErrorMsg)) {
            // 更新当前记录
            buildUpdateRecordStatusRequest(userId, id, CouponActivityGiveRecordStatusEnum.FAIL.getCode(), date, autoGiveErrorMsg);
            throw new BusinessException(CouponErrorCode.AUTO_GIVE_ERROR, autoGiveErrorMsg);
        }

        // 当前批次号的自动发放，在此之前是否全部失败：是-发放活动次数累加1，否-不累加
        boolean cumulationFlag = false;
        String batchNumber = autoGiveRecord.getBatchNumber();
        List<CouponActivityAutoGiveRecordDO> recordListByBatchNumber = this.getListByBatchNumber(batchNumber);
        if (CollUtil.isNotEmpty(recordListByBatchNumber)) {
            long count = recordListByBatchNumber.stream()
                .filter(r -> ObjectUtil.equal(CouponActivityGiveRecordStatusEnum.SUCCESS.getCode(), r.getStatus())).count();
            if (count == 0) {
                cumulationFlag = true;
            }
        }

        // 发放
        buildCouponSave(userId, userName, autoGiveRecord, date, autoGiveId, couponActivityId, couponActivity, canGiveNumList.get(0));
        // 更新当前记录
        buildUpdateRecordStatusRequest(userId, id, CouponActivityGiveRecordStatusEnum.SUCCESS.getCode(), date, "");
        // 更新发放活动已发放数量
        return updateGiveCount(date, autoGive, cumulationFlag);
    }

    private boolean updateGiveCount(Date date, CouponActivityAutoGiveDO autoGive, boolean cumulationFlag) {
        if (cumulationFlag) {
            UpdateAutoGiveCountRequest updateAutoGiveCountRequest = new UpdateAutoGiveCountRequest();
            updateAutoGiveCountRequest.setIds(Arrays.asList(autoGive.getId()));
            updateAutoGiveCountRequest.setOpTime(date);
            updateAutoGiveCountRequest.setOpUserId(0L);
            return autoGiveService.updateGiveCountByIdList(updateAutoGiveCountRequest);
        }
        return true;
    }

    private Boolean buildUpdateRecordStatusRequest(Long userId, Long id, Integer status, Date date, String autoGiveErrorMsg) {
        List<UpdateAutoGiveRecordStatusRequest> autoGiveList = new ArrayList<>();
        UpdateAutoGiveRecordStatusRequest request = new UpdateAutoGiveRecordStatusRequest();
        request.setId(id);
        request.setStatus(status);
        request.setFaileReason(autoGiveErrorMsg);
        request.setOpUserId(userId);
        request.setOpTime(date);
        autoGiveList.add(request);
        return this.updateRecordStatus(autoGiveList);
    }

    private Boolean buildCouponSave(Long userId, String userName, CouponActivityAutoGiveRecordDO autoGiveRecord, Date date, Long autoGiveId,
                                    Long couponActivityId, CouponActivityDO couponActivity, int canGiveNum) {
        List<SaveCouponRequest> couponList = new ArrayList<>();
        for (int i = 0; i < canGiveNum; i++) {
            SaveCouponRequest request = new SaveCouponRequest();
            request.setCouponActivityAutoId(autoGiveId);
            request.setCouponActivityAutoName(autoGiveRecord.getCouponActivityAutoGiveName());
            request.setCouponActivityAutoId(couponActivityId);
            request.setCouponActivityName(couponActivity.getName());
            request.setEid(autoGiveRecord.getEid());
            request.setEname(autoGiveRecord.getEname());
            request.setGetType(CouponGetTypeEnum.AUTO_GIVE.getCode());
            request.setGetUserId(userId);
            request.setGetUserName(userName);
            request.setGetTime(date);
            request.setBeginTime(couponActivity.getBeginTime());
            request.setEndTime(couponActivity.getEndTime());
            request.setCreateUser(userId);
            request.setCreateTime(date);
            couponList.add(request);
        }
        return couponService.insertBatch(couponList);
    }

    private void buildGiveRecordFail(Long userId, Long id, Date date, CouponActivityAutoGiveRecordDO autoGiveRecord, String autoGiveErrorMsg) {
        CouponActivityAutoGiveRecordDO autoGiveRecordFail = new CouponActivityAutoGiveRecordDO();
        BeanUtil.copyProperties(autoGiveRecord, autoGiveRecordFail);
        autoGiveRecordFail.setId(id);
        autoGiveRecordFail.setStatus(CouponActivityResultTypeEnum.FAIL.getCode());
        autoGiveRecordFail.setFaileReason(autoGiveErrorMsg);
        autoGiveRecordFail.setCreateUser(userId);
        autoGiveRecordFail.setCreateTime(date);
        this.save(autoGiveRecordFail);
    }

    public String checkAutoGive(CouponActivityAutoGiveDO autoGive, CouponActivityAutoGiveCouponDTO autoGiveCoupon, CouponActivityDO couponActivity,
                                CouponActivityAutoGiveRecordDO autoGiveRecord, int couponCount, List<Integer> canGiveNumList) {
        Long giveId = autoGive.getId();
        Date endTime = autoGive.getEndTime();
        long nowTime = System.currentTimeMillis();
        // 自动发券活动结束时间
        if ((endTime.getTime() <= nowTime)) {
            return "自动发券活动已结束，不能发放";
        }
        // 发放校验
        Long couponActivityId = autoGiveCoupon.getCouponActivityId();
        Integer repeatGive = autoGive.getRepeatGive();
        if (ObjectUtil.equal(CouponActivityRepeatGiveTypeEnum.ONE.getCode(), repeatGive)) {
            // 仅发一次
            Integer giveNum = autoGiveCoupon.getGiveNum();
            if (ObjectUtil.isNull(giveNum) || giveNum <= 0) {
                return "自动发券活动[" + giveId + "]设置的优惠券[" + couponActivityId + "]可发放数量为空或0，不能发放";
            }
        } else if (ObjectUtil.equal(CouponActivityRepeatGiveTypeEnum.MANY.getCode(), repeatGive)) {
            // 重复发放
            Integer maxGiveNum = autoGive.getMaxGiveNum();
            if (ObjectUtil.isNull(maxGiveNum) || maxGiveNum <= 0) {
                return "自动发券活动[" + giveId + "]为可重复发放，最多发放次数为空或0，不能发放";
            }
            Integer giveCount = autoGive.getGiveCount();
            if (ObjectUtil.isNull(giveCount) || giveCount <= 0) {
                giveCount = 0;
            }
            if (giveCount >= maxGiveNum) {
                return "自动发券活动[" + giveId + "]为可重复发放，最多发放次数，不能发放";
            }
        }

        Integer totalCount = couponActivity.getTotalCount();
        int canGiveNum = totalCount - couponCount;
        if (canGiveNum <= 0) {
            return "此优惠券活动发放数量已达到最大总数量，自动发放活动id:[" + giveId + "]，优惠券活动id:[" + couponActivityId + "]，优惠券总数量:[" + totalCount + "]";
        }
        canGiveNumList.add(canGiveNum);
        return null;
    }

    @Override
    public Boolean insertBatch(List<CouponActivityAutoGiveRecordDO> list) {
        if (CollUtil.isEmpty(list)) {
            return false;
        }
        return this.saveBatch(list);
    }

    @Override
    public Boolean updateBatch(List<CouponActivityAutoGiveRecordDO> list) {
        if (CollUtil.isEmpty(list)) {
            return false;
        }
        return this.updateBatchById(list);
    }

    @Override
    public List<CouponActivityAutoGiveRecordDTO> getByIdList(List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return ListUtil.empty();
        }
        LambdaQueryWrapper<CouponActivityAutoGiveRecordDO> queryWrapper = new LambdaQueryWrapper<>();

        return null;
    }

    @Override
    public List<CouponActivityAutoGiveRecordDTO> getByCouponActivityId(Long couponActivityId) {
        if (ObjectUtil.isNull(couponActivityId)) {
            return ListUtil.empty();
        }
        LambdaQueryWrapper<CouponActivityAutoGiveRecordDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CouponActivityAutoGiveRecordDO::getCouponActivityId, couponActivityId);
        List<CouponActivityAutoGiveRecordDO> list = this.list(queryWrapper);
        return PojoUtils.map(list, CouponActivityAutoGiveRecordDTO.class);
    }

    @Override
    public Boolean deleteByIds(List<Long> ids, Long userId) {
        if (CollUtil.isEmpty(ids)) {
            return false;
        }
        List<CouponActivityAutoGiveRecordDO> list = new ArrayList<>();
        CouponActivityAutoGiveRecordDO giveRecord;
        Date date = new Date();
        for (Long id : ids) {
            giveRecord = new CouponActivityAutoGiveRecordDO();
            giveRecord.setId(id);
            giveRecord.setDelFlag(1);
            giveRecord.setUpdateUser(userId);
            giveRecord.setUpdateTime(date);
            list.add(giveRecord);
        }
        return this.updateBatchById(list);
    }

    @Override
    public CouponActivityAutoGiveRecordDO getAutoGiveRecordLastOneByEid(Long eid) {
        if (ObjectUtil.isNull(eid)) {
            return null;
        }
        return this.baseMapper.getAutoGiveRecordLastOneByEid(eid);
    }

    @Override
    public List<CouponActivityAutoGiveRecordDO> getRecordListByEidAndAutoGiveIds(Long eid, List<Long> autoGiveIdList) {
        if (ObjectUtil.isNull(eid) || CollUtil.isEmpty(autoGiveIdList)) {
            return ListUtil.empty();
        }
        LambdaQueryWrapper<CouponActivityAutoGiveRecordDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CouponActivityAutoGiveRecordDO::getEid, eid);
        queryWrapper.eq(CouponActivityAutoGiveRecordDO::getGetType, CouponGetTypeEnum.AUTO_GIVE.getCode());
        queryWrapper.eq(CouponActivityAutoGiveRecordDO::getStatus, CouponActivityResultTypeEnum.SUCCESS.getCode());
        queryWrapper.in(CouponActivityAutoGiveRecordDO::getCouponActivityAutoGiveId, autoGiveIdList);
        return this.list(queryWrapper);
    }

    @Override
    public Boolean saveAutoGiveRecordWithWaitStatus(List<CouponActivityAutoGiveRecordDO> autoGiveList) {
        if (CollUtil.isEmpty(autoGiveList)) {
            throw new BusinessException(ResultCode.PARAM_MISS);
        }
        return this.saveBatch(autoGiveList);
    }

    @Override
    public Boolean updateRecordStatus(List<UpdateAutoGiveRecordStatusRequest> autoGiveList) {
        if (CollUtil.isEmpty(autoGiveList)) {
            throw new BusinessException(ResultCode.PARAM_MISS);
        }
        autoGiveList.stream().forEach(r -> {
            if (ObjectUtil.isNull(r.getId())) {
                throw new BusinessException(ResultCode.PARAM_MISS);
            }
        });
        List<CouponActivityAutoGiveRecordDO> list = PojoUtils.map(autoGiveList, CouponActivityAutoGiveRecordDO.class);
        return this.updateBatchById(list);
    }

    @Override
    public List<CouponActivityAutoGiveRecordDO> getListByBatchNumber(String batchNumber) {
        if (StrUtil.isBlank(batchNumber)) {
            throw new BusinessException(ResultCode.PARAM_MISS);
        }
        LambdaQueryWrapper<CouponActivityAutoGiveRecordDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(CouponActivityAutoGiveRecordDO::getBatchNumber, batchNumber);
        return this.list(queryWrapper);
    }

    @Override
    public List<CouponActivityAutoGiveRecordDO> getRecordListByAutoGiveIds(List<Long> autoGiveIdList) {
        if (CollUtil.isEmpty(autoGiveIdList)) {
            return ListUtil.empty();
        }
        LambdaQueryWrapper<CouponActivityAutoGiveRecordDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CouponActivityAutoGiveRecordDO::getGetType, CouponGetTypeEnum.AUTO_GIVE.getCode());
        queryWrapper.eq(CouponActivityAutoGiveRecordDO::getStatus, CouponActivityResultTypeEnum.SUCCESS.getCode());
        queryWrapper.in(CouponActivityAutoGiveRecordDO::getCouponActivityAutoGiveId, autoGiveIdList);
        return this.list(queryWrapper);
    }

    private LambdaQueryWrapper<CouponActivityAutoGiveRecordDO> getCouponActivityWrapper(QueryCouponActivityAutoGiveRecordRequest request) {
        LambdaQueryWrapper<CouponActivityAutoGiveRecordDO> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(request.getCouponActivityAutoGiveId()) && request.getCouponActivityAutoGiveId() != 0) {
            queryWrapper.eq(CouponActivityAutoGiveRecordDO::getCouponActivityAutoGiveId, request.getCouponActivityAutoGiveId());
        }
        if (StrUtil.isNotEmpty(request.getCouponActivityAutoGiveName())) {
            queryWrapper.like(CouponActivityAutoGiveRecordDO::getCouponActivityAutoGiveName, request.getCouponActivityAutoGiveName());
        }
        if (ObjectUtil.isNotNull(request.getCouponActivityId()) && request.getCouponActivityId() != 0) {
            queryWrapper.eq(CouponActivityAutoGiveRecordDO::getCouponActivityId, request.getCouponActivityId());
        }
        if (StrUtil.isNotEmpty(request.getCouponActivityName())) {
            queryWrapper.like(CouponActivityAutoGiveRecordDO::getCouponActivityName, request.getCouponActivityName());
        }
        if (ObjectUtil.isNotNull(request.getEid()) && request.getEid() != 0) {
            queryWrapper.eq(CouponActivityAutoGiveRecordDO::getEid, request.getEid());
        }
        if (StrUtil.isNotEmpty(request.getEname())) {
            queryWrapper.like(CouponActivityAutoGiveRecordDO::getEname, request.getEname());
        }
        if (ObjectUtil.isNotNull(request.getGetType()) && request.getGetType() != 0) {
            queryWrapper.eq(CouponActivityAutoGiveRecordDO::getGetType, request.getGetType());
        }
        if (ObjectUtil.isNotNull(request.getStatus()) && request.getStatus() != 0) {
            queryWrapper.eq(CouponActivityAutoGiveRecordDO::getStatus, request.getStatus());
        }
        if (ObjectUtil.isNotNull(request.getCreateBeginTime())) {
            queryWrapper.ge(CouponActivityAutoGiveRecordDO::getCreateTime, DateUtil.beginOfDay(request.getCreateBeginTime()));
        }
        if (ObjectUtil.isNotNull(request.getCreateEndTime())) {
            queryWrapper.le(CouponActivityAutoGiveRecordDO::getCreateTime, DateUtil.endOfDay(request.getCreateEndTime()));
        }
        queryWrapper.orderByDesc(CouponActivityAutoGiveRecordDO::getCreateTime);
        return queryWrapper;
    }

}
