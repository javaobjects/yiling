package com.yiling.marketing.strategy.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.strategy.entity.StrategyGiftFailDO;

/**
 * <p>
 * 策略满赠赠送赠品失败记录表 服务类
 * </p>
 *
 * @author zhangy
 * @date 2022-09-20
 */
public interface StrategyGiftFailService extends BaseService<StrategyGiftFailDO> {


    List<StrategyGiftFailDO> listByRecordId(Long activityRecordId);
}
