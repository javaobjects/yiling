package com.yiling.export.imports.service;

import java.io.InputStream;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yiling.dataflow.flowcollect.api.SalesAppealConfirmApi;
import com.yiling.dataflow.flowcollect.enums.FlowMonthUploadCheckStatusEnum;
import com.yiling.dataflow.flowcollect.enums.FlowMonthUploadImportStatusEnum;
import com.yiling.export.excel.entity.ExcelTaskRecordDO;
import com.yiling.export.excel.model.ImportResultModel;
import com.yiling.export.excel.service.BaseExcelImportService;
import com.yiling.export.excel.service.ExcelTaskRecordService;
import com.yiling.export.imports.handler.ImportSalesAppealConfirmHandler;
import com.yiling.export.imports.model.ImportSalesAppealConfirmModel;
import com.yiling.export.imports.util.EasyExcelUtils;
import com.yiling.export.imports.util.ImportConstants;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.sjms.flee.api.SaleaAppealFormApi;
import com.yiling.sjms.flee.dto.SalesAppealFormDTO;
import com.yiling.sjms.flee.dto.request.UpdateSalesAppealFormRequest;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 销量申诉
 *
 * @author: shixing.sun
 * @date: 2023-03-04
 */
@Slf4j
@Service
public class ImportSalesAppealConfirmService implements BaseExcelImportService {


    @Autowired
    ExcelTaskRecordService excelTaskRecordService;
    @DubboReference
    SaleaAppealFormApi saleaAppealFormApi;
    @DubboReference(timeout = 10 * 1000)
    SalesAppealConfirmApi salesAppealConfirmApi;


    @Override
    public ImportResultModel importExcel(InputStream inputstream, Integer readRows, Map<String, Object> param) throws Exception {
        Long recordId = (Long) param.get(ImportConstants.TASK_RECORD_ID);
        Long opUserId = (Long) param.getOrDefault(MyMetaHandler.FIELD_OP_USER_ID,0);
        ExcelTaskRecordDO taskRecordDO = excelTaskRecordService.getById(recordId);

        String fileName = taskRecordDO.getFileName();
        ImportResultModel<Object> resultModel = null;
        ImportSalesAppealConfirmHandler importSalesAppealConfirmHandler=new ImportSalesAppealConfirmHandler();
        importSalesAppealConfirmHandler.setHeadRowNumber(3);
        importSalesAppealConfirmHandler.setAfterSave(true);
        importSalesAppealConfirmHandler.setOnlyExportFail(true);
        importSalesAppealConfirmHandler.setSaleaAppealFormApi(saleaAppealFormApi);
        importSalesAppealConfirmHandler.setExcelTaskRecordService(excelTaskRecordService);
        importSalesAppealConfirmHandler.setSalesAppealConfirmApi(salesAppealConfirmApi);
        importSalesAppealConfirmHandler.setParamMap(param);
        resultModel = EasyExcelUtils.importExcelMore(inputstream, ImportSalesAppealConfirmModel.class, importSalesAppealConfirmHandler);
        SalesAppealFormDTO salesAppealFormDTO=saleaAppealFormApi.getByTaskId(recordId);
        if (resultModel != null) {
            // 生成月流向记录
            if ((CollUtil.isNotEmpty(resultModel.getFailList()) || (CollUtil.isEmpty(resultModel.getFailList()) && CollUtil.isEmpty(resultModel.getList())))) {
                UpdateSalesAppealFormRequest request=new UpdateSalesAppealFormRequest();
                request.setId(salesAppealFormDTO.getId());
                request.setTargetUrl(StrUtil.isNotEmpty(resultModel.getResultUrl()) ? resultModel.getResultUrl() : taskRecordDO.getRequestUrl());
                request.setImportStatus(FlowMonthUploadImportStatusEnum.FAIL.getCode());
                request.setCheckStatus(FlowMonthUploadCheckStatusEnum.WARN.getCode());
                request.setOpUserId(opUserId);
                saleaAppealFormApi.updateSalesConfirm(request);
                log.info("销量申诉确认上传检查文件失败 文件名={} 成功数量={} 失败数量={}", fileName, resultModel.getSuccessCount(), resultModel.getFailCount());
            } else if (CollUtil.isEmpty(resultModel.getFailList()) && CollUtil.isNotEmpty(resultModel.getList())) {
                UpdateSalesAppealFormRequest appealFormRequest=new UpdateSalesAppealFormRequest();
                appealFormRequest.setId(salesAppealFormDTO.getId());
                appealFormRequest.setTargetUrl(resultModel.getResultUrl());
                appealFormRequest.setImportStatus(FlowMonthUploadImportStatusEnum.SUCCESS.getCode());
                appealFormRequest.setCheckStatus(FlowMonthUploadCheckStatusEnum.PASS.getCode());
                appealFormRequest.setOpUserId(opUserId);
                saleaAppealFormApi.updateSalesConfirm(appealFormRequest);
                log.info("销量申诉确认文件成功 文件名={} 成功数量={} 失败数量={}", fileName, resultModel.getSuccessCount(), resultModel.getFailCount());
            }
        }else{
            UpdateSalesAppealFormRequest updateSalesAppealFormRequest = new UpdateSalesAppealFormRequest();
            updateSalesAppealFormRequest.setId(salesAppealFormDTO.getId());
            updateSalesAppealFormRequest.setImportStatus(FlowMonthUploadImportStatusEnum.FAIL.getCode());
            updateSalesAppealFormRequest.setCheckStatus(FlowMonthUploadCheckStatusEnum.NOT_PASS.getCode());
            updateSalesAppealFormRequest.setFailReason("未知异常,删除以后重新上传!");
            saleaAppealFormApi.updateSalesConfirm(updateSalesAppealFormRequest);
        }
        return resultModel;
    }

}
