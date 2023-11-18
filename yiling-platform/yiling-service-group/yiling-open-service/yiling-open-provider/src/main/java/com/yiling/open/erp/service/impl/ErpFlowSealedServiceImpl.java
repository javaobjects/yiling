package com.yiling.open.erp.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.rocketmq.mq.RocketMqProducerService;
import com.yiling.open.erp.dao.ErpFlowSealedMapper;
import com.yiling.open.erp.dto.request.ErpFlowSealedLockOrUnlockRequest;
import com.yiling.open.erp.dto.request.QueryErpFlowSealedRequest;
import com.yiling.open.erp.dto.request.QueryErpSealedPageRequest;
import com.yiling.open.erp.dto.request.QueryErpSealedSaveRequest;
import com.yiling.open.erp.entity.ErpFlowSealedDO;
import com.yiling.open.erp.enums.ErpFlowSealedStatusEnum;
import com.yiling.open.erp.enums.ErpFlowSealedTopicName;
import com.yiling.open.erp.enums.ErpFlowSealedTypeEnum;
import com.yiling.open.erp.enums.OpenErrorCode;
import com.yiling.open.erp.service.ErpFlowSealedService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 流向封存 服务实现类
 *
 * @author: houjie.sun
 * @date: 2022/4/14
 */
@Slf4j
@Service("erpFlowSealedService")
@CacheConfig(cacheNames = "open:erpFlowSealed")
public class ErpFlowSealedServiceImpl extends BaseServiceImpl<ErpFlowSealedMapper, ErpFlowSealedDO> implements ErpFlowSealedService {

    @Autowired(required = false)
    private RocketMqProducerService rocketMqProducerService;

    @Override
    public Page<ErpFlowSealedDO> page(QueryErpSealedPageRequest request) {
        Page<ErpFlowSealedDO> page = new Page<>(request.getCurrent(), request.getSize());
        return this.page(page, getErpClientPageQueryWrapper(request));
    }

    @Override
    public List<ErpFlowSealedDO> getByEidList(List<Long> eidList) {
        if(CollUtil.isEmpty(eidList)){
            return ListUtil.empty();
        }
        LambdaQueryWrapper<ErpFlowSealedDO> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(ErpFlowSealedDO::getEid, eidList);
        return this.list(queryWrapper);
    }

    @Override
    @CacheEvict(allEntries = true)
    public Boolean save(QueryErpSealedSaveRequest request) {
        if(ObjectUtil.isNull(request)){
            throw new BusinessException(ResultCode.PARAM_MISS);
        }

        Assert.notEmpty(request.getEidList(), "参数 eidList 不能为空");
        Assert.notEmpty(request.getTypeList(), "参数 typeList 不能为空");
        Assert.notEmpty(request.getMonthList(), "参数 monthList 不能为空");
        request.getEidList().forEach(eid -> {Assert.notNull(eid, "参数 eidList 中不能有空数据");});
        request.getTypeList().forEach(type -> {Assert.notNull(type, "参数 typeList 中不能有空数据");});
        request.getMonthList().forEach(month -> {if(StrUtil.isBlank(month)){month = null;}Assert.notNull(month, "参数 monthList 中不能有空数据");});

        List<Long> eidList = request.getEidList();
        List<Integer> typeList = request.getTypeList();
        List<String> monthRequestList = new ArrayList<>();
        request.getMonthList().stream().forEach(m -> {
            monthRequestList.add(m.replace("年", "-").replace("月", ""));
        });
        Map<Long, String> enameMap = request.getEnameMap();
        Long opUserId = request.getOpUserId();
        Date opTime = new Date();

        // 请求流向封存数据
        List<ErpFlowSealedDO> requestList = buildFlowSealedrequestList(eidList, typeList, monthRequestList, enameMap, opUserId, opTime);

        // 已存在的流向封存信息
        List<ErpFlowSealedDO> oldSealedList = getOldSealedList(eidList, typeList, monthRequestList);

        // 都不存在，直接保存
        if(CollUtil.isEmpty(oldSealedList)){
            return this.saveBatch(requestList);
        } else {
            // 匹配月份是否已封存
            List<ErpFlowSealedDO> saveList = new ArrayList<>();
            List<ErpFlowSealedDO> updateList = new ArrayList<>();
            Map<String, ErpFlowSealedDO> oldMap = oldSealedList.stream().collect(Collectors.toMap(f -> getUniqueKey(f), f -> f, (v1,v2) -> v1));
            for (ErpFlowSealedDO erpFlowSealed : requestList) {
                ErpFlowSealedDO oldErpFlowSealed = oldMap.get(getUniqueKey(erpFlowSealed));
                if(ObjectUtil.isNotNull(oldErpFlowSealed)){
                    if(ObjectUtil.equal(ErpFlowSealedStatusEnum.LOCK.getCode(),oldErpFlowSealed.getStatus())){
                        continue;
                    }
                    ErpFlowSealedDO updateDo = new ErpFlowSealedDO();
                    updateDo.setId(oldErpFlowSealed.getId());
                    updateDo.setStatus(ErpFlowSealedStatusEnum.LOCK.getCode());
                    updateDo.setUpdateUser(opUserId);
                    updateDo.setUpdateTime(opTime);
                    updateList.add(updateDo);
                } else {
                    saveList.add(erpFlowSealed);
                }
            }
            if(CollUtil.isNotEmpty(updateList)){
                this.updateBatchById(updateList);
            }
            if(CollUtil.isNotEmpty(saveList)){
                this.saveBatch(saveList);
            }
        }
        return true;
    }

