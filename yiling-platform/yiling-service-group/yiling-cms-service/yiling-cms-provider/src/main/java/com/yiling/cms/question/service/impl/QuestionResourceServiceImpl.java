package com.yiling.cms.question.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.cms.question.dao.QuestionResourceMapper;
import com.yiling.cms.question.entity.QuestionResourceDO;
import com.yiling.cms.question.service.QuestionResourceService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 疑问处理库关联表 服务实现类
 * </p>
 *
 * @author wei.wang
 * @date 2022-06-02
 */
@Service
public class QuestionResourceServiceImpl extends BaseServiceImpl<QuestionResourceMapper, QuestionResourceDO> implements QuestionResourceService {

    @Override
    public List<QuestionResourceDO> listByQuestionId(Long questionId) {
        QueryWrapper<QuestionResourceDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(QuestionResourceDO :: getQuestionId,questionId);
        return list(wrapper);
    }

    @Override
    public List<QuestionResourceDO> listByQuestionAndType(List<Long>  questionIds, Integer type) {
        QueryWrapper<QuestionResourceDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(QuestionResourceDO :: getQuestionId,questionIds)
                .eq(QuestionResourceDO :: getType,type);
        return list(wrapper);
    }
}
