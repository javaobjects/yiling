package com.yiling.export.imports.service;

import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yiling.dataflow.flowcollect.api.FlowMonthUploadRecordApi;
import com.yiling.dataflow.flowcollect.enums.FlowMonthUploadCheckStatusEnum;
import com.yiling.dataflow.flowcollect.enums.FlowMonthUploadDataTypeEnum;
import com.yiling.dataflow.flowcollect.enums.FlowMonthUploadImportStatusEnum;
import com.yiling.export.excel.entity.ExcelTaskRecordDO;
import com.yiling.export.excel.model.ImportResultModel;
import com.yiling.export.excel.service.BaseExcelImportService;
import com.yiling.export.excel.service.ExcelTaskRecordService;
import com.yiling.export.imports.handler.ImportFixFlowMonthInventoryHandler;
import com.yiling.export.imports.handler.ImportFixFlowMonthPurchaseHandler;
import com.yiling.export.imports.handler.ImportFixFlowMonthSalesHandler;
import com.yiling.export.imports.model.ImportFlowMonthInventoryModel;
import com.yiling.export.imports.model.ImportFlowMonthPurchaseModel;
import com.yiling.export.imports.model.ImportFlowMonthSalesModel;
import com.yiling.export.imports.util.EasyExcelUtils;
import com.yiling.export.imports.util.ImportConstants;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.sjms.flow.api.FixMonthFlowApi;
import com.yiling.sjms.flow.dto.MonthFlowFormDTO;
import com.yiling.sjms.flow.dto.request.SaveSubFormRequest;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 补传月流向
 *
 * @author: gxl
 * @date: 2023-03-04
 */
@Slf4j
@Service
public class ImportFixMonthFlowService implements BaseExcelImportService {


    @Autowired
    ExcelTaskRecordService excelTaskRecordService;
    @DubboReference
    FixMonthFlowApi fixMonthFlowApi;

    @DubboReference
    FlowMonthUploadRecordApi flowMonthUploadRecordApi;
    @Override
    public ImportResultModel importExcel(InputStream inputstream, Integer readRows, Map<String, Object> param) throws Exception {
        Long recordId = (Long) param.get(ImportConstants.TASK_RECORD_ID);
        Long opUserId = (Long) param.get(MyMetaHandler.FIELD_OP_USER_ID);
        ExcelTaskRecordDO taskRecordDO = excelTaskRecordService.getById(recordId);

        String fileName = taskRecordDO.getFileName();
        Integer dataType = flowMonthUploadRecordApi.getDataType(fileName);
        ImportResultModel<Object> resultModel = null;

        if (FlowMonthUploadDataTypeEnum.getByCode(dataType) == FlowMonthUploadDataTypeEnum.SALES) {
            ImportFixFlowMonthSalesHandler importFlowMonthSalesHandler = new ImportFixFlowMonthSalesHandler();
            importFlowMonthSalesHandler.setAfterSave(true);
            importFlowMonthSalesHandler.setOnlyExportFail(true);
            importFlowMonthSalesHandler.setParamMap(param);
            resultModel = EasyExcelUtils.importExcelMore(inputstream, ImportFlowMonthSalesModel.class, importFlowMonthSalesHandler);

        }
        else if (FlowMonthUploadDataTypeEnum.getByCode(dataType) == FlowMonthUploadDataTypeEnum.INVENTORY) {
            ImportFixFlowMonthInventoryHandler importFlowMonthInventoryHandler=new ImportFixFlowMonthInventoryHandler();
            importFlowMonthInventoryHandler.setAfterSave(true);
            importFlowMonthInventoryHandler.setOnlyExportFail(true);
            importFlowMonthInventoryHandler.setParamMap(param);
            resultModel = EasyExcelUtils.importExcelMore(inputstream, ImportFlowMonthInventoryModel.class, importFlowMonthInventoryHandler);

        } else {
            ImportFixFlowMonthPurchaseHandler importFlowMonthPurchaseHandler=new ImportFixFlowMonthPurchaseHandler();
            importFlowMonthPurchaseHandler.setAfterSave(true);
            importFlowMonthPurchaseHandler.setOnlyExportFail(true);
            importFlowMonthPurchaseHandler.setParamMap(param);
            resultModel = EasyExcelUtils.importExcelMore(inputstream, ImportFlowMonthPurchaseModel.class, importFlowMonthPurchaseHandler);
        }
        MonthFlowFormDTO uploadRecordDTO = Optional.ofNullable(fixMonthFlowApi.getByRecordId(recordId)).orElse(new MonthFlowFormDTO());
        if(Objects.isNull(uploadRecordDTO.getId()) || 0==uploadRecordDTO.getId()){
            //等待month_flow_form表插入数据
            Thread.sleep(2000L);
        }
        if (resultModel != null) {
            // 生成月流向记录
            if ((CollUtil.isNotEmpty(resultModel.getFailList()) || (CollUtil.isEmpty(resultModel.getFailList()) && CollUtil.isEmpty(resultModel.getList())))) {
                SaveSubFormRequest request = new SaveSubFormRequest();
                request.setId(uploadRecordDTO.getId());
                request.setTargetUrl(StrUtil.isNotEmpty(resultModel.getResultUrl()) ? resultModel.getResultUrl() : taskRecordDO.getRequestUrl());
                request.setImportStatus(FlowMonthUploadImportStatusEnum.FAIL.getCode());
                request.setCheckStatus(FlowMonthUploadCheckStatusEnum.WARN.getCode());
                request.setOpUserId(opUserId);
                fixMonthFlowApi.updateFlowMonthRecord(request);
                log.info("补传月流向上传检查文件失败 文件名={} 成功数量={} 失败数量={}", fileName, resultModel.getSuccessCount(), resultModel.getFailCount());

            } else if (CollUtil.isNotEmpty(resultModel.getList()) && CollUtil.isEmpty(resultModel.getFailList())) {
                SaveSubFormRequest request = new SaveSubFormRequest();
                request.setId(uploadRecordDTO.getId());
                request.setTargetUrl(resultModel.getResultUrl());
                request.setImportStatus(FlowMonthUploadImportStatusEnum.SUCCESS.getCode());
                request.setCheckStatus(FlowMonthUploadCheckStatusEnum.PASS.getCode());
                request.setOpUserId(opUserId);
                fixMonthFlowApi.updateFlowMonthRecord(request);
                log.info("补传月流向上传检查并保存文件成功 文件名={} 成功数量={}", fileName, resultModel.getSuccessCount());
            }
        }else{
            SaveSubFormRequest updateFlowMonthUploadRecordRequest = new SaveSubFormRequest();
            updateFlowMonthUploadRecordRequest.setId(uploadRecordDTO.getId());
            updateFlowMonthUploadRecordRequest.setTargetUrl(resultModel.getResultUrl());
            updateFlowMonthUploadRecordRequest.setImportStatus(FlowMonthUploadImportStatusEnum.FAIL.getCode());
            updateFlowMonthUploadRecordRequest.setCheckStatus(FlowMonthUploadCheckStatusEnum.NOT_PASS.getCode());
            updateFlowMonthUploadRecordRequest.setOpUserId(opUserId);
            updateFlowMonthUploadRecordRequest.setReason("未知异常,删除以后重新上传!");
            fixMonthFlowApi.updateFlowMonthRecord(updateFlowMonthUploadRecordRequest);
        }

        return resultModel;
    }



}
