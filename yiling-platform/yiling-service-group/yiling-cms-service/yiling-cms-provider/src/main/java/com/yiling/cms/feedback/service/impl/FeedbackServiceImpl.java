package com.yiling.cms.feedback.service.impl;


import java.util.Objects;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.feedback.dao.FeedbackMapper;
import com.yiling.cms.feedback.dto.FeedbackDTO;
import com.yiling.cms.feedback.dto.request.AddFeedbackRequest;
import com.yiling.cms.feedback.dto.request.QueryFeedbackPageListRequest;
import com.yiling.cms.feedback.entity.FeedbackDO;
import com.yiling.cms.feedback.service.FeedbackService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;

/**
 * <p>
 * 使用反馈 服务实现类
 * </p>
 *
 * @author gxl
 * @date 2022-07-27
 */
@Service
public class FeedbackServiceImpl extends BaseServiceImpl<FeedbackMapper, FeedbackDO> implements FeedbackService {

    @Override
    public Page<FeedbackDTO> queryPage(QueryFeedbackPageListRequest request) {
        LambdaQueryWrapper<FeedbackDO> wrapper = Wrappers.lambdaQuery();
        wrapper.ge(Objects.nonNull(request.getStartTime()), FeedbackDO::getCreateTime, request.getStartTime());
        if (Objects.nonNull(request.getEndTime())) {
            request.setEndTime(DateUtil.endOfDay(request.getEndTime()));
            wrapper.le(FeedbackDO::getCreateTime, request.getEndTime());
        }
        wrapper.eq(Objects.nonNull(request.getSource()),FeedbackDO::getSource,request.getSource());
        wrapper.orderByDesc(FeedbackDO::getId);
        Page<FeedbackDO> page = this.page(request.getPage(), wrapper);
        return PojoUtils.map(page,FeedbackDTO.class);
    }

    @Override
    public void add(AddFeedbackRequest request) {
        FeedbackDO feedbackDO = new FeedbackDO();
        PojoUtils.map(request,feedbackDO);
        if(CollUtil.isNotEmpty(request.getFeedbackPicList())){
            feedbackDO.setFeedbackPic(String.join(",",request.getFeedbackPicList()));
        }
        this.save(feedbackDO);
    }
}
