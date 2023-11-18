package com.yiling.b2b.app.goods.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.b2b.app.common.vo.HotGoodsVO;
import com.yiling.b2b.app.goods.form.QueryActivityGoodsSearchFrom;
import com.yiling.b2b.app.goods.form.QueryEsGoodsSearchForm;
import com.yiling.b2b.app.goods.form.QueryFeaturedGoodsPageFrom;
import com.yiling.b2b.app.goods.form.QueryStandardGoodsSearchFrom;
import com.yiling.b2b.app.goods.utils.GoodsAssemblyUtils;
import com.yiling.b2b.app.goods.vo.ChoicenessGoodsVO;
import com.yiling.b2b.app.goods.vo.GoodsDetailVO;
import com.yiling.b2b.app.goods.vo.GoodsSkuVO;
import com.yiling.b2b.app.goods.vo.PurchaseRestrictionVO;
import com.yiling.b2b.app.goods.vo.QtyVO;
import com.yiling.b2b.app.goods.vo.SimpleActivityVO;
import com.yiling.b2b.app.goods.vo.StrategyActivityVO;
import com.yiling.b2b.app.promotion.vo.CombinationPackageVO;
import com.yiling.b2b.app.shop.vo.ShopDetailVO;
import com.yiling.basic.dict.api.DictApi;
import com.yiling.basic.dict.bo.DictBO;
import com.yiling.common.web.goods.utils.PictureUrlUtils;
import com.yiling.common.web.goods.vo.GoodsItemVO;
import com.yiling.common.web.goods.vo.GoodsPriceVO;
import com.yiling.common.web.goods.vo.PresaleInfoVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.annotations.UserAccessAuthentication;
import com.yiling.framework.common.base.EsAggregationDTO;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.enums.SortEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.pojo.vo.SimpleGoodsVO;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.inventory.api.InventoryApi;
import com.yiling.goods.inventory.dto.InventoryDTO;
import com.yiling.goods.medicine.api.B2bGoodsApi;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.bo.ChoicenessGoodsBO;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.goods.medicine.dto.GoodsFullDTO;
import com.yiling.goods.medicine.dto.GoodsInfoDTO;
import com.yiling.goods.medicine.dto.GoodsSkuDTO;
import com.yiling.goods.medicine.dto.StandardGoodsBasicDTO;
import com.yiling.goods.medicine.dto.request.QueryChoicenessGoodsPageListRequest;
import com.yiling.goods.medicine.enums.GoodsLineEnum;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.goods.restriction.api.GoodsPurchaseRestrictionApi;
import com.yiling.goods.restriction.dto.request.QueryGoodsPurchaseRestrictionRequest;
import com.yiling.goods.restriction.enums.RestrictionTimeTypeEnum;
import com.yiling.goods.standard.api.StandardGoodsSpecificationApi;
import com.yiling.goods.standard.dto.StandardGoodsSpecificationPicDTO;
import com.yiling.goods.standard.dto.request.IndexStandardGoodsSpecificationPageRequest;
import com.yiling.mall.agreement.api.AgreementBusinessApi;
import com.yiling.mall.agreement.enums.GoodsLimitStatusEnum;
import com.yiling.marketing.common.enums.CouponPlatformTypeEnum;
import com.yiling.marketing.couponactivity.api.CouponActivityApi;
import com.yiling.marketing.couponactivity.dto.CouponActivityEidOrGoodsIdDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityListFiveByGoodsIdDTO;
import com.yiling.marketing.presale.api.PresaleActivityApi;
import com.yiling.marketing.presale.dto.PresaleActivityGoodsDTO;
import com.yiling.marketing.presale.dto.request.QueryPresaleInfoRequest;
import com.yiling.marketing.promotion.api.PromotionActivityApi;
import com.yiling.marketing.promotion.dto.PromotionGoodsLimitDTO;
import com.yiling.marketing.promotion.dto.request.PromotionAppRequest;
import com.yiling.marketing.promotion.enums.PromotionTypeEnum;
import com.yiling.marketing.strategy.api.StrategyActivityApi;
import com.yiling.marketing.strategy.dto.StrategyActivityDTO;
import com.yiling.marketing.strategy.dto.StrategyAmountLadderDTO;
import com.yiling.marketing.strategy.dto.StrategyGiftDTO;
import com.yiling.marketing.strategy.dto.request.QueryGoodsStrategyInfoRequest;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.dto.request.QueryUserBuyNumberRequest;
import com.yiling.pricing.goods.api.GoodsPriceApi;
import com.yiling.pricing.goods.dto.GoodsPriceDTO;
import com.yiling.pricing.goods.dto.request.QueryGoodsPriceRequest;
import com.yiling.search.goods.api.EsGoodsSearchApi;
import com.yiling.search.goods.dto.request.EsActivityGoodsSearchRequest;
import com.yiling.search.goods.dto.request.EsGoodsSearchRequest;
import com.yiling.user.common.enums.EnterpriseCustomerLineEnum;
import com.yiling.user.enterprise.api.CustomerApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.api.EnterpriseHighQualitySupplierApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryCanBuyEidRequest;
import com.yiling.user.shop.api.ShopApi;
import com.yiling.user.shop.dto.ShopDTO;
import com.yiling.user.system.bo.CurrentStaffInfo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author dexi.yao
 * @date 2021-06-17
 */
@RestController
@Api(tags = "商品信息相关")
@RequestMapping("/goods")
public class GoodsController {

