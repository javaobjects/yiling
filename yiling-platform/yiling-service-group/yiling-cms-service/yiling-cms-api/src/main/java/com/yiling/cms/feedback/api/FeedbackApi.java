package com.yiling.cms.feedback.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.feedback.dto.FeedbackDTO;
import com.yiling.cms.feedback.dto.request.AddFeedbackRequest;
import com.yiling.cms.feedback.dto.request.QueryFeedbackPageListRequest;

/**
 * @author: gxl
 * @date: 2022/7/27
 */
public interface FeedbackApi {
    /**
     * 反馈分页列表
     * @param request
     * @return
     */
    Page<FeedbackDTO> queryPage(QueryFeedbackPageListRequest request);

    /**
     * 添加反馈
     */
    void add(AddFeedbackRequest request);
}
