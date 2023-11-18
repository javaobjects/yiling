package com.yiling.cms.question.api;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.question.dto.QuestionDTO;
import com.yiling.cms.question.dto.QuestionDetailInfoDTO;
import com.yiling.cms.question.dto.request.QueryQuestionPageRequest;
import com.yiling.cms.question.dto.request.SaveQuestionRequest;
import com.yiling.cms.question.entity.QuestionContentRelationDO;
import com.yiling.cms.question.entity.QuestionDO;
import com.yiling.cms.question.service.QuestionContentRelationService;
import com.yiling.cms.question.service.QuestionService;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollectionUtil;

/**
 * 疑问库查询
 * @author: wang.wei
 * @date: 2022/6/6
 */
@DubboService
public class QuestionApiImpl implements QuestionApi {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private QuestionContentRelationService questionContentRelationService;

    @Override
    public Page<QuestionDTO> listPage(QueryQuestionPageRequest request) {
        Page<QuestionDO> questionDOPage = questionService.listPage(request);
        Page<QuestionDTO> result = PojoUtils.map(questionDOPage, QuestionDTO.class);
        if(CollectionUtil.isNotEmpty(result.getRecords())){
            List<Long> ids = result.getRecords().stream().map(o -> o.getId()).collect(Collectors.toList());
            List<QuestionContentRelationDO> questionContentRelationList = questionContentRelationService.listByQuestionId(ids);
            Map<Long, String> map = questionContentRelationList.stream().collect(Collectors.toMap(QuestionContentRelationDO::getQuestionId, QuestionContentRelationDO::getContent, (k1, k2) -> k2));
            result.getRecords().stream().forEach(s->s.setContent(map.get(s.getId())));
        }
        return result;
    }

    @Override
    public QuestionDetailInfoDTO getQuestionDetail(Long questionId) {

        return questionService.getQuestionDetail(questionId);
    }

    @Override
    public Boolean deleteQuestion(Long questionId, Long OpUserId) {
        return questionService.deleteQuestion(questionId,OpUserId);
    }

    @Override
    public Boolean saveOrUpdateQuestion(SaveQuestionRequest request) {
        return  questionService.saveOrUpdateQuestion(request);
    }

    @Override
    public Integer getNotReplyQuestion(Long userId) {
        QueryWrapper<QuestionDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(QuestionDO :: getToUserId,userId)
                .eq(QuestionDO::getReplyFlag,1);
        return questionService.count(wrapper);
    }
}
