package com.yiling.goods.medicine.service.impl;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.inventory.dto.request.AddOrSubtractQtyRequest;
import com.yiling.goods.inventory.dto.request.SaveInventoryRequest;
import com.yiling.goods.inventory.entity.InventoryDO;
import com.yiling.goods.inventory.enums.InventoryErrorCode;
import com.yiling.goods.inventory.service.InventoryService;
import com.yiling.goods.medicine.dto.GoodsSkuDTO;
import com.yiling.goods.medicine.dto.StandardGoodsBasicDTO;
import com.yiling.goods.medicine.dto.request.HmcSaveOrUpdateGoodsRequest;
import com.yiling.goods.medicine.dto.request.SaveOrUpdateGoodsSkuRequest;
import com.yiling.goods.medicine.entity.GoodsDO;
import com.yiling.goods.medicine.entity.GoodsSkuDO;
import com.yiling.goods.medicine.enums.GoodsLineEnum;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.goods.medicine.service.GoodsService;
import com.yiling.goods.medicine.service.GoodsSkuService;
import com.yiling.goods.medicine.service.HmcGoodsService;
import com.yiling.goods.standard.dto.StandardGoodsBasicInfoDTO;
import com.yiling.goods.standard.dto.StandardGoodsInfoDTO;
import com.yiling.goods.standard.entity.StandardGoodsDO;
import com.yiling.goods.standard.entity.StandardGoodsPicDO;
import com.yiling.goods.standard.entity.StandardGoodsSpecificationDO;
import com.yiling.goods.standard.service.StandardGoodsPicService;
import com.yiling.goods.standard.service.StandardGoodsService;
import com.yiling.goods.standard.service.StandardGoodsSpecificationService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;

/**
 * @author shichen
 * @类名 HmcGoodsServiceImpl
 * @描述
 * @创建时间 2022/3/29
 * @修改人 shichen
 * @修改时间 2022/3/29
 **/
@Service
public class HmcGoodsServiceImpl implements HmcGoodsService {
    @Autowired
    GoodsService goodsService;

    @Autowired
    StandardGoodsSpecificationService standardGoodsSpecificationService;

    @Autowired
    StandardGoodsService standardGoodsService;

    @Autowired
    GoodsSkuService goodsSkuService;

    @Autowired
    StandardGoodsPicService standardGoodsPicService;

    @Autowired
    InventoryService inventoryService;



