package com.yiling.marketing.coupon.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.common.enums.CouponGetTypeEnum;
import com.yiling.marketing.common.enums.CouponStatusEnum;
import com.yiling.marketing.common.enums.CouponUsedStatusEnum;
import com.yiling.marketing.coupon.bo.CouponUseOrderBO;
import com.yiling.marketing.coupon.dao.CouponMapper;
import com.yiling.marketing.coupon.dto.CouponDTO;
import com.yiling.marketing.coupon.dto.request.CouponAutoGiveRequest;
import com.yiling.marketing.coupon.dto.request.QueryCouponPageRequest;
import com.yiling.marketing.coupon.dto.request.QueryHasGiveCouponAutoRequest;
import com.yiling.marketing.coupon.dto.request.SaveCouponRequest;
import com.yiling.marketing.coupon.dto.request.UpdateCouponRequest;
import com.yiling.marketing.coupon.dto.request.UseMemberCouponRequest;
import com.yiling.marketing.coupon.entity.CouponDO;
import com.yiling.marketing.coupon.service.CouponService;
import com.yiling.marketing.couponactivity.entity.CouponActivityDO;
import com.yiling.marketing.couponactivity.enums.CouponActivityErrorCode;
import com.yiling.marketing.couponactivity.service.CouponActivityService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * 用户优惠券信息表 服务实现类
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
@Service
public class CouponServiceImpl extends BaseServiceImpl<CouponMapper, CouponDO> implements CouponService {

    @Autowired
    private CouponActivityService couponActivityService;

    @Override
    public Boolean insertBatch(List<SaveCouponRequest> list) {
        if (CollUtil.isNotEmpty(list)) {
            List<CouponDO> couponList = PojoUtils.map(list, CouponDO.class);
            return this.saveBatch(couponList);
        }
        return false;
    }

    @Override
    public Boolean updateBatch(List<UpdateCouponRequest> list) {
        if (CollUtil.isNotEmpty(list)) {
            List<CouponDO> couponList = PojoUtils.map(list, CouponDO.class);
            return this.updateBatchById(couponList);
        }
        return false;
    }

    @Override
    public Boolean autoGive(CouponAutoGiveRequest request) {
        if (ObjectUtil.isNull(request)) {
            return false;
        }
        CouponDO coupon = PojoUtils.map(request, CouponDO.class);
        coupon.setUsedStatus(CouponUsedStatusEnum.NOT_USED.getCode());
        coupon.setStatus(CouponStatusEnum.NORMAL_COUPON.getCode());
        return this.save(coupon);
    }