    private String getUniqueKey(ErpFlowSealedDO erpFlowSealedDO){
        if(ObjectUtil.isNull(erpFlowSealedDO)){
            return "";
        }
        return erpFlowSealedDO.getEid().toString().concat(":").concat(erpFlowSealedDO.getType().toString()).concat(":").concat(erpFlowSealedDO.getMonth());
    }

    @Override
    public ErpFlowSealedDO getErpFlowSealedById(Long id) {
        if(ObjectUtil.isNull(id)){
            return null;
        }
        return this.getById(id);
    }

    @Override
    @CacheEvict(allEntries = true)
    public Boolean lockOrUnLock(ErpFlowSealedLockOrUnlockRequest request) {
        if(ObjectUtil.isNull(request) || ObjectUtil.isNull(request.getId()) || ObjectUtil.isNull(request.getStatus())){
            return false;
        }
        ErpFlowSealedDO erpFlowSealedDO = this.getById(request.getId());

        LambdaQueryWrapper<ErpFlowSealedDO> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(ErpFlowSealedDO::getId, request.getId());

        ErpFlowSealedDO entity = new ErpFlowSealedDO();
        entity.setStatus(request.getStatus());
        entity.setUpdateUser(request.getOpUserId());
        entity.setUpdateTime(new Date());
        boolean updateResult = this.update(entity, queryWrapper);

        // 解封同步
        if (ObjectUtil.equal(ErpFlowSealedStatusEnum.LOCK.getCode(), erpFlowSealedDO.getStatus()) && ObjectUtil.equal(ErpFlowSealedStatusEnum.UN_LOCK.getCode(), request.getStatus())) {
            // 同步当前子公司eid、月份 的OP库数据到线上，发送mq
            SendResult sendResult = null;
            OpenErrorCode errorMsg = null;
            if (ObjectUtil.equal(ErpFlowSealedTypeEnum.PURCHASE.getCode(), erpFlowSealedDO.getType())) {
                sendResult = rocketMqProducerService.sendSync(ErpFlowSealedTopicName.UN_LOCK_PURCHASE.getTopic(), erpFlowSealedDO.getEid().toString(), DateUtil.formatDate(new Date()), erpFlowSealedDO.getMonth());
                errorMsg = OpenErrorCode.ERP_FLOW_SEALED_PURCHASE_ERROR;
            } else if (ObjectUtil.equal(ErpFlowSealedTypeEnum.SALE.getCode(), erpFlowSealedDO.getType())) {
                sendResult = rocketMqProducerService.sendSync(ErpFlowSealedTopicName.UN_LOCK_SALE.getTopic(), erpFlowSealedDO.getEid().toString(), DateUtil.formatDate(new Date()), erpFlowSealedDO.getMonth());
                errorMsg = OpenErrorCode.ERP_FLOW_SEALED_SALE_ERROR;
            } else if (ObjectUtil.equal(ErpFlowSealedTypeEnum.SHOP_SALE.getCode(), erpFlowSealedDO.getType())) {
                sendResult = rocketMqProducerService.sendSync(ErpFlowSealedTopicName.UN_LOCK_SHOP_SALE.getTopic(), erpFlowSealedDO.getEid().toString(), DateUtil.formatDate(new Date()), erpFlowSealedDO.getMonth());
                errorMsg = OpenErrorCode.ERP_FLOW_SEALED_SHOP_SALE_ERROR;
            }
            if (sendResult == null || !sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
                throw new BusinessException(errorMsg);
            }
        }

        return updateResult;
    }

