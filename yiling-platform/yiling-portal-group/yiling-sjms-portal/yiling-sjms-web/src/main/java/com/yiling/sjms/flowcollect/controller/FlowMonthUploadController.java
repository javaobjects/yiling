package com.yiling.sjms.flowcollect.controller;

import java.util.Objects;

import javax.validation.Valid;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.flowcollect.api.FlowMonthUploadRecordApi;
import com.yiling.dataflow.flowcollect.bo.FlowMonthUploadRecordBO;
import com.yiling.dataflow.flowcollect.dto.FlowMonthUploadRecordDTO;
import com.yiling.dataflow.flowcollect.dto.request.QueryFlowMonthUploadPageRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowMonthUploadRecordRequest;
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
import com.yiling.sjms.flee.api.SaleaAppealFormApi;
import com.yiling.sjms.flee.dto.FleeingGoodsFormDTO;
import com.yiling.sjms.flee.dto.SalesAppealFormDTO;
import com.yiling.sjms.flowcollect.form.DeleteFlowMonthUploadRecordForm;
import com.yiling.sjms.flowcollect.form.GetDownLoadNameForm;
import com.yiling.sjms.flowcollect.form.QueryFlowMonthUploadPageForm;
import com.yiling.sjms.flowcollect.form.SaveExcelUploadTaskForm;
import com.yiling.sjms.flowcollect.vo.FlowMonthUploadFlagVO;
import com.yiling.sjms.flowcollect.vo.FlowMonthUploadRecordListItemVO;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 月流向上传 前端控制器
 *
 * @author: lun.yu
 * @date: 2023-03-08
 */
@Slf4j
@RestController
@RequestMapping(value = "/flowMonthUpload")
@Api(tags = "月流向上传")
public class FlowMonthUploadController extends BaseController {

    @DubboReference
    FlowMonthUploadRecordApi flowMonthUploadRecordApi;
    @DubboReference
    FlowMonthWashControlApi  flowMonthWashControlApi;
    @DubboReference
    ExcelTaskConfigApi       excelTaskConfigApi;
    @DubboReference
    ExcelTaskRecordApi       excelTaskRecordApi;
    @DubboReference
    SaleaAppealFormApi       saleaAppealFormApi;
    @DubboReference
    FleeingGoodsFormApi      fleeingGoodsFormApi;

    @Autowired
    FileService fileService;

    @ApiOperation(value = "月流向上传记录列表分页")
    @PostMapping("/queryFlowMonthPage")
    public Result<Page<FlowMonthUploadRecordListItemVO>> queryFlowMonthPage(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid QueryFlowMonthUploadPageForm form) {
        QueryFlowMonthUploadPageRequest request = PojoUtils.map(form, QueryFlowMonthUploadPageRequest.class);
        request.setCreateUser(userInfo.getCurrentUserId());
        Page<FlowMonthUploadRecordBO> uploadRecordBOPage = flowMonthUploadRecordApi.queryFlowMonthPage(request);
        Page<FlowMonthUploadRecordListItemVO> voPage = PojoUtils.map(uploadRecordBOPage, FlowMonthUploadRecordListItemVO.class);
        voPage.getRecords().forEach(uploadRecordListItemVO -> {
            if (StrUtil.isNotEmpty(uploadRecordListItemVO.getFileUrl())) {
                String prefix = uploadRecordListItemVO.getFileName().split("\\.")[0];
                String suffix = uploadRecordListItemVO.getFileUrl().split("\\.")[1];
                uploadRecordListItemVO.setDownLoadName(prefix + "." + suffix);
            }
            uploadRecordListItemVO.setFileUrl(fileService.getUrl(uploadRecordListItemVO.getFileUrl(), FileTypeEnum.EXCEL_IMPORT_RESULT));
        });
        return Result.success(voPage);
    }

    @ApiOperation(value = "上传按钮是否可使用")
    @GetMapping("/getUploadFlag")
    public Result<FlowMonthUploadFlagVO> getUploadFlag(@CurrentUser CurrentSjmsUserInfo userInfo) {
        FlowMonthWashControlDTO flowMonthWashControlDTO = flowMonthWashControlApi.getWashStatus();
        FlowMonthUploadFlagVO uploadFlagVO = PojoUtils.map(flowMonthWashControlDTO, FlowMonthUploadFlagVO.class);
        if (Objects.nonNull(flowMonthWashControlDTO)) {
            uploadFlagVO.setFlag(true);
        }
        return Result.success(uploadFlagVO);
    }

