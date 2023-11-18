package com.yiling.marketing.strategy.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.strategy.api.StrategyStageMemberEffectApi;
import com.yiling.marketing.strategy.dto.StrategyStageMemberEffectDTO;
import com.yiling.marketing.strategy.dto.request.AddStrategyStageMemberEffectRequest;
import com.yiling.marketing.strategy.dto.request.DeleteStrategyStageMemberEffectRequest;
import com.yiling.marketing.strategy.dto.request.QueryStrategyStageMemberEffectPageRequest;
import com.yiling.marketing.strategy.entity.StrategyStageMemberEffectDO;
import com.yiling.marketing.strategy.service.StrategyStageMemberEffectService;

import lombok.RequiredArgsConstructor;

/**
 * 策略满赠购买会员方案表Api
 *
 * @author: yong.zhang
 * @date: 2022/9/5
 */
@DubboService
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StrategyStageMemberEffectApiImpl implements StrategyStageMemberEffectApi {

    private final StrategyStageMemberEffectService stageMemberEffectService;

    @Override
    public boolean add(AddStrategyStageMemberEffectRequest request) {
        return stageMemberEffectService.add(request);
    }

    @Override
    public boolean delete(DeleteStrategyStageMemberEffectRequest request) {
        return stageMemberEffectService.delete(request);
    }

    @Override
    public Page<StrategyStageMemberEffectDTO> pageList(QueryStrategyStageMemberEffectPageRequest request) {
        Page<StrategyStageMemberEffectDO> doPage = stageMemberEffectService.pageList(request);
        return PojoUtils.map(doPage, StrategyStageMemberEffectDTO.class);
    }

    @Override
    public Integer countStageMemberEffectByActivityId(Long strategyActivityId) {
        return stageMemberEffectService.countStageMemberEffectByActivityId(strategyActivityId);
    }

    @Override
    public List<StrategyStageMemberEffectDTO> listByActivityId(Long strategyActivityId) {
        List<StrategyStageMemberEffectDO> doList = stageMemberEffectService.listByActivityId(strategyActivityId);
        return PojoUtils.map(doList, StrategyStageMemberEffectDTO.class);
    }
}
