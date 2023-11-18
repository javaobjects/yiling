package com.yiling.admin.cms.evaluate.controller;


import com.yiling.admin.cms.evaluate.form.AddHealthEvaluateResultForm;
import com.yiling.admin.cms.evaluate.form.DeleteEvaluateResultForm;
import com.yiling.admin.cms.evaluate.form.UpdateHealthEvaluateResultForm;
import com.yiling.admin.cms.evaluate.vo.HealthEvaluateResultVO;
import com.yiling.cms.evaluate.api.HealthEvaluateResultApi;
import com.yiling.cms.evaluate.dto.HealthEvaluateResultDTO;
import com.yiling.cms.evaluate.dto.request.AddHealthEvaluateResultRequest;
import com.yiling.cms.evaluate.dto.request.UpdateHealthEvaluateResultRequest;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.system.bo.CurrentAdminInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 健康测评结果 前端控制器
 *
 * @author fan.shen
 * @date 2022-12-08
 */
@Api(tags = "测评结果")
@RestController
@RequestMapping("/cms/healthEvaluateResult")
public class HealthEvaluateResultController extends BaseController {

    @DubboReference
    private HealthEvaluateResultApi healthEvaluateResultApi;

    @Log(title = "添加测评结果", businessType = BusinessTypeEnum.INSERT)
    @ApiOperation("添加测评结果")
    @PostMapping("addHealthEvaluateResult")
    public Result<Boolean> addHealthEvaluateResult(@CurrentUser CurrentAdminInfo currentAdminInfo, @RequestBody @Valid AddHealthEvaluateResultForm form) {
        if (form.getIfResult() == 0) {
            return Result.success();
        }
        AddHealthEvaluateResultRequest request = PojoUtils.map(form, AddHealthEvaluateResultRequest.class);
        request.setOpUserId(currentAdminInfo.getCurrentUserId());
        return Result.success(healthEvaluateResultApi.addHealthEvaluateResult(request));
    }

    @Log(title = "编辑测评结果", businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation("编辑")
    @PostMapping("updateHealthEvaluateResult")
    public Result<Boolean> updateHealthEvaluateResult(@CurrentUser CurrentAdminInfo currentAdminInfo, @RequestBody @Valid UpdateHealthEvaluateResultForm form) {
        UpdateHealthEvaluateResultRequest request = PojoUtils.map(form, UpdateHealthEvaluateResultRequest.class);
        request.setOpUserId(currentAdminInfo.getCurrentUserId());
        return Result.success(healthEvaluateResultApi.updateHealthEvaluateResult(request));
    }

    @ApiOperation("删除测评结果")
    @PostMapping("delHealthEvaluateResultById")
    public Result<Boolean> delHealthEvaluateResultById(@RequestBody DeleteEvaluateResultForm form) {
        return Result.success(healthEvaluateResultApi.delHealthEvaluateResultById(form.getId()));
    }

    @ApiOperation("获取测评结果")
    @GetMapping("getResultListByEvaluateId")
    public Result<List<HealthEvaluateResultVO>> getResultListByEvaluateId(@RequestParam Long evaluateId) {
        List<HealthEvaluateResultDTO> resultDTOList = healthEvaluateResultApi.getResultListByEvaluateId(evaluateId);
        List<HealthEvaluateResultVO> resultVOList = PojoUtils.map(resultDTOList, HealthEvaluateResultVO.class);
        return Result.success(resultVOList);
    }


}
