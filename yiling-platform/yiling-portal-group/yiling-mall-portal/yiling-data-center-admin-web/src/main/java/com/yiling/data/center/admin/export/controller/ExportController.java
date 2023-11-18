package com.yiling.data.center.admin.export.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.data.center.admin.export.form.QueryExportPageListForm;
import com.yiling.data.center.admin.export.form.SaveExportSearchConditionForm;
import com.yiling.data.center.admin.export.form.SaveExportTaskForm;
import com.yiling.data.center.admin.export.vo.ExportTaskRecordVO;
import com.yiling.export.excel.enums.ExcelSourceEnum;
import com.yiling.export.export.api.ExportApi;
import com.yiling.export.export.dto.ExportTaskRecordDTO;
import com.yiling.export.export.dto.request.QueryExportPageListRequest;
import com.yiling.export.export.dto.request.SaveExportTaskRequest;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.BoolObject;
import com.yiling.framework.common.web.rest.UrlObject;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.user.system.bo.CurrentStaffInfo;

import cn.hutool.core.collection.CollUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 下载中心
 *
 * @author: shuang.zhang
 * @date: 2021/5/31
 */
@RestController
@Api(tags = "下载中心")
@RequestMapping("/data/export")
@Slf4j
public class ExportController extends BaseController {

    @DubboReference
    ExportApi exportApi;

    @Autowired
    FileService fileService;

    @ApiOperation(value = "导出任务分页", httpMethod = "POST")
    @PostMapping(path = "/page")
    public Result<Page<ExportTaskRecordVO>> page(@RequestBody @Valid QueryExportPageListForm form
            , @CurrentUser CurrentStaffInfo staffInfo) {
        QueryExportPageListRequest request = PojoUtils.map(form, QueryExportPageListRequest.class);
        request.setUserId(staffInfo.getCurrentUserId());
        return Result.success(PojoUtils.map(exportApi.pageList(request), ExportTaskRecordVO.class));
    }

    @ApiOperation(value = "导出任务下载", httpMethod = "GET")
    @GetMapping(path = "/downloadFile")
    public Result<UrlObject> downloadFile(@RequestParam(value = "taskId", required = true) Long taskId
            , @CurrentUser CurrentStaffInfo staffInfo) {
        ExportTaskRecordDTO record = exportApi.findExportTaskRecord(staffInfo.getCurrentUserId(), taskId);
        if (null != record) {
            return Result.success(new UrlObject(fileService.getUrl(record.getRequestUrl(), FileTypeEnum.FILE_EXPORT_CENTER)));
        }
        return Result.failed(ResultCode.FAILED);
    }

    @ApiOperation(value = "创建导出任务", httpMethod = "POST")
    @PostMapping(path = "/export")
    public Result<BoolObject> export(@RequestBody @Valid SaveExportTaskForm form
            , @CurrentUser CurrentStaffInfo staffInfo) {
        SaveExportSearchConditionForm conditionForm=new SaveExportSearchConditionForm();
        conditionForm.setDesc("操作企业");
        conditionForm.setName("eid");
        conditionForm.setValue(String.valueOf(staffInfo.getCurrentEid()));
        conditionForm.setVisibility(0);
        if(CollUtil.isEmpty(form.getSearchConditionList())){
            List<SaveExportSearchConditionForm> searchConditionList=new ArrayList<>();
            form.setSearchConditionList(searchConditionList);
        }
        form.getSearchConditionList().add(conditionForm);
        SaveExportSearchConditionForm conditionUserForm=new SaveExportSearchConditionForm();
        conditionUserForm.setDesc("当前用户");
        conditionUserForm.setName("currentUserId");
        conditionUserForm.setValue(String.valueOf(staffInfo.getCurrentUserId()));
        conditionUserForm.setVisibility(0);
        form.getSearchConditionList().add(conditionUserForm);
        SaveExportTaskRequest request = PojoUtils.map(form, SaveExportTaskRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setSource(ExcelSourceEnum.BUSINESS.getCode());
        return Result.success(new BoolObject(exportApi.saveExportTaskRecord(request)));
    }

}
