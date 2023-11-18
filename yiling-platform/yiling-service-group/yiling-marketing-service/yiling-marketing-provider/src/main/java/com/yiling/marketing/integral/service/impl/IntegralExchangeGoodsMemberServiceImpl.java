package com.yiling.marketing.integral.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.marketing.integral.dao.IntegralExchangeGoodsMemberMapper;
import com.yiling.marketing.integral.entity.IntegralExchangeGoodsMemberDO;
import com.yiling.marketing.integral.service.IntegralExchangeGoodsMemberService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 积分兑换商品指定会员类型表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-09
 */
@Service
public class IntegralExchangeGoodsMemberServiceImpl extends BaseServiceImpl<IntegralExchangeGoodsMemberMapper, IntegralExchangeGoodsMemberDO> implements IntegralExchangeGoodsMemberService {

    @Override
    public List<Long> getByExchangeGoodsId(Long exchangeGoodsId) {
        LambdaQueryWrapper<IntegralExchangeGoodsMemberDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralExchangeGoodsMemberDO::getExchangeGoodsId, exchangeGoodsId);
        return this.list(wrapper).stream().map(IntegralExchangeGoodsMemberDO::getMemberId).distinct().collect(Collectors.toList());
    }
}
