package com.yiling.goods.ylprice.service;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.goods.ylprice.dto.GoodsYilingPriceDTO;
import com.yiling.goods.ylprice.dto.request.AddReportPriceRequest;
import com.yiling.goods.ylprice.dto.request.QueryGoodsYilingPriceRequest;
import com.yiling.goods.ylprice.dto.request.QueryReportPricePageRequest;
import com.yiling.goods.ylprice.dto.request.UpdateReportPriceRequest;
import com.yiling.goods.ylprice.entity.GoodsYilingPriceDO;

/**
 * @author: houjie.sun
 * @date: 2022/8/8
 */
public interface GoodsYilingPriceService extends BaseService<GoodsYilingPriceDO> {

    /**
     * 添加
     *
     * @param addReportPriceRequest
     */
    GoodsYilingPriceDO add(AddReportPriceRequest addReportPriceRequest);

    /**
     * 商品价格列表
     *
     * @param queryReportPricePageRequest
     * @return
     */
    Page<GoodsYilingPriceDTO> listReportPricePage(QueryReportPricePageRequest queryReportPricePageRequest);

    /**
     * 修改
     *
     * @param updateReportPriceRequest
     */
    GoodsYilingPriceDO updateReportPrice(UpdateReportPriceRequest updateReportPriceRequest);

    /**
     *
     * @param goodsIds
     * @param date
     * @return
     */
    List<GoodsYilingPriceDO> listByGoodsIdAndDate(List<Long> goodsIds, Date date);

    /**
     * 根据规格id、时间查询商销价
     *
     * @param request
     * @return
     */
    List<GoodsYilingPriceDO> listBySpecificationIdAndDate(QueryGoodsYilingPriceRequest request);


    List<GoodsYilingPriceDO> getPriceBySpecificationIdList(List<Long> specificationIdList, Long paramId, Date date);
}
