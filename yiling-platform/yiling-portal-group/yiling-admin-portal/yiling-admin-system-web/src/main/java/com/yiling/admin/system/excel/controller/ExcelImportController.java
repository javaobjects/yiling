package com.yiling.admin.system.excel.controller;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.system.excel.form.QueryExcelTaskRecordPageListForm;
import com.yiling.admin.system.excel.vo.ExcelTaskConfigVO;
import com.yiling.admin.system.excel.vo.ExcelTaskRecordVO;
import com.yiling.export.excel.api.ExcelTaskConfigApi;
import com.yiling.export.excel.api.ExcelTaskRecordApi;
import com.yiling.export.excel.dto.ExcelTaskConfigDTO;
import com.yiling.export.excel.dto.ExcelTaskRecordDTO;
import com.yiling.export.excel.dto.request.QueryExcelTaskRecordPageListRequest;
import com.yiling.export.excel.dto.request.SaveExcelTaskRecordRequest;
import com.yiling.export.excel.enums.ExcelSourceEnum;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.UrlObject;
import com.yiling.framework.oss.bo.FileInfo;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentAdminInfo;
import com.yiling.user.system.dto.UserDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: shuang.zhang
 * @date: 2022/11/17
 */
@RestController
@Api(tags = "导入中心")
@RequestMapping("/excel/import")
@Slf4j
public class ExcelImportController extends BaseController {

    @Autowired
    FileService        fileService;
    @DubboReference
    ExcelTaskConfigApi excelTaskConfigApi;
    @DubboReference
    ExcelTaskRecordApi excelTaskRecordApi;
    @DubboReference
    UserApi            userApi;

    @ApiOperation(value = "导入任务分页", httpMethod = "POST")
    @PostMapping(path = "/{excelCode}/page")
    public Result<Page<ExcelTaskRecordVO>> page(@PathVariable("excelCode") String excelCode, @RequestBody @Valid QueryExcelTaskRecordPageListForm form, @CurrentUser CurrentAdminInfo adminInfo) {
        ExcelTaskConfigDTO excelTaskConfigDTO = excelTaskConfigApi.findExcelTaskConfigByCode(excelCode);

        QueryExcelTaskRecordPageListRequest request = PojoUtils.map(form, QueryExcelTaskRecordPageListRequest.class);
        request.setTaskConfigId(excelTaskConfigDTO.getId());
        request.setSource(ExcelSourceEnum.ADMIN.getCode());
        request.setEid(0L);
        Page<ExcelTaskRecordDTO> page = excelTaskRecordApi.pageList(request);
        List<ExcelTaskRecordDTO> list = page.getRecords();
        if (CollUtil.isEmpty(list)) {
            return Result.success(form.getPage());
        }

        // 用户字典
        List<Long> userIds = list.stream().map(ExcelTaskRecordDTO::getCreateUser).distinct().collect(Collectors.toList());
        List<UserDTO> userDTOList = userApi.listByIds(userIds);
        Map<Long, UserDTO> userDTOMap = userDTOList.stream().collect(Collectors.toMap(UserDTO::getId, Function.identity()));

        List<ExcelTaskRecordVO> voList = CollUtil.newArrayList();
        list.forEach(e -> {
            ExcelTaskRecordVO vo = PojoUtils.map(e, ExcelTaskRecordVO.class);
            vo.setDownloadName(FileNameUtil.getName(e.getResultUrl()));
            vo.setCreateName(userDTOMap.getOrDefault(e.getCreateUser(), new UserDTO()).getName());
            voList.add(vo);
        });

        Page<ExcelTaskRecordVO> voPage = form.getPage();
        voPage.setTotal(page.getTotal());
        voPage.setRecords(voList);

        return Result.success(voPage);
    }

