package com.yiling.cms.evaluate.service;

import com.yiling.cms.evaluate.dto.QuestionContextDTO;
import com.yiling.cms.evaluate.dto.HealthEvaluateQuestionBlankDTO;
import com.yiling.cms.evaluate.entity.HealthEvaluateQuestionBlankDO;
import com.yiling.framework.common.base.BaseService;

import java.util.List;

/**
 * <p>
 * 健康测评填空题 服务类
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-06
 */
public interface HealthEvaluateQuestionBlankService extends BaseService<HealthEvaluateQuestionBlankDO> {

    /**
     * 添加BMI
     * @param contextDTO
     * @return
     */
    Boolean add(QuestionContextDTO contextDTO);

    /**
     * 获取填空题
     * @param selectIdList
     * @return
     */
    List<HealthEvaluateQuestionBlankDTO> getByQuestionIdList(List<Long> selectIdList);

    /**
     * 删除记录
     * @param id
     */
    void delByQuestionId(Long id);
}
