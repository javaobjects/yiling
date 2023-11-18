package com.yiling.b2b.app.goods.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.yiling.b2b.app.goods.vo.AppStandardGoodsVO;
import com.yiling.b2b.app.goods.vo.ChoicenessGoodsVO;
import com.yiling.b2b.app.goods.vo.GoodsDetailVO;
import com.yiling.b2b.app.goods.vo.GoodsSkuVO;
import com.yiling.b2b.app.goods.vo.StandardGoodsPicVO;
import com.yiling.b2b.app.promotion.vo.CombinationPackageVO;
import com.yiling.b2b.app.shop.vo.ShopDetailVO;
import com.yiling.common.web.goods.utils.PictureUrlUtils;
import com.yiling.common.web.goods.vo.GoodsItemVO;
import com.yiling.common.web.goods.vo.GoodsPriceVO;
import com.yiling.common.web.goods.vo.PresaleInfoVO;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.pojo.vo.SimpleGoodsVO;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.B2bGoodsApi;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.api.PopGoodsApi;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.goods.medicine.dto.GoodsFullDTO;
import com.yiling.goods.medicine.dto.GoodsSkuDTO;
import com.yiling.goods.medicine.dto.PopGoodsDTO;
import com.yiling.goods.medicine.dto.StandardGoodsBasicDTO;
import com.yiling.goods.medicine.enums.GoodsLineEnum;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.goods.standard.enums.StandardGoodsTypeEnum;
import com.yiling.mall.agreement.api.AgreementBusinessApi;
import com.yiling.mall.agreement.enums.GoodsLimitStatusEnum;
import com.yiling.marketing.couponactivity.api.CouponActivityApi;
import com.yiling.marketing.couponactivity.dto.request.CouponActivityExistFlagDetailRequest;
import com.yiling.marketing.couponactivity.dto.request.CouponActivityExistFlagRequest;
import com.yiling.marketing.presale.api.PresaleActivityApi;
import com.yiling.marketing.presale.dto.PresaleActivityGoodsDTO;
import com.yiling.marketing.presale.dto.request.QueryPresaleInfoRequest;
import com.yiling.marketing.promotion.api.PromotionActivityApi;
import com.yiling.marketing.promotion.dto.PromotionCombinationPackDTO;
import com.yiling.marketing.promotion.dto.PromotionGoodsLimitDTO;
import com.yiling.marketing.promotion.dto.request.PromotionAppRequest;
import com.yiling.marketing.promotion.enums.PromotionTypeEnum;
import com.yiling.marketing.strategy.api.StrategyActivityApi;
import com.yiling.marketing.strategy.dto.request.QueryGoodsStrategyInfoRequest;
import com.yiling.pricing.goods.api.GoodsPriceApi;
import com.yiling.pricing.goods.dto.GoodsPriceDTO;
import com.yiling.pricing.goods.dto.request.QueryGoodsPriceRequest;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.shop.api.ShopApi;
import com.yiling.user.shop.dto.ShopDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;

/**
 * @author: shuang.zhang
 * @date: 2021/6/22
 */
@Component
public class GoodsAssemblyUtils {

    @DubboReference
    GoodsApi             goodsApi;
    @DubboReference
    B2bGoodsApi          b2bGoodsApi;
    @DubboReference
    PopGoodsApi          popGoodsApi;
    @DubboReference
    GoodsPriceApi        goodsPriceApi;
    @DubboReference
    EnterpriseApi        enterpriseApi;
    @DubboReference
    PromotionActivityApi promotionActivityApi;
    @DubboReference
    ShopApi              shopApi;
    @DubboReference
    AgreementBusinessApi agreementBusinessApi;
    @DubboReference
    CouponActivityApi    couponActivityApi;
    @DubboReference
    StrategyActivityApi strategyActivityApi;
    @DubboReference
    PresaleActivityApi   presaleActivityApi;
    @Autowired
    PictureUrlUtils      pictureUrlUtils;

