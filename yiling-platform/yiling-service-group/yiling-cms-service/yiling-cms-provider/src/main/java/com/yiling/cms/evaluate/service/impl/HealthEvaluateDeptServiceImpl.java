package com.yiling.cms.evaluate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.cms.evaluate.entity.HealthEvaluateDeptDO;
import com.yiling.cms.evaluate.dao.HealthEvaluateDeptMapper;
import com.yiling.cms.evaluate.service.HealthEvaluateDeptService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 健康测评关联科室表 服务实现类
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-12
 */
@Service
public class HealthEvaluateDeptServiceImpl extends BaseServiceImpl<HealthEvaluateDeptMapper, HealthEvaluateDeptDO> implements HealthEvaluateDeptService {

    @Override
    public List<Long> getByEvaluateId(Long id) {
        QueryWrapper<HealthEvaluateDeptDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(HealthEvaluateDeptDO::getHealthEvaluateId, id);
        List<HealthEvaluateDeptDO> list = this.list(wrapper);
        return list.stream().map(HealthEvaluateDeptDO::getDeptId).collect(Collectors.toList());
    }
}
