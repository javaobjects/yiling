package com.yiling.dataflow.order.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.order.dao.FlowPurchaseInventoryMapper;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseInventoryListPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseInventorySettlementRequest;
import com.yiling.dataflow.order.dto.request.SaveFlowPurchaseInventoryLogRequest;
import com.yiling.dataflow.order.dto.request.SaveFlowPurchaseInventoryRequest;
import com.yiling.dataflow.order.dto.request.SaveFlowPurchaseInventoryTotalPoQuantityRequest;
import com.yiling.dataflow.order.dto.request.UpdateFlowPurchaseInventoryQuantityRequest;
import com.yiling.dataflow.order.dto.request.UpdateFlowPurchaseInventoryQuantityRequestDetail;
import com.yiling.dataflow.order.entity.FlowPurchaseInventoryDO;
import com.yiling.dataflow.order.enums.FlowPurchaseInventoryBusinessTypeEnum;
import com.yiling.dataflow.order.enums.FlowPurchaseInventoryErrorCode;
import com.yiling.dataflow.order.service.FlowPurchaseInventoryLogService;
import com.yiling.dataflow.order.service.FlowPurchaseInventoryService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.redis.RedisDistributedLock;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 流向商品库存信息 服务实现类
 * </p>
 *
 * @author houjie.sun
 * @date 2022-05-26
 */
@Slf4j
@Service
public class FlowPurchaseInventoryServiceImpl extends BaseServiceImpl<FlowPurchaseInventoryMapper, FlowPurchaseInventoryDO> implements FlowPurchaseInventoryService {

    @Autowired
    private FlowPurchaseInventoryLogService flowPurchaseInventoryLogService;
    @Autowired
    private FlowPurchaseInventoryMapper flowPurchaseInventoryMapper;
    @Autowired
    protected RedisDistributedLock redisDistributedLock;

