package com.yiling.goods.medicine.service;

import com.yiling.framework.common.base.BaseService;
import com.yiling.goods.medicine.dto.request.StatisticsGoodsSaleRequest;
import com.yiling.goods.medicine.entity.GoodsStatisticsDO;

/**
 * <p>
 * 商品统计表 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-11-04
 */
public interface GoodsStatisticsService extends BaseService<GoodsStatisticsDO> {

    /**
     * 商品销量统计接口（每天只会统计一次进去，以第一次为准）
     * @param request
     * @return
     */
    Boolean statisticsGoodsSale(StatisticsGoodsSaleRequest request);

}