    @DubboReference
    EnterpriseApi                 enterpriseApi;
    @DubboReference
    B2bGoodsApi                   b2bGoodsApi;
    @DubboReference
    GoodsApi                      goodsApi;
    @DubboReference
    GoodsPriceApi                 goodsPriceApi;
    @DubboReference
    InventoryApi                  inventoryApi;
    @DubboReference
    ShopApi                       shopApi;
    @DubboReference
    CouponActivityApi             couponActivityApi;
    @DubboReference
    PromotionActivityApi          promotionActivityApi;
    @DubboReference
    AgreementBusinessApi          agreementBusinessApi;
    @DubboReference
    EsGoodsSearchApi              esGoodsSearchApi;
    @DubboReference
    DictApi                       dictApi;
    @DubboReference
    StandardGoodsSpecificationApi standardGoodsSpecificationApi;
    @DubboReference
    StrategyActivityApi strategyActivityApi;
    @DubboReference
    PresaleActivityApi            presaleActivityApi;
    @DubboReference
    CustomerApi                   customerApi;
    @DubboReference
    GoodsPurchaseRestrictionApi goodsPurchaseRestrictionApi;
    @DubboReference
    EnterpriseHighQualitySupplierApi enterpriseHighQualitySupplierApi;

    @DubboReference
    OrderApi orderApi;

    @Autowired
    PictureUrlUtils               pictureUrlUtils;
    @Autowired
    GoodsAssemblyUtils            goodsAssemblyUtils;

    private static final String EXPLAIN_TYPE = "explain_type";

    private static final String SPECIAL_EXPLAIN = "special_explain";

    private static final String POST_SALE_FLOW_EXPLAIN = "post_sale_flow_explain";


