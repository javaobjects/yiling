package com.yiling.marketing.strategy.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.strategy.api.StrategyActivityRecordApi;
import com.yiling.marketing.strategy.dto.StrategyActivityRecordDTO;
import com.yiling.marketing.strategy.dto.StrategyGiftFailDTO;
import com.yiling.marketing.strategy.dto.request.QueryStrategyActivityRecordListRequest;
import com.yiling.marketing.strategy.dto.request.QueryStrategyActivityRecordPageRequest;
import com.yiling.marketing.strategy.entity.StrategyActivityRecordDO;
import com.yiling.marketing.strategy.entity.StrategyGiftFailDO;
import com.yiling.marketing.strategy.service.StrategyActivityRecordService;
import com.yiling.marketing.strategy.service.StrategyGiftFailService;

import lombok.RequiredArgsConstructor;

/**
 * 营销活动记录表Api
 *
 * @author: yong.zhang
 * @date: 2022/9/6
 */
@DubboService
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StrategyActivityRecordApiImpl implements StrategyActivityRecordApi {

    private final StrategyActivityRecordService activityRecordService;

    private final StrategyGiftFailService strategyGiftFailService;

    @Override
    public Page<StrategyActivityRecordDTO> pageList(QueryStrategyActivityRecordPageRequest request) {
        Page<StrategyActivityRecordDO> doPage = activityRecordService.pageList(request);
        return PojoUtils.map(doPage, StrategyActivityRecordDTO.class);
    }

    @Override
    public Integer countRecordByActivityId(Long strategyActivityId) {
        return activityRecordService.countRecordByActivityId(strategyActivityId);
    }

    @Override
    public List<StrategyActivityRecordDTO> listByCondition(QueryStrategyActivityRecordListRequest request) {
        List<StrategyActivityRecordDO> doList = activityRecordService.listByCondition(request);
        return PojoUtils.map(doList, StrategyActivityRecordDTO.class);
    }

    @Override
    public List<StrategyGiftFailDTO> listByRecordId(Long activityRecordId) {
        List<StrategyGiftFailDO> giftFailDOList = strategyGiftFailService.listByRecordId(activityRecordId);
        return PojoUtils.map(giftFailDOList, StrategyGiftFailDTO.class);
    }
}
