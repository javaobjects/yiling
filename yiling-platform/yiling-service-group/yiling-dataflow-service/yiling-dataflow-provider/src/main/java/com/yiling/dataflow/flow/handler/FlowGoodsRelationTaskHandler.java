package com.yiling.dataflow.flow.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.dataflow.flow.FlowUtils;
import com.yiling.dataflow.order.entity.FlowSaleDO;
import com.yiling.dataflow.order.service.FlowSaleService;
import com.yiling.dataflow.order.service.FlowSettlementEnterpriseTagService;
import com.yiling.dataflow.relation.dto.request.SaveFlowGoodsRelationEditTaskRequest;
import com.yiling.dataflow.relation.entity.FlowGoodsRelationDO;
import com.yiling.dataflow.relation.enums.FlowGoodsRelationEditTaskBusinessTypeEnum;
import com.yiling.dataflow.relation.service.FlowGoodsRelationEditTaskService;
import com.yiling.dataflow.relation.service.FlowGoodsRelationService;
import com.yiling.framework.common.util.Constants;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: houjie.sun
 * @date: 2022/12/26
 */
@Slf4j
@Service
public class FlowGoodsRelationTaskHandler {

    @Autowired
    private FlowSaleService flowSaleService;
    @Autowired
    private FlowGoodsRelationService flowGoodsRelationService;
    @Autowired
    private FlowGoodsRelationEditTaskService flowGoodsRelationEditTaskService;
    @Autowired
    private FlowSettlementEnterpriseTagService flowSettlementEnterpriseTagService;
    @Autowired
    @Lazy
    FlowGoodsRelationTaskHandler _this;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @DubboReference
    MqMessageSendApi mqMessageSendApi;

    private static final String DATAFLOW_ENTERPRISE_TAG_EIDS = "dataflow:enterpriseTag:eids";

    /**
     * 销售匹配yiGoodsId变更时，保存流向销售单主键id的修改任务，并发送消息给流向报表
     *
     * @param flowSaleIds
     */
    public void flowSaleYlGoodsIdSendMsg(List<Long> flowSaleIds) {
        Assert.notEmpty(flowSaleIds, "参数 flowSaleIds 不能为空");

        List<List<Long>> flowSaleIdSubList = FlowUtils.partitionList(flowSaleIds, 200);
        for (List<Long> idList : flowSaleIdSubList) {
            List<FlowSaleDO> flowSaleDOList = flowSaleService.getByIdsContainsDeleteFlag(idList);
            if (CollUtil.isEmpty(flowSaleDOList)) {
                log.warn("FlowGoodsRelationTaskHandler, flowSaleYlGoodsIdSendMsg, flowSaleDOList is empty, flowSaleIds:{}", JSONUtil.toJsonStr(idList));
                return;
            }
            List<Long> flowSaleEidList = flowSaleDOList.stream().filter(o -> o.getEid().intValue() != 0).map(FlowSaleDO::getEid).distinct().collect(Collectors.toList());

            // 是否符合企业标签
            List<Long> effectiveEidList = getEnterpriseTagEidList(idList, flowSaleEidList);
            if (CollUtil.isEmpty(effectiveEidList)) {
                return;
            }
            flowSaleDOList = flowSaleDOList.stream().filter(o -> effectiveEidList.contains(o.getEid())).collect(Collectors.toList());

            // 保存以岭品关系修改任务
            saveFlowGoodsRelationEditTask(flowSaleDOList);
        }
    }

    private List<Long> getEnterpriseTagEidList(List<Long> flowSaleIds, List<Long> flowSaleEidList) {
        String enterpriseTagEidStr = stringRedisTemplate.opsForValue().get(DATAFLOW_ENTERPRISE_TAG_EIDS);
        if (StrUtil.isBlank(enterpriseTagEidStr)) {
            List<Long> eidList = flowSettlementEnterpriseTagService.getFlowSettlementEnterpriseTagNameList();
            if (CollUtil.isNotEmpty(eidList)) {
                String jsonStr = JSONUtil.toJsonStr(eidList);
                stringRedisTemplate.opsForValue().set(DATAFLOW_ENTERPRISE_TAG_EIDS, jsonStr, 5, TimeUnit.MINUTES);
                enterpriseTagEidStr = jsonStr;
            }
        }
        JSONArray enterpriseTagEidArray = JSONUtil.parseArray(enterpriseTagEidStr);
        if (ObjectUtil.isNull(enterpriseTagEidArray) || enterpriseTagEidArray.size() == 0) {
            log.warn("FlowGoodsRelationTaskHandler, flowSaleYlGoodsIdSendMsg, enterpriseTagEidArray is empty");
            return ListUtil.empty();
        }
        List<Object> eids = enterpriseTagEidArray.stream().distinct().collect(Collectors.toList());
        List<Long> eidList = Convert.toList(Long.class, eids);

        // 符合企业标签的企业id
        List<Long> effectiveEidList = flowSaleEidList.stream().filter(o -> eidList.contains(o)).distinct().collect(Collectors.toList());

        if (CollUtil.isEmpty(effectiveEidList)) {
            log.warn("FlowGoodsRelationTaskHandler, flowSaleYlGoodsIdSendMsg, 符合企业标签的企业id为空, flowSaleIds:{}, flowSaleEidList:{}, ", JSONUtil.toJsonStr(flowSaleIds), JSONUtil.toJsonStr(flowSaleEidList));
            return ListUtil.empty();
        }
        return effectiveEidList;
    }