    @ApiOperation(value = "商品详情页")
    @GetMapping("/detail")
    @UserAccessAuthentication
    public Result<GoodsDetailVO> detail(
            @RequestParam("goodsId") @ApiParam(required = true, name = "goodsId", value = "商品id") Long goodsId,@RequestParam(value = "currentPage",required = false) @ApiParam(required = false, name = "currentPage", value = "当前页面（非必传） 1：组合包页面") Integer currentPage,
            @CurrentUser CurrentStaffInfo staffInfo) {
        // 企业ID
        Long buyerEid = staffInfo != null ? staffInfo.getCurrentEid() : 0L;

        //获取普通商品
        GoodsFullDTO goodsInfoDTO = goodsApi.queryFullInfo(goodsId);
        if (goodsInfoDTO == null) {
            return Result.failed("商品信息不存在");
        }
        if (!GoodsStatusEnum.AUDIT_PASS.getCode().equals(goodsInfoDTO.getAuditStatus())) {
            return Result.failed("该商品已失效");
        }
        GoodsDetailVO goodsDetailVO = goodsAssemblyUtils.toGoodsDetailVO(goodsInfoDTO);

        {
            //销售包装信息
            List<GoodsSkuDTO> goodsSkuDTOList = goodsApi.getGoodsSkuByGoodsId(goodsId);
            goodsSkuDTOList = goodsSkuDTOList.stream().filter(e -> e.getGoodsLine().equals(GoodsLineEnum.B2B.getCode())).collect(Collectors.toList());
            goodsDetailVO.setCountSku(goodsSkuDTOList.size());
            goodsDetailVO.setGoodsSkuList(PojoUtils.map(goodsSkuDTOList, GoodsSkuVO.class));
//            List<GoodsSkuDTO> expiryList = goodsSkuDTOList.stream().filter(goodsSkuDTO -> null != goodsSkuDTO.getExpiryDate()).collect(Collectors.toList());
//            if(CollectionUtil.isNotEmpty(expiryList)){
//                GoodsSkuDTO minExpirySkuDTO = expiryList.stream().min(Comparator.comparing(GoodsSkuDTO::getExpiryDate)).get();
//                goodsDetailVO.setExpiryDate(minExpirySkuDTO.getExpiryDate());
//                goodsDetailVO.setBatchNumber(minExpirySkuDTO.getBatchNumber());
//            }
        }

        {
            //销售数量
            List<Long> eids = shopApi.getSellEidByEidSaleArea(staffInfo.getCurrentEid());
            Map<Long, List<Long>> sellSpecificationMap = b2bGoodsApi.getSellerGoodsIdsBySellSpecificationsIds(Arrays.asList(goodsInfoDTO.getSellSpecificationsId()), eids);
            goodsDetailVO.setSellerCount(sellSpecificationMap.getOrDefault(goodsInfoDTO.getSellSpecificationsId(), ListUtil.empty()).size());
        }

        {
            //按钮显示
            Map<Long, Integer> goodsLimitMap = agreementBusinessApi.getB2bGoodsLimitByGids(Arrays.asList(goodsId), buyerEid);
            if (goodsLimitMap != null) {
                Integer limitType = goodsLimitMap.get(goodsId);
                goodsDetailVO.setGoodsLimitStatus(limitType);
                if(GoodsLimitStatusEnum.NORMAL.getCode().equals(limitType)){
                    goodsDetailVO.getAddToCartButtonInfo().setText("加入采购车");
                }
            }
        }

        if (buyerEid != null && buyerEid > 0) {
            //获取商品定价系统
            QueryGoodsPriceRequest queryGoodsPriceRequest = new QueryGoodsPriceRequest();
            queryGoodsPriceRequest.setCustomerEid(buyerEid);
            queryGoodsPriceRequest.setGoodsIds(Arrays.asList(goodsId));
            Map<Long, GoodsPriceDTO> priceMap = goodsPriceApi.queryB2bGoodsPriceInfoMap(queryGoodsPriceRequest);
            GoodsPriceVO goodsPriceVO = new GoodsPriceVO();
            GoodsPriceDTO goodsPriceDTO = priceMap.get(goodsId);
            goodsPriceVO.setBuyPrice(goodsPriceDTO.getBuyPrice());
            goodsPriceVO.setLinePrice(goodsPriceDTO.getLinePrice());
            goodsPriceVO.setIsShow(goodsPriceDTO.getIsShow());
            goodsDetailVO.setPriceInfo(goodsPriceVO);

            //供应商信息
            ShopDTO shopDTO = shopApi.getShop(goodsInfoDTO.getEid());
            goodsDetailVO.setShopDetailVO(PojoUtils.map(shopDTO, ShopDetailVO.class));

            //获取供应商商品前4的商品
            {
                List<GoodsDTO> goodsDTOList = b2bGoodsApi.getB2bGoodsSaleTopLimit(Arrays.asList(goodsInfoDTO.getEid()), 4);
                List<Long> goodsIds = goodsDTOList.stream().map(e -> e.getId()).collect(Collectors.toList());
                List<StandardGoodsBasicDTO> standardGoodsBasicDTOList = goodsApi.batchQueryStandardGoodsBasic(goodsIds);
                Map<Long, StandardGoodsBasicDTO> standardGoodsBasicDTOMap = standardGoodsBasicDTOList.stream().collect(Collectors.toMap(StandardGoodsBasicDTO::getId, Function.identity()));
                //获取商品定价系统
                QueryGoodsPriceRequest queryGoodsPriceRequestTop = new QueryGoodsPriceRequest();
                queryGoodsPriceRequestTop.setCustomerEid(buyerEid);
                queryGoodsPriceRequestTop.setGoodsIds(goodsIds);
                Map<Long, GoodsPriceDTO> priceMapTop = goodsPriceApi.queryB2bGoodsPriceInfoMap(queryGoodsPriceRequestTop);
                Map<Long, Integer> top4GoodsLimit = agreementBusinessApi.getB2bGoodsLimitByGids(goodsIds, buyerEid);

                List<GoodsItemVO> top4GoodsList = new ArrayList<>();
                for (GoodsDTO goodsDTO : goodsDTOList) {
                    StandardGoodsBasicDTO standardGoodsBasicDTO = standardGoodsBasicDTOMap.get(goodsDTO.getId());
                    GoodsItemVO goodsItemVO = new GoodsItemVO();
                    SimpleGoodsVO goodsInfo = new SimpleGoodsVO();
                    goodsInfo.setId(goodsDTO.getId());
                    goodsInfo.setName(standardGoodsBasicDTO.getStandardGoods().getName());
                    goodsInfo.setPictureUrl(pictureUrlUtils.getGoodsPicUrl(standardGoodsBasicDTO.getPic()));
                    goodsInfo.setGoodsType(standardGoodsBasicDTO.getStandardGoods().getGoodsType());

                    GoodsPriceVO priceInfo = new GoodsPriceVO();
                    GoodsPriceDTO goodsPriceTopDTO = priceMapTop.get(goodsDTO.getId());
                    priceInfo.setBuyPrice(goodsPriceTopDTO.getBuyPrice());
                    priceInfo.setLinePrice(goodsPriceTopDTO.getLinePrice());
                    priceInfo.setIsShow(goodsPriceTopDTO.getIsShow());
                    goodsItemVO.setGoodsInfo(goodsInfo);
                    goodsItemVO.setPriceInfo(priceInfo);
                    goodsItemVO.setGoodsLimitStatus(top4GoodsLimit.getOrDefault(goodsDTO.getId(),0));
                    top4GoodsList.add(goodsItemVO);
                }
                goodsDetailVO.setTop4GoodsList(top4GoodsList);
            }

            {
                //满减卷活动
                List<CouponActivityListFiveByGoodsIdDTO> couponActivityListFiveByGoodsIdDTOList = couponActivityApi.getListFiveByGoodsIdAndEid(goodsId, goodsInfoDTO.getEid(), 3, CouponPlatformTypeEnum.B2B.getCode());
                List<SimpleActivityVO> simpleActivityList = PojoUtils.map(couponActivityListFiveByGoodsIdDTOList, SimpleActivityVO.class);
                goodsDetailVO.setSimpleActivityList(simpleActivityList);
                //满赠活动
                PromotionAppRequest request = PromotionAppRequest.builder().goodsIdList(Arrays.asList(goodsId)).build();
                request.setPlatform(PlatformEnum.B2B.getCode());
                request.setBuyerEid(staffInfo.getCurrentEid());
                List<PromotionGoodsLimitDTO> promotionGoodsList = promotionActivityApi.queryGoodsPromotionInfo(request);
                if (CollUtil.isNotEmpty(promotionGoodsList)) {
                    List<SimpleActivityVO> giftActivity = Lists.newArrayList();
                    // 特价 & 秒杀
                    List<PromotionGoodsLimitDTO> secKillOrSpecialPrice = promotionGoodsList.stream().filter(item -> PromotionTypeEnum.isSecKillOrSpecialPrice(item.getType())).collect(Collectors.toList());
                    if (CollUtil.isNotEmpty(secKillOrSpecialPrice) && goodsPriceDTO.getIsShow()) {
                        secKillOrSpecialPrice.stream().forEach(goodsLimitDTO -> {
                            SimpleActivityVO giftActivityVO = new SimpleActivityVO();
                            giftActivityVO.setId(goodsLimitDTO.getPromotionActivityId());
                            giftActivityVO.setType(goodsLimitDTO.getType());
                            giftActivityVO.setName(goodsLimitDTO.getPromotionName() + "*限购" + goodsLimitDTO.getAllowBuyCount() + "盒");
                            giftActivityVO.setEndTime(goodsLimitDTO.getEndTime());
                            giftActivity.add(giftActivityVO);
                        });
                    }
                    // 满赠
                    List<PromotionGoodsLimitDTO> collect = promotionGoodsList.stream().filter(item -> PromotionTypeEnum.isFullGift(item.getType()) && CollUtil.isNotEmpty(item.getGoodsGiftLimitList())).collect(Collectors.toList());
                    if (CollUtil.isNotEmpty(collect)) {
                        collect.stream().forEach(goodsLimitDTO -> goodsLimitDTO.getGoodsGiftLimitList().forEach(gift -> {
                            SimpleActivityVO giftActivityVO = new SimpleActivityVO();
                            giftActivityVO.setId(goodsLimitDTO.getPromotionActivityId());
                            giftActivityVO.setType(PromotionTypeEnum.FULL_GIFT.getType());
                            giftActivityVO.setName("满" + gift.getPromotionAmount() + "元," + gift.getGiftName());
                            giftActivityVO.setEndTime(goodsLimitDTO.getEndTime());
                            giftActivity.add(giftActivityVO);
                        }));
                    }
                    goodsDetailVO.setGiftActivity(giftActivity);
                }
            }
            /**
             * 策略满赠
             */
            {
                QueryGoodsStrategyInfoRequest strategyInfoRequest = new QueryGoodsStrategyInfoRequest();
                strategyInfoRequest.setPlatformSelected(1);
                strategyInfoRequest.setBuyerEid(staffInfo.getCurrentEid());
                strategyInfoRequest.setGoodsIdList(new ArrayList<Long>() {{
                    add(goodsId);
                }});
                strategyInfoRequest.setSellSpecificationsId(goodsInfoDTO.getSellSpecificationsId());
                strategyInfoRequest.setSellerEid(goodsInfoDTO.getEid());
                List<StrategyActivityDTO> strategyActivityDTOList = strategyActivityApi.queryGoodsStrategyGift(strategyInfoRequest);
                if (CollUtil.isNotEmpty(strategyActivityDTOList)) {
                    List<StrategyActivityVO> strategyActivityList = new ArrayList<>();
                    for (StrategyActivityDTO strategyActivityDTO : strategyActivityDTOList) {
                        StrategyActivityVO strategyActivityVO = new StrategyActivityVO();
                        strategyActivityVO.setId(strategyActivityDTO.getId());
                        strategyActivityVO.setType(1);
                        strategyActivityVO.setTitle(strategyActivityDTO.getName());
                        strategyActivityVO.setBeginTime(strategyActivityDTO.getBeginTime());
                        strategyActivityVO.setEndTime(strategyActivityDTO.getEndTime());
                        // 1-按单累计匹配;2-活动结束整体匹配;3-按单匹配
                        Integer orderAmountLadderType = strategyActivityDTO.getOrderAmountLadderType();
                        // 2022.7.1-2022.7.15期间，订单累计金额满1000元赠季度会员100元券*1，全场9折券*3；满2000元赠年度会员150元券*2，双十一钜惠抽奖*3；满5000元赠年度会员150元券*2，会员体验卡*1；满8000元赠年度会员20...
                        // 2022.11.1-2022.11.15期间，单笔订单金额满100元送10元优惠券*2；满500元送8折优惠券*3，双十一抽奖*1；满1000元送8折优惠券*5，双十一抽奖*2
                        StringBuilder sb = new StringBuilder(DateUtil.format(strategyActivityDTO.getBeginTime(), "yyyy.MM.dd") + "-" + DateUtil.format(strategyActivityDTO.getEndTime(), "yyyy.MM.dd") + "期间，");
                        sb.append(3 == orderAmountLadderType ? "单笔订单金额" : "订单累计金额");
                        for (StrategyAmountLadderDTO strategyAmountLadderDTO : strategyActivityDTO.getStrategyAmountLadderList()) {
                            sb.append("满").append(strategyAmountLadderDTO.getAmountLimit().toPlainString()).append("元");
                            for (StrategyGiftDTO strategyGiftDTO : strategyAmountLadderDTO.getStrategyGiftList()) {
                                sb.append(3 == orderAmountLadderType ? "送" : "赠").append(strategyGiftDTO.getGiftName()).append("*").append(strategyGiftDTO.getCount()).append(",");
                            }
                            sb.deleteCharAt(sb.length() - 1);
                            sb.append(";");
                        }
                        strategyActivityVO.setGiftRules(sb.toString());
                        strategyActivityList.add(strategyActivityVO);
                    }
                    goodsDetailVO.setStrategyActivityList(strategyActivityList);
                }
            }
            /**
             * 预售
             */
            {
                QueryPresaleInfoRequest presaleRequest = new QueryPresaleInfoRequest();
                presaleRequest.setBuyEid(buyerEid);
                presaleRequest.setGoodsId(ListUtil.toList(goodsId));
                presaleRequest.setPlatformSelected(1);
                List<PresaleActivityGoodsDTO> presaleInfoList = presaleActivityApi.getPresaleInfoByGoodsIdAndBuyEid(presaleRequest);
                if(CollectionUtil.isNotEmpty(presaleInfoList) && null!=presaleInfoList.get(0)){
                    goodsDetailVO.setPresaleInfoVO(PojoUtils.map(presaleInfoList.get(0), PresaleInfoVO.class));
                    //非组合包页面下单按钮展示为 预定/加入进货单
                    if(null !=currentPage && currentPage !=1){
                        if(GoodsLimitStatusEnum.NORMAL.getCode().equals(goodsDetailVO.getGoodsLimitStatus())
                                && null!=goodsDetailVO.getAddToCartButtonInfo()){
                            goodsDetailVO.getAddToCartButtonInfo().setText("预定/加入采购车");
                        }
                    }
                }
            }
            /**
             * 限购
             */
            {
                QueryGoodsPurchaseRestrictionRequest restrictionRequest = new QueryGoodsPurchaseRestrictionRequest();
                restrictionRequest.setGoodsId(goodsId);
                restrictionRequest.setCustomerEid(buyerEid);
                PurchaseRestrictionVO restrictionVO = PojoUtils.map(goodsPurchaseRestrictionApi.getValidPurchaseRestriction(restrictionRequest), PurchaseRestrictionVO.class);
                if(null!=restrictionVO){
                    QueryUserBuyNumberRequest queryUserBuyNumberRequest = new QueryUserBuyNumberRequest();
                    queryUserBuyNumberRequest.setGoodId(goodsId);
                    queryUserBuyNumberRequest.setBuyerEid(buyerEid);
                    queryUserBuyNumberRequest.setStartTime(restrictionVO.getStartTime());
                    queryUserBuyNumberRequest.setEndTime(restrictionVO.getEndTime());
                    queryUserBuyNumberRequest.setSelectRuleEnum(QueryUserBuyNumberRequest.SelectRuleEnum.getByCode(restrictionVO.getTimeType()));
                    Long buyNumber = orderApi.getUserBuyNumber(queryUserBuyNumberRequest);
                    restrictionVO.setPurchaseQuantity(buyNumber);
                } else {
                    restrictionVO = new PurchaseRestrictionVO();
                    restrictionVO.setOrderRestrictionQuantity(0L);
                    restrictionVO.setTimeRestrictionQuantity(0L);
                    restrictionVO.setTimeType(RestrictionTimeTypeEnum.EVERY_DAY.getType());
                }
                goodsDetailVO.setPurchaseRestrictionVO(restrictionVO);
            }
        }
        DictBO dict = dictApi.getDictByName(EXPLAIN_TYPE);
        goodsDetailVO.setSpecialExplain(StringEscapeUtils.unescapeJava(dict.getDataList().stream().filter(dictData -> SPECIAL_EXPLAIN.equals(dictData.getLabel())).findFirst().get().getDescription()));
        goodsDetailVO.setPostSaleFlow(StringEscapeUtils.unescapeJava(dict.getDataList().stream().filter(dictData -> POST_SALE_FLOW_EXPLAIN.equals(dictData.getLabel())).findFirst().get().getDescription()));
        return Result.success(goodsDetailVO);
    }

