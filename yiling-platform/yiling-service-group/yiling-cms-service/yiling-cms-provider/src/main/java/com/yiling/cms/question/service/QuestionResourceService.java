package com.yiling.cms.question.service;

import java.util.List;

import com.yiling.cms.question.dto.QuestionResourceDTO;
import com.yiling.cms.question.entity.QuestionResourceDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 疑问处理库关联表 服务类
 * </p>
 *
 * @author wei.wang
 * @date 2022-06-02
 */
public interface QuestionResourceService extends BaseService<QuestionResourceDO> {

    /**
     * 根据疑问Id获取关联信息
     * @param questionId 疑问id
     * @return
     */
    List<QuestionResourceDO> listByQuestionId(Long questionId);

    /**
     * 关联类型 1-关联文献 2-关联药品 3-关联链接 4-图片说明
     * @param questionIds
     * @param type
     * @return
     */
    List<QuestionResourceDO> listByQuestionAndType(List<Long> questionIds, Integer type);

}
