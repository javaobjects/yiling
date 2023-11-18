package com.yiling.sjms.excel.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.api.FlowInTransitOrderApi;
import com.yiling.dataflow.wash.api.FlowMonthWashControlApi;
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
import com.yiling.sjms.excel.form.QueryExcelTaskRecordPageListForm;
import com.yiling.sjms.excel.form.SaveExcelTaskRecordForm;
import com.yiling.sjms.excel.vo.ExcelTaskConfigVO;
import com.yiling.sjms.excel.vo.ExcelTaskRecordVO;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

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
    FileService fileService;
    @DubboReference
    ExcelTaskConfigApi excelTaskConfigApi;
    @DubboReference
    ExcelTaskRecordApi excelTaskRecordApi;
    @DubboReference
    UserApi userApi;
    @DubboReference
    private FlowMonthWashControlApi flowMonthWashControlApi;
    @DubboReference
    private FlowInTransitOrderApi flowInTransitOrderApi;

    @ApiOperation(value = "导入任务分页", httpMethod = "POST")
    @PostMapping(path = "/{excelCode}/page")
    public Result<Page<ExcelTaskRecordVO>> page(@PathVariable("excelCode") String excelCode, @RequestBody @Valid QueryExcelTaskRecordPageListForm form, @CurrentUser CurrentSjmsUserInfo staffInfo) {
        ExcelTaskConfigDTO excelTaskConfigDTO = excelTaskConfigApi.findExcelTaskConfigByCode(excelCode);

        QueryExcelTaskRecordPageListRequest request = PojoUtils.map(form, QueryExcelTaskRecordPageListRequest.class);
        request.setTaskConfigId(excelTaskConfigDTO.getId());
        request.setEid(0L);
        request.setSource(ExcelSourceEnum.DIH.getCode());
        // 时间范围3个月以内
        Date date = new Date();
        Date createTimeEnd = DateUtil.endOfDay(date);
        DateTime dateTimeStart = DateUtil.offsetMonth(date, -3);
        Date createTimeStart = DateUtil.beginOfDay(dateTimeStart);
        request.setCreateTimeStart(createTimeStart);
        request.setCreateTimeEnd(createTimeEnd);
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
    public Result<UrlObject> downloadFile(@PathVariable("excelCode") String excelCode, @PathVariable("id") Long id, @CurrentUser CurrentSjmsUserInfo staffInfo) {
        ExcelTaskRecordDTO record = excelTaskRecordApi.findExcelTaskRecordById(id);
        if (null != record) {
            return Result.success(new UrlObject(fileService.getUrl(record.getResultUrl(), FileTypeEnum.EXCEL_IMPORT_RESULT)));
        }
        return Result.failed(ResultCode.FAILED);
    }

    @ApiOperation(value = "展示导入模板信息", httpMethod = "GET")
    @GetMapping(path = "{excelCode}/config")
    public Result<ExcelTaskConfigVO> get(@PathVariable("excelCode") String excelCode, @CurrentUser CurrentSjmsUserInfo staffInfo) {
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
    public Result<Boolean> save(@PathVariable("excelCode") String excelCode, @CurrentUser CurrentSjmsUserInfo staffInfo, @RequestBody @Valid SaveExcelTaskRecordForm form) {
        ExcelTaskConfigDTO excelTaskConfigDTO = excelTaskConfigApi.findExcelTaskConfigByCode(excelCode);
        if (excelTaskConfigDTO == null) {
            return Result.failed("没有配置模板信息");
        }

        FileInfo fileInfo=fileService.get(FileTypeEnum.EXCEL_IMPORT_RESULT,form.getOssKey());
        if (fileInfo == null) {
            return Result.failed("通过ossKey没有找到对应的文件信息");
        }

        // 终端库存导入，所属年月必填
        boolean flowGoodsBatchTerminalFlag = ObjectUtil.equal(excelCode, "importFlowGoodsBatchTerminal") ? true : false;
        if (flowGoodsBatchTerminalFlag){
            // 所属年月对应的流向清洗日程终端库存上报是否开启状态
            if (StrUtil.isBlank(form.getGbDetailMonth())) {
                return Result.failed("所属年月不能为空");
            }
            if (form.getGbDetailMonth().length() > 7) {
                String gbDetailMonth = DateUtil.format(DateUtil.parse(form.getGbDetailMonth()), "yyyy-MM");
                form.setGbDetailMonth(gbDetailMonth);
            }
            // 终端库存导入新增，校验清洗日程
            String errorMsg = flowInTransitOrderApi.checkFlowMonthWashControl("终端库存上报", form.getGbDetailMonth());
            if (StrUtil.isNotBlank(errorMsg)) {
                return Result.failed(errorMsg);
            }
        }

        SaveExcelTaskRecordRequest recordRequest = new SaveExcelTaskRecordRequest();
        recordRequest.setClassName(excelTaskConfigDTO.getClassName());
        recordRequest.setVerifyHandlerBeanName(excelTaskConfigDTO.getVerifyHandlerBeanName());
        recordRequest.setImportDataHandlerBeanName(excelTaskConfigDTO.getImportDataHandlerBeanName());
        recordRequest.setModelClass(excelTaskConfigDTO.getModelClass());
        recordRequest.setOpUserId(staffInfo.getCurrentUserId());
        recordRequest.setTitle(excelTaskConfigDTO.getTitle());
        recordRequest.setMenuName(excelTaskConfigDTO.getMenuName());
        recordRequest.setTaskConfigId(excelTaskConfigDTO.getId());
        recordRequest.setSource(ExcelSourceEnum.DIH.getCode());
        recordRequest.setFileName(fileInfo.getName());
        // 设置参数
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(CurrentSjmsUserInfo.CURRENT_USER_ID, staffInfo.getCurrentUserId());
        paramMap.put(CurrentSjmsUserInfo.CURRENT_USER_CODE, staffInfo.getCurrentUserCode());
        if (flowGoodsBatchTerminalFlag){
            paramMap.put("gbDetailMonth", form.getGbDetailMonth());
        }
        String paramStr = JSONUtil.toJsonStr(paramMap);
        recordRequest.setParam(paramStr);
        try {
            recordRequest.setRequestUrl(fileInfo.getKey());
            boolean result = false;
            Long taskRecordId = excelTaskRecordApi.saveExcelTaskRecord(recordRequest);
            if (taskRecordId > 0) {
                result = true;
            }
            return Result.success(result);
        } catch (Exception e) {
            log.error("创建导入任务失败", e);
        }
        return Result.failed("创建导入任务失败");
    }
}
