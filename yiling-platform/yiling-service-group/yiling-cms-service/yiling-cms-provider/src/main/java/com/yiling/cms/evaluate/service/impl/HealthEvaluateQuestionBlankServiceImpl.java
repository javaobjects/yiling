package com.yiling.cms.evaluate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.cms.evaluate.dao.HealthEvaluateQuestionBlankMapper;
import com.yiling.cms.evaluate.dto.QuestionContextDTO;
import com.yiling.cms.evaluate.dto.HealthEvaluateQuestionBlankDTO;
import com.yiling.cms.evaluate.dto.request.HealthEvaluateQuestionDetailRequest;
import com.yiling.cms.evaluate.entity.HealthEvaluateQuestionBlankDO;
import com.yiling.cms.evaluate.service.HealthEvaluateQuestionBlankService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 健康测评填空题 服务实现类
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-06
 */
@Service
public class HealthEvaluateQuestionBlankServiceImpl extends BaseServiceImpl<HealthEvaluateQuestionBlankMapper, HealthEvaluateQuestionBlankDO> implements HealthEvaluateQuestionBlankService {

    @Override
    public Boolean add(QuestionContextDTO contextDTO) {
        HealthEvaluateQuestionDetailRequest questionDetailRequest = contextDTO.getQuestionDetailRequest();
        HealthEvaluateQuestionBlankDO blankDO = PojoUtils.map(questionDetailRequest.getBlank(), HealthEvaluateQuestionBlankDO.class);
        blankDO.setHealthEvaluateQuestionId(contextDTO.getHealthEvaluateQuestionId());
        blankDO.setHealthEvaluateId(contextDTO.getHealthEvaluateId());
        blankDO.setCreateUser(contextDTO.getOpUserId());
        blankDO.setUpdateUser(contextDTO.getOpUserId());
        this.save(blankDO);
        return Boolean.TRUE;
    }

    @Override
    public List<HealthEvaluateQuestionBlankDTO> getByQuestionIdList(List<Long> selectIdList) {
        QueryWrapper<HealthEvaluateQuestionBlankDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(HealthEvaluateQuestionBlankDO::getHealthEvaluateQuestionId, selectIdList);
        List<HealthEvaluateQuestionBlankDO> list = this.list(wrapper);
        return PojoUtils.map(list, HealthEvaluateQuestionBlankDTO.class);
    }

    @Override
    public void delByQuestionId(Long id) {
        QueryWrapper<HealthEvaluateQuestionBlankDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(HealthEvaluateQuestionBlankDO::getHealthEvaluateQuestionId, id);
        HealthEvaluateQuestionBlankDO entity = new HealthEvaluateQuestionBlankDO();
        this.batchDeleteWithFill(entity, wrapper);
    }
}
