package com.yiling.pricing.goods.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.api.GoodsLimitPriceApi;
import com.yiling.goods.medicine.dto.CustomerPriceLimitDTO;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.goods.medicine.dto.GoodsPriceLimitDTO;
import com.yiling.goods.medicine.enums.GoodsLineEnum;
import com.yiling.pricing.goods.dto.GoodsPriceDTO;
import com.yiling.pricing.goods.dto.PopGoodsLimitPriceDTO;
import com.yiling.pricing.goods.dto.request.QueryGoodsLinePriceRequest;
import com.yiling.pricing.goods.dto.request.QueryGoodsPriceRequest;
import com.yiling.pricing.goods.dto.request.QueryPopLimitPriceRequest;
import com.yiling.pricing.goods.entity.GoodsPriceCustomerDO;
import com.yiling.pricing.goods.entity.GoodsPriceCustomerGroupDO;
import com.yiling.pricing.goods.enums.PopGoodsLimitPriceStatusEnum;
import com.yiling.pricing.goods.service.GoodsPriceCustomerGroupService;
import com.yiling.pricing.goods.service.GoodsPriceCustomerService;
import com.yiling.pricing.goods.service.GoodsPriceService;
import com.yiling.marketing.promotion.api.PromotionActivityApi;
import com.yiling.marketing.promotion.dto.PromotionGoodsLimitDTO;
import com.yiling.marketing.promotion.dto.request.PromotionAppRequest;
import com.yiling.pricing.goods.service.PopGoodsLimitPriceService;
import com.yiling.user.common.enums.EnterpriseCustomerLineEnum;
import com.yiling.user.enterprise.api.CustomerApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.api.EnterpriseCustomerLineApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.enums.EnterpriseChannelEnum;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 商品定价 Service 实现
 *
 * @author: xuan.zhou
 * @date: 2021/7/9
 */
@Service
@Slf4j
public class GoodsPriceServiceImpl implements GoodsPriceService {

    @DubboReference
    GoodsApi                  goodsApi;
    @DubboReference
    GoodsLimitPriceApi        goodsLimitPriceApi;
    @DubboReference
    EnterpriseApi             enterpriseApi;
    @DubboReference
    CustomerApi               customerApi;
    @DubboReference
    EnterpriseCustomerLineApi enterpriseCustomerLineApi;
    @DubboReference
    PromotionActivityApi      promotionActivityApi;

    @Autowired
    private GoodsPriceCustomerService      goodsPriceCustomerService;
    @Autowired
    private GoodsPriceCustomerGroupService goodsPriceCustomerGroupService;
    @Autowired
    private PopGoodsLimitPriceService      popGoodsLimitPriceService;


