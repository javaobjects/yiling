package com.yiling.export.imports.service;

import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yiling.dataflow.flowcollect.api.FlowMonthInventoryApi;
import com.yiling.dataflow.flowcollect.api.FlowMonthPurchaseApi;
import com.yiling.dataflow.flowcollect.api.FlowMonthSalesApi;
import com.yiling.dataflow.flowcollect.api.FlowMonthUploadRecordApi;
import com.yiling.dataflow.flowcollect.dto.FlowMonthUploadRecordDTO;
import com.yiling.dataflow.flowcollect.dto.request.UpdateFlowMonthUploadRecordRequest;
import com.yiling.dataflow.flowcollect.enums.FlowMonthUploadCheckStatusEnum;
import com.yiling.dataflow.flowcollect.enums.FlowMonthUploadDataTypeEnum;
import com.yiling.dataflow.flowcollect.enums.FlowMonthUploadImportStatusEnum;
import com.yiling.export.excel.entity.ExcelTaskRecordDO;
import com.yiling.export.excel.model.ImportResultModel;
import com.yiling.export.excel.service.BaseExcelImportService;
import com.yiling.export.excel.service.ExcelTaskRecordService;
import com.yiling.export.imports.handler.ImportFlowMonthInventoryHandler;
import com.yiling.export.imports.handler.ImportFlowMonthPurchaseHandler;
import com.yiling.export.imports.handler.ImportFlowMonthSalesHandler;
import com.yiling.export.imports.model.ImportFlowMonthInventoryModel;
import com.yiling.export.imports.model.ImportFlowMonthPurchaseModel;
import com.yiling.export.imports.model.ImportFlowMonthSalesModel;
import com.yiling.export.imports.util.EasyExcelUtils;
import com.yiling.export.imports.util.ImportConstants;
import com.yiling.framework.common.mybatis.MyMetaHandler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 导入月流向
 *
 * @author: lun.yu
 * @date: 2023-03-04
 */
@Slf4j
@Service
public class ImportFlowMonthService implements BaseExcelImportService {

    @Autowired
    ExcelTaskRecordService          excelTaskRecordService;
    @DubboReference
    FlowMonthUploadRecordApi        flowMonthUploadRecordApi;
    @DubboReference(timeout = 5 * 60 * 1000)
    FlowMonthSalesApi               flowMonthSalesApi;
    @DubboReference(timeout = 5 * 60 * 1000)
    FlowMonthPurchaseApi  flowMonthPurchaseApi;
    @DubboReference(timeout = 5 * 60 * 1000)
    FlowMonthInventoryApi flowMonthInventoryApi;



