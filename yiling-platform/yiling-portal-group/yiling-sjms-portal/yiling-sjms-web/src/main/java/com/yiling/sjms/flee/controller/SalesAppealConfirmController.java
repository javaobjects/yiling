package com.yiling.sjms.flee.controller;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.flowcollect.api.FlowMonthUploadRecordApi;
import com.yiling.dataflow.flowcollect.enums.FlowMonthUploadCheckStatusEnum;
import com.yiling.dataflow.flowcollect.enums.FlowMonthUploadImportStatusEnum;
import com.yiling.dataflow.wash.api.FlowMonthWashControlApi;
import com.yiling.export.excel.api.ExcelTaskConfigApi;
import com.yiling.export.excel.api.ExcelTaskRecordApi;
import com.yiling.export.excel.dto.ExcelTaskConfigDTO;
import com.yiling.export.excel.dto.request.SaveExcelTaskRecordRequest;
import com.yiling.export.excel.enums.ExcelSourceEnum;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.service.FileService;
import com.yiling.sjms.flee.api.SaleaAppealFormApi;
import com.yiling.sjms.flee.bo.SalesAppealFormBO;
import com.yiling.sjms.flee.dto.SalesAppealExtFormDTO;
import com.yiling.sjms.flee.dto.SalesAppealFormDTO;
import com.yiling.sjms.flee.dto.request.CreateSalesAppealFlowRequest;
import com.yiling.sjms.flee.dto.request.QuerySalesAppealPageRequest;
import com.yiling.sjms.flee.dto.request.RemoveFleeingGoodsFormRequest;
import com.yiling.sjms.flee.dto.request.SaveSalesAppealFormDetailRequest;
import com.yiling.sjms.flee.form.CreateFleeFlowForm;
import com.yiling.sjms.flee.form.QuerySalesAppealForm;
import com.yiling.sjms.flee.form.QuerySalesAppealPageForm;
import com.yiling.sjms.flee.form.SaveExcelUploadTaskForm;
import com.yiling.sjms.flee.vo.SalesAppealFormVO;
import com.yiling.sjms.flee.vo.SalesAppealVO;
import com.yiling.sjms.flee.vo.SeasAppealFormVO;
import com.yiling.sjms.form.api.FormApi;
import com.yiling.sjms.form.dto.FormDTO;
import com.yiling.sjms.org.api.BusinessDepartmentApi;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.yiling.framework.oss.enums.FileTypeEnum.EXCEL_IMPORT_RESULT;

/**
 * <p>
 * 销量申诉表单 前端控制器
 * </p>
 *
 * @author shixing.sun
 * @date 2023-03-14
 */
@Api(tags = "销量申诉确认表单")
@RestController
@Slf4j
@RequestMapping("/sales/appeal/confirm")
public class SalesAppealConfirmController extends BaseController {

    @DubboReference
    FormApi formApi;

    @DubboReference(timeout = 15 * 1000)
    SaleaAppealFormApi saleaAppealFormApi;

    @DubboReference
    UserApi userApi;

    @DubboReference
    private EsbEmployeeApi esbEmployeeApi;

    @DubboReference
    FlowMonthUploadRecordApi flowMonthUploadRecordApi;
    @DubboReference
    FlowMonthWashControlApi  flowMonthWashControlApi;
    @DubboReference
    ExcelTaskConfigApi       excelTaskConfigApi;
    @DubboReference
    ExcelTaskRecordApi       excelTaskRecordApi;
    @Autowired
    FileService              fileService;
    @DubboReference
    private BusinessDepartmentApi businessDepartmentApi;

    @ApiOperation(value = "流程列表")
    @PostMapping("/queryFormPage")
    public Result<Page<SalesAppealVO>> queryFormPage(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody QuerySalesAppealPageForm form) {
        QuerySalesAppealPageRequest request = new QuerySalesAppealPageRequest();
        PojoUtils.map(form, request);
        request.setEmpId(userInfo.getCurrentUserCode());
        Page<SalesAppealFormBO> boPage = saleaAppealFormApi.pageForm(request);
        if (CollectionUtil.isNotEmpty(boPage.getRecords())) {
            boPage.getRecords().forEach(item -> {
                EsbEmployeeDTO esbEmployeeDTO = esbEmployeeApi.getByEmpId(item.getConfirmUser());
                if (ObjectUtil.isNotNull(esbEmployeeDTO)) {
                    item.setConfirmUserName(esbEmployeeDTO.getEmpName());
                }
            });
        }
        return Result.success(PojoUtils.map(boPage, SalesAppealVO.class));
    }


