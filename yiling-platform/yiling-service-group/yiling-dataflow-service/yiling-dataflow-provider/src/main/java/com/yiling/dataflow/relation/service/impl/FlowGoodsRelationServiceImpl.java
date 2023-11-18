package com.yiling.dataflow.relation.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsRelationListRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsRelationPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsRelationYlGoodsIdRequest;
import com.yiling.dataflow.order.dto.request.SaveOrUpdateFlowGoodsRelationRequest;
import com.yiling.dataflow.order.dto.request.UpdateFlowGoodsRelationRequest;
import com.yiling.dataflow.order.enums.DataFlowErrorCode;
import com.yiling.dataflow.relation.dao.FlowGoodsRelationMapper;
import com.yiling.dataflow.relation.entity.FlowGoodsRelationDO;
import com.yiling.dataflow.relation.enums.FlowGoodsRelationLabelEnum;
import com.yiling.dataflow.relation.service.FlowGoodsRelationService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.redis.RedisDistributedLock;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.rocketmq.mq.RocketMqProducerService;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.user.enterprise.api.EnterpriseApi;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: houjie.sun
 * @date: 2022/5/23
 */
@Slf4j
@Service
public class FlowGoodsRelationServiceImpl extends BaseServiceImpl<FlowGoodsRelationMapper, FlowGoodsRelationDO> implements FlowGoodsRelationService {

    @DubboReference
    private GoodsApi goodsApi;
    @DubboReference
    private EnterpriseApi enterpriseApi;

    @Autowired(required = false)
    private RocketMqProducerService rocketMqProducerService;
    @Autowired
    protected RedisDistributedLock redisDistributedLock;

    @Override
    public Page<FlowGoodsRelationDO> page(QueryFlowGoodsRelationPageRequest request) {
        return this.baseMapper.page(request.getPage(), request);
    }

