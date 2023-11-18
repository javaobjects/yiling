package com.yiling.marketing.couponactivityautoget.service.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.yiling.marketing.common.enums.CouponErrorCode;
import com.yiling.marketing.common.util.CouponUtil;
import com.yiling.marketing.couponactivity.enums.CouponActivityErrorCode;
import com.yiling.marketing.couponactivityautoget.dao.CouponActivityAutoGetEnterpriseLimitMapper;
import com.yiling.marketing.couponactivityautoget.entity.CouponActivityAutoGetEnterpriseLimitDO;
import com.yiling.marketing.couponactivityautoget.entity.CouponActivityAutoGetPromotionLimitDO;
import com.yiling.marketing.couponactivityautoget.service.CouponActivityAutoGetEnterpriseLimitService;
import com.yiling.marketing.couponactivityautoget.service.CouponActivityAutoGetPromotionLimitService;
import com.yiling.marketing.couponactivityautogive.dto.request.DeleteCouponActivityAutoGetMemberLimitRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.QueryCouponActivityAutoGetMemberLimitRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.SaveCouponActivityAutoGetMemberLimitDetailRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.SaveCouponActivityAutoGetMemberLimitRequest;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 自主领券活动会员企业限制表 服务实现类
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
@Slf4j
@Service
public class CouponActivityAutoGetEnterpriseLimitServiceImpl extends
                                                             BaseServiceImpl<CouponActivityAutoGetEnterpriseLimitMapper, CouponActivityAutoGetEnterpriseLimitDO>
                                                             implements CouponActivityAutoGetEnterpriseLimitService {

    @Autowired
    protected RedisDistributedLock redisDistributedLock;

    @Autowired
    protected CouponActivityAutoGetPromotionLimitService couponActivityAutoGetPromotionLimitService;

    @Override
    public List<CouponActivityAutoGetEnterpriseLimitDO> getByAutoGetId(Long autoGetId) {
        if (ObjectUtil.isNull(autoGetId)) {
            return ListUtil.empty();
        }
        LambdaQueryWrapper<CouponActivityAutoGetEnterpriseLimitDO> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(CouponActivityAutoGetEnterpriseLimitDO::getCouponActivityAutoGetId, autoGetId);
        return this.list(queryWrapper);
    }

    @Override
    public Page<CouponActivityAutoGetEnterpriseLimitDO> queryEnterpriseLimitListPage(QueryCouponActivityAutoGetMemberLimitRequest request) {
        Page<CouponActivityAutoGetEnterpriseLimitDO> page = new Page();
        if (ObjectUtil.isNull(request)) {
            return page;
        }
        LambdaQueryWrapper<CouponActivityAutoGetEnterpriseLimitDO> queryWrapper = getCouponActivityWrapper(request);
        return page(request.getPage(), queryWrapper);
    }

    @Override
    public Boolean savePromotionEnterpriseLimit(SaveCouponActivityAutoGetMemberLimitRequest request) {
        if (ObjectUtil.isNull(request) || CollUtil.isEmpty(request.getEnterpriseLimitList())) {
            return false;
        }
        request.getEnterpriseLimitList().forEach(e -> {
            if (ObjectUtil.isNull(e.getCouponActivityAutoGetId()) || ObjectUtil.isNull(e.getEid()) || StrUtil.isBlank(e.getEname())) {
                throw new BusinessException(ResultCode.PARAM_MISS);
            }
        });

        long couponActivityAutoGetId = request.getEnterpriseLimitList().get(0).getCouponActivityAutoGetId();
        String lockName = CouponUtil.getLockName("couponActivityAutoGetEnterpriseLimit", "saveEnterpriseLimit");
        String lockId = "";
        try {
            lockId = redisDistributedLock.lock(lockName, 60, 60, TimeUnit.SECONDS);
            if (StringUtils.isEmpty(lockId)) {
                log.error("自主领取活动添加会员企业：系统繁忙, 加锁失败，请稍等重试, couponActivityAutoGetId -> {}, request -> {}", couponActivityAutoGetId,
                        JSONObject.toJSONString(request));
                throw new BusinessException(CouponErrorCode.REDIS_LOCK_SAVE_ENTERPRISE_LIMIT_DOING_ERROR);
            }
            // 查询此优惠券活动已添加企业，eid不重复则新增、否则更新
            LambdaQueryWrapper<CouponActivityAutoGetPromotionLimitDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(CouponActivityAutoGetPromotionLimitDO::getCouponActivityAutoGetId, couponActivityAutoGetId);
            List<CouponActivityAutoGetPromotionLimitDO> dbList = couponActivityAutoGetPromotionLimitService.list(lambdaQueryWrapper);
            Map<Long, CouponActivityAutoGetPromotionLimitDO> dbMap = new HashMap<>();
            if (CollUtil.isNotEmpty(dbList)) {
                dbMap = dbList.stream().collect(Collectors.toMap(e -> e.getEid(), e -> e, (v1, v2) -> v1));
            }

            // 待新增数据
            List<SaveCouponActivityAutoGetMemberLimitDetailRequest> insertList = new ArrayList<>();
            Long userId = request.getUserId();
            Date date = new Date();
            for (SaveCouponActivityAutoGetMemberLimitDetailRequest entity : request.getEnterpriseLimitList()) {
                CouponActivityAutoGetPromotionLimitDO enterpriseLimitDb = dbMap.get(entity.getEid());
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
                List<CouponActivityAutoGetPromotionLimitDO> doInsertList = PojoUtils.map(insertList, CouponActivityAutoGetPromotionLimitDO.class);
                couponActivityAutoGetPromotionLimitService.saveBatch(doInsertList);
            }
        } catch (Exception e) {
            log.error("自主领取活动添加会员企业失败，couponActivityAutoGiveId -> {}, exception -> {}", couponActivityAutoGetId, e);
            throw new BusinessException(CouponActivityErrorCode.AUTO_GET_ENTERPRISE_LIMIT_SAVE_ERROR, e.getMessage());
        } finally {
            redisDistributedLock.releaseLock(lockName, lockId);
        }
        return true;
    }

    @Override
    public Boolean saveEnterpriseLimit(SaveCouponActivityAutoGetMemberLimitRequest request) {
        if (ObjectUtil.isNull(request) || CollUtil.isEmpty(request.getEnterpriseLimitList())) {
            return false;
        }
        request.getEnterpriseLimitList().forEach(e -> {
            if (ObjectUtil.isNull(e.getCouponActivityAutoGetId()) || ObjectUtil.isNull(e.getEid()) || StrUtil.isBlank(e.getEname())) {
                throw new BusinessException(ResultCode.PARAM_MISS);
            }
        });

        long couponActivityAutoGetId = request.getEnterpriseLimitList().get(0).getCouponActivityAutoGetId();
        String lockName = CouponUtil.getLockName("couponActivityAutoGetEnterpriseLimit", "saveEnterpriseLimit");
        String lockId = "";
        try {
            lockId = redisDistributedLock.lock(lockName, 60, 60, TimeUnit.SECONDS);
            if (StringUtils.isEmpty(lockId)) {
                log.error("自主领取活动添加会员企业：系统繁忙, 加锁失败，请稍等重试, couponActivityAutoGetId -> {}, request -> {}", couponActivityAutoGetId,
                    JSONObject.toJSONString(request));
                throw new BusinessException(CouponErrorCode.REDIS_LOCK_SAVE_ENTERPRISE_LIMIT_DOING_ERROR);
            }
            // 查询此优惠券活动已添加企业，eid不重复则新增、否则更新
            LambdaQueryWrapper<CouponActivityAutoGetEnterpriseLimitDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(CouponActivityAutoGetEnterpriseLimitDO::getCouponActivityAutoGetId, couponActivityAutoGetId);
            List<CouponActivityAutoGetEnterpriseLimitDO> dbList = this.list(lambdaQueryWrapper);
            Map<Long, CouponActivityAutoGetEnterpriseLimitDO> dbMap = new HashMap<>();
            if (CollUtil.isNotEmpty(dbList)) {
                dbMap = dbList.stream().collect(Collectors.toMap(e -> e.getEid(), e -> e, (v1, v2) -> v1));
            }

            // 待新增数据
            List<SaveCouponActivityAutoGetMemberLimitDetailRequest> insertList = new ArrayList<>();
            Long userId = request.getUserId();
            Date date = new Date();
            for (SaveCouponActivityAutoGetMemberLimitDetailRequest entity : request.getEnterpriseLimitList()) {
                CouponActivityAutoGetEnterpriseLimitDO enterpriseLimitDb = dbMap.get(entity.getEid());
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
                List<CouponActivityAutoGetEnterpriseLimitDO> doInsertList = PojoUtils.map(insertList, CouponActivityAutoGetEnterpriseLimitDO.class);
                this.saveBatch(doInsertList);
            }
        } catch (Exception e) {
            log.error("自主领取活动添加会员企业失败，couponActivityAutoGiveId -> {}, exception -> {}", couponActivityAutoGetId, e);
            throw new BusinessException(CouponActivityErrorCode.AUTO_GET_ENTERPRISE_LIMIT_SAVE_ERROR, e.getMessage());
        } finally {
            redisDistributedLock.releaseLock(lockName, lockId);
        }
        return true;
    }

    @Override
    public Boolean deleteEnterpriseLimit(DeleteCouponActivityAutoGetMemberLimitRequest request) {
        if (ObjectUtil.isNull(request) || ObjectUtil.isNull(request.getCouponActivityAutoGetId()) || CollUtil.isEmpty(request.getIds())
            || ObjectUtil.isNull(request.getUserId())) {
            return false;
        }

        Long userId = request.getUserId();
        Long couponActivityAutoGetId = request.getCouponActivityAutoGetId();
        List<Long> ids = request.getIds();
        Date date = new Date();
        try {
            // 删除条件
            LambdaQueryWrapper<CouponActivityAutoGetEnterpriseLimitDO> deleteWrapper = new LambdaQueryWrapper<>();
            deleteWrapper.eq(CouponActivityAutoGetEnterpriseLimitDO::getCouponActivityAutoGetId, couponActivityAutoGetId);
            deleteWrapper.in(CouponActivityAutoGetEnterpriseLimitDO::getId, ids);
            // 删除
            CouponActivityAutoGetEnterpriseLimitDO couponActivityEnterpriseLimit = new CouponActivityAutoGetEnterpriseLimitDO();
            couponActivityEnterpriseLimit.setUpdateUser(userId);
            couponActivityEnterpriseLimit.setUpdateTime(date);
            this.batchDeleteWithFill(couponActivityEnterpriseLimit, deleteWrapper);
        } catch (Exception e) {
            log.error("删除自主领取活动已添加会员企业失败，couponActivityAutoGiveId -> {}, ids -> {}, exception -> {}", couponActivityAutoGetId, ids, e);
            throw new BusinessException(CouponActivityErrorCode.AUTO_GIVE_ENTERPRISE_LIMIT_DELETE_ERROR);
        }
        return true;
    }

    @Override
    public Boolean deletePromotionEnterpriseLimit(DeleteCouponActivityAutoGetMemberLimitRequest request) {
        if (ObjectUtil.isNull(request) || ObjectUtil.isNull(request.getCouponActivityAutoGetId()) || CollUtil.isEmpty(request.getIds())
                || ObjectUtil.isNull(request.getUserId())) {
            return false;
        }

        Long userId = request.getUserId();
        Long couponActivityAutoGetId = request.getCouponActivityAutoGetId();
        List<Long> ids = request.getIds();
        Date date = new Date();
        try {
            // 删除条件
            LambdaQueryWrapper<CouponActivityAutoGetPromotionLimitDO> deleteWrapper = new LambdaQueryWrapper<>();
            deleteWrapper.eq(CouponActivityAutoGetPromotionLimitDO::getCouponActivityAutoGetId, couponActivityAutoGetId);
            deleteWrapper.in(CouponActivityAutoGetPromotionLimitDO::getId, ids);
            // 删除
            CouponActivityAutoGetPromotionLimitDO activityAutoGetPromotionLimitDO = new CouponActivityAutoGetPromotionLimitDO();
            activityAutoGetPromotionLimitDO.setUpdateUser(userId);
            activityAutoGetPromotionLimitDO.setUpdateTime(date);
            couponActivityAutoGetPromotionLimitService.batchDeleteWithFill(activityAutoGetPromotionLimitDO, deleteWrapper);
        } catch (Exception e) {
            log.error("删除自主领取活动已添加会员企业失败，couponActivityAutoGiveId -> {}, ids -> {}, exception -> {}", couponActivityAutoGetId, ids, e);
            throw new BusinessException(CouponActivityErrorCode.AUTO_GIVE_ENTERPRISE_LIMIT_DELETE_ERROR);
        }
        return true;
    }

    @Override
    public Page<CouponActivityAutoGetPromotionLimitDO> queryPromotionListPage(QueryCouponActivityAutoGetMemberLimitRequest request) {
        Page<CouponActivityAutoGetPromotionLimitDO> page = new Page();
        if (ObjectUtil.isNull(request)) {
            return page;
        }

        LambdaQueryWrapper<CouponActivityAutoGetPromotionLimitDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CouponActivityAutoGetPromotionLimitDO::getCouponActivityAutoGetId, request.getCouponActivityAutoGetId());
        if (ObjectUtil.isNotNull(request.getEid()) && request.getEid() != 0) {
            queryWrapper.eq(CouponActivityAutoGetPromotionLimitDO::getEid, request.getEid());
        }
        if (CollectionUtils.isNotEmpty(request.getEids())) {
            queryWrapper.in(CouponActivityAutoGetPromotionLimitDO::getEid, request.getEids());
        }
        if (StrUtil.isNotBlank(request.getEname())) {
            queryWrapper.like(CouponActivityAutoGetPromotionLimitDO::getEname, request.getEname());
        }
        queryWrapper.orderByDesc(CouponActivityAutoGetPromotionLimitDO::getCreateTime);
        return couponActivityAutoGetPromotionLimitService.page(request.getPage(), queryWrapper);
    }

    private LambdaQueryWrapper<CouponActivityAutoGetEnterpriseLimitDO> getCouponActivityWrapper(QueryCouponActivityAutoGetMemberLimitRequest request) {
        LambdaQueryWrapper<CouponActivityAutoGetEnterpriseLimitDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CouponActivityAutoGetEnterpriseLimitDO::getCouponActivityAutoGetId, request.getCouponActivityAutoGetId());
        if (ObjectUtil.isNotNull(request.getEid()) && request.getEid() != 0) {
            queryWrapper.eq(CouponActivityAutoGetEnterpriseLimitDO::getEid, request.getEid());
        }
        if (StrUtil.isNotBlank(request.getEname())) {
            queryWrapper.like(CouponActivityAutoGetEnterpriseLimitDO::getEname, request.getEname());
        }
        queryWrapper.orderByDesc(CouponActivityAutoGetEnterpriseLimitDO::getCreateTime);
        return queryWrapper;
    }
}
