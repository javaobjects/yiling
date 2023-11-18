package com.yiling.cms.evaluate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.cms.evaluate.dto.HealthEvaluateUserDTO;
import com.yiling.cms.evaluate.dto.request.UserStartEvaluateRequest;
import com.yiling.cms.evaluate.entity.HealthEvaluateUserDO;
import com.yiling.cms.evaluate.dao.HealthEvaluateUserMapper;
import com.yiling.cms.evaluate.service.HealthEvaluateUserService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 健康测评用户表 服务实现类
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-06
 */
@Service
public class HealthEvaluateUserServiceImpl extends BaseServiceImpl<HealthEvaluateUserMapper, HealthEvaluateUserDO> implements HealthEvaluateUserService {

    @Override
    public Map<Long, Long> getTotalUserByEvaluateIdList(List<Long> healthEvaluateIdList) {
        QueryWrapper<HealthEvaluateUserDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(HealthEvaluateUserDO::getHealthEvaluateId, healthEvaluateIdList);
        List<HealthEvaluateUserDO> list = this.list(wrapper);
        return list.stream().collect(Collectors.groupingBy(HealthEvaluateUserDO::getHealthEvaluateId, Collectors.counting()));
    }

    @Override
    public List<HealthEvaluateUserDTO> getUserByEvaluateIdList(List<Long> healthEvaluateIdList) {
        QueryWrapper<HealthEvaluateUserDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(HealthEvaluateUserDO::getHealthEvaluateId, healthEvaluateIdList);
        List<HealthEvaluateUserDO> list = this.list(wrapper);
        return PojoUtils.map(list, HealthEvaluateUserDTO.class);
    }

    @Override
    public Long startEvaluate(UserStartEvaluateRequest request) {
        HealthEvaluateUserDO evaluateUserDO = PojoUtils.map(request, HealthEvaluateUserDO.class);
        this.save(evaluateUserDO);
        return evaluateUserDO.getId();
    }

    @Override
    public void finishEvaluate(Long startEvaluateId) {
        HealthEvaluateUserDO evaluateUserDO = this.getById(startEvaluateId);
        evaluateUserDO.setFinishFlag(1);
        this.updateById(evaluateUserDO);
    }
}
