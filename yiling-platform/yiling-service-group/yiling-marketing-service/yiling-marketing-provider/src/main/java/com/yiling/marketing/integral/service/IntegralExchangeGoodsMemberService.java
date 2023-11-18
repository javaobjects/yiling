package com.yiling.marketing.integral.service;

import java.util.List;

import com.yiling.marketing.integral.entity.IntegralExchangeGoodsMemberDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 积分兑换商品指定会员类型表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-09
 */
public interface IntegralExchangeGoodsMemberService extends BaseService<IntegralExchangeGoodsMemberDO> {

    /**
     * 根据积分兑换商品ID查询区分用户会员ID
     *
     * @param exchangeGoodsId
     * @return
     */
    List<Long> getByExchangeGoodsId(Long exchangeGoodsId);

}
