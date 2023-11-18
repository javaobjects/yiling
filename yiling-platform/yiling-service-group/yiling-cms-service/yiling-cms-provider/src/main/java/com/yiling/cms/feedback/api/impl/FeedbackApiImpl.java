package com.yiling.cms.feedback.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.feedback.api.FeedbackApi;
import com.yiling.cms.feedback.dto.FeedbackDTO;
import com.yiling.cms.feedback.dto.request.AddFeedbackRequest;
import com.yiling.cms.feedback.dto.request.QueryFeedbackPageListRequest;
import com.yiling.cms.feedback.service.FeedbackService;

/**
 * @author: gxl
 * @date: 2022/7/27
 */
@DubboService
public class FeedbackApiImpl implements FeedbackApi {

    @Autowired
    private FeedbackService feedbackService;
    @Override
    public Page<FeedbackDTO> queryPage(QueryFeedbackPageListRequest request) {
        return feedbackService.queryPage(request);
    }

    @Override
    public void add(AddFeedbackRequest request) {
        feedbackService.add(request);
    }
}