    @ApiOperation(value = "下载任务执行结果文件", httpMethod = "GET")
    @GetMapping(path = "/{excelCode}/result/{id}")
    public Result<UrlObject> downloadFile(@PathVariable("excelCode") String excelCode, @PathVariable("id") Long id, @CurrentUser CurrentAdminInfo adminInfo) {
        ExcelTaskRecordDTO record = excelTaskRecordApi.findExcelTaskRecordById(id);
        if (null != record) {
            return Result.success(new UrlObject(fileService.getUrl(record.getResultUrl(), FileTypeEnum.EXCEL_IMPORT_RESULT)));
        }
        return Result.failed(ResultCode.FAILED);
    }

    @ApiOperation(value = "展示导入模板信息", httpMethod = "GET")
    @GetMapping(path = "{excelCode}/config")
    public Result<ExcelTaskConfigVO> get(@PathVariable("excelCode") String excelCode, @CurrentUser CurrentAdminInfo adminInfo) {
        ExcelTaskConfigDTO excelTaskConfigDTO = excelTaskConfigApi.findExcelTaskConfigByCode(excelCode);

        ExcelTaskConfigVO excelTaskConfigVO = new ExcelTaskConfigVO();
        excelTaskConfigVO.setExcelCode(excelTaskConfigDTO.getExcelCode());
        excelTaskConfigVO.setTitle(excelTaskConfigDTO.getTitle());
        excelTaskConfigVO.setDescriptionList(JSONUtil.toList(JSONUtil.parseArray(excelTaskConfigDTO.getExcelDescribe()), String.class));
        excelTaskConfigVO.setLimitType(excelTaskConfigDTO.getLimitType());
        excelTaskConfigVO.setLimitSize(excelTaskConfigDTO.getLimitSize());
        excelTaskConfigVO.setTemplateUrl(excelTaskConfigDTO.getTemplateUrl());

        return Result.success(excelTaskConfigVO);
    }

    @ApiOperation(value = "创建导入任务", httpMethod = "POST")
    @PostMapping(path = "{excelCode}/save")
    public Result<Boolean> save(@RequestParam(value = "file", required = true) MultipartFile file, @PathVariable("excelCode") String excelCode, @CurrentUser CurrentAdminInfo adminInfo) {
        ExcelTaskConfigDTO excelTaskConfigDTO = excelTaskConfigApi.findExcelTaskConfigByCode(excelCode);
        if (excelTaskConfigDTO == null) {
            return Result.failed("没有配置模板信息");
        }
        if (file == null) {
            return Result.failed("上传文件信息为空");
        }
        if (file.getSize() / 1024 > excelTaskConfigDTO.getLimitSize()) {
            return Result.failed("上传文件超过最大限制");
        }
        SaveExcelTaskRecordRequest recordRequest = new SaveExcelTaskRecordRequest();
        recordRequest.setClassName(excelTaskConfigDTO.getClassName());
        recordRequest.setVerifyHandlerBeanName(excelTaskConfigDTO.getVerifyHandlerBeanName());
        recordRequest.setImportDataHandlerBeanName(excelTaskConfigDTO.getImportDataHandlerBeanName());
        recordRequest.setModelClass(excelTaskConfigDTO.getModelClass());
        recordRequest.setEid(0L);
        recordRequest.setOpUserId(adminInfo.getCurrentUserId());
        recordRequest.setTitle(excelTaskConfigDTO.getTitle());
        recordRequest.setMenuName(excelTaskConfigDTO.getMenuName());
        recordRequest.setTaskConfigId(excelTaskConfigDTO.getId());
        recordRequest.setSource(ExcelSourceEnum.ADMIN.getCode());
        recordRequest.setFileName(file.getOriginalFilename());
        try {
            FileInfo fileInfo = fileService.upload(file, FileTypeEnum.EXCEL_IMPORT_RESULT);
            recordRequest.setRequestUrl(fileInfo.getKey());
            excelTaskRecordApi.saveExcelTaskRecord(recordRequest);
            return Result.success(true);
        } catch (Exception e) {
            log.error("创建导入任务失败", e);
        }
        return Result.failed("创建导入任务失败");
    }
}