    @ApiOperation(value = "sku库存加减校验")
    @GetMapping("/verifyInventory")
    @UserAccessAuthentication
    public Result<QtyVO> verifyInventory(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam Long
            skuId, @RequestParam Long stockNum) {
        InventoryDTO inventoryDTO = inventoryApi.getBySkuId(skuId);
        QtyVO qtyVO = new QtyVO();
        if (inventoryDTO != null) {
            Long usableInventory = inventoryDTO.getQty() - inventoryDTO.getFrozenQty();
            if (usableInventory >= stockNum) {
                qtyVO.setUsableBool(true);
                qtyVO.setUsableInventory(stockNum);
            } else {
                qtyVO.setUsableBool(false);
                qtyVO.setUsableInventory(usableInventory);
            }
        } else {
            qtyVO.setUsableBool(false);
            qtyVO.setUsableInventory(0L);
        }
        return Result.success(qtyVO);
    }

    /**
     * 查询商品信息列表
     *
     * @param form
     * @return
     */
    @UserAccessAuthentication
    @ApiOperation(value = "查询商品信息列表")
    @PostMapping(path = "/search")
    public Result<EsAggregationDTO<GoodsItemVO>> searchGoods(@RequestBody QueryEsGoodsSearchForm form, @CurrentUser CurrentStaffInfo staffInfo) {
        // 企业ID
        Long buyerEid = staffInfo != null ? staffInfo.getCurrentEid() : 0L;

        EsGoodsSearchRequest request = PojoUtils.map(form, EsGoodsSearchRequest.class);
        request.setMallFlag(1);
        request.setMallStatus(GoodsStatusEnum.UP_SHELF.getCode());
        request.setAuditStatus(GoodsStatusEnum.AUDIT_PASS.getCode());
        request.setExcludeEids(enterpriseApi.listSubEids(Constants.YILING_EID));

        if (form.getHasStock() != null) {
            if (form.getHasStock()) {
                request.setHasB2bStock(1);
            } else {
                request.setHasB2bStock(0);
            }
        }

        if (form.getPriceDesc() != null) {
            request.setSortField("price");
            if (form.getPriceDesc()) {
                request.setSortEnum(SortEnum.DESC);
            } else {
                request.setSortEnum(SortEnum.ASC);
            }
        }
        if (form.getSaleDesc() != null) {
            request.setSortField("b2b_sale_number");
            if (form.getSaleDesc()) {
                request.setSortEnum(SortEnum.DESC);
            } else {
                request.setSortEnum(SortEnum.ASC);
            }
        }

        EsAggregationDTO data = esGoodsSearchApi.searchGoods(request);
        List<GoodsInfoDTO> goodsAggDTOList = data.getData();

        if (CollUtil.isNotEmpty(goodsAggDTOList)) {
            // 商品ID集合
            List<Long> goodsIds = goodsAggDTOList.stream().map(e -> e.getId()).distinct().collect(Collectors.toList());
            data.setData(goodsAssemblyUtils.assembly(goodsIds, buyerEid));
        }
        return Result.success(data);
    }

