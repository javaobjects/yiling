package com.yiling.cms.evaluate.service;

import com.yiling.cms.evaluate.dto.QuestionContextDTO;
import com.yiling.cms.evaluate.dto.HealthEvaluateQuestionSelectDTO;
import com.yiling.cms.evaluate.entity.HealthEvaluateQuestionSelectDO;
import com.yiling.framework.common.base.BaseService;

import java.util.List;

/**
 * <p>
 * 健康测评选择题 服务类
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-06
 */
public interface HealthEvaluateQuestionSelectService extends BaseService<HealthEvaluateQuestionSelectDO> {

    /**
     * 添加题目
     * @param contextDTO
     * @return
     */
    Boolean add(QuestionContextDTO contextDTO);

    /**
     * 获取选择题
     * @param selectIdList
     * @return
     */
    List<HealthEvaluateQuestionSelectDTO> getByQuestionIdList(List<Long> selectIdList);

    /**
     * 删除
     * @param id
     */
    void delByQuestionId(Long id);
}