    @Override
    @Transactional
    public Long generateGoods(HmcSaveOrUpdateGoodsRequest request) {
        StandardGoodsSpecificationDO standardGoodsSpecification = standardGoodsSpecificationService.getById(request.getSellSpecificationsId());
        if(ObjectUtil.isNull(standardGoodsSpecification)){
            throw new BusinessException(ResultCode.FAILED,"选择的标准库规格不存在");
        }
        StandardGoodsDO standardGoodsDO = standardGoodsService.getById(standardGoodsSpecification.getStandardId());
        if(ObjectUtil.isNull(standardGoodsDO)){
            throw new BusinessException(ResultCode.FAILED,"选择的标准库商品不存在");
        }

        Long goodsId = goodsService.queryInfoBySpecIdAndEid(request.getSellSpecificationsId(), request.getEid());
        if(0L<goodsId){
            List<GoodsSkuDTO> skuDTOList = goodsSkuService.getGoodsSkuByGidAndGoodsLine(goodsId, GoodsLineEnum.HMC);
            if(CollectionUtils.isEmpty(skuDTOList)){
                SaveOrUpdateGoodsSkuRequest saveOrUpdateGoodsSkuRequest = new SaveOrUpdateGoodsSkuRequest();
                saveOrUpdateGoodsSkuRequest.setGoodsId(goodsId);
                saveOrUpdateGoodsSkuRequest.setGoodsLine(GoodsLineEnum.HMC.getCode());
                saveOrUpdateGoodsSkuRequest.setEid(request.getEid());
                saveOrUpdateGoodsSkuRequest.setOpUserId(request.getOpUserId());
                saveOrUpdateGoodsSkuRequest.setPackageNumber(1L);
                goodsSkuService.saveOrUpdateByNumber(saveOrUpdateGoodsSkuRequest);
            }
            return goodsId;
        }
        //保存商品
        GoodsDO goodsDO = PojoUtils.map(request, GoodsDO.class);
        goodsDO.setStandardId(standardGoodsDO.getId());
        goodsDO.setGoodsType(standardGoodsDO.getGoodsType());
        goodsDO.setName(standardGoodsDO.getName());
        goodsDO.setManufacturer(standardGoodsDO.getManufacturer());
        goodsDO.setManufacturerAddress(standardGoodsDO.getManufacturerAddress());
        goodsDO.setSellSpecificationsId(standardGoodsSpecification.getId());
        goodsDO.setSellSpecifications(standardGoodsSpecification.getSellSpecifications());
        goodsDO.setSpecifications(standardGoodsSpecification.getSellSpecifications());
        goodsDO.setSellUnit(standardGoodsSpecification.getUnit());
        goodsDO.setUnit(standardGoodsSpecification.getUnit());
        goodsDO.setLicenseNo(standardGoodsSpecification.getLicenseNo());
        goodsDO.setMiddlePackage(1);
        goodsDO.setAuditStatus(GoodsStatusEnum.AUDIT_PASS.getCode());
        goodsService.saveOrUpdate(goodsDO);
        //保存sku
        SaveOrUpdateGoodsSkuRequest saveOrUpdateGoodsSkuRequest = new SaveOrUpdateGoodsSkuRequest();
        saveOrUpdateGoodsSkuRequest.setGoodsId(goodsDO.getId());
        saveOrUpdateGoodsSkuRequest.setGoodsLine(GoodsLineEnum.HMC.getCode());
        saveOrUpdateGoodsSkuRequest.setEid(request.getEid());
        saveOrUpdateGoodsSkuRequest.setOpUserId(request.getOpUserId());
        saveOrUpdateGoodsSkuRequest.setPackageNumber(1L);
        goodsSkuService.saveOrUpdateByNumber(saveOrUpdateGoodsSkuRequest);
        return goodsDO.getId();
    }

    @Override
    public List<StandardGoodsBasicDTO> batchQueryStandardGoodsBasic(List<Long> goodsIds) {
        return goodsService.batchQueryStandardGoodsBasic(goodsIds);
    }

    @Override
    public List<StandardGoodsBasicDTO> batchQueryStandardGoodsBasicBySpecificationsIds(List<Long> specificationsIds) {
        if (CollUtil.isEmpty(specificationsIds)) {
            return ListUtil.empty();
        }
        //批量获取标准库规格信息
        List<StandardGoodsSpecificationDO> specificationList = standardGoodsSpecificationService.listByIds(specificationsIds);
        List<Long> standardIds = specificationList.stream().map(e -> e.getStandardId()).collect(Collectors.toList());
        //批量获取标准库信息
        List<StandardGoodsDO> standardGoodsDOList = standardGoodsService.listByIds(standardIds);
        Map<Long, StandardGoodsDO> standardGoodsDOMap = standardGoodsDOList.stream().collect(Collectors.toMap(StandardGoodsDO::getId, Function.identity()));
        //批量获取图片信息
        Map<Long, List<StandardGoodsPicDO>> standardGoodsPicMap = standardGoodsPicService.getMapStandardGoodsPicByStandardId(standardIds);
        List<StandardGoodsBasicDTO> goodList = specificationList.stream().map(specification -> {
            StandardGoodsBasicDTO standardGoodsBasicDTO = new StandardGoodsBasicDTO();
            standardGoodsBasicDTO.setSellSpecificationsId(specification.getId());
            standardGoodsBasicDTO.setSellSpecifications(specification.getSellSpecifications());
            standardGoodsBasicDTO.setStandardId(specification.getStandardId());
            standardGoodsBasicDTO.setUnit(specification.getUnit());
            standardGoodsBasicDTO.setSellUnit(specification.getUnit());
            standardGoodsBasicDTO.setManufacturer(specification.getManufacturer());
            standardGoodsBasicDTO.setName(specification.getName());
            standardGoodsBasicDTO.setLicenseNo(specification.getLicenseNo());
            return standardGoodsBasicDTO;
        }).collect(Collectors.toList());
        goodList.forEach(e -> {
            if (e.getStandardId() != null && e.getStandardId() != 0) {
                if (standardGoodsDOMap != null && standardGoodsDOMap.get(e.getStandardId()) != null) {
                    StandardGoodsDO standardGoodsDO = standardGoodsDOMap.get(e.getStandardId());
                    StandardGoodsBasicInfoDTO infoDTO = PojoUtils.map(standardGoodsDO, StandardGoodsBasicInfoDTO.class);
                    e.setStandardGoods(infoDTO);
                }
                //获取默认图片
                e.setPic(goodsService.getDefaultUrlByStandardGoodsPicList(standardGoodsPicMap.get(e.getStandardId()), e.getSellSpecificationsId()));
            }
        });
        return goodList;
    }

