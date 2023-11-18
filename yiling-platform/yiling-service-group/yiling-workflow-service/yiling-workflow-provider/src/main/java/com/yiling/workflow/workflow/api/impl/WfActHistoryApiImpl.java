package com.yiling.workflow.workflow.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.yiling.workflow.workflow.api.WfActHistoryApi;
import com.yiling.workflow.workflow.dto.request.AddWfActHistoryRequest;
import com.yiling.workflow.workflow.service.WfActHistoryService;

/**
 * 流程操作历史记录 API
 *
 * @author: xuan.zhou
 * @date: 2023/2/15
 */
@DubboService
public class WfActHistoryApiImpl implements WfActHistoryApi {

    @Autowired
    WfActHistoryService wfActHistoryService;

    @Override
    public Long addActHistory(AddWfActHistoryRequest request) {
        return wfActHistoryService.addActHistory(request);
    }

    @Override
    public Boolean hasComment(Long commentForwardHistoryId, Long opUserId) {
        Assert.notNull(commentForwardHistoryId, "参数commentForwardHistoryId不能为空");
        Assert.notNull(opUserId, "参数opUserId不能为空");
        return wfActHistoryService.hasComment(commentForwardHistoryId, opUserId);
    }
}
