package com.yiling.sjms.monthflow.controller;

import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.dataflow.flowcollect.api.FlowMonthUploadRecordApi;
import com.yiling.dataflow.flowcollect.enums.FlowMonthUploadCheckStatusEnum;
import com.yiling.dataflow.flowcollect.enums.FlowMonthUploadImportStatusEnum;
import com.yiling.export.excel.api.ExcelTaskConfigApi;
import com.yiling.export.excel.api.ExcelTaskRecordApi;
import com.yiling.export.excel.dto.ExcelTaskConfigDTO;
import com.yiling.export.excel.dto.request.SaveExcelTaskRecordRequest;
import com.yiling.export.excel.enums.ExcelSourceEnum;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.sjms.flow.api.FixMonthFlowApi;
import com.yiling.sjms.flow.dto.MonthFlowFormDTO;
import com.yiling.sjms.flow.dto.request.DeleteFormRequest;
import com.yiling.sjms.flow.dto.request.QueryMonthFlowFormRequest;
import com.yiling.sjms.flow.dto.request.SaveMonthFlowFormRequest;
import com.yiling.sjms.flow.dto.request.SubmitMonthFlowFormRequest;
import com.yiling.sjms.monthflow.form.SaveMonthFlowForm;
import com.yiling.sjms.monthflow.form.SubmitMonthFlowForm;
import com.yiling.sjms.monthflow.vo.MonthFlowFormVO;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author: gxl
 * @date: 2023/6/25
 */
@Api(tags = "补传月流向表单")
@RestController
@RequestMapping("/fixMonthFlow")
public class MonthFlowFormController {
    @DubboReference
    private EsbEmployeeApi esbEmployeeApi;

    @DubboReference
    private FixMonthFlowApi fixMonthFlowApi;

    @DubboReference
    ExcelTaskConfigApi excelTaskConfigApi;
    @DubboReference
    ExcelTaskRecordApi excelTaskRecordApi;

    @DubboReference
    FlowMonthUploadRecordApi flowMonthUploadRecordApi;

    @Autowired
    private FileService fileService;

    @ApiOperation("补传月流向上传新增")
    @PostMapping("/saveUpload")
    public Result<Long> saveUpload(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid SaveMonthFlowForm form) {
        SaveMonthFlowFormRequest request = PojoUtils.map(form, SaveMonthFlowFormRequest.class);
        request.setOpUserId(userInfo.getCurrentUserId());
        EsbEmployeeDTO esbEmployeeDTO = esbEmployeeApi.getByEmpId(userInfo.getCurrentUserCode());
        request.setEmpName(esbEmployeeDTO.getEmpName());
        request.setEmpId(esbEmployeeDTO.getEmpId());
        request.getSubForms().forEach(item->{
            String failReason = flowMonthUploadRecordApi.checkFlowFileNameNew(item.getName(),userInfo,true);
            if(null!=failReason){
                item.setCheckStatus(FlowMonthUploadCheckStatusEnum.NOT_PASS.getCode());
                item.setImportStatus(FlowMonthUploadImportStatusEnum.FAIL.getCode());
                item.setReason(failReason);
            }else{
                Boolean byFileName = fixMonthFlowApi.getByFileName(item.getName());
                if(byFileName){
                    item.setCheckStatus(FlowMonthUploadCheckStatusEnum.NOT_PASS.getCode());
                    item.setImportStatus(FlowMonthUploadImportStatusEnum.FAIL.getCode());
                    item.setReason("文件名称与当前流向清洗队列中的文件名称重复");
                }else{
                    item.setCheckStatus(FlowMonthUploadCheckStatusEnum.PASS.getCode());
                    item.setImportStatus(FlowMonthUploadImportStatusEnum.IN_PROGRESS.getCode());
                    Long taskId = this.createImportTask(userInfo.getCurrentUserId(),item.getName(),item.getKey());
                    item.setTaskId(taskId);
                }

            }

        });
        Long formId = fixMonthFlowApi.save(request);
        return Result.success(formId);
    }

    @ApiOperation("提交审核")
    @PostMapping("/submit")
    public Result<Boolean> submit(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid SubmitMonthFlowForm form) {
        EsbEmployeeDTO esbEmployeeDTO = esbEmployeeApi.getByEmpId(userInfo.getCurrentUserCode());
        // 提交审核
        SubmitMonthFlowFormRequest submitRequest = PojoUtils.map(form, SubmitMonthFlowFormRequest.class);
        submitRequest.setOpUserId(userInfo.getCurrentUserId());
        submitRequest.setEmpId(userInfo.getCurrentUserCode());
        submitRequest.setEmpName(esbEmployeeDTO.getEmpName());
        fixMonthFlowApi.submit(submitRequest);
        return Result.success(true);
    }


