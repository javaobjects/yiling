package com.yiling.cms.evaluate.service;

import com.yiling.cms.evaluate.dto.QuestionContextDTO;
import com.yiling.cms.evaluate.dto.HealthEvaluateQuestionBmiDTO;
import com.yiling.cms.evaluate.entity.HealthEvaluateQuestionBmiDO;
import com.yiling.framework.common.base.BaseService;

import java.util.List;

/**
 * <p>
 * 健康测评BIM题 服务类
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-06
 */
public interface HealthEvaluateQuestionBmiService extends BaseService<HealthEvaluateQuestionBmiDO> {

    /**
     * 添加题目
     * @param contextDTO
     * @return
     */
    Boolean add(QuestionContextDTO contextDTO);

    /**
     * 获取bmi
     * @param selectIdList
     * @return
     */
    List<HealthEvaluateQuestionBmiDTO> getByQuestionIdList(List<Long> selectIdList);

    /**
     * 删除记录
     * @param id
     */
    void delByQuestionId(Long id);
}
