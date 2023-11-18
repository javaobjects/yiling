package com.yiling.marketing.strategy.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.strategy.dto.request.QueryStrategyActivityAutoGiveGiftPageRequest;
import com.yiling.marketing.strategy.dto.request.SaveActivityAutoGiveGiftRequest;
import com.yiling.marketing.strategy.entity.StrategyActivityAutoGiveGiftDO;

/**
 * <p>
 * 策略满赠续费会员自动赠送赠品表 服务类
 * </p>
 *
 * @author zhangy
 * @date 2022-09-20
 */
public interface StrategyActivityAutoGiveGiftService extends BaseService<StrategyActivityAutoGiveGiftDO> {


    /**
     * 策略满赠续费会员自动赠送赠品表
     *
     * @param request 新增数据
     * @return 成功/失败
     */
    Boolean saveList(SaveActivityAutoGiveGiftRequest request);

    /**
     * 分页查询策略满赠续费会员自动赠送赠品表
     *
     * @param request 查询条件
     * @return 策略满赠续费会员自动赠送赠品
     */
    Page<StrategyActivityAutoGiveGiftDO> pageByCondition(QueryStrategyActivityAutoGiveGiftPageRequest request);

    /**
     * 查询策略满赠续费会员自动赠送赠品表
     *
     * @param request 查询条件
     * @return 策略满赠续费会员自动赠送赠品
     */
    List<StrategyActivityAutoGiveGiftDO> listByCondition(QueryStrategyActivityAutoGiveGiftPageRequest request);

    Boolean deleteByIdList(List<Long> idList, Long opUserId);
}