    @Override
    public FlowPurchaseInventoryDO getByEidAndGoodsInSnAndSource(Long eid, String goodsInSn, Integer source) {
        LambdaQueryWrapper<FlowPurchaseInventoryDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FlowPurchaseInventoryDO::getEid, eid);
        lambdaQueryWrapper.eq(FlowPurchaseInventoryDO::getGoodsInSn, goodsInSn);
        lambdaQueryWrapper.eq(FlowPurchaseInventoryDO::getPoSource, source);
        return this.getOne(lambdaQueryWrapper);
    }

    @Override
    public List<FlowPurchaseInventoryDO> getListByEidAndYlGoodsIdAndGoodsInSn(QueryFlowPurchaseInventorySettlementRequest request) {
        return this.baseMapper.getListByEidAndYlGoodsIdAndGoodsInSn(request.getList());
    }

    @Override
    //    @Transactional(rollbackFor = Exception.class)
    public int updateQuantityById(UpdateFlowPurchaseInventoryQuantityRequest request) {
        List<UpdateFlowPurchaseInventoryQuantityRequestDetail> list = request.getList();
        Long opUserId = request.getOpUserId();
        if (ObjectUtil.isNull(opUserId)) {
            opUserId = 0L;
        }
        Date opTime = request.getOpTime();
        if (ObjectUtil.isNull(opTime)) {
            opTime = new Date();
        }

        for (UpdateFlowPurchaseInventoryQuantityRequestDetail detail : list) {
            String inventoryLockName = RedisKey.generate("mph-erp-online-lock-flow-purchase-inventory", "updatePoQuantityById", detail.getId().toString());
            String lockId = redisDistributedLock.lock2(inventoryLockName, 60, 120, TimeUnit.SECONDS);
            try {
                FlowPurchaseInventoryDO flowPurchaseInventory = this.getById(detail.getId());
                if (ObjectUtil.isNull(flowPurchaseInventory)) {
                    if (ObjectUtil.equal(request.getBusinessType(), FlowPurchaseInventoryBusinessTypeEnum.SETTLEMENT.getCode()) || ObjectUtil.equal(request.getBusinessType(), FlowPurchaseInventoryBusinessTypeEnum.SETTLEMENT_ADJUSTMENT.getCode())) {
                        log.warn("[采购流向库存] 扣减没有找到对应的库存信息, request:{}", JSONUtil.toJsonStr(request));
                        throw new BusinessException(FlowPurchaseInventoryErrorCode.FLOW_INVENTORY_MISSING);
                    } else {
                        continue;
                    }
                }

                // 变更前库存
                BigDecimal beforeQuantity = flowPurchaseInventory.getPoQuantity();
                // 变更库存
                BigDecimal changeQuantity = detail.getQuantity();
                // 变更后库存
                BigDecimal afterQuantity = beforeQuantity.add(changeQuantity);
                // 应变更库存
                BigDecimal inventoryChangeQuantity = changeQuantity;
                if (afterQuantity.compareTo(BigDecimal.ZERO) < 0) {
                    afterQuantity = BigDecimal.ZERO;
                    inventoryChangeQuantity = beforeQuantity;
                }

                SaveFlowPurchaseInventoryLogRequest logRequest = new SaveFlowPurchaseInventoryLogRequest();
                logRequest.setBusinessType(request.getBusinessType());
                logRequest.setFlowPurchaseInventoryId(flowPurchaseInventory.getId());
                logRequest.setPoSource(flowPurchaseInventory.getPoSource());
                logRequest.setEid(flowPurchaseInventory.getEid());
                logRequest.setYlGoodsId(flowPurchaseInventory.getYlGoodsId());
                logRequest.setGoodsInSn(flowPurchaseInventory.getGoodsInSn());
                logRequest.setBeforeQuantity(beforeQuantity);
                logRequest.setChangeQuantity(changeQuantity);
                logRequest.setAfterQuantity(afterQuantity);
                logRequest.setOpUserId(opUserId);
                logRequest.setOpTime(opTime);
                flowPurchaseInventoryLogService.saveGoodInventoryLog(logRequest);

                Integer updateCount = this.baseMapper.updateQuantityById(detail.getId(), inventoryChangeQuantity, opUserId, opTime);
                if (updateCount <= 0 && ObjectUtil.equal(request.getBusinessType(), FlowPurchaseInventoryBusinessTypeEnum.SETTLEMENT.getCode())) {
                    log.warn("[采购流向库存] 扣减错误, request:{}, detail:{}, beforeQuantity:{}, changeQuantity:{}, afterQuantity:{}, inventoryChangeQuantity:{}", JSONUtil.toJsonStr(request), JSONUtil.toJsonStr(detail), beforeQuantity, changeQuantity, afterQuantity, inventoryChangeQuantity);
                    throw new BusinessException(FlowPurchaseInventoryErrorCode.FLOW_INVENTORY_ERROR);
                }
            } finally {
                redisDistributedLock.releaseLock(inventoryLockName, lockId);
            }
        }
        return 1;
    }

    @Override
    //    @Transactional(rollbackFor = Exception.class)
    public Boolean saveOrUpdateFlowPurchaseInventory(SaveFlowPurchaseInventoryRequest request) {
        Long opUserId = request.getOpUserId();
        if (ObjectUtil.isNull(opUserId)) {
            opUserId = 0L;
        }
        Date opTime = request.getOpTime();
        if (ObjectUtil.isNull(opTime)) {
            opTime = new Date();
        }
        Long eid = request.getEid();
        String goodsInSn = request.getGoodsInSn();
        Integer poSource = request.getPoSource();
        // 是否已存在
        FlowPurchaseInventoryDO flowPurchaseInventoryOld = this.getByEidAndGoodsInSnAndSource(eid, goodsInSn, poSource);
        if (ObjectUtil.isNull(flowPurchaseInventoryOld)) {
            save(request, opUserId, opTime);
        } else {
            List<UpdateFlowPurchaseInventoryQuantityRequestDetail> list = new ArrayList<>();
            UpdateFlowPurchaseInventoryQuantityRequestDetail datail = new UpdateFlowPurchaseInventoryQuantityRequestDetail();
            datail.setId(flowPurchaseInventoryOld.getId());
            datail.setQuantity(request.getPoQuantity());
            list.add(datail);
            UpdateFlowPurchaseInventoryQuantityRequest updateRequest = new UpdateFlowPurchaseInventoryQuantityRequest();
            updateRequest.setOpUserId(opUserId);
            updateRequest.setOpTime(opTime);
            updateRequest.setBusinessType(FlowPurchaseInventoryBusinessTypeEnum.FLOW.getCode());
            updateRequest.setList(list);
            updateQuantityById(updateRequest);
        }
        return true;
    }

    @Override
    public List<FlowPurchaseInventoryDO> getListByEidAndYlGoodsIdAndGoodsInSnAndSource(QueryFlowPurchaseInventorySettlementRequest request) {
        if (ObjectUtil.isNull(request) || CollUtil.isEmpty(request.getList())) {
            return ListUtil.empty();
        }
        return this.baseMapper.getListByEidAndYlGoodsIdAndGoodsInSn(request.getList());
    }

    @Override
    public Boolean saveOrUpdateTotalPoQuantity(SaveFlowPurchaseInventoryTotalPoQuantityRequest request) {
        if (ObjectUtil.isNull(request)) {
            return false;
        }
        Long eid = request.getEid();
        String goodsInSn = request.getGoodsInSn();
        Integer poSource = request.getPoSource();
        // 是否已存在
        FlowPurchaseInventoryDO flowPurchaseInventoryOld = this.getByEidAndGoodsInSnAndSource(eid, goodsInSn, poSource);
        Long opUserId = 0L;
        Date opTime = new Date();
        if (ObjectUtil.isNull(flowPurchaseInventoryOld)) {
            // 统计库存总数时新增，默认 库存数量 = 库存数量总计
            SaveFlowPurchaseInventoryRequest saveRequest = PojoUtils.map(request, SaveFlowPurchaseInventoryRequest.class);
            saveRequest.setBusinessType(FlowPurchaseInventoryBusinessTypeEnum.FLOW_STATISTICS_TOTAL_QUANTITY.getCode());
            saveRequest.setPoQuantity(request.getTotalPoQuantity());
            saveRequest.setTotalPoQuantity(request.getTotalPoQuantity());
            saveRequest.setGoodsInSn(request.getGoodsInSn().trim());
            save(saveRequest, opUserId, opTime);
        } else {
            String inventoryLockName = RedisKey.generate("mph-erp-online-lock-flow-purchase-inventory", "updateTotalPoQuantity", flowPurchaseInventoryOld.getId().toString());
            String lockId = redisDistributedLock.lock2(inventoryLockName, 60, 120, TimeUnit.SECONDS);
            try {
                FlowPurchaseInventoryDO updateInventory = new FlowPurchaseInventoryDO();
                updateInventory.setId(flowPurchaseInventoryOld.getId());
                updateInventory.setTotalPoQuantity(request.getTotalPoQuantity());
                updateInventory.setOpUserId(opUserId);
                updateInventory.setOpTime(opTime);
                this.baseMapper.updateById(updateInventory);
                // 日志
                SaveFlowPurchaseInventoryLogRequest logRequest = new SaveFlowPurchaseInventoryLogRequest();
                logRequest.setBusinessType(FlowPurchaseInventoryBusinessTypeEnum.FLOW_STATISTICS_TOTAL_QUANTITY.getCode());
                logRequest.setFlowPurchaseInventoryId(flowPurchaseInventoryOld.getId());
                logRequest.setPoSource(flowPurchaseInventoryOld.getPoSource());
                logRequest.setEid(flowPurchaseInventoryOld.getEid());
                logRequest.setYlGoodsId(flowPurchaseInventoryOld.getYlGoodsId());
                logRequest.setGoodsInSn(flowPurchaseInventoryOld.getGoodsInSn());
                logRequest.setBeforeQuantity(flowPurchaseInventoryOld.getTotalPoQuantity());
                logRequest.setChangeQuantity(request.getTotalPoQuantity().subtract(flowPurchaseInventoryOld.getTotalPoQuantity()));
                logRequest.setAfterQuantity(request.getTotalPoQuantity());
                logRequest.setOpUserId(opUserId);
                logRequest.setOpTime(opTime);
                flowPurchaseInventoryLogService.saveGoodInventoryLog(logRequest);
            } finally {
                redisDistributedLock.releaseLock(inventoryLockName, lockId);
            }
        }

        return null;
    }

    @Override
    public Page<FlowPurchaseInventoryDO> pageByEidAndYlGoodsId(QueryFlowPurchaseInventoryListPageRequest request) {
        if (ObjectUtil.isNull(request) || (ObjectUtil.isNull(request.getEid()) && ObjectUtil.isNull(request.getYlGoodsId()))) {
            return request.getPage();
        }
        return this.baseMapper.listPage(request.getPage(), request);
    }

    @Override
    public List<FlowPurchaseInventoryDO> getByEidAndGoodsInSn(Long eid, String goodsInSn) {
        LambdaQueryWrapper<FlowPurchaseInventoryDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FlowPurchaseInventoryDO::getEid, eid);
        lambdaQueryWrapper.eq(FlowPurchaseInventoryDO::getGoodsInSn, goodsInSn);
        return this.list(lambdaQueryWrapper);
    }


    private void save(SaveFlowPurchaseInventoryRequest request, Long opUserId, Date opTime) {
        Long eid = request.getEid();
        String goodsInSn = request.getGoodsInSn().trim();
        Integer poSource = request.getPoSource();
        String inventoryLockName = RedisKey.generate("mph-erp-online-lock-flow-purchase-inventory", "save", eid.toString(), goodsInSn, poSource.toString());
        String lockId = redisDistributedLock.lock2(inventoryLockName, 60, 120, TimeUnit.SECONDS);
        try {
            FlowPurchaseInventoryDO flowPurchaseInventoryOld = this.getByEidAndGoodsInSnAndSource(eid, goodsInSn, poSource);
            if (ObjectUtil.isNotNull(flowPurchaseInventoryOld)) {
                return;
            }
            BigDecimal beforeQuantity = BigDecimal.ZERO;
            BigDecimal changeQuantity = request.getPoQuantity();
            BigDecimal afterQuantity = changeQuantity;
            if (afterQuantity.compareTo(BigDecimal.ZERO) < 0) {
                afterQuantity = BigDecimal.ZERO;
            }
            // 新增库存
            FlowPurchaseInventoryDO entity = PojoUtils.map(request, FlowPurchaseInventoryDO.class);
            entity.setPoQuantity(afterQuantity);
            entity.setOpUserId(opUserId);
            entity.setOpTime(opTime);
            this.save(entity);
            // 库存变更日志
            SaveFlowPurchaseInventoryLogRequest logRequest = new SaveFlowPurchaseInventoryLogRequest();
            logRequest.setBusinessType(request.getBusinessType());
            logRequest.setFlowPurchaseInventoryId(entity.getId());
            logRequest.setPoSource(request.getPoSource());
            logRequest.setEid(request.getEid());
            logRequest.setGoodsInSn(request.getGoodsInSn());
            logRequest.setYlGoodsId(request.getYlGoodsId());
            logRequest.setOpUserId(opUserId);
            logRequest.setOpTime(opTime);
            logRequest.setBeforeQuantity(beforeQuantity);
            logRequest.setChangeQuantity(changeQuantity);
            logRequest.setAfterQuantity(afterQuantity);
            boolean saveFlag = flowPurchaseInventoryLogService.saveGoodInventoryLog(logRequest);
            if (!saveFlag) {
                throw new BusinessException(FlowPurchaseInventoryErrorCode.FLOW_INVENTORY_SAVE_ERROR);
            }
        } finally {
            redisDistributedLock.releaseLock(inventoryLockName, lockId);
        }
    }

}
