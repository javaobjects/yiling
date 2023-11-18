package com.yiling.dataflow.flow.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.dataflow.flow.dao.FlowEnterpriseGoodsMappingMapper;
import com.yiling.dataflow.flow.dto.FlowEnterpriseGoodsMappingDTO;
import com.yiling.dataflow.flow.dto.request.SaveFlowEnterpriseGoodsMappingRequest;
import com.yiling.dataflow.flow.entity.FlowEnterpriseGoodsMappingDO;
import com.yiling.dataflow.flow.service.FlowEnterpriseGoodsMappingService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import cn.hutool.core.lang.Assert;

/**
 * @author shichen
 * @类名 FlowEnterpriseGoodsMappingServiceImpl
 * @描述
 * @创建时间 2023/2/27
 * @修改人 shichen
 * @修改时间 2023/2/27
 **/
@Service
@Slf4j
public class FlowEnterpriseGoodsMappingServiceImpl extends BaseServiceImpl<FlowEnterpriseGoodsMappingMapper, FlowEnterpriseGoodsMappingDO> implements FlowEnterpriseGoodsMappingService {

    @Autowired
    @Lazy
    FlowEnterpriseGoodsMappingServiceImpl _this;
    @DubboReference
    MqMessageSendApi mqMessageSendApi;

    @Override
    public Long saveOrUpdate(SaveFlowEnterpriseGoodsMappingRequest request) {
        if(request.getId()!=null && request.getId()>0){
            this.updateById(PojoUtils.map(request,FlowEnterpriseGoodsMappingDO.class));
            return request.getId();
        }else {
            FlowEnterpriseGoodsMappingDTO mappingDTO = this.findByFlowGoodsNameAndFlowSpecificationAndCrmEnterpriseId(request.getFlowGoodsName(), request.getFlowSpecification(), request.getCrmEnterpriseId());
            if(null!=mappingDTO){
                request.setId(mappingDTO.getId());
                this.updateById(PojoUtils.map(request,FlowEnterpriseGoodsMappingDO.class));
                return request.getId();
            }
            FlowEnterpriseGoodsMappingDO mappingDO = PojoUtils.map(request, FlowEnterpriseGoodsMappingDO.class);
            this.save(mappingDO);
            return mappingDO.getId();
        }
    }

    @Override
    public FlowEnterpriseGoodsMappingDTO findByFlowGoodsNameAndFlowSpecificationAndCrmEnterpriseId(String flowGoodsName, String flowSpecification, Long crmEnterpriseId) {
        QueryWrapper<FlowEnterpriseGoodsMappingDO> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(FlowEnterpriseGoodsMappingDO::getFlowGoodsName,flowGoodsName)
                .eq(FlowEnterpriseGoodsMappingDO::getFlowSpecification,flowSpecification)
                .eq(FlowEnterpriseGoodsMappingDO::getCrmEnterpriseId,crmEnterpriseId)
                .last("limit 1");
        return PojoUtils.map(this.getOne(queryWrapper),FlowEnterpriseGoodsMappingDTO.class);
    }

    @Override
    public Boolean batchUpdateById(List<SaveFlowEnterpriseGoodsMappingRequest> requestList) {
        List<SaveFlowEnterpriseGoodsMappingRequest> filterList = requestList.stream().filter(request -> null == request.getId() || request.getId() <= 0).collect(Collectors.toList());
        if(CollectionUtil.isNotEmpty(filterList)){
            throw new BusinessException(ResultCode.FAILED,"更新数据的id为空");
        }
        return this.updateBatchById(PojoUtils.map(requestList,FlowEnterpriseGoodsMappingDO.class));
    }

    @Override
    public Boolean sendRefreshGoodsFlowMq(List<FlowEnterpriseGoodsMappingDTO> mappingList) {
        log.info("商品映射刷新流向参数：{}",mappingList);
        return _this.sendMq(Constants.TOPIC_GOODS_MAPPING_MONTH_WASH,Constants.TAG_GOODS_MAPPING_MONTH_WASH, JSON.toJSONString(mappingList));
    }

    @Override
    public FlowEnterpriseGoodsMappingDTO findByCrmEnterpriseIdAndCrmGoodsCode(Long crmEnterpriseId, Long crmGoodsCode) {
        Assert.notNull(crmEnterpriseId, "参数 crmEnterpriseId 不能为空");
        Assert.notNull(crmGoodsCode, "参数 crmGoodsCode 不能为空");
        QueryWrapper<FlowEnterpriseGoodsMappingDO> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(FlowEnterpriseGoodsMappingDO::getCrmEnterpriseId, crmEnterpriseId)
                .eq(FlowEnterpriseGoodsMappingDO::getCrmGoodsCode, crmGoodsCode)
                .last("limit 1");
        return PojoUtils.map(this.getOne(queryWrapper), FlowEnterpriseGoodsMappingDTO.class);
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
}