    @Override
    public List<CouponDTO> getByCouponActivityIdForGiveDelete(Long couponActivityId, List<Long> eidList) {
        if (ObjectUtil.isNull(couponActivityId) || CollUtil.isEmpty(eidList)) {
            throw new BusinessException(CouponActivityErrorCode.GIVE_DELETE_ENTERPRISE_PARAM_ERROR);
        }
        LambdaQueryWrapper<CouponDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CouponDO::getCouponActivityId, couponActivityId);
        queryWrapper.in(CouponDO::getEid, eidList);
        queryWrapper.eq(CouponDO::getGetType, CouponGetTypeEnum.GIVE.getCode());
        queryWrapper.eq(CouponDO::getStatus, CouponStatusEnum.NORMAL_COUPON.getCode());
        List<CouponDO> list = this.list(queryWrapper);
        return PojoUtils.map(list, CouponDTO.class);
    }

    @Override
    public Boolean deleteByIds(List<Long> ids, Long userId) {
        if (CollUtil.isEmpty(ids)) {
            return false;
        }
        List<CouponDO> list = new ArrayList<>();
        CouponDO coupon;
        Date date = new Date();
        for (Long id : ids) {
            coupon = new CouponDO();
            coupon.setId(id);
            coupon.setDelFlag(1);
            coupon.setUpdateUser(userId);
            coupon.setUpdateTime(date);
            list.add(coupon);
        }
        return this.updateBatchById(list);
    }

    @Override
    public List<Map<String, Long>> getGiveCountByCouponActivityId(List<Long> couponActivityIdList) {
        if (CollUtil.isEmpty(couponActivityIdList)) {
            return ListUtil.empty();
        }
        LambdaQueryWrapper<CouponActivityDO> objectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        objectLambdaQueryWrapper.in(CouponActivityDO::getId,couponActivityIdList);
        List<CouponActivityDO> list = couponActivityService.list(objectLambdaQueryWrapper);
        if(CollectionUtils.isEmpty(list)){
            return ListUtil.empty();
        }
        List<Map<String, Long>> objects = new ArrayList<>();
        list.forEach(item->{
            Map<String, Long> result = new HashMap<>();
            result.put("couponActivityId",item.getId());
            result.put("giveCount",item.getGiveCount().longValue());
            objects.add(result);
        });
        return objects;
    }

    @Override
    public List<Map<String, Long>> getUseCountByCouponActivityId(List<Long> couponActivityIdList) {
        if (CollUtil.isEmpty(couponActivityIdList)) {
            return ListUtil.empty();
        }
        return this.baseMapper.getUseCountByCouponActivityId(couponActivityIdList);
    }

    @Override
    public List<Map<Long, Integer>> getCountByCouponActivityAutoIdList(Integer getType, List<Long> couponActivityAutoGiveIdList) {
        if (CollUtil.isEmpty(couponActivityAutoGiveIdList)) {
            return ListUtil.empty();
        }
        return this.baseMapper.getCountByCouponActivityAutoIdList(getType, couponActivityAutoGiveIdList);
    }


    @Override
    public Page<CouponDTO> queryListPage(QueryCouponPageRequest request) {
        if (ObjectUtil.isNull(request)) {
            return new Page();
        }
        LambdaQueryWrapper<CouponDO> queryWrapper = getCouponActivityWrapper(request);
        return PojoUtils.map(page(request.getPage(), queryWrapper), CouponDTO.class);
    }

    @Override
    public List<CouponDTO> getByCouponActivityAutoId(Long couponActivityAutoId) {
        if (ObjectUtil.isNull(couponActivityAutoId)) {
            return ListUtil.empty();
        }
        LambdaQueryWrapper<CouponDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CouponDO::getCouponActivityAutoId, couponActivityAutoId);
        queryWrapper.eq(CouponDO::getStatus, CouponStatusEnum.NORMAL_COUPON.getCode());
        queryWrapper.eq(CouponDO::getGetType, CouponGetTypeEnum.AUTO_GIVE.getCode());
        queryWrapper.last("limit 1");
        return PojoUtils.map(this.list(queryWrapper), CouponDTO.class);
    }

    @Override
    public Integer getEffectiveCountByCouponActivityId(Long couponActivityAutoId) {
        if (ObjectUtil.isNull(couponActivityAutoId)) {
            return 0;
        }
        Integer effectiveCount = this.baseMapper.getEffectiveCountByCouponActivityId(couponActivityAutoId);
        if (ObjectUtil.isNull(effectiveCount)) {
            return 0;
        }
        return effectiveCount;
    }

    @Override
    public Integer getEffectiveCountByEid(Long eid) {
        if (ObjectUtil.isNull(eid)) {
            return 0;
        }
        Integer effectiveCount = this.baseMapper.getEffectiveCountByEid(eid);
        if (ObjectUtil.isNull(effectiveCount)) {
            return 0;
        }
        return effectiveCount;
    }

    @Override
    public List<CouponDO> getByEidAndCouponActivityId(Long eid, List<Long> couponActivityIdList) {
        if (ObjectUtil.isNull(eid) || CollUtil.isEmpty(couponActivityIdList)) {
            return ListUtil.empty();
        }
        LambdaQueryWrapper<CouponDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(CouponDO::getEid, eid);
        queryWrapper.in(CouponDO::getCouponActivityId, couponActivityIdList);
        queryWrapper.eq(CouponDO::getStatus, CouponStatusEnum.NORMAL_COUPON.getCode());
        queryWrapper.eq(CouponDO::getGetType, CouponGetTypeEnum.AUTO_GET.getCode());
        return this.list(queryWrapper);
    }

    @Override
    public Integer getEffectiveCountWithoutScrapByEid(Long eid, Long couponActivityId, Long autoGetOrRulesId, Integer businessType) {
        if (ObjectUtil.isNull(eid)) {
            return 0;
        }
        Integer effectiveCount = this.baseMapper.getEffectiveCountWithoutScrapByEid(eid, couponActivityId, autoGetOrRulesId, businessType);
        if (ObjectUtil.isNull(effectiveCount)) {
            return 0;
        }
        return effectiveCount;
    }

    @Override
    public List<CouponDO> getEffectiveCanUseListByEid(Long currentEid, List<Long> eids) {
        //        if(ObjectUtil.isNull(eid)){
        //            return ListUtil.empty();
        //        }
        //        Date date = new Date();
        //        LambdaQueryWrapper<CouponDO> queryWrapper = new LambdaQueryWrapper<>();
        //        queryWrapper.eq(CouponDO::getEid, eid);
        //        queryWrapper.eq(CouponDO::getUsedStatus, CouponUsedStatusEnum.NOT_USED.getCode());
        //        queryWrapper.eq(CouponDO::getStatus, CouponStatusEnum.NORMAL_COUPON.getCode());
        //        queryWrapper.lt(CouponDO::getBeginTime, date);
        //        queryWrapper.gt(CouponDO::getEndTime, date);
        return this.baseMapper.getEffectiveCanUseListByEid(currentEid, eids);
    }

    //    @Override
    //    public Page<CouponDO> getCouponAndActivityListPageByEid(QueryCouponListPageRequest request) {
    //        if (ObjectUtil.isNull(request)) {
    //            throw new BusinessException(ResultCode.PARAM_MISS);
    //        }
    //        Long eid = request.getEid();
    //        Integer type = request.getUsedStatusType();
    //        Date date = new Date();
    //        LambdaQueryWrapper<CouponDO> queryWrapper = new LambdaQueryWrapper<>();
    //        queryWrapper.eq(CouponDO::getEid, eid);
    //        queryWrapper.eq(CouponDO::getStatus, CouponStatusEnum.NORMAL_COUPON.getCode());
    //        if (ObjectUtil.equal(CouponUsedTypeEnum.NOT_USED.getCode(), type)) {
    //            queryWrapper.eq(CouponDO::getUsedStatus, CouponUsedStatusEnum.NOT_USED.getCode());
    //        } else if (ObjectUtil.equal(CouponUsedTypeEnum.USED.getCode(), type)) {
    //            queryWrapper.eq(CouponDO::getUsedStatus, CouponUsedStatusEnum.USED.getCode());
    //        } else if (ObjectUtil.equal(CouponUsedTypeEnum.EXPIRE.getCode(), type)) {
    //            queryWrapper.gt(CouponDO::getEndTime, date);
    //        }
    //        return page(request.getPage(), queryWrapper);
    //    }

    @Override
    public List<CouponDO> getEffectiveListByIdList(List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return ListUtil.empty();
        }
        Date date = new Date();
        LambdaQueryWrapper<CouponDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(CouponDO::getId, ids);
        queryWrapper.eq(CouponDO::getUsedStatus, CouponUsedStatusEnum.NOT_USED.getCode());
        queryWrapper.eq(CouponDO::getStatus, CouponStatusEnum.NORMAL_COUPON.getCode());
        queryWrapper.lt(CouponDO::getBeginTime, date);
        queryWrapper.gt(CouponDO::getEndTime, date);
        return this.list(queryWrapper);
    }

    @Override
    public List<CouponDO> getEffectiveListByCouponActivityIdList(List<Long> ids,Long eid) {
        if (CollUtil.isEmpty(ids)) {
            return ListUtil.empty();
        }
        Date date = new Date();
        LambdaQueryWrapper<CouponDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(CouponDO::getCouponActivityId, ids);
        queryWrapper.in(CouponDO::getEid, eid);
        //queryWrapper.eq(CouponDO::getUsedStatus, CouponUsedStatusEnum.NOT_USED.getCode());
        //queryWrapper.eq(CouponDO::getStatus, CouponStatusEnum.NORMAL_COUPON.getCode());
        // 结束时间大于当前就行，表示未开始，或者正在进行中
        // 必须是生效的，未开始的也不能查看
        //queryWrapper.gt(CouponDO::getEndTime, date);
        //queryWrapper.lt(CouponDO::getBeginTime, date);
        return this.list(queryWrapper);
    }

    @Override
    public Boolean useCoupon(List<Long> ids, Long userId) {
        if (CollUtil.isEmpty(ids)) {
            return false;
        }
        LambdaQueryWrapper<CouponDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(CouponDO::getId, ids);

        Date date = new Date();
        CouponDO coupon = new CouponDO();
        coupon.setUsedStatus(CouponUsedStatusEnum.USED.getCode());
        coupon.setUserId(userId);
        //        coupon.setUserName(userName);
        coupon.setUseTime(date);
        coupon.setUpdateTime(date);
        coupon.setUpdateUser(userId);
        return this.update(coupon, queryWrapper);
    }

    @Override
    public List<CouponDO> getByIdList(List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return ListUtil.empty();
        }
        LambdaQueryWrapper<CouponDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(CouponDO::getId, ids);
        return this.list(queryWrapper);
    }

    @Override
    public Boolean returenCouponByIds(List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            throw new BusinessException(ResultCode.PARAM_MISS);
        }
        LambdaQueryWrapper<CouponDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(CouponDO::getId, ids);
        CouponDO entity = new CouponDO();
        entity.setUsedStatus(CouponUsedStatusEnum.NOT_USED.getCode());
        entity.setOpTime(new Date());
        return this.update(entity, queryWrapper);
    }

    @Override
    public List<CouponDTO> getHasGiveCountByCouponActivityList(List<Long> couponActivityIdList) {
        if (CollUtil.isEmpty(couponActivityIdList)) {
            return ListUtil.empty();
        }
        LambdaQueryWrapper<CouponActivityDO> objectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        objectLambdaQueryWrapper.in(CouponActivityDO::getId,couponActivityIdList);
        List<CouponActivityDO> list = couponActivityService.list(objectLambdaQueryWrapper);
        if(CollectionUtils.isEmpty(list)){
            return ListUtil.empty();
        }
        List<CouponDTO> objects = new ArrayList<>();
        list.forEach(item->{
            CouponDTO result = new CouponDTO();
            result.setCouponActivityId(item.getId());
            result.setId(item.getId());
            result.setNum(item.getGiveCount());
            objects.add(result);
        });
        return objects;

    }

    @Override
    public List<CouponDTO> getHasGiveListByCouponActivityIdList(QueryHasGiveCouponAutoRequest request) {
        if (ObjectUtil.isNull(request) || ObjectUtil.isNull(request.getGetType()) || CollUtil.isEmpty(request.getCouponActivityIds())
        || ObjectUtil.isNull(request.getBusinessType()) || CollUtil.isEmpty(request.getAutoGetIds())) {
            throw new BusinessException(ResultCode.PARAM_MISS);
        }
        LambdaQueryWrapper<CouponDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(CouponDO::getCouponActivityId, request.getCouponActivityIds());
        queryWrapper.eq(CouponDO::getGetType, request.getGetType());
        queryWrapper.eq(CouponDO::getStatus, CouponStatusEnum.NORMAL_COUPON.getCode());
        if(ObjectUtil.equal(1, request.getBusinessType()) || ObjectUtil.equal(3, request.getBusinessType())){
            queryWrapper.in(CouponDO::getCouponActivityAutoId, request.getAutoGetIds());
        } else if(ObjectUtil.equal(2, request.getBusinessType())){
            queryWrapper.in(CouponDO::getCouponActivityBusinessAutoId, request.getAutoGetIds());
        }
        return PojoUtils.map(this.list(queryWrapper), CouponDTO.class);
    }

    @Override
    public Page<CouponUseOrderBO> getOrderCountUsePageByActivityId(QueryCouponPageRequest request) {
        return this.baseMapper.getOrderCountUsePageByActivityId(request.getPage(), request.getCouponActivityId());
    }

    @Override
    public List<CouponDTO> getHasGiveCountByCouponActivityIdList(QueryHasGiveCouponAutoRequest getRequest) {
        return this.baseMapper.getHasGiveCountByCouponActivityIdList(getRequest.getAutoGetIds(), getRequest.getGetType());
    }

    private LambdaQueryWrapper<CouponDO> getCouponActivityWrapper(QueryCouponPageRequest request) {
        LambdaQueryWrapper<CouponDO> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(request.getCouponActivityId()) && request.getCouponActivityId() != 0) {
            queryWrapper.eq(CouponDO::getCouponActivityId, request.getCouponActivityId());
        }
        if (CollUtil.isNotEmpty(request.getCouponActivityIdList())) {
            queryWrapper.in(CouponDO::getCouponActivityId, request.getCouponActivityIdList());
        }
        if (ObjectUtil.isNotNull(request.getEid())) {
            queryWrapper.eq(CouponDO::getEid, request.getEid());
        }
        if (StrUtil.isNotEmpty(request.getEname())) {
            queryWrapper.like(CouponDO::getEname, request.getEname());
        }
        if (ObjectUtil.isNotNull(request.getCouponActivityAutoId()) && request.getCouponActivityAutoId() != 0) {
            queryWrapper.eq(CouponDO::getCouponActivityAutoId, request.getCouponActivityAutoId());
        }
        if (ObjectUtil.isNotNull(request.getGetType()) && request.getGetType() != 0) {
            queryWrapper.eq(CouponDO::getGetType, request.getGetType());
        }
        if (ObjectUtil.isNotNull(request.getGetUserId()) && request.getGetUserId() != 0) {
            queryWrapper.eq(CouponDO::getGetUserId, request.getGetUserId());
        }
        if (StrUtil.isNotEmpty(request.getGetUserName())) {
            queryWrapper.eq(CouponDO::getGetUserName, request.getGetUserName());
        }
        if (ObjectUtil.isNotNull(request.getUsedStatus()) && request.getUsedStatus() != 0) {
            queryWrapper.eq(CouponDO::getUsedStatus, request.getUsedStatus());
        }
        if (ObjectUtil.isNotNull(request.getStatus()) && request.getStatus() != 0) {
            queryWrapper.eq(CouponDO::getStatus, request.getStatus());
        }
        if (ObjectUtil.isNotNull(request.getBeginTime())) {
            queryWrapper.ge(CouponDO::getBeginTime, DateUtil.beginOfDay(request.getBeginTime()));
        }
        if (ObjectUtil.isNotNull(request.getEndTime())) {
            queryWrapper.le(CouponDO::getEndTime, DateUtil.endOfDay(request.getEndTime()));
        }
        if (ObjectUtil.isNotNull(request.getCreateTime())) {
            queryWrapper.ge(CouponDO::getCreateTime, DateUtil.beginOfDay(request.getCreateTime()));
        }
        queryWrapper.orderByDesc(CouponDO::getId);
        return queryWrapper;
    }
}