    @ApiOperation(value = "根据formId查看表头部分-销量申诉确认")
    @PostMapping("/querySubmitInfo")
    public Result<SeasAppealFormVO> querySubmitInfo(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody QuerySalesAppealPageForm form) {

        SeasAppealFormVO seasAppealFormVO = new SeasAppealFormVO();

        if (ObjectUtil.isNotNull(form.getFormId())) {
            SalesAppealExtFormDTO appendix = saleaAppealFormApi.queryAppendix(form.getFormId());
            if (ObjectUtil.isNotEmpty(appendix)) {
                seasAppealFormVO.setConfirmRemark(appendix.getConfirmRemark());
                seasAppealFormVO.setConfirmStatus(appendix.getConfirmStatus());
                if (appendix.getConfirmStatus() == 1) {
                    EsbEmployeeDTO employee = esbEmployeeApi.getByEmpId(userInfo.getCurrentUserCode());
                    seasAppealFormVO.setEmpName(employee.getEmpName());
                    seasAppealFormVO.setBizDept(employee.getYxDept());
                } else if (appendix.getConfirmStatus() == 2) {
                    EsbEmployeeDTO employee = esbEmployeeApi.getByEmpId(appendix.getConfirmUser());
                    if (ObjectUtil.isNotEmpty(employee)) {
                        seasAppealFormVO.setEmpName(employee.getEmpName());
                        seasAppealFormVO.setBizDept(employee.getYxDept());
                    }
                }
            }
            FormDTO formDTO = formApi.getById(form.getFormId());
            // 传输类型：1、上传excel; 2、选择流向
            seasAppealFormVO.setTransferType(formDTO.getTransferType());
        }
        return Result.success(seasAppealFormVO);
    }

    @ApiOperation("分页查询")
    @PostMapping("/pageList")
    public Result<List<SalesAppealFormVO>> pageList(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody QuerySalesAppealForm form) {
        if (Objects.isNull(form.getFormId())) {
            return Result.success(new ArrayList<>());
        }

        List<SalesAppealFormDTO> dtoPage = saleaAppealFormApi.pageList(form.getFormId(), 2);
        List<SalesAppealFormVO> voPage = PojoUtils.map(dtoPage, SalesAppealFormVO.class);
        for (SalesAppealFormVO item : voPage) {
            String uploader = item.getUploader();
            if (StringUtils.isNotEmpty(uploader)) {
                EsbEmployeeDTO esbEmployeeDTO = esbEmployeeApi.getByEmpId(uploader);
                item.setUploaderName(null != esbEmployeeDTO ? esbEmployeeDTO.getEmpName() : "");
            }
            String prefix = item.getExcelName().split("\\.")[0];
            String suffix = item.getSourceUrl().split("\\.")[1];
            if (StringUtils.isNotEmpty(item.getTargetUrl())) {
                suffix = item.getTargetUrl().split("\\.")[1];
            }
            item.setDownLoadName(prefix + "." + suffix);
            if (StringUtils.isNotEmpty(item.getTargetUrl())) {
                String target = fileService.getUrl(item.getTargetUrl(), EXCEL_IMPORT_RESULT);
                item.setTargetUrl(target);

            }
            if (StringUtils.isNotEmpty(item.getSourceUrl())) {
                String target = fileService.getUrl(item.getSourceUrl(), EXCEL_IMPORT_RESULT);
                item.setSourceUrl(target);
            }
            item.setFileUrl(StringUtils.isEmpty(item.getTargetUrl()) ? item.getSourceUrl() : item.getTargetUrl());
        }
        return Result.success(voPage);
    }


