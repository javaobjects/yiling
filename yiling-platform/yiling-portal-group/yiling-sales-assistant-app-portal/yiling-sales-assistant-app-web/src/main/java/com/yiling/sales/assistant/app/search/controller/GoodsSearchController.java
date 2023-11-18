package com.yiling.sales.assistant.app.search.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.annotations.UserAccessAuthentication;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.goods.inventory.api.InventoryApi;
import com.yiling.goods.inventory.dto.InventoryDTO;
import com.yiling.goods.medicine.api.B2bGoodsApi;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.api.PopGoodsApi;
import com.yiling.goods.medicine.bo.GoodsListItemBO;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.goods.medicine.dto.GoodsSkuDTO;
import com.yiling.goods.medicine.dto.PopGoodsDTO;
import com.yiling.goods.medicine.dto.StandardGoodsBasicDTO;
import com.yiling.goods.medicine.dto.request.QueryGoodsPageListRequest;
import com.yiling.goods.medicine.enums.GoodsAuditStatusEnum;
import com.yiling.goods.medicine.enums.GoodsLineEnum;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.mall.agreement.api.AgreementBusinessApi;
import com.yiling.mall.customer.api.CustomerSearchApi;
import com.yiling.mall.customer.dto.request.CustomerVerificationRequest;
import com.yiling.marketing.common.enums.CouponPlatformTypeEnum;
import com.yiling.marketing.couponactivity.api.CouponActivityApi;
import com.yiling.marketing.couponactivity.dto.CouponActivityListFiveByGoodsIdDTO;
import com.yiling.marketing.promotion.api.PromotionActivityApi;
import com.yiling.marketing.promotion.dto.PromotionGoodsLimitDTO;
import com.yiling.marketing.promotion.dto.request.ActivityGoodsPageRequest;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.pricing.goods.api.GoodsPriceApi;
import com.yiling.pricing.goods.dto.GoodsPriceDTO;
import com.yiling.pricing.goods.dto.request.QueryGoodsPriceRequest;
import com.yiling.sales.assistant.app.cart.util.SimpleGoodInfoUtils;
import com.yiling.sales.assistant.app.order.vo.GoodsPriceVO;
import com.yiling.sales.assistant.app.search.form.ActivityGoodsPageForm;
import com.yiling.sales.assistant.app.search.form.QueryAgreementInfoForm;
import com.yiling.sales.assistant.app.search.form.QueryEsGoodsSearchForm;
import com.yiling.sales.assistant.app.search.vo.AgreementQueryVO;
import com.yiling.sales.assistant.app.search.vo.AppStandardGoodsVO;
import com.yiling.sales.assistant.app.search.vo.GoodsSkuVO;
import com.yiling.sales.assistant.app.search.vo.QtyVO;
import com.yiling.sales.assistant.app.search.vo.SaleGoodsInfoVO;
import com.yiling.sales.assistant.app.search.vo.SimpleActivityVO;
import com.yiling.sales.assistant.app.search.vo.StandardGoodsPicVO;
import com.yiling.sales.assistant.app.util.PictureUrlUtils;
import com.yiling.sales.assistant.task.api.TaskApi;
import com.yiling.sales.assistant.task.dto.request.QueryTaskGoodsMatchRequest;
import com.yiling.sales.assistant.task.dto.request.TaskGoodsMatchListDTO;
import com.yiling.user.agreement.api.AgreementApi;
import com.yiling.user.agreement.dto.SupplementAgreementDetailDTO;
import com.yiling.user.agreement.util.AgreementUtils;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.procrelation.api.ProcurementRelationGoodsApi;
import com.yiling.user.procrelation.dto.DistributorGoodsBO;
import com.yiling.user.procrelation.dto.request.QueryGoodsByEachOtherRequest;
import com.yiling.user.shop.api.ShopApi;
import com.yiling.user.shop.dto.ShopDTO;
import com.yiling.user.system.bo.CurrentStaffInfo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 销售助手选品
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.sales.assistant.app.controller
 * @date: 2021/9/9
 */
@RestController
@Api(tags = "选品相关")
@RequestMapping("/search/")
@Slf4j
public class GoodsSearchController extends BaseController {
    @DubboReference
    GoodsApi             goodsApi;
    @DubboReference
    GoodsPriceApi        goodsPriceApi;
    @DubboReference
    InventoryApi         inventoryApi;
    @DubboReference
    AgreementApi         agreementApi;
    @DubboReference
    EnterpriseApi        enterpriseApi;
    @DubboReference
    B2bGoodsApi          b2bGoodsApi;
    @DubboReference
    ShopApi              shopApi;
    @DubboReference
    PopGoodsApi          popGoodsApi;
    @DubboReference
    CouponActivityApi    couponActivityApi;
    @DubboReference
    PromotionActivityApi promotionActivityApi;
    @DubboReference
    AgreementBusinessApi agreementBusinessApi;
    @DubboReference
    CustomerSearchApi    customerSearchApi;
    @DubboReference
    TaskApi              taskApi;
    @DubboReference
    ProcurementRelationGoodsApi procurementRelationGoodsApi;
    @Autowired
    PictureUrlUtils      pictureUrlUtils;