    /**
     * 查询商品信息列表
     *
     * @param form
     * @return
     */
    @UserAccessAuthentication
    @ApiOperation(value = "商品搜索推荐")
    @PostMapping(path = "/searchGoodsSuggest")
    public Result<List<String>> searchGoodsSuggest(@RequestBody QueryEsGoodsSearchForm form, @CurrentUser CurrentStaffInfo staffInfo) {
        EsGoodsSearchRequest request = PojoUtils.map(form, EsGoodsSearchRequest.class);
        request.setMallFlag(1);
        request.setMallStatus(GoodsStatusEnum.UP_SHELF.getCode());
        request.setAuditStatus(GoodsStatusEnum.AUDIT_PASS.getCode());
        request.setExcludeEids(enterpriseApi.listSubEids(Constants.YILING_EID));
        List<String> suggest = esGoodsSearchApi.searchGoodsSuggest(request);
        return Result.success(suggest);
    }

    /**
     * 查询商品信息列表
     *
     * @param form
     * @return
     */
    @UserAccessAuthentication
    @ApiOperation(value = "查询商品活动信息列表")
    @PostMapping(path = "/searchActivityByGoods")
    public Result<EsAggregationDTO<CombinationPackageVO>> searchActivityByGoods(@RequestBody QueryEsGoodsSearchForm form, @CurrentUser CurrentStaffInfo staffInfo) {
        // 企业ID
        Long buyerEid = staffInfo != null ? staffInfo.getCurrentEid() : 0L;

        EsGoodsSearchRequest request = PojoUtils.map(form, EsGoodsSearchRequest.class);
        request.setMallFlag(1);
        request.setMallStatus(GoodsStatusEnum.UP_SHELF.getCode());
        request.setAuditStatus(GoodsStatusEnum.AUDIT_PASS.getCode());
        request.setExcludeEids(enterpriseApi.listSubEids(Constants.YILING_EID));

        if (form.getHasStock() != null) {
            if (form.getHasStock()) {
                request.setHasB2bStock(1);
            } else {
                request.setHasB2bStock(0);
            }
        }
        EsAggregationDTO data = esGoodsSearchApi.searchGoods(request);
        List<GoodsInfoDTO> goodsAggDTOList = data.getData();
        List<CombinationPackageVO> combinationPackageVOList = Lists.newArrayList();
        if (CollUtil.isNotEmpty(goodsAggDTOList)) {
            // 商品ID集合
            List<Long> goodsIds = goodsAggDTOList.stream().map(e -> e.getId()).distinct().collect(Collectors.toList());
            data.setData(goodsAssemblyUtils.combinationPackageAssembly(goodsIds,buyerEid));
        }
        return Result.success(data);
    }