    @Override
    public Map<Long, BigDecimal> queryGoodsPriceMap(QueryGoodsPriceRequest request) {
        //设置产品线
        QueryGoodsLinePriceRequest queryGoodsLinePriceRequest = PojoUtils.map(request, QueryGoodsLinePriceRequest.class);
        queryGoodsLinePriceRequest.setGoodsLine(GoodsLineEnum.POP.getCode());
        List<Long> goodsIds = request.getGoodsIds();
        if (CollUtil.isEmpty(goodsIds)) {
            return MapUtil.empty();
        }

        List<GoodsDTO> goodsList = goodsApi.batchQueryInfo(goodsIds);
        Map<Long, List<GoodsDTO>> goodsMap = goodsList.stream().collect(Collectors.groupingBy(GoodsDTO::getEid));

        // 商品基价
        Map<Long, BigDecimal> goodsPriceMap = goodsList.stream().collect(Collectors.toMap(GoodsDTO::getId, GoodsDTO::getPrice));
        Map<Long, BigDecimal> baseGoodsPriceMap = new HashMap<>(goodsPriceMap);

        // 未登录情况，返回基价
        Long customerEid = request.getCustomerEid();
        if (customerEid == null || customerEid == 0L) {
            return goodsPriceMap;
        }

        List<Long> subEids = enterpriseApi.listSubEids(Constants.YILING_EID);
        //商品限价
        Map<Long, EnterpriseDTO> enterpriseMap = enterpriseApi.getMapByIds(ListUtil.toList(goodsMap.keySet()));
        List<Long> specificationsIdList = goodsList.stream().map(GoodsDTO::getSellSpecificationsId).collect(Collectors.toList());
        QueryPopLimitPriceRequest limitPriceRequest = new QueryPopLimitPriceRequest();
        limitPriceRequest.setSellSpecificationsIds(specificationsIdList);
        limitPriceRequest.setStatus(PopGoodsLimitPriceStatusEnum.NORMAL.getCode());
        List<PopGoodsLimitPriceDTO> goodsLimitPriceList = popGoodsLimitPriceService.batchQueryLimitPrice(limitPriceRequest);
        Map<Long, PopGoodsLimitPriceDTO> limitPriceMap = goodsLimitPriceList.stream().collect(Collectors.toMap(PopGoodsLimitPriceDTO::getSellSpecificationsId, Function.identity(), (e1, e2) -> e1));


        for (Map.Entry<Long, List<GoodsDTO>> entry : goodsMap.entrySet()) {
            Long eid = entry.getKey();

            //获取限价并赋值
            EnterpriseDTO enterpriseDTO = enterpriseMap.get(eid);
            if(null!=enterpriseDTO && CollectionUtil.isNotEmpty(entry.getValue())){
                if(EnterpriseChannelEnum.INDUSTRY_DIRECT.getCode().equals(enterpriseDTO.getChannelId())
                        ||EnterpriseChannelEnum.LEVEL_1.getCode().equals(enterpriseDTO.getChannelId())
                        ||EnterpriseChannelEnum.LEVEL_2.getCode().equals(enterpriseDTO.getChannelId())
                        ||EnterpriseChannelEnum.Z2P1.getCode().equals(enterpriseDTO.getChannelId())){
                    entry.getValue().forEach(goods->{
                        PopGoodsLimitPriceDTO limitPriceDTO = limitPriceMap.get(goods.getSellSpecificationsId());
                        if(null!=limitPriceDTO){
                            //如果基价大于最大限价，则使用最大限价
                            if(limitPriceDTO.getUpperLimitPrice().compareTo(goods.getPrice())<0){
                                goodsPriceMap.put(goods.getId(),limitPriceDTO.getUpperLimitPrice());
                                //如果基价小于最小限价，则使用最小限价
                            }else if(limitPriceDTO.getLowerLimitPrice().compareTo(goods.getPrice())>0){
                                goodsPriceMap.put(goods.getId(),limitPriceDTO.getLowerLimitPrice());
                            }
                        }
                    });
                }
            }
            if (subEids.contains(entry.getKey())) {
                eid = Constants.YILING_EID;
            }
            queryGoodsLinePriceRequest.setEid(eid);
            // 客户分组定价
            Map<Long, GoodsPriceCustomerGroupDO> goodsCustomerGroupPriceMap = goodsPriceCustomerGroupService.listGoodsCustomerGroupPriceInfos(queryGoodsLinePriceRequest);
            for (Long goodsId : goodsCustomerGroupPriceMap.keySet()) {
                GoodsPriceCustomerGroupDO goodsPriceCustomerGroupDO = goodsCustomerGroupPriceMap.get(goodsId);
                goodsPriceMap.put(goodsId, this.calculatePrice(baseGoodsPriceMap.get(goodsId), goodsPriceCustomerGroupDO.getPriceRule(), goodsPriceCustomerGroupDO.getPriceValue(),4));
            }

            // 客户定价
            Map<Long, GoodsPriceCustomerDO> goodsCustomerPriceMap = goodsPriceCustomerService.listGoodsCustomerPriceInfos(queryGoodsLinePriceRequest);
            for (Long goodsId : goodsCustomerPriceMap.keySet()) {
                GoodsPriceCustomerDO goodsPriceCustomerDO = goodsCustomerPriceMap.get(goodsId);
                goodsPriceMap.put(goodsId, this.calculatePrice(baseGoodsPriceMap.get(goodsId), goodsPriceCustomerDO.getPriceRule(), goodsPriceCustomerDO.getPriceValue(),4));
            }
        }

        return goodsPriceMap;
    }