    /**
     *  查询商品价格，以及库存信息
     * @param saleGoodsInfoVOList 建采商品信息
     * @param customerEid 客户企业ID
     */
    private void assemblyToPop(List<SaleGoodsInfoVO> saleGoodsInfoVOList, Long customerEid){

        Map<Long,Long> distributorGoodsMap = saleGoodsInfoVOList.stream().collect(Collectors.toMap(SaleGoodsInfoVO::getGoodsId,SaleGoodsInfoVO::getDistributeGoodId));

        if (MapUtils.isEmpty(distributorGoodsMap)) {

            return ;
        }

        // 配送商，商品ID
        List<Long> distributorGoodIds = distributorGoodsMap.values().stream().collect(Collectors.toList());
        // 查询标准库商品信息
        List<StandardGoodsBasicDTO> standardGoodsDTOList = goodsApi.batchQueryStandardGoodsBasic(distributorGoodIds);
        Map<Long, StandardGoodsBasicDTO> goodsInfoDTOMap = standardGoodsDTOList.stream().collect(Collectors.toMap(StandardGoodsBasicDTO::getId, Function.identity()));
        // 查询价格
        QueryGoodsPriceRequest queryGoodsPriceRequest = new QueryGoodsPriceRequest();
        queryGoodsPriceRequest.setCustomerEid(customerEid);
        queryGoodsPriceRequest.setGoodsIds(distributorGoodIds);
        Map<Long, BigDecimal> priceMap = goodsPriceApi.queryGoodsPriceMap(queryGoodsPriceRequest);
        //获取按钮的状态
        Map<Long, Integer> goodsLimitMap = agreementBusinessApi.getGoodsLimitByGids(distributorGoodIds, customerEid);

        saleGoodsInfoVOList.forEach(infoVO -> {
            Long distributorGoodId = distributorGoodsMap.get(infoVO.getGoodsId());
            StandardGoodsBasicDTO basicDTO = Optional.ofNullable(distributorGoodId).map(inventory -> goodsInfoDTOMap.get(distributorGoodId)).orElse(goodsInfoDTOMap.get(infoVO.getGoodsId()));
            GoodsPriceVO goodsPriceVO = new GoodsPriceVO();
            goodsPriceVO.setBuyPrice(priceMap.getOrDefault(distributorGoodId,BigDecimal.ZERO));
            goodsPriceVO.setLinePrice(priceMap.getOrDefault(distributorGoodId,BigDecimal.ZERO));
            infoVO.setPriceInfo(goodsPriceVO);
            infoVO.setGoodsInfo(SimpleGoodInfoUtils.toSimpleGoodsVO(basicDTO));
            infoVO.setIsAgreementGood(true);
            infoVO.setGiftActivity(ListUtil.empty());
            infoVO.setSimpleActivityList(ListUtil.empty());
            infoVO.setStartAmount(BigDecimal.ZERO);
            infoVO.setGoodsLimitStatus(goodsLimitMap.get(distributorGoodId));
            infoVO.setAgreementIds(ListUtil.empty());
            //销售包装信息
            List<GoodsSkuDTO> goodsSkuDTOList = goodsApi.getGoodsSkuByGoodsId(distributorGoodId);
            goodsSkuDTOList = goodsSkuDTOList.stream().filter(e -> e.getGoodsLine().equals(GoodsLineEnum.POP.getCode())).collect(Collectors.toList());
            infoVO.setCountSku(goodsSkuDTOList.size());
            infoVO.setGoodsSkuList(PojoUtils.map(goodsSkuDTOList, GoodsSkuVO.class));
            AppStandardGoodsVO appStandardGoodsVO = new AppStandardGoodsVO();
            appStandardGoodsVO.setGdfName(basicDTO.getStandardGoods().getGdfName());
            appStandardGoodsVO.setIsCn(basicDTO.getStandardGoods().getIsCn());
            appStandardGoodsVO.setManufacturerAddress(basicDTO.getStandardGoods().getManufacturerAddress());
            appStandardGoodsVO.setOtcType(basicDTO.getStandardGoods().getOtcType());
            infoVO.setAppStandardGoodsVO(appStandardGoodsVO);
            setPic(infoVO,basicDTO);
        });
    }

