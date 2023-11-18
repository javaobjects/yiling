package com.yiling.cms.question.api;

import com.yiling.cms.question.dto.QuestionReplyDTO;
import com.yiling.cms.question.dto.request.SaveQuestionReplyRequest;

/**
 * 疑问回复api
 * @author: wang.wei
 * @date: 2022/6/6
 */
public interface QuestionReplyApi {
    /**
     * 查询最后回复人
     * @param questionId
     * @return
     */
    QuestionReplyDTO selectLastReply(Long questionId);

    /**
     * 保存回复
     * @param replyRequest
     * @return
     */
    Boolean saveQuestionReply(SaveQuestionReplyRequest replyRequest);
}
