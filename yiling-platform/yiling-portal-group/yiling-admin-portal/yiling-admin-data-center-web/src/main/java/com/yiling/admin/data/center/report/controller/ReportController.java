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
import com.yiling.user.enterprise.api.CustomerApi;
import com.yiling.user.enterprise.dto.EnterpriseCustomerDTO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
import com.yiling.admin.data.center.report.form.ImportRebateForm;
import com.yiling.admin.data.center.report.form.QueryReportB2bDetailPageForm;
import com.yiling.admin.data.center.report.form.QueryReportFlowDetailPageForm;
import com.yiling.admin.data.center.report.form.QueryReportPageListForm;
import com.yiling.admin.data.center.report.form.RebateForm;
import com.yiling.admin.data.center.report.form.RejectReportForm;
import com.yiling.admin.data.center.report.form.ReportAdjustForm;
import com.yiling.admin.data.center.report.form.ReportConfirmForm;
import com.yiling.admin.data.center.report.handler.ImportRebateHandler;
import com.yiling.admin.data.center.report.vo.OrderB2bDetailItemVO;
import com.yiling.admin.data.center.report.vo.ReportDetailFlowListItemVO;
import com.yiling.admin.data.center.report.vo.ReportDetailPageVO;
import com.yiling.admin.data.center.report.vo.ReportPageListItemVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.vo.ImportResultVO;
import com.yiling.settlement.report.api.ReportApi;
import com.yiling.settlement.report.dto.ReportDTO;
import com.yiling.settlement.report.dto.ReportDetailB2bDTO;
import com.yiling.settlement.report.dto.ReportDetailFlowDTO;
import com.yiling.settlement.report.dto.ReportLogDTO;
import com.yiling.settlement.report.dto.request.AdjustReportRequest;
import com.yiling.settlement.report.dto.request.ConfirmReportRequest;
import com.yiling.settlement.report.dto.request.QueryReportDetailB2bPageRequest;
import com.yiling.settlement.report.dto.request.QueryReportDetailFlowPageRequest;
import com.yiling.settlement.report.dto.request.QueryReportPageRequest;
import com.yiling.settlement.report.dto.request.RebateByReportRequest;
import com.yiling.settlement.report.dto.request.RejectReportRequest;
import com.yiling.settlement.report.enums.ReportStatusEnum;
import com.yiling.settlement.report.enums.ReportTypeEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterpriseListRequest;
import com.yiling.user.enterprise.enums.EnterpriseAuthStatusEnum;
import com.yiling.user.enterprise.enums.EnterpriseStatusEnum;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentAdminInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: dexi.yao
 * @date: 2022-05-26
 */
@RestController
@RequestMapping("/report")
@Api(tags = "返利总表")
@Slf4j
public class ReportController extends BaseController {

    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    ReportApi reportApi;
    @DubboReference
    UserApi userApi;
    @DubboReference
    CustomerApi customerApi;

    @Autowired
    ImportRebateHandler importRebateHandler;

