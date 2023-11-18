package com.yiling.sjms.flee.controller;


import java.util.ArrayList;
import java.util.Date;
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

import com.alibaba.fastjson.JSON;
import com.yiling.dataflow.flowcollect.enums.FlowMonthUploadCheckStatusEnum;
import com.yiling.dataflow.flowcollect.enums.FlowMonthUploadImportStatusEnum;
import com.yiling.dataflow.wash.api.FlowMonthWashControlApi;
import com.yiling.dataflow.wash.dto.FlowMonthWashControlDTO;
import com.yiling.export.excel.api.ExcelTaskConfigApi;
import com.yiling.export.excel.api.ExcelTaskRecordApi;
import com.yiling.export.excel.dto.ExcelTaskConfigDTO;
import com.yiling.export.excel.dto.request.SaveExcelTaskRecordRequest;
import com.yiling.export.excel.enums.ExcelSourceEnum;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.sjms.flee.api.FleeingGoodsFormApi;
import com.yiling.sjms.flee.dto.FleeingGoodsFormDTO;
import com.yiling.sjms.flee.dto.FleeingGoodsFormExtDTO;
import com.yiling.sjms.flee.dto.request.CreateFleeFlowRequest;
import com.yiling.sjms.flee.dto.request.QueryFleeingGoodsFormListRequest;
import com.yiling.sjms.flee.dto.request.RemoveFleeingGoodsFormRequest;
import com.yiling.sjms.flee.dto.request.SaveBatchFleeingGoodsFormRequest;
import com.yiling.sjms.flee.dto.request.SaveFleeingFormDraftRequest;
import com.yiling.sjms.flee.dto.request.SaveFleeingGoodsFormRequest;
import com.yiling.sjms.flee.dto.request.SubmitFleeingGoodsFormRequest;
import com.yiling.sjms.flee.form.CreateFleeFlowForm;
import com.yiling.sjms.flee.form.QueryFleeingGoodsFormListForm;
import com.yiling.sjms.flee.form.SaveConfirmFleeingGoodsForm;
import com.yiling.sjms.flee.form.SaveFleeingGoodsFormForm;
import com.yiling.sjms.flee.form.SubmitFleeingGoodsFormForm;
import com.yiling.sjms.flee.vo.FleeingGoodsFormVO;
import com.yiling.sjms.flowcollect.vo.FlowMonthUploadFlagVO;
import com.yiling.sjms.form.api.FormApi;
import com.yiling.sjms.form.enums.FormTypeEnum;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;

import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 窜货申报表单 前端控制器
 * </p>
 *
 * @author yong.zhang
 * @date 2023-03-10
 */
@Slf4j
@Api(tags = "窜货申报表单数据")
@RestController
@RequestMapping("/flee/goods")
public class FleeingGoodsFormController extends BaseController {

    @DubboReference
    FormApi formApi;

    @DubboReference
    FleeingGoodsFormApi fleeingGoodsFormApi;

    @DubboReference
    UserApi userApi;

    @DubboReference
    private EsbEmployeeApi esbEmployeeApi;

    @DubboReference
    ExcelTaskConfigApi excelTaskConfigApi;

    @DubboReference
    ExcelTaskRecordApi excelTaskRecordApi;

    @Autowired
    FileService fileService;

    @DubboReference
    FlowMonthWashControlApi flowMonthWashControlApi;

