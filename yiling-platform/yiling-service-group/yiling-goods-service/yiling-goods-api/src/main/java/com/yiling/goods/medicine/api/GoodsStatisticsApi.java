package com.yiling.goods.medicine.api;

import com.yiling.goods.medicine.dto.request.StatisticsGoodsSaleRequest;

/**
 * @author: shuang.zhang
 * @date: 2021/5/20
 */
public interface GoodsStatisticsApi {

    /**
     * 商品销量统计接口（每天只会统计一次进去，以第一次为准）
     * @param request
     * @return
     */
    Boolean statisticsGoodsSale(StatisticsGoodsSaleRequest request);

}
