package com.yiling.admin.data.center.report.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.data.center.report.form.QueryParamFlowGoodsRelationPageForm;
import com.yiling.admin.data.center.report.form.SaveParamFlowGoodsRelationForm;
import com.yiling.admin.data.center.report.vo.GoodsInfoVO;
import com.yiling.admin.data.center.report.vo.ParamFlowGoodsRelationPageVO;
import com.yiling.dataflow.order.dto.FlowGoodsRelationDTO;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsRelationPageRequest;
import com.yiling.dataflow.order.dto.request.UpdateFlowGoodsRelationRequest;
import com.yiling.dataflow.relation.api.FlowGoodsRelationApi;
import com.yiling.dataflow.relation.api.FlowGoodsRelationEditTaskApi;
import com.yiling.dataflow.relation.enums.FlowGoodsRelationEditTaskSyncStatusEnum;
import com.yiling.dataflow.relation.enums.FlowGoodsRelationLabelEnum;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.bo.GoodsListItemBO;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.goods.medicine.dto.request.QueryGoodsPageListRequest;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.settlement.report.api.ReportApi;
import com.yiling.settlement.report.dto.request.QueryReportGoodsRequest;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.system.api.StaffApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentAdminInfo;
import com.yiling.user.system.bo.Staff;
import com.yiling.user.system.dto.UserDTO;
import com.yiling.user.system.dto.request.QueryStaffListRequest;
import com.yiling.user.system.enums.UserStatusEnum;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 维护参数-商家品与以岭品
 * erp流向商家品和以岭品关系
 *
 * @author: houjie.sun
 * @date: 2022/5/23
 */
@Slf4j
@Api(tags = "维护参数-商家品与以岭品接口")
@RequestMapping("/report/param/flowGoodsRelation")
@RestController
public class ParamFlowGoodsRelationController {

    @DubboReference
    UserApi userApi;
    @DubboReference
    StaffApi staffApi;
    @DubboReference
    FlowGoodsRelationApi flowGoodsRelationApi;
    @DubboReference
    private GoodsApi goodsApi;
    @DubboReference
    private EnterpriseApi enterpriseApi;
    @DubboReference
    private ReportApi reportApi;
    @DubboReference
    private FlowGoodsRelationEditTaskApi flowGoodsRelationEditTaskApi;

//    @Autowired
//    private ImportFlowGoodsRelationHandler importFlowGoodsRelationHandler;

    @ApiOperation(value = "商家品与以岭品列表分页", httpMethod = "POST")
    @PostMapping("/queryPageList")
    public Result<Page<ParamFlowGoodsRelationPageVO>> queryPageList(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid QueryParamFlowGoodsRelationPageForm form) {
        Page<ParamFlowGoodsRelationPageVO> page = new Page();
        if (ObjectUtil.isNull(form) && ObjectUtil.isNull(form.getEid()) && StrUtil.isBlank(form.getYlGoodsName()) && StrUtil.isBlank(form.getOpUserName()) && ObjectUtil.isNull(form.getRelationFlag()) && ObjectUtil.isNull(form.getCreateTimeStart()) && ObjectUtil.isNull(form.getCreateTimeEnd())) {
            return Result.success(page);
        }

        QueryFlowGoodsRelationPageRequest request = PojoUtils.map(form, QueryFlowGoodsRelationPageRequest.class);
        if (StrUtil.isNotBlank(form.getOpUserName())) {
            QueryStaffListRequest queryStaffListRequest = new QueryStaffListRequest();
            queryStaffListRequest.setNameEq(form.getOpUserName());
            queryStaffListRequest.setStatusNe(UserStatusEnum.DEREGISTER.getCode());
            List<Staff> staffList = staffApi.list(queryStaffListRequest);
            if (CollUtil.isNotEmpty(staffList)) {
                List<Long> userIdList = staffList.stream().map(Staff::getId).distinct().collect(Collectors.toList());
                request.setOpUserIdList(userIdList);
            }
        }

        Page<FlowGoodsRelationDTO> pageDTO = flowGoodsRelationApi.page(request);
        if (ObjectUtil.isNotNull(pageDTO) && CollUtil.isNotEmpty(pageDTO.getRecords())) {
            Map<Long, String> userMap = new HashMap<>();
            List<Long> opUserIdList = pageDTO.getRecords().stream().map(FlowGoodsRelationDTO::getOpUserId).distinct().collect(Collectors.toList());
            List<UserDTO> userList = userApi.listByIds(opUserIdList);
            if (CollUtil.isNotEmpty(userList)) {
                userMap = userList.stream().collect(Collectors.toMap(user -> user.getId(), user -> user.getName(), (k1, k2) -> k2));
            }

            List<ParamFlowGoodsRelationPageVO> list = new ArrayList<>();
            for (FlowGoodsRelationDTO dto : pageDTO.getRecords()) {
                ParamFlowGoodsRelationPageVO vo = PojoUtils.map(dto, ParamFlowGoodsRelationPageVO.class);
                String userName = userMap.get(dto.getOpUserId());
                if (StrUtil.isNotBlank(userName)) {
                    vo.setOpUserName(userName);
                }
                list.add(vo);
            }
            page.setRecords(list);
            page.setSize(pageDTO.getSize());
            page.setCurrent(pageDTO.getCurrent());
            page.setTotal(pageDTO.getTotal());
        }
        return Result.success(page);
    }

