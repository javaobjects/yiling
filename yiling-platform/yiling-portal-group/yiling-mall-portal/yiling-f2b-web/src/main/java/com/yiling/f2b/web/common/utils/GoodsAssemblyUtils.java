package com.yiling.f2b.web.common.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.math3.stat.descriptive.summary.Product;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yiling.common.web.goods.vo.GoodsItemVO;
import com.yiling.common.web.goods.vo.GoodsPriceVO;
import com.yiling.f2b.web.goods.vo.DistributorGoodsVO;
import com.yiling.f2b.web.goods.vo.GoodsSkuVO;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.inventory.api.InventoryApi;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.api.PopGoodsApi;
import com.yiling.goods.medicine.bo.GoodsListItemBO;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.goods.medicine.dto.GoodsSkuDTO;
import com.yiling.goods.medicine.dto.PopGoodsDTO;
import com.yiling.goods.medicine.dto.StandardGoodsBasicDTO;
import com.yiling.goods.medicine.enums.GoodsLineEnum;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.mall.agreement.api.AgreementBusinessApi;
import com.yiling.pricing.goods.api.GoodsPriceApi;
import com.yiling.pricing.goods.dto.request.QueryGoodsPriceRequest;
import com.yiling.user.agreement.api.AgreementGoodsApi;
import com.yiling.user.agreement.dto.AgreementGoodsDTO;
import com.yiling.user.agreement.dto.request.AgreementPurchaseGoodsRequest;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.api.EnterprisePurchaseRelationApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.procrelation.api.ProcurementRelationGoodsApi;
import com.yiling.user.procrelation.dto.DistributorGoodsBO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuang.zhang
 * @date: 2021/6/22
 */
@Component
@Slf4j
public class GoodsAssemblyUtils {

    @DubboReference
    GoodsApi goodsApi;

    @DubboReference
    PopGoodsApi popGoodsApi;

    @DubboReference
    GoodsPriceApi goodsPriceApi;

    @DubboReference
    EnterpriseApi enterpriseApi;

    @DubboReference
    InventoryApi inventoryApi;

    @Autowired
    VoUtils voUtils;

    @DubboReference
    AgreementBusinessApi agreementBusinessApi;

    @DubboReference
    AgreementGoodsApi agreementGoodsApi;

    @DubboReference
    EnterprisePurchaseRelationApi enterprisePurchaseRelationApi;

    @DubboReference
    ProcurementRelationGoodsApi procurementRelationGoodsApi;

    public List<GoodsItemVO> assembly(List<Long> goodsIds, Long buyerEid) {
        log.info("goodsIds:{},buyerEid:{}",goodsIds,buyerEid);
        List<GoodsItemVO> goodsList = new ArrayList<>(goodsIds.size());

        //获取按钮的状态
        Map<Long, Integer> goodsLimitMap = agreementBusinessApi.getGoodsLimitByGids(goodsIds, buyerEid);

        //获取标准库商品信息
        List<StandardGoodsBasicDTO> standardGoodsDTOList = goodsApi.batchQueryStandardGoodsBasic(goodsIds);
        Map<Long, StandardGoodsBasicDTO> goodsInfoDTOMap = standardGoodsDTOList.stream().collect(Collectors.toMap(StandardGoodsBasicDTO::getId, Function.identity()));

        //获取配送商商品id
        List<Long> specIdList = standardGoodsDTOList.stream().map(StandardGoodsBasicDTO::getSellSpecificationsId).collect(Collectors.toList());
        Map<Long, List<Long>> sellerGoodsIdMap = this.getDistributorGoodsIdBySpec(specIdList,buyerEid);
        List<Long> sellerGoodsIdList = sellerGoodsIdMap.values().stream().flatMap(Collection::stream).collect(Collectors.toList());

        //获取商品定价系统
        QueryGoodsPriceRequest queryGoodsPriceRequest = new QueryGoodsPriceRequest();
        queryGoodsPriceRequest.setCustomerEid(buyerEid);
        queryGoodsPriceRequest.setGoodsIds(sellerGoodsIdList);
        Map<Long, BigDecimal> priceMap = goodsPriceApi.queryGoodsPriceMap(queryGoodsPriceRequest);

        goodsIds.forEach(e -> {
            GoodsItemVO goodsItemVO = new GoodsItemVO();
            StandardGoodsBasicDTO goodsInfoDTO = goodsInfoDTOMap.get(e);
            if (goodsInfoDTO != null) {
                goodsItemVO.setGoodsInfo(voUtils.toSimpleGoodsVO(goodsInfoDTO));
                //根据该商品规格id获取配送商商品id列表
                List<Long> sellerGoodsIds = sellerGoodsIdMap.get(goodsInfoDTO.getSellSpecificationsId());
                GoodsPriceVO goodsPriceVO = this.getPriceByDistributorGoods(sellerGoodsIds, priceMap);
                goodsItemVO.setPriceInfo(goodsPriceVO);
                goodsItemVO.setGoodsLimitStatus(goodsLimitMap.get(e));
                goodsList.add(goodsItemVO);
            }
        });
        return goodsList;
    }

