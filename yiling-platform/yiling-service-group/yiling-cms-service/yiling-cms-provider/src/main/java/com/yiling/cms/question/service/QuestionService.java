package com.yiling.cms.question.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.question.dto.QuestionDetailInfoDTO;
import com.yiling.cms.question.dto.request.QueryQuestionPageRequest;
import com.yiling.cms.question.dto.request.SaveQuestionRequest;
import com.yiling.cms.question.entity.QuestionDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 疑问处理库 服务类
 * </p>
 *
 * @author wei.wang
 * @date 2022-06-02
 */
public interface QuestionService extends BaseService<QuestionDO> {

    /**
     * 保存编辑
     * @param request
     * @return
     */
    Boolean saveOrUpdateQuestion(SaveQuestionRequest request);

    /**
     * 删除数据
     * @param questionId
     * @param OpUserId
     * @return
     */
    Boolean deleteQuestion(Long questionId,Long OpUserId);

    /**
     * 获取分页列表数据
     * @param request
     * @return
     */
    Page<QuestionDO> listPage(QueryQuestionPageRequest request);

    /**
     * 获取疑问明细
     * @param questionId 问题ID
     * @return
     */
    QuestionDetailInfoDTO getQuestionDetail(Long questionId );

    /**
     * 添加阅读量
     * @param questionId
     * @param count
     * @return
     */
    Boolean addViewCount(Long questionId, Integer count);
}