    /**
     * 查询标准库信息列表
     *
     * @param form
     * @return
     */
    @UserAccessAuthentication
    @ApiOperation(value = "商品准库库列表")
    @PostMapping(path = "/searchStandard")
    @Deprecated
    public Result<Page<HotGoodsVO>> searchStandard(@RequestBody QueryStandardGoodsSearchFrom form, @CurrentUser CurrentStaffInfo staffInfo) {
        // 企业ID
        Long buyerEid = ObjectUtil.isNotNull(staffInfo) ? staffInfo.getCurrentEid() : 0L;

        //获取销售商品ID
        List<Long> eids = shopApi.getSellEidByEidSaleArea(buyerEid);
        if (CollUtil.isEmpty(eids)) {
            return Result.success(new Page<>());
        }
        IndexStandardGoodsSpecificationPageRequest request = PojoUtils.map(form, IndexStandardGoodsSpecificationPageRequest.class);
        request.setBuyerEid(buyerEid);
        request.setIncludeEids(eids);
        Page<StandardGoodsSpecificationPicDTO> data = standardGoodsSpecificationApi.getIndexStandardGoodsSpecificationInfoPage(request);
        Page<HotGoodsVO> goodsVOPage = PojoUtils.map(data, HotGoodsVO.class);
        List<HotGoodsVO> goodsVOPageRecords = goodsVOPage.getRecords();

        List<Long> specificationIdList = goodsVOPageRecords.stream().map(HotGoodsVO::getId).collect(Collectors.toList());
        Map<Long, List<Long>> sellSpecificationMap = b2bGoodsApi.getSellerGoodsIdsBySellSpecificationsIds(specificationIdList, eids);


        goodsVOPageRecords.forEach(hotGoodsVO -> {
            BigDecimal minPrice = BigDecimal.ZERO;
            List<Long> goodsIds = sellSpecificationMap.getOrDefault(hotGoodsVO.getId(), ListUtil.empty());
            if (CollUtil.isNotEmpty(goodsIds)) {
                //获取商品定价系统
                QueryGoodsPriceRequest queryGoodsPriceRequest = new QueryGoodsPriceRequest();
                queryGoodsPriceRequest.setCustomerEid(staffInfo.getCurrentEid());
                queryGoodsPriceRequest.setGoodsIds(goodsIds);
                Map<Long, GoodsPriceDTO> priceMap = goodsPriceApi.queryB2bGoodsPriceInfoMap(queryGoodsPriceRequest);

                for (Map.Entry<Long, GoodsPriceDTO> entry : priceMap.entrySet()) {
                    GoodsPriceDTO goodsPriceDTO=entry.getValue();
                    if (goodsPriceDTO.getIsShow()) {
                        if (minPrice.compareTo(BigDecimal.ZERO) == 0) {
                            minPrice = goodsPriceDTO.getBuyPrice();
                        } else if (minPrice.compareTo(goodsPriceDTO.getBuyPrice()) > 0) {
                            minPrice = goodsPriceDTO.getBuyPrice();
                        }
                    }
                }
            }
            GoodsPriceVO goodsPriceVO = new GoodsPriceVO();
            goodsPriceVO.setBuyPrice(minPrice);
            if (minPrice.compareTo(BigDecimal.ZERO) == 0) {
                goodsPriceVO.setIsShow(false);
            } else {
                goodsPriceVO.setIsShow(true);
            }
            hotGoodsVO.setGoodsPriceVO(goodsPriceVO);
            hotGoodsVO.setSellerCount(sellSpecificationMap.getOrDefault(hotGoodsVO.getId(), ListUtil.empty()).size());

            hotGoodsVO.setPic(pictureUrlUtils.getGoodsPicUrl(hotGoodsVO.getPic()));
            hotGoodsVO.setSellSpecificationsId(hotGoodsVO.getId());
        });
        return Result.success(goodsVOPage);
    }

    @UserAccessAuthentication
    @ApiOperation(value = "搜索标准规格")
    @PostMapping(path = "/searchStandardSpecification")
    public Result<Page<HotGoodsVO>> searchStandardSpecification(@RequestBody QueryStandardGoodsSearchFrom form, @CurrentUser CurrentStaffInfo staffInfo){
        Long buyerEid = staffInfo != null ? staffInfo.getCurrentEid() : 0L;
        IndexStandardGoodsSpecificationPageRequest request = PojoUtils.map(form, IndexStandardGoodsSpecificationPageRequest.class);
        if(StringUtils.isBlank(form.getName())){
            request.setYlFlag(1);
        }
        //获取销售商品ID
        List<Long> eids = shopApi.getSellEidByEidSaleArea(buyerEid);
        if (CollUtil.isEmpty(eids)) {
            return Result.success(new Page<>());
        }
        request.setIncludeEids(eids);
        Page<StandardGoodsSpecificationPicDTO> page = standardGoodsSpecificationApi.querySpecificationByB2b(request);
        Page<HotGoodsVO> goodsVOPage = PojoUtils.map(page, HotGoodsVO.class);
        if(CollectionUtil.isNotEmpty(goodsVOPage.getRecords())){
            List<Long> specIds = page.getRecords().stream().map(StandardGoodsSpecificationPicDTO::getId).collect(Collectors.toList());
            //每个规格最低价
            Map<Long, BigDecimal> priceMap = b2bGoodsApi.getMinPriceBySpecificationsIds(specIds);
            //每个规格的售卖商品id
            Map<Long, List<Long>> sellerEidMap = b2bGoodsApi.getSellerEidsBySellSpecificationsIds(specIds,eids);
            //获取建采商家
            QueryCanBuyEidRequest enterpriseRequest = new QueryCanBuyEidRequest();
            enterpriseRequest.setCustomerEid(buyerEid);
            enterpriseRequest.setLine(EnterpriseCustomerLineEnum.B2B.getCode());
            List<Long> purchaseEids = customerApi.getEidListByCustomerEid(enterpriseRequest);
            goodsVOPage.getRecords().forEach(hotGoodsVO -> {
                hotGoodsVO.setSellSpecificationsId(hotGoodsVO.getId());
                BigDecimal minPrice = priceMap.getOrDefault(hotGoodsVO.getSellSpecificationsId(),BigDecimal.ZERO);
                GoodsPriceVO goodsPriceVO = new GoodsPriceVO();
                goodsPriceVO.setMinPrice(minPrice);
                if (minPrice.compareTo(BigDecimal.ZERO) == 0) {
                    goodsPriceVO.setIsShow(false);
                } else {
                    goodsPriceVO.setIsShow(true);
                }
                hotGoodsVO.setGoodsPriceVO(goodsPriceVO);
                List<Long> sellerEids = sellerEidMap.getOrDefault(hotGoodsVO.getId(), ListUtil.empty());
                hotGoodsVO.setSellerCount(sellerEids.size());
                //在售卖商家内取建采商家
                List<Long> purchaseEidsBySeller = ListUtil.toList(CollectionUtil.intersection(purchaseEids, sellerEids));
                hotGoodsVO.setPurchaseCount(purchaseEidsBySeller.size());
                hotGoodsVO.setPic(pictureUrlUtils.getGoodsPicUrl(hotGoodsVO.getPic()));
            });
        }

        return Result.success(goodsVOPage);
    }

