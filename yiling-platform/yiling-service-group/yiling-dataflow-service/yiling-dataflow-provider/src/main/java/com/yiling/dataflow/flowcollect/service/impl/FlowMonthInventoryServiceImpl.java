package com.yiling.dataflow.flowcollect.service.impl;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.flowcollect.dao.FlowMonthInventoryMapper;
import com.yiling.dataflow.flowcollect.dto.FlowMonthInventoryDTO;
import com.yiling.dataflow.flowcollect.dto.request.QueryFlowMonthPageRequest;
import com.yiling.dataflow.flowcollect.entity.FlowMonthInventoryDO;
import com.yiling.dataflow.flowcollect.entity.FlowMonthUploadRecordDO;
import com.yiling.dataflow.flowcollect.service.FlowMonthInventoryService;
import com.yiling.dataflow.flowcollect.service.FlowMonthUploadRecordService;
import com.yiling.dataflow.wash.enums.FlowTypeEnum;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 月流向库存上传数据表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2023-03-06
 */
@Slf4j
@Service
public class FlowMonthInventoryServiceImpl extends BaseServiceImpl<FlowMonthInventoryMapper, FlowMonthInventoryDO> implements FlowMonthInventoryService {

    @Autowired
    FlowMonthCommonService flowMonthCommonService;
    @Autowired
    FlowMonthUploadRecordService flowMonthUploadRecordService;
    @DubboReference
    MqMessageSendApi mqMessageSendApi;

    @Lazy
    @Autowired
    FlowMonthInventoryServiceImpl _this;

    @Override
    public Page<FlowMonthInventoryDTO> queryFlowMonthInventoryPage(QueryFlowMonthPageRequest request) {
        LambdaQueryWrapper<FlowMonthInventoryDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FlowMonthInventoryDO::getTaskId, request.getTaskId());
        Page<FlowMonthInventoryDO> page = this.page(request.getPage(), wrapper);
        return PojoUtils.map(page, FlowMonthInventoryDTO.class);
    }

    @Override
    public boolean updateFlowMonthInventoryAndTask(Long opUserId,Long recordId) {

        CrmEnterpriseDTO crmEnterpriseDTO = flowMonthCommonService.getCrmEnterprise(recordId);

        MqMessageBO mqMessageBO = _this.updateInventoryAndTask(opUserId,crmEnterpriseDTO, recordId);
        if (mqMessageBO != null) {
            mqMessageSendApi.send(mqMessageBO);
        }

        return true;
    }

    public MqMessageBO updateInventoryAndTask(Long opUserId, CrmEnterpriseDTO crmEnterpriseDTO, Long recordId) {
        Long taskId = flowMonthCommonService.saveTask(FlowTypeEnum.GOODS_BATCH, recordId, crmEnterpriseDTO.getId(), opUserId);

        LambdaQueryWrapper<FlowMonthInventoryDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FlowMonthInventoryDO::getRecordId, recordId);

        FlowMonthInventoryDO flowMonthInventoryDO=new FlowMonthInventoryDO();
        flowMonthInventoryDO.setTaskId(taskId);
        this.update(flowMonthInventoryDO,wrapper);
        FlowMonthUploadRecordDO flowMonthUploadRecordDO = new FlowMonthUploadRecordDO();
        flowMonthUploadRecordDO.setId(recordId);
        flowMonthUploadRecordDO.setWashTaskId(taskId);
        flowMonthUploadRecordService.updateById(flowMonthUploadRecordDO);
        // 发送MQ通知
        MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_FLOW_WASH_TASK, Constants.TAG_FLOW_WASH_TASK, taskId.toString());
        mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);

        return mqMessageBO;
    }

}
