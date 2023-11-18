package com.yiling.export.imports.service;

import java.io.InputStream;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yiling.dataflow.flowcollect.api.FlowFleeingGoodsApi;
import com.yiling.dataflow.flowcollect.enums.FlowMonthUploadCheckStatusEnum;
import com.yiling.dataflow.flowcollect.enums.FlowMonthUploadImportStatusEnum;
import com.yiling.export.excel.entity.ExcelTaskRecordDO;
import com.yiling.export.excel.model.ImportResultModel;
import com.yiling.export.excel.service.BaseExcelImportService;
import com.yiling.export.excel.service.ExcelTaskRecordService;
import com.yiling.export.imports.handler.ImportConfirmFleeingGoodsHandler;
import com.yiling.export.imports.model.ImportConfirmFleeingGoodsModel;
import com.yiling.export.imports.util.EasyExcelUtils;
import com.yiling.export.imports.util.ImportConstants;
import com.yiling.sjms.flee.api.FleeingGoodsFormApi;
import com.yiling.sjms.flee.dto.FleeingGoodsFormDTO;
import com.yiling.sjms.flee.dto.request.UpdateFleeingGoodsFormRequest;

import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 窜货申报确认上传
 *
 * @author: yong.zhang
 * @date: 2023/3/15 0015
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ImportConfirmFleeingGoodsService implements BaseExcelImportService {

    @DubboReference
    FleeingGoodsFormApi fleeingGoodsFormApi;

    @DubboReference(timeout = 10 * 1000)
    FlowFleeingGoodsApi flowFleeingGoodsApi;

    private final ExcelTaskRecordService excelTaskRecordService;

    @Override
    public ImportResultModel importExcel(InputStream inputstream, Integer readRows, Map<String, Object> param) throws Exception {
        Long recordId = (Long) param.get(ImportConstants.TASK_RECORD_ID);
        log.info("ImportConfirmFleeingGoodsService importExcel start recordId:[{}]", recordId);
        ExcelTaskRecordDO taskRecordDO = excelTaskRecordService.getById(recordId);
        String fileName = taskRecordDO.getFileName();

        ImportConfirmFleeingGoodsHandler importConfirmFleeingGoodsHandler = new ImportConfirmFleeingGoodsHandler();
        importConfirmFleeingGoodsHandler.setHeadRowNumber(3);
        importConfirmFleeingGoodsHandler.setAfterSave(true);
        importConfirmFleeingGoodsHandler.setOnlyExportFail(true);
        importConfirmFleeingGoodsHandler.setParamMap(param);
        importConfirmFleeingGoodsHandler.setFleeingGoodsFormApi(fleeingGoodsFormApi);
        importConfirmFleeingGoodsHandler.setFlowFleeingGoodsApi(flowFleeingGoodsApi);
        importConfirmFleeingGoodsHandler.setExcelTaskRecordService(excelTaskRecordService);

        ImportResultModel<Object> resultModel = EasyExcelUtils.importExcelMore(inputstream, ImportConfirmFleeingGoodsModel.class, importConfirmFleeingGoodsHandler);
        FleeingGoodsFormDTO fleeingGoodsFormDTO = fleeingGoodsFormApi.getByTaskId(recordId);
        if (resultModel != null) {
            if ((CollUtil.isNotEmpty(resultModel.getFailList()) || (CollUtil.isEmpty(resultModel.getFailList()) && CollUtil.isEmpty(resultModel.getList())))) {
                UpdateFleeingGoodsFormRequest request = new UpdateFleeingGoodsFormRequest();
                request.setId(fleeingGoodsFormDTO.getId());
                request.setFileUrl(taskRecordDO.getRequestUrl());
                request.setResultUrl(resultModel.getResultUrl());
                request.setImportStatus(FlowMonthUploadImportStatusEnum.FAIL.getCode());
                request.setCheckStatus(FlowMonthUploadCheckStatusEnum.WARN.getCode());
                fleeingGoodsFormApi.updateUploadRecord(request);
                log.info("检查文件失败 文件名={} 成功数量={} 失败数量={}", fileName, resultModel.getSuccessCount(), resultModel.getFailCount());
            } else if (CollUtil.isNotEmpty(resultModel.getList()) && CollUtil.isEmpty(resultModel.getFailList())) {
                UpdateFleeingGoodsFormRequest updateFleeingGoodsFormRequest = new UpdateFleeingGoodsFormRequest();
                updateFleeingGoodsFormRequest.setId(fleeingGoodsFormDTO.getId());
                updateFleeingGoodsFormRequest.setImportStatus(FlowMonthUploadImportStatusEnum.SUCCESS.getCode());
                updateFleeingGoodsFormRequest.setCheckStatus(FlowMonthUploadCheckStatusEnum.PASS.getCode());
                updateFleeingGoodsFormRequest.setFileUrl(taskRecordDO.getRequestUrl());
                updateFleeingGoodsFormRequest.setResultUrl(resultModel.getResultUrl());
                fleeingGoodsFormApi.updateUploadRecord(updateFleeingGoodsFormRequest);
                log.info("检查并保存文件成功 文件名={} 成功数量={}", fileName, resultModel.getSuccessCount());
            }
        } else {
            UpdateFleeingGoodsFormRequest updateFleeingGoodsFormRequest = new UpdateFleeingGoodsFormRequest();
            updateFleeingGoodsFormRequest.setId(fleeingGoodsFormDTO.getId());
            updateFleeingGoodsFormRequest.setImportStatus(FlowMonthUploadImportStatusEnum.FAIL.getCode());
            updateFleeingGoodsFormRequest.setCheckStatus(FlowMonthUploadCheckStatusEnum.NOT_PASS.getCode());
            updateFleeingGoodsFormRequest.setFileUrl(taskRecordDO.getRequestUrl());
            updateFleeingGoodsFormRequest.setResultUrl(resultModel.getResultUrl());
            updateFleeingGoodsFormRequest.setFailReason("未知异常,删除以后重新上传!");
            fleeingGoodsFormApi.updateUploadRecord(updateFleeingGoodsFormRequest);
        }
        log.info("ImportConfirmFleeingGoodsService importExcel end");
        return resultModel;
    }
}
