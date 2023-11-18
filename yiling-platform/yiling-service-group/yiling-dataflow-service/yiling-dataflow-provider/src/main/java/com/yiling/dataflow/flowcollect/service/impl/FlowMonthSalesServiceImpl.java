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
import com.yiling.dataflow.flowcollect.dao.FlowMonthSalesMapper;
import com.yiling.dataflow.flowcollect.dto.FlowMonthSalesDTO;
import com.yiling.dataflow.flowcollect.dto.request.QueryFlowMonthPageRequest;
import com.yiling.dataflow.flowcollect.entity.FlowMonthSalesDO;
import com.yiling.dataflow.flowcollect.entity.FlowMonthUploadRecordDO;
import com.yiling.dataflow.flowcollect.service.FlowMonthSalesService;
import com.yiling.dataflow.flowcollect.service.FlowMonthUploadRecordService;
import com.yiling.dataflow.wash.enums.FlowTypeEnum;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 月流向销售上传数据表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2023-03-06
 */
@Slf4j
@Service
public class FlowMonthSalesServiceImpl extends BaseServiceImpl<FlowMonthSalesMapper, FlowMonthSalesDO> implements FlowMonthSalesService {

    @Autowired
    FlowMonthCommonService flowMonthCommonService;
    @Autowired
    FlowMonthUploadRecordService flowMonthUploadRecordService;
    @DubboReference
    MqMessageSendApi mqMessageSendApi;

    @Lazy
    @Autowired
    FlowMonthSalesServiceImpl _this;

    @Override
    public Page<FlowMonthSalesDTO> queryFlowMonthSalePage(QueryFlowMonthPageRequest request) {
        LambdaQueryWrapper<FlowMonthSalesDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FlowMonthSalesDO::getTaskId, request.getTaskId());
        Page<FlowMonthSalesDO> page = this.page(request.getPage(), wrapper);
        return PojoUtils.map(page, FlowMonthSalesDTO.class);
    }

    @Override
    public boolean updateFlowMonthSalesAndTask(Long opUserId,Long recordId) {
        CrmEnterpriseDTO crmEnterpriseDTO = flowMonthCommonService.getCrmEnterprise(recordId);

        MqMessageBO mqMessageBO = _this.updateSalesAndTask(opUserId, crmEnterpriseDTO,recordId);
        if (mqMessageBO != null) {
            mqMessageSendApi.send(mqMessageBO);
        }
        return true;
    }


    public MqMessageBO updateSalesAndTask(Long opUserId, CrmEnterpriseDTO crmEnterpriseDTO, Long recordId) {
        Long taskId = flowMonthCommonService.saveTask(FlowTypeEnum.SALE, recordId, crmEnterpriseDTO.getId(),opUserId);

        LambdaQueryWrapper<FlowMonthSalesDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FlowMonthSalesDO::getRecordId, recordId);

        FlowMonthSalesDO flowMonthSalesDO=new FlowMonthSalesDO();
        flowMonthSalesDO.setTaskId(taskId);
        this.update(flowMonthSalesDO,wrapper);
        FlowMonthUploadRecordDO flowMonthUploadRecordDO = new FlowMonthUploadRecordDO();
        flowMonthUploadRecordDO.setId(recordId);
        flowMonthUploadRecordDO.setWashTaskId(taskId);
        flowMonthUploadRecordService.updateById(flowMonthUploadRecordDO);
//        // 发送MQ通知
        MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_FLOW_WASH_TASK, Constants.TAG_FLOW_WASH_TASK, taskId.toString());
        mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);

        return mqMessageBO;
    }

}
