package com.yiling.dataflow.flowcollect.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.flowcollect.api.FlowMonthUploadRecordApi;
import com.yiling.dataflow.flowcollect.bo.FlowMonthUploadRecordBO;
import com.yiling.dataflow.flowcollect.dto.FlowMonthUploadRecordDTO;
import com.yiling.dataflow.flowcollect.dto.request.QueryFlowMonthUploadPageRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowMonthUploadRecordRequest;
import com.yiling.dataflow.flowcollect.dto.request.UpdateFlowMonthUploadRecordRequest;
import com.yiling.dataflow.flowcollect.service.FlowMonthUploadRecordService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;

import lombok.extern.slf4j.Slf4j;

/**
 * 月流向上传记录 API实现
 *
 * @author: lun.yu
 * @date: 2023-03-04
 */
@Slf4j
@DubboService
public class FlowMonthUploadRecordApiImpl implements FlowMonthUploadRecordApi {

    @Autowired
    private FlowMonthUploadRecordService flowMonthUploadRecordService;

    @Override
    public Page<FlowMonthUploadRecordBO> queryFlowMonthPage(QueryFlowMonthUploadPageRequest request) {
        return flowMonthUploadRecordService.queryFlowMonthPage(request);
    }

    @Override
    public String checkFlowFileName(String fileName) {
        return flowMonthUploadRecordService.checkFlowFileName(fileName);
    }

    @Override
    public Long saveFlowMonthRecord(SaveFlowMonthUploadRecordRequest request) {
        return flowMonthUploadRecordService.saveFlowMonthRecord(request);
    }

    @Override
    public boolean updateFlowMonthRecord(UpdateFlowMonthUploadRecordRequest request) {
        return flowMonthUploadRecordService.updateFlowMonthRecord(request);
    }

    @Override
    public Integer getDataType(String fileName) {
        return flowMonthUploadRecordService.getDataType(fileName);
    }

    @Override
    public String getExcelCodeByFileName(String fileName) {
        return flowMonthUploadRecordService.getExcelCodeByFileName(fileName);
    }

    @Override
    public Boolean deleteRecord(Long id, Long currentUserId) {
        return flowMonthUploadRecordService.deleteRecord(id, currentUserId);
    }

    @Override
    public FlowMonthUploadRecordDTO getByFileName(String fileName) {
        return flowMonthUploadRecordService.getByFileName(fileName);
    }

    @Override
    public FlowMonthUploadRecordDTO getByRecordId(Long recordId) {
        return flowMonthUploadRecordService.getByRecordId(recordId);
    }

    @Override
    public FlowMonthUploadRecordDTO getById(Long id) {
        return PojoUtils.map(flowMonthUploadRecordService.getById(id), FlowMonthUploadRecordDTO.class);
    }

    @Override
    public String checkFlowFileNameNew(String fileName, CurrentSjmsUserInfo userInfo, boolean isFixUpload) {
        return flowMonthUploadRecordService.checkFlowFileNameNew(fileName, userInfo,isFixUpload);
    }


}