    /**
     * 可采商品列表
     *
     * @param form
     * @return
     */
    @ApiOperation(value = "可采商品列表")
    @RequestMapping(path = "/agreement/goods",method={org.springframework.web.bind.annotation.RequestMethod.POST})
    @UserAccessAuthentication
    public Result<Page<SaleGoodsInfoVO>> purchaseGood(@RequestBody @Valid QueryEsGoodsSearchForm form, @CurrentUser CurrentStaffInfo staffInfo) {
        // 非以岭的无此操作权限
        if (!staffInfo.getYilingFlag()) {

            return Result.failed("非以岭用户无此操作!");
        }
        CustomerVerificationRequest verificationRequest = new CustomerVerificationRequest();
        verificationRequest.setCurrentEid(staffInfo.getCurrentEid());
        verificationRequest.setContactUserId(staffInfo.getCurrentUserId());
        verificationRequest.setCustomerEid(form.getEid());
        verificationRequest.setOrderType(staffInfo.getYilingFlag() ? OrderTypeEnum.POP.getCode() : OrderTypeEnum.B2B.getCode());
        Boolean checkResult = customerSearchApi.checkIsMyCustomer(verificationRequest);

        if (!checkResult) {

            return Result.failed("请选择自己名下的客户!");
        }

        QueryGoodsByEachOtherRequest queryGoodsByEachOtherRequest = new QueryGoodsByEachOtherRequest();
        queryGoodsByEachOtherRequest.setBuyerEid(form.getEid());
        queryGoodsByEachOtherRequest.setSellerEid(form.getDistributorEid());
        // queryGoodsByEachOtherRequest.setGoodsName(form.getKeyWord());

        List<DistributorGoodsBO> distributorGoodsBOS = procurementRelationGoodsApi.queryGoodsListByEachOther(queryGoodsByEachOtherRequest);

        if (CollectionUtil.isEmpty(distributorGoodsBOS)) {

            return Result.success(new Page<>(form.getCurrent(),form.getSize()));
        }

        Map<Long, List<DistributorGoodsBO>> sellSpecificationsIdMap = distributorGoodsBOS.stream().collect(Collectors.groupingBy(DistributorGoodsBO::getSellSpecificationsId));
        List<Long> includeSellSpecificationsIds = distributorGoodsBOS.stream().map(t -> t.getSellSpecificationsId()).distinct().collect(Collectors.toList());
        QueryGoodsPageListRequest request = new QueryGoodsPageListRequest();
        request.setName(form.getKeyWord());
        request.setIncludeSellSpecificationsIds(includeSellSpecificationsIds);
        request.setEidList(ListUtil.toList(form.getDistributorEid()));
        request.setGoodsStatus(GoodsStatusEnum.UP_SHELF.getCode());

        Page<PopGoodsDTO> popGoodsDTOPage = popGoodsApi.queryPopGoodsPage(request);

        if (popGoodsDTOPage.getTotal() == 0 || CollectionUtil.isEmpty(popGoodsDTOPage.getRecords())) {

            return Result.success(new Page<>(form.getCurrent(),form.getSize()));
        }

        Boolean isExistExceptionData = popGoodsDTOPage.getRecords().stream().anyMatch(t -> CollectionUtil.isEmpty(sellSpecificationsIdMap.get(t.getSellSpecificationsId())));

        if (isExistExceptionData) {

            log.error("通过规格获取商品异常");

            return Result.success(new Page<>(form.getCurrent(),form.getSize()));
        }


        List<SaleGoodsInfoVO> saleGoodsInfoVOList = popGoodsDTOPage.getRecords().stream().map(t -> {
            List<DistributorGoodsBO> goodsBOList = sellSpecificationsIdMap.get(t.getSellSpecificationsId());

            // 同一个配送商，一个规格,只有一个品
            DistributorGoodsBO goodsBO = goodsBOList.stream().findFirst().get();
            SaleGoodsInfoVO saleGoodsInfoVO = new SaleGoodsInfoVO();
            saleGoodsInfoVO.setDistributionEid(form.getDistributorEid());
            saleGoodsInfoVO.setDistributeGoodId(t.getGoodsId());
            saleGoodsInfoVO.setGoodsId(goodsBO.getGoodsId());
            saleGoodsInfoVO.setPurchaseEid(form.getEid());
            saleGoodsInfoVO.setRebate(goodsBO.getRebate());

            return saleGoodsInfoVO;

        }).collect(Collectors.toList());

        this.assemblyToPop(saleGoodsInfoVOList,form.getEid());

        Page<SaleGoodsInfoVO> listPage = PojoUtils.map(popGoodsDTOPage,SaleGoodsInfoVO.class);

        listPage.setRecords(saleGoodsInfoVOList);

        return Result.success(listPage);
    }


