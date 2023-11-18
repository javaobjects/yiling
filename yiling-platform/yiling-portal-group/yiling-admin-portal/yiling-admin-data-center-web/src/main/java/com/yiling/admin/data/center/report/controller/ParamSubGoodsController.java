package com.yiling.admin.data.center.report.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.yiling.export.excel.model.ImportResultModel;
import com.yiling.export.excel.util.ExeclImportUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.data.center.report.form.AddReportGoodsCategoryForm;
import com.yiling.admin.data.center.report.form.DeleteParSubGoodsForm;
import com.yiling.admin.data.center.report.form.ImportParLadderForm;
import com.yiling.admin.data.center.report.form.ImportParSubGoodsForm;
import com.yiling.admin.data.center.report.form.QueryActivityGoodsForm;
import com.yiling.admin.data.center.report.form.QueryGoodsCategoryPageForm;
import com.yiling.admin.data.center.report.form.QueryParamSubGoodsPageListForm;
import com.yiling.admin.data.center.report.form.SaveReportActivityGoodsForm;
import com.yiling.admin.data.center.report.form.SaveReportLadderGoodsForm;
import com.yiling.admin.data.center.report.form.UpdateCategoryGoodsForm;
import com.yiling.admin.data.center.report.form.UpdateReportActivityGoodsForm;
import com.yiling.admin.data.center.report.form.UpdateReportLadderGoodsForm;
import com.yiling.admin.data.center.report.handler.ImportParLadderHandler;
import com.yiling.admin.data.center.report.handler.ImportParSubGoodsHandler;
import com.yiling.admin.data.center.report.vo.ActivityGoodsListItemVO;
import com.yiling.admin.data.center.report.vo.ParamSubGoodsPageListItemVO;
import com.yiling.admin.data.center.report.vo.ReportGoodsCategoryPageVO;
import com.yiling.dataflow.order.bo.FlowGoodsRelationBO;
import com.yiling.dataflow.relation.api.FlowGoodsRelationApi;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.framework.oss.vo.ImportResultVO;
import com.yiling.settlement.report.api.ReportParamApi;
import com.yiling.settlement.report.dto.ReportParamSubDTO;
import com.yiling.settlement.report.dto.ReportParamSubGoodsDTO;
import com.yiling.settlement.report.dto.request.QueryParamSubGoodsPageListRequest;
import com.yiling.settlement.report.dto.request.SaveOrUpdateParamSubGoodsRequest;
import com.yiling.settlement.report.enums.ReportParamTypeEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.system.api.StaffApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentAdminInfo;
import com.yiling.user.system.bo.Staff;
import com.yiling.user.system.dto.UserDTO;
import com.yiling.user.system.dto.request.QueryStaffListRequest;
import com.yiling.user.system.enums.UserStatusEnum;

import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: gxl
 * @date: 2022/2/23
 */
@RestController
@RequestMapping("/report/param")
@Api(tags = "报表参数维护商品相关")
@Slf4j
public class ParamSubGoodsController extends BaseController {

    @DubboReference
    ReportParamApi reportParamApi;
    @DubboReference
    UserApi userApi;
    @DubboReference
    StaffApi staffApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    FlowGoodsRelationApi flowGoodsRelationApi;

    @Autowired
    ImportParSubGoodsHandler importParSubGoodsHandler;
    @Autowired
    ImportParLadderHandler importParLadderHandler;