    @Override
    public ImportResultModel importExcel(InputStream inputstream, Integer readRows, Map<String, Object> param) throws Exception {
        Long recordId = (Long) param.get(ImportConstants.TASK_RECORD_ID);
        Long opUserId = (Long) param.get(MyMetaHandler.FIELD_OP_USER_ID);
        ExcelTaskRecordDO taskRecordDO = excelTaskRecordService.getById(recordId);

        String fileName = taskRecordDO.getFileName();
        Integer dataType = flowMonthUploadRecordApi.getDataType(fileName);
        FlowMonthUploadRecordDTO uploadRecordDTO = Optional.ofNullable(flowMonthUploadRecordApi.getByRecordId(recordId)).orElse(new FlowMonthUploadRecordDTO());
        ImportResultModel<Object> resultModel = null;

        if (FlowMonthUploadDataTypeEnum.getByCode(dataType) == FlowMonthUploadDataTypeEnum.SALES) {
            ImportFlowMonthSalesHandler importFlowMonthSalesHandler = new ImportFlowMonthSalesHandler();
            //屏蔽指定行数的Excel解析
//            importFlowMonthSalesHandler.setHeadRowNumber(3);
            importFlowMonthSalesHandler.setAfterSave(true);
            importFlowMonthSalesHandler.setOnlyExportFail(true);
            importFlowMonthSalesHandler.setParamMap(param);
            importFlowMonthSalesHandler.setFlowMonthSalesApi(flowMonthSalesApi);
            importFlowMonthSalesHandler.setFlowMonthUploadRecordApi(flowMonthUploadRecordApi);
            importFlowMonthSalesHandler.setExcelTaskRecordService(excelTaskRecordService);
            resultModel = EasyExcelUtils.importExcelMore(inputstream, ImportFlowMonthSalesModel.class, importFlowMonthSalesHandler);

        }
        else if (FlowMonthUploadDataTypeEnum.getByCode(dataType) == FlowMonthUploadDataTypeEnum.INVENTORY) {
            ImportFlowMonthInventoryHandler importFlowMonthInventoryHandler=new ImportFlowMonthInventoryHandler();
            importFlowMonthInventoryHandler.setAfterSave(true);
            importFlowMonthInventoryHandler.setOnlyExportFail(true);
            importFlowMonthInventoryHandler.setParamMap(param);
            importFlowMonthInventoryHandler.setFlowMonthInventoryApi(flowMonthInventoryApi);
            importFlowMonthInventoryHandler.setFlowMonthUploadRecordApi(flowMonthUploadRecordApi);
            importFlowMonthInventoryHandler.setExcelTaskRecordService(excelTaskRecordService);
            resultModel = EasyExcelUtils.importExcelMore(inputstream, ImportFlowMonthInventoryModel.class, importFlowMonthInventoryHandler);

        } else {
            ImportFlowMonthPurchaseHandler importFlowMonthPurchaseHandler=new ImportFlowMonthPurchaseHandler();
            importFlowMonthPurchaseHandler.setAfterSave(true);
            importFlowMonthPurchaseHandler.setOnlyExportFail(true);
            importFlowMonthPurchaseHandler.setParamMap(param);
            importFlowMonthPurchaseHandler.setFlowMonthPurchaseApi(flowMonthPurchaseApi);
            importFlowMonthPurchaseHandler.setFlowMonthUploadRecordApi(flowMonthUploadRecordApi);
            importFlowMonthPurchaseHandler.setExcelTaskRecordService(excelTaskRecordService);
            resultModel = EasyExcelUtils.importExcelMore(inputstream, ImportFlowMonthPurchaseModel.class, importFlowMonthPurchaseHandler);
        }

        if (resultModel != null) {
            // 生成月流向记录
            if ((CollUtil.isNotEmpty(resultModel.getFailList()) || (CollUtil.isEmpty(resultModel.getFailList()) && CollUtil.isEmpty(resultModel.getList())))) {
                UpdateFlowMonthUploadRecordRequest request = new UpdateFlowMonthUploadRecordRequest();
                request.setId(uploadRecordDTO.getId());
                request.setFileUrl(StrUtil.isNotEmpty(resultModel.getResultUrl()) ? resultModel.getResultUrl() : taskRecordDO.getRequestUrl());
                request.setImportStatus(FlowMonthUploadImportStatusEnum.FAIL.getCode());
                request.setCheckStatus(FlowMonthUploadCheckStatusEnum.WARN.getCode());
                request.setOpUserId(opUserId);
                flowMonthUploadRecordApi.updateFlowMonthRecord(request);
                log.info("月流向上传检查文件失败 文件名={} 成功数量={} 失败数量={}", fileName, resultModel.getSuccessCount(), resultModel.getFailCount());

            } else if (CollUtil.isNotEmpty(resultModel.getList()) && CollUtil.isEmpty(resultModel.getFailList())) {
                UpdateFlowMonthUploadRecordRequest request = new UpdateFlowMonthUploadRecordRequest();
                request.setId(uploadRecordDTO.getId());
                request.setFileUrl(resultModel.getResultUrl());
                request.setImportStatus(FlowMonthUploadImportStatusEnum.SUCCESS.getCode());
                request.setCheckStatus(FlowMonthUploadCheckStatusEnum.PASS.getCode());
                request.setOpUserId(opUserId);
                flowMonthUploadRecordApi.updateFlowMonthRecord(request);
                log.info("月流向上传检查并保存文件成功 文件名={} 成功数量={}", fileName, resultModel.getSuccessCount());
            }
        }else{
            UpdateFlowMonthUploadRecordRequest updateFlowMonthUploadRecordRequest = new UpdateFlowMonthUploadRecordRequest();
            updateFlowMonthUploadRecordRequest.setId(uploadRecordDTO.getId());
            updateFlowMonthUploadRecordRequest.setFileUrl(resultModel.getResultUrl());
            updateFlowMonthUploadRecordRequest.setImportStatus(FlowMonthUploadImportStatusEnum.FAIL.getCode());
            updateFlowMonthUploadRecordRequest.setCheckStatus(FlowMonthUploadCheckStatusEnum.NOT_PASS.getCode());
            updateFlowMonthUploadRecordRequest.setOpUserId(opUserId);
            updateFlowMonthUploadRecordRequest.setFailReason("未知异常,删除以后重新上传!");
            flowMonthUploadRecordApi.updateFlowMonthRecord(updateFlowMonthUploadRecordRequest);
        }

        return resultModel;
    }

}
