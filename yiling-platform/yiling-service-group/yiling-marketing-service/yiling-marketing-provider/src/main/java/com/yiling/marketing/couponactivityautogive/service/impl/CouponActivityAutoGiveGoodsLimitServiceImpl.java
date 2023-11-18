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

import com.alibaba.fastjson.JSON;
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
import com.yiling.marketing.couponactivity.dto.CouponActivityGoodsDTO;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityAutoGiveGoodsRequest;
import com.yiling.marketing.couponactivity.enums.CouponActivityErrorCode;
import com.yiling.marketing.couponactivity.service.CouponActivityGoodsLimitService;
import com.yiling.marketing.couponactivityautogive.dao.CouponActivityAutoGiveGoodsLimitMapper;
import com.yiling.marketing.couponactivityautogive.dto.request.DeleteCouponActivityAutoGiveGoodsLimitRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.SaveCouponActivityAutoGiveGoodsLimitDetailRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.SaveCouponActivityAutoGiveGoodsLimitRequest;
import com.yiling.marketing.couponactivityautogive.entity.CouponActivityAutoGiveGoodsLimitDO;
import com.yiling.marketing.couponactivityautogive.service.CouponActivityAutoGiveGoodsLimitService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 自动发券活动商品限制表 服务实现类
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
@Slf4j
@Service
public class CouponActivityAutoGiveGoodsLimitServiceImpl extends
                                                         BaseServiceImpl<CouponActivityAutoGiveGoodsLimitMapper, CouponActivityAutoGiveGoodsLimitDO>
                                                         implements CouponActivityAutoGiveGoodsLimitService {

    @Autowired
    protected RedisDistributedLock          redisDistributedLock;
    @Autowired
    private CouponActivityGoodsLimitService couponActivityGoodsLimitService;

    @Override
    public Page<CouponActivityGoodsDTO> pageList(QueryCouponActivityAutoGiveGoodsRequest request) {
        if (ObjectUtil.isNull(request) || ObjectUtil.isNull(request.getCouponActivityAutoGiveId())) {
            throw new BusinessException(ResultCode.PARAM_MISS);
        }
        //        Page<CouponActivityAutoGiveGoodsLimitDO> enterpriseLimitDOPage = this.baseMapper.pageList(request);
        LambdaQueryWrapper<CouponActivityAutoGiveGoodsLimitDO> queryWrapper = this.getCouponActivityWrapper(request);
        Page<CouponActivityGoodsDTO> page = PojoUtils.map(page(request.getPage(), queryWrapper), CouponActivityGoodsDTO.class);
        if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
            return page;
        }
        //        couponActivityGoodsLimitService.buildGoodsLimitPage(page);
        return page;
    }

    @Override
    public Boolean saveGoodsLimit(SaveCouponActivityAutoGiveGoodsLimitRequest request) {
        if (ObjectUtil.isNull(request) && CollUtil.isEmpty(request.getGoodsLimitList())) {
            throw new BusinessException(ResultCode.PARAM_MISS);
        }

        long couponActivityAutoGiveId = request.getGoodsLimitList().get(0).getCouponActivityAutoGiveId();
        String lockName = CouponUtil.getLockName("couponActivityAutoGiveGoodsLimit", "saveGoodsLimit");
        String lockId = "";
        try {
            lockId = redisDistributedLock.lock(lockName, 60, 60, TimeUnit.SECONDS);
            if (StringUtils.isEmpty(lockId)) {
                log.error("自动发券活动添加商品：系统繁忙, 加锁失败，请稍等重试, couponActivityAutoGiveId -> {}, request -> {}", couponActivityAutoGiveId,
                    JSONObject.toJSONString(request));
                throw new BusinessException(CouponErrorCode.REDIS_LOCK_SAVE_GOODS_LIMIT_DOING_ERROR);
            }

            // 查询此优惠券活动已添加商品，goodsId不重复则新增、否则更新
            LambdaQueryWrapper<CouponActivityAutoGiveGoodsLimitDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(CouponActivityAutoGiveGoodsLimitDO::getCouponActivityAutoId, couponActivityAutoGiveId);
            List<CouponActivityAutoGiveGoodsLimitDO> dbList = this.list(queryWrapper);
            Map<Long, CouponActivityAutoGiveGoodsLimitDO> goodsDbMap = new HashMap<>();
            if (CollUtil.isNotEmpty(dbList)) {
                goodsDbMap = dbList.stream().collect(Collectors.toMap(e -> e.getGoodsId(), e -> e, (v1, v2) -> v1));
            }

            // 新增数据
            List<CouponActivityAutoGiveGoodsLimitDO> insertList = new ArrayList<>();
            // 更新数据
            List<CouponActivityAutoGiveGoodsLimitDO> updateList = new ArrayList<>();

            Date date = new Date();
            Long userId = request.getUserId();
            for (SaveCouponActivityAutoGiveGoodsLimitDetailRequest entity : request.getGoodsLimitList()) {
                CouponActivityAutoGiveGoodsLimitDO goodsLimitDb = goodsDbMap.get(entity.getGoodsId());
                if (ObjectUtil.isNotNull(goodsLimitDb)) {
                    String msg = MessageFormat.format(CouponErrorCode.GOODS_LIMIT_EXIST_ERROR.getMessage(), goodsLimitDb.getGoodsId());
                    throw new BusinessException(CouponErrorCode.GOODS_LIMIT_EXIST_ERROR, msg);
                }
                // 新增
                CouponActivityAutoGiveGoodsLimitDO saveEntity = PojoUtils.map(entity, CouponActivityAutoGiveGoodsLimitDO.class);
                saveEntity.setCouponActivityAutoId(entity.getCouponActivityAutoGiveId());
                saveEntity.setCreateTime(date);
                saveEntity.setCreateUser(userId);
                insertList.add(saveEntity);
//                else {
//                    // 更新
//                    CouponActivityAutoGiveGoodsLimitDO updateEntity = new CouponActivityAutoGiveGoodsLimitDO();
//                    updateEntity.setId(goodsLimitDb.getId());
//                    updateEntity.setGoodsName(entity.getGoodsName());
//                    updateEntity.setUpdateUser(userId);
//                    updateEntity.setUpdateTime(date);
//                    updateList.add(updateEntity);
//                }
            }

            if (CollUtil.isNotEmpty(insertList)) {
                this.saveBatch(insertList);
            }
//            if (CollUtil.isNotEmpty(updateList)) {
//                this.updateBatchById(updateList);
//            }
        } catch (Exception e) {
            log.error("自动发券活动添加商品失败，couponActivityAutoGiveId -> {}, exception -> {}", couponActivityAutoGiveId, e);
            throw new BusinessException(CouponActivityErrorCode.AUTO_GIVE_GOODS_LIMIT_SAVE_ERROR, e.getMessage());
        } finally {
            redisDistributedLock.releaseLock(lockName, lockId);
        }
        return true;
    }

    @Override
    public Boolean deleteGoodsLimit(DeleteCouponActivityAutoGiveGoodsLimitRequest request) {
        log.info("deleteGoodsLimit, request -> {}", JSON.toJSONString(request));
        if (ObjectUtil.isNull(request) || ObjectUtil.isNull(request.getOpUserId()) || ObjectUtil.isNull(request.getCouponActivityAutoGiveId())
            || CollUtil.isEmpty(request.getIds())) {
            throw new BusinessException(ResultCode.PARAM_MISS);
        }

        Long userId = request.getOpUserId();
        List<Long> ids = request.getIds();
        Long couponActivityAutoGiveId = request.getCouponActivityAutoGiveId();
        Date date = new Date();
        try {
            // 删除条件
            LambdaQueryWrapper<CouponActivityAutoGiveGoodsLimitDO> deleteWrapper = new LambdaQueryWrapper<>();
            deleteWrapper.eq(CouponActivityAutoGiveGoodsLimitDO::getCouponActivityAutoId, couponActivityAutoGiveId);
            deleteWrapper.in(CouponActivityAutoGiveGoodsLimitDO::getId, ids);
            // 删除
            CouponActivityAutoGiveGoodsLimitDO couponActivityGoodsLimit = new CouponActivityAutoGiveGoodsLimitDO();
            couponActivityGoodsLimit.setUpdateUser(userId);
            couponActivityGoodsLimit.setUpdateTime(date);
            this.batchDeleteWithFill(couponActivityGoodsLimit, deleteWrapper);
        } catch (Exception e) {
            log.error("自动发券活动已添加商品失败，couponActivityAutoGiveId -> {}, ids -> {}, exception -> {}", couponActivityAutoGiveId, ids, e);
            throw new BusinessException(CouponActivityErrorCode.GOODS_LIMIT_DELETE_ERROR);
        }
        return true;
    }

    @Override
    public List<CouponActivityAutoGiveGoodsLimitDO> getGoodsLimitByAutoGiveIdList(List<Long> autoGiveIdList) {
        if (CollUtil.isEmpty(autoGiveIdList)) {
            return ListUtil.empty();
        }
        LambdaQueryWrapper<CouponActivityAutoGiveGoodsLimitDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(CouponActivityAutoGiveGoodsLimitDO::getCouponActivityAutoId, autoGiveIdList);
        return this.list(queryWrapper);
    }

    @Override
    public List<CouponActivityAutoGiveGoodsLimitDO> getGoodsLimitByAutoGiveId(Long autoGiveId) {
        if (ObjectUtil.isNull(autoGiveId)) {
            return ListUtil.empty();
        }
        LambdaQueryWrapper<CouponActivityAutoGiveGoodsLimitDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CouponActivityAutoGiveGoodsLimitDO::getCouponActivityAutoId, autoGiveId);
        return this.list(queryWrapper);
    }

    private LambdaQueryWrapper<CouponActivityAutoGiveGoodsLimitDO> getCouponActivityWrapper(QueryCouponActivityAutoGiveGoodsRequest request) {
        LambdaQueryWrapper<CouponActivityAutoGiveGoodsLimitDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CouponActivityAutoGiveGoodsLimitDO::getCouponActivityAutoId, request.getCouponActivityAutoGiveId());
        if (ObjectUtil.isNotNull(request.getEid()) && request.getEid() != 0) {
            queryWrapper.eq(CouponActivityAutoGiveGoodsLimitDO::getEid, request.getEid());
        }
        if (StrUtil.isNotBlank(request.getEname())) {
            queryWrapper.like(CouponActivityAutoGiveGoodsLimitDO::getEname, request.getEname());
        }
        if (ObjectUtil.isNotNull(request.getGoodsId()) && request.getGoodsId() != 0) {
            queryWrapper.eq(CouponActivityAutoGiveGoodsLimitDO::getGoodsId, request.getGoodsId());
        }
        if (StrUtil.isNotBlank(request.getGoodsName())) {
            queryWrapper.like(CouponActivityAutoGiveGoodsLimitDO::getGoodsName, request.getGoodsName());
        }
        queryWrapper.orderByDesc(CouponActivityAutoGiveGoodsLimitDO::getCreateTime);
        return queryWrapper;
    }
}
