package com.yiling.goods.medicine.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.goods.medicine.dao.GoodsDiscountRateMapper;
import com.yiling.goods.medicine.entity.GoodsDiscountRateDO;
import com.yiling.goods.medicine.service.GoodsDiscountRateService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;

/**
 * <p>
 * 商品最低折扣比率 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2021-07-21
 */
@Service
public class GoodsDiscountRateServiceImpl extends BaseServiceImpl<GoodsDiscountRateMapper, GoodsDiscountRateDO> implements GoodsDiscountRateService {

    /**
     * 根据客户id和商品id集合查询商品id对应的最低折扣比率
     * @param customerEid 客户ID
     * @param goodsIdList 商品id集合
     * @return 商品最低折扣比率集合
     */
    @Override
    public List<GoodsDiscountRateDO> queryGoodsDiscountRateList(Long customerEid, List<Long> goodsIdList) {
        if(Objects.isNull(customerEid)){
            return ListUtil.toList();
        }
        LambdaQueryWrapper<GoodsDiscountRateDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(GoodsDiscountRateDO::getCustomerEid,customerEid);
        if(CollUtil.isNotEmpty(goodsIdList)){
            lambdaQueryWrapper.in(GoodsDiscountRateDO::getGoodsId, goodsIdList);
        }

        List<GoodsDiscountRateDO> list = list(lambdaQueryWrapper);
        list.forEach(goodsDiscountRateDO -> goodsDiscountRateDO.setMinDiscountRate(
                goodsDiscountRateDO.getMinDiscountRate().divide(BigDecimal.valueOf(100),2, BigDecimal.ROUND_HALF_UP)));

        return list;
    }
}
