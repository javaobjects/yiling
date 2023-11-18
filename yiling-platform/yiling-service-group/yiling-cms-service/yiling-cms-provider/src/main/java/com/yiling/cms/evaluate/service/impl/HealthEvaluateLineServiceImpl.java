package com.yiling.cms.evaluate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.cms.evaluate.dto.HealthEvaluateLineDTO;
import com.yiling.cms.evaluate.entity.HealthEvaluateLineDO;
import com.yiling.cms.evaluate.dao.HealthEvaluateLineMapper;
import com.yiling.cms.evaluate.service.HealthEvaluateLineService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 健康测评引用业务线表 服务实现类
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-06
 */
@Service
public class HealthEvaluateLineServiceImpl extends BaseServiceImpl<HealthEvaluateLineMapper, HealthEvaluateLineDO> implements HealthEvaluateLineService {

    @Override
    public List<Long> getByHealthEvaluateId(Long id) {
        QueryWrapper<HealthEvaluateLineDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(HealthEvaluateLineDO::getHealthEvaluateId, id);
        List<HealthEvaluateLineDO> list = this.list(wrapper);
        return list.stream().map(HealthEvaluateLineDO::getLineId).collect(Collectors.toList());
    }

    @Override
    public List<Long> getByLineId(Integer lineId) {
        LambdaQueryWrapper<HealthEvaluateLineDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HealthEvaluateLineDO::getLineId, lineId);
        wrapper.select(HealthEvaluateLineDO::getHealthEvaluateId);
        return this.list(wrapper).stream().map(HealthEvaluateLineDO::getHealthEvaluateId).collect(Collectors.toList());
    }

    @Override
    public List<HealthEvaluateLineDTO> getByHealthEvaluateIdList(List<Long> evaluateIdList) {
        QueryWrapper<HealthEvaluateLineDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(HealthEvaluateLineDO::getHealthEvaluateId, evaluateIdList);
        List<HealthEvaluateLineDO> list = this.list(wrapper);
        return PojoUtils.map(list, HealthEvaluateLineDTO.class);
    }
}
