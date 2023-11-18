package com.yiling.user.integral.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.user.integral.entity.IntegralExchangeGoodsMemberDO;
import com.yiling.user.integral.dao.IntegralExchangeGoodsMemberMapper;
import com.yiling.user.integral.service.IntegralExchangeGoodsMemberService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 积分兑换商品指定会员类型表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-06
 */
@Slf4j
@Service
public class IntegralExchangeGoodsMemberServiceImpl extends BaseServiceImpl<IntegralExchangeGoodsMemberMapper, IntegralExchangeGoodsMemberDO> implements IntegralExchangeGoodsMemberService {

    @Override
    public List<Long> getMemberByExchangeGoodsId(Long exchangeGoodsId) {
        LambdaQueryWrapper<IntegralExchangeGoodsMemberDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralExchangeGoodsMemberDO::getExchangeGoodsId, exchangeGoodsId);
        return this.list(wrapper).stream().map(IntegralExchangeGoodsMemberDO::getMemberId).distinct().collect(Collectors.toList());
    }

    @Override
    public Map<Long, List<Long>> getMemberByExchangeGoodsIdList(List<Long> exchangeGoodsIdList) {
        if (CollUtil.isEmpty(exchangeGoodsIdList)) {
            return MapUtil.newHashMap();
        }

        LambdaQueryWrapper<IntegralExchangeGoodsMemberDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(IntegralExchangeGoodsMemberDO::getExchangeGoodsId, exchangeGoodsIdList);
        return this.list(wrapper).stream().collect(Collectors.groupingBy(IntegralExchangeGoodsMemberDO::getExchangeGoodsId, Collectors.mapping(IntegralExchangeGoodsMemberDO::getMemberId, Collectors.toList())));
    }
}