    @Override
    public Map<Long, BigDecimal> queryB2bGoodsPriceMap(QueryGoodsPriceRequest request) {
        //设置产品线
        QueryGoodsLinePriceRequest queryGoodsLinePriceRequest = PojoUtils.map(request, QueryGoodsLinePriceRequest.class);
        queryGoodsLinePriceRequest.setGoodsLine(GoodsLineEnum.B2B.getCode());

        List<Long> goodsIds = request.getGoodsIds();
        if (CollUtil.isEmpty(goodsIds)) {
            return MapUtil.empty();
        }

        List<GoodsDTO> goodsList = goodsApi.batchQueryInfo(goodsIds);
        //商品Map
        Map<Long, GoodsDTO> goodsMap = goodsList.stream().collect(Collectors.toMap(GoodsDTO::getId, Function.identity()));
        //商品售卖规格
        List<Long> sellSpecificationsIds = goodsList.stream().map(e -> e.getSellSpecificationsId()).collect(Collectors.toList());

        // 商品基价
        Map<Long, BigDecimal> goodsPriceMap = goodsList.stream().collect(Collectors.toMap(GoodsDTO::getId, GoodsDTO::getPrice));
        Map<Long, BigDecimal> baseGoodsPriceMap = new HashMap<>(goodsPriceMap);

        // 未登录情况，返回基价
        Long customerEid = request.getCustomerEid();
        if (customerEid == null || customerEid == 0L) {
            return goodsPriceMap;
        }

        // 客户分组定价
        Map<Long, List<GoodsDTO>> listMap = goodsList.stream().collect(Collectors.groupingBy(GoodsDTO::getEid));
        Map<Long, GoodsPriceCustomerGroupDO> goodsCustomerGroupPriceMap = new HashMap<>();
        for (Map.Entry<Long, List<GoodsDTO>> entry : listMap.entrySet()) {
            QueryGoodsLinePriceRequest queryGroupGoodsLinePriceRequest = new QueryGoodsLinePriceRequest();
            queryGroupGoodsLinePriceRequest.setGoodsLine(queryGoodsLinePriceRequest.getGoodsLine());
            queryGroupGoodsLinePriceRequest.setCustomerEid(queryGoodsLinePriceRequest.getCustomerEid());
            queryGroupGoodsLinePriceRequest.setEid(entry.getKey());
            queryGroupGoodsLinePriceRequest.setGoodsIds(entry.getValue().stream().map(e -> e.getId()).collect(Collectors.toList()));
            goodsCustomerGroupPriceMap.putAll(goodsPriceCustomerGroupService.listGoodsCustomerGroupPriceInfos(queryGroupGoodsLinePriceRequest));
        }

        for (Long goodsId : goodsCustomerGroupPriceMap.keySet()) {
            GoodsPriceCustomerGroupDO goodsPriceCustomerGroupDO = goodsCustomerGroupPriceMap.get(goodsId);
            goodsPriceMap.put(goodsId, this.calculatePrice(baseGoodsPriceMap.get(goodsId), goodsPriceCustomerGroupDO.getPriceRule(), goodsPriceCustomerGroupDO.getPriceValue(),2));
        }

        // 客户定价
        Map<Long, GoodsPriceCustomerDO> goodsCustomerPriceMap = goodsPriceCustomerService.listGoodsCustomerPriceInfos(queryGoodsLinePriceRequest);
        for (Long goodsId : goodsCustomerPriceMap.keySet()) {
            GoodsPriceCustomerDO goodsPriceCustomerDO = goodsCustomerPriceMap.get(goodsId);
            goodsPriceMap.put(goodsId, this.calculatePrice(baseGoodsPriceMap.get(goodsId), goodsPriceCustomerDO.getPriceRule(), goodsPriceCustomerDO.getPriceValue(),2));
        }

        // 获取所有的限价商品
        List<GoodsDTO> ylGoodsDTOList = goodsApi.findGoodsBySellSpecificationsIdAndEid(sellSpecificationsIds, enterpriseApi.listSubEids(Constants.YILING_EID));
        List<Long> ylGoodsIds = ylGoodsDTOList.stream().map(e -> e.getId()).collect(Collectors.toList());
        Map<Long, List<GoodsDTO>> ylGoodsDTOMap = ylGoodsDTOList.stream().collect(Collectors.groupingBy(GoodsDTO::getSellSpecificationsId));
        Map<Long, List<GoodsPriceLimitDTO>> goodsPriceLimitDTOMap = goodsLimitPriceApi.listGoodsPriceLimitByGoodsIds(ylGoodsIds);

        EnterpriseDTO enterpriseDTO = enterpriseApi.getById(customerEid);
        for (Long goodsId : request.getGoodsIds()) {
            //通过供应商的商品ID转成以岭的商品ID
            GoodsDTO goodsDTO = goodsMap.get(goodsId);
            List<GoodsDTO> ylGoodsList = ylGoodsDTOMap.get(goodsDTO.getSellSpecificationsId());
            //以岭品
            if (CollUtil.isNotEmpty(ylGoodsList)) {
                //管控的商业公司
                CustomerPriceLimitDTO customerPriceLimitDTO = goodsLimitPriceApi.getCustomerPriceLimitByCustomerEid(Constants.YILING_EID, goodsDTO.getEid());
                //管控的以岭商品
                List<Long> controlGoodsIds = goodsLimitPriceApi.getGoodsIdsByCustomerEid(Constants.YILING_EID, goodsDTO.getEid());
                //以岭限价商品
                GoodsDTO ylGoods = ylGoodsList.get(0);
                List<GoodsPriceLimitDTO> goodsPriceLimitDTOList = goodsPriceLimitDTOMap.get(ylGoods.getId());
                //管控商业公司是否配置商品
                if (customerPriceLimitDTO != null && CollUtil.isNotEmpty(controlGoodsIds) && customerPriceLimitDTO.getLimitFlag() == 1 && controlGoodsIds.contains(ylGoods.getId())) {
                    BigDecimal limitPrice = calculateLimitPrice(goodsPriceLimitDTOList, enterpriseDTO);
                    if (limitPrice != null) {
                        goodsPriceMap.put(goodsId, limitPrice);
                    } else {
                        goodsPriceMap.put(goodsId, ylGoods.getPrice());
                    }
                    continue;
                }

                //不管控 客户定价  不能低于以岭的基价 有限价不能低于限价
                BigDecimal limitPrice = BigDecimal.ZERO;
                if (CollUtil.isNotEmpty(goodsPriceLimitDTOList)) {
                    limitPrice = calculateLimitPrice(goodsPriceLimitDTOList, enterpriseDTO);
                }
                if (limitPrice == null || limitPrice.compareTo(BigDecimal.ZERO) == 0) {
                    limitPrice = ylGoods.getPrice();
                }
                if (goodsPriceMap.get(goodsId).compareTo(limitPrice) < 0) {
                    goodsPriceMap.put(goodsId, limitPrice);
                }
            }
        }
        return goodsPriceMap;
    }

