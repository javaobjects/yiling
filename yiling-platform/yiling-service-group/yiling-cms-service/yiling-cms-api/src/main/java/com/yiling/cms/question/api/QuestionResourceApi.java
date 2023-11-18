package com.yiling.cms.question.api;

import java.util.List;

import com.yiling.cms.question.dto.QuestionResourceDTO;

public interface QuestionResourceApi {

    /**
     * 关联类型 1-关联文献 2-关联药品 3-关联链接 4-图片说明
     * @param questionIds
     * @param type
     * @return
     */
    List<QuestionResourceDTO> listByQuestionAndType(List<Long>  questionIds,Integer type);
}
