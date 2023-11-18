package com.yiling.sales.assistant.app.task.controller;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sales.assistant.app.task.form.QueryAccompanyingBillPageForm;
import com.yiling.sales.assistant.app.task.form.QueryTaskAccompanyBillPageForm;
import com.yiling.sales.assistant.app.task.form.SaveAccompanyingBillForm;
import com.yiling.sales.assistant.app.task.vo.AccompanyingBillVO;
import com.yiling.sales.assistant.app.task.vo.TaskAccompanyingBillVO;
import com.yiling.sales.assistant.task.api.AppTaskApi;
import com.yiling.sales.assistant.task.dto.TaskAccompanyingBillDTO;
import com.yiling.sales.assistant.task.dto.app.AccompanyingBillDTO;
import com.yiling.sales.assistant.task.dto.request.QueryTaskAccompanyBillPageRequest;
import com.yiling.sales.assistant.task.dto.request.app.QueryAccompanyingBillPageRequest;
import com.yiling.sales.assistant.task.dto.request.app.SaveAccompanyingBillRequest;
import com.yiling.user.system.bo.CurrentStaffInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 资料上报
 * @author: gxl
 * @date: 2023/1/10
 */
@Api(tags = "资料上报")
@RestController
@RequestMapping("accompanyingBill")
public class AccompanyingBillController extends BaseController {
    @DubboReference
    AppTaskApi appTaskApi;


    @ApiOperation("新增或编辑（accompanyingBillPic 上传图片用）")
    @PostMapping("upload")
    public Result<Boolean> upload(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid SaveAccompanyingBillForm form){
        SaveAccompanyingBillRequest request = new SaveAccompanyingBillRequest();
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setDistributorEid(staffInfo.getCurrentEid());
        PojoUtils.map(form,request);
        appTaskApi.save(request);
        return Result.success(true);
    }

    @ApiOperation("查看详情")
    @GetMapping("getById")
    public Result<AccompanyingBillVO> getById(@CurrentUser CurrentStaffInfo staffInfo,Long id){
        AccompanyingBillDTO accompanyingBillDTO = appTaskApi.getDetailById(id);
        AccompanyingBillVO accompanyingBillVO = new AccompanyingBillVO();
        PojoUtils.map(accompanyingBillDTO,accompanyingBillVO);
        return Result.success(accompanyingBillVO);
    }

    @ApiOperation("资料查看列表")
    @GetMapping("queryPage")
    public Result<Page<AccompanyingBillVO>> queryAccompanyingBillPage(@CurrentUser CurrentStaffInfo staffInfo, QueryAccompanyingBillPageForm form){
        QueryAccompanyingBillPageRequest request = new QueryAccompanyingBillPageRequest();
        PojoUtils.map(form,request);
        request.setUserId(staffInfo.getCurrentUserId());
        Page<AccompanyingBillDTO> accompanyingBillDTOPage = appTaskApi.queryAccompanyingBillPage(request);
        Page<AccompanyingBillVO> accompanyingBillVOPage = PojoUtils.map(accompanyingBillDTOPage,AccompanyingBillVO.class);
        return Result.success(accompanyingBillVOPage);
    }

    @ApiOperation("任务详情（上传资料类型）-记录查看")
    @GetMapping("queryTaskAccompanyBillPage")
    public Result<Page<TaskAccompanyingBillVO>> queryTaskAccompanyBillPage(@Valid QueryTaskAccompanyBillPageForm form){
        QueryTaskAccompanyBillPageRequest queryTaskAccompanyBillPageRequest = new QueryTaskAccompanyBillPageRequest();
        queryTaskAccompanyBillPageRequest.setUserTaskId(form.getUserTaskId());
        Page<TaskAccompanyingBillDTO> taskAccompanyingBillDTOPage = appTaskApi.queryTaskAccompanyBillPage(queryTaskAccompanyBillPageRequest);
        Page<TaskAccompanyingBillVO> taskAccompanyingBillVOPage = PojoUtils.map(taskAccompanyingBillDTOPage,TaskAccompanyingBillVO.class);
        return Result.success(taskAccompanyingBillVOPage);
    }
}