    public Map<Long, List<Long>> getDistributorGoodsIdBySpec(List<Long> specIdList,Long buyerEid){
//        List<Long> sellerIds = enterprisePurchaseRelationApi.listSellerEidsByBuyerEid(buyerEid);
        Map<Long, List<DistributorGoodsBO>> distributorBySpecIdMap = procurementRelationGoodsApi.getDistributorBySpecIdAndBuyer(specIdList, buyerEid);
        if(CollectionUtil.isEmpty(distributorBySpecIdMap)){
            return MapUtil.empty();
        }
        List<Long> sellerIds = ListUtil.toList();
        distributorBySpecIdMap.values().forEach(list->{
            if(CollectionUtil.isNotEmpty(list)){
                sellerIds.addAll(list.stream().map(DistributorGoodsBO::getDistributorEid).collect(Collectors.toList()));
            }
        });
        if(CollectionUtil.isEmpty(sellerIds)){
            return MapUtil.empty();
        }
        log.info("获取售卖商品相关参数，specIdList:{},buyerEid:{},sellerIds:{}",specIdList,buyerEid,sellerIds.stream().distinct().collect(Collectors.toList()));
        List<GoodsListItemBO> sellerGoodsList = popGoodsApi.findGoodsBySpecificationIdListAndEidList(specIdList, sellerIds,GoodsStatusEnum.UP_SHELF);
        //key 规格id value 配送商商品List
        Map<Long, List<GoodsListItemBO>> popUpShelfMap = sellerGoodsList.stream().collect(Collectors.groupingBy(GoodsListItemBO::getSellSpecificationsId));
        Map<Long, List<Long>> resultMap = MapUtil.newHashMap();
        distributorBySpecIdMap.entrySet().forEach(entry->{
            if(CollectionUtil.isNotEmpty(entry.getValue())){
                //获取该规格上架商品
                List<GoodsListItemBO> sellerGoods = popUpShelfMap.getOrDefault(entry.getKey(),ListUtil.empty());
                //获取建采配送商
                List<Long> distributorEid = entry.getValue().stream().map(DistributorGoodsBO::getDistributorEid).collect(Collectors.toList());
                //获取规格和eid都符合的配送商商品id
                List<Long> distributorGoodsId = sellerGoods.stream().filter(goods -> entry.getKey().equals(goods.getSellSpecificationsId()) && distributorEid.contains(goods.getEid())).map(GoodsListItemBO::getId).collect(Collectors.toList());
                resultMap.put(entry.getKey(),distributorGoodsId);
            }
        });
        return resultMap;
    }

