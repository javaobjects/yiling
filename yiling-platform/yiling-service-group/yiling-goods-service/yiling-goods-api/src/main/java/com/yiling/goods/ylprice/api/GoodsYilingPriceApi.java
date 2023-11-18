package com.yiling.goods.ylprice.api;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.goods.ylprice.dto.GoodsYilingPriceDTO;
import com.yiling.goods.ylprice.dto.request.AddReportPriceRequest;
import com.yiling.goods.ylprice.dto.request.QueryGoodsYilingPriceRequest;
import com.yiling.goods.ylprice.dto.request.QueryReportPricePageRequest;
import com.yiling.goods.ylprice.dto.request.UpdateReportPriceRequest;

/**
 * @author: houjie.sun
 * @date: 2022/8/8
 */
public interface GoodsYilingPriceApi {

    /**
     * 添加商品价格
     *
     * @param addReportPriceRequest
     */
    GoodsYilingPriceDTO addReportPrice(AddReportPriceRequest addReportPriceRequest);

    /**
     * 商品价格列表
     *
     * @param queryReportPricePageRequest
     * @return
     */
    Page<GoodsYilingPriceDTO> listReportPricePage(QueryReportPricePageRequest queryReportPricePageRequest);

    /**
     * 修改商品价格结束日期
     *
     * @param updateReportPriceRequest
     */
    GoodsYilingPriceDTO updateReportPrice(UpdateReportPriceRequest updateReportPriceRequest);

    /**
     * 获取商销价等价格
     *
     * @param goodsIds
     * @param date
     * @return
     */
    List<GoodsYilingPriceDTO> getPriceParamNameList(List<Long> goodsIds, Date date);

    /**
     * 根据id查询详情
     * @param id
     * @return
     */
    GoodsYilingPriceDTO getById(Long id);

    /**
     * 根据规格id、时间查询商销价
     *
     * @param request
     * @return
     */
    List<GoodsYilingPriceDTO> listBySpecificationIdAndDate(QueryGoodsYilingPriceRequest request);

    /**
     * 根据specId查询查询商销价
     * @return
     */
    List<GoodsYilingPriceDTO> getPriceBySpecificationIdList(List<Long> specificationIdList, Long paramId, Date date);
}