    @Override
    public Map<Long, GoodsPriceDTO> queryB2bGoodsPriceInfoMap(QueryGoodsPriceRequest request) {
        log.info("b2b商品定价参数[queryB2bGoodsPriceInfoMap]：{}", JSONUtil.toJsonStr(request));
        //设置产品线
        QueryGoodsLinePriceRequest queryGoodsLinePriceRequest = PojoUtils.map(request, QueryGoodsLinePriceRequest.class);
        queryGoodsLinePriceRequest.setGoodsLine(GoodsLineEnum.B2B.getCode());

        List<Long> goodsIds = request.getGoodsIds();
        if (CollUtil.isEmpty(goodsIds)) {
            return MapUtil.empty();
        }

        List<GoodsDTO> goodsList = goodsApi.batchQueryInfo(goodsIds);
        //商品Map
        Map<Long, GoodsDTO> goodsMap = goodsList.stream().collect(Collectors.toMap(GoodsDTO::getId, Function.identity()));
        // 商品基价
        Map<Long, BigDecimal> goodsPriceMap = goodsList.stream().collect(Collectors.toMap(GoodsDTO::getId, GoodsDTO::getPrice));

        Map<Long, GoodsPriceDTO> baseGoodsPriceMap = new HashMap<>();

        List<Long> eids = goodsList.stream().map(e -> e.getEid()).distinct().collect(Collectors.toList());
        Map<Long, Boolean> enterpriseCustomerLineApiCustomerLineListFlagMap = enterpriseCustomerLineApi.getCustomerLineListFlag(eids, request.getCustomerEid(), EnterpriseCustomerLineEnum.B2B);
        for (GoodsDTO goodsDTO : goodsList) {
            GoodsPriceDTO goodsPriceDTO = new GoodsPriceDTO();
            goodsPriceDTO.setId(goodsDTO.getId());
            goodsPriceDTO.setLimitPrice(BigDecimal.ZERO);
            goodsPriceDTO.setBuyPrice(goodsPriceMap.get(goodsDTO.getId()));
            goodsPriceDTO.setLinePrice(goodsPriceDTO.getBuyPrice());
            if (enterpriseCustomerLineApiCustomerLineListFlagMap.get(goodsDTO.getEid())) {
                goodsPriceDTO.setIsShow(true);
            } else {
                goodsPriceDTO.setIsShow(false);
            }
            baseGoodsPriceMap.put(goodsDTO.getId(), goodsPriceDTO);
        }

        // 未登录情况，返回基价
        Long customerEid = request.getCustomerEid();
        if (customerEid == null || customerEid == 0L) {
            return baseGoodsPriceMap;
        }

        // 客户分组定价
        Map<Long, List<GoodsDTO>> listMap = goodsList.stream().collect(Collectors.groupingBy(GoodsDTO::getEid));
        Map<Long, GoodsPriceCustomerGroupDO> goodsCustomerGroupPriceMap = new HashMap<>();
        for (Map.Entry<Long, List<GoodsDTO>> entry : listMap.entrySet()) {
            QueryGoodsLinePriceRequest queryGroupGoodsLinePriceRequest = new QueryGoodsLinePriceRequest();
            queryGroupGoodsLinePriceRequest.setGoodsLine(queryGoodsLinePriceRequest.getGoodsLine());
            queryGroupGoodsLinePriceRequest.setCustomerEid(queryGoodsLinePriceRequest.getCustomerEid());
            queryGroupGoodsLinePriceRequest.setEid(entry.getKey());
            queryGroupGoodsLinePriceRequest.setGoodsIds(entry.getValue().stream().map(e -> e.getId()).collect(Collectors.toList()));
            goodsCustomerGroupPriceMap.putAll(goodsPriceCustomerGroupService.listGoodsCustomerGroupPriceInfos(queryGroupGoodsLinePriceRequest));
        }

        for (Long goodsId : goodsCustomerGroupPriceMap.keySet()) {
            GoodsPriceCustomerGroupDO goodsPriceCustomerGroupDO = goodsCustomerGroupPriceMap.get(goodsId);
            GoodsPriceDTO goodsPriceDTO = baseGoodsPriceMap.get(goodsId);
            goodsPriceDTO.setBuyPrice(this.calculatePrice(baseGoodsPriceMap.get(goodsId).getBuyPrice(), goodsPriceCustomerGroupDO.getPriceRule(), goodsPriceCustomerGroupDO.getPriceValue(),2));
            goodsPriceDTO.setLinePrice(goodsPriceDTO.getBuyPrice());
            baseGoodsPriceMap.put(goodsId, goodsPriceDTO);
        }

        // 客户定价
        Map<Long, GoodsPriceCustomerDO> goodsCustomerPriceMap = goodsPriceCustomerService.listGoodsCustomerPriceInfos(queryGoodsLinePriceRequest);
        for (Long goodsId : goodsCustomerPriceMap.keySet()) {
            GoodsPriceCustomerDO goodsPriceCustomerDO = goodsCustomerPriceMap.get(goodsId);
            GoodsPriceDTO goodsPriceDTO = baseGoodsPriceMap.get(goodsId);
            goodsPriceDTO.setBuyPrice(this.calculatePrice(baseGoodsPriceMap.get(goodsId).getBuyPrice(), goodsPriceCustomerDO.getPriceRule(), goodsPriceCustomerDO.getPriceValue(),2));
            goodsPriceDTO.setLinePrice(goodsPriceDTO.getBuyPrice());
            baseGoodsPriceMap.put(goodsId, goodsPriceDTO);
        }

        //获取所有的限价商品
        //商品售卖规格
        List<Long> sellSpecificationsIds = goodsList.stream().map(e -> e.getSellSpecificationsId()).collect(Collectors.toList());
        List<GoodsDTO> ylGoodsDTOList = goodsApi.findGoodsBySellSpecificationsIdAndEid(sellSpecificationsIds, enterpriseApi.listSubEids(Constants.YILING_EID));
        List<Long> ylGoodsIds = ylGoodsDTOList.stream().map(e -> e.getId()).collect(Collectors.toList());
        Map<Long, List<GoodsDTO>> ylGoodsDTOMap = ylGoodsDTOList.stream().collect(Collectors.groupingBy(GoodsDTO::getSellSpecificationsId));
        Map<Long, List<GoodsPriceLimitDTO>> goodsPriceLimitDTOMap = goodsLimitPriceApi.listGoodsPriceLimitByGoodsIds(ylGoodsIds);

        Map<Long, List<Long>> controlGoodsByCustomerEidMap = goodsLimitPriceApi.getLimitByGidsAndCustomerEidsGroupByCustomerEid(ylGoodsIds, eids, Constants.YILING_EID);
        EnterpriseDTO enterpriseDTO = enterpriseApi.getById(request.getCustomerEid());
        for (Long goodsId : request.getGoodsIds()) {
            //通过供应商的商品ID转成以岭的商品ID
            GoodsDTO goodsDTO = goodsMap.get(goodsId);
            if(null == goodsDTO){
                continue;
            }
            List<GoodsDTO> ylGoodsList = ylGoodsDTOMap.get(goodsDTO.getSellSpecificationsId());
            //以岭品
            if (CollUtil.isNotEmpty(ylGoodsList)) {
                //管控的以岭商品
                List<Long> controlGoodsIds = controlGoodsByCustomerEidMap.get(goodsDTO.getEid());
                //                CustomerPriceLimitDTO customerPriceLimitDTO = goodsLimitPriceApi.getCustomerPriceLimitByCustomerEid(Constants.YILING_EID, goodsDTO.getEid());
                //                List<Long> controlGoodsIds = goodsLimitPriceApi.getGoodsIdsByCustomerEid(Constants.YILING_EID, goodsDTO.getEid());
                //以岭限价商品
                GoodsDTO ylGoods = ylGoodsList.get(0);
                List<GoodsPriceLimitDTO> goodsPriceLimitDTOList = goodsPriceLimitDTOMap.get(ylGoods.getId());
                //管控商业公司是否配置商品
                GoodsPriceDTO goodsPriceDTO = baseGoodsPriceMap.get(goodsId);
                goodsPriceDTO.setBasePrice(ylGoods.getPrice());
                //if (customerPriceLimitDTO != null && CollUtil.isNotEmpty(controlGoodsIds) && customerPriceLimitDTO.getLimitFlag() == 1 && controlGoodsIds.contains(ylGoods.getId())) {
                if (CollUtil.isNotEmpty(controlGoodsIds) && controlGoodsIds.contains(ylGoods.getId())) {
                    BigDecimal limitPrice = calculateLimitPrice(goodsPriceLimitDTOList, enterpriseDTO);
                    if (limitPrice != null) {
                        goodsPriceDTO.setBuyPrice(limitPrice);
                        goodsPriceDTO.setLimitPrice(limitPrice);
                    } else {
                        goodsPriceDTO.setBuyPrice(ylGoods.getPrice());
                    }
                    goodsPriceDTO.setLinePrice(goodsPriceDTO.getBuyPrice());
                    baseGoodsPriceMap.put(goodsId, goodsPriceDTO);
                    continue;
                }

                //不管控 客户定价  不能低于以岭的基价 有限价不能低于限价
                BigDecimal limitPrice = BigDecimal.ZERO;
                if (CollUtil.isNotEmpty(goodsPriceLimitDTOList)) {
                    limitPrice = calculateLimitPrice(goodsPriceLimitDTOList, enterpriseDTO);
                }
                if (limitPrice == null || limitPrice.compareTo(BigDecimal.ZERO) == 0) {
                    limitPrice = ylGoods.getPrice();
                }else {
                    goodsPriceDTO.setLimitPrice(limitPrice);
                }
                if (baseGoodsPriceMap.get(goodsId).getBuyPrice().compareTo(limitPrice) < 0) {
                    goodsPriceDTO.setBuyPrice(limitPrice);
                    goodsPriceDTO.setLinePrice(goodsPriceDTO.getBuyPrice());
                    baseGoodsPriceMap.put(goodsId, goodsPriceDTO);
                }
            }
        }

        //获取商品的活动价格
        PromotionAppRequest promotionAppRequest = new PromotionAppRequest();
        promotionAppRequest.setBuyerEid(request.getCustomerEid());
        promotionAppRequest.setPlatform(PlatformEnum.B2B.getCode());
        promotionAppRequest.setGoodsIdList(request.getGoodsIds());
        promotionAppRequest.setTypeList(Arrays.asList(2, 3));
        List<PromotionGoodsLimitDTO> promotionGoodsList = promotionActivityApi.queryGoodsPromotionInfo(promotionAppRequest);
        for (PromotionGoodsLimitDTO promotionGoodsLimitDTO : promotionGoodsList) {
            GoodsPriceDTO goodsPriceDTO = baseGoodsPriceMap.get(promotionGoodsLimitDTO.getGoodsId());
            goodsPriceDTO.setLinePrice(goodsPriceDTO.getBuyPrice());
            goodsPriceDTO.setBuyPrice(promotionGoodsLimitDTO.getPromotionPrice());
            baseGoodsPriceMap.put(promotionGoodsLimitDTO.getGoodsId(),goodsPriceDTO);
        }
        return baseGoodsPriceMap;
    }