    /**
     * 标准库商品信息转前端商品信息对象
     *
     * @param standardGoodsBasicDTO
     * @return
     */
    public SimpleGoodsVO toSimpleGoodsVO(StandardGoodsBasicDTO standardGoodsBasicDTO) {
        if (standardGoodsBasicDTO == null) {
            return null;
        }

        SimpleGoodsVO simpleGoodsVO = PojoUtils.map(standardGoodsBasicDTO, SimpleGoodsVO.class);
        simpleGoodsVO.setPictureUrl(pictureUrlUtils.getGoodsPicUrl(standardGoodsBasicDTO.getPic()));
        simpleGoodsVO.setSpecification(standardGoodsBasicDTO.getSellSpecifications());
        simpleGoodsVO.setUnit(standardGoodsBasicDTO.getSellUnit());
        simpleGoodsVO.setName(standardGoodsBasicDTO.getStandardGoods().getName());
        simpleGoodsVO.setManufacturer(standardGoodsBasicDTO.getStandardGoods().getManufacturer());
        simpleGoodsVO.setLicenseNo(standardGoodsBasicDTO.getStandardGoods().getLicenseNo());
        return simpleGoodsVO;
    }

    /**
     * 后端商品所有详情转前端商品详情
     *
     * @param goodsFullDTO
     * @return
     */
    public GoodsDetailVO toGoodsDetailVO(GoodsFullDTO goodsFullDTO) {
        GoodsDetailVO goodsDetailVO = PojoUtils.map(goodsFullDTO.getStandardGoodsAllInfo(), GoodsDetailVO.class);
        {
            //设置商品基础信息
            SimpleGoodsVO goodsInfo = new SimpleGoodsVO();
            goodsInfo.setId(goodsFullDTO.getId());
            goodsInfo.setName(goodsFullDTO.getStandardGoodsAllInfo().getBaseInfo().getName());
            goodsInfo.setGoodsType(goodsFullDTO.getGoodsType());
            goodsInfo.setLicenseNo(goodsFullDTO.getStandardGoodsAllInfo().getBaseInfo().getLicenseNo());
            goodsInfo.setManufacturer(goodsFullDTO.getStandardGoodsAllInfo().getBaseInfo().getManufacturer());
            goodsInfo.setSpecification(goodsFullDTO.getSellSpecifications());
            goodsInfo.setUnit(goodsFullDTO.getSellUnit());
            goodsInfo.setPictureUrl(pictureUrlUtils.getGoodsPicUrl(goodsFullDTO.getPic()));
            goodsDetailVO.setSellSpecificationsId(goodsFullDTO.getSellSpecificationsId());
            goodsDetailVO.setManufacturingDate(goodsFullDTO.getManufacturingDate());
            goodsDetailVO.setExpiryDate(goodsFullDTO.getExpiryDate());
            goodsDetailVO.setGoodsInfo(goodsInfo);
        }

        {
            //设置以岭商品Id
            Map<Long, Long> ylGoodsMap = this.getYlGoodsIdBySellSpecificationsIds(Arrays.asList(goodsFullDTO.getSellSpecificationsId()));
            if (ylGoodsMap.getOrDefault(goodsFullDTO.getSellSpecificationsId(), 0L) == 0) {
                goodsDetailVO.setYlGoodsId(goodsFullDTO.getId());
            } else {
                goodsDetailVO.setYlGoodsId(ylGoodsMap.get(goodsFullDTO.getSellSpecificationsId()));
            }
        }

        {
            //如果商品图片为空需要赋值默认图片
            if (CollUtil.isEmpty(goodsDetailVO.getPicBasicsInfoList())) {
                StandardGoodsPicVO standardGoodsPicVO = new StandardGoodsPicVO();
                standardGoodsPicVO.setPic(goodsFullDTO.getPic());
                standardGoodsPicVO.setPicDefault(1);
                standardGoodsPicVO.setPicOrder(0);
                List<StandardGoodsPicVO> picBasicsInfoList = new ArrayList<>();
                picBasicsInfoList.add(standardGoodsPicVO);
                picBasicsInfoList.forEach(e -> {
                    e.setPicUrl(pictureUrlUtils.getGoodsPicUrl(e.getPic()));
                });
                goodsDetailVO.setPicBasicsInfoList(picBasicsInfoList);
            } else {
                goodsDetailVO.getPicBasicsInfoList().forEach(e -> {
                    e.setPicUrl(pictureUrlUtils.getGoodsPicUrl(e.getPic()));
                });
            }
        }

        {
            //app标准商品信息
            AppStandardGoodsVO appStandardGoodsVO = new AppStandardGoodsVO();
            appStandardGoodsVO.setGdfName(goodsFullDTO.getStandardGoodsAllInfo().getBaseInfo().getGdfName());
            appStandardGoodsVO.setIsCn(goodsFullDTO.getStandardGoodsAllInfo().getBaseInfo().getIsCn());
            appStandardGoodsVO.setManufacturerAddress(goodsFullDTO.getStandardGoodsAllInfo().getBaseInfo().getManufacturerAddress());
            appStandardGoodsVO.setOtcType(goodsFullDTO.getStandardGoodsAllInfo().getBaseInfo().getOtcType());
            appStandardGoodsVO.setBusinessScope(goodsFullDTO.getStandardGoodsAllInfo().getBaseInfo().getBusinessScope());
            Integer goodsType = goodsFullDTO.getStandardGoodsAllInfo().getBaseInfo().getGoodsType();
            if (goodsType != null) {
                StandardGoodsTypeEnum goodsTypeEnum = StandardGoodsTypeEnum.find(goodsType);
                switch (goodsTypeEnum){
                    case GOODS_TYPE:
                        if (goodsFullDTO.getStandardGoodsAllInfo().getGoodsInstructionsInfo() != null) {
                            appStandardGoodsVO.setExpirationDate(goodsFullDTO.getStandardGoodsAllInfo().getGoodsInstructionsInfo().getShelfLife());
                        }
                        break;
                    case DECOCTION_TYPE:
                        if (goodsFullDTO.getStandardGoodsAllInfo().getDecoctionInstructionsInfo() != null) {
                            appStandardGoodsVO.setExpirationDate(goodsFullDTO.getStandardGoodsAllInfo().getDecoctionInstructionsInfo().getExpirationDate());
                        }
                        break;
                    case MATERIAL_TYPE:
                        if (goodsFullDTO.getStandardGoodsAllInfo().getMaterialsInstructionsInfo() != null) {
                            appStandardGoodsVO.setExpirationDate(goodsFullDTO.getStandardGoodsAllInfo().getMaterialsInstructionsInfo().getExpirationDate());
                        }
                        break;
                    case DISINFECTION_TYPE:
                        if (goodsFullDTO.getStandardGoodsAllInfo().getDisinfectionInstructionsInfo() != null) {
                            appStandardGoodsVO.setExpirationDate(goodsFullDTO.getStandardGoodsAllInfo().getDisinfectionInstructionsInfo().getExpirationDate());
                        }
                        break;
                    case HEALTH_TYPE:
                        if (goodsFullDTO.getStandardGoodsAllInfo().getHealthInstructionsInfo() != null) {
                            appStandardGoodsVO.setExpirationDate(goodsFullDTO.getStandardGoodsAllInfo().getHealthInstructionsInfo().getExpirationDate());
                        }
                        break;
                    case FOODS_TYPE:
                        if (goodsFullDTO.getStandardGoodsAllInfo().getFoodsInstructionsInfo() != null) {
                            appStandardGoodsVO.setExpirationDate(goodsFullDTO.getStandardGoodsAllInfo().getFoodsInstructionsInfo().getExpirationDate());
                        }
                        break;
                    case MEDICAL_INSTRUMENT_TYPE:
                        if (goodsFullDTO.getStandardGoodsAllInfo().getMedicalInstrumentInfo() != null) {
                            appStandardGoodsVO.setExpirationDate(goodsFullDTO.getStandardGoodsAllInfo().getMedicalInstrumentInfo().getExpirationDate());
                        }
                        break;
                    case DISPENSING_GRANULE_TYPE:
                        if (goodsFullDTO.getStandardGoodsAllInfo().getDispensingGranuleInfo() != null) {
                            appStandardGoodsVO.setExpirationDate(goodsFullDTO.getStandardGoodsAllInfo().getDispensingGranuleInfo().getExpirationDate());
                        }
                        break;
                }
            }
            goodsDetailVO.setAppStandardGoodsVO(appStandardGoodsVO);
        }
        return goodsDetailVO;
    }

