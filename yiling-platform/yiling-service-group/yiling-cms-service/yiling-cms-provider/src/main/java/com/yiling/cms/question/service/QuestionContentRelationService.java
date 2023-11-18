package com.yiling.cms.question.service;

import java.util.List;

import com.yiling.cms.question.entity.QuestionContentRelationDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 疑问处理内容关联表 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2022-06-06
 */
public interface QuestionContentRelationService extends BaseService<QuestionContentRelationDO> {
    /**
     * 根据QuestionId查询内容
     * @param questionIds
     * @return
     */
    List<QuestionContentRelationDO> listByQuestionId(List<Long> questionIds);

    /**
     * 根据QuestionId查询内容
     * @param questionId
     * @return
     */
    QuestionContentRelationDO selectOneByQuestionId(Long questionId);
}
