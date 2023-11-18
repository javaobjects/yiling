package com.yiling.cms.evaluate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.yiling.cms.evaluate.dto.QuestionContextDTO;
import com.yiling.cms.evaluate.dto.HealthEvaluateQuestionSelectDTO;
import com.yiling.cms.evaluate.dto.request.HealthEvaluateQuestionDetailRequest;
import com.yiling.cms.evaluate.entity.HealthEvaluateQuestionSelectDO;
import com.yiling.cms.evaluate.dao.HealthEvaluateQuestionSelectMapper;
import com.yiling.cms.evaluate.service.HealthEvaluateQuestionSelectService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 健康测评选择题 服务实现类
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-06
 */
@Service
public class HealthEvaluateQuestionSelectServiceImpl extends BaseServiceImpl<HealthEvaluateQuestionSelectMapper, HealthEvaluateQuestionSelectDO> implements HealthEvaluateQuestionSelectService {

    @Override
    public Boolean add(QuestionContextDTO contextDTO) {
        HealthEvaluateQuestionDetailRequest questionDetailRequest = contextDTO.getQuestionDetailRequest();
        List<HealthEvaluateQuestionSelectDO> selectDOList = Lists.newArrayList();
        questionDetailRequest.getSelectList().forEach(item -> {
            HealthEvaluateQuestionSelectDO selectDO = PojoUtils.map(item, HealthEvaluateQuestionSelectDO.class);
            selectDO.setHealthEvaluateQuestionId(contextDTO.getHealthEvaluateQuestionId());
            selectDO.setHealthEvaluateId(contextDTO.getHealthEvaluateId());
            selectDO.setCreateUser(contextDTO.getOpUserId());
            selectDO.setUpdateUser(contextDTO.getOpUserId());
            selectDOList.add(selectDO);
        });
        this.saveBatch(selectDOList);
        return Boolean.TRUE;
    }

    @Override
    public List<HealthEvaluateQuestionSelectDTO> getByQuestionIdList(List<Long> selectIdList) {
        QueryWrapper<HealthEvaluateQuestionSelectDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(HealthEvaluateQuestionSelectDO::getHealthEvaluateQuestionId, selectIdList);
        List<HealthEvaluateQuestionSelectDO> list = this.list(wrapper);
        return PojoUtils.map(list, HealthEvaluateQuestionSelectDTO.class);
    }

    @Override
    public void delByQuestionId(Long id) {
        QueryWrapper<HealthEvaluateQuestionSelectDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(HealthEvaluateQuestionSelectDO::getHealthEvaluateQuestionId, id);
        HealthEvaluateQuestionSelectDO entity = new HealthEvaluateQuestionSelectDO();

        this.batchDeleteWithFill(entity, wrapper);
    }
}