    public List<GoodsItemVO> assembly(List<Long> goodsIds, Long buyerEid) {
        List<GoodsItemVO> goodsList = new ArrayList<>(goodsIds.size());

        //获取标准库商品信息
        List<StandardGoodsBasicDTO> standardGoodsDTOList = goodsApi.batchQueryStandardGoodsBasic(goodsIds);
        Map<Long, StandardGoodsBasicDTO> goodsInfoDTOMap = standardGoodsDTOList.stream().collect(Collectors.toMap(StandardGoodsBasicDTO::getId, Function.identity()));

        //获取商品定价系统
        QueryGoodsPriceRequest queryGoodsPriceRequest = new QueryGoodsPriceRequest();
        queryGoodsPriceRequest.setCustomerEid(buyerEid);
        queryGoodsPriceRequest.setGoodsIds(goodsIds);
        Map<Long, GoodsPriceDTO> priceMap = goodsPriceApi.queryB2bGoodsPriceInfoMap(queryGoodsPriceRequest);

        //获取预售信息
        Map<Long, PresaleActivityGoodsDTO> presaleInfoMap;
        QueryPresaleInfoRequest presaleRequest = new QueryPresaleInfoRequest();
        presaleRequest.setBuyEid(buyerEid);
        presaleRequest.setGoodsId(goodsIds);
        presaleRequest.setPlatformSelected(1);
        List<PresaleActivityGoodsDTO> presaleInfoList = presaleActivityApi.getPresaleInfoByGoodsIdAndBuyEid(presaleRequest);
        if(CollectionUtil.isNotEmpty(presaleInfoList)){
            presaleInfoMap = presaleInfoList.stream().collect(Collectors.toMap(PresaleActivityGoodsDTO::getGoodsId, Function.identity(),(k1, k2)->k1));
        }else {
            presaleInfoMap = new HashMap<>();
        }
        List<Long> eids = shopApi.getSellEidByEidSaleArea(buyerEid);
        List<Long> sellSpecificationIds = standardGoodsDTOList.stream().map(e -> e.getSellSpecificationsId()).collect(Collectors.toList());
        Map<Long, List<Long>> sellSpecificationMap = b2bGoodsApi.getSellerGoodsIdsBySellSpecificationsIds(sellSpecificationIds, eids);

        Map<Long, Long> ylGoodsMap = getYlGoodsIdBySellSpecificationsIds(sellSpecificationIds);

        goodsIds.forEach(e -> {
            GoodsItemVO goodsItemVO = new GoodsItemVO();
            StandardGoodsBasicDTO goodsInfoDTO = goodsInfoDTOMap.get(e);
            if (goodsInfoDTO != null) {
                goodsItemVO.setGoodsInfo(this.toSimpleGoodsVO(goodsInfoDTO));
                goodsItemVO.setEname(goodsInfoDTO.getEname());
                goodsItemVO.setEid(goodsInfoDTO.getEid());
                if (ylGoodsMap.getOrDefault(goodsInfoDTO.getSellSpecificationsId(), 0L) == 0) {
                    goodsItemVO.setYlGoodsId(e);
                } else {
                    goodsItemVO.setYlGoodsId(ylGoodsMap.get(goodsInfoDTO.getSellSpecificationsId()));
                }

                GoodsPriceVO goodsPriceVO = new GoodsPriceVO();
                GoodsPriceDTO goodsPriceDTO=priceMap.get(e);
                goodsPriceVO.setBuyPrice(goodsPriceDTO.getBuyPrice());
                goodsPriceVO.setLinePrice(goodsPriceDTO.getLinePrice());
                goodsPriceVO.setIsShow(goodsPriceDTO.getIsShow());
                goodsItemVO.setPriceInfo(goodsPriceVO);
                goodsItemVO.setSellerCount(sellSpecificationMap.getOrDefault(goodsInfoDTO.getSellSpecificationsId(), ListUtil.empty()).size());
                goodsItemVO.setSellSpecificationsId(goodsInfoDTO.getSellSpecificationsId());
                //预售
                goodsItemVO.setPresaleInfoVO(PojoUtils.map(presaleInfoMap.get(e), PresaleInfoVO.class));
                goodsList.add(goodsItemVO);
            }
        });
        return goodsList;
    }

