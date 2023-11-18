package com.yiling.user.integral.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.integral.bo.IntegralBehaviorBO;
import com.yiling.user.integral.dto.IntegralBehaviorDTO;
import com.yiling.user.integral.dto.request.QueryIntegralBehaviorRequest;
import com.yiling.user.integral.entity.IntegralBehaviorDO;
import com.yiling.user.integral.dao.IntegralBehaviorMapper;
import com.yiling.user.integral.service.IntegralBehaviorPlatformService;
import com.yiling.user.integral.service.IntegralBehaviorService;
import com.yiling.framework.common.base.BaseServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 积分行为表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-29
 */
@Slf4j
@Service
public class IntegralBehaviorServiceImpl extends BaseServiceImpl<IntegralBehaviorMapper, IntegralBehaviorDO> implements IntegralBehaviorService {

    @Autowired
    IntegralBehaviorPlatformService integralBehaviorPlatformService;

    @Override
    public Page<IntegralBehaviorBO> queryListPage(QueryIntegralBehaviorRequest request) {
        Page<IntegralBehaviorDTO> behaviorDTOPage = baseMapper.queryListPage(request.getPage(), request);

        Page<IntegralBehaviorBO> page = PojoUtils.map(behaviorDTOPage, IntegralBehaviorBO.class);
        page.getRecords().forEach(integralBehaviorBO -> {
            List<Integer> platformList = integralBehaviorPlatformService.getByBehaviorId(integralBehaviorBO.getId());
            integralBehaviorBO.setPlatformList(platformList);
        });
        return page;
    }

    @Override
    public IntegralBehaviorDTO getByName(String name) {
        LambdaQueryWrapper<IntegralBehaviorDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralBehaviorDO::getName, name);
        wrapper.last("limit 1");
        return PojoUtils.map(this.getOne(wrapper), IntegralBehaviorDTO.class);
    }

}