    /**
     * 保存以岭品关系修改任务
     *
     * @param list
     * @return
     */
    private boolean saveFlowGoodsRelationEditTask(List<FlowSaleDO> list) {
        if (CollUtil.isEmpty(list)) {
            return false;
        }
        // 以岭品关系
        List<Long> eidList = list.stream().map(FlowSaleDO::getEid).distinct().collect(Collectors.toList());
        List<String> goodsInSnList = list.stream().filter(o -> StrUtil.isNotBlank(o.getGoodsInSn())).map(FlowSaleDO::getGoodsInSn).distinct().collect(Collectors.toList());
        List<FlowGoodsRelationDO> flowGoodsRelationList = flowGoodsRelationService.getByEidAndGoodsInSn(eidList, goodsInSnList);
        if (CollUtil.isNotEmpty(flowGoodsRelationList)) {
            flowGoodsRelationList = flowGoodsRelationList.stream().filter(o -> ObjectUtil.isNotNull(o.getYlGoodsId()) && 0 != o.getYlGoodsId().intValue()).collect(Collectors.toList());
        }
        // 根据eid、goodsInSn分组
        Map<String, FlowGoodsRelationDO> flowGoodsRelationMap = flowGoodsRelationList.stream().collect(Collectors.toMap(o -> o.getEid() + "_" + o.getGoodsInSn().trim(), o -> o, (k1, k2) -> k1));
        // 组装以岭品关系修改任务，key = flow_goods_relation_id + eid, value = 销售单流向id集合
        Map<String, List<Long>> relationIdAndEidMap = new HashMap<>();
        for (FlowSaleDO flowSaleDO : list) {
            FlowGoodsRelationDO flowGoodsRelationDO = flowGoodsRelationMap.get(flowSaleDO.getEid() + "_" + flowSaleDO.getGoodsInSn());
            if (ObjectUtil.isNotNull(flowGoodsRelationDO)) {
                // 匹配关系id主键
                String taskKey = flowGoodsRelationDO.getId() + "_" + flowGoodsRelationDO.getEid();
                List<Long> flowSaleIdList = relationIdAndEidMap.get(taskKey);
                if (CollUtil.isEmpty(flowSaleIdList)) {
                    flowSaleIdList = new ArrayList<>();
                }
                flowSaleIdList.add(flowSaleDO.getId());
                relationIdAndEidMap.put(taskKey, flowSaleIdList);
            }
        }
        //没有以岭商品ID不发送mq
        if (MapUtil.isNotEmpty(relationIdAndEidMap)) {
            List<SaveFlowGoodsRelationEditTaskRequest> taskRequestList = new ArrayList<>();
            Date date = new Date();
            for (String key : relationIdAndEidMap.keySet()) {
                List<Long> flowSaleIdList = relationIdAndEidMap.get(key);
                String[] keyArray = key.split("_");
                SaveFlowGoodsRelationEditTaskRequest request = new SaveFlowGoodsRelationEditTaskRequest();
                request.setBusinessType(FlowGoodsRelationEditTaskBusinessTypeEnum.ERP_FLOW_SALE_SYNC.getCode());
                request.setFlowGoodsRelationId(Long.parseLong(keyArray[0]));
                request.setEid(Long.parseLong(keyArray[1]));
                request.setFlowSaleIds(String.join(",", Convert.toList(String.class, flowSaleIdList)));
                request.setOpUserId(0L);
                request.setOpTime(date);
                taskRequestList.add(request);
            }
            if (CollUtil.isEmpty(taskRequestList)) {
                return false;
            }
            List<Long> taskIds = flowGoodsRelationEditTaskService.saveByRequestList(taskRequestList);
            if (CollUtil.isNotEmpty(taskIds)) {
                // 发送返利报表消息，以岭品关系修改任务表主键
                JSONObject dataFlowJson = new JSONObject();
                dataFlowJson.put("taskIds", taskIds);
                String json = JSONUtil.toJsonStr(dataFlowJson);
                MqMessageBO mqMessageBO = _this.sendPrepare(Constants.TOPIC_FLOW_GOODS_RELATION_EDIT_TASK_SEND, Constants.TAG_FLOW_GOODS_RELATION_EDIT_TASK_SEND, json);
                mqMessageSendApi.send(mqMessageBO);

                // 销售更新同步返利状态，销售未逻辑删除、商品内码不为空、同步返利状态=0
                List<Long> flowSaleUpdateIdList = list.stream().filter(o -> 0 == o.getDelFlag().intValue() && StrUtil.isNotBlank(o.getGoodsInSn()) && (ObjectUtil.isNull(o.getReportSyncStatus()) || 0 == o.getReportSyncStatus().intValue())).map(FlowSaleDO::getId).distinct().collect(Collectors.toList());
                if (CollUtil.isNotEmpty(flowSaleUpdateIdList)) {
                    flowSaleService.updateReportSyncStatusByIdList(flowSaleUpdateIdList);
                }

                return true;
            }
        }
        return false;
    }

    /**
     * 消息持久化
     *
     * @param topic
     * @param topicTag
     * @param msg
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public MqMessageBO sendPrepare(String topic, String topicTag, String msg) {
        MqMessageBO mqMessageBO = new MqMessageBO(topic, topicTag, msg);
        mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
        return mqMessageBO;
    }

}