    public List<CombinationPackageVO> combinationPackageAssembly(List<Long> goodsIds, Long buyerEid){
        List<CombinationPackageVO> combinationPackageVOList = Lists.newArrayList();
        //获取商品关联活动
        PromotionAppRequest combinationPackageRequest = PromotionAppRequest.builder().goodsIdList(goodsIds).build();
        combinationPackageRequest.setBuyerEid(buyerEid);
        combinationPackageRequest.setPlatform(PlatformEnum.B2B.getCode());
        combinationPackageRequest.setTypeList(Lists.newArrayList(PromotionTypeEnum.COMBINATION_PACKAGE.getType()));
        List<PromotionGoodsLimitDTO> combinationPackages = promotionActivityApi.queryGoodsPromotionInfo(combinationPackageRequest);
        Set<Long> priceGoodsIds= new HashSet<>();
        //获取商品关联组合包活动
        if(CollectionUtil.isNotEmpty(combinationPackages)){
            combinationPackages.forEach(promotion->{
                promotion.getGoodsLimitDTOS().forEach(goodsLimit->{
                    priceGoodsIds.add(goodsLimit.getGoodsId());
                } );
            });
        }

        priceGoodsIds.addAll(goodsIds);
        //获取商品定价系统
        QueryGoodsPriceRequest queryGoodsPriceRequest = new QueryGoodsPriceRequest();
        queryGoodsPriceRequest.setCustomerEid(buyerEid);
        queryGoodsPriceRequest.setGoodsIds(Lists.newArrayList(priceGoodsIds));
        Map<Long, GoodsPriceDTO> priceMap = goodsPriceApi.queryB2bGoodsPriceInfoMap(queryGoodsPriceRequest);
        combinationPackages.forEach(packageGoods->{
            CombinationPackageVO goodsListItemVO = PojoUtils.map(packageGoods, CombinationPackageVO.class);
            List<PromotionGoodsLimitDTO> goodsLimitDTOS = goodsListItemVO.getGoodsLimitDTOS();
            List<BigDecimal> sellingPrice = new ArrayList<>();
            ArrayList<BigDecimal> combinationPackagePrice = new ArrayList<>();
            goodsLimitDTOS.forEach(goodsLimit->{
                GoodsPriceDTO priceDTO = priceMap.get(goodsLimit.getGoodsId());
                BigDecimal singleGoodsPrice = NumberUtil.round(NumberUtil.mul(priceDTO.getLinePrice(), goodsLimit.getAllowBuyCount()), 2);
                BigDecimal promotionGoodsPrice = NumberUtil.round(NumberUtil.mul(goodsLimit.getPromotionPrice(), goodsLimit.getAllowBuyCount()), 2);
                sellingPrice.add(singleGoodsPrice);
                combinationPackagePrice.add(promotionGoodsPrice);
            });
            goodsListItemVO.setCombinationPackagePrice(combinationPackagePrice.stream().reduce(BigDecimal::add).get());
            goodsListItemVO.setSellingPrice(sellingPrice.stream().reduce(BigDecimal::add).get());
            goodsListItemVO.setCombinationDiscountPrice(NumberUtil.round(NumberUtil.sub(goodsListItemVO.getSellingPrice(),goodsListItemVO.getCombinationPackagePrice()),2));
            combinationPackageVOList.add(goodsListItemVO);
        });
        return combinationPackageVOList;
    }


