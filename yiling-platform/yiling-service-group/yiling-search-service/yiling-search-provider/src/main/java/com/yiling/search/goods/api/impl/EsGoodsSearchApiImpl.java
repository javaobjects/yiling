package com.yiling.search.goods.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.base.EsAggregationDTO;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.goods.medicine.dto.GoodsInfoDTO;
import com.yiling.search.goods.api.EsGoodsSearchApi;
import com.yiling.search.goods.dto.request.EsActivityGoodsSearchRequest;
import com.yiling.search.goods.dto.request.EsGoodsInventoryIndexRequest;
import com.yiling.search.goods.dto.request.EsGoodsSearchRequest;
import com.yiling.search.goods.service.EsGoodsSearchService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: fei.wu <br>
 * @date: 2021/6/17 <br>
 */

@DubboService
@Slf4j
public class EsGoodsSearchApiImpl implements EsGoodsSearchApi {

    @Autowired
    EsGoodsSearchService esGoodsSearchService;

    @Override
    public EsAggregationDTO<GoodsInfoDTO> searchGoods(EsGoodsSearchRequest request){
        try {
            request.setCollapseField(null);
            return esGoodsSearchService.searchGoods(request);
        } catch (Exception e) {
            log.error("[EsGoodsSearchApiImpl][searchGoods] 异常！"+e.getMessage(),e);
            throw new BusinessException (ResultCode.FAILED);
        }
    }

    @Override
    public List<String> searchGoodsSuggest(EsGoodsSearchRequest request) {
        try {
            return esGoodsSearchService.searchGoodsSuggest(request);
        } catch (Exception e) {
            log.error("[EsGoodsSearchApiImpl][searchGoodsSuggest] 异常！"+e.getMessage(),e);
            throw new BusinessException (ResultCode.FAILED);
        }
    }

    @Override
    public EsAggregationDTO<GoodsInfoDTO> searchActivityGoods(EsActivityGoodsSearchRequest request) {
        try {
            request.setCollapseField(null);
            return esGoodsSearchService.searchActivityGoods(request);
        } catch (Exception e) {
            log.error("[EsGoodsSearchApiImpl][searchActivityGoods] 异常！"+e.getMessage(),e);
            throw new BusinessException (ResultCode.FAILED);
        }
    }

    @Override
    public List<String> searchActivityGoodsSuggest(EsActivityGoodsSearchRequest request) {
        try {
            return esGoodsSearchService.searchActivityGoodsSuggest(request);
        } catch (Exception e) {
            log.error("[EsGoodsSearchApiImpl][searchActivityGoodsSuggest] 异常！"+e.getMessage(),e);
            throw new BusinessException (ResultCode.FAILED);
        }
    }

    @Override
    public Boolean updateQty(EsGoodsInventoryIndexRequest request) {
        try {
            return esGoodsSearchService.updateQty(request);
        } catch (Exception e) {
            log.error("[EsGoodsSearchApiImpl][updateQty] 异常！"+e.getMessage(),e);
            throw new BusinessException (ResultCode.FAILED);
        }
    }

    @Override
    public Boolean updateQtyFlag(EsGoodsInventoryIndexRequest request) {
        try {
            return esGoodsSearchService.updateQtyFlag(request);
        } catch (Exception e) {
            log.error("[EsGoodsSearchApiImpl][updateQtyFlag] 异常！"+e.getMessage(),e);
            throw new BusinessException (ResultCode.FAILED);
        }
    }
}
