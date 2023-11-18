package com.yiling.goods.medicine.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.dao.GoodsStatisticsMapper;
import com.yiling.goods.medicine.dto.request.StatisticsGoodsSaleRequest;
import com.yiling.goods.medicine.entity.GoodsStatisticsDO;
import com.yiling.goods.medicine.service.GoodsStatisticsService;

/**
 * <p>
 * 商品统计表 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-11-04
 */
@Service
public class GoodsStatisticsServiceImpl extends BaseServiceImpl<GoodsStatisticsMapper, GoodsStatisticsDO> implements GoodsStatisticsService {

    @Override
    public Boolean statisticsGoodsSale(StatisticsGoodsSaleRequest request) {
        QueryWrapper<GoodsStatisticsDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(GoodsStatisticsDO::getGoodsId, request.getGoodsId());

        GoodsStatisticsDO goodsStatisticsDO = this.getOne(wrapper);
        GoodsStatisticsDO statisticsGoods= PojoUtils.map(request,GoodsStatisticsDO.class);
        if (goodsStatisticsDO != null) {
            statisticsGoods.setId(goodsStatisticsDO.getId());
            statisticsGoods.setOpUserId(goodsStatisticsDO.getOpUserId());
            statisticsGoods.setB2bSaleNumber(goodsStatisticsDO.getB2bSaleNumber()+request.getB2bSaleNumber());
        }
        return this.saveOrUpdate(statisticsGoods);
    }
}
