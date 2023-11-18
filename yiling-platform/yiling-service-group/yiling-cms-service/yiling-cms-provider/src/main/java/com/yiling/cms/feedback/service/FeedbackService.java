package com.yiling.cms.feedback.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.feedback.dto.FeedbackDTO;
import com.yiling.cms.feedback.dto.request.AddFeedbackRequest;
import com.yiling.cms.feedback.dto.request.QueryFeedbackPageListRequest;
import com.yiling.cms.feedback.entity.FeedbackDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 使用反馈 服务类
 * </p>
 *
 * @author gxl
 * @date 2022-07-27
 */
public interface FeedbackService extends BaseService<FeedbackDO> {

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
