package com.yiling.dataflow.relation.handler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.order.dto.request.QueryFlowSalePageByEidAndGoodsInSnRequest;
import com.yiling.dataflow.order.dto.request.SaveFlowPurchaseInventoryLogRequest;
import com.yiling.dataflow.order.entity.FlowPurchaseInventoryDO;
import com.yiling.dataflow.order.entity.FlowSaleDO;
import com.yiling.dataflow.order.enums.FlowPurchaseInventoryBusinessTypeEnum;
import com.yiling.dataflow.order.service.FlowGoodsBatchService;
import com.yiling.dataflow.order.service.FlowPurchaseInventoryLogService;
import com.yiling.dataflow.order.service.FlowPurchaseInventoryService;
import com.yiling.dataflow.order.service.FlowPurchaseService;
import com.yiling.dataflow.order.service.FlowSaleService;
import com.yiling.dataflow.order.service.FlowSettlementEnterpriseTagService;
import com.yiling.dataflow.relation.dto.request.SaveFlowGoodsRelationEditTaskRequest;
import com.yiling.dataflow.relation.entity.FlowGoodsRelationDO;
import com.yiling.dataflow.relation.enums.FlowGoodsRelationEditTaskBusinessTypeEnum;
import com.yiling.dataflow.relation.enums.FlowGoodsRelationEditTaskSyncStatusEnum;
import com.yiling.dataflow.relation.service.FlowGoodsRelationEditTaskService;
import com.yiling.dataflow.relation.service.FlowGoodsRelationService;
import com.yiling.framework.common.redis.RedisDistributedLock;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.rocketmq.mq.RocketMqProducerService;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsFullDTO;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.user.enterprise.api.EnterpriseTagApi;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 商家品与以岭品映射关系变更处理
 *
 * @author: houjie.sun
 * @date: 2022/6/8
 */
@Slf4j
@Service
public class FlowGoodsRelationHandler {

    @Autowired
    private FlowPurchaseService flowPurchaseService;
    @Autowired
    private FlowSaleService flowSaleService;
    @Autowired
    private FlowGoodsBatchService flowGoodsBatchService;
    @Autowired(required = false)
    private RocketMqProducerService rocketMqProducerService;
    @Autowired
    protected RedisDistributedLock redisDistributedLock;
    @Autowired
    private FlowSettlementEnterpriseTagService flowSettlementEnterpriseTagService;
    @Autowired
    private FlowGoodsRelationService flowGoodsRelationService;
    @Autowired
    private FlowPurchaseInventoryService flowPurchaseInventoryService;
    @Autowired
    private FlowGoodsRelationEditTaskService flowGoodsRelationEditTaskService;
    @Autowired
    private FlowPurchaseInventoryLogService flowPurchaseInventoryLogService;

    @DubboReference
    EnterpriseTagApi enterpriseTagApi;
    @DubboReference
    private GoodsApi goodsApi;

    public int handleFlowGoodsRelationMqSync(Long flowGoodsRelationId, Long opUserId) {
        if (ObjectUtil.isNull(flowGoodsRelationId)) {
            return -1;
        }

        long startTime = System.currentTimeMillis();
        log.info("商家品与以岭品映射关系变更，流向更新ylGoodsId开始，startTime:{}", startTime);

        FlowGoodsRelationDO flowGoodsRelation = flowGoodsRelationService.getById(flowGoodsRelationId);
        if (ObjectUtil.isNull(flowGoodsRelation)) {
            log.info("商家品与以岭品映射关系变更，流向更新ylGoodsId开始，以岭品关系不存在，flowGoodsRelationId:{}", flowGoodsRelationId);
            return -1;
        }
        Long eid = flowGoodsRelation.getEid();
        String goodsInSn = flowGoodsRelation.getGoodsInSn();
        Long ylGoodsId = flowGoodsRelation.getYlGoodsId();

        // 当前企业是否存在正在同步的任务
        Boolean relationEditTaskFlag = flowGoodsRelationEditTaskService.existFlowGoodsRelationEditTask(flowGoodsRelationId, FlowGoodsRelationEditTaskSyncStatusEnum.DOING.getCode());
        if(relationEditTaskFlag){
            log.warn("商家品与以岭品映射关系变更，以岭品关系id:{}修改，此企业id:{}已经存在正在同步中的以岭品修改任务");
            return 0;
        }
        if (ObjectUtil.isNull(ylGoodsId)) {
            // 修改时删除映射关系
            ylGoodsId = 0L;
        }

        /* 匹配流向销售单、保存以岭品关系修改任务 */
        buildFlowGoodsRelationEditTask(eid, goodsInSn, flowGoodsRelationId);

        /* 采购库存更新以岭品id */
        updatePurchaseInventory(eid, goodsInSn, flowGoodsRelationId, ylGoodsId, opUserId);

        long endTime = System.currentTimeMillis();
        log.info("商家品与以岭品映射关系变更，流向更新ylGoodsId结束，endTime:{}, 耗时:{}", endTime, endTime - startTime);
        return 1;
    }

