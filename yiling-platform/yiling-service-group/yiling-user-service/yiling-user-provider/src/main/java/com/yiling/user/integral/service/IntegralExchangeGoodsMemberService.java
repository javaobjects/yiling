package com.yiling.user.integral.service;

import java.util.List;
import java.util.Map;

import com.yiling.user.integral.entity.IntegralExchangeGoodsMemberDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 积分兑换商品指定会员类型表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-06
 */
public interface IntegralExchangeGoodsMemberService extends BaseService<IntegralExchangeGoodsMemberDO> {

    /**
     * 根据积分兑换商品ID查询区分用户会员ID
     *
     * @param exchangeGoodsId
     * @return
     */
    List<Long> getMemberByExchangeGoodsId(Long exchangeGoodsId);

    /**
     * 根据积分兑换商品ID集合查询对应的区分用户会员ID
     *
     * @param exchangeGoodsIdList
     * @return key为积分兑换商品表ID，value为会员ID
     */
    Map<Long, List<Long>> getMemberByExchangeGoodsIdList(List<Long> exchangeGoodsIdList);
}
