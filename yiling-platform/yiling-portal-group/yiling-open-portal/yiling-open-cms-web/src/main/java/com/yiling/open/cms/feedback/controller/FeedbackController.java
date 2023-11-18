package com.yiling.open.cms.feedback.controller;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.cms.common.enums.BusinessLineEnum;
import com.yiling.cms.feedback.api.FeedbackApi;
import com.yiling.cms.feedback.dto.request.AddFeedbackRequest;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.cms.feedback.form.AddFeedbackForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author: gxl
 * @date: 2022/7/28
 */
@Api(tags = "用户反馈")
@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    @DubboReference
    private FeedbackApi feedbackApi;

    @ApiOperation("添加")
    @PostMapping("add")
    public Result<Boolean> add(@Valid @RequestBody AddFeedbackForm form){
        AddFeedbackRequest request = new AddFeedbackRequest();
        PojoUtils.map(form,request);
        request.setSource(BusinessLineEnum.DOCTOR.getCode());
        request.setOpUserId((long)form.getWxDoctorId());
        feedbackApi.add(request);
        return Result.success(true);
    }
}