    public List<ChoicenessGoodsVO> choicenessGoodsAssembly(List<Long> goodsIds, Long buyerEid,Long sellSpecificationsId ) {
        List<ChoicenessGoodsVO> goodsList = new ArrayList<>(goodsIds.size());

        //获取标准库商品信息
        List<StandardGoodsBasicDTO> standardGoodsDTOList = goodsApi.batchQueryStandardGoodsBasic(goodsIds);
        Map<Long, StandardGoodsBasicDTO> goodsInfoDTOMap = standardGoodsDTOList.stream().collect(Collectors.toMap(StandardGoodsBasicDTO::getId, Function.identity()));
        //获取商品关联活动
        PromotionAppRequest promotionRequest = PromotionAppRequest.builder().goodsIdList(goodsIds).build();
        promotionRequest.setPlatform(PlatformEnum.B2B.getCode());
        promotionRequest.setBuyerEid(buyerEid);
        List<PromotionGoodsLimitDTO> promotionGoodsList = promotionActivityApi.queryGoodsPromotionInfo(promotionRequest);
        Set<Long>  priceGoodsIds= new HashSet<>();
        //获取商品关联组合包活动
        // 获取组合包的限购信息
        List<Long> combinationIds = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(promotionGoodsList)){
            List<PromotionGoodsLimitDTO> combinationPackages = promotionGoodsList.stream().filter(promotionGoods -> PromotionTypeEnum.isCombinationPackage(promotionGoods.getType())).collect(Collectors.toList());
            combinationPackages.forEach(promotion->{
                promotion.getGoodsLimitDTOS().forEach(goodsLimit->{
                    priceGoodsIds.add(goodsLimit.getGoodsId());
                    combinationIds.add(promotion.getPromotionActivityId());
                } );
            });
        }

        priceGoodsIds.addAll(goodsIds);
        //获取商品定价系统
        QueryGoodsPriceRequest queryGoodsPriceRequest = new QueryGoodsPriceRequest();
        queryGoodsPriceRequest.setCustomerEid(buyerEid);
        queryGoodsPriceRequest.setGoodsIds(Lists.newArrayList(priceGoodsIds));
        Map<Long, GoodsPriceDTO> priceMap = goodsPriceApi.queryB2bGoodsPriceInfoMap(queryGoodsPriceRequest);

