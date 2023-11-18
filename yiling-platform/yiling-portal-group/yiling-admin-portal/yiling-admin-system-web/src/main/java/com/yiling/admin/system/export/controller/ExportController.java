package com.yiling.admin.system.export.controller;

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
import com.yiling.admin.system.export.form.QueryExportPageListForm;
import com.yiling.admin.system.export.form.SaveExportTaskForm;
import com.yiling.admin.system.export.vo.ExportTaskRecordVO;
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
import com.yiling.user.system.bo.CurrentAdminInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 下载中心
 *
 * @author: shuang.zhang
 * @date: 2021/5/31
 */
@RestController
@Api(tags = "下载中心")
@RequestMapping("/admin/export")
@Slf4j
public class ExportController extends BaseController {

    @DubboReference
    ExportApi exportApi;

    @Autowired
    FileService fileService;

    @ApiOperation(value = "导出任务分页", httpMethod = "POST")
    @PostMapping(path = "/page")
    public Result<Page<ExportTaskRecordVO>> page(@RequestBody @Valid QueryExportPageListForm form
            , @CurrentUser CurrentAdminInfo adminInfo) {
        QueryExportPageListRequest request = PojoUtils.map(form, QueryExportPageListRequest.class);
        request.setUserId(adminInfo.getCurrentUserId());
        return Result.success(PojoUtils.map(exportApi.pageList(request), ExportTaskRecordVO.class));
    }

    @ApiOperation(value = "导出任务下载", httpMethod = "GET")
    @GetMapping(path = "/downloadFile")
    public Result<UrlObject> downloadFile(@RequestParam @ApiParam(name = "taskId", value = "任务ID号", required = true) Long taskId
            , @CurrentUser CurrentAdminInfo adminInfo) {
        ExportTaskRecordDTO record = exportApi.findExportTaskRecord(adminInfo.getCurrentUserId(), taskId);
        if (null != record) {
            return Result.success(new UrlObject(fileService.getUrl(record.getRequestUrl(), FileTypeEnum.FILE_EXPORT_CENTER)));
        }
        return Result.failed(ResultCode.FAILED);
    }


    @ApiOperation(value = "创建导出任务", httpMethod = "POST")
    @PostMapping(path = "/export")
    public Result<BoolObject> export(@RequestBody @Valid SaveExportTaskForm form
            , @CurrentUser CurrentAdminInfo adminInfo) {
        SaveExportTaskRequest request = PojoUtils.map(form, SaveExportTaskRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        request.setSource(ExcelSourceEnum.ADMIN.getCode());
        Boolean result=exportApi.saveExportTaskRecord(request);
        return Result.success(new BoolObject(result));
    }

}
