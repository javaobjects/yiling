package com.yiling.cms.question.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.cms.question.dao.QuestionReplyResourceMapper;
import com.yiling.cms.question.entity.QuestionReplyResourceDO;
import com.yiling.cms.question.service.QuestionReplyResourceService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 医药代表回复信息关联表 服务实现类
 * </p>
 *
 * @author wei.wang
 * @date 2022-06-02
 */
@Service
public class QuestionReplyResourceServiceImpl extends BaseServiceImpl<QuestionReplyResourceMapper, QuestionReplyResourceDO> implements QuestionReplyResourceService {

    @Override
    public List<QuestionReplyResourceDO> listByReplyId(Long replyId) {
        QueryWrapper<QuestionReplyResourceDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(QuestionReplyResourceDO :: getReplyId,replyId);
        return list(wrapper);
    }

    @Override
    public List<QuestionReplyResourceDO> listByReplyIds(List<Long> replyIds) {
        QueryWrapper<QuestionReplyResourceDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(QuestionReplyResourceDO :: getReplyId,replyIds);
        return list(wrapper);
    }
}