    @Override
    public Long updateGoodsInventoryBySku(Long skuId, Long inventoryQty,Long opUserId) {
        GoodsSkuDO skuDO = goodsSkuService.getById(skuId);
        if(null!=skuDO){
            InventoryDO inventoryDO = inventoryService.getById(skuDO.getInventoryId());
            //实际库存不能小于冻结库存
            if(inventoryDO.getFrozenQty()>inventoryQty){
                throw new BusinessException(InventoryErrorCode.INVENTORY_MISSING,"实际库存数量不能小于冻结库存");
            }
            if(0>inventoryQty){
                throw new BusinessException(InventoryErrorCode.INVENTORY_MISSING,"修改库存必须为不小于0的正整数");
            }
            SaveInventoryRequest request = new SaveInventoryRequest();
            request.setId(inventoryDO.getId());
            request.setGid(skuDO.getGoodsId());
            request.setQty(inventoryQty);
            request.setOpUserId(opUserId);
            return inventoryService.save(request);
        }
        return 0L;
    }

    @Override
    public boolean subtractFrozenQtyAndQty(AddOrSubtractQtyRequest request) {
        int flag = inventoryService.subtractFrozenQtyAndQty(request);
        return flag>0;
    }

    @Override
    public int batchSubtractFrozenQtyAndQty(List<AddOrSubtractQtyRequest> requestList) {
        if(CollectionUtils.isEmpty(requestList)){
            return 0;
        }
        requestList.forEach(request->{
            subtractFrozenQtyAndQty(request);
        });
        return requestList.size();
    }

    @Override
    public boolean addHmcFrozenQty(AddOrSubtractQtyRequest request) {
        int flag = inventoryService.addHmcFrozenQty(request);
        return flag>0;
    }

    @Override
    public int batchAddHmcFrozenQty(List<AddOrSubtractQtyRequest> requestList) {
        return inventoryService.batchAddHmcFrozenQty(requestList);
    }

    @Override
    public int subtractHmcFrozenQty(AddOrSubtractQtyRequest request) {
        return inventoryService.subtractFrozenQty(request);
    }

    @Override
    public List<StandardGoodsInfoDTO> queryStandardGoodsByStandardIds(List<Long> standardIds) {
        List<StandardGoodsDO> doList = standardGoodsService.listByIds(standardIds);
        List<StandardGoodsInfoDTO> dtoList = PojoUtils.map(doList, StandardGoodsInfoDTO.class);
        dtoList.forEach(dto->{
            String sagName1 = dto.getStandardCategoryName1();
            String sagName2 = dto.getStandardCategoryName2();
            StringBuilder sagName = new StringBuilder();
            if (StringUtils.isNotBlank(sagName1)) {
                sagName.append(sagName1);
            }
            if (StringUtils.isNotBlank(sagName2)) {
                sagName.append("-").append(sagName2);
            }
            dto.setStandardCategoryName(sagName.toString());
        });
        return dtoList;
    }
}
