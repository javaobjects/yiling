package com.yiling.cms.question.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.question.dto.QuestionDTO;
import com.yiling.cms.question.dto.QuestionDetailInfoDTO;
import com.yiling.cms.question.dto.request.QueryQuestionPageRequest;
import com.yiling.cms.question.dto.request.SaveQuestionRequest;

/**
 * 疑问库API
 * @author: wang.wei
 * @date: 2022/6/6
 */
public interface QuestionApi {
    /**
     * 疑问库列表查询
     * @param request
     * @return
     */
    Page<QuestionDTO>  listPage(QueryQuestionPageRequest request);

    /**
     * 获取问题明细
     * @param questionId 问题ID
     * @return
     */
    QuestionDetailInfoDTO getQuestionDetail(Long questionId );

    /**
     * 删除疑问
     * @param questionId 疑问id
     * @param OpUserId 操作人id
     * @return
     */
    Boolean deleteQuestion(Long questionId,Long OpUserId);


    /**
     * 保存编辑
     * @param request
     * @return
     */
    Boolean saveOrUpdateQuestion(SaveQuestionRequest request);

    /**
     * 查询没有医药代表回复的消息
     * @param userId
     * @return
     */
    Integer getNotReplyQuestion(Long userId);
}
