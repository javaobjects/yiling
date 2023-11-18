package com.yiling.cms.question.api;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.cms.question.dto.QuestionResourceDTO;
import com.yiling.cms.question.entity.QuestionResourceDO;
import com.yiling.cms.question.service.QuestionResourceService;
import com.yiling.framework.common.util.PojoUtils;

/**
 * 疑问关联信息
 * @author: wang.wei
 * @date: 2022/6/6
 */
@DubboService
public class QuestionResourceApiImpl  implements QuestionResourceApi {

    @Autowired
    private QuestionResourceService questionResourceService;

    @Override
    public List<QuestionResourceDTO> listByQuestionAndType(List<Long>  questionIds, Integer type) {
        List<QuestionResourceDO> questionResourceList = questionResourceService.listByQuestionAndType(questionIds, type);
        return PojoUtils.map(questionResourceList,QuestionResourceDTO.class);
    }
}
