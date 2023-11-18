package com.yiling.cms.evaluate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.cms.evaluate.entity.HealthEvaluateDeptDO;
import com.yiling.cms.evaluate.entity.HealthEvaluateDiseaseDO;
import com.yiling.cms.evaluate.dao.HealthEvaluateDiseaseMapper;
import com.yiling.cms.evaluate.service.HealthEvaluateDiseaseService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 健康测评关联疾病 服务实现类
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-06
 */
@Service
public class HealthEvaluateDiseaseServiceImpl extends BaseServiceImpl<HealthEvaluateDiseaseMapper, HealthEvaluateDiseaseDO> implements HealthEvaluateDiseaseService {


    @Override
    public List<Long> getByEvaluateId(Long id) {
        QueryWrapper<HealthEvaluateDiseaseDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(HealthEvaluateDiseaseDO::getHealthEvaluateId, id);
        List<HealthEvaluateDiseaseDO> list = this.list(wrapper);
        return list.stream().map(HealthEvaluateDiseaseDO::getDiseaseId).collect(Collectors.toList());
    }

}
