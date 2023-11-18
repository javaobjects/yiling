package com.yiling.marketing.couponactivity.service.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
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
import com.yiling.marketing.common.enums.CouponLimitTypeEnum;
import com.yiling.marketing.common.util.CouponUtil;
import com.yiling.marketing.couponactivity.dao.CouponActivityGoodsLimitMapper;
import com.yiling.marketing.couponactivity.dto.CouponActivityGoodsDTO;
import com.yiling.marketing.couponactivity.dto.request.DeleteCouponActivityGoodsLimitRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityGoodsRequest;
import com.yiling.marketing.couponactivity.dto.request.SaveCouponActivityGoodsLimitDetailRequest;
import com.yiling.marketing.couponactivity.dto.request.SaveCouponActivityGoodsLimitRequest;
import com.yiling.marketing.couponactivity.entity.CouponActivityDO;
import com.yiling.marketing.couponactivity.entity.CouponActivityEnterpriseLimitDO;
import com.yiling.marketing.couponactivity.entity.CouponActivityGoodsLimitDO;
import com.yiling.marketing.couponactivity.enums.CouponActivityErrorCode;
import com.yiling.marketing.couponactivity.service.CouponActivityEnterpriseLimitService;
import com.yiling.marketing.couponactivity.service.CouponActivityGoodsLimitService;
import com.yiling.marketing.couponactivity.service.CouponActivityService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 优惠券活动商品限制表 服务实现类
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
@Slf4j
@Service
public class CouponActivityGoodsLimitServiceImpl extends BaseServiceImpl<CouponActivityGoodsLimitMapper, CouponActivityGoodsLimitDO>
                                                 implements CouponActivityGoodsLimitService {

    @Autowired
    protected RedisDistributedLock                 redisDistributedLock;
    @Autowired
    private   CouponActivityService                couponActivityService;
    @Autowired
    private   CouponActivityEnterpriseLimitService enterpriseLimitService;

    @Override
    public Boolean insertBatch(List<CouponActivityGoodsLimitDO> list) {
        if (CollUtil.isNotEmpty(list)) {
            return this.saveBatch(list);
        }
        return false;
    }

    //    @Override
    //    public Page<CouponActivityGoodsDTO> queryGoodsListPage(QueryCouponActivityGoodsRequest request) {
    //        if (ObjectUtil.isNull(request)) {
    //            throw new BusinessException(ResultCode.PARAM_MISS);
    //        }

    //        // 查询商品信息
    //            QueryGoodsPageListRequest goodsRequest = new QueryGoodsPageListRequest();
    //            goodsRequest.setCurrent(request.getCurrent());
    //            goodsRequest.setSize(request.getSize());
    //            if (ObjectUtil.isNotNull(request.getEid())) {
    //                List<Long> eidList = new ArrayList<>();
    //                eidList.add(request.getEid());
    //                goodsRequest.setEidList(eidList);
    //            }
    //            if (ObjectUtil.isNotNull(request.getGoodsId())) {
    //                goodsRequest.setGoodsId(request.getGoodsId());
    //            }
    //            if (StrUtil.isNotBlank(request.getGoodsName())) {
    //                goodsRequest.setName(request.getGoodsName());
    //            }
    //            goodsRequest.setGoodsStatus(GoodsStatusEnum.UP_SHELF.getCode());
    //            Page<GoodsListItemBO> goodsPage = b2bgoodsApi.queryB2bGoodsPageList(goodsRequest);
    //            List<CouponActivityGoodsDTO> list = new ArrayList<>();
    //            if (CollUtil.isNotEmpty(goodsPage.getRecords())) {
    //                // 查询库存
    //                List<Long> goodsIdList = goodsPage.getRecords().stream().map(GoodsListItemBO::getId).collect(Collectors.toList());
    //                Map<Long, Long> inventoryMap = inventoryApi.getAvailableQtyByGoodsIds(goodsIdList);
    //                CouponActivityGoodsDTO couponActivityGoods;
    //                int index = 0;
    //                for (GoodsListItemBO goods : goodsPage.getRecords()) {
    //                    couponActivityGoods = new CouponActivityGoodsDTO();
    //                    // todo coupon B2B查询商品接口 goodsApi.queryB2bGoodsPageList，（因搜索条件可能只有商品名称）需提供查es接口
    //                    couponActivityGoods.setEid(goods.getEid());
    //                    couponActivityGoods.setEname(goods.getEname());
    //                    couponActivityGoods.setGoodsId(goods.getId());
    //                    couponActivityGoods.setGoodsName(goods.getName());
    //                    couponActivityGoods.setGoodsType(goods.getGoodsType());
    //                    couponActivityGoods.setSellSpecifications(goods.getSellSpecifications());
    //                    couponActivityGoods.setPrice(goods.getPrice());
    //                    couponActivityGoods.setSellUnit(goods.getSellUnit());
    //                    // 商品库存
    //                    Long inventory = inventoryMap.get(goods.getId());
    //                    if (ObjectUtil.isNull(inventory) || inventory.longValue() < 0) {
    //                        inventory = 0L;
    //                    }
    //                    couponActivityGoods.setGoodsInventory(inventory);
    //
    //                    list.add(index, couponActivityGoods);
    //                    index++;
    //                }
    //        }
    //        Page<CouponActivityGoodsDTO> page = PojoUtils.map(goodsPage, CouponActivityGoodsDTO.class);
    //        page.setRecords(list);
    //        return new Page();
    //    }

    @Override
    public Page<CouponActivityGoodsDTO> pageList(QueryCouponActivityGoodsRequest request) {
        if (ObjectUtil.isNull(request) || ObjectUtil.isNull(request.getCouponActivityId())) {
            throw new BusinessException(ResultCode.PARAM_MISS);
        }
        Page<CouponActivityGoodsLimitDO> enterpriseLimitDOPage = this.baseMapper.pageList(request.getPage(), request);
        Page<CouponActivityGoodsDTO> page = PojoUtils.map(enterpriseLimitDOPage, CouponActivityGoodsDTO.class);
        if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
            return page;
        }
        //        this.buildGoodsLimitPage(page);
        return page;
    }

    //    @Override
    //    public void buildGoodsLimitPage(Page<CouponActivityGoodsDTO> page) {
    //        List<Long> goodsIdList = page.getRecords().stream().map(CouponActivityGoodsDTO::getGoodsId).collect(Collectors.toList());
    //
    //        // 根据goodsIds批量查询 商品基础信息
    //        Map<Long, GoodsDTO> goodsInfoMap = new HashMap<>();
    //        List<GoodsDTO> goodsInfoList = goodsApi.batchQueryInfo(goodsIdList);
    //        if (CollUtil.isNotEmpty(goodsInfoList)) {
    //            goodsInfoMap = goodsInfoList.stream().collect(Collectors.toMap(g -> g.getId(), g -> g, (v1, v2) -> v1));
    //        }
    //        // 商品id集合查询商品状态、价格
    //        Map<Long, B2bGoodsDTO> goodsStatusPriceMap = new HashMap<>();
    //        List<B2bGoodsDTO> goodsStatusPriceList = b2bgoodsApi.getB2bGoodsListByGoodsIds(goodsIdList);
    //        if (CollUtil.isNotEmpty(goodsStatusPriceList)) {
    //            goodsStatusPriceMap = goodsStatusPriceList.stream().collect(Collectors.toMap(g -> g.getGoodsId(), g -> g, (v1, v2) -> v1));
    //        }
    //        // 统计商品库存
    //        Map<Long, Long> inventoryMap = inventoryApi.getAvailableQtyByGoodsIds(goodsIdList);
    //
    //        // 获取商品基本信息、状态、价格、统计商品库存
    //        for (CouponActivityGoodsDTO entity : page.getRecords()) {
    //            GoodsDTO goodsInfo = goodsInfoMap.get(entity.getGoodsId());
    //            B2bGoodsDTO goodsStatusPrice = goodsStatusPriceMap.get(entity.getGoodsId());
    //            if (ObjectUtil.isNotNull(goodsInfo)) {
    //                entity.setGoodsName(goodsInfo.getName());
    //                entity.setGoodsType(goodsInfo.getGoodsType());
    //                entity.setSellSpecifications(goodsInfo.getSellSpecifications());
    //                entity.setSellUnit(goodsInfo.getSellUnit());
    //            }
    //            if (ObjectUtil.isNotNull(goodsStatusPrice)) {
    //                entity.setGoodsStatus(goodsStatusPrice.getGoodsStatus());
    //                entity.setPrice(goodsInfo.getPrice());
    //            }
    //            // 商品库存
    //            Long inventory = inventoryMap.get(entity.getId());
    //            if (ObjectUtil.isNull(inventory) || inventory.longValue() < 0) {
    //                inventory = 0L;
    //            }
    //            entity.setGoodsInventory(inventory);
    //        }
    //    }

    @Override
    public Boolean saveGoodsLimit(SaveCouponActivityGoodsLimitRequest request) {
        if (ObjectUtil.isNull(request)) {
            throw new BusinessException(ResultCode.PARAM_MISS);
        }
        if(ObjectUtil.equal(1, request.getBusinessType()) && ObjectUtil.isNull(request.getEnterpriseLimit())){
            throw new BusinessException(ResultCode.PARAM_MISS);
        }
        if (CollUtil.isEmpty(request.getGoodsLimitList())) {
            return false;
        }

        long couponActivityId = request.getGoodsLimitList().get(0).getCouponActivityId();
        String lockName = CouponUtil.getLockName("couponActivityGoodsLimit", "saveGoodsLimit");
        String lockId = "";
        try {
            lockId = redisDistributedLock.lock(lockName, 60, 60, TimeUnit.SECONDS);
            if (StringUtils.isEmpty(lockId)) {
                log.info("优惠券活动添加商品：系统繁忙, 加锁失败，请稍等重试, couponActivityId -> {}, request -> {}", couponActivityId, JSONObject.toJSONString(request));
                throw new BusinessException(CouponErrorCode.REDIS_LOCK_SAVE_GOODS_LIMIT_DOING_ERROR);
            }

            // 查询此优惠券活动已添加商品，goodsId不重复则新增、否则更新
            LambdaQueryWrapper<CouponActivityGoodsLimitDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(CouponActivityGoodsLimitDO::getCouponActivityId, couponActivityId);
            List<CouponActivityGoodsLimitDO> dbList = this.list(queryWrapper);

            CouponActivityDO couponActivity = couponActivityService.getById(couponActivityId);
            if (ObjectUtil.isNull(couponActivity)) {
                throw new BusinessException(CouponErrorCode.COUPON_ACTIVITY_NOT_EXIST_ERROR);
            }
            // 商家设置为部分商家可用，校验商品所属企业是否在已选择商家列表中
            if (ObjectUtil.equal(CouponLimitTypeEnum.PART.getCode(), request.getEnterpriseLimit())) {
                // 查询商家设置
                checkGoodsLimitEnterprise(request, couponActivityId, dbList);
            }

            Map<Long, CouponActivityGoodsLimitDO> goodsDbMap = new HashMap<>();
            if (CollUtil.isNotEmpty(dbList)) {
                goodsDbMap = dbList.stream().collect(Collectors.toMap(e -> e.getGoodsId(), e -> e, (v1, v2) -> v1));
            }

            // 新增数据
            List<SaveCouponActivityGoodsLimitDetailRequest> insertList = new ArrayList<>();
            // 更新数据
            List<SaveCouponActivityGoodsLimitDetailRequest> updateList = new ArrayList<>();

            Date date = new Date();
            Long userId = request.getOpUserId();
            for (SaveCouponActivityGoodsLimitDetailRequest entity : request.getGoodsLimitList()) {
                CouponActivityGoodsLimitDO goodsLimitDb = goodsDbMap.get(entity.getGoodsId());
                if (ObjectUtil.isNull(goodsLimitDb)) {
                    // 新增
                    entity.setCreateTime(date);
                    entity.setCreateUser(userId);
                    insertList.add(entity);
                } else {
                    // 更新
                    entity.setId(goodsLimitDb.getId());
                    entity.setUpdateUser(userId);
                    entity.setUpdateTime(date);
                    updateList.add(entity);
                }
            }

            if (CollUtil.isNotEmpty(insertList)) {
                List<CouponActivityGoodsLimitDO> doInsertList = PojoUtils.map(insertList, CouponActivityGoodsLimitDO.class);
                this.saveBatch(doInsertList);
            }
            if (CollUtil.isNotEmpty(updateList)) {
                List<CouponActivityGoodsLimitDO> doUpdateList = PojoUtils.map(updateList, CouponActivityGoodsLimitDO.class);
                this.updateBatchById(doUpdateList);
            }
        } catch (Exception e) {
            throw new BusinessException(CouponActivityErrorCode.GOODS_LIMIT_SAVE_ERROR, e.getMessage());
        } finally {
            redisDistributedLock.releaseLock(lockName, lockId);
        }
        return true;
    }

    public void checkGoodsLimitEnterprise(SaveCouponActivityGoodsLimitRequest request, long couponActivityId, List<CouponActivityGoodsLimitDO> dbList) {
        List<CouponActivityEnterpriseLimitDO> enterpriseLimitList = enterpriseLimitService.getByCouponActivityId(couponActivityId);
        if (CollUtil.isEmpty(enterpriseLimitList)) {
            throw new BusinessException(CouponErrorCode.TENTERPRISE_LIMIT_NULL_ERROR);
        }
        Map<Long, CouponActivityEnterpriseLimitDO> enterpriseLimitMap = enterpriseLimitList.stream()
            .collect(Collectors.toMap(e -> e.getEid(), e -> e, (v1, v2) -> v1));
        // 校验商品所属企业是否在已选择商家列表中
        TreeSet<CouponActivityGoodsLimitDO> goodsLimitSet = new TreeSet<>((o1, o2) -> o1.getGoodsId().compareTo(o2.getGoodsId()));
        goodsLimitSet.addAll(dbList);
        goodsLimitSet.addAll(PojoUtils.map(request.getGoodsLimitList(), CouponActivityGoodsLimitDO.class));
        List<CouponActivityGoodsLimitDO> goodsLimitList = new ArrayList<>(goodsLimitSet);
        for (CouponActivityGoodsLimitDO goodsLimit : goodsLimitList) {
            CouponActivityEnterpriseLimitDO enterpriseLimit = enterpriseLimitMap.get(goodsLimit.getEid());
            if (ObjectUtil.isNull(enterpriseLimit)) {
                String errorMsg = MessageFormat.format(CouponErrorCode.GOODS_LIMIT_ENTERPRISE_ERROR.getMessage(), goodsLimit.getGoodsId().toString(),
                    goodsLimit.getEid().toString());
                throw new BusinessException(CouponErrorCode.GOODS_LIMIT_ENTERPRISE_ERROR, errorMsg);
            }
        }
    }

    @Override
    public Boolean deleteGoodsLimit(DeleteCouponActivityGoodsLimitRequest request) {
        if (ObjectUtil.isNull(request) || CollUtil.isEmpty(request.getIds()) || ObjectUtil.isNull(request.getCouponActivityId())) {
            throw new BusinessException(ResultCode.PARAM_MISS);
        }

        Long userId = request.getOpUserId();
        List<Long> ids = request.getIds();
        Long couponActivityId = request.getCouponActivityId();
        Date date = new Date();
        try {
            // 运营后台修改优惠券活动，已开始不能删除商品设置
            if(ObjectUtil.equal(1, request.getPlatformType())){
                String errorMsg = couponActivityService.deleteCouponActivityLimitCheck(request.getCurrentEid(), couponActivityId);
                if(StrUtil.isNotBlank(errorMsg)){
                    throw new BusinessException(CouponErrorCode.ACTIVITY_RUNNING_DELETE_GOODS_LIMIT_ERROR, errorMsg + "，禁止删除商品");
                }
            }

            // 删除条件
            LambdaQueryWrapper<CouponActivityGoodsLimitDO> deleteWrapper = new LambdaQueryWrapper<>();
            deleteWrapper.eq(CouponActivityGoodsLimitDO::getCouponActivityId, couponActivityId);
            deleteWrapper.in(CouponActivityGoodsLimitDO::getId, ids);
            // 删除
            CouponActivityGoodsLimitDO couponActivityGoodsLimit = new CouponActivityGoodsLimitDO();
            couponActivityGoodsLimit.setUpdateUser(userId);
            couponActivityGoodsLimit.setUpdateTime(date);
            this.batchDeleteWithFill(couponActivityGoodsLimit, deleteWrapper);
        } catch (Exception e) {
            log.error("删除优惠券活动已添加商品失败，couponActivityId -> {}, ids -> {}, exception -> {}", couponActivityId, ids, e);
            throw new BusinessException(CouponActivityErrorCode.GOODS_LIMIT_DELETE_ERROR, e.getMessage());
        }
        return true;
    }

    @Override
    public List<CouponActivityGoodsLimitDO> getListByGoodsIdAndEid(Long goodsId, Long eid) {
        if (ObjectUtil.isNull(goodsId) || ObjectUtil.isNull(eid)) {
            return ListUtil.empty();
        }
        LambdaQueryWrapper<CouponActivityGoodsLimitDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CouponActivityGoodsLimitDO::getEid, eid);
        queryWrapper.eq(CouponActivityGoodsLimitDO::getGoodsId, goodsId);
        return this.list(queryWrapper);
    }

    @Override
    public List<CouponActivityGoodsLimitDO> getListByGoodsIdAndEidList(List<Long> eidList, List<Long> goodsIdList) {
        if (CollUtil.isEmpty(eidList) || CollUtil.isEmpty(goodsIdList)) {
            return ListUtil.empty();
        }
        LambdaQueryWrapper<CouponActivityGoodsLimitDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(CouponActivityGoodsLimitDO::getEid, eidList);
        queryWrapper.in(CouponActivityGoodsLimitDO::getGoodsId, goodsIdList);
        return this.list(queryWrapper);
    }

    @Override
    public List<CouponActivityGoodsLimitDO> getListByCouponActivityIdList(List<Long> couponActivityId) {
        if (CollUtil.isEmpty(couponActivityId)) {
            return ListUtil.empty();
        }
        LambdaQueryWrapper<CouponActivityGoodsLimitDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(CouponActivityGoodsLimitDO::getCouponActivityId, couponActivityId);
        return this.list(queryWrapper);
    }

    @Override
    public List<CouponActivityGoodsLimitDO> getListByCouponActivityIdAndGoodsId(List<Long> couponActivityId,List<Long> goodsId) {
        if (CollUtil.isEmpty(couponActivityId)) {
            return ListUtil.empty();
        }
        LambdaQueryWrapper<CouponActivityGoodsLimitDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(CouponActivityGoodsLimitDO::getCouponActivityId, couponActivityId);
        queryWrapper.in(CouponActivityGoodsLimitDO::getGoodsId, goodsId);
        return this.list(queryWrapper);
    }

    @Override
    public List<CouponActivityGoodsLimitDO> getByCouponActivityId(Long couponActivityId) {
        if (ObjectUtil.isNull(couponActivityId)) {
            return ListUtil.empty();
        }
        LambdaQueryWrapper<CouponActivityGoodsLimitDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CouponActivityGoodsLimitDO::getCouponActivityId, couponActivityId);
        return this.list(queryWrapper);
    }

    @Override
    public Integer deleteByCouponActivityId(DeleteCouponActivityGoodsLimitRequest request) {
        if (ObjectUtil.isNull(request) || ObjectUtil.isNull(request.getCouponActivityId())) {
            return 0;
        }

        // 删除条件
        LambdaQueryWrapper<CouponActivityGoodsLimitDO> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(CouponActivityGoodsLimitDO::getCouponActivityId, request.getCouponActivityId());
        // 删除
        CouponActivityGoodsLimitDO couponActivityGoodsLimit = new CouponActivityGoodsLimitDO();
        couponActivityGoodsLimit.setUpdateUser(request.getOpUserId());
        couponActivityGoodsLimit.setUpdateTime(request.getOpTime());
        return this.batchDeleteWithFill(couponActivityGoodsLimit, deleteWrapper);
    }

    @Override
    public Boolean batchSaveGoodsLimit(SaveCouponActivityGoodsLimitRequest request) {
        if (ObjectUtil.isNull(request)) {
            throw new BusinessException(ResultCode.PARAM_MISS);
        }
        if(ObjectUtil.equal(1, request.getBusinessType()) && ObjectUtil.isNull(request.getEnterpriseLimit())){
            throw new BusinessException(ResultCode.PARAM_MISS);
        }
        if (CollUtil.isEmpty(request.getGoodsLimitList())) {
            return false;
        }

        long couponActivityId = request.getGoodsLimitList().get(0).getCouponActivityId();
        try {
            List<Long> goodsIds = request.getGoodsLimitList().stream().map(SaveCouponActivityGoodsLimitDetailRequest::getGoodsId).collect(Collectors.toList());

            // 查询此优惠券活动已添加商品，goodsId不重复则新增、否则更新
            LambdaQueryWrapper<CouponActivityGoodsLimitDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(CouponActivityGoodsLimitDO::getCouponActivityId, couponActivityId);
            queryWrapper.in(CouponActivityGoodsLimitDO::getGoodsId, goodsIds);
            List<CouponActivityGoodsLimitDO> dbList = this.list(queryWrapper);

            CouponActivityDO couponActivity = couponActivityService.getById(couponActivityId);
            if (ObjectUtil.isNull(couponActivity)) {
                throw new BusinessException(CouponErrorCode.COUPON_ACTIVITY_NOT_EXIST_ERROR);
            }
            // 商家设置为部分商家可用，校验商品所属企业是否在已选择商家列表中
            if (ObjectUtil.equal(CouponLimitTypeEnum.PART.getCode(), request.getEnterpriseLimit())) {
                // 查询商家设置
                List<CouponActivityEnterpriseLimitDO> enterpriseLimitList = enterpriseLimitService.getByCouponActivityId(couponActivityId);
                if (CollUtil.isEmpty(enterpriseLimitList)) {
                    throw new BusinessException(CouponErrorCode.TENTERPRISE_LIMIT_NULL_ERROR);
                }
                List<Long> goodsLimitEids = request.getGoodsLimitList().stream().map(SaveCouponActivityGoodsLimitDetailRequest::getEid).distinct().collect(Collectors.toList());
                List<Long> eids = enterpriseLimitList.stream().map(CouponActivityEnterpriseLimitDO::getEid).distinct().collect(Collectors.toList());

                // 校验商品所属企业是否在已选择商家列表中.差集如果不为空，报错
                if (CollUtil.isNotEmpty(CollectionUtil.subtract(goodsLimitEids, eids))) {
                    throw new BusinessException(CouponErrorCode.GOODS_LIMIT_NOTCONTAIN_ERROR);
                }
            }
            Map<Long, CouponActivityGoodsLimitDO> goodsDbMap = new HashMap<>();
            if (CollUtil.isNotEmpty(dbList)) {
                goodsDbMap = dbList.stream().collect(Collectors.toMap(e -> e.getGoodsId(), e -> e, (v1, v2) -> v1));
            }

            // 新增数据
            List<SaveCouponActivityGoodsLimitDetailRequest> insertList = new ArrayList<>();
            // 更新数据
            List<SaveCouponActivityGoodsLimitDetailRequest> updateList = new ArrayList<>();

            Date date = new Date();
            Long userId = request.getOpUserId();
            for (SaveCouponActivityGoodsLimitDetailRequest entity : request.getGoodsLimitList()) {
                CouponActivityGoodsLimitDO goodsLimitDb = goodsDbMap.get(entity.getGoodsId());
                if (ObjectUtil.isNull(goodsLimitDb)) {
                    // 新增
                    entity.setCreateTime(date);
                    entity.setCreateUser(userId);
                    insertList.add(entity);
                } else {
                    // 更新
                    entity.setId(goodsLimitDb.getId());
                    entity.setUpdateUser(userId);
                    entity.setUpdateTime(date);
                    updateList.add(entity);
                }
            }

            if (CollUtil.isNotEmpty(insertList)) {
                List<CouponActivityGoodsLimitDO> doInsertList = PojoUtils.map(insertList, CouponActivityGoodsLimitDO.class);
                log.info("配置优惠券关联商品添加的数量大小"+doInsertList.size());
                this.saveBatch(doInsertList);
            }
            if (CollUtil.isNotEmpty(updateList)) {
                List<CouponActivityGoodsLimitDO> doUpdateList = PojoUtils.map(updateList, CouponActivityGoodsLimitDO.class);
                log.info("配置优惠券关联商品更新的数量大小"+doUpdateList.size());
                this.updateBatchById(doUpdateList);
            }
        } catch (Exception e) {
            throw new BusinessException(CouponActivityErrorCode.GOODS_LIMIT_SAVE_ERROR, e.getMessage());
        }
        return true;
    }

    @Override
    public List<Long> queryGoodsLimitList(QueryCouponActivityGoodsRequest request) {
        return this.baseMapper.getIds(request);
    }
}