    /**
     * 通过售卖规格ID获取以岭商品ID
     *
     * @param sellSpecificationsIds
     * @return
     */
    private Map<Long, Long> getYlGoodsIdBySellSpecificationsIds(List<Long> sellSpecificationsIds) {
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
            if (CollectionUtil.isEmpty(goodsList)) {
                returnMap.put(sellSpecificationsId, ylGoodsId);
                continue;
            }
            for (GoodsDTO goodsDTO : goodsList) {
                if (popGoodsDTOMap.get(goodsDTO.getId()) != null && popGoodsDTOMap.get(goodsDTO.getId()).getGoodsStatus().equals(GoodsStatusEnum.UP_SHELF.getCode())) {
                    ylGoodsId = goodsDTO.getId();
                    break;
                }
            }
            if (ylGoodsId == 0 && CollUtil.isNotEmpty(goodsList)) {
                ylGoodsId = goodsList.get(0).getId();
            }
            returnMap.put(sellSpecificationsId, ylGoodsId);
        }
        return returnMap;
    }


    /**
     * 获取B2b销售商品
     * @param goodsIds
     * @param buyerEid
     * @param distributorEid
     * @return
     */
    public List<SaleGoodsInfoVO> assemblyToB2b(List<Long> goodsIds, Long buyerEid,Long distributorEid) {
        ShopDTO shopDTO = shopApi.getShop(distributorEid);
        if (shopDTO == null) {
            throw new BusinessException(UserErrorCode.SHOP_NOT_EXISTS);
        }
        //获取标准库商品信息
        List<StandardGoodsBasicDTO> standardGoodsDTOList = goodsApi.batchQueryStandardGoodsBasic(goodsIds);
        Map<Long, StandardGoodsBasicDTO> goodsInfoDTOMap = standardGoodsDTOList.stream().collect(Collectors.toMap(StandardGoodsBasicDTO::getId, Function.identity()));
        //获取商品定价系统
        QueryGoodsPriceRequest queryGoodsPriceRequest = new QueryGoodsPriceRequest();
        queryGoodsPriceRequest.setCustomerEid(buyerEid);
        queryGoodsPriceRequest.setGoodsIds(goodsIds);
        Map<Long, GoodsPriceDTO> priceMap = goodsPriceApi.queryB2bGoodsPriceInfoMap(queryGoodsPriceRequest);
        //获取按钮的状态
        Map<Long, Integer> goodsLimitMap = agreementBusinessApi.getB2bGoodsLimitByGids(goodsIds, buyerEid);
        List<Long> sellSpecificationIds = standardGoodsDTOList.stream().map(e -> e.getSellSpecificationsId()).collect(Collectors.toList());
        Map<Long, Long> ylGoodsMap = getYlGoodsIdBySellSpecificationsIds(sellSpecificationIds);
        //满赠活动
      /*  PromotionAppRequest appRequest = new PromotionAppRequest();
        appRequest.setPlatform(PlatformEnum.SALES_ASSIST.getCode());
        appRequest.setBuyerEid(buyerEid);
        appRequest.setTypeList(ListUtil.toList(PromotionTypeEnum.FULL_GIFT.getType()));
        appRequest.setGoodsIdList(goodsIds);
        List<PromotionGoodsLimitDTO> promotionAppDTOList = promotionActivityApi.queryGoodsPromotionInfo(appRequest);
*/
        List<SaleGoodsInfoVO> resultList =  goodsIds.stream().map(e -> {
            StandardGoodsBasicDTO goodsInfoDTO = goodsInfoDTOMap.get(e);
            if (goodsInfoDTO == null) {
                return null;
            }
            SaleGoodsInfoVO saleGoodsInfoVO = new SaleGoodsInfoVO();
            if (ylGoodsMap.getOrDefault(goodsInfoDTO.getSellSpecificationsId(), 0L) == 0) {
                saleGoodsInfoVO.setGoodsId(e);
            } else {
                saleGoodsInfoVO.setGoodsId(ylGoodsMap.get(goodsInfoDTO.getSellSpecificationsId()));
            }
            saleGoodsInfoVO.setGoodsLimitStatus(goodsLimitMap.get(e));
            saleGoodsInfoVO.setGoodsInfo(SimpleGoodInfoUtils.toSimpleGoodsVO(goodsInfoDTO));
            saleGoodsInfoVO.setSellSpecificationsId(goodsInfoDTO.getSellSpecificationsId());
            saleGoodsInfoVO.setDistributeGoodId(goodsInfoDTO.getId());
            saleGoodsInfoVO.setDistributionEid(distributorEid);
            saleGoodsInfoVO.setPurchaseEid(buyerEid);
            saleGoodsInfoVO.setIsAgreementGood(false);
            saleGoodsInfoVO.setAgreementIds(ListUtil.empty());
            //销售包装信息
            List<GoodsSkuDTO> goodsSkuDTOList = goodsApi.getGoodsSkuByGoodsId(saleGoodsInfoVO.getDistributeGoodId());
            goodsSkuDTOList = goodsSkuDTOList.stream().filter(t -> t.getGoodsLine().equals(GoodsLineEnum.B2B.getCode())).collect(Collectors.toList());
            saleGoodsInfoVO.setCountSku(goodsSkuDTOList.size());
            saleGoodsInfoVO.setGoodsSkuList(PojoUtils.map(goodsSkuDTOList, GoodsSkuVO.class));
            AppStandardGoodsVO appStandardGoodsVO = new AppStandardGoodsVO();
            appStandardGoodsVO.setGdfName(goodsInfoDTO.getStandardGoods().getGdfName());
            appStandardGoodsVO.setIsCn(goodsInfoDTO.getStandardGoods().getIsCn());
            appStandardGoodsVO.setManufacturerAddress(goodsInfoDTO.getStandardGoods().getManufacturerAddress());
            appStandardGoodsVO.setOtcType(goodsInfoDTO.getStandardGoods().getOtcType());
            saleGoodsInfoVO.setAppStandardGoodsVO(appStandardGoodsVO);
            saleGoodsInfoVO.setStartAmount(shopDTO.getStartAmount());
           // setActivity(promotionAppDTOList,saleGoodsInfoVO,e,goodsInfoDTO);
            saleGoodsInfoVO.setGiftActivity(ListUtil.empty());
            saleGoodsInfoVO.setSimpleActivityList(ListUtil.empty());

            setPic(saleGoodsInfoVO,goodsInfoDTO);
            GoodsPriceVO goodsPriceVO = new GoodsPriceVO();
            goodsPriceVO.setBuyPrice(Optional.ofNullable(priceMap.get(e)).map(t -> t.getBuyPrice()).orElse(BigDecimal.ZERO));
            goodsPriceVO.setLinePrice(Optional.ofNullable(priceMap.get(e)).map(t -> t.getLinePrice()).orElse(BigDecimal.ZERO));
            saleGoodsInfoVO.setPriceInfo(goodsPriceVO);
            return saleGoodsInfoVO;
        }).filter(e -> !ObjectUtil.isEmpty(e)).collect(Collectors.toList());

        return resultList;
    }

    /**
     * 设置商品图片
     * @param saleGoodsInfoVO
     * @param goodsInfoDTO
     */
    private void setPic (SaleGoodsInfoVO saleGoodsInfoVO,StandardGoodsBasicDTO goodsInfoDTO) {
        //如果商品图片为空需要赋值默认图片
        if (CollUtil.isEmpty(saleGoodsInfoVO.getPicBasicsInfoList())) {
            StandardGoodsPicVO standardGoodsPicVO = new StandardGoodsPicVO();
            standardGoodsPicVO.setPic(goodsInfoDTO.getPic());
            standardGoodsPicVO.setPicDefault(1);
            standardGoodsPicVO.setPicOrder(0);
            List<StandardGoodsPicVO> picBasicsInfoList = new ArrayList<>();
            picBasicsInfoList.add(standardGoodsPicVO);
            picBasicsInfoList.forEach(z -> {
                z.setPicUrl(pictureUrlUtils.getGoodsPicUrl(z.getPic()));
            });
            saleGoodsInfoVO.setPicBasicsInfoList(picBasicsInfoList);
        } else {
            saleGoodsInfoVO.getPicBasicsInfoList().forEach(z -> {
                z.setPicUrl(pictureUrlUtils.getGoodsPicUrl(z.getPic()));
            });
        }

    }
    /**
     * 设置满减活动信息
     *  @param promotionAppDTOList 赠品信息
     * @param saleGoodsInfoVO
     * @param goodId
     * @param goodsInfoDTO
     */
    private void setActivity( List<PromotionGoodsLimitDTO> promotionAppDTOList,SaleGoodsInfoVO saleGoodsInfoVO, Long goodId,StandardGoodsBasicDTO goodsInfoDTO) {
        //满减卷活动
        List<CouponActivityListFiveByGoodsIdDTO> couponActivityListFiveByGoodsIdDTOList = couponActivityApi.getListFiveByGoodsIdAndEid(goodId, goodsInfoDTO.getEid(), 3, CouponPlatformTypeEnum.B2B.getCode());
        List<SimpleActivityVO> simpleActivityList = PojoUtils.map(couponActivityListFiveByGoodsIdDTOList, SimpleActivityVO.class);
        saleGoodsInfoVO.setSimpleActivityList(simpleActivityList);
        if (CollectionUtil.isEmpty(promotionAppDTOList)) {
            return;
        }
        List<PromotionGoodsLimitDTO> promotionAppDTOResultList = promotionAppDTOList.stream().filter(t -> t.getGoodsId().equals(goodId)).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(promotionAppDTOResultList)) {
            return;
        }
        List<SimpleActivityVO> giftActivityList = Lists.newArrayList();
        promotionAppDTOResultList.forEach(promotionGoodsLimitDTO -> {
            List<SimpleActivityVO> giftActivity = promotionGoodsLimitDTO.getGoodsGiftLimitList().stream().map(promotionAppDTO -> {
                SimpleActivityVO giftActivityVO = new SimpleActivityVO();
                giftActivityVO.setId(promotionAppDTO.getId());
                giftActivityVO.setName("满" + promotionAppDTO.getPromotionAmount() + "元," + promotionAppDTO.getGiftName());
                return giftActivityVO;
            }).collect(Collectors.toList());
            giftActivityList.addAll(giftActivity);
        });
        saleGoodsInfoVO.setGiftActivity(giftActivityList);
    }


    @ApiOperation(value = "B2B全品")
    @RequestMapping(path = "/b2b/goods",method={org.springframework.web.bind.annotation.RequestMethod.POST})
    @UserAccessAuthentication
    public Result<Page<SaleGoodsInfoVO>> b2bAllGoods(@RequestBody @Valid QueryEsGoodsSearchForm form, @CurrentUser CurrentStaffInfo staffInfo) {
        // b2b商品，只有非以岭人员有此操作权限
        if (staffInfo.getYilingFlag()) {
            return Result.failed("以岭人员无此操作权限!");
        }
        CustomerVerificationRequest verificationRequest = new CustomerVerificationRequest();
        verificationRequest.setCurrentEid(staffInfo.getCurrentEid());
        verificationRequest.setContactUserId(staffInfo.getCurrentUserId());
        verificationRequest.setCustomerEid(form.getEid());
        verificationRequest.setOrderType(staffInfo.getYilingFlag() ? OrderTypeEnum.POP.getCode() : OrderTypeEnum.B2B.getCode());
        Boolean checkResult = customerSearchApi.checkIsMyCustomer(verificationRequest);
        if (!checkResult) {
            return Result.failed("请选择自己名下的客户!");
        }
        // 企业ID
        Long buyerEid = staffInfo != null ? form.getEid() : 0L;
        QueryGoodsPageListRequest request = PojoUtils.map(form,QueryGoodsPageListRequest.class);
        request.setName(form.getKeyWord());
        request.setEidList(ListUtil.toList(form.getDistributorEid()));
        request.setGoodsStatus(GoodsStatusEnum.UP_SHELF.getCode());
        request.setAuditStatus(GoodsAuditStatusEnum.PASS_AUDIT.getCode());
        request.setGoodsLine(GoodsLineEnum.B2B.getCode());
        Page<GoodsListItemBO> goodsListItemBOPage = b2bGoodsApi.queryB2bGoodsPageList(request);

        if (CollectionUtil.isEmpty(goodsListItemBOPage.getRecords())) {

            return Result.success(new Page<>(form.getCurrent(),form.getSize()));
        }
        Page<SaleGoodsInfoVO> listPage = new Page<>(form.getCurrent(),form.getSize(),goodsListItemBOPage.getTotal());
        List<GoodsListItemBO> goodsAggDTOList = goodsListItemBOPage.getRecords();
        // 商品ID集合
        List<Long> goodsIds = goodsAggDTOList.stream().map(e -> e.getId()).distinct().collect(Collectors.toList());
        listPage.setRecords(this.assemblyToB2b(goodsIds, buyerEid,form.getDistributorEid()));

        return Result.success(listPage);

    }



    @ApiOperation(value = "B2B以岭品")
    @RequestMapping(path = "/b2b/yl/goods",method={org.springframework.web.bind.annotation.RequestMethod.POST})
    @UserAccessAuthentication
    public Result<Page<SaleGoodsInfoVO>> b2bYlGoods(@RequestBody @Valid QueryEsGoodsSearchForm form, @CurrentUser CurrentStaffInfo staffInfo) {
        // b2b商品，只有非以岭人员有此操作权限
        if (staffInfo.getYilingFlag()) {
            return Result.failed("以岭人员无此操作权限!");
        }
        CustomerVerificationRequest verificationRequest = new CustomerVerificationRequest();
        verificationRequest.setCurrentEid(staffInfo.getCurrentEid());
        verificationRequest.setContactUserId(staffInfo.getCurrentUserId());
        verificationRequest.setCustomerEid(form.getEid());
        verificationRequest.setOrderType(staffInfo.getYilingFlag() ? OrderTypeEnum.POP.getCode() : OrderTypeEnum.B2B.getCode());
        Boolean checkResult = customerSearchApi.checkIsMyCustomer(verificationRequest);
        if (!checkResult) {
            return Result.failed("请选择自己名下的客户!");
        }

        Long buyerEid = staffInfo != null ? form.getEid() : 0L;
        List<Long> eidList = enterpriseApi.listSubEids(Constants.YILING_EID);
        QueryGoodsPageListRequest request = PojoUtils.map(form,QueryGoodsPageListRequest.class);
        request.setName(form.getKeyWord());
        request.setIncludeEids(eidList);
        request.setEidList(ListUtil.toList(form.getDistributorEid()));
        request.setGoodsStatus(GoodsStatusEnum.UP_SHELF.getCode());
        request.setAuditStatus(GoodsAuditStatusEnum.PASS_AUDIT.getCode());
        request.setYilingGoodsFlag(1);
        request.setGoodsLine(GoodsLineEnum.B2B.getCode());
        Page<GoodsListItemBO> goodsListItemBOPage = b2bGoodsApi.queryB2bGoodsPageList(request);

        if (CollectionUtil.isEmpty(goodsListItemBOPage.getRecords())) {

            return Result.success(new Page<>(form.getCurrent(),form.getSize()));
        }

        Page<SaleGoodsInfoVO> listPage = new Page<>(form.getCurrent(),form.getSize(),goodsListItemBOPage.getTotal());
        List<GoodsListItemBO> goodsAggDTOList = goodsListItemBOPage.getRecords();
        // 商品ID集合
        List<Long> goodsIds = goodsAggDTOList.stream().map(e -> e.getId()).distinct().collect(Collectors.toList());
        listPage.setRecords(this.assemblyToB2b(goodsIds, buyerEid,form.getDistributorEid()));

        return Result.success(listPage);
    }


    @ApiOperation(value = "B2B参与任务品")
    @RequestMapping(path = "/b2b/task/goods",method={org.springframework.web.bind.annotation.RequestMethod.POST})
    @UserAccessAuthentication
    public Result<Page<SaleGoodsInfoVO>> b2bTaskGoods(@RequestBody @Valid QueryEsGoodsSearchForm form, @CurrentUser CurrentStaffInfo staffInfo) {
        // b2b商品，只有非以岭人员有此操作权限
        if (staffInfo.getYilingFlag()) {
            return Result.failed("以岭人员无此操作权限!");
        }
        CustomerVerificationRequest verificationRequest = new CustomerVerificationRequest();
        verificationRequest.setCurrentEid(staffInfo.getCurrentEid());
        verificationRequest.setContactUserId(staffInfo.getCurrentUserId());
        verificationRequest.setCustomerEid(form.getEid());
        verificationRequest.setOrderType(staffInfo.getYilingFlag() ? OrderTypeEnum.POP.getCode() : OrderTypeEnum.B2B.getCode());
        Boolean checkResult = customerSearchApi.checkIsMyCustomer(verificationRequest);
        if (!checkResult) {
            return Result.failed("请选择自己名下的客户!");
        }

        // 企业ID
        Long buyerEid = staffInfo != null ? form.getEid() : 0L;
        // 查询参与任务的品
        QueryTaskGoodsMatchRequest queryTaskGoodsMatchRequest = new QueryTaskGoodsMatchRequest();
        queryTaskGoodsMatchRequest.setUserId(staffInfo.getCurrentUserId());
        queryTaskGoodsMatchRequest.setGoodsName(form.getKeyWord());
        queryTaskGoodsMatchRequest.setEid(form.getDistributorEid());


        List<TaskGoodsMatchListDTO> taskGoodsMatchDTOS = taskApi.queryTaskGoodsList(Collections.singletonList(queryTaskGoodsMatchRequest));

        if (log.isDebugEnabled()) {

            log.debug("调用任务接口:queryTaskGoodsList..入参:{},返回参数:{}",queryTaskGoodsMatchRequest,taskGoodsMatchDTOS);
        }

        if (CollectionUtil.isEmpty(taskGoodsMatchDTOS)) {

            return Result.success(new Page<>(form.getCurrent(),form.getSize()));
        }

        TaskGoodsMatchListDTO taskGoodsMatchListDTO = taskGoodsMatchDTOS.stream().findFirst().get();

        QueryGoodsPageListRequest queryGoodsPageListRequest = new QueryGoodsPageListRequest();
        queryGoodsPageListRequest.setIncludeSellSpecificationsIds(taskGoodsMatchListDTO.getGoodsMatchList().stream().map(t -> t.getSellSpecificationsId()).collect(Collectors.toList()));
        queryGoodsPageListRequest.setEidList(ListUtil.toList(form.getDistributorEid()));
        queryGoodsPageListRequest.setBuyerEid(buyerEid);
        queryGoodsPageListRequest.setName(form.getKeyWord());

        Page<GoodsListItemBO> goodsListItemBOPage = b2bGoodsApi.queryB2bGoodsPageList(queryGoodsPageListRequest);


        if (log.isDebugEnabled()) {

            log.debug("调用商品接口:queryB2bGoodsPageList..入参:{},返回参数:{}",queryGoodsPageListRequest,goodsListItemBOPage);
        }

        if (goodsListItemBOPage == null || CollectionUtil.isEmpty(goodsListItemBOPage.getRecords())) {

            return Result.success(new Page<>(form.getCurrent(),form.getSize()));
        }

        Page<SaleGoodsInfoVO> listPage = new Page<>(form.getCurrent(),form.getSize(),goodsListItemBOPage.getTotal());

        // 商品ID集合
        List<Long> goodsIds = goodsListItemBOPage.getRecords().stream().map(e -> e.getId()).distinct().collect(Collectors.toList());
        listPage.setRecords(this.assemblyToB2b(goodsIds, buyerEid,form.getDistributorEid()));

        return Result.success(listPage);
    }

    /**
     * 促销采购商品
     * @param form
     * @return
     */
    @ApiOperation(value = "促销采购商品--已作废")
    @RequestMapping(path = "/activity/goods",method={org.springframework.web.bind.annotation.RequestMethod.POST})
    @UserAccessAuthentication
    @Deprecated
    public Result<Page<SaleGoodsInfoVO>> activityPurchaseGood(@RequestBody @Valid QueryEsGoodsSearchForm form, @CurrentUser CurrentStaffInfo staffInfo) {
        // 非以岭的无此操作权限
        if (!staffInfo.getYilingFlag()) {
            return Result.failed("非以岭用户无此操作");
        }
        CustomerVerificationRequest verificationRequest = new CustomerVerificationRequest();
        verificationRequest.setCurrentEid(staffInfo.getCurrentEid());
        verificationRequest.setContactUserId(staffInfo.getCurrentUserId());
        verificationRequest.setCustomerEid(form.getEid());
        verificationRequest.setOrderType(staffInfo.getYilingFlag() ? OrderTypeEnum.POP.getCode() : OrderTypeEnum.B2B.getCode());
        Boolean checkResult = customerSearchApi.checkIsMyCustomer(verificationRequest);
        if (!checkResult) {
            return Result.failed("请选择自己名下的客户!");
        }

        return Result.success(new Page<>(form.getCurrent(),form.getSize()));
    }


    @ApiOperation(value = "查看协议内容")
    @PostMapping("/find/agreement")
    @UserAccessAuthentication
    public Result<CollectionObject<AgreementQueryVO>> selectAgreementsByIds(@RequestBody @Valid QueryAgreementInfoForm form, @CurrentUser CurrentStaffInfo staffInfo) {
        List<SupplementAgreementDetailDTO> agreementLists = agreementApi.querySupplementAgreementsDetailList(form.getAgreementIds());
        if (CollUtil.isEmpty(agreementLists)) {
            return Result.failed("协议不存在");
        }

        // 协议描述转换
        List<AgreementQueryVO> agreementQueryVOList = agreementLists.stream().map(e -> {
            AgreementQueryVO vo = PojoUtils.map(e,AgreementQueryVO.class);
            vo.setSimpleContent(AgreementUtils.getAgreementText(e));
            return vo;

        }).collect(Collectors.toList());

        return Result.success(new CollectionObject(agreementQueryVOList));
    }

    @ApiOperation(value = "查询此满赠活动的商品信息")
    @PostMapping("/queryGoodsByActivityId")
    public Result<Page<SaleGoodsInfoVO>> queryById(@CurrentUser CurrentStaffInfo staffInfo, @Valid @RequestBody ActivityGoodsPageForm form) {

        ShopDTO shopDTO = shopApi.getShop(form.getDistributorEid());
        if (shopDTO == null) {
            throw new BusinessException(UserErrorCode.SHOP_NOT_EXISTS);
        }
        Page<SaleGoodsInfoVO> page = new Page<>();
        ActivityGoodsPageRequest request = PojoUtils.map(form, ActivityGoodsPageRequest.class);
        request.setShopEid(form.getDistributorEid());
        request.setGoodsName(form.getKeyWord());
        // 促销活动商品表
        Page<PromotionGoodsLimitDTO> dtoPage = promotionActivityApi.pageGoodsByActivityId(request);
        List<PromotionGoodsLimitDTO> dtoList = dtoPage.getRecords();
        if (CollUtil.isEmpty(dtoList)) {
            return Result.success(page);
        }
        List<Long> goodsIdList = dtoList.stream().map(PromotionGoodsLimitDTO::getGoodsId).collect(Collectors.toList());
        page.setRecords(this.assemblyToB2b(goodsIdList, form.getPurchaseEid(),form.getDistributorEid()));
        page.setCurrent(dtoPage.getCurrent());
        page.setSize(dtoPage.getSize());
        page.setTotal(dtoPage.getTotal());
        return Result.success(page);
    }


    @ApiOperation(value = "sku库存加减校验")
    @GetMapping("/verifyInventory")
    @UserAccessAuthentication
    public Result<QtyVO> verifyInventory(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam Long skuId, @RequestParam Long stockNum) {
        InventoryDTO inventoryDTO = inventoryApi.getBySkuId(skuId);
        QtyVO qtyVO = new QtyVO();
        if (inventoryDTO == null) {
            qtyVO.setUsableBool(false);
            qtyVO.setUsableInventory(0L);
            return Result.success(qtyVO);
        }
        Long usableInventory = inventoryDTO.getQty() - inventoryDTO.getFrozenQty();
        if (usableInventory >= stockNum) {
            qtyVO.setUsableBool(true);
            qtyVO.setUsableInventory(stockNum);
            return Result.success(qtyVO);
        }
        qtyVO.setUsableBool(false);
        qtyVO.setUsableInventory(usableInventory);

        return Result.success(qtyVO);
    }


}