        //获取以岭商品ID
        List<Long> eids = shopApi.getSellEidByEidSaleArea(buyerEid);
        List<Long> sellSpecificationIds = standardGoodsDTOList.stream().map(e -> e.getSellSpecificationsId()).collect(Collectors.toList());
        Map<Long, List<Long>> sellSpecificationMap = b2bGoodsApi.getSellerGoodsIdsBySellSpecificationsIds(sellSpecificationIds, eids);
        Map<Long, Long> ylGoodsMap = getYlGoodsIdBySellSpecificationsIds(sellSpecificationIds);

        //获取满赠
        List<CouponActivityExistFlagDetailRequest> list = new ArrayList<>();
        for (Long goodsId : goodsIds) {
            CouponActivityExistFlagDetailRequest detailRequest = new CouponActivityExistFlagDetailRequest();
            detailRequest.setGoodsId(goodsId);
            detailRequest.setEid(goodsInfoDTOMap.get(goodsId).getEid());
            list.add(detailRequest);
        }

        Map<Long, List<PromotionGoodsLimitDTO>> promotionGoodsMap = promotionGoodsList.stream().collect(Collectors.groupingBy(PromotionGoodsLimitDTO::getGoodsId));

        //获取满减卷
        CouponActivityExistFlagRequest request = new CouponActivityExistFlagRequest();
        request.setDetailList(list);
        request.setCurrentEid(buyerEid);
        Map<Long, List<Integer>> couponActivityMap = couponActivityApi.getCouponActivityExistFlag(request);

        // 获取策略满赠
        QueryGoodsStrategyInfoRequest strategyInfoRequest=new QueryGoodsStrategyInfoRequest();
        strategyInfoRequest.setGoodsIdList(goodsIds);
        strategyInfoRequest.setPlatformSelected(1);
        strategyInfoRequest.setBuyerEid(buyerEid);
        strategyInfoRequest.setSellSpecificationsId(sellSpecificationsId);
        List<Long> goodsStrategyInfo = strategyActivityApi.queryGoodsStrategyInfo(strategyInfoRequest);

        //获取预售信息
        Map<Long, PresaleActivityGoodsDTO> presaleInfoMap;
        QueryPresaleInfoRequest presaleRequest = new QueryPresaleInfoRequest();
        presaleRequest.setBuyEid(buyerEid);
        presaleRequest.setGoodsId(goodsIds);
        presaleRequest.setPlatformSelected(1);
        List<PresaleActivityGoodsDTO> presaleInfoList = presaleActivityApi.getPresaleInfoByGoodsIdAndBuyEid(presaleRequest);
        if(CollectionUtil.isNotEmpty(presaleInfoList)){
            presaleInfoMap = presaleInfoList.stream().collect(Collectors.toMap(PresaleActivityGoodsDTO::getGoodsId, Function.identity(),(k1, k2)->k1));
        }else {
            presaleInfoMap = new HashMap<>();
        }