    /**
     * 查询某个活动下面的活动商品
     *
     * @param form
     * @return
     */
    @UserAccessAuthentication
    @ApiOperation(value = "查询某个活动下面的活动商品")
    @PostMapping(path = "/activityGoodsSearch")
    public Result<EsAggregationDTO<GoodsItemVO>> activityGoodsSearch(@RequestBody QueryActivityGoodsSearchFrom form, @CurrentUser CurrentStaffInfo staffInfo) {
        // 企业ID
        Long buyerEid = staffInfo != null ? staffInfo.getCurrentEid() : 0L;

        EsActivityGoodsSearchRequest request = PojoUtils.map(form, EsActivityGoodsSearchRequest.class);
        request.setMallFlag(1);
        request.setMallStatus(GoodsStatusEnum.UP_SHELF.getCode());
        request.setAuditStatus(GoodsStatusEnum.AUDIT_PASS.getCode());

        CouponActivityEidOrGoodsIdDTO couponActivityEidOrGoodsIdDTO = couponActivityApi.getGoodsListByGoodsIdAndEid(form.getGoodsId(), form.getEid());
        if (couponActivityEidOrGoodsIdDTO == null) {
            return Result.success(new EsAggregationDTO<GoodsItemVO>());
        }
        if(buyerEid > 0){
            QueryCanBuyEidRequest enterpriseRequest = new QueryCanBuyEidRequest();
            enterpriseRequest.setCustomerEid(buyerEid);
            enterpriseRequest.setLine(EnterpriseCustomerLineEnum.B2B.getCode());
            enterpriseRequest.setLimit(50);
            List<Long> sortEid = customerApi.getEidListByCustomerEid(enterpriseRequest);
            request.setSortEid(sortEid);
        }
        request.setAllEidFlag(couponActivityEidOrGoodsIdDTO.getAllEidFlag());
        request.setEidList(couponActivityEidOrGoodsIdDTO.getEidList());
        request.setGoodsIdList(couponActivityEidOrGoodsIdDTO.getGoodsIdList());
        request.setExcludeEids(enterpriseApi.listSubEids(Constants.YILING_EID));
        EsAggregationDTO data = esGoodsSearchApi.searchActivityGoods(request);
        List<GoodsInfoDTO> goodsAggDTOList = data.getData();

        if (CollUtil.isNotEmpty(goodsAggDTOList)) {
            // 商品ID集合
            List<Long> goodsIds = goodsAggDTOList.stream().map(e -> e.getId()).distinct().collect(Collectors.toList());
            data.setData(goodsAssemblyUtils.assembly(goodsIds, buyerEid));
        }
        return Result.success(data);
    }


    /**
     * 查询某个精选商品商家列表
     *
     * @param from
     * @return
     */
    @UserAccessAuthentication
    @ApiOperation(value = "查询某个精选商品商家列表")
    @Deprecated
    @PostMapping(path = "/choicenessGoodsList")
    public Result<Page<ChoicenessGoodsVO>> choicenessGoodsList(@RequestBody QueryFeaturedGoodsPageFrom from,
                                                               @CurrentUser CurrentStaffInfo staffInfo) {
        // 企业ID
        Long buyerEid = staffInfo != null ? staffInfo.getCurrentEid() : 0L;
        QueryChoicenessGoodsPageListRequest request = PojoUtils.map(from,QueryChoicenessGoodsPageListRequest.class);
        request.setCustomerEid(buyerEid);
        List<Long> eids = shopApi.getSellEidByEidSaleArea(buyerEid);
        request.setEids(eids);
        Page<ChoicenessGoodsVO> newPageList = new Page<>();
        if (CollUtil.isEmpty(eids)) {
            return Result.success(newPageList);
        }
        QueryCanBuyEidRequest enterpriseRequest = new QueryCanBuyEidRequest();
        enterpriseRequest.setCustomerEid(buyerEid);
        enterpriseRequest.setLine(EnterpriseCustomerLineEnum.B2B.getCode());
        List<Long> purchaseEids = customerApi.getEidListByCustomerEid(enterpriseRequest);
        request.setPurchaseEids(purchaseEids);
        Page<ChoicenessGoodsBO> pageList = b2bGoodsApi.getChoicenessByCustomerAndSellSpecificationsId(request);
        if (CollUtil.isNotEmpty(pageList.getRecords())) {
            List<Long> goodsIds = pageList.getRecords().stream().map(e -> e.getGoodsId()).collect(Collectors.toList());
            List<ChoicenessGoodsVO> goodsItemVOList = goodsAssemblyUtils.choicenessGoodsAssembly(goodsIds, buyerEid,request.getSellSpecificationsId());
            //翻页
            newPageList.setRecords(goodsItemVOList);
            newPageList.setSize(pageList.getSize());
            newPageList.setCurrent(pageList.getCurrent());
            newPageList.setTotal(pageList.getTotal());
        }
        return Result.success(newPageList);
    }