    @ApiOperation(value = "查询返利总表")
    @GetMapping("/queryReportPageList")
    public Result<Page<ReportPageListItemVO>> queryReportPageList(QueryReportPageListForm form) {
        QueryReportPageRequest request = PojoUtils.map(form, QueryReportPageRequest.class);

        if (!(StringUtils.isEmpty(form.getProvinceCode()) && StringUtils.isEmpty(form.getCityCode()) && StringUtils.isEmpty(form.getRegionCode()))) {
            QueryEnterpriseListRequest eidRequest = new QueryEnterpriseListRequest();
            eidRequest.setCityCode(form.getCityCode());
            eidRequest.setRegionCode(form.getRegionCode());
            eidRequest.setProvinceCode(form.getProvinceCode());
            eidRequest.setStatus(EnterpriseStatusEnum.ENABLED.getCode());
            eidRequest.setAuthStatus(EnterpriseAuthStatusEnum.AUTH_SUCCESS.getCode());
            List<EnterpriseDTO> enterpriseDTOS = enterpriseApi.queryListByArea(eidRequest);
            List<Long> eids = enterpriseDTOS.stream().map(EnterpriseDTO::getId).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(eids)) {
                request.setEidList(eids);
            } else {
                return Result.success(new Page<>());
            }
        }
        if (ObjectUtil.isNotNull(form.getEid()) && ObjectUtil.notEqual(form.getEid(), 0L)) {
            EnterpriseDTO enterpriseDTO = enterpriseApi.getById(form.getEid());
            if (ObjectUtil.isNull(enterpriseDTO)) {
                return Result.success(new Page<>());
            }
            if (CollUtil.isNotEmpty(request.getEidList()) && !request.getEidList().contains(enterpriseDTO.getId())) {
                return Result.success(new Page<>());
            }
            request.setEidList(ListUtil.toList(enterpriseDTO.getId()));
        }
        Page<ReportDTO> page = reportApi.queryReportPage(request);
        Page<ReportPageListItemVO> result = PojoUtils.map(page, ReportPageListItemVO.class);
        if (CollUtil.isEmpty(page.getRecords())) {
            return Result.success(result);
        }
        List<ReportPageListItemVO> records = result.getRecords();
        //查询用户及企业数据
        List<Long> eidList = records.stream().map(ReportPageListItemVO::getEid).distinct().collect(Collectors.toList());
        List<Long> userIdList = records.stream().map(ReportPageListItemVO::getUpdateUser).distinct().collect(Collectors.toList());
        Map<Long, String> entMap = enterpriseApi.listByIds(eidList).stream().collect(Collectors.toMap(EnterpriseDTO::getId, EnterpriseDTO::getName));
        Map<Long, String> userMap = userApi.listByIds(userIdList).stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName));

        records.forEach(e -> {
            e.setEName(entMap.getOrDefault(e.getEid(), ""));
            e.setUpdateUserName(userMap.getOrDefault(e.getUpdateUser(), ""));
        });

        return Result.success(result);
    }

    @ApiOperation(value = "查询b2b总表会员是否有退款，用于b2b总表运营&财务确认前调用 ture时弹窗提示。反之")
    @GetMapping("/queryIsRefund")
    public Result<Boolean> queryIsRefund(@RequestParam Long reportId) {
        ReportDTO reportDTO = reportApi.queryReportById(reportId);
        if (ObjectUtil.isNull(reportDTO) || ObjectUtil.equal(reportDTO.getType(), ReportTypeEnum.FLOW.getCode())) {
            return Result.failed("报表不存在或此报表不是b2b报表");
        }
        return Result.success(reportApi.queryMemberRefundIsAlert(reportDTO.getId(), reportDTO.getEid()));
    }

    @ApiOperation(value = "驳回报表")
    @PostMapping("/reject")
    public Result<Boolean> reject(@RequestBody @Valid RejectReportForm form, @CurrentUser CurrentAdminInfo adminInfo) {
        RejectReportRequest request = PojoUtils.map(form, RejectReportRequest.class);
        if (ObjectUtil.equal(form.getRejectType(), 1)) {
            request.setStatusEnum(ReportStatusEnum.OPERATOR_REJECT);
        } else if (ObjectUtil.equal(form.getRejectType(), 2)) {
            request.setStatusEnum(ReportStatusEnum.FINANCE_REJECT);
        } else {
            request.setStatusEnum(ReportStatusEnum.ADMIN_REJECT);
        }
        request.setOpUserId(adminInfo.getCurrentUserId());
        request.setOpTime(new Date());
        Boolean result = reportApi.rejectReport(request);
        return Result.success(result);
    }

    @ApiOperation(value = "报表确认")
    @PostMapping("/confirm")
    public Result<Boolean> confirm(@RequestBody @Valid ReportConfirmForm form, @CurrentUser CurrentAdminInfo adminInfo) {
        ConfirmReportRequest request = PojoUtils.map(form, ConfirmReportRequest.class);
        if (ObjectUtil.equal(form.getType(), 1)) {
            request.setStatusEnum(ReportStatusEnum.UN_FINANCE_CONFIRM);
        } else {
            request.setStatusEnum(ReportStatusEnum.FINANCE_CONFIRMED);
        }
        request.setOpUserId(adminInfo.getCurrentUserId());
        request.setOpTime(new Date());
        Boolean result = reportApi.confirm(request);
        return Result.success(result);
    }

    @ApiOperation(value = "调整金额")
    @PostMapping("/adjust")
    public Result<Boolean> adjust(@RequestBody @Valid ReportAdjustForm form, @CurrentUser CurrentAdminInfo adminInfo) {
        AdjustReportRequest request = PojoUtils.map(form, AdjustReportRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        request.setOpTime(new Date());
        Boolean result = reportApi.adjust(request);
        return Result.success(result);
    }

    @ApiOperation(value = "查询b2b总表明细")
    @GetMapping("/queryB2bPageList")
    public Result<ReportDetailPageVO<OrderB2bDetailItemVO>> queryB2bOrderPageList(@Valid QueryReportB2bDetailPageForm form) {
        QueryReportDetailB2bPageRequest request = PojoUtils.map(form, QueryReportDetailB2bPageRequest.class);

        //查询总表
        ReportDTO reportDTO = reportApi.queryReportById(form.getReportId());
        if (ObjectUtil.isNull(reportDTO)) {
            return Result.failed("报表不存在");
        }
        ReportDetailPageVO<OrderB2bDetailItemVO> result = PojoUtils.map(reportDTO, ReportDetailPageVO.class);
        //查询日志
        List<ReportLogDTO> logList = reportApi.queryLogList(reportDTO.getId());
        result.setLogList(PojoUtils.map(logList, ReportDetailPageVO.LogVO.class));
        result.setEName(enterpriseApi.getById(reportDTO.getEid()).getName());

        if (StrUtil.isNotEmpty(form.getProvinceCode()) || StrUtil.isNotEmpty(form.getCityCode()) || StrUtil.isNotEmpty(form.getRegionCode())) {
            QueryEnterpriseListRequest eidRequest = new QueryEnterpriseListRequest();
            eidRequest.setCityCode(form.getCityCode());
            eidRequest.setRegionCode(form.getRegionCode());
            eidRequest.setProvinceCode(form.getProvinceCode());
            eidRequest.setStatus(EnterpriseStatusEnum.ENABLED.getCode());
            eidRequest.setAuthStatus(EnterpriseAuthStatusEnum.AUTH_SUCCESS.getCode());
            List<EnterpriseDTO> enterpriseDTOList = enterpriseApi.queryListByArea(eidRequest);
            if (CollectionUtils.isNotEmpty(enterpriseDTOList)) {
                List<Long> eids = enterpriseDTOList.stream().map(EnterpriseDTO::getId).collect(Collectors.toList());
                request.setBuyerEidList(eids);
            } else {

                return Result.success(result);
            }
        }
        if (ObjectUtil.isNotNull(form.getEid()) && ObjectUtil.notEqual(form.getEid(), 0L)) {
            EnterpriseDTO enterpriseDTO = enterpriseApi.getById(form.getEid());
            if (ObjectUtil.isNull(enterpriseDTO)) {
                return Result.success(result);
            }
            if (CollUtil.isNotEmpty(request.getBuyerEidList()) && !request.getBuyerEidList().contains(enterpriseDTO.getId())) {
                return Result.success(result);
            }
            request.setBuyerEidList(ListUtil.toList(enterpriseDTO.getId()));
        }

        Page<ReportDetailB2bDTO> page = reportApi.queryReportDetailB2bPage(request);
        PojoUtils.map(page, result);
        result.setRecords(PojoUtils.map(page.getRecords(), OrderB2bDetailItemVO.class));

        List<OrderB2bDetailItemVO> resultRecords = result.getRecords();
        if (CollUtil.isEmpty(resultRecords)) {
            return Result.success(result);
        }

        List<Long> eidList = resultRecords.stream().map(OrderB2bDetailItemVO::getSellerEid).collect(Collectors.toList());
        eidList.addAll(resultRecords.stream().map(OrderB2bDetailItemVO::getBuyerEid).collect(Collectors.toList()));
        eidList.add(reportDTO.getEid());
        eidList = eidList.stream().distinct().collect(Collectors.toList());

        //查询用户及企业数据
        List<Long> userIdList = logList.stream().map(ReportLogDTO::getCreateUser).distinct().collect(Collectors.toList());
        Map<Long, EnterpriseDTO> entMap = enterpriseApi.listByIds(eidList).stream().collect(Collectors.toMap(EnterpriseDTO::getId, e -> e));
        Map<Long, String> userMap = MapUtil.newHashMap();
        if (CollUtil.isNotEmpty(userIdList)) {
            userMap = userApi.listByIds(userIdList).stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName));
        }

        result.setEName(entMap.get(result.getEid()).getName());
        resultRecords.forEach(e -> {
            e.setSellerEName(entMap.get(e.getSellerEid()).getName());
            e.setBuyerEName(entMap.get(e.getBuyerEid()).getName());
            EnterpriseCustomerDTO customerDTO = customerApi.get(e.getSellerEid(), e.getBuyerEid());
            if (ObjectUtil.isNotNull(customerDTO)){
                e.setErpCustomerName(customerDTO.getCustomerName());
            }
            e.setProvinceName(entMap.get(e.getBuyerEid()).getProvinceName());
            e.setCityName(entMap.get(e.getBuyerEid()).getCityName());
            e.setRegionName(entMap.get(e.getBuyerEid()).getRegionName());
        });
        Map<Long, String> finalUserMap = userMap;
        result.getLogList().forEach(e -> {
            e.setCreateUserName(finalUserMap.getOrDefault(e.getCreateUser(), ""));
        });

        result.setReportId(form.getReportId());
        result.setOrderCount(reportApi.queryB2bRebateOrderCount(request));
        result.setGoodsCount(page.getTotal());
        return Result.success(result);
    }


    @ApiOperation(value = "查询流向总表订单明细")
    @GetMapping("/queryFlowOrderPageList")
    public Result<ReportDetailPageVO<ReportDetailFlowListItemVO>> queryFlowOrderPageList(QueryReportFlowDetailPageForm form) {
        QueryReportDetailFlowPageRequest request = PojoUtils.map(form, QueryReportDetailFlowPageRequest.class);
        //查询总表
        ReportDTO reportDTO = reportApi.queryReportById(form.getReportId());
        if (ObjectUtil.isNull(reportDTO)) {
            return Result.failed("报表不存在");
        } else {
            request.setReportIdList(ListUtil.toList(reportDTO.getId()));
        }
        ReportDetailPageVO<ReportDetailFlowListItemVO> result = PojoUtils.map(reportDTO, ReportDetailPageVO.class);
        result.setEName(enterpriseApi.getById(reportDTO.getEid()).getName());

        Page<ReportDetailFlowDTO> page = reportApi.queryReportDetailFlowPage(request);
        PojoUtils.map(page, result);
        List<ReportDetailFlowDTO> records = page.getRecords();
        List<ReportDetailFlowListItemVO> resultRecords = PojoUtils.map(records, ReportDetailFlowListItemVO.class);
        result.setRecords(resultRecords);

        List<ReportLogDTO> logList = reportApi.queryLogList(reportDTO.getId());
        result.setLogList(PojoUtils.map(logList, ReportDetailPageVO.LogVO.class));

        if (CollUtil.isEmpty(resultRecords)) {
            return Result.success(result);
        }

        List<Long> eidList = resultRecords.stream().map(ReportDetailFlowListItemVO::getSellerEid).collect(Collectors.toList());
        eidList.addAll(resultRecords.stream().map(ReportDetailFlowListItemVO::getSellerEid).collect(Collectors.toList()));
        eidList.add(reportDTO.getEid());
        eidList = eidList.stream().distinct().collect(Collectors.toList());

        //查询用户及企业数据
        List<Long> userIdList = logList.stream().map(ReportLogDTO::getCreateUser).distinct().collect(Collectors.toList());
        Map<Long, EnterpriseDTO> entMap = enterpriseApi.listByIds(eidList).stream().collect(Collectors.toMap(EnterpriseDTO::getId, e -> e));
        Map<Long, String> userMap = userApi.listByIds(userIdList).stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName));

        resultRecords.forEach(e -> {
            e.setSellerEName(entMap.get(e.getSellerEid()).getName());
        });
        result.getLogList().forEach(e -> {
            e.setCreateUserName(userMap.getOrDefault(e.getCreateUser(), ""));
        });
        result.setOrderCount(reportApi.queryFlowRebateOrderCount(request));
        result.setGoodsCount(page.getTotal());
        return Result.success(result);
    }

    @ApiOperation(value = "标识返利")
    @PostMapping("/rebate")
    public Result<Boolean> rebate(@RequestBody @Valid RebateForm form, @CurrentUser CurrentAdminInfo adminInfo) {
        RebateByReportRequest request = PojoUtils.map(form, RebateByReportRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        Boolean isSuccess = reportApi.rebateByReport(request);
        return Result.success(isSuccess);
    }

    @ApiOperation(value = "导入返利")
    @PostMapping("/importRebate")
    @Log(title = "导入返利", businessType = BusinessTypeEnum.IMPORT)
    public Result<ImportResultVO> importRebate(@CurrentUser CurrentAdminInfo adminInfo, @RequestParam("file") MultipartFile file) {
        ImportParams params = new ImportParams();
        params.setNeedSave(false);
        params.setNeedVerify(true);
        params.setVerifyHandler(importRebateHandler);

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
            importResultModel = ExeclImportUtils.importExcelMore(in, ImportRebateForm.class, params, importRebateHandler, paramMap);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed(ResultCode.EXCEL_PARSING_ERROR);
        }

        return Result.success(PojoUtils.map(importResultModel, ImportResultVO.class));
    }


}