    @Override
    @Cacheable(key="'getErpFlowSealedByEidAndTypeAndMonth:eid_' + #requerst.eid + ':type_' + #requerst.type + ':month_' + #requerst.month")
    public ErpFlowSealedDO getErpFlowSealedByEidAndTypeAndMonth(QueryErpFlowSealedRequest requerst) {
        if(ObjectUtil.isNull(requerst) || ObjectUtil.isNull(requerst.getEid()) || ObjectUtil.isNull(requerst.getType()) || StrUtil.isBlank(requerst.getMonth())){
            throw new BusinessException(ResultCode.PARAM_MISS);
        }
        LambdaQueryWrapper<ErpFlowSealedDO> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(ErpFlowSealedDO::getEid, requerst.getEid());
        queryWrapper.eq(ErpFlowSealedDO::getType, requerst.getType());
        queryWrapper.eq(ErpFlowSealedDO::getMonth, requerst.getMonth());
        return this.getOne(queryWrapper);
    }

    private List<ErpFlowSealedDO> getOldSealedList(List<Long> eidList, List<Integer> typeList, List<String> monthRequestList) {
        LambdaQueryWrapper<ErpFlowSealedDO> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(ErpFlowSealedDO::getEid, eidList);
        queryWrapper.in(ErpFlowSealedDO::getType, typeList);
        queryWrapper.in(ErpFlowSealedDO::getMonth, monthRequestList);
        return this.list(queryWrapper);
    }

    private List<ErpFlowSealedDO> buildFlowSealedrequestList(List<Long> eidList, List<Integer> typeList, List<String> monthRequestList,
                                                             Map<Long, String> enameMap, Long opUserId, Date opTime) {
        List<ErpFlowSealedDO> requestList = new ArrayList<>();
        ErpFlowSealedDO erpFlowSealedDO;
        for (Long eid : eidList) {
            for (Integer type : typeList) {
                for (String month : monthRequestList) {
                    erpFlowSealedDO = new ErpFlowSealedDO();
                    erpFlowSealedDO.setEid(eid);
                    erpFlowSealedDO.setEname(enameMap.get(eid));
                    erpFlowSealedDO.setType(type);
                    erpFlowSealedDO.setMonth(month);
                    erpFlowSealedDO.setStatus(ErpFlowSealedStatusEnum.LOCK.getCode());
                    erpFlowSealedDO.setCreateUser(opUserId);
                    erpFlowSealedDO.setCreateTime(opTime);
                    requestList.add(erpFlowSealedDO);
                }
            }
        }
        return requestList;
    }

//    public static void main(String[] args) {
//        QueryErpSealedSaveRequest request = new QueryErpSealedSaveRequest();
//        List<Long> eidList = new ArrayList<>();
//        eidList.add(1L);
//        eidList.add(null);
//        List<Integer> typeList = new ArrayList<>();
//        typeList.add(2);
//        typeList.add(null);
//        List<String> monthList = new ArrayList<>();
//        monthList.add("2022年03月");
//        monthList.add("");
//        request.setEidList(eidList);
//        request.setTypeList(typeList);
//        request.setMonthList(monthList);
//
//        Assert.notNull(request, "参数不能为空");
//        Assert.notEmpty(request.getEidList(), "参数eidList不能为空");
//        Assert.notEmpty(request.getTypeList(), "参数typeList不能为空");
//        Assert.notEmpty(request.getMonthList(), "参数monthList不能为空");
//
//        request.getEidList().forEach(eid -> {Assert.notNull(eid, "参数eidList中不能有空数据");});
//        request.getTypeList().forEach(type -> {Assert.notNull(type, "参数typeList中不能有空数据");});
//        request.getMonthList().forEach(month -> {if(StrUtil.isBlank(month)){month = null;}Assert.notNull(month, "参数monthList中不能有空数据");});
//    }

    private LambdaQueryWrapper<ErpFlowSealedDO> getErpClientPageQueryWrapper(QueryErpSealedPageRequest request) {
        LambdaQueryWrapper<ErpFlowSealedDO> lambdaQueryWrapper = new LambdaQueryWrapper();

        Long eid = request.getEid();
        if (eid != null && eid != 0) {
            lambdaQueryWrapper.eq(ErpFlowSealedDO::getEid, eid);
        }

        String ename = request.getEname();
        if (StrUtil.isNotBlank(ename)) {
            lambdaQueryWrapper.like(ErpFlowSealedDO::getEname, ename);
        }

        Integer type = request.getType();
        if (type != null && type != 0) {
            lambdaQueryWrapper.eq(ErpFlowSealedDO::getType, type);
        }

        Integer status = request.getStatus();
        if (status != null && status != 0) {
            lambdaQueryWrapper.eq(ErpFlowSealedDO::getStatus, status);
        }

        lambdaQueryWrapper.orderByDesc(ErpFlowSealedDO::getCreateTime);
        return lambdaQueryWrapper;
    }
}