    @Override
    public Map<Long, Boolean> queryB2bGoodsPriceShowMap(QueryGoodsPriceRequest request) {
        List<Long> goodsIds = request.getGoodsIds();
        if (CollUtil.isEmpty(goodsIds)) {
            return MapUtil.empty();
        }

        Map<Long, Boolean> goodsPriceShowMap = new HashMap<>();
        for (Long goodsId : goodsIds) {
            goodsPriceShowMap.put(goodsId, false);
        }

        List<GoodsDTO> goodsDTOList = goodsApi.batchQueryInfo(goodsIds);
        List<Long> eids = goodsDTOList.stream().map(e -> e.getEid()).distinct().collect(Collectors.toList());
        Map<Long, Boolean> enterpriseCustomerLineApiCustomerLineListFlagMap = enterpriseCustomerLineApi.getCustomerLineListFlag(eids, request.getCustomerEid(), EnterpriseCustomerLineEnum.B2B);
        for (GoodsDTO goodsDTO : goodsDTOList) {
            if (enterpriseCustomerLineApiCustomerLineListFlagMap.get(goodsDTO.getEid())) {
                goodsPriceShowMap.put(goodsDTO.getId(), true);
            }
        }
        return goodsPriceShowMap;
    }


    @Override
    public Map<Long, Integer> getB2bGoodsPriceLimitByGids(List<Long> gidList, Long eid, Long buyerEid) {
        if (CollUtil.isEmpty(gidList)) {
            return Collections.emptyMap();
        }

        //是否限价
        List<Long> goodsIds = goodsLimitPriceApi.getIsGoodsPriceByGoodsIdsAndBuyerEid(gidList, eid, buyerEid);
        Map<Long, Integer> limitMap = new HashMap<>(gidList.size());
        for (Long gid : gidList) {
            if (goodsIds.contains(gid)) {
                limitMap.put(gid, 1);
            } else {
                limitMap.put(gid, 0);
            }
        }
        return limitMap;
    }