    @ApiOperation(value = "查询以岭品", httpMethod = "GET")
    @GetMapping("/getYlGoodsList")
    public Result<CollectionObject<GoodsInfoVO>> getYlGoodsList(@CurrentUser CurrentAdminInfo adminInfo, @RequestParam @Valid @NotEmpty String key) {
        QueryGoodsPageListRequest request = new QueryGoodsPageListRequest();
        request.setAuditStatus(GoodsStatusEnum.AUDIT_PASS.getCode());
        request.setSize(100);
        request.setName(key);
        request.setEidList(enterpriseApi.listSubEids(Constants.YILING_EID));
        request.setYilingGoodsFlag(1);
        Page<GoodsListItemBO> page = goodsApi.queryPageListGoods(request);
        List<GoodsInfoVO> list = PojoUtils.map(page.getRecords(), GoodsInfoVO.class);
        return Result.success(new CollectionObject<>(list));
    }


    @Log(title = "维护参数，商家品与以岭品-修改", businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation(value = "修改", httpMethod = "POST")
    @PostMapping("/edit")
    public Result<Boolean> edit(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid SaveParamFlowGoodsRelationForm form) {
        FlowGoodsRelationDTO flowGoodsRelationDTO = flowGoodsRelationApi.getById(form.getId());
        if (ObjectUtil.isNull(flowGoodsRelationDTO)) {
            throw new BusinessException(ResultCode.FAILED, "当前企业当前品对应的以岭品关系不存在，请确认后再操作");
        }

        // 商品关系标签校验
        Integer goodsRelationLabel = form.getGoodsRelationLabel();
        if(ObjectUtil.isNull(goodsRelationLabel) || FlowGoodsRelationLabelEnum.EMPTY.getCode() == goodsRelationLabel.intValue()){
            throw new BusinessException(ResultCode.FAILED, "商品关系标签不能为空、不能为无标签，请选择商品关系标签后再保存");
        }
        Long ylGoodsId = form.getYlGoodsId();
        if(FlowGoodsRelationLabelEnum.YILING.getCode() == goodsRelationLabel.intValue()){
            if(ObjectUtil.isNull(ylGoodsId) || 0 == ylGoodsId.intValue()){
                throw new BusinessException(ResultCode.FAILED, "当商品关系标签为“以岭品”，以岭商品ID不能为空或0，请选择以岭品后再保存");
            }
        }
        // 以岭品id校验
        if (ObjectUtil.isNotNull(ylGoodsId) && 0 != ylGoodsId.intValue()) {
            if (ObjectUtil.equal(ylGoodsId, flowGoodsRelationDTO.getYlGoodsId()) && ObjectUtil.equal(goodsRelationLabel, flowGoodsRelationDTO.getGoodsRelationLabel())) {
                throw new BusinessException(ResultCode.FAILED, "当前企业当前品对应的以岭品未变更，请确认后再操作");
            }
            GoodsDTO goodsDTO = goodsApi.queryInfo(ylGoodsId);
            if (ObjectUtil.isNull(goodsDTO)) {
                throw new BusinessException(ResultCode.FAILED, "以岭商品信息不存在，请确认后再操作");
            }
            if (goodsDTO.getAuditStatus().intValue() != GoodsStatusEnum.AUDIT_PASS.getCode().intValue()) {
                throw new BusinessException(ResultCode.FAILED, "此以岭商品未审核通过，请确认后再操作");
            }
            form.setYlGoodsName(goodsDTO.getName());
            form.setYlGoodsSpecifications(goodsDTO.getSellSpecifications());
        }

        // 校验当前企业是否存在正在同步的以岭品关系修改，因返利报表的计算和驳回维度是按照企业id
        Boolean relationEditTaskFlag = flowGoodsRelationEditTaskApi.existFlowGoodsRelationEditTask(flowGoodsRelationDTO.getId(), FlowGoodsRelationEditTaskSyncStatusEnum.DOING.getCode());
        if(relationEditTaskFlag){
            throw new BusinessException(ResultCode.FAILED, "此以岭商品存在未完成的修改任务，正在处理任务，请稍后再操作");
        }
        // 校验现有报表有没有要修改的品，true-可修改、false-不可修改
        if(ObjectUtil.isNotNull(flowGoodsRelationDTO.getEid()) && !ObjectUtil.equal(0L,flowGoodsRelationDTO.getEid())
                && ObjectUtil.isNotNull(flowGoodsRelationDTO.getYlGoodsId()) && !ObjectUtil.equal(0L,flowGoodsRelationDTO.getYlGoodsId())
                && StrUtil.isNotBlank(flowGoodsRelationDTO.getGoodsInSn())){
            QueryReportGoodsRequest reportRequest = new QueryReportGoodsRequest();
            reportRequest.setEid(flowGoodsRelationDTO.getEid());
            reportRequest.setGoodsInSn(flowGoodsRelationDTO.getGoodsInSn());
            reportRequest.setYlGoodsId(flowGoodsRelationDTO.getYlGoodsId());
            Boolean reportFlag = reportApi.isExitReportByGoods(reportRequest);
            if (!reportFlag) {
                throw new BusinessException(ResultCode.FAILED, "此以岭商品存在未驳回的返利报表，请先驳回再修改以岭品关系");
            }
        }
        // 校验当前企业有没有在生成报表中
        Boolean inProductionReportTaskFlag = reportApi.isInProductionReportTask(flowGoodsRelationDTO.getEid());
        if(inProductionReportTaskFlag){
            throw new BusinessException(ResultCode.FAILED, "当前企业有返利报表正在生成，请稍后再操作");
        }

        UpdateFlowGoodsRelationRequest request = PojoUtils.map(form, UpdateFlowGoodsRelationRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        return Result.success(flowGoodsRelationApi.edit(request));
    }

    //    @ApiOperation(value = "导入以岭商品")
    //    @PostMapping("/importFlowGoodsRelation")
    //    @Log(title = "导入以岭商品", businessType = BusinessTypeEnum.IMPORT)
    //    public Result importFlowGoodsRelation(@CurrentUser CurrentAdminInfo adminInfo, @RequestParam("file") MultipartFile file) {
    //        ImportParams params = new ImportParams();
    //        params.setNeedSave(false);
    //        params.setNeedVerify(true);
    //        params.setVerifyHandler(importFlowGoodsRelationHandler);
    //        params.setKeyIndex(0);
    //
    //        InputStream in;
    //        try {
    //            in = file.getInputStream();
    //        } catch (IOException e) {
    //            log.error(e.getMessage(), e);
    //            return Result.failed(ResultCode.UPLOAD_FILE_FAILED);
    //        }
    //
    //        ImportResultModel importResultModel;
    //        try {
    //            Map<String, Object> paramMap = MapUtil.newHashMap();
    //            paramMap.put(MyMetaHandler.FIELD_OP_USER_ID, adminInfo.getCurrentUserId());
    //            importResultModel = ExeclImportUtils.importExcelMore(in, ImportFlowGoodsRelationForm.class, params, importFlowGoodsRelationHandler, paramMap);
    //        } catch (Exception e) {
    //            log.error(e.getMessage(), e);
    //            return Result.failed(ResultCode.EXCEL_PARSING_ERROR);
    //        }
    //
    //        return Result.success(PojoUtils.map(importResultModel, ImportResultVO.class));
    //    }

}