    public GoodsPriceVO getPriceByDistributorGoods(List<Long> distributorGoodsIds,Map<Long, BigDecimal> priceMap){
        GoodsPriceVO goodsPriceVO = new GoodsPriceVO();
        if(CollectionUtil.isNotEmpty(distributorGoodsIds)){
            //根据配送商id获取该规格id下所有的配送商商品价格
            List<BigDecimal> sellerPriceList = ListUtil.toList();
            distributorGoodsIds.forEach(goodsId->{
                BigDecimal price = priceMap.get(goodsId);
                if(null!=price && price.compareTo(BigDecimal.ZERO)>0){
                    sellerPriceList.add(price);
                }
            });
            if(CollectionUtil.isNotEmpty(sellerPriceList)){
                BigDecimal minPrice = sellerPriceList.stream().min(BigDecimal::compareTo).get();
                BigDecimal maxPrice = sellerPriceList.stream().max(BigDecimal::compareTo).get();
                goodsPriceVO.setMinPrice(minPrice);
                goodsPriceVO.setMaxPrice(maxPrice);
                goodsPriceVO.setBuyPrice(minPrice);
            }
        }
        return goodsPriceVO;
    }

    public List<DistributorGoodsVO> getDistributorGoods(Long skuId, Long goodsId, List<Long> sellerIds,Long buyerEid) {
        List<EnterpriseDTO> enterpriseDTOList = enterpriseApi.listByIds(sellerIds);
        Map<Long, EnterpriseDTO> enterpriseDTOmap = enterpriseDTOList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, Function.identity()));

        //获取配送商Id
        Map<Long, Long> distributorGoodsIdMap = goodsApi.getDistributorGoodsIdMapByYlGoodsId(sellerIds,goodsId);
        if (CollUtil.isEmpty(distributorGoodsIdMap)) {
            return ListUtil.empty();
        }
        //获取配送商商品列表信息
        List<Long> distributorGoodsIds = distributorGoodsIdMap.values().stream().collect(Collectors.toList());
        //获取商品定价系统
        QueryGoodsPriceRequest queryGoodsPriceRequest = new QueryGoodsPriceRequest();
        queryGoodsPriceRequest.setCustomerEid(buyerEid);
        queryGoodsPriceRequest.setGoodsIds(distributorGoodsIds);
        Map<Long, BigDecimal> priceMap = goodsPriceApi.queryGoodsPriceMap(queryGoodsPriceRequest);

        //获取标准库商品信息
        List<StandardGoodsBasicDTO> standardGoodsDTOList = goodsApi.batchQueryStandardGoodsBasic(distributorGoodsIds);
        Map<Long, StandardGoodsBasicDTO> goodsInfoDTOMap = standardGoodsDTOList.stream().collect(Collectors.toMap(StandardGoodsBasicDTO::getId, Function.identity()));

        List<PopGoodsDTO> popGoodsDTOS = popGoodsApi.getPopGoodsListByGoodsIds(distributorGoodsIds);
        Map<Long, PopGoodsDTO> popGoodsDTOMap = popGoodsDTOS.stream().collect(Collectors.toMap(PopGoodsDTO::getGoodsId, Function.identity()));

        List<DistributorGoodsVO> list = new ArrayList<>();
        for (Long e : sellerIds) {
            //查询库存信息
            if (distributorGoodsIdMap.get(e) != null&&distributorGoodsIdMap.get(e) !=0) {
                DistributorGoodsVO distributorGoodsVO = new DistributorGoodsVO();
                Long distributorGoodsId = distributorGoodsIdMap.get(e);
                StandardGoodsBasicDTO goodsInfo = goodsInfoDTOMap.get(distributorGoodsId);
                PopGoodsDTO popGoodsDTO = popGoodsDTOMap.get(distributorGoodsId);
                if (popGoodsDTO!=null&&popGoodsDTO.getGoodsStatus().equals(GoodsStatusEnum.UP_SHELF.getCode())) {
                    List<GoodsSkuDTO> goodsSkuDTOList = goodsApi.getGoodsSkuByGoodsId(goodsInfo.getId());
                    goodsSkuDTOList=goodsSkuDTOList.stream().filter(f->f.getGoodsLine().equals(GoodsLineEnum.POP.getCode())).collect(Collectors.toList());
                    List<GoodsSkuVO> goodsSkuVOList =PojoUtils.map(goodsSkuDTOList, GoodsSkuVO.class);
                    //选中skuId
                    if (skuId != null && skuId != 0) {
                        for(GoodsSkuVO goodsSkuVO:goodsSkuVOList){
                            if(goodsSkuVO.getId().equals(skuId)){
                                goodsSkuVO.setSelectFlag(1);
                            }
                        }
                    }
                    distributorGoodsVO.setGoodsSkuList(goodsSkuVOList);
                    distributorGoodsVO.setDistributorId(e);
                    distributorGoodsVO.setDistributorName(enterpriseDTOmap.get(e).getName());
                    distributorGoodsVO.setName(goodsInfo.getStandardGoods().getName());
                    distributorGoodsVO.setGoodsId(distributorGoodsId);
                    distributorGoodsVO.setYlGoodsId(goodsId);
                    distributorGoodsVO.setOverSoldType(goodsInfo.getOverSoldType());
                    //商品价格体系
                    GoodsPriceVO goodsPriceVO = new GoodsPriceVO();
                    goodsPriceVO.setBuyPrice(priceMap.getOrDefault(distributorGoodsId, BigDecimal.ZERO));
                    distributorGoodsVO.setPriceInfo(goodsPriceVO);
                    list.add(distributorGoodsVO);
                }
            }
        }
        return list;
    }

    public List<DistributorGoodsVO> getDistributorGoods(Long goodsId, List<DistributorGoodsBO> distributorGoodsList,Long buyerEid){
        List<Long> sellerEids = distributorGoodsList.stream().map(DistributorGoodsBO::getDistributorEid).collect(Collectors.toList());
        List<DistributorGoodsVO> distributorGoodsVOList = this.getDistributorGoods(0L, goodsId, sellerEids, buyerEid);
        Map<String, BigDecimal> rebateMap = distributorGoodsList.stream().collect(Collectors.toMap(bo -> bo.getGoodsId() + "_" + bo.getDistributorEid(), DistributorGoodsBO::getRebate, (e1, e2) -> e1));
        distributorGoodsVOList.forEach(e->{
            e.setRebate(rebateMap.getOrDefault(e.getYlGoodsId()+"_"+e.getDistributorId(),BigDecimal.ZERO));
        });
        return distributorGoodsVOList;
    }


    /**
     * 获取配送商品信息
     *
     * @param skuId          配送商商品skuId
     * @param goodsId        以岭商品ID
     * @param buyerEid       购买者
     * @param distributorEid 配送着
     * @return
     */
    @Deprecated
    public List<DistributorGoodsVO> queryDistributorInfoList(Long skuId, Long goodsId, Long buyerEid, Long distributorEid) {
        //直接查询配送商信息
        List<Long> sellerIds = enterprisePurchaseRelationApi.listSellerEidsByBuyerEid(buyerEid);
        if (CollUtil.isEmpty(sellerIds)) {
            return new ArrayList<>(ListUtil.empty());
        }

        if (distributorEid != null && distributorEid != 0) {
            if (!sellerIds.contains(distributorEid)) {
                return new ArrayList<>(ListUtil.empty());
            }
            sellerIds = Arrays.asList(distributorEid);
        }
        //临时协议
        Iterator<Long> disEidIt = sellerIds.iterator();
        while (disEidIt.hasNext()) {
            Long disEid = disEidIt.next();
            AgreementPurchaseGoodsRequest request = new AgreementPurchaseGoodsRequest();
            request.setPurchaseEid(buyerEid);
            request.setDistributionEid(disEid);
            request.setGoodsId(goodsId);
            List<AgreementGoodsDTO> tempList = agreementGoodsApi.getTempPurchaseGoodsByDistributionList(request);
            if (CollUtil.isEmpty(tempList)) {
                disEidIt.remove();
            }
        }

        if (CollUtil.isEmpty(sellerIds)) {
            return new ArrayList<>(ListUtil.empty());
        }

        return this.getDistributorGoods(skuId, goodsId, sellerIds,buyerEid);
    }
}
