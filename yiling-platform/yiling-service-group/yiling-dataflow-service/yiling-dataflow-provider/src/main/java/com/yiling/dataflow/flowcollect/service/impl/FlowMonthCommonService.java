package com.yiling.dataflow.flowcollect.service.impl;

import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.service.CrmEnterpriseService;
import com.yiling.dataflow.flowcollect.entity.FlowMonthUploadRecordDO;
import com.yiling.dataflow.flowcollect.enums.FlowMonthUploadTypeEnum;
import com.yiling.dataflow.flowcollect.service.FlowMonthUploadRecordService;
import com.yiling.dataflow.wash.dto.FlowMonthWashControlDTO;
import com.yiling.dataflow.wash.dto.request.SaveFlowMonthWashTaskRequest;
import com.yiling.dataflow.wash.enums.CollectionMethodEnum;
import com.yiling.dataflow.wash.enums.FlowClassifyEnum;
import com.yiling.dataflow.wash.enums.FlowTypeEnum;
import com.yiling.dataflow.wash.service.FlowMonthWashControlService;
import com.yiling.dataflow.wash.service.FlowMonthWashTaskService;
import com.yiling.framework.common.util.PojoUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 月流向上传公共 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2023-03-07
 */
@Slf4j
@Component
public class FlowMonthCommonService {

    @Autowired
    FlowMonthWashControlService flowMonthWashControlService;
    @Autowired
    FlowMonthWashTaskService flowMonthWashTaskService;
    @Autowired
    FlowMonthUploadRecordService flowMonthUploadRecordService;
    @Autowired
    CrmEnterpriseService crmEnterpriseService;


    public CrmEnterpriseDTO getCrmEnterprise(Long uploadRecordId) {
        FlowMonthUploadRecordDO flowMonthUploadRecordDO = flowMonthUploadRecordService.getById(uploadRecordId);
        String code = flowMonthUploadRecordDO.getFileName().split("_")[1];
        return PojoUtils.map(crmEnterpriseService.getById(code), CrmEnterpriseDTO.class);
    }

    @Transactional(rollbackFor = Exception.class)
    public Long saveTask(FlowTypeEnum flowTypeEnum, Long recordId, Long crmEnterpriseId, Long opUserId) {
        FlowMonthWashControlDTO flowMonthWashControlDTO = flowMonthWashControlService.getWashStatus();

        SaveFlowMonthWashTaskRequest request = new SaveFlowMonthWashTaskRequest();
        request.setCrmEnterpriseId(crmEnterpriseId);
        request.setFmwcId(flowMonthWashControlDTO.getId());
        request.setCollectionMethod(CollectionMethodEnum.EXCEL.getCode());
        FlowMonthUploadRecordDO flowMonthUploadRecordDO = flowMonthUploadRecordService.getById(recordId);
        if(Objects.nonNull(flowMonthUploadRecordDO) && flowMonthUploadRecordDO.getUploadType().equals(FlowMonthUploadTypeEnum.FIX.getCode())){
            request.setFlowClassify(FlowClassifyEnum.SUPPLY_TRANS_MONTH_FLOW.getCode());
            request.setAppealType(1);
        }else{
            request.setFlowClassify(FlowClassifyEnum.NORMAL.getCode());
        }
        request.setFlowType(flowTypeEnum.getCode());
        request.setFileName(Optional.ofNullable(flowMonthUploadRecordDO).orElse(new FlowMonthUploadRecordDO()).getFileName());
        request.setOpUserId(opUserId);

        return flowMonthWashTaskService.create(request, false);
    }

}