    @ApiOperation("删除")
    @GetMapping("/remove")
    public Result<Boolean> remove(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestParam(value = "id") Long id) {
        RemoveFleeingGoodsFormRequest request = new RemoveFleeingGoodsFormRequest();
        request.setOpUserId(userInfo.getCurrentUserId());
        request.setId(id);
        boolean isSuccess = saleaAppealFormApi.removeById(request);
        if (isSuccess) {
            return Result.success(true);
        }
        return Result.failed("删除失败");
    }


    @ApiOperation(value = "确认流向申诉上传文件接口", httpMethod = "POST")
    @PostMapping(path = "/saveExcelTask")
    public Result<Boolean> saveExcelTask(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid SaveExcelUploadTaskForm form) {
        String fileName = form.getFileName();
        String excelCode = "importSalesAppeal";
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
            String failReason = saleaAppealFormApi.checkFlowFileName(fileName);
            // 存在失败原因则保存上传失败的记录，不再创建excel导入任务
            if (failReason != null) {
                SaveSalesAppealFormDetailRequest request = new SaveSalesAppealFormDetailRequest();
                request.setExcelName(fileName);
                request.setType(2);
                request.setFormId(form.getFormId());
                request.setSourceUrl(form.getFileKey());
                request.setCheckStatus(FlowMonthUploadCheckStatusEnum.NOT_PASS.getCode());
                request.setImportStatus(FlowMonthUploadImportStatusEnum.FAIL.getCode());
                request.setReason(failReason);
                request.setUploadTime(new Date());
                request.setUploader(userInfo.getCurrentUserCode());
                request.setAppealMonth(getDateByFileName(fileName));
                request.setDataType(saleaAppealFormApi.getDateType(fileName));
                request.setOpUserId(userInfo.getCurrentUserId());
                saleaAppealFormApi.saveSalesConfirm(request);
            } else {
                Long recordId = excelTaskRecordApi.saveExcelTaskRecord(recordRequest);
                SaveSalesAppealFormDetailRequest request = new SaveSalesAppealFormDetailRequest();
                request.setTaskId(recordId);
                request.setExcelName(fileName);
                request.setType(2);
                request.setFormId(form.getFormId());
                request.setSourceUrl(form.getFileKey());
                //request.setCheckStatus(FlowMonthUploadCheckStatusEnum.NOT_PASS.getCode());
                request.setImportStatus(FlowMonthUploadImportStatusEnum.IN_PROGRESS.getCode());
                request.setUploadTime(new Date());
                request.setUploader(userInfo.getCurrentUserCode());
                request.setAppealMonth(getDateByFileName(fileName));
                request.setDataType(saleaAppealFormApi.getDateType(fileName));
                request.setOpUserId(userInfo.getCurrentUserId());
                saleaAppealFormApi.saveSalesConfirm(request);
            }
            return Result.success(true);
        } catch (Exception e) {
            log.error("创建导入任务失败", e);
        }
        return Result.failed("创建导入任务失败");
    }

    @ApiOperation("生成流向表单")
    @PostMapping("/createSalesAppealFlowForm")
    public Result<Boolean> createFleeFlowForm(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody CreateFleeFlowForm form) {
        CreateSalesAppealFlowRequest flowRequest = PojoUtils.map(form, CreateSalesAppealFlowRequest.class);
        flowRequest.setOpUserId(userInfo.getCurrentUserId());
        flowRequest.setEmpId(userInfo.getCurrentUserCode());
//        String isSuccess = saleaAppealFormApi.createFleeFlowForm(flowRequest);
        String isSuccess = saleaAppealFormApi.compatibleCreateFleeFlowForm(flowRequest);
        if (StringUtils.isEmpty(isSuccess)) {
            return Result.success(true);
        }
        return Result.failed(isSuccess);
    }


    public DateTime getDateByFileName(String fileName) {
        try {
            String[] fileNameArr = fileName.split("_");
            String[] dateArr = fileNameArr[3].split("\\.");
            DateTime date = DateUtil.parse(dateArr[0], "yyyyMMdd");
            return date;
        } catch (Exception e) {
            return null;
        }
    }
}
