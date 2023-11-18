package com.yiling.goods.medicine.api.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.goods.inventory.dto.request.AddOrSubtractQtyRequest;
import com.yiling.goods.medicine.api.GoodsHmcApi;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.goods.medicine.dto.GoodsSkuDTO;
import com.yiling.goods.medicine.dto.StandardGoodsBasicDTO;
import com.yiling.goods.medicine.dto.request.HmcSaveOrUpdateGoodsRequest;
import com.yiling.goods.medicine.enums.GoodsLineEnum;
import com.yiling.goods.medicine.service.GoodsService;
import com.yiling.goods.medicine.service.GoodsSkuService;
import com.yiling.goods.medicine.service.HmcGoodsService;
import com.yiling.goods.standard.dto.StandardGoodsInfoDTO;
import com.yiling.goods.standard.dto.StandardGoodsSpecificationDTO;
import com.yiling.goods.standard.dto.request.StandardSpecificationPageRequest;
import com.yiling.goods.standard.service.StandardGoodsSpecificationService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 GoodsHmcApiImpl
 * @描述
 * @创建时间 2022/3/29
 * @修改人 shichen
 * @修改时间 2022/3/29
 **/
@DubboService
@Slf4j
public class GoodsHmcApiImpl implements GoodsHmcApi {
    @Autowired
    HmcGoodsService hmcGoodsService;

    @Autowired
    StandardGoodsSpecificationService standardGoodsSpecificationService;

    @Autowired
    GoodsService GoodsService;

    @Autowired
    GoodsSkuService goodsSkuService;

    @Override
    public Long generateGoods(HmcSaveOrUpdateGoodsRequest request) {
        return hmcGoodsService.generateGoods(request);
    }

    @Override
    public List<StandardGoodsBasicDTO> batchQueryStandardGoodsBasic(List<Long> goodsIds) {
        return hmcGoodsService.batchQueryStandardGoodsBasic(goodsIds);
    }

    @Override
    public List<StandardGoodsBasicDTO> batchQueryStandardGoodsBasicBySpecificationsIds(List<Long> specificationsIds) {
        return hmcGoodsService.batchQueryStandardGoodsBasicBySpecificationsIds(specificationsIds);
    }

    @Override
    public StandardGoodsSpecificationDTO getStandardGoodsSpecification(Long specificationId) {
        return standardGoodsSpecificationService.getStandardGoodsSpecification(specificationId);
    }

    @Override
    public List<StandardGoodsSpecificationDTO> getListStandardGoodsSpecificationByIds(List<Long> specificationIds) {
        return standardGoodsSpecificationService.getListStandardGoodsSpecificationByIds(specificationIds);
    }

    @Override
    public Page<StandardGoodsSpecificationDTO> queryStandardSpecificationPage(StandardSpecificationPageRequest request) {
        return standardGoodsSpecificationService.querySpecificationPage(request);
    }

    @Override
    public List<GoodsDTO> batchQueryGoodsInfo(List<Long> goodsIds) {
        return GoodsService.batchQueryInfo(goodsIds);
    }

    @Override
    public List<GoodsSkuDTO> getGoodsSkuByGid(Long goodsId) {
        List<GoodsSkuDTO> list = goodsSkuService.getGoodsSkuByGoodsId(goodsId);
        return list.stream().filter(sku -> GoodsLineEnum.HMC.getCode().equals(sku.getGoodsLine())).collect(Collectors.toList());
    }

    @Override
    public List<GoodsSkuDTO> getGoodsSkuByGids(List<Long> goodsIds) {
        List<GoodsSkuDTO> list = goodsSkuService.getGoodsSkuByGoodsIds(goodsIds);
        return list.stream().filter(sku -> GoodsLineEnum.HMC.getCode().equals(sku.getGoodsLine())).collect(Collectors.toList());
    }

    @Override
    public Long updateGoodsInventoryBySku(Long skuId, Long inventoryQty,Long opUserId) {
        return hmcGoodsService.updateGoodsInventoryBySku(skuId,inventoryQty,opUserId);
    }

    @Override
    public boolean subtractFrozenQtyAndQty(AddOrSubtractQtyRequest request) {
        return hmcGoodsService.subtractFrozenQtyAndQty(request);
    }

    @Override
    public int batchSubtractFrozenQtyAndQty(List<AddOrSubtractQtyRequest> requestList) {
        return hmcGoodsService.batchSubtractFrozenQtyAndQty(requestList);
    }

    @Override
    public boolean addHmcFrozenQty(AddOrSubtractQtyRequest request) {
        return hmcGoodsService.addHmcFrozenQty(request);
    }

    @Override
    public int batchAddHmcFrozenQty(List<AddOrSubtractQtyRequest> requestList) {
        return hmcGoodsService.batchAddHmcFrozenQty(requestList);
    }

    @Override
    public int subtractHmcFrozenQty(AddOrSubtractQtyRequest request) {
        return hmcGoodsService.subtractHmcFrozenQty(request);
    }

    @Override
    public List<StandardGoodsInfoDTO> queryStandardGoodsByStandardIds(List<Long> standardIds) {
        return hmcGoodsService.queryStandardGoodsByStandardIds(standardIds);
    }
}