    /**
     * 创建导入任务
     * @param opUserId
     * @param fileName
     * @param fileKey
     * @return
     */
    private Long createImportTask(Long opUserId,String fileName,String fileKey){
        ExcelTaskConfigDTO excelTaskConfigDTO = excelTaskConfigApi.findExcelTaskConfigByCode("fixMonthFlow");
        if (excelTaskConfigDTO == null) {
            throw new BusinessException(ResultCode.FAILED,"没有配置模板信息");
        }

        SaveExcelTaskRecordRequest recordRequest = new SaveExcelTaskRecordRequest();
        recordRequest.setClassName(excelTaskConfigDTO.getClassName());
        recordRequest.setVerifyHandlerBeanName(excelTaskConfigDTO.getVerifyHandlerBeanName());
        recordRequest.setImportDataHandlerBeanName(excelTaskConfigDTO.getImportDataHandlerBeanName());
        recordRequest.setModelClass(excelTaskConfigDTO.getModelClass());
        recordRequest.setEid(0L);
        recordRequest.setOpUserId(opUserId);
        recordRequest.setTitle(excelTaskConfigDTO.getTitle());
        recordRequest.setMenuName(excelTaskConfigDTO.getMenuName());
        recordRequest.setTaskConfigId(excelTaskConfigDTO.getId());
        recordRequest.setSource(ExcelSourceEnum.DIH.getCode());
        recordRequest.setFileName(fileName);
        recordRequest.setRequestUrl(fileKey);
        Long recordId = excelTaskRecordApi.saveExcelTaskRecord(recordRequest);
        return recordId;
    }

    @ApiOperation("流程申请-补传月流向列表")
    @GetMapping("/list")
    public Result<CollectionObject<MonthFlowFormVO>> pageList(@CurrentUser CurrentSjmsUserInfo userInfo, Long formId) {
        QueryMonthFlowFormRequest request = new QueryMonthFlowFormRequest();
        request.setFormId(formId);
        List<MonthFlowFormDTO> monthFlowFormDTOS = fixMonthFlowApi.list(request);
        List<MonthFlowFormVO> monthFlowFormVOS = PojoUtils.map(monthFlowFormDTOS, MonthFlowFormVO.class);
        for (MonthFlowFormVO item : monthFlowFormVOS) {
            String uploader = item.getUploader();
            if (StringUtils.isNotEmpty(uploader)) {
                EsbEmployeeDTO esbEmployeeDTO = esbEmployeeApi.getByEmpId(uploader);
                item.setUploaderName(null != esbEmployeeDTO ? esbEmployeeDTO.getEmpName() : "");
            }
            if (StringUtils.isNotEmpty(item.getTargetUrl())) {
                String resultUrl = fileService.getUrl(item.getTargetUrl(), FileTypeEnum.EXCEL_IMPORT_RESULT);
                item.setFileUrl(resultUrl);
            } else if (StringUtils.isNotEmpty(item.getSourceUrl())) {
                String fileUrl = fileService.getUrl(item.getSourceUrl(),  FileTypeEnum.FIX_FLOW_MONTH_UPLOAD_FILE);
                item.setFileUrl(fileUrl);
            }
            item.setDownLoadName(item.getExcelName());
        }
        CollectionObject<MonthFlowFormVO> collectionObject = new CollectionObject<>(monthFlowFormVOS);
        return Result.success(collectionObject);
    }
    @ApiOperation("我的已办/待办-补传月流向列表")
    @GetMapping("/queryList")
    public Result<CollectionObject<MonthFlowFormVO>> queryList(@CurrentUser CurrentSjmsUserInfo userInfo,@RequestParam(required = true) Long formId) {
        QueryMonthFlowFormRequest request = new QueryMonthFlowFormRequest();
        request.setCheckStatus(1);
        request.setFormId(formId);
        List<MonthFlowFormDTO> monthFlowFormDTOS = fixMonthFlowApi.list(request);
        List<MonthFlowFormVO> monthFlowFormVOS = PojoUtils.map(monthFlowFormDTOS, MonthFlowFormVO.class);
        for (MonthFlowFormVO item : monthFlowFormVOS) {
            String uploader = item.getUploader();
            if (StringUtils.isNotEmpty(uploader)) {
                EsbEmployeeDTO esbEmployeeDTO = esbEmployeeApi.getByEmpId(uploader);
                item.setUploaderName(null != esbEmployeeDTO ? esbEmployeeDTO.getEmpName() : "");
            }
            if (StringUtils.isNotEmpty(item.getSourceUrl())) {
                String fileUrl = fileService.getUrl(item.getSourceUrl(),  FileTypeEnum.FIX_FLOW_MONTH_UPLOAD_FILE);
                item.setFileUrl(fileUrl);
                item.setDownLoadName(item.getExcelName());
            }
        }
        CollectionObject<MonthFlowFormVO> collectionObject = new CollectionObject<>(monthFlowFormVOS);
        return Result.success(collectionObject);
    }

    @ApiOperation(value = "删除单个单据")
    @GetMapping(value = "deleteOne")
    public Result<Boolean> delete(@CurrentUser CurrentSjmsUserInfo userInfo,@RequestParam Long id){
        DeleteFormRequest request = new DeleteFormRequest();
        request.setId(id).setOpUserId(userInfo.getCurrentUserId());
        fixMonthFlowApi.deleteById(request);
        return Result.success(true);
    }
}