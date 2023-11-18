package com.yiling.search.goods.api;

import java.util.List;

import com.yiling.framework.common.base.EsAggregationDTO;
import com.yiling.goods.medicine.dto.GoodsInfoDTO;
import com.yiling.search.goods.dto.request.EsActivityGoodsSearchRequest;
import com.yiling.search.goods.dto.request.EsGoodsInventoryIndexRequest;
import com.yiling.search.goods.dto.request.EsGoodsSearchRequest;

/**
 * @author: fei.wu <br>
 * @date: 2021/6/17 <br>
 */

public interface EsGoodsSearchApi {

    /**
     * Elasticsearch search goods
     * @param request
     * @return
     */
    EsAggregationDTO<GoodsInfoDTO> searchGoods(EsGoodsSearchRequest request);

    /**
     * 搜素商品建议
     */
    List<String> searchGoodsSuggest(EsGoodsSearchRequest request);

    /**
     * Elasticsearch search goods
     * @param request
     * @return
     */
    EsAggregationDTO<GoodsInfoDTO> searchActivityGoods(EsActivityGoodsSearchRequest request);

    /**
     * 搜素活动商品建议
     */
    List<String> searchActivityGoodsSuggest(EsActivityGoodsSearchRequest request);

    /**
     * 刷新某一个库存信息
     * @param request
     * @return
     */
    Boolean updateQty(EsGoodsInventoryIndexRequest request);

    /**
     * 刷新库存可用标识信息
     * @param request
     * @return
     */
    Boolean updateQtyFlag(EsGoodsInventoryIndexRequest request);

}
