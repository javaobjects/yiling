package com.yiling.sjms.wash.controller;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.sjms.api.SjmsUserDatascopeApi;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.dataflow.sjms.enums.OrgDatascopeEnum;
import com.yiling.dataflow.wash.api.FlowMonthWashTaskApi;
import com.yiling.dataflow.wash.dto.FlowMonthWashTaskDTO;
import com.yiling.dataflow.wash.dto.request.QueryFlowMonthWashTaskPageRequest;
import com.yiling.dataflow.wash.dto.request.SaveFlowMonthWashTaskRequest;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.wash.form.QueryFlowMonthWashTaskPageForm;
import com.yiling.sjms.wash.vo.FlowMonthWashTaskListVO;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2023/3/1
 */
@Slf4j
@RestController
@RequestMapping("/flowMonthWashTask")
@Api(tags = "月流向清洗任务表")
public class FlowMonthWashTaskController extends BaseController {

    @DubboReference(timeout = 5 * 1000)
    private FlowMonthWashTaskApi flowMonthWashTaskApi;

    @DubboReference
    private SjmsUserDatascopeApi sjmsUserDatascopeApi;

    @ApiOperation(value = "查询月流向清洗任务列表")
    @PostMapping("/list")
    public Result<Page<FlowMonthWashTaskListVO>> queryFlowMonthWashTaskListPage(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid QueryFlowMonthWashTaskPageForm form) {

        //  查询权限
        SjmsUserDatascopeBO datascopeBO = sjmsUserDatascopeApi.getByEmpId(userInfo.getCurrentUserCode());
        if (datascopeBO == null || OrgDatascopeEnum.NONE.getCode().equals(datascopeBO.getOrgDatascope())) {
            return Result.success(new Page<>());
        }

        QueryFlowMonthWashTaskPageRequest request = PojoUtils.map(form, QueryFlowMonthWashTaskPageRequest.class);
        if (OrgDatascopeEnum.PORTION.getCode().equals(datascopeBO.getOrgDatascope())) {
            request.setCrmEids(datascopeBO.getOrgPartDatascopeBO().getCrmEids());
            request.setProvinceCodes(datascopeBO.getOrgPartDatascopeBO().getProvinceCodes());
        }

        Page<FlowMonthWashTaskListVO> resultPage = PojoUtils.map(flowMonthWashTaskApi.listPage(request), FlowMonthWashTaskListVO.class);
        return Result.success(resultPage);
    }

    @ApiOperation(value = "流向确认")
    @GetMapping("/confirm")
    public Result confirm(@RequestParam("id") Long id) {
        flowMonthWashTaskApi.confirm(id);
        return Result.success();
    }

    @ApiOperation(value = "删除")
    @GetMapping("/delete")
    public Result deleteById(@RequestParam("id") Long id) {
        flowMonthWashTaskApi.deleteTaskAndFlowDataById(id);
        return Result.success();
    }

    @ApiOperation(value = "删除，并自动采集")
    @GetMapping("/deleteAndCollect")
    public Result deleteAndCollect(@RequestParam("id") Long id) {
        FlowMonthWashTaskDTO flowMonthWashTaskDTO = flowMonthWashTaskApi.getById(id);
        if (flowMonthWashTaskDTO == null) {
            // 已结束不可删除
            throw new BusinessException(ResultCode.FAILED, "该流向清洗任务不存在或已删除");
        }

        SaveFlowMonthWashTaskRequest request = new SaveFlowMonthWashTaskRequest();
        request.setFmwcId(flowMonthWashTaskDTO.getFmwcId());
        request.setCrmEnterpriseId(flowMonthWashTaskDTO.getCrmEnterpriseId());
        request.setEid(flowMonthWashTaskDTO.getEid());
        request.setCollectionMethod(flowMonthWashTaskDTO.getCollectionMethod());
        request.setFlowClassify(flowMonthWashTaskDTO.getFlowClassify());
        request.setFlowType(flowMonthWashTaskDTO.getFlowType());

        // 删除
        flowMonthWashTaskApi.deleteTaskAndFlowDataById(id);

        // 重新采集
        flowMonthWashTaskApi.create(request, true);
        return Result.success();
    }

}