    /**
     * 匹配流向销售单、保存以岭品关系修改任务
     * 根据eid、goodsInSn查询销售流向，销售单id多个则用逗号隔开
     *
     * @param eid 企业id
     * @param goodsInSn 商品内码
     * @param flowGoodsRelationId 以岭品关系id
     */
    private boolean buildFlowGoodsRelationEditTask(Long eid, String goodsInSn, Long flowGoodsRelationId) {
        int size = 200;
        List<Long> flowSaleIdList = new ArrayList<>();
        List<Long> flowSaleUpdateIdList = new ArrayList<>();
        // 是否符合企业标签
        boolean enterpriseTagFlag = false;
        List<Long> eidList = flowSettlementEnterpriseTagService.getFlowSettlementEnterpriseTagNameList();
        if (CollUtil.isNotEmpty(eidList) && eidList.contains(eid)) {
            enterpriseTagFlag = true;
        }

        Page<FlowSaleDO> salePage = null;
        int saleCurrent = 1;
        QueryFlowSalePageByEidAndGoodsInSnRequest saleRequest = new QueryFlowSalePageByEidAndGoodsInSnRequest();
        saleRequest.setSize(size);
        saleRequest.setEid(eid);
        saleRequest.setGoodsInSn(goodsInSn);
        do {
            saleRequest.setCurrent(saleCurrent);
            salePage = flowSaleService.pageByEidAndGoodsInSn(saleRequest);
            if (ObjectUtil.isNull(salePage) || CollUtil.isEmpty(salePage.getRecords())) {
                break;
            }
            List<Long> idList = salePage.getRecords().stream().map(FlowSaleDO::getId).distinct().collect(Collectors.toList());
            flowSaleIdList.addAll(idList);
            List<Long> updateIdList = salePage.getRecords().stream().filter(o -> ObjectUtil.isNull(o.getReportSyncStatus()) || ObjectUtil.equal(0, o.getReportSyncStatus())).map(FlowSaleDO::getId).distinct().collect(Collectors.toList());
            flowSaleUpdateIdList.addAll(updateIdList);
            if(salePage.getRecords().size() < size){
                break;
            }
            saleCurrent++;
        } while (salePage != null && CollUtil.isNotEmpty(salePage.getRecords()));

        // 保存以岭品关系修改任务
        if (enterpriseTagFlag && CollUtil.isNotEmpty(flowSaleIdList)) {
            SaveFlowGoodsRelationEditTaskRequest flowGoodsRelationEditTaskRequest = new SaveFlowGoodsRelationEditTaskRequest();
            flowGoodsRelationEditTaskRequest.setBusinessType(FlowGoodsRelationEditTaskBusinessTypeEnum.FLOW_GOODS_RELATION_EDIT.getCode());
            flowGoodsRelationEditTaskRequest.setFlowGoodsRelationId(flowGoodsRelationId);
            flowGoodsRelationEditTaskRequest.setEid(eid);
            flowGoodsRelationEditTaskRequest.setFlowSaleIds(String.join(",", Convert.toList(String.class, flowSaleIdList)));
            flowGoodsRelationEditTaskRequest.setOpUserId(0L);
            flowGoodsRelationEditTaskRequest.setOpTime(new Date());
            Long taskId = flowGoodsRelationEditTaskService.saveByRequest(flowGoodsRelationEditTaskRequest);
            if (ObjectUtil.isNotNull(taskId) && 0 != taskId.intValue()) {
                // 发送返利报表消息，以岭品关系修改任务表主键
                List<Long> taskIds = new ArrayList<>();
                taskIds.add(taskId);
                JSONObject dataFlowJson = new JSONObject();
                dataFlowJson.put("taskIds", taskIds);
                String json = JSONUtil.toJsonStr(dataFlowJson);
                SendResult sendResult = rocketMqProducerService.sendSync(Constants.TOPIC_FLOW_GOODS_RELATION_EDIT_TASK_SEND, taskId.toString(), DateUtil.formatDate(new Date()), json);
                if (sendResult == null || !sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
                    log.error("FlowGoodsRelationHandler.handleFlowGoodsRelationMqSync, 发送以岭品关系修改任务消息失败, json:{}", json);
                }
                // 销售更新同步返利状态
                if (CollUtil.isNotEmpty(flowSaleUpdateIdList)) {
                    flowSaleService.updateReportSyncStatusByIdList(flowSaleUpdateIdList);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 采购库存更新以岭品id
     *
     * @param eid 企业id
     * @param goodsInSn 商品内码
     * @param flowGoodsRelationId 以岭品关系id
     * @param ylGoodsId 修改后以岭品id
     * @param opUserId 操作人
     */
    private void updatePurchaseInventory(Long eid, String goodsInSn, Long flowGoodsRelationId, Long ylGoodsId, Long opUserId) {
        // 以岭品信息
        GoodsFullDTO ylGoods = goodsApi.queryFullInfo(ylGoodsId);
        if (ObjectUtil.isNotNull(ylGoods) && ylGoods.getAuditStatus().intValue() == GoodsStatusEnum.AUDIT_PASS.getCode().intValue()) {
            List<FlowPurchaseInventoryDO> flowPurchaseInventoryList = flowPurchaseInventoryService.getByEidAndGoodsInSn(eid, goodsInSn);
            if (CollUtil.isNotEmpty(flowPurchaseInventoryList)) {
                Date date = new Date();
                for (FlowPurchaseInventoryDO purchaseInventoryOld : flowPurchaseInventoryList) {
                    String inventoryLockName = RedisKey.generate("mph-erp-online-lock-flow-purchase-inventory", "updateYlGoodsId", purchaseInventoryOld.getId().toString());
                    String lockId = redisDistributedLock.lock2(inventoryLockName, 5, 5, TimeUnit.SECONDS);
                    try {
                        FlowPurchaseInventoryDO flowPurchaseInventoryNew = new FlowPurchaseInventoryDO();
                        flowPurchaseInventoryNew.setId(purchaseInventoryOld.getId());
                        flowPurchaseInventoryNew.setYlGoodsId(ylGoodsId);
                        flowPurchaseInventoryService.updateById(flowPurchaseInventoryNew);
                        // 日志
                        SaveFlowPurchaseInventoryLogRequest logRequest = new SaveFlowPurchaseInventoryLogRequest();
                        logRequest.setBusinessType(FlowPurchaseInventoryBusinessTypeEnum.FLOW_GOODS_RELATION.getCode());
                        logRequest.setFlowPurchaseInventoryId(purchaseInventoryOld.getId());
                        logRequest.setPoSource(purchaseInventoryOld.getPoSource());
                        logRequest.setEid(purchaseInventoryOld.getEid());
                        logRequest.setYlGoodsId(ylGoodsId);
                        logRequest.setGoodsInSn(goodsInSn);
                        logRequest.setBeforeQuantity(purchaseInventoryOld.getPoQuantity());
                        logRequest.setChangeQuantity(BigDecimal.ZERO);
                        logRequest.setAfterQuantity(purchaseInventoryOld.getPoQuantity());
                        logRequest.setOpUserId(opUserId);
                        logRequest.setOpTime(date);
                        flowPurchaseInventoryLogService.saveGoodInventoryLog(logRequest);
                    } finally {
                        redisDistributedLock.releaseLock(inventoryLockName, lockId);
                    }
                }
            }
        } else {
            log.info("商家品与以岭品映射关系变更，采购库存更新ylGoodsId开始，以岭品信息不存在或未审核通过，flowGoodsRelationId:{}, ylGoodsId:{}", flowGoodsRelationId,ylGoodsId);
        }
    }

}