        //获取省市区
        List<EnterpriseDTO> enterpriseDTOList = enterpriseApi.listByIds(standardGoodsDTOList.stream().map(e -> e.getEid()).collect(Collectors.toList()));
        Map<Long, EnterpriseDTO> enterpriseDTOMap = enterpriseDTOList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, Function.identity()));
        Map<Long, PromotionCombinationPackDTO> combinationPackDTOMap = new HashMap<>(2);
        if (CollectionUtil.isNotEmpty(combinationIds)) {
            List<PromotionCombinationPackDTO> resullt = promotionActivityApi.quaryCombinationPackByActivityIds(combinationIds,buyerEid);
            combinationPackDTOMap = resullt.stream().collect(Collectors.toMap(PromotionCombinationPackDTO::getPromotionActivityId, o -> o, (k1, k2) -> k1));
        }
        Map<Long, PromotionCombinationPackDTO> finalCombinationPackDTOMap = combinationPackDTOMap;
        Map<Long, Integer> goodsLimitMap = agreementBusinessApi.getB2bGoodsLimitByGids(goodsIds, buyerEid);
        goodsIds.forEach(e -> {
            ChoicenessGoodsVO choicenessGoodsVO = new ChoicenessGoodsVO();
            StandardGoodsBasicDTO goodsInfoDTO = goodsInfoDTOMap.get(e);
            if (goodsInfoDTO != null) {
                choicenessGoodsVO.setGoodsInfo(this.toSimpleGoodsVO(goodsInfoDTO));
                choicenessGoodsVO.setEid(goodsInfoDTO.getEid());
                choicenessGoodsVO.setExpiryDate(goodsInfoDTO.getExpiryDate());
                if (ylGoodsMap.getOrDefault(goodsInfoDTO.getSellSpecificationsId(), 0L) == 0) {
                    choicenessGoodsVO.setYlGoodsId(e);
                } else {
                    choicenessGoodsVO.setYlGoodsId(ylGoodsMap.get(goodsInfoDTO.getSellSpecificationsId()));
                }

                GoodsPriceVO goodsPriceVO = new GoodsPriceVO();
                GoodsPriceDTO goodsPriceDTO= priceMap.get(e);
                goodsPriceVO.setBuyPrice(goodsPriceDTO.getBuyPrice());
                goodsPriceVO.setIsShow(goodsPriceDTO.getIsShow());
                choicenessGoodsVO.setPriceInfo(goodsPriceVO);
                choicenessGoodsVO.setSellerCount(sellSpecificationMap.getOrDefault(goodsInfoDTO.getSellSpecificationsId(), ListUtil.empty()).size());
                choicenessGoodsVO.setSellSpecificationsId(goodsInfoDTO.getSellSpecificationsId());
                //供应商信息
                ShopDTO shopDTO = shopApi.getShop(goodsInfoDTO.getEid());
                choicenessGoodsVO.setShopDetailVO(PojoUtils.map(shopDTO, ShopDetailVO.class));

                List<Integer> couponTypes = couponActivityMap.get(e);
                if(CollectionUtil.isNotEmpty(couponTypes)){
                    //满减卷
                    if(couponTypes.contains(1)){
                        choicenessGoodsVO.setIsReduceCoupon(true);
                    }
                    //满折卷
                    if(couponTypes.contains(2)){
                        choicenessGoodsVO.setIsDiscountCoupon(true);
                    }
                }

                choicenessGoodsVO.setHasStrategyActivity(goodsStrategyInfo.contains(e));

                // 满赠、秒杀、特价
                List<PromotionGoodsLimitDTO> goodsLimitList = promotionGoodsMap.get(e);
                if (CollUtil.isNotEmpty(goodsLimitList)) {
                    choicenessGoodsVO.setIsGiftActivity(goodsLimitList.stream().anyMatch(item -> PromotionTypeEnum.isFullGift(item.getType())));
                    choicenessGoodsVO.setHasSecKill(goodsLimitList.stream().anyMatch(item -> PromotionTypeEnum.isSecKill(item.getType())));
                    choicenessGoodsVO.setHasSpecial(goodsLimitList.stream().anyMatch(item -> PromotionTypeEnum.isSpecialPrice(item.getType())));

                    //组合包处理
                    List<PromotionGoodsLimitDTO> packageGoodsDTOS = goodsLimitList.stream().filter(item -> PromotionTypeEnum.isCombinationPackage(item.getType())).collect(Collectors.toList());
                    List<CombinationPackageVO> combinationPackageList =new ArrayList<>();
                    packageGoodsDTOS.forEach(packageGoods->{
                        CombinationPackageVO goodsListItemVO = PojoUtils.map(packageGoods, CombinationPackageVO.class);
                        List<PromotionGoodsLimitDTO> goodsLimitDTOS = goodsListItemVO.getGoodsLimitDTOS();

                        List<BigDecimal> sellingPrice = new ArrayList<>();
                        ArrayList<BigDecimal> combinationPackagePrice = new ArrayList<>();
                        goodsLimitDTOS.forEach(goodsLimit->{
                            GoodsPriceDTO priceDTO = priceMap.get(goodsLimit.getGoodsId());
                            BigDecimal singleGoodsPrice = NumberUtil.round(NumberUtil.mul(priceDTO.getLinePrice(), goodsLimit.getAllowBuyCount()), 2);
                            BigDecimal promotionGoodsPrice = NumberUtil.round(NumberUtil.mul(goodsLimit.getPromotionPrice(), goodsLimit.getAllowBuyCount()), 2);
                            sellingPrice.add(singleGoodsPrice);
                            combinationPackagePrice.add(promotionGoodsPrice);
                        });
                        goodsListItemVO.setSellingPrice(sellingPrice.stream().reduce(BigDecimal::add).get());
                        goodsListItemVO.setCombinationPackagePrice(combinationPackagePrice.stream().reduce(BigDecimal::add).get());
                        goodsListItemVO.setCombinationDiscountPrice(NumberUtil.round(NumberUtil.sub(goodsListItemVO.getSellingPrice(),goodsListItemVO.getCombinationPackagePrice()),2));
                        PromotionCombinationPackDTO promotionCombinationPackDTO = finalCombinationPackDTOMap.get(goodsListItemVO.getPromotionActivityId());
                        if(ObjectUtil.isNotNull(promotionCombinationPackDTO)){
                            goodsListItemVO.setSurplusBuyNum(promotionCombinationPackDTO.getSurplusBuyNum());
                            goodsListItemVO.setTotalNum(promotionCombinationPackDTO.getTotalNum());
                            goodsListItemVO.setPerDayNum(promotionCombinationPackDTO.getPerDayNum());
                            goodsListItemVO.setPerPersonNum(promotionCombinationPackDTO.getPerPersonNum());
                            goodsListItemVO.setReachLimit(promotionCombinationPackDTO.getReachLimit());
                        }
                        combinationPackageList.add(goodsListItemVO);
                    });
                    choicenessGoodsVO.setCombinationPackageList(combinationPackageList);
                }

                {
                    //销售包装信息
                    List<GoodsSkuDTO> goodsSkuDTOList = goodsApi.getGoodsSkuByGoodsId(e);
                    goodsSkuDTOList = goodsSkuDTOList.stream().filter(f -> f.getGoodsLine().equals(GoodsLineEnum.B2B.getCode())).collect(Collectors.toList());
                    choicenessGoodsVO.setGoodsSkuList(PojoUtils.map(goodsSkuDTOList, GoodsSkuVO.class));
                }

                EnterpriseDTO enterpriseDTO = enterpriseDTOMap.get(goodsInfoDTO.getEid());
                choicenessGoodsVO.setProvinceName(enterpriseDTO.getProvinceName());
                choicenessGoodsVO.setRegionName(enterpriseDTO.getRegionName());
                choicenessGoodsVO.setCityName(enterpriseDTO.getCityName());

                //预售
                choicenessGoodsVO.setPresaleInfoVO(PojoUtils.map(presaleInfoMap.get(e), PresaleInfoVO.class));
                //按钮显示
                if (goodsLimitMap != null) {
                    Integer limitType = goodsLimitMap.get(e);
                    choicenessGoodsVO.setGoodsLimitStatus(limitType);
                    if(GoodsLimitStatusEnum.NORMAL.getCode().equals(limitType)){
                        choicenessGoodsVO.getAddToCartButtonInfo().setText("加入采购车");
                    }
                }
                goodsList.add(choicenessGoodsVO);
            }
        });
        return goodsList;
    }

    /**
     * 通过售卖规格ID获取以岭商品ID
     *
     * @param sellSpecificationsIds
     * @return
     */
    public Map<Long, Long> getYlGoodsIdBySellSpecificationsIds(List<Long> sellSpecificationsIds) {
        Map<Long, Long> returnMap = new HashMap<>();
        List<Long> eids = enterpriseApi.listSubEids(Constants.YILING_EID);
        //以岭商品
        List<GoodsDTO> goodsDTOList = goodsApi.findGoodsBySellSpecificationsIdAndEid(sellSpecificationsIds, eids);
        List<Long> goodsIds = goodsDTOList.stream().map(e -> e.getId()).collect(Collectors.toList());
        List<PopGoodsDTO> popGoodsDTOList = popGoodsApi.getPopGoodsListByGoodsIds(goodsIds);
        Map<Long, PopGoodsDTO> popGoodsDTOMap = popGoodsDTOList.stream().collect(Collectors.toMap(PopGoodsDTO::getGoodsId, Function.identity()));

        Map<Long, List<GoodsDTO>> goodsMap = goodsDTOList.stream().collect(Collectors.groupingBy(GoodsDTO::getSellSpecificationsId));
        for (Long sellSpecificationsId : sellSpecificationsIds) {
            Long ylGoodsId = 0L;
            List<GoodsDTO> goodsList = goodsMap.get(sellSpecificationsId);
            if (CollUtil.isNotEmpty(goodsList)) {
                for (GoodsDTO goodsDTO : goodsList) {
                    if (popGoodsDTOMap.get(goodsDTO.getId()) != null && popGoodsDTOMap.get(goodsDTO.getId()).getGoodsStatus().equals(GoodsStatusEnum.UP_SHELF.getCode())) {
                        ylGoodsId = goodsDTO.getId();
                        break;
                    }
                }
                if (ylGoodsId == 0 && CollUtil.isNotEmpty(goodsList)) {
                    ylGoodsId = goodsList.get(0).getId();
                }
            }
            returnMap.put(sellSpecificationsId, ylGoodsId);
        }
        return returnMap;
    }
}