    /**
     * 计算限价
     *
     * @param goodsPriceLimitDTOList
     * @return
     */
    private BigDecimal calculateLimitPrice(List<GoodsPriceLimitDTO> goodsPriceLimitDTOList, EnterpriseDTO enterpriseDTO) {
        if (CollUtil.isEmpty(goodsPriceLimitDTOList)) {
            return null;
        }
        //全部匹配的情况
        List<GoodsPriceLimitDTO> allGoodsPriceLimitDTOList = goodsPriceLimitDTOList.stream().filter(e -> e.getRegionCode().equals(enterpriseDTO.getRegionCode()) && e.getCustomerType().equals(enterpriseDTO.getType())).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(allGoodsPriceLimitDTOList)) {
            return allGoodsPriceLimitDTOList.get(0).getPrice();
        }
        //区匹配上,类型没有设置
        List<GoodsPriceLimitDTO> typeGoodsPriceLimitDTOList = goodsPriceLimitDTOList.stream().filter(e -> e.getRegionCode().equals(enterpriseDTO.getRegionCode()) && e.getCustomerType() == 0).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(typeGoodsPriceLimitDTOList)) {
            return typeGoodsPriceLimitDTOList.get(0).getPrice();
        }
        //市匹配、区为0&&类型匹配上
        List<GoodsPriceLimitDTO> cityTypeGoodsPriceLimitDTOList = goodsPriceLimitDTOList.stream().filter(e -> StrUtil.isEmpty(e.getRegionCode()) && e.getCityCode().equals(enterpriseDTO.getCityCode()) && e.getCustomerType().equals(enterpriseDTO.getType())).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(cityTypeGoodsPriceLimitDTOList)) {
            return cityTypeGoodsPriceLimitDTOList.get(0).getPrice();
        }
        //市匹配、区为0&&类型没有匹配
        List<GoodsPriceLimitDTO> cityGoodsPriceLimitDTOList = goodsPriceLimitDTOList.stream().filter(e -> StrUtil.isEmpty(e.getRegionCode()) && e.getCityCode().equals(enterpriseDTO.getCityCode()) && e.getCustomerType() == 0).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(cityGoodsPriceLimitDTOList)) {
            return cityGoodsPriceLimitDTOList.get(0).getPrice();
        }
        //省匹配市、区为0&&类型匹配
        List<GoodsPriceLimitDTO> provinceTypeGoodsPriceLimitDTOList = goodsPriceLimitDTOList.stream().filter(e -> StrUtil.isEmpty(e.getRegionCode()) && StrUtil.isEmpty(e.getCityCode()) && e.getProvinceCode().equals(enterpriseDTO.getProvinceCode()) && e.getCustomerType().equals(enterpriseDTO.getType())).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(provinceTypeGoodsPriceLimitDTOList)) {
            return provinceTypeGoodsPriceLimitDTOList.get(0).getPrice();
        }
        //省匹配市、区为0&&类型没有匹配
        List<GoodsPriceLimitDTO> provinceGoodsPriceLimitDTOList = goodsPriceLimitDTOList.stream().filter(e -> StrUtil.isEmpty(e.getRegionCode()) && StrUtil.isEmpty(e.getCityCode()) && e.getProvinceCode().equals(enterpriseDTO.getProvinceCode()) && e.getCustomerType() == 0).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(provinceGoodsPriceLimitDTOList)) {
            return provinceGoodsPriceLimitDTOList.get(0).getPrice();
        }
        //省、市、区为0&&类型没有匹配
        List<GoodsPriceLimitDTO> notGoodsPriceLimitDTOList = goodsPriceLimitDTOList.stream().filter(e -> StrUtil.isEmpty(e.getRegionCode()) && StrUtil.isEmpty(e.getCityCode()) && StrUtil.isEmpty(e.getProvinceCode()) && e.getCustomerType() == 0).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(notGoodsPriceLimitDTOList)) {
            return notGoodsPriceLimitDTOList.get(0).getPrice();
        }
        return null;
    }

    /**
     * 计算价格
     *
     * @param originalPrice 原价
     * @param priceRule     定价规则：1-浮动点位 2-具体价格
     * @param priceValue    浮动点位/价格
     * @param precision     精度
     * @return
     */
    private BigDecimal calculatePrice(BigDecimal originalPrice, Integer priceRule, BigDecimal priceValue,int precision) {
        if (priceRule == 1) {
            return NumberUtil.add(100, priceValue).multiply(originalPrice).divide(BigDecimal.valueOf(100), precision, RoundingMode.HALF_UP);
        } else {
            return priceValue;
        }
    }
}