    @ApiOperation("分页查询")
    @PostMapping("/pageList")
    public Result<List<FleeingGoodsFormVO>> pageList(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody QueryFleeingGoodsFormListForm form) {
        QueryFleeingGoodsFormListRequest request = PojoUtils.map(form, QueryFleeingGoodsFormListRequest.class);
        if (Objects.isNull(request.getFormId())) {
            return Result.success(new ArrayList<>());
        }
        List<FleeingGoodsFormDTO> dtoList = fleeingGoodsFormApi.pageList(request);
        List<FleeingGoodsFormVO> voList = PojoUtils.map(dtoList, FleeingGoodsFormVO.class);

        Integer importFileType = request.getImportFileType();
        FleeingGoodsFormExtDTO fleeingGoodsFormExtDTO = fleeingGoodsFormApi.queryExtByFormId(form.getFormId());
        for (FleeingGoodsFormVO item : voList) {
            String importUser = item.getImportUser();
            if (StringUtils.isNotEmpty(importUser)) {
                EsbEmployeeDTO esbEmployeeDTO = esbEmployeeApi.getByEmpId(importUser);
                item.setImportUserName(null != esbEmployeeDTO ? esbEmployeeDTO.getEmpName() : "");
            }

            if (Objects.nonNull(fleeingGoodsFormExtDTO)) {
                item.setReportType(fleeingGoodsFormExtDTO.getReportType());
            }

            FileTypeEnum fileTypeEnum = null;
            if (ObjectUtil.equal(1, importFileType)) {
                fileTypeEnum = FileTypeEnum.FLEEING_GOODS_UPLOAD_FILE;
            }
            if (ObjectUtil.equal(2, importFileType)) {
                fileTypeEnum = FileTypeEnum.FLEEING_GOODS_CONFIRM_UPLOAD_FILE;
            }
            String prefix = item.getFileName().split("\\.")[0];
            String suffix = item.getFileUrl().split("\\.")[1];
            if (StringUtils.isNotEmpty(item.getResultUrl())) {
                suffix = item.getResultUrl().split("\\.")[1];
            }
            item.setDownLoadName(prefix + "." + suffix);
            if (StringUtils.isNotEmpty(item.getResultUrl())) {
                String resultUrl = fileService.getUrl(item.getResultUrl(), FileTypeEnum.EXCEL_IMPORT_RESULT);
                item.setFileUrl(resultUrl);
            } else if (StringUtils.isNotEmpty(item.getFileUrl())) {
                String fileUrl = fileService.getUrl(item.getFileUrl(), fileTypeEnum);
                item.setFileUrl(fileUrl);
            }
        }
        return Result.success(voList);
    }

    @ApiOperation(value = "上传按钮是否可使用")
    @GetMapping("/getUploadFlag")
    public Result<FlowMonthUploadFlagVO> getUploadFlag(@CurrentUser CurrentSjmsUserInfo userInfo) {
        FlowMonthWashControlDTO flowMonthWashControlDTO = flowMonthWashControlApi.getWashStatus();
        FlowMonthUploadFlagVO uploadFlagVO = PojoUtils.map(flowMonthWashControlDTO, FlowMonthUploadFlagVO.class);
        if (Objects.nonNull(flowMonthWashControlDTO)) {
            uploadFlagVO.setFlag(true);
            uploadFlagVO.setGoodsMappingStartTime(flowMonthWashControlDTO.getFlowCrossStartTime());
            uploadFlagVO.setGoodsMappingEndTime(flowMonthWashControlDTO.getFlowCrossEndTime());
        }
        return Result.success(uploadFlagVO);
    }

    @ApiOperation(value = "窜货申报批量导入")
    @PostMapping("/saveBatchUpload")
    public Result<Long> saveBatchUpload(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid SaveFleeingGoodsFormForm form) {
        SaveBatchFleeingGoodsFormRequest request = PojoUtils.map(form, SaveBatchFleeingGoodsFormRequest.class);
        request.setOpUserId(userInfo.getCurrentUserId());
        EsbEmployeeDTO esbEmployeeDTO = esbEmployeeApi.getByEmpId(userInfo.getCurrentUserCode());
        request.setEmpName(esbEmployeeDTO.getEmpName());
        request.setEmpId(esbEmployeeDTO.getEmpId());
        request.setImportFileType(1);
        Long formId = fleeingGoodsFormApi.saveBatchUpload(request);
        return Result.success(formId);
    }

    @ApiOperation("提交审核")
    @PostMapping("/submit")
    public Result<Boolean> submit(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody SubmitFleeingGoodsFormForm form) {
        {
            // 保存草稿
            SaveFleeingFormDraftRequest request = PojoUtils.map(form, SaveFleeingFormDraftRequest.class);
            request.setOpUserId(userInfo.getCurrentUserId());
            request.setAppendix(JSON.toJSONString(request.getAppendixList()));
            EsbEmployeeDTO esbEmployeeDTO = esbEmployeeApi.getByEmpId(userInfo.getCurrentUserCode());
            request.setEmpName(esbEmployeeDTO.getEmpName());
            request.setImportFileType(1);
            Long formId = fleeingGoodsFormApi.saveDraft(request);
            if (Objects.isNull(form.getFormId()) || 0L == form.getFormId()) {
                form.setFormId(formId);
            }
        }
        {
            // 提交审核
            SubmitFleeingGoodsFormRequest submitRequest = PojoUtils.map(form, SubmitFleeingGoodsFormRequest.class);
            submitRequest.setOpUserId(userInfo.getCurrentUserId());
            submitRequest.setFormTypeEnum(FormTypeEnum.GOODS_FLEEING);
            submitRequest.setEmpId(userInfo.getCurrentUserCode());
            fleeingGoodsFormApi.submit(submitRequest);
        }
        return Result.success(true);
    }