    @ApiOperation(value = "获取下载文件名")
    @PostMapping("/getDownLoadName")
    public Result<String> getDownLoadName(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid GetDownLoadNameForm form) {
        String downLoadName = null;
        // 月流向上传
        if (form.getType() == 1) {
            FlowMonthUploadRecordDTO uploadRecordDTO = flowMonthUploadRecordApi.getById(form.getId());
            if (Objects.nonNull(uploadRecordDTO) && StrUtil.isNotEmpty(uploadRecordDTO.getFileUrl())) {
                String prefix = uploadRecordDTO.getFileName().split("\\.")[0];
                String suffix = uploadRecordDTO.getFileUrl().split("\\.")[1];
                downLoadName = prefix + "." + suffix;
            }

        } else if (form.getType() == 2) {
            // 销售申述
            SalesAppealFormDTO salesAppealFormDTO = saleaAppealFormApi.getById(form.getId());
            if (Objects.nonNull(salesAppealFormDTO) && StrUtil.isNotEmpty(salesAppealFormDTO.getTargetUrl())) {
                String prefix = salesAppealFormDTO.getExcelName().split("\\.")[0];
                String suffix = salesAppealFormDTO.getTargetUrl().split("\\.")[1];
                downLoadName = prefix + "." + suffix;
            }

        } else if (form.getType() == 3) {
            // 销售申述
            FleeingGoodsFormDTO fleeingGoodsFormDTO = fleeingGoodsFormApi.getById(form.getId());
            if (Objects.nonNull(fleeingGoodsFormDTO) && StrUtil.isNotEmpty(fleeingGoodsFormDTO.getResultUrl())) {
                String prefix = fleeingGoodsFormDTO.getFileName().split("\\.")[0];
                String suffix = fleeingGoodsFormDTO.getResultUrl().split("\\.")[1];
                downLoadName = prefix + "." + suffix;
            }

        }

        return Result.success(downLoadName);
    }

    @ApiOperation(value = "删除")
    @PostMapping("/deleteRecord")
    public Result<Boolean> deleteRecord(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid DeleteFlowMonthUploadRecordForm form) {
        Boolean flag = flowMonthUploadRecordApi.deleteRecord(form.getId(), userInfo.getCurrentUserId());
        return Result.success(flag);
    }

    @ApiOperation(value = "生成月流向文件导入任务", httpMethod = "POST")
    @PostMapping(path = "/saveExcelTask")
    public Result<Boolean> saveExcelTask(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid SaveExcelUploadTaskForm form) {
        String fileName = form.getFileName();

        // 检查月流向文件名格式
        String failReason = flowMonthUploadRecordApi.checkFlowFileNameNew(fileName, userInfo,false);
        // 存在失败原因则保存上传失败的记录，不再创建excel导入任务
        if (failReason != null) {
            SaveFlowMonthUploadRecordRequest request = new SaveFlowMonthUploadRecordRequest();
            request.setFileName(fileName);
            request.setFileUrl(form.getFileKey());
            request.setCheckStatus(FlowMonthUploadCheckStatusEnum.NOT_PASS.getCode());
            request.setImportStatus(FlowMonthUploadImportStatusEnum.FAIL.getCode());
            request.setFailReason(failReason);
            request.setDataType(flowMonthUploadRecordApi.getDataType(fileName));
            request.setOpUserId(userInfo.getCurrentUserId());
            flowMonthUploadRecordApi.saveFlowMonthRecord(request);
            return Result.success(true);
        }

        // 根据文件名获取excelCode
        String excelCode = flowMonthUploadRecordApi.getExcelCodeByFileName(fileName);
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
        recordRequest.setRequestUrl(form.getFileKey());
        try {
            Long recordId = excelTaskRecordApi.saveExcelTaskRecord(recordRequest);
            SaveFlowMonthUploadRecordRequest request = new SaveFlowMonthUploadRecordRequest();
            request.setRecordId(recordId);
            request.setFileName(fileName);
            request.setFileUrl(form.getFileKey());
            request.setImportStatus(FlowMonthUploadImportStatusEnum.IN_PROGRESS.getCode());
            request.setDataType(flowMonthUploadRecordApi.getDataType(fileName));
            request.setOpUserId(userInfo.getCurrentUserId());
            flowMonthUploadRecordApi.saveFlowMonthRecord(request);
            return Result.success(true);
        } catch (Exception e) {
            log.error("创建导入任务失败", e);
        }
        return Result.failed("创建导入任务失败");
    }

}
