package com.yiling.pricing.goods.api;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.yiling.pricing.goods.dto.GoodsPriceDTO;
import com.yiling.pricing.goods.dto.request.QueryGoodsPriceRequest;

/**
 * 商品定价 API
 *
 * @author xuan.zhou
 * @date 2021/7/9
 */
public interface GoodsPriceApi {

    /**
     * 批量获取商品定价 <br/>
     * 取价优先级：客户定价 > 客户分组定价 > 基价 <br/>
     *
     * @param request
     * @return key：商品ID，value：商品价格
     */
    Map<Long, BigDecimal> queryGoodsPriceMap(QueryGoodsPriceRequest request);

    /**
     * 批量获取B2B商品定价 <br/>
     * 取价优先级：活动价 > 限价 > 客户定价 > 客户分组定价 > 基价 <br/>
     *
     * @param request
     * @return key：商品ID，value：商品价格
     */
    Map<Long, GoodsPriceDTO> queryB2bGoodsPriceInfoMap(QueryGoodsPriceRequest request);

    /**
     * 批量获取B2B商品定价 <br/>
     * 取价优先级：限价 > 客户定价 > 客户分组定价 > 基价 <br/>
     *
     * @param request
     * @return key：商品ID，value：商品价格
     */
    @Deprecated
    Map<Long, BigDecimal> queryB2bGoodsPriceMap(QueryGoodsPriceRequest request);

    /**
     * 批量获取B2B商品定价 <br/>
     * 取价优先级：限价 > 客户定价 > 客户分组定价 > 基价 <br/>
     *
     * @param request
     * @return key：商品ID，value：商品价格
     */
    @Deprecated
    Map<Long, Boolean> queryB2bGoodsPriceShowMap(QueryGoodsPriceRequest request);

    /**
     * 批量查询商品对应客户是否设置限价
     *
     * @param gidList  商品ID列表
     * @param buyerEid 买家企业ID
     * @return key=商品Id value=0不限价1限价
     */
    @Deprecated
    Map<Long, Integer> getB2bGoodsPriceLimitByGids(List<Long> gidList, Long eid, Long buyerEid);
}
