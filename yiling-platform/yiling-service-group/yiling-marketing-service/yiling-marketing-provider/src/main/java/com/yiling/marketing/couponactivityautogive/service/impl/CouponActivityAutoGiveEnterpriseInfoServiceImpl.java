package com.yiling.marketing.couponactivityautogive.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityGiveEnterpriseInfoRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryGiveEnterpriseInfoListRequest;
import com.yiling.marketing.couponactivity.dto.request.SaveCouponActivityGiveEnterpriseInfoDetailRequest;
import com.yiling.marketing.couponactivity.dto.request.SaveCouponActivityGiveEnterpriseInfoRequest;
import com.yiling.marketing.couponactivityautogive.dao.CouponActivityAutoGiveEnterpriseInfoMapper;
import com.yiling.marketing.couponactivityautogive.entity.CouponActivityAutoGiveEnterpriseInfoDO;
import com.yiling.marketing.couponactivityautogive.service.CouponActivityAutoGiveEnterpriseInfoService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * 自动发券企业信息表 服务实现类
 * </p>
 *
 * @author houjie.sun
 * @date 2021-11-24
 */
@Service
public class CouponActivityAutoGiveEnterpriseInfoServiceImpl extends
                                                             BaseServiceImpl<CouponActivityAutoGiveEnterpriseInfoMapper, CouponActivityAutoGiveEnterpriseInfoDO>
                                                             implements CouponActivityAutoGiveEnterpriseInfoService {

    @Override
    public Boolean insertBatch(List<CouponActivityAutoGiveEnterpriseInfoDO> list) {
        if (CollUtil.isEmpty(list)) {
            return false;
        }
        return this.saveBatch(list);
    }

    @Override
    public Boolean updateBatch(List<CouponActivityAutoGiveEnterpriseInfoDO> list) {
        if (CollUtil.isEmpty(list)) {
            return false;
        }
        return this.updateBatchById(list);
    }

    @Override
    public List<CouponActivityAutoGiveEnterpriseInfoDO> getByCouponActivityId(Long couponActivityId) {
        if (ObjectUtil.isNull(couponActivityId)) {
            throw new BusinessException(ResultCode.PARAM_MISS);
        }
        LambdaQueryWrapper<CouponActivityAutoGiveEnterpriseInfoDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CouponActivityAutoGiveEnterpriseInfoDO::getCouponActivityId, couponActivityId);
        return this.list(queryWrapper);
    }

    @Override
    public List<CouponActivityAutoGiveEnterpriseInfoDO> getByEid(Long eid) {
        if (ObjectUtil.isNull(eid)) {
            throw new BusinessException(ResultCode.PARAM_MISS);
        }
        LambdaQueryWrapper<CouponActivityAutoGiveEnterpriseInfoDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CouponActivityAutoGiveEnterpriseInfoDO::getEid, eid);
        return this.list(queryWrapper);
    }

    @Override
    public Page<CouponActivityAutoGiveEnterpriseInfoDO> queryGiveEnterpriseInfoListPage(QueryCouponActivityGiveEnterpriseInfoRequest request) {
        Page<CouponActivityAutoGiveEnterpriseInfoDO> page;
        if (Objects.isNull(request)) {
            return new Page();
        }
        LambdaQueryWrapper<CouponActivityAutoGiveEnterpriseInfoDO> queryWrapper = getCouponActivityWrapper(request);
        return page(request.getPage(), queryWrapper);
    }

    @Override
    public Boolean saveOrUpdate(SaveCouponActivityGiveEnterpriseInfoRequest request) {
        if (ObjectUtil.isNull(request) || CollUtil.isEmpty(request.getGiveDetailList())) {
            return false;
        }

        Long currentUserId = request.getOpUserId();
        long couponActivityId = request.getGiveDetailList().get(0).getCouponActivityId();
        long eid = request.getGiveDetailList().get(0).getEid();
        Date date = new Date();

        // 待保存发放企业记录
        List<SaveCouponActivityGiveEnterpriseInfoDetailRequest> saveRecordList = new ArrayList<>();
        // 待更新发放企业记录
        List<SaveCouponActivityGiveEnterpriseInfoDetailRequest> updateRecordList = new ArrayList<>();

        // 查询已存在发放企业信息，企业id + 优惠券id，是否已存在
        List<CouponActivityAutoGiveEnterpriseInfoDO> giveEnterpriseInfoList = new ArrayList<>();
        if (request.getCouponActivityFlag()) {
            giveEnterpriseInfoList = this.getByCouponActivityId(couponActivityId);
        } else if (request.getEnterpriseFlag()) {
            giveEnterpriseInfoList = this.getByEid(eid);
        }
        Map<String, CouponActivityAutoGiveEnterpriseInfoDO> recordMap = new HashMap<>();
        if (CollUtil.isNotEmpty(giveEnterpriseInfoList)) {
            recordMap = giveEnterpriseInfoList.stream()
                .collect(Collectors.toMap(r -> r.getEid() + "_" + r.getCouponActivityId(), r -> r, (v1, v2) -> v1));
        }

        for (SaveCouponActivityGiveEnterpriseInfoDetailRequest infoRequest : request.getGiveDetailList()) {
            CouponActivityAutoGiveEnterpriseInfoDO giveEnterpriseInfo = recordMap.get(infoRequest.getEid() + "_" + infoRequest.getCouponActivityId());
            // 更新已发放企业记录，累加发放数量
            if (ObjectUtil.isNotNull(giveEnterpriseInfo)) {
//                SaveCouponActivityGiveEnterpriseInfoDetailRequest updateInfoRequest = new SaveCouponActivityGiveEnterpriseInfoDetailRequest();
//                updateInfoRequest.setGiveNum(infoRequest.getGiveNum());
//                updateInfoRequest.setId(giveEnterpriseInfo.getId());
//                updateInfoRequest.setUpdateUser(currentUserId);
//                updateInfoRequest.setUpdateTime(date);
//                updateRecordList.add(updateInfoRequest);
                CouponActivityAutoGiveEnterpriseInfoDO entity = new CouponActivityAutoGiveEnterpriseInfoDO();
                entity.setGiveNum(giveEnterpriseInfo.getGiveNum() + infoRequest.getGiveNum());
                entity.setUpdateUser(currentUserId);
                entity.setUpdateTime(date);
                LambdaQueryWrapper<CouponActivityAutoGiveEnterpriseInfoDO> queryWrapper = new LambdaQueryWrapper();
                queryWrapper.eq(CouponActivityAutoGiveEnterpriseInfoDO::getId, giveEnterpriseInfo.getId());
                queryWrapper.eq(CouponActivityAutoGiveEnterpriseInfoDO::getGiveNum, giveEnterpriseInfo.getGiveNum());
                this.update(entity, queryWrapper);
            } else {
                infoRequest.setCreateUser(currentUserId);
                infoRequest.setCreateTime(date);
                saveRecordList.add(infoRequest);
            }
        }
        // 优惠券活动发放企业信息
        if (CollUtil.isNotEmpty(saveRecordList)) {
            this.insertBatch(PojoUtils.map(saveRecordList, CouponActivityAutoGiveEnterpriseInfoDO.class));
        }
//        if (CollUtil.isNotEmpty(updateRecordList)) {
//            this.baseMapper.updateGiveNumByIds(updateRecordList);
//        }
        return true;
    }

    @Override
    public List<CouponActivityAutoGiveEnterpriseInfoDO> getByEidAndCouponActivityId(QueryGiveEnterpriseInfoListRequest request) {
        if (ObjectUtil.isNull(request) || ObjectUtil.isNull(request.getCouponActivityId()) || CollUtil.isEmpty(request.getEidList())) {
            throw new BusinessException(ResultCode.PARAM_MISS);
        }
        LambdaQueryWrapper<CouponActivityAutoGiveEnterpriseInfoDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CouponActivityAutoGiveEnterpriseInfoDO::getCouponActivityId, request.getCouponActivityId());
        queryWrapper.in(CouponActivityAutoGiveEnterpriseInfoDO::getEid, request.getEidList());
        return this.list(queryWrapper);
    }

    private LambdaQueryWrapper<CouponActivityAutoGiveEnterpriseInfoDO> getCouponActivityWrapper(QueryCouponActivityGiveEnterpriseInfoRequest request) {
        LambdaQueryWrapper<CouponActivityAutoGiveEnterpriseInfoDO> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(request.getCouponActivityAutoGiveId()) && request.getCouponActivityAutoGiveId() != 0) {
            queryWrapper.eq(CouponActivityAutoGiveEnterpriseInfoDO::getCouponActivityAutoGiveId, request.getCouponActivityAutoGiveId());
        }
        if (StrUtil.isNotEmpty(request.getCouponActivityAutoGiveName())) {
            queryWrapper.like(CouponActivityAutoGiveEnterpriseInfoDO::getCouponActivityAutoGiveName, request.getCouponActivityAutoGiveName());
        }
        if (ObjectUtil.isNotNull(request.getCouponActivityId()) && request.getCouponActivityId() != 0) {
            queryWrapper.eq(CouponActivityAutoGiveEnterpriseInfoDO::getCouponActivityId, request.getCouponActivityId());
        }
        if (StrUtil.isNotEmpty(request.getCouponActivityName())) {
            queryWrapper.like(CouponActivityAutoGiveEnterpriseInfoDO::getCouponActivityName, request.getCouponActivityName());
        }
        if (ObjectUtil.isNotNull(request.getEid()) && request.getEid() != 0) {
            queryWrapper.eq(CouponActivityAutoGiveEnterpriseInfoDO::getEid, request.getEid());
        }
        if (StrUtil.isNotEmpty(request.getEname())) {
            queryWrapper.like(CouponActivityAutoGiveEnterpriseInfoDO::getEname, request.getEname());
        }
        if (ObjectUtil.isNotNull(request.getOwnEid()) && request.getOwnEid() != 0) {
            queryWrapper.eq(CouponActivityAutoGiveEnterpriseInfoDO::getOwnEid, request.getOwnEid());
        }
        if (StrUtil.isNotEmpty(request.getOwnEname())) {
            queryWrapper.like(CouponActivityAutoGiveEnterpriseInfoDO::getOwnEname, request.getOwnEname());
        }
        if (ObjectUtil.isNotNull(request.getCreateBeginTime())) {
            queryWrapper.ge(CouponActivityAutoGiveEnterpriseInfoDO::getCreateTime, DateUtil.beginOfDay(request.getCreateBeginTime()));
        }
        if (ObjectUtil.isNotNull(request.getCreateEndTime())) {
            queryWrapper.le(CouponActivityAutoGiveEnterpriseInfoDO::getCreateTime, DateUtil.endOfDay(request.getCreateEndTime()));
        }
        queryWrapper.orderByDesc(CouponActivityAutoGiveEnterpriseInfoDO::getCreateTime);
        return queryWrapper;
    }

}
