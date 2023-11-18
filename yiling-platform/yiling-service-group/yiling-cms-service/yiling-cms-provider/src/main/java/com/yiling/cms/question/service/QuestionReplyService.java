package com.yiling.cms.question.service;

import java.util.List;

import com.yiling.cms.question.dto.request.SaveQuestionReplyRequest;
import com.yiling.cms.question.entity.QuestionReplyDO;
import com.yiling.cms.question.entity.QuestionResourceDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 医药代表回复表 服务类
 * </p>
 *
 * @author wei.wang
 * @date 2022-06-02
 */
public interface QuestionReplyService extends BaseService<QuestionReplyDO> {

    /**
     * 根据问题id获取回复信息
     * @param questionId 疑问id
     * @return
     */
    List<QuestionReplyDO> listByQuestionId(Long questionId);

    /**
     * 保存回复
     * @param replyRequest
     * @return
     */
    Boolean saveQuestionReply(SaveQuestionReplyRequest replyRequest);

    /**
     * 查询最后回复人
     * @param questionId
     * @return
     */
    QuestionReplyDO selectLastReply(Long questionId);
}
