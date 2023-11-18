package com.yiling.cms.question.api;


import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.cms.question.dto.QuestionReplyDTO;
import com.yiling.cms.question.dto.request.SaveQuestionReplyRequest;
import com.yiling.cms.question.entity.QuestionReplyDO;
import com.yiling.cms.question.service.QuestionReplyService;
import com.yiling.framework.common.util.PojoUtils;

/**
 * 疑问回复
 * @author: wang.wei
 * @date: 2022/6/6
 */
@DubboService
public class QuestionReplyApiImpl implements QuestionReplyApi {
    @Autowired
    private QuestionReplyService questionReplyService;

    @Override
    public QuestionReplyDTO selectLastReply(Long questionId) {
        QuestionReplyDO replyDO = questionReplyService.selectLastReply(questionId);
        return PojoUtils.map(replyDO,QuestionReplyDTO.class);
    }

    @Override
    public Boolean saveQuestionReply(SaveQuestionReplyRequest replyRequest) {
        return questionReplyService.saveQuestionReply(replyRequest);
    }
}