    @UserAccessAuthentication
    @ApiOperation(value = "查询某个精选商品商家列表（get废弃）")
    @Deprecated
    @GetMapping(path = "/choicenessGoodsList")
    public Result<Page<ChoicenessGoodsVO>> choicenessGoodsList(@RequestParam("sellSpecificationsId") @ApiParam(required = true, name = "sellSpecificationsId", value = "商品售卖规格id") Long sellSpecificationsId,
                                                               @CurrentUser CurrentStaffInfo staffInfo) {
        // 企业ID
        Long buyerEid = staffInfo != null ? staffInfo.getCurrentEid() : 0L;
        QueryChoicenessGoodsPageListRequest request = new QueryChoicenessGoodsPageListRequest();
        request.setCustomerEid(buyerEid);
        request.setSellSpecificationsId(sellSpecificationsId);
        List<Long> eids = shopApi.getSellEidByEidSaleArea(buyerEid);
        request.setEids(eids);
        Page<ChoicenessGoodsVO> newPageList = new Page<>();
        if (CollUtil.isEmpty(eids)) {
            return Result.success(newPageList);
        }
        Page<ChoicenessGoodsBO> pageList = b2bGoodsApi.getChoicenessByCustomerAndSellSpecificationsId(request);
        if (CollUtil.isNotEmpty(pageList.getRecords())) {
            List<Long> goodsIds = pageList.getRecords().stream().map(e -> e.getGoodsId()).collect(Collectors.toList());
            List<ChoicenessGoodsVO> goodsItemVOList = goodsAssemblyUtils.choicenessGoodsAssembly(goodsIds, buyerEid,sellSpecificationsId);
            //翻页
            newPageList.setRecords(goodsItemVOList);
            newPageList.setSize(pageList.getSize());
            newPageList.setCurrent(pageList.getCurrent());
            newPageList.setTotal(pageList.getTotal());
        }
        return Result.success(newPageList);
    }

    @UserAccessAuthentication
    @ApiOperation(value = "查询规格下配送商商品")
    @PostMapping(path = "/queryDistributorGoodsBySpec")
    public Result<Page<ChoicenessGoodsVO>> queryDistributorGoodsBySpec(@RequestBody QueryFeaturedGoodsPageFrom from,
                                                               @CurrentUser CurrentStaffInfo staffInfo){
        // 企业ID
        Long buyerEid = staffInfo != null ? staffInfo.getCurrentEid() : 0L;
        QueryChoicenessGoodsPageListRequest request = PojoUtils.map(from, QueryChoicenessGoodsPageListRequest.class);
        String buyerProvinceCode = "";
        EnterpriseDTO enterpriseDTO = enterpriseApi.getById(buyerEid);
        if(null!=enterpriseDTO){
            buyerProvinceCode = enterpriseDTO.getProvinceCode();
        }
        request.setProvinceCode(buyerProvinceCode);
        //可销售商家
        List<Long> eids = shopApi.getSellEidByEidSaleArea(buyerEid);
        Page<ChoicenessGoodsVO> newPageList = new Page<>();
        if (CollUtil.isEmpty(eids)) {
            return Result.success(newPageList);
        }
        request.setEids(eids);
        //建采商家
        QueryCanBuyEidRequest enterpriseRequest = new QueryCanBuyEidRequest();
        enterpriseRequest.setCustomerEid(buyerEid);
        enterpriseRequest.setLine(EnterpriseCustomerLineEnum.B2B.getCode());
        List<Long> purchaseEids = customerApi.getEidListByCustomerEid(enterpriseRequest);
        request.setPurchaseEids(purchaseEids);

        //价格排序
        if(null!=from.getSortPriceDesc() || null !=from.getSortPriceAsc()){
            if(from.getSortPriceDesc()){
                request.setSortPrice(1);
            }else if(from.getSortPriceAsc()){
                request.setSortPrice(2);
            }
        }
        //优质商家
        List<Long> highQualityEids = enterpriseHighQualitySupplierApi.getAllSupplier();
        request.setHighQualityEids(highQualityEids);
        //有库存商品
        List<Long> inStockGoodsIds = b2bGoodsApi.getInStockGoodsBySpecId(request.getSellSpecificationsId());
        request.setInStockGoodsIds(inStockGoodsIds);
        DateTime yesterday = DateUtil.yesterday();
        //查询前30天销量
        request.setSaleTimStart(DateUtil.beginOfDay(DateUtil.offsetDay(yesterday,-29)));
        request.setSaleTimEnd(DateUtil.endOfDay(yesterday));
        Page<ChoicenessGoodsBO> page = b2bGoodsApi.queryDistributorGoodsBySpec(request);
        if (CollUtil.isNotEmpty(page.getRecords())) {
            List<Long> goodsIds = page.getRecords().stream().map(e -> e.getGoodsId()).collect(Collectors.toList());
            List<ChoicenessGoodsVO> goodsItemVOList = goodsAssemblyUtils.choicenessGoodsAssembly(goodsIds, buyerEid,request.getSellSpecificationsId());
            //是否有库存标签
            goodsItemVOList.forEach(e->{
                if(inStockGoodsIds.contains(e.getGoodsInfo().getId())){
                    e.setHasStock(true);
                }
            });
            //翻页
            newPageList.setRecords(goodsItemVOList);
            newPageList.setSize(page.getSize());
            newPageList.setCurrent(page.getCurrent());
            newPageList.setTotal(page.getTotal());
        }
        return Result.success(newPageList);
    }

}
