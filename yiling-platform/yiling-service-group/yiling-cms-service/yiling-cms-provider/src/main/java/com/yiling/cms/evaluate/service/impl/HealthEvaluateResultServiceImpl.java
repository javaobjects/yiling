package com.yiling.cms.evaluate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.yiling.cms.evaluate.dto.HealthEvaluateResultDTO;
import com.yiling.cms.evaluate.dto.request.AddHealthEvaluateResultRequest;
import com.yiling.cms.evaluate.dto.request.UpdateHealthEvaluateResultRequest;
import com.yiling.cms.evaluate.entity.HealthEvaluateResultDO;
import com.yiling.cms.evaluate.dao.HealthEvaluateResultMapper;
import com.yiling.cms.evaluate.service.HealthEvaluateResultService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 健康测评结果表 服务实现类
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-06
 */
@Service
public class HealthEvaluateResultServiceImpl extends BaseServiceImpl<HealthEvaluateResultMapper, HealthEvaluateResultDO> implements HealthEvaluateResultService {

    @Override
    public Boolean addHealthEvaluateResult(AddHealthEvaluateResultRequest request) {
        List<HealthEvaluateResultDO> resultDOList = Lists.newArrayList();
        request.getHealthEvaluateResultList().forEach(item -> {
            HealthEvaluateResultDO resultDO = PojoUtils.map(item, HealthEvaluateResultDO.class);
            resultDO.setCreateUser(request.getOpUserId());
            resultDO.setUpdateUser(request.getOpUserId());
            resultDOList.add(resultDO);
        });
        return this.saveBatch(resultDOList);
    }

    @Override
    public Boolean updateHealthEvaluateResult(UpdateHealthEvaluateResultRequest request) {
        HealthEvaluateResultDO resultDO = PojoUtils.map(request, HealthEvaluateResultDO.class);
        return this.updateById(resultDO);
    }

    @Override
    public Boolean delHealthEvaluateResultById(Long id) {
        HealthEvaluateResultDO entity = new HealthEvaluateResultDO();
        entity.setId(id);
        int i = this.deleteByIdWithFill(entity);
        return i > 0 ? Boolean.TRUE : Boolean.FALSE;
    }

    @Override
    public List<HealthEvaluateResultDTO> getResultListByEvaluateId(Long evaluateId) {
        QueryWrapper<HealthEvaluateResultDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(HealthEvaluateResultDO::getHealthEvaluateId, evaluateId);
        List<HealthEvaluateResultDO> list = this.list(wrapper);
        return PojoUtils.map(list, HealthEvaluateResultDTO.class);
    }
}
