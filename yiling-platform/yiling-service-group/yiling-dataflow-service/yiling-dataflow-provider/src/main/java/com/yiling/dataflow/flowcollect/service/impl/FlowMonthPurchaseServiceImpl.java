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
import com.yiling.dataflow.flowcollect.dao.FlowMonthPurchaseMapper;
import com.yiling.dataflow.flowcollect.dto.FlowMonthPurchaseDTO;
import com.yiling.dataflow.flowcollect.dto.request.QueryFlowMonthPageRequest;
import com.yiling.dataflow.flowcollect.entity.FlowMonthPurchaseDO;
import com.yiling.dataflow.flowcollect.entity.FlowMonthUploadRecordDO;
import com.yiling.dataflow.flowcollect.service.FlowMonthPurchaseService;
import com.yiling.dataflow.flowcollect.service.FlowMonthUploadRecordService;
import com.yiling.dataflow.wash.enums.FlowTypeEnum;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 月流向采购上传数据表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2023-03-06
 */
@Slf4j
@Service
public class FlowMonthPurchaseServiceImpl extends BaseServiceImpl<FlowMonthPurchaseMapper, FlowMonthPurchaseDO> implements FlowMonthPurchaseService {

    @Autowired
    FlowMonthCommonService flowMonthCommonService;
    @Autowired
    FlowMonthUploadRecordService flowMonthUploadRecordService;
    @DubboReference
    MqMessageSendApi mqMessageSendApi;

    @Lazy
    @Autowired
    FlowMonthPurchaseServiceImpl _this;

    @Override
    public boolean updateFlowMonthPurchaseAndTask(Long opUserId,Long recordId) {
        CrmEnterpriseDTO crmEnterpriseDTO = flowMonthCommonService.getCrmEnterprise(recordId);

        MqMessageBO mqMessageBO = _this.updatePurchaseAndTask(opUserId, crmEnterpriseDTO, recordId);
        if (mqMessageBO != null) {
            mqMessageSendApi.send(mqMessageBO);
        }

        return true;
    }

    public MqMessageBO updatePurchaseAndTask(Long opUserId,CrmEnterpriseDTO crmEnterpriseDTO, Long recordId) {
        Long taskId = flowMonthCommonService.saveTask(FlowTypeEnum.PURCHASE, recordId, crmEnterpriseDTO.getId(), opUserId);

        LambdaQueryWrapper<FlowMonthPurchaseDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FlowMonthPurchaseDO::getRecordId, recordId);

        FlowMonthPurchaseDO flowMonthPurchaseDO=new FlowMonthPurchaseDO();
        flowMonthPurchaseDO.setTaskId(taskId);
        this.update(flowMonthPurchaseDO,wrapper);
        FlowMonthUploadRecordDO flowMonthUploadRecordDO = new FlowMonthUploadRecordDO();
        flowMonthUploadRecordDO.setId(recordId);
        flowMonthUploadRecordDO.setWashTaskId(taskId);
        flowMonthUploadRecordService.updateById(flowMonthUploadRecordDO);
        // 发送MQ通知
        MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_FLOW_WASH_TASK, Constants.TAG_FLOW_WASH_TASK, taskId.toString());
        mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);

        return mqMessageBO;
    }

    @Override
    public Page<FlowMonthPurchaseDTO> queryFlowMonthPurchasePage(QueryFlowMonthPageRequest request) {
        LambdaQueryWrapper<FlowMonthPurchaseDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FlowMonthPurchaseDO::getTaskId, request.getTaskId());
        Page<FlowMonthPurchaseDO> page = this.page(request.getPage(), wrapper);
        return PojoUtils.map(page, FlowMonthPurchaseDTO.class);
    }

}
