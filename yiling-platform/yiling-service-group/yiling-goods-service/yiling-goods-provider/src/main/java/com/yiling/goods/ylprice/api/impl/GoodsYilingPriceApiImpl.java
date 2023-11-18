package com.yiling.goods.ylprice.api.impl;

import java.util.Date;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.ylprice.api.GoodsYilingPriceApi;
import com.yiling.goods.ylprice.dto.GoodsYilingPriceDTO;
import com.yiling.goods.ylprice.dto.request.AddReportPriceRequest;
import com.yiling.goods.ylprice.dto.request.QueryGoodsYilingPriceRequest;
import com.yiling.goods.ylprice.dto.request.QueryReportPricePageRequest;
import com.yiling.goods.ylprice.dto.request.UpdateReportPriceRequest;
import com.yiling.goods.ylprice.entity.GoodsYilingPriceDO;
import com.yiling.goods.ylprice.service.GoodsYilingPriceService;

/**
 * @author: houjie.sun
 * @date: 2022/8/8
 */
@DubboService
public class GoodsYilingPriceApiImpl implements GoodsYilingPriceApi {

    @Autowired
    GoodsYilingPriceService goodsYilingPriceService;

    @Override
    public GoodsYilingPriceDTO addReportPrice(AddReportPriceRequest addReportPriceRequest) {
        return PojoUtils.map(goodsYilingPriceService.add(addReportPriceRequest), GoodsYilingPriceDTO.class);
    }

    @Override
    public Page<GoodsYilingPriceDTO> listReportPricePage(QueryReportPricePageRequest queryReportPricePageRequest) {
        return goodsYilingPriceService.listReportPricePage(queryReportPricePageRequest);
    }

    @Override
    public GoodsYilingPriceDTO updateReportPrice(UpdateReportPriceRequest updateReportPriceRequest) {
        return PojoUtils.map(goodsYilingPriceService.updateReportPrice(updateReportPriceRequest), GoodsYilingPriceDTO.class);
    }

    @Override
    public List<GoodsYilingPriceDTO> getPriceParamNameList(List<Long> goodsIds, Date date) {
        return PojoUtils.map(goodsYilingPriceService.listByGoodsIdAndDate(goodsIds, date), GoodsYilingPriceDTO.class);
    }

    @Override
    public GoodsYilingPriceDTO getById(Long id) {
        return PojoUtils.map(goodsYilingPriceService.getById(id), GoodsYilingPriceDTO.class);
    }

    @Override
    public List<GoodsYilingPriceDTO> listBySpecificationIdAndDate(QueryGoodsYilingPriceRequest request) {
        return PojoUtils.map(goodsYilingPriceService.listBySpecificationIdAndDate(request), GoodsYilingPriceDTO.class);
    }

    @Override
    public List<GoodsYilingPriceDTO> getPriceBySpecificationIdList(List<Long> specificationIdList, Long paramId, Date date) {
        List<GoodsYilingPriceDO> goodsYilingPriceDOList = goodsYilingPriceService.getPriceBySpecificationIdList(specificationIdList, paramId, date);
        return PojoUtils.map(goodsYilingPriceDOList, GoodsYilingPriceDTO.class);
    }


}
