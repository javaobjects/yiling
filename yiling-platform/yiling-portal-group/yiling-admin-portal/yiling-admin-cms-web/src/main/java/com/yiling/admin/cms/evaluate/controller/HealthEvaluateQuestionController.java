package com.yiling.admin.cms.evaluate.controller;


import com.yiling.admin.cms.evaluate.form.AddHealthEvaluateQuestionForm;
import com.yiling.admin.cms.evaluate.form.DeleteEvaluateResultForm;
import com.yiling.admin.cms.evaluate.form.EditHealthEvaluateQuestionForm;
import com.yiling.admin.cms.evaluate.vo.HealthEvaluateQuestionVO;
import com.yiling.cms.evaluate.api.HealthEvaluateQuestionApi;
import com.yiling.cms.evaluate.dto.HealthEvaluateQuestionDTO;
import com.yiling.cms.evaluate.dto.request.AddHealthEvaluateQuestionRequest;
import com.yiling.cms.evaluate.dto.request.EditHealthEvaluateQuestionRequest;
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
import java.util.Objects;

/**
 * 健康测评题目 前端控制器
 *
 * @author fan.shen
 * @date 2022-12-07
 */
@Api(tags = "测评题目")
@RestController
@RequestMapping("/cms/healthEvaluateQuestion")
public class HealthEvaluateQuestionController extends BaseController {

    @DubboReference
    private HealthEvaluateQuestionApi healthEvaluateQuestionApi;

    @Log(title = "添加测评题目", businessType = BusinessTypeEnum.INSERT)
    @ApiOperation("添加测评题目")
    @PostMapping("addHealthEvaluateQuestion")
    public Result<Boolean> addHealthEvaluate(@CurrentUser CurrentAdminInfo currentAdminInfo, @RequestBody @Valid AddHealthEvaluateQuestionForm form) {
        AddHealthEvaluateQuestionRequest request = PojoUtils.map(form, AddHealthEvaluateQuestionRequest.class);
        request.setOpUserId(currentAdminInfo.getCurrentUserId());
        return Result.success(healthEvaluateQuestionApi.addHealthEvaluateQuestion(request));
    }

    @ApiOperation("获取测评题目")
    @GetMapping("getQuestionsById")
    public Result<List<HealthEvaluateQuestionVO>> getQuestionsById(@RequestParam Long id) {
        List<HealthEvaluateQuestionDTO> questionDTOList = healthEvaluateQuestionApi.getFullQuestionsByHealthEvaluateId(id);
        if (Objects.isNull(questionDTOList)) {
            return Result.failed("未获取到健康测评题目");
        }
        List<HealthEvaluateQuestionVO> questionVOList = PojoUtils.map(questionDTOList, HealthEvaluateQuestionVO.class);
        return Result.success(questionVOList);
    }

    @ApiOperation("删除测评题目")
    @PostMapping("delQuestionsById")
    public Result<Boolean> delQuestionsById(@RequestBody DeleteEvaluateResultForm form) {
        return Result.success(healthEvaluateQuestionApi.delQuestionsById(form.getId()));
    }

    @ApiOperation("编辑测评题目")
    @PostMapping("editQuestions")
    public Result<Boolean> editQuestions(@CurrentUser CurrentAdminInfo currentAdminInfo, @RequestBody @Valid EditHealthEvaluateQuestionForm form) {
        EditHealthEvaluateQuestionRequest request = PojoUtils.map(form, EditHealthEvaluateQuestionRequest.class);
        request.setOpUserId(currentAdminInfo.getCurrentUserId());
        return Result.success(healthEvaluateQuestionApi.editQuestions(request));
    }

}
