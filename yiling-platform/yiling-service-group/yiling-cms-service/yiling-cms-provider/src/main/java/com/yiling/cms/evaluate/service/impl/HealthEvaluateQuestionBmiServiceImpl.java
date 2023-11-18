package com.yiling.cms.evaluate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.yiling.cms.evaluate.dao.HealthEvaluateQuestionBmiMapper;
import com.yiling.cms.evaluate.dto.QuestionContextDTO;
import com.yiling.cms.evaluate.dto.HealthEvaluateQuestionBmiDTO;
import com.yiling.cms.evaluate.dto.request.HealthEvaluateQuestionDetailRequest;
import com.yiling.cms.evaluate.entity.HealthEvaluateQuestionBmiDO;
import com.yiling.cms.evaluate.service.HealthEvaluateQuestionBmiService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 健康测评BIM题 服务实现类
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-06
 */
@Service
public class HealthEvaluateQuestionBmiServiceImpl extends BaseServiceImpl<HealthEvaluateQuestionBmiMapper, HealthEvaluateQuestionBmiDO> implements HealthEvaluateQuestionBmiService {

    @Override
    public Boolean add(QuestionContextDTO contextDTO) {
        HealthEvaluateQuestionDetailRequest questionDetailRequest = contextDTO.getQuestionDetailRequest();
        List<HealthEvaluateQuestionBmiDO> bmiDOList = Lists.newArrayList();
        questionDetailRequest.getBmiList().forEach(item -> {
            HealthEvaluateQuestionBmiDO bmiDO = PojoUtils.map(item, HealthEvaluateQuestionBmiDO.class);
            bmiDO.setHealthEvaluateId(contextDTO.getHealthEvaluateId());
            bmiDO.setHealthEvaluateQuestionId(contextDTO.getHealthEvaluateQuestionId());
            bmiDO.setCreateUser(contextDTO.getOpUserId());
            bmiDO.setUpdateUser(contextDTO.getOpUserId());
            bmiDOList.add(bmiDO);
        });
        this.saveBatch(bmiDOList);
        return Boolean.TRUE;
    }

    @Override
    public List<HealthEvaluateQuestionBmiDTO> getByQuestionIdList(List<Long> selectIdList) {
        QueryWrapper<HealthEvaluateQuestionBmiDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(HealthEvaluateQuestionBmiDO::getHealthEvaluateQuestionId, selectIdList);
        List<HealthEvaluateQuestionBmiDO> list = this.list(wrapper);
        return PojoUtils.map(list, HealthEvaluateQuestionBmiDTO.class);
    }

    @Override
    public void delByQuestionId(Long id) {
        QueryWrapper<HealthEvaluateQuestionBmiDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(HealthEvaluateQuestionBmiDO::getHealthEvaluateQuestionId, id);
        HealthEvaluateQuestionBmiDO entity = new HealthEvaluateQuestionBmiDO();
        this.batchDeleteWithFill(entity, wrapper);
    }
}
