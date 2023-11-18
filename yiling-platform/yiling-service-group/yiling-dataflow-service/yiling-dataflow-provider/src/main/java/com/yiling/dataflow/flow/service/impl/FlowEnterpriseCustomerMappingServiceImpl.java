package com.yiling.dataflow.flow.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.dataflow.flow.dao.FlowEnterpriseCustomerMappingMapper;
import com.yiling.dataflow.flow.dto.FlowEnterpriseCustomerMappingDTO;
import com.yiling.dataflow.flow.dto.request.SaveFlowEnterpriseCustomerMappingRequest;
import com.yiling.dataflow.flow.entity.FlowEnterpriseCustomerMappingDO;
import com.yiling.dataflow.flow.service.FlowEnterpriseCustomerMappingService;
import com.yiling.dataflow.wash.enums.WashErrorEnum;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.rocketmq.mq.RocketMqProducerService;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 FlowEnterpriseCustomerMappingServiceImpl
 * @描述
 * @创建时间 2023/3/1
 * @修改人 shichen
 * @修改时间 2023/3/1
 **/
@Service
@Slf4j
public class FlowEnterpriseCustomerMappingServiceImpl extends BaseServiceImpl<FlowEnterpriseCustomerMappingMapper, FlowEnterpriseCustomerMappingDO> implements FlowEnterpriseCustomerMappingService {

    @Autowired
    @Lazy
    FlowEnterpriseCustomerMappingServiceImpl _this;
    @DubboReference
    MqMessageSendApi mqMessageSendApi;

    @Autowired(required = false)
    private RocketMqProducerService rocketMqProducerService;

    @Override
    public Long saveOrUpdate(SaveFlowEnterpriseCustomerMappingRequest request) {
        if(request.getId()!=null && request.getId()>0){
            this.updateById(PojoUtils.map(request, FlowEnterpriseCustomerMappingDO.class));
            return request.getId();
        }else {
            FlowEnterpriseCustomerMappingDTO mappingDTO = this.findByCustomerNameAndCrmEnterpriseId(request.getFlowCustomerName(), request.getCrmEnterpriseId());
            if(null!=mappingDTO){
                request.setId(mappingDTO.getId());
                this.updateById(PojoUtils.map(request,FlowEnterpriseCustomerMappingDO.class));
                return request.getId();
            }
            FlowEnterpriseCustomerMappingDO mappingDO = PojoUtils.map(request, FlowEnterpriseCustomerMappingDO.class);
            this.save(mappingDO);

            // 自动跑客户匹配度
            unlockCustomerMatchingRateHandle(mappingDO);
            return mappingDO.getId();
        }
    }

    @Override
    public FlowEnterpriseCustomerMappingDTO findByCustomerNameAndCrmEnterpriseId(String flowCustomerName, Long crmEnterpriseId) {
        QueryWrapper<FlowEnterpriseCustomerMappingDO> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(FlowEnterpriseCustomerMappingDO::getFlowCustomerName,flowCustomerName)
                .eq(FlowEnterpriseCustomerMappingDO::getCrmEnterpriseId,crmEnterpriseId)
                .last("limit 1");
        return PojoUtils.map(this.getOne(queryWrapper),FlowEnterpriseCustomerMappingDTO.class);
    }

    @Override
    public Boolean batchUpdateById(List<SaveFlowEnterpriseCustomerMappingRequest> requestList) {
        List<SaveFlowEnterpriseCustomerMappingRequest> filterList = requestList.stream().filter(request -> null == request.getId() || request.getId() <= 0).collect(Collectors.toList());
        if(CollectionUtil.isNotEmpty(filterList)){
            throw new BusinessException(ResultCode.FAILED,"更新数据的id为空");
        }
        return this.updateBatchById(PojoUtils.map(requestList, FlowEnterpriseCustomerMappingDO.class));
    }

    @Override
    public Boolean sendRefreshCustomerFlowMq(List<FlowEnterpriseCustomerMappingDTO> mappingList) {
        log.info("客户映射刷新流向参数：{}",mappingList);
        return _this.sendMq(Constants.TOPIC_CUSTOMER_MAPPING_MONTH_WASH,Constants.TAG_CUSTOMER_MAPPING_MONTH_WASH, JSON.toJSONString(mappingList));
    }

    @Override
    public List<FlowEnterpriseCustomerMappingDTO> findUnmappedByFlowCustomerName(String flowCustomerName) {
        if(StringUtils.isBlank(flowCustomerName)){
            return ListUtil.empty();
        }
        QueryWrapper<FlowEnterpriseCustomerMappingDO> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(FlowEnterpriseCustomerMappingDO::getFlowCustomerName,flowCustomerName)
                .eq(FlowEnterpriseCustomerMappingDO::getCrmOrgId,0L);
        return PojoUtils.map(this.list(queryWrapper),FlowEnterpriseCustomerMappingDTO.class);
    }

    /**
     * 发送消息
     *
     * @param topic
     * @param topicTag
     * @param msg
     * @return
     */
    public boolean sendMq(String topic,String topicTag,String msg) {
        MqMessageBO mqMessageBO = _this.sendPrepare(topic, topicTag, msg);
        mqMessageSendApi.send(mqMessageBO);
        return true;
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
    public MqMessageBO sendPrepare(String topic,String topicTag,String msg) {
        MqMessageBO mqMessageBO = new MqMessageBO(topic, topicTag, msg);
        mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
        return mqMessageBO;
    }

    private void unlockCustomerMatchingRateHandle(FlowEnterpriseCustomerMappingDO mappingDO) {
        if (mappingDO.getCrmOrgId() == null || mappingDO.getCrmOrgId() == 0) {
            Map<String, Object> params = new HashMap<>();
            params.put("name", mappingDO.getFlowCustomerName());
            SendResult sendResult = rocketMqProducerService.sendSync(Constants.TOPIC_UNLOCK_CUSTOMER_MATCHING_RATE_TASK, Constants.TAG_UNLOCK_CUSTOMER_MATCHING_RATE_TASK, DateUtil.formatDate(new Date()), JSONUtil.toJsonStr(params));
            if (sendResult == null || !sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
                log.error("自动客户对照匹配度任务处理执行失败，客户名称：{}", mappingDO.getFlowCustomerName());
            }
        }
    }
}