    @ApiOperation(value = "商品类型-查看当前类型的商品")
    @PostMapping("/listCategoryGoodsPage")
    public Result<ReportGoodsCategoryPageVO<ReportGoodsCategoryPageVO.ReportGoodsCategoryItemVO>> listCategoryGoodsPage(@RequestBody @Valid QueryGoodsCategoryPageForm form) {
        ReportParamSubDTO paramSubDTO = reportParamApi.queryReportParamSubById(form.getParamSubId());
        if (ObjectUtil.isNull(paramSubDTO)) {
            return Result.failed("子参数类型不存在");
        }
        ReportGoodsCategoryPageVO result = new ReportGoodsCategoryPageVO();
        result.setCategory(paramSubDTO.getName());
        QueryParamSubGoodsPageListRequest request = PojoUtils.map(form, QueryParamSubGoodsPageListRequest.class);

        Page<ReportParamSubGoodsDTO> page = reportParamApi.queryParamSubGoodsPageList(request);
        if (CollUtil.isEmpty(page.getRecords())) {
            return Result.success(result);
        }

        List<ReportGoodsCategoryPageVO.ReportGoodsCategoryItemVO> record = PojoUtils.map(page.getRecords(), ReportGoodsCategoryPageVO.ReportGoodsCategoryItemVO.class);
        //查询操作人
        List<Long> userIds = page.getRecords().stream().map(ReportParamSubGoodsDTO::getUpdateUser).collect(Collectors.toList());
        Map<Long, String> userDTOMap = userApi.listByIds(userIds).stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName));
        record.forEach(e -> {
            e.setUpdateUserName(userDTOMap.getOrDefault(e.getUpdateUser(), Constants.SEPARATOR_MIDDLELINE));
        });
        PojoUtils.map(page, result);
        result.setRecords(record);
        return Result.success(result);
    }


    @ApiOperation(value = "向商品分类添加商品")
    @PostMapping("/addCategoryGoods")
    public Result<Boolean> addCategoryGoods(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid AddReportGoodsCategoryForm form) {
        ReportParamSubDTO paramSubDTO = reportParamApi.queryReportParamSubById(form.getParamSubId());
        if (ObjectUtil.isNull(paramSubDTO)) {
            return Result.failed("子参数id不存在");
        }
        SaveOrUpdateParamSubGoodsRequest request = PojoUtils.map(form, SaveOrUpdateParamSubGoodsRequest.class);
        request.setParamId(paramSubDTO.getParamId());
        request.setParType(ReportParamTypeEnum.GOODS.getCode());
        request.setOpUserId(adminInfo.getCurrentUserId());
        request.setOpTime(new Date());
        return Result.success(reportParamApi.saveOrUpdateParamSubCategoryGoods(request));
    }

    @ApiOperation(value = "修改商品类型下商品的结束时间")
    @PostMapping("/updateCategoryGoods")
    public Result<Boolean> updateCategoryGoods(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody UpdateCategoryGoodsForm form) {
        ReportParamSubGoodsDTO subGoodsDTO = reportParamApi.queryParamSubGoodsById(form.getId());
        if (ObjectUtil.isNull(subGoodsDTO)) {
            return Result.failed("商品不存在");
        }
        SaveOrUpdateParamSubGoodsRequest request = PojoUtils.map(form, SaveOrUpdateParamSubGoodsRequest.class);
        request.setParType(ReportParamTypeEnum.GOODS.getCode());
        request.setOpUserId(adminInfo.getCurrentUserId());
        request.setOpTime(new Date());
        return Result.success(reportParamApi.saveOrUpdateParamSubCategoryGoods(request));
    }

    @ApiOperation(value = "查询子参数下的商品--用于阶梯及活动")
    @PostMapping("/queryParamSubGoodsPageList")
    public Result<Page<ParamSubGoodsPageListItemVO>> queryParamSubGoodsPageList(@RequestBody @Valid QueryParamSubGoodsPageListForm form) {
        ReportParamSubDTO paramSubDTO = reportParamApi.queryReportParamSubById(form.getParamSubId());
        if (ObjectUtil.isNull(paramSubDTO)) {
            return Result.failed("子参数类型不存在");
        }
        QueryParamSubGoodsPageListRequest request = PojoUtils.map(form, QueryParamSubGoodsPageListRequest.class);
        //如果传了操作人
        if (StrUtil.isNotBlank(form.getUserName())) {
            QueryStaffListRequest queryStaffListRequest = new QueryStaffListRequest();
            queryStaffListRequest.setNameEq(form.getUserName());
            queryStaffListRequest.setStatusNe(UserStatusEnum.DEREGISTER.getCode());
            List<Staff> staffList = staffApi.list(queryStaffListRequest);
            List<Long> userIds = staffList.stream().map(Staff::getId).collect(Collectors.toList());
            request.setUpdateUserList(userIds);
        }
        //如果传了商业
        if (ObjectUtil.isNotNull(form.getEid()) && ObjectUtil.notEqual(form.getEid(), 0L)) {
            EnterpriseDTO enterpriseDTO = enterpriseApi.getById(form.getEid());
            request.setEidList(ListUtil.toList(enterpriseDTO.getId()));
        }

        Page<ReportParamSubGoodsDTO> page = reportParamApi.queryParamSubGoodsPageList(request);
        if (CollUtil.isEmpty(page.getRecords())) {
            return Result.success(form.getPage());
        }
        Page<ParamSubGoodsPageListItemVO> result = PojoUtils.map(page, ParamSubGoodsPageListItemVO.class);

        //查询操作人
        List<Long> userIds = page.getRecords().stream().map(ReportParamSubGoodsDTO::getUpdateUser).collect(Collectors.toList());
        Map<Long, String> userDTOMap = userApi.listByIds(userIds).stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName));
        //查询企业信息
        List<Long> eIds = page.getRecords().stream().map(ReportParamSubGoodsDTO::getEid).collect(Collectors.toList());
        Map<Long, String> entDTOMap = enterpriseApi.listByIds(eIds).stream().collect(Collectors.toMap(EnterpriseDTO::getId, EnterpriseDTO::getName));
        result.getRecords().forEach(e -> {
            e.setUpdateUserName(userDTOMap.getOrDefault(e.getUpdateUser(), Constants.SEPARATOR_MIDDLELINE));
            e.setEName(entDTOMap.getOrDefault(e.getEid(), Constants.SEPARATOR_MIDDLELINE));
        });
        return Result.success(result);
    }

    @ApiOperation(value = "活动&阶梯参数查询商品")
    @GetMapping("/queryActivityGoods")
    public Result<CollectionObject<List<ActivityGoodsListItemVO>>> queryActivityGoods(QueryActivityGoodsForm form) {
        List<FlowGoodsRelationBO> goodsList = flowGoodsRelationApi.getListByEidAndGoodsName(form.getEid(), form.getGoodsName());

        List<ActivityGoodsListItemVO> list = PojoUtils.map(goodsList, ActivityGoodsListItemVO.class);
        CollectionObject<List<ActivityGoodsListItemVO>> result = new CollectionObject(list);
        return Result.success(result);
    }

    @ApiOperation(value = "向活动参数添加商品")
    @PostMapping("/saveActivityGoods")
    public Result<Boolean> saveActivityGoods(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid SaveReportActivityGoodsForm form) {
        ReportParamSubDTO paramSubDTO = reportParamApi.queryReportParamSubById(form.getParamSubId());
        if (ObjectUtil.isNull(paramSubDTO)) {
            return Result.failed("子参数类型不存在");
        }
        SaveOrUpdateParamSubGoodsRequest request = PojoUtils.map(form, SaveOrUpdateParamSubGoodsRequest.class);
        request.setParamId(paramSubDTO.getParamId());
        request.setParType(ReportParamTypeEnum.ACTIVITY.getCode());
        request.setOpUserId(adminInfo.getCurrentUserId());
        request.setOpTime(new Date());
        return Result.success(reportParamApi.saveOrUpdateParamSubActivityGoods(request));
    }

    @ApiOperation(value = "修改活动参数商品")
    @PostMapping("/updateActivityGoods")
    public Result<Boolean> updateActivityGoods(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody UpdateReportActivityGoodsForm form) {
        ReportParamSubGoodsDTO subGoodsDTO = reportParamApi.queryParamSubGoodsById(form.getId());
        if (ObjectUtil.isNull(subGoodsDTO)) {
            return Result.failed("商品不存在");
        }
        SaveOrUpdateParamSubGoodsRequest request = PojoUtils.map(form, SaveOrUpdateParamSubGoodsRequest.class);
        request.setId(subGoodsDTO.getId());

        request.setEid(subGoodsDTO.getEid());
        request.setParamSubId(subGoodsDTO.getParamSubId());
        request.setGoodsInSn(subGoodsDTO.getGoodsInSn());
        request.setParType(ReportParamTypeEnum.ACTIVITY.getCode());
        request.setOpUserId(adminInfo.getCurrentUserId());
        request.setOpTime(new Date());
        return Result.success(reportParamApi.saveOrUpdateParamSubActivityGoods(request));
    }

    @ApiOperation(value = "向阶梯参数添加商品")
    @PostMapping("/saveLadderGoods")
    public Result<Boolean> saveLadderGoods(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid SaveReportLadderGoodsForm form) {
        ReportParamSubDTO paramSubDTO = reportParamApi.queryReportParamSubById(form.getParamSubId());
        if (ObjectUtil.isNull(paramSubDTO)) {
            return Result.failed("子参数类型不存在");
        }
        SaveOrUpdateParamSubGoodsRequest request = PojoUtils.map(form, SaveOrUpdateParamSubGoodsRequest.class);
        request.setParamId(paramSubDTO.getParamId());
        request.setParType(ReportParamTypeEnum.LADDER.getCode());
        request.setOpUserId(adminInfo.getCurrentUserId());
        request.setOpTime(new Date());
        return Result.success(reportParamApi.saveOrUpdateParamSubLadderGoods(request));
    }

    @ApiOperation(value = "修改阶梯参数商品")
    @PostMapping("/updateLadderGoods")
    public Result<Boolean> updateLadderGoods(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody UpdateReportLadderGoodsForm form) {
        ReportParamSubGoodsDTO subGoodsDTO = reportParamApi.queryParamSubGoodsById(form.getId());
        if (ObjectUtil.isNull(subGoodsDTO)) {
            return Result.failed("商品不存在");
        }
        SaveOrUpdateParamSubGoodsRequest request = PojoUtils.map(form, SaveOrUpdateParamSubGoodsRequest.class);
        request.setId(subGoodsDTO.getId());
        request.setParType(subGoodsDTO.getParType());
        request.setEid(subGoodsDTO.getEid());
        request.setParamSubId(subGoodsDTO.getParamSubId());
        request.setGoodsInSn(subGoodsDTO.getGoodsInSn());
        request.setOpUserId(adminInfo.getCurrentUserId());
        request.setOpTime(new Date());
        return Result.success(reportParamApi.saveOrUpdateParamSubLadderGoods(request));
    }

    @ApiOperation(value = "删除活动商品&阶梯品")
    @PostMapping("/deleteParSubGoods")
    public Result<Boolean> deleteParSubGoods(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody DeleteParSubGoodsForm form) {
        Boolean isSucceed = reportParamApi.deleteParSubGoods(form.getId(), adminInfo.getCurrentUserId());
        return Result.success(isSucceed);
    }

    @ApiOperation(value = "导入小三员活动参数")
    @PostMapping("/importSubParXsy")
    @Log(title = "导入小三员活动参数", businessType = BusinessTypeEnum.IMPORT)
    public Result<ImportResultVO> importSubParXsy(@CurrentUser CurrentAdminInfo adminInfo, @RequestParam("file") MultipartFile file) {
        importParSubGoodsHandler.initPar(5L, 9L, ReportParamTypeEnum.ACTIVITY);
        ImportParams params = new ImportParams();
        params.setNeedSave(false);
        params.setNeedVerify(true);
        params.setVerifyHandler(importParSubGoodsHandler);

        InputStream in;
        try {
            in = file.getInputStream();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return Result.failed(ResultCode.UPLOAD_FILE_FAILED);
        }

        ImportResultModel importResultModel;
        try {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put(MyMetaHandler.FIELD_OP_USER_ID, adminInfo.getCurrentUserId());
            importResultModel = ExeclImportUtils.importExcelMore(in, ImportParSubGoodsForm.class, params, importParSubGoodsHandler, paramMap);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed(ResultCode.EXCEL_PARSING_ERROR);
        }

        return Result.success(PojoUtils.map(importResultModel, ImportResultVO.class));
    }

    @ApiOperation(value = "导入员活动1参数")
    @PostMapping("/importSubParFirst")
    @Log(title = "导入员活动1参数", businessType = BusinessTypeEnum.IMPORT)
    public Result<ImportResultVO> importSubParFirst(@CurrentUser CurrentAdminInfo adminInfo, @RequestParam("file") MultipartFile file) {
        importParSubGoodsHandler.initPar(5L, 4L, ReportParamTypeEnum.ACTIVITY);
        ImportParams params = new ImportParams();
        params.setNeedSave(false);
        params.setNeedVerify(true);
        params.setVerifyHandler(importParSubGoodsHandler);

        InputStream in;
        try {
            in = file.getInputStream();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return Result.failed(ResultCode.UPLOAD_FILE_FAILED);
        }

        ImportResultModel importResultModel;
        try {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put(MyMetaHandler.FIELD_OP_USER_ID, adminInfo.getCurrentUserId());
            importResultModel = ExeclImportUtils.importExcelMore(in, ImportParSubGoodsForm.class, params, importParSubGoodsHandler, paramMap);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed(ResultCode.EXCEL_PARSING_ERROR);
        }

        return Result.success(PojoUtils.map(importResultModel, ImportResultVO.class));
    }

    @ApiOperation(value = "导入员活动2参数")
    @PostMapping("/importSubParSecond")
    @Log(title = "导入员活动2参数", businessType = BusinessTypeEnum.IMPORT)
    public Result<ImportResultVO> importSubParSecond(@CurrentUser CurrentAdminInfo adminInfo, @RequestParam("file") MultipartFile file) {
        importParSubGoodsHandler.initPar(5L, 5L, ReportParamTypeEnum.ACTIVITY);
        ImportParams params = new ImportParams();
        params.setNeedSave(false);
        params.setNeedVerify(true);
        params.setVerifyHandler(importParSubGoodsHandler);

        InputStream in;
        try {
            in = file.getInputStream();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return Result.failed(ResultCode.UPLOAD_FILE_FAILED);
        }

        ImportResultModel importResultModel;
        try {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put(MyMetaHandler.FIELD_OP_USER_ID, adminInfo.getCurrentUserId());
            importResultModel = ExeclImportUtils.importExcelMore(in, ImportParSubGoodsForm.class, params, importParSubGoodsHandler, paramMap);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed(ResultCode.EXCEL_PARSING_ERROR);
        }

        return Result.success(PojoUtils.map(importResultModel, ImportResultVO.class));
    }

    @ApiOperation(value = "导入员活动3参数")
    @PostMapping("/importSubParThird")
    @Log(title = "导入员活动3参数", businessType = BusinessTypeEnum.IMPORT)
    public Result<ImportResultVO> importSubParThird(@CurrentUser CurrentAdminInfo adminInfo, @RequestParam("file") MultipartFile file) {
        importParSubGoodsHandler.initPar(5L, 6L, ReportParamTypeEnum.ACTIVITY);
        ImportParams params = new ImportParams();
        params.setNeedSave(false);
        params.setNeedVerify(true);
        params.setVerifyHandler(importParSubGoodsHandler);

        InputStream in;
        try {
            in = file.getInputStream();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return Result.failed(ResultCode.UPLOAD_FILE_FAILED);
        }

        ImportResultModel importResultModel;
        try {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put(MyMetaHandler.FIELD_OP_USER_ID, adminInfo.getCurrentUserId());
            importResultModel = ExeclImportUtils.importExcelMore(in, ImportParSubGoodsForm.class, params, importParSubGoodsHandler, paramMap);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed(ResultCode.EXCEL_PARSING_ERROR);
        }

        return Result.success(PojoUtils.map(importResultModel, ImportResultVO.class));
    }

    @ApiOperation(value = "导入员活动4参数")
    @PostMapping("/importSubParFourth")
    @Log(title = "导入员活动4参数", businessType = BusinessTypeEnum.IMPORT)
    public Result<ImportResultVO> importSubParFourth(@CurrentUser CurrentAdminInfo adminInfo, @RequestParam("file") MultipartFile file) {
        importParSubGoodsHandler.initPar(5L, 7L, ReportParamTypeEnum.ACTIVITY);
        ImportParams params = new ImportParams();
        params.setNeedSave(false);
        params.setNeedVerify(true);
        params.setVerifyHandler(importParSubGoodsHandler);

        InputStream in;
        try {
            in = file.getInputStream();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return Result.failed(ResultCode.UPLOAD_FILE_FAILED);
        }

        ImportResultModel importResultModel;
        try {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put(MyMetaHandler.FIELD_OP_USER_ID, adminInfo.getCurrentUserId());
            importResultModel = ExeclImportUtils.importExcelMore(in, ImportParSubGoodsForm.class, params, importParSubGoodsHandler, paramMap);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed(ResultCode.EXCEL_PARSING_ERROR);
        }

        return Result.success(PojoUtils.map(importResultModel, ImportResultVO.class));
    }

    @ApiOperation(value = "导入员活动5参数")
    @PostMapping("/importSubParFifth")
    @Log(title = "导入员活动5参数", businessType = BusinessTypeEnum.IMPORT)
    public Result<ImportResultVO> importSubParFifth(@CurrentUser CurrentAdminInfo adminInfo, @RequestParam("file") MultipartFile file) {
        importParSubGoodsHandler.initPar(5L, 8L, ReportParamTypeEnum.ACTIVITY);
        ImportParams params = new ImportParams();
        params.setNeedSave(false);
        params.setNeedVerify(true);
        params.setVerifyHandler(importParSubGoodsHandler);

        InputStream in;
        try {
            in = file.getInputStream();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return Result.failed(ResultCode.UPLOAD_FILE_FAILED);
        }

        ImportResultModel importResultModel;
        try {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put(MyMetaHandler.FIELD_OP_USER_ID, adminInfo.getCurrentUserId());
            importResultModel = ExeclImportUtils.importExcelMore(in, ImportParSubGoodsForm.class, params, importParSubGoodsHandler, paramMap);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed(ResultCode.EXCEL_PARSING_ERROR);
        }

        return Result.success(PojoUtils.map(importResultModel, ImportResultVO.class));
    }

    @ApiOperation(value = "导入阶梯1参数")
    @PostMapping("/importLadderFirst")
    @Log(title = "导入阶梯1参数", businessType = BusinessTypeEnum.IMPORT)
    public Result<ImportResultVO> importLadderFirst(@CurrentUser CurrentAdminInfo adminInfo, @RequestParam("file") MultipartFile file) {
        importParLadderHandler.initPar(6L, 1L, ReportParamTypeEnum.LADDER);
        ImportParams params = new ImportParams();
        params.setNeedSave(false);
        params.setNeedVerify(true);
        params.setVerifyHandler(importParLadderHandler);

        InputStream in;
        try {
            in = file.getInputStream();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return Result.failed(ResultCode.UPLOAD_FILE_FAILED);
        }

        ImportResultModel importResultModel;
        try {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put(MyMetaHandler.FIELD_OP_USER_ID, adminInfo.getCurrentUserId());
            importResultModel = ExeclImportUtils.importExcelMore(in, ImportParLadderForm.class, params, importParLadderHandler, paramMap);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed(ResultCode.EXCEL_PARSING_ERROR);
        }

        return Result.success(PojoUtils.map(importResultModel, ImportResultVO.class));
    }

    @ApiOperation(value = "导入阶梯2参数")
    @PostMapping("/importLadderSecond")
    @Log(title = "导入阶梯2参数", businessType = BusinessTypeEnum.IMPORT)
    public Result<ImportResultVO> importLadderSecond(@CurrentUser CurrentAdminInfo adminInfo, @RequestParam("file") MultipartFile file) {
        importParLadderHandler.initPar(6L, 2L, ReportParamTypeEnum.LADDER);
        ImportParams params = new ImportParams();
        params.setNeedSave(false);
        params.setNeedVerify(true);
        params.setVerifyHandler(importParLadderHandler);

        InputStream in;
        try {
            in = file.getInputStream();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return Result.failed(ResultCode.UPLOAD_FILE_FAILED);
        }

        ImportResultModel importResultModel;
        try {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put(MyMetaHandler.FIELD_OP_USER_ID, adminInfo.getCurrentUserId());
            importResultModel = ExeclImportUtils.importExcelMore(in, ImportParLadderForm.class, params, importParLadderHandler, paramMap);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed(ResultCode.EXCEL_PARSING_ERROR);
        }

        return Result.success(PojoUtils.map(importResultModel, ImportResultVO.class));
    }

    @ApiOperation(value = "导入阶梯3参数")
    @PostMapping("/importLadderThird")
    @Log(title = "导入阶梯3参数", businessType = BusinessTypeEnum.IMPORT)
    public Result<ImportResultVO> importLadderThird(@CurrentUser CurrentAdminInfo adminInfo, @RequestParam("file") MultipartFile file) {
        importParLadderHandler.initPar(6L, 3L, ReportParamTypeEnum.LADDER);
        ImportParams params = new ImportParams();
        params.setNeedSave(false);
        params.setNeedVerify(true);
        params.setVerifyHandler(importParLadderHandler);

        InputStream in;
        try {
            in = file.getInputStream();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return Result.failed(ResultCode.UPLOAD_FILE_FAILED);
        }

        ImportResultModel importResultModel;
        try {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put(MyMetaHandler.FIELD_OP_USER_ID, adminInfo.getCurrentUserId());
            importResultModel = ExeclImportUtils.importExcelMore(in, ImportParLadderForm.class, params, importParLadderHandler, paramMap);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed(ResultCode.EXCEL_PARSING_ERROR);
        }

        return Result.success(PojoUtils.map(importResultModel, ImportResultVO.class));
    }

}