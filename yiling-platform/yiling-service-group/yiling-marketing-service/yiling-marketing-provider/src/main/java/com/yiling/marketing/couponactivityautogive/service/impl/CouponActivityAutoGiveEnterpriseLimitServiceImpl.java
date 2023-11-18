package com.yiling.marketing.couponactivityautogive.service.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
import com.yiling.marketing.couponactivityautogive.dao.CouponActivityAutoGiveEnterpriseLimitMapper;
import com.yiling.marketing.couponactivityautogive.dto.request.DeleteCouponActivityAutoGiveMemberLimitRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.QueryCouponActivityAutoGiveMemberLimitRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.SaveCouponActivityAutoGiveMemberLimitDetailRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.SaveCouponActivityAutoGiveMemberLimitRequest;
import com.yiling.marketing.couponactivityautogive.entity.CouponActivityAutoGiveEnterpriseLimitDO;
import com.yiling.marketing.couponactivityautogive.service.CouponActivityAutoGiveEnterpriseLimitService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 自动发券活动会员企业限制表 服务实现类
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
@Slf4j
@Service
public class CouponActivityAutoGiveEnterpriseLimitServiceImpl extends
                                                              BaseServiceImpl<CouponActivityAutoGiveEnterpriseLimitMapper, CouponActivityAutoGiveEnterpriseLimitDO>
                                                              implements CouponActivityAutoGiveEnterpriseLimitService {

    @Autowired
    protected RedisDistributedLock redisDistributedLock;

    //    @Override
    //    public Page<CouponActivityAutoGiveMemberDTO> queryEnterpriseListPage(QueryCouponActivityAutoGiveMemberRequest request) {
    //        Page<CouponActivityAutoGiveMemberDTO> page = new Page();
    //        if(ObjectUtil.isNull(request) || ObjectUtil.isNull(request.getEid()) || StrUtil.isBlank(request.getEname())){
    //            return page;
    //        }
    //        // 查询有效的会员信息
    //        QueryEnterpriseMemberRequest memberRequest = new QueryEnterpriseMemberRequest();
    //        memberRequest.setEid(request.getEid());
    //        memberRequest.setEname(request.getEname());
    //        Page<EnterpriseMemberDTO> memberPage = memberApi.queryEnterpriseMemberPage(memberRequest);
    //        if(ObjectUtil.isNull(memberPage) || CollUtil.isEmpty(memberPage.getRecords())){
    //            return page;
    //        }
    //        return PojoUtils.map(memberPage, CouponActivityAutoGiveMemberDTO.class);
    //    }

    @Override
    public Page<CouponActivityAutoGiveEnterpriseLimitDO> queryEnterpriseLimitListPage(QueryCouponActivityAutoGiveMemberLimitRequest request) {
        Page<CouponActivityAutoGiveEnterpriseLimitDO> page = new Page();
        if (ObjectUtil.isNull(request)) {
            return page;
        }
        LambdaQueryWrapper<CouponActivityAutoGiveEnterpriseLimitDO> queryWrapper = getCouponActivityWrapper(request);
        return page(request.getPage(), queryWrapper);
    }

    @Override
    public Boolean saveEnterpriseLimit(SaveCouponActivityAutoGiveMemberLimitRequest request) {
        if (ObjectUtil.isNull(request) || CollUtil.isEmpty(request.getEnterpriseLimitList())) {
            return false;
        }
        request.getEnterpriseLimitList().forEach(e -> {
            if (ObjectUtil.isNull(e.getCouponActivityAutoGiveId()) || ObjectUtil.isNull(e.getEid()) || StrUtil.isBlank(e.getEname())) {
                throw new BusinessException(ResultCode.PARAM_MISS);
            }
        });

        long couponActivityAutoGiveId = request.getEnterpriseLimitList().get(0).getCouponActivityAutoGiveId();
        String lockName = CouponUtil.getLockName("couponActivityAutoGiveEnterpriseLimit", "saveEnterpriseLimit");
        String lockId = "";
        try {
            lockId = redisDistributedLock.lock(lockName, 60, 60, TimeUnit.SECONDS);
            if (StringUtils.isEmpty(lockId)) {
                log.error("自动发券活动添加会员企业：系统繁忙, 加锁失败，请稍等重试, couponActivityAutoGiveId -> {}, request -> {}", couponActivityAutoGiveId,
                    JSONObject.toJSONString(request));
                throw new BusinessException(CouponErrorCode.REDIS_LOCK_SAVE_ENTERPRISE_LIMIT_DOING_ERROR);
            }
            // 查询此优惠券活动已添加企业，eid不重复则新增、否则更新
            LambdaQueryWrapper<CouponActivityAutoGiveEnterpriseLimitDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(CouponActivityAutoGiveEnterpriseLimitDO::getCouponActivityAutoGiveId, couponActivityAutoGiveId);
            List<CouponActivityAutoGiveEnterpriseLimitDO> dbList = this.list(lambdaQueryWrapper);
            Map<Long, CouponActivityAutoGiveEnterpriseLimitDO> dbMap = new HashMap<>();
            if (CollUtil.isNotEmpty(dbList)) {
                dbMap = dbList.stream().collect(Collectors.toMap(e -> e.getEid(), e -> e, (v1, v2) -> v1));
            }

            // 待新增数据
            List<SaveCouponActivityAutoGiveMemberLimitDetailRequest> insertList = new ArrayList<>();

            Long userId = request.getUserId();
            Date date = new Date();
            for (SaveCouponActivityAutoGiveMemberLimitDetailRequest entity : request.getEnterpriseLimitList()) {
                CouponActivityAutoGiveEnterpriseLimitDO enterpriseLimitDb = dbMap.get(entity.getEid());
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
                List<CouponActivityAutoGiveEnterpriseLimitDO> doInsertList = PojoUtils.map(insertList, CouponActivityAutoGiveEnterpriseLimitDO.class);
                this.saveBatch(doInsertList);
            }
        } catch (Exception e) {
            log.error("自动发券活动添加会员企业失败，couponActivityAutoGiveId -> {}, exception -> {}", couponActivityAutoGiveId, e);
            throw new BusinessException(CouponActivityErrorCode.AUTO_GIVE_ENTERPRISE_LIMIT_SAVE_ERROR);
        } finally {
            redisDistributedLock.releaseLock(lockName, lockId);
        }
        return true;
    }

    @Override
    public Boolean deleteEnterpriseLimit(DeleteCouponActivityAutoGiveMemberLimitRequest request) {
        if (ObjectUtil.isNull(request) || CollUtil.isEmpty(request.getIds())) {
            return false;
        }

        Long userId = request.getUserId();
        Long couponActivityAutoGiveId = request.getCouponActivityAutoGiveId();
        List<Long> ids = request.getIds();
        Date date = new Date();
        try {
            // 删除条件
            LambdaQueryWrapper<CouponActivityAutoGiveEnterpriseLimitDO> deleteWrapper = new LambdaQueryWrapper<>();
            deleteWrapper.eq(CouponActivityAutoGiveEnterpriseLimitDO::getCouponActivityAutoGiveId, couponActivityAutoGiveId);
            deleteWrapper.in(CouponActivityAutoGiveEnterpriseLimitDO::getId, ids);
            // 删除
            CouponActivityAutoGiveEnterpriseLimitDO couponActivityEnterpriseLimit = new CouponActivityAutoGiveEnterpriseLimitDO();
            couponActivityEnterpriseLimit.setUpdateUser(userId);
            couponActivityEnterpriseLimit.setUpdateTime(date);
            this.batchDeleteWithFill(couponActivityEnterpriseLimit, deleteWrapper);
        } catch (Exception e) {
            log.error("删除自动发券活动已添加会员企业失败，couponActivityAutoGiveId -> {}, ids -> {}, exception -> {}", couponActivityAutoGiveId, ids, e);
            throw new BusinessException(CouponActivityErrorCode.AUTO_GIVE_ENTERPRISE_LIMIT_DELETE_ERROR);
        }
        return true;
    }

    @Override
    public List<CouponActivityAutoGiveEnterpriseLimitDO> getByAutoGiveId(Long id) {
        LambdaQueryWrapper<CouponActivityAutoGiveEnterpriseLimitDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CouponActivityAutoGiveEnterpriseLimitDO::getCouponActivityAutoGiveId, id);
        return this.list(queryWrapper);
    }

    @Override
    public List<CouponActivityAutoGiveEnterpriseLimitDO> getEnterpriseLimitByAutoGiveIdList(List<Long> autoGiveIdList) {
        if (CollUtil.isEmpty(autoGiveIdList)) {
            return ListUtil.empty();
        }
        LambdaQueryWrapper<CouponActivityAutoGiveEnterpriseLimitDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(CouponActivityAutoGiveEnterpriseLimitDO::getCouponActivityAutoGiveId, autoGiveIdList);
        return this.list(queryWrapper);
    }

    private LambdaQueryWrapper<CouponActivityAutoGiveEnterpriseLimitDO> getCouponActivityWrapper(QueryCouponActivityAutoGiveMemberLimitRequest request) {
        LambdaQueryWrapper<CouponActivityAutoGiveEnterpriseLimitDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CouponActivityAutoGiveEnterpriseLimitDO::getCouponActivityAutoGiveId, request.getCouponActivityAutoGiveId());
        if (ObjectUtil.isNotNull(request.getEid()) && request.getEid() != 0) {
            queryWrapper.eq(CouponActivityAutoGiveEnterpriseLimitDO::getEid, request.getEid());
        }
        if (StrUtil.isNotBlank(request.getEname())) {
            queryWrapper.like(CouponActivityAutoGiveEnterpriseLimitDO::getEname, request.getEname());
        }
        if (ObjectUtil.isNotNull(request.getMemberId()) && request.getMemberId() != 0) {
            queryWrapper.eq(CouponActivityAutoGiveEnterpriseLimitDO::getMemberId, request.getMemberId());
        }
        if (StrUtil.isNotBlank(request.getMemberName())) {
            queryWrapper.like(CouponActivityAutoGiveEnterpriseLimitDO::getMemberName, request.getMemberName());
        }
        queryWrapper.orderByDesc(CouponActivityAutoGiveEnterpriseLimitDO::getCreateTime);
        return queryWrapper;
    }
}
