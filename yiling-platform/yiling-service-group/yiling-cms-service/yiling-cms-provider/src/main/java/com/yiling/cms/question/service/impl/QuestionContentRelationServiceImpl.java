package com.yiling.cms.question.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.cms.question.entity.QuestionContentRelationDO;
import com.yiling.cms.question.dao.QuestionContentRelationMapper;
import com.yiling.cms.question.service.QuestionContentRelationService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 疑问处理内容关联表 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2022-06-06
 */
@Service
public class QuestionContentRelationServiceImpl extends BaseServiceImpl<QuestionContentRelationMapper, QuestionContentRelationDO> implements QuestionContentRelationService {

    @Override
    public List<QuestionContentRelationDO> listByQuestionId(List<Long> questionIds) {
        QueryWrapper<QuestionContentRelationDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(QuestionContentRelationDO :: getQuestionId,questionIds);
        return list(wrapper);
    }

    @Override
    public QuestionContentRelationDO selectOneByQuestionId(Long questionId) {
        QueryWrapper<QuestionContentRelationDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(QuestionContentRelationDO :: getQuestionId,questionId);
        return getOne(wrapper);
    }
}