    @ApiOperation("删除")
    @GetMapping("/remove")
    public Result<Boolean> remove(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestParam(value = "id") Long id) {
        RemoveFleeingGoodsFormRequest request = new RemoveFleeingGoodsFormRequest();
        request.setOpUserId(userInfo.getCurrentUserId());
        request.setId(id);
        boolean isSuccess = fleeingGoodsFormApi.removeById(request);
        if (isSuccess) {
            return Result.success(true);
        }
        return Result.failed("删除失败");
    }

    @ApiOperation(value = "窜货确认导入", httpMethod = "POST")
    @PostMapping(path = "/SaveConfirmFleeingGoods")
    public Result<Boolean> SaveConfirmFleeingGoods(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid SaveConfirmFleeingGoodsForm form) {
        String fileName = form.getFileName();

        String excelCode = "importConfirmFleeingGoods";
        ExcelTaskConfigDTO excelTaskConfigDTO = excelTaskConfigApi.findExcelTaskConfigByCode(excelCode);
        if (excelTaskConfigDTO == null) {
            return Result.failed("没有配置模板信息");
        }
        SaveExcelTaskRecordRequest recordRequest = new SaveExcelTaskRecordRequest();
        recordRequest.setClassName(excelTaskConfigDTO.getClassName());
        recordRequest.setVerifyHandlerBeanName(excelTaskConfigDTO.getVerifyHandlerBeanName());
        recordRequest.setImportDataHandlerBeanName(excelTaskConfigDTO.getImportDataHandlerBeanName());
        recordRequest.setModelClass(excelTaskConfigDTO.getModelClass());
        recordRequest.setEid(0L);
        recordRequest.setOpUserId(userInfo.getCurrentUserId());
        recordRequest.setTitle(excelTaskConfigDTO.getTitle());
        recordRequest.setMenuName(excelTaskConfigDTO.getMenuName());
        recordRequest.setTaskConfigId(excelTaskConfigDTO.getId());
        recordRequest.setSource(ExcelSourceEnum.DIH.getCode());
        recordRequest.setFileName(fileName);
        recordRequest.setParam(form.getFormId().toString() + "_" + userInfo.getCurrentUserCode());
        try {
            recordRequest.setRequestUrl(form.getFileKey());

            // 检查月流向文件名格式
            String failReason = fleeingGoodsFormApi.checkUploadFileName(fileName);
            // 存在失败原因则保存上传失败的记录，不再创建excel导入任务
            if (failReason != null) {
                SaveFleeingGoodsFormRequest request = new SaveFleeingGoodsFormRequest();
                request.setFormId(form.getFormId());
                request.setFileName(fileName);
                request.setFileUrl(form.getFileKey());
                request.setCheckStatus(FlowMonthUploadCheckStatusEnum.NOT_PASS.getCode());
                request.setImportStatus(FlowMonthUploadImportStatusEnum.FAIL.getCode());
                request.setFailReason(failReason);
                request.setImportFileType(2);
                request.setImportUser(userInfo.getCurrentUserCode());
                request.setImportTime(new Date());
                request.setOpUserId(userInfo.getCurrentUserId());
                fleeingGoodsFormApi.saveUpload(request);
            } else {
                Long recordId=excelTaskRecordApi.saveExcelTaskRecord(recordRequest);
                SaveFleeingGoodsFormRequest request = new SaveFleeingGoodsFormRequest();
                request.setFormId(form.getFormId());
                request.setTaskId(recordId);
                request.setFileName(fileName);
                request.setFileUrl(form.getFileKey());
                request.setImportStatus(FlowMonthUploadImportStatusEnum.IN_PROGRESS.getCode());
                request.setImportFileType(2);
                request.setImportUser(userInfo.getCurrentUserCode());
                request.setImportTime(new Date());
                request.setOpUserId(userInfo.getCurrentUserId());
                fleeingGoodsFormApi.saveUpload(request);
            }

            return Result.success(true);
        } catch (Exception e) {
            log.error("创建导入任务失败", e);
        }
        return Result.failed("创建导入任务失败");
    }

    @ApiOperation("生成流向表单")
    @PostMapping("/createFleeFlowForm")
    public Result<Boolean> createFleeFlowForm(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody CreateFleeFlowForm form) {
        CreateFleeFlowRequest flowRequest = PojoUtils.map(form, CreateFleeFlowRequest.class);
        flowRequest.setEmpId(userInfo.getCurrentUserCode());
        flowRequest.setOpUserId(userInfo.getCurrentUserId());
        boolean isSuccess = fleeingGoodsFormApi.createFleeFlowForm(flowRequest);
        if (isSuccess) {
            return Result.success(true);
        }
        return Result.failed("生成流向表单失败");
    }
}
