package com.yiling.open.cms.fine.controller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import com.alibaba.fastjson.JSON;
import com.yiling.bi.resource.api.UploadResourceApi;
import com.yiling.bi.resource.dto.UploadResourceDTO;
import com.yiling.bi.resource.dto.UploadResourceLogDTO;
import com.yiling.bi.resource.dto.request.UpdateResourceLogRequest;
import com.yiling.export.excel.api.ExcelTaskConfigApi;
import com.yiling.export.excel.api.ExcelTaskRecordApi;
import com.yiling.export.excel.dto.ExcelTaskConfigDTO;
import com.yiling.export.excel.dto.request.SaveExcelTaskRecordRequest;
import com.yiling.export.excel.enums.ExcelSourceEnum;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.oss.bo.FileInfo;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.open.cms.fine.form.UploadResourceForm;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shuan
 */
@RestController
@Slf4j
public class FineRequestController extends BaseController {

    @Autowired
    FileService        fileService;
    @DubboReference
    UploadResourceApi  uploadResourceApi;
    @DubboReference
    ExcelTaskRecordApi excelTaskRecordApi;
    @DubboReference
    ExcelTaskConfigApi excelTaskConfigApi;

    @PostMapping(path = "/fine/ask")
    public Result<String> fineAsk(@RequestBody @Valid UploadResourceForm form) {
        log.info("收到数据请求:{}", JSON.toJSONString(form));
        String returnBody = "";
        try {
            returnBody = uploadResourceApi.importInputLsflRecord(form.getUploadResourceId());
        } catch (Exception e) {
            log.error("BI接口请求报错", e);
            return Result.failed("请求报错");
        }
        return Result.success(returnBody);
    }


    @PostMapping(path = "/fine/import")
    public Result<String> fineImport(@RequestBody @Valid UploadResourceForm form) {
        log.info("收到数据请求:{}", JSON.toJSONString(form));
        UploadResourceLogDTO uploadResourceLogDTO = uploadResourceApi.getUploadResourceLogById(form.getUploadResourceId());
        if (uploadResourceLogDTO == null) {
            throw new BusinessException(ResultCode.FAILED, "uploadResourceLog数据不存在");
        }
        ExcelTaskConfigDTO excelTaskConfigDTO = excelTaskConfigApi.findExcelTaskConfigByCode(uploadResourceLogDTO.getDataSource());
        if (excelTaskConfigDTO == null) {
            throw new BusinessException(ResultCode.FAILED, "excel_config库中未配置该项目");
        }

        SaveExcelTaskRecordRequest recordRequest = new SaveExcelTaskRecordRequest();
        recordRequest.setClassName(excelTaskConfigDTO.getClassName());
        recordRequest.setVerifyHandlerBeanName(excelTaskConfigDTO.getVerifyHandlerBeanName());
        recordRequest.setImportDataHandlerBeanName(excelTaskConfigDTO.getImportDataHandlerBeanName());
        recordRequest.setModelClass(excelTaskConfigDTO.getModelClass());
        recordRequest.setEid(0L);
        recordRequest.setOpUserId(0L);
        recordRequest.setTitle(excelTaskConfigDTO.getTitle());
        recordRequest.setMenuName(excelTaskConfigDTO.getMenuName());
        recordRequest.setTaskConfigId(excelTaskConfigDTO.getId());
        recordRequest.setSource(ExcelSourceEnum.FINE.getCode());
        recordRequest.setFileName(uploadResourceLogDTO.getFileName());
        // 设置参数
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("dyear", uploadResourceLogDTO.getDyear());
        paramMap.put("status", uploadResourceLogDTO.getStatus());
        paramMap.put("createId", uploadResourceLogDTO.getCreateId());
        paramMap.put("createName", uploadResourceLogDTO.getCreateName());
        paramMap.put("createRole", uploadResourceLogDTO.getCreateRole());
        String paramStr = JSONUtil.toJsonStr(paramMap);
        recordRequest.setParam(paramStr);

        try {
            UploadResourceDTO uploadResourceDTO = uploadResourceApi.getUploadResourceByDataId(uploadResourceLogDTO.getId());
            InputStream inputStream = new ByteArrayInputStream(uploadResourceDTO.getFileStream());
            FileInfo fileInfo = fileService.upload(inputStream, uploadResourceLogDTO.getFileName(), FileTypeEnum.EXCEL_IMPORT_RESULT);
            recordRequest.setRequestUrl(fileInfo.getKey());
            Long id=excelTaskRecordApi.saveExcelTaskRecord(recordRequest);
            UpdateResourceLogRequest resourceLogRequest=new UpdateResourceLogRequest();
            resourceLogRequest.setId(uploadResourceLogDTO.getId());
            resourceLogRequest.setExcelTaskRecordId(id);
            uploadResourceApi.updateUploadResourceLog(resourceLogRequest);
        } catch (Exception e) {
            log.error("创建导入任务失败", e);
            return Result.failed("创建导入任务失败");
        }
        return Result.success();
    }


}