    @Override
    public FlowGoodsRelationDO getById(Long id) {
        LambdaQueryWrapper<FlowGoodsRelationDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FlowGoodsRelationDO::getId, id);
        return this.getOne(lambdaQueryWrapper);
    }

    @Override
    public List<FlowGoodsRelationDO> getByIdList(List<Long> idList) {
        LambdaQueryWrapper<FlowGoodsRelationDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(FlowGoodsRelationDO::getId, idList);
        return this.list(lambdaQueryWrapper);
    }

    @Override
    public Boolean edit(UpdateFlowGoodsRelationRequest request) {
        FlowGoodsRelationDO oldFlowGoodsRelation = getById(request.getId());
        if(ObjectUtil.isNull(oldFlowGoodsRelation)){
            throw new BusinessException(ResultCode.FAILED, "当前企业当前品对应的以岭品关系不存在，请确认后再操作");
        }

        FlowGoodsRelationDO entity = PojoUtils.map(request, FlowGoodsRelationDO.class);
        Long opUserId = ObjectUtil.isNull(request.getOpUserId()) ? 0 : request.getOpUserId();
        Date opTime = ObjectUtil.isNull(request.getOpTime()) ? new Date() : request.getOpTime();
        entity.setUpdateUser(opUserId);
        entity.setUpdateTime(opTime);
        boolean updateFlag = false;
        String lockName = RedisKey.generate("mph-erp-online-lock-flow-goods-relation", "saveOrUpdate", oldFlowGoodsRelation.getEid().toString(), oldFlowGoodsRelation.getGoodsInSn());
        String lockId = "";
        try {
            lockId = redisDistributedLock.lock2(lockName, 5, 5, TimeUnit.SECONDS);
            // 以岭品关系
            updateFlag = this.updateById(entity);
        } finally {
            redisDistributedLock.releaseLock(lockName, lockId);
        }

        if(updateFlag && !ObjectUtil.equal(request.getYlGoodsId(), oldFlowGoodsRelation.getYlGoodsId())){
            Long eid = oldFlowGoodsRelation.getEid();
            // 发送消息通知，匹配流向销售单、并保存以岭品关系修改任务
            JSONObject dataFlowJson = new JSONObject();
            dataFlowJson.put("eid", eid);
            dataFlowJson.put("goodsInSn", oldFlowGoodsRelation.getGoodsInSn());
            dataFlowJson.put("oldYlGoodsId", oldFlowGoodsRelation.getYlGoodsId());
            dataFlowJson.put("ylGoodsId", request.getYlGoodsId());
            dataFlowJson.put("opUserId", request.getOpUserId());
            SendResult sendResult = rocketMqProducerService.sendSync(Constants.TOPIC_ERP_FLOW_GOODS_RELATION_DATAFLOW, oldFlowGoodsRelation.getId().toString(), DateUtil.formatDate(new Date()), JSONUtil.toJsonStr(dataFlowJson));
            if (sendResult == null || !sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
                log.error("flowGoodsRelation edit, 商家品和以岭品关系变更，发送dataFlow消息失败, contentDataFlow:{}", dataFlowJson);
            }
            // ylGoodsId变更，发送消息给settlement服务，顺序发送消息
//            JSONObject settlementJson = new JSONObject();
//            settlementJson.put("eid", eid);
//            settlementJson.put("oldYlingGoodsId", oldFlowGoodsRelation.getYlGoodsId());
//            settlementJson.put("ylGoodsId", request.getYlGoodsId());
//            SendResult sendSettlement = rocketMqProducerService.sendOrderly(Constants.TOPIC_ERP_FLOW_GOODS_RELATION_SETTLEMENT_SEND, Constants.TAG_ERP_FLOW_GOODS_RELATION_SETTLEMENT_SEND, JSONUtil.toJsonStr(settlementJson), 1);
//            if (sendSettlement == null || !sendSettlement.getSendStatus().equals(SendStatus.SEND_OK)) {
//                log.error("flowGoodsRelation edit, 商家品和以岭品关系变更，发送settlement报表计算消息失败, contentSettlement:{}", settlementJson);
//            }
        }
        return true;
    }

    @Override
    public List<FlowGoodsRelationDO> getListByEidAndGoodsName(Long eid, String goodsName) {
        LambdaQueryWrapper<FlowGoodsRelationDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FlowGoodsRelationDO::getEid, eid);
        lambdaQueryWrapper.like(FlowGoodsRelationDO::getGoodsName, goodsName);
        lambdaQueryWrapper.notIn(FlowGoodsRelationDO::getYlGoodsId, ListUtil.toList(0L));
        return this.list(lambdaQueryWrapper);
    }

    @Override
    public List<FlowGoodsRelationDO> getListByEidAndYlGoodsId(Long eid, Long ylGoodsId) {
        LambdaQueryWrapper<FlowGoodsRelationDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FlowGoodsRelationDO::getEid, eid);
        lambdaQueryWrapper.eq(FlowGoodsRelationDO::getYlGoodsId, ylGoodsId);
        return this.list(lambdaQueryWrapper);
    }

    @Override
    public FlowGoodsRelationDO getByEidAndGoodsInSn(Long eid, String goodsInSn) {
        LambdaQueryWrapper<FlowGoodsRelationDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FlowGoodsRelationDO::getEid, eid);
        lambdaQueryWrapper.eq(FlowGoodsRelationDO::getGoodsInSn, goodsInSn);
        return this.getOne(lambdaQueryWrapper);
    }

    @Override
    public void saveOrUpdateByFlowSync(QueryFlowGoodsRelationYlGoodsIdRequest request) {
        Long eid = request.getEid();
        String goodsInSn = request.getGoodsInSn();
        String lockName = RedisKey.generate("mph-erp-online-lock-flow-goods-relation", "saveOrUpdate", eid.toString(), goodsInSn);
        String lockId = "";
        FlowGoodsRelationDO flowGoodsRelation;
        try {
            lockId = redisDistributedLock.lock2(lockName, 3, 3, TimeUnit.SECONDS);
            if (StringUtils.isEmpty(lockId)) {
                throw new BusinessException(DataFlowErrorCode.DATA_FLOW_SYNC_ERROR, "系统繁忙，获取锁失败，lockName:" + lockName);
            }
            // 以岭品关系
            flowGoodsRelation = buildGoodsRelationByFlowSync(request, eid, goodsInSn);
        } finally {
            redisDistributedLock.releaseLock(lockName, lockId);
        }

        if(ObjectUtil.isNull(flowGoodsRelation.getYlGoodsId()) || 0 == flowGoodsRelation.getYlGoodsId().intValue()){
            mathYlGoodsId(flowGoodsRelation.getId(), eid, goodsInSn);
        }
    }

    private FlowGoodsRelationDO buildGoodsRelationByFlowSync(QueryFlowGoodsRelationYlGoodsIdRequest request, Long eid, String goodsInSn) {
        FlowGoodsRelationDO flowGoodsRelation = this.getByEidAndGoodsInSn(eid, goodsInSn);
        if(ObjectUtil.isNotNull(flowGoodsRelation)){
            return flowGoodsRelation;
        } else {
            // 保存以岭品关系
            long ylGoodsId = 0;
            String ylGoodsName = "";
            String ylGoodsSpecifications = "";
            FlowGoodsRelationDO entity = new FlowGoodsRelationDO();
            entity.setEid(eid);
            entity.setEname(request.getEname());
            entity.setGoodsName(request.getGoodsName());
            entity.setGoodsInSn(goodsInSn);
            entity.setGoodsSpecifications(request.getGoodsSpecifications());
            entity.setYlGoodsId(ylGoodsId);
            entity.setYlGoodsName(ylGoodsName);
            entity.setYlGoodsSpecifications(ylGoodsSpecifications);
            entity.setOpUserId(0L);
            entity.setOpTime(new Date());
            this.save(entity);
            return entity;
        }
    }

    public long mathYlGoodsId(Long id, Long eid, String goodsInSn){
        GoodsDTO ylGoods = this.getYlGoods(eid, goodsInSn);
        if(ObjectUtil.isNull(ylGoods)){
            return 0;
        }
        // 更新以岭品信息
        FlowGoodsRelationDO entity = new FlowGoodsRelationDO();
        entity.setId(id);
        entity.setYlGoodsId(ylGoods.getId());
        entity.setYlGoodsName(ylGoods.getName());
        entity.setYlGoodsSpecifications(ylGoods.getSellSpecifications());
        entity.setOpUserId(0L);
        entity.setOpTime(new Date());
        // 商品关系标签
        Long ylGoodsId = entity.getYlGoodsId();
        if (ObjectUtil.isNotNull(ylGoodsId) && 0 != ylGoodsId.intValue()) {
            entity.setGoodsRelationLabel(FlowGoodsRelationLabelEnum.YILING.getCode());
        }
        this.updateById(entity);
        return ylGoods.getId();
    }

    @Override
    @GlobalTransactional
    public Boolean saveOrUpdateByGoodsAuditApproved(SaveOrUpdateFlowGoodsRelationRequest request) {
        Assert.notNull(request, "参数不能为空");
        Assert.notNull(request.getEid(), "参数 eid 不能为空");
        Assert.notNull(request.getGoodsInSnList(), "参数 goodsInSnList 不能为空");
        Assert.notEmpty(request.getGoodsSpecifications(), "参数 goodsSpecifications 不能为空");
        Assert.notNull(request.getYlGoodsId(), "参数 ylGoodsId 不能为空");
        Assert.notEmpty(request.getYlGoodsSpecifications(), "参数 yLGoodsSpecifications 不能为空");

        Long eid = request.getEid();
        Long ylGoodsId = request.getYlGoodsId();
        List<String> goodsInSnList = new ArrayList<>();
        goodsInSnList.addAll(request.getGoodsInSnList());

        Map<String, String> lockMap = new HashMap<>();

        Iterator<String> it = goodsInSnList.iterator();
        while (it.hasNext()) {
            String goodsInSn = it.next();
            String lockName = RedisKey.generate("mph-erp-online-lock-flow-goods-relation", "saveOrUpdate", eid.toString(), goodsInSn);
            String lockId = null;
            try {
                lockId = redisDistributedLock.lock(lockName, 5, 5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (StrUtil.isNotBlank(lockId)) {
                lockMap.put(lockId, lockName);
            } else {
                it.remove();
            }
        }

        try {
            // 以岭品关系已存在，且以岭品id不为空/0的，则不需要改变映射关系
            List<FlowGoodsRelationDO> flowGoodsRelationList = this.getByEidAndGoodsInSn(eid, goodsInSnList);
            Map<String, FlowGoodsRelationDO> exitMap = new HashMap<>();
            if (CollUtil.isNotEmpty(flowGoodsRelationList)) {
                exitMap = flowGoodsRelationList.stream().collect(Collectors.toMap(FlowGoodsRelationDO::getGoodsInSn, Function.identity()));
            }

            for (String goodsInSn : goodsInSnList) {
                // 以岭品关系 更新、新增
                FlowGoodsRelationDO flowGoodsRelation = exitMap.get(goodsInSn);
                if (ObjectUtil.isNotNull(flowGoodsRelation)) {
                    if (ObjectUtil.isNotNull(flowGoodsRelation.getYlGoodsId()) && 0 != flowGoodsRelation.getYlGoodsId().intValue()) {
                        continue;
                    }
                    FlowGoodsRelationDO entity = new FlowGoodsRelationDO();
                    entity.setId(flowGoodsRelation.getId());
                    entity.setYlGoodsId(ylGoodsId);
                    entity.setYlGoodsName(request.getYlGoodsName());
                    entity.setYlGoodsSpecifications(request.getYlGoodsSpecifications());
                    entity.setGoodsManufacturer(request.getGoodsManufacturer());
                    entity.setOpUserId(0L);
                    entity.setOpTime(new Date());
                    // 商品关系标签
                    if (ObjectUtil.isNotNull(ylGoodsId) && 0 != ylGoodsId.intValue()) {
                        entity.setGoodsRelationLabel(FlowGoodsRelationLabelEnum.YILING.getCode());
                    }
                    this.updateById(entity);
                } else {
                    FlowGoodsRelationDO entity = PojoUtils.map(request, FlowGoodsRelationDO.class);
                    // 商品关系标签
                    if (ObjectUtil.isNotNull(ylGoodsId) && 0 != ylGoodsId.intValue()) {
                        entity.setGoodsRelationLabel(FlowGoodsRelationLabelEnum.YILING.getCode());
                    }
                    entity.setGoodsManufacturer(request.getGoodsManufacturer());
                    entity.setGoodsInSn(goodsInSn);
                    entity.setOpUserId(0L);
                    entity.setOpTime(new Date());
                    this.save(entity);
                }
                // 发送以岭品关系修改消息
                buildFlowGoodsRelationDataflowMsg(eid, goodsInSn, flowGoodsRelation.getYlGoodsId(), ylGoodsId);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            for (Map.Entry<String, String> entry : lockMap.entrySet()) {
                redisDistributedLock.releaseLock(entry.getValue(), entry.getKey());
            }
        }
        return false;
    }

    /**
     * 发送以岭品关系修改消息
     *
     * @param eid 企业id
     * @param goodsInSn 商品内码
     * @param oldYlGoodsId 修改前以岭品id
     * @param ylGoodsId 修改后以岭品id
     */
    private void buildFlowGoodsRelationDataflowMsg(Long eid, String goodsInSn, Long oldYlGoodsId, Long ylGoodsId) {
        // 发送消息通知，匹配流向销售单、并保存以岭品关系修改任务
        JSONObject dataFlowJson = new JSONObject();
        dataFlowJson.put("eid", eid);
        dataFlowJson.put("goodsInSn", goodsInSn);
        dataFlowJson.put("oldYlGoodsId", oldYlGoodsId);
        dataFlowJson.put("ylGoodsId", ylGoodsId);
        dataFlowJson.put("opUserId", 0L);
        String json = JSONUtil.toJsonStr(dataFlowJson);
        SendResult sendResult = rocketMqProducerService.sendSync(Constants.TOPIC_ERP_FLOW_GOODS_RELATION_DATAFLOW, oldYlGoodsId.toString(), DateUtil.formatDate(new Date()), json);
        if (sendResult == null || !sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
            log.error("saveOrUpdateByGoodsAuditApproved, 商品审核或修改，发送以岭品关系变更消息失败, dataFlowJson:{}", json);
        }
    }

    @Override
    public List<FlowGoodsRelationDO> getByEidAndGoodsInSn(Long eid, List<String> goodsInSnList) {
        Assert.notNull(eid, "参数 eid 不能为空");
        Assert.notNull(goodsInSnList, "参数 goodsInSnList 不能为空");
        LambdaQueryWrapper<FlowGoodsRelationDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FlowGoodsRelationDO::getEid, eid);
        lambdaQueryWrapper.in(FlowGoodsRelationDO::getGoodsInSn, goodsInSnList);
        return this.list(lambdaQueryWrapper);
    }

    @Override
    public List<FlowGoodsRelationDO> getByEidAndGoodsInSn(List<Long> eidList, List<String> goodsInSnList) {
        Assert.notEmpty(eidList, "参数 eid 不能为空");
        Assert.notEmpty(goodsInSnList, "参数 goodsInSnList 不能为空");
        LambdaQueryWrapper<FlowGoodsRelationDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(FlowGoodsRelationDO::getEid, eidList);
        lambdaQueryWrapper.in(FlowGoodsRelationDO::getGoodsInSn, goodsInSnList);
        return this.list(lambdaQueryWrapper);
    }

    @Override
    public List<FlowGoodsRelationDO> getByEidAndGoodsInSnAndYlGoodsId(List<QueryFlowGoodsRelationListRequest> list) {
        Assert.notNull(list, "参数 list 不能为空");
        List<Long> eidList = new ArrayList<>();
        List<String> goodsInSnList = new ArrayList<>();
        List<Long> ylGoodsIdList = new ArrayList<>();
        list.forEach(o -> {
            Assert.notNull(o.getEid(), "参数 eid 不能为空");
            Assert.notBlank(o.getGoodsInSn(), "参数 goodsInSn 不能为空");
            Assert.notNull(o.getYlGoodsId(), "参数 ylGoodsId 不能为空");
            eidList.add(o.getEid());
            goodsInSnList.add(o.getGoodsInSn());
            ylGoodsIdList.add(o.getYlGoodsId());
        });

        LambdaQueryWrapper<FlowGoodsRelationDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(FlowGoodsRelationDO::getEid, eidList);
        lambdaQueryWrapper.in(FlowGoodsRelationDO::getGoodsInSn, goodsInSnList);
        lambdaQueryWrapper.in(FlowGoodsRelationDO::getYlGoodsId, ylGoodsIdList);
        return this.list(lambdaQueryWrapper);
    }

    @Override
    public List<FlowGoodsRelationDO> statisticsByYlGoodsId() {
        return this.baseMapper.statisticsByYlGoodsId();
    }

    private GoodsDTO getYlGoods(Long eid, String goodsInSn) {
        List<Long> yilingEids = enterpriseApi.listSubEids(Constants.YILING_EID);
        GoodsDTO yiLingGoods = goodsApi.getYlGoodsByEidAndInSn(eid, goodsInSn, yilingEids);
        return yiLingGoods;
    }

}
