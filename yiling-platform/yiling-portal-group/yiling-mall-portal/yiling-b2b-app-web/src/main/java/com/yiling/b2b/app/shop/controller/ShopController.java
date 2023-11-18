package com.yiling.b2b.app.shop.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.b2b.app.coupon.vo.GetCouponActivityResultVO;
import com.yiling.b2b.app.shop.form.GetCouponsForm;
import com.yiling.b2b.app.shop.form.QueryFloorGoodsPageForm;
import com.yiling.b2b.app.shop.form.QueryShopCouponsPageForm;
import com.yiling.b2b.app.shop.form.QueryShopForm;
import com.yiling.b2b.app.shop.vo.CouponActivityHasGetVO;
import com.yiling.b2b.app.shop.vo.CouponActivityVO;
import com.yiling.b2b.app.shop.vo.EnterpriseCertificateVO;
import com.yiling.b2b.app.shop.vo.ShopCertificateVO;
import com.yiling.b2b.app.shop.vo.ShopDetailVO;
import com.yiling.b2b.app.shop.vo.ShopFloorBasicVO;
import com.yiling.b2b.app.shop.vo.ShopGreatGoodsVO;
import com.yiling.b2b.app.shop.vo.ShopListItemGoodsVO;
import com.yiling.b2b.app.shop.vo.ShopListItemVO;
import com.yiling.common.web.goods.utils.PictureUrlUtils;
import com.yiling.common.web.goods.vo.GoodsPriceVO;
import com.yiling.common.web.goods.vo.PresaleInfoVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.goods.medicine.api.B2bGoodsApi;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.goods.medicine.dto.StandardGoodsBasicDTO;
import com.yiling.marketing.couponactivity.dto.GetCouponActivityResultDTO;
import com.yiling.marketing.presale.api.PresaleActivityApi;
import com.yiling.marketing.presale.dto.PresaleActivityGoodsDTO;
import com.yiling.marketing.presale.dto.request.QueryPresaleInfoRequest;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.request.QueryBackOrderInfoRequest;
import com.yiling.order.order.enums.OrderStatusEnum;
import com.yiling.pricing.goods.api.GoodsPriceApi;
import com.yiling.pricing.goods.dto.GoodsPriceDTO;
import com.yiling.pricing.goods.dto.request.QueryGoodsPriceRequest;
import com.yiling.marketing.common.enums.CouponPlatformTypeEnum;
import com.yiling.marketing.couponactivity.api.CouponActivityApi;
import com.yiling.marketing.couponactivity.dto.CouponActivityEnterpDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityHasGetDTO;
import com.yiling.marketing.couponactivity.dto.request.CouponActivityReceiveRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityCanReceivePageRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityCanReceiveRequest;
import com.yiling.marketing.couponactivity.enums.PayMethodLimitEnum;
import com.yiling.marketing.couponactivity.enums.PayMethodTypeEnum;
import com.yiling.marketing.promotion.api.PromotionActivityApi;
import com.yiling.marketing.promotion.dto.PromotionGoodsLimitDTO;
import com.yiling.marketing.promotion.dto.request.PromotionAppRequest;
import com.yiling.marketing.promotion.enums.PromotionTypeEnum;
import com.yiling.order.order.enums.PromotionActivityTypeEnum;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.common.enums.EnterpriseCustomerLineEnum;
import com.yiling.user.enterprise.api.CertificateApi;
import com.yiling.user.enterprise.api.CustomerApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.api.EnterpriseCustomerLineApi;
import com.yiling.user.enterprise.api.EnterpriseHighQualitySupplierApi;
import com.yiling.user.enterprise.api.EnterprisePurchaseApplyApi;
import com.yiling.user.enterprise.bo.EnterprisePurchaseApplyBO;
import com.yiling.user.enterprise.dto.EnterpriseCertificateDTO;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryCanBuyEidRequest;
import com.yiling.user.enterprise.enums.EnterpriseCertificateTypeEnum;
import com.yiling.user.enterprise.enums.EnterprisePurchaseApplyStatusEnum;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;
import com.yiling.user.member.api.MemberApi;
import com.yiling.user.member.dto.CurrentMemberDTO;
import com.yiling.user.member.dto.request.CurrentMemberForMarketingDTO;
import com.yiling.user.shop.api.ShopApi;
import com.yiling.user.shop.api.ShopFloorApi;
import com.yiling.user.shop.dto.ShopDTO;
import com.yiling.user.shop.dto.ShopFloorDTO;
import com.yiling.user.shop.dto.ShopFloorGoodsDTO;
import com.yiling.user.shop.dto.ShopListItemDTO;
import com.yiling.user.shop.dto.request.QueryFloorGoodsPageRequest;
import com.yiling.user.shop.dto.request.QueryShopRequest;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import io.lettuce.core.ScriptOutputType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 店铺 Controller
 *
 * @author: lun.yu
 * @date: 2021/10/19
 */
@RestController
@RequestMapping("/shop")
@Api(tags = "店铺接口")
@Slf4j
public class ShopController extends BaseController {

    @DubboReference
    ShopApi shopApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    CertificateApi certificateApi;
    @DubboReference
    B2bGoodsApi b2bGoodsApi;
    @DubboReference
    CouponActivityApi couponActivityApi;
    @DubboReference
    UserApi userApi;
    @DubboReference
    GoodsPriceApi goodsPriceApi;
    @DubboReference
    MemberApi memberApi;
    @DubboReference
    GoodsApi goodsApi;
    @DubboReference
    EnterprisePurchaseApplyApi enterprisePurchaseApplyApi;
    @DubboReference
    EnterpriseCustomerLineApi enterpriseCustomerLineApi;
    @DubboReference
    PromotionActivityApi promotionActivityApi;
    @DubboReference
    PresaleActivityApi presaleActivityApi;
    @DubboReference
    OrderApi orderApi;
    @DubboReference
    ShopFloorApi shopFloorApi;
    @DubboReference
    EnterpriseHighQualitySupplierApi enterpriseHighQualitySupplierApi;
    @DubboReference
    CustomerApi customerApi;
    @Autowired
    PictureUrlUtils pictureUrlUtils;
    @Autowired
    FileService fileService;

    @ApiOperation(value = "店铺分页列表")
    @PostMapping("/queryShopListPage")
    public Result<Page<ShopListItemVO>> queryShopListPage(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryShopForm form) {
        QueryShopRequest request = PojoUtils.map(form, QueryShopRequest.class);

        //新增需求：只展示当前登录人可以购买的店铺（根据店铺销售区域进行判断）
        //根据当前登录企业的区域信息，获取可以买哪些企业的接口
        List<Long> couldBuyEidList = shopApi.getSellEidByEidSaleArea(staffInfo.getCurrentEid());
        if (CollUtil.isEmpty(couldBuyEidList)) {
            return Result.success(new Page<>());
        }
        //查询企业商品信息，并且过滤没有商品的企业店铺
        List<Long> filterBuyEidList = b2bGoodsApi.getEidListBySaleGoods(couldBuyEidList);
        request.setShopEidList(filterBuyEidList);
        if (CollUtil.isEmpty(request.getShopEidList())) {
            return Result.success(new Page<>());
        }
        request.setCustomerEid(staffInfo.getCurrentEid());
        // 近30天销量
        DateTime yesterday = DateUtil.yesterday();
        request.setStartRecentTime(DateUtil.beginOfDay(DateUtil.offsetDay(yesterday,-29)));
        request.setEndRecentTime(DateUtil.endOfDay(yesterday));
        // 当前登录终端的省区
        request.setProvinceCode(enterpriseApi.getById(staffInfo.getCurrentEid()).getProvinceCode());
        // 优质商家
        List<Long> highQualityEidList = enterpriseHighQualitySupplierApi.getAllSupplier();
        request.setHighQualityEidList(highQualityEidList);
        // 建采且可购买商家
        QueryCanBuyEidRequest enterpriseRequest = new QueryCanBuyEidRequest();
        enterpriseRequest.setCustomerEid(staffInfo.getCurrentEid());
        enterpriseRequest.setLine(EnterpriseCustomerLineEnum.B2B.getCode());
        List<Long> purchaseEidList = customerApi.getEidListByCustomerEid(enterpriseRequest);
        List<Long> containList = purchaseEidList.stream().filter(filterBuyEidList::contains).collect(Collectors.toList());
        request.setPurchaseEidList(containList);

        Page<ShopListItemDTO> dtoPage = shopApi.queryPurchaseShopListPage(request);

        List<Long> eidList = dtoPage.getRecords().stream().map(ShopListItemDTO::getShopEid).collect(Collectors.toList());
        log.info("b2b-queryShopListPage接口调用，eidList:{}", eidList);
        Map<Long, List<GoodsDTO>> eidGoodsMap = b2bGoodsApi.getShopGoodsByEidAndLimit(eidList, 4);
        Page<ShopListItemVO> page = PojoUtils.map(dtoPage, ShopListItemVO.class);
        //根据企业ID获取店铺商品种类数量；根据企业ID获取商品列表，此处固定取4条即可
        if (CollUtil.isNotEmpty(eidList)) {
            List<Long> goodsIds = new ArrayList<>();
            for (List<GoodsDTO> goodsDTOList : eidGoodsMap.values()) {
                goodsIds.addAll(goodsDTOList.stream().map(GoodsDTO::getId).collect(Collectors.toList()));
            }

            TimeInterval timer = DateUtil.timer();
            QueryGoodsPriceRequest queryGoodsPriceRequestTop = new QueryGoodsPriceRequest();
            queryGoodsPriceRequestTop.setCustomerEid(staffInfo.getCurrentEid());
            queryGoodsPriceRequestTop.setGoodsIds(goodsIds);
            Map<Long, GoodsPriceDTO> priceMapTop = goodsPriceApi.queryB2bGoodsPriceInfoMap(queryGoodsPriceRequestTop);
            log.info("b2b-queryShopListPage接口调用，批量获取B2B商品定价耗时={}", timer.intervalRestart());
            List<StandardGoodsBasicDTO> standardGoodsBasicDTOList = goodsApi.batchQueryStandardGoodsBasic(goodsIds);
            Map<Long, StandardGoodsBasicDTO> standardGoodsBasicDtoMap = standardGoodsBasicDTOList.stream().collect(Collectors.toMap(StandardGoodsBasicDTO::getId, Function.identity()));
            log.info("b2b-queryShopListPage接口调用，根据goodsIds查询商品基础信息耗时={}", timer.intervalRestart());

            List<Long> goodsIdList = Lists.newArrayList();
            page.getRecords().forEach(shopListItemVO -> {
                List<GoodsDTO> goodsDtoList = eidGoodsMap.get(shopListItemVO.getShopEid());
                goodsIdList.addAll(goodsDtoList.stream().map(GoodsDTO::getId).collect(Collectors.toList()));
            });
            Map<Long, PromotionGoodsLimitDTO> promotionMap = getGoodsType(goodsIdList, staffInfo.getCurrentEid());

            page.getRecords().forEach(shopListItemVO -> {
                List<GoodsDTO> goodsDtoList = eidGoodsMap.get(shopListItemVO.getShopEid());

                if (CollUtil.isNotEmpty(goodsDtoList)) {
                    List<ShopListItemGoodsVO> goodsVOList = goodsDtoList.stream().map(goodsDto -> {
                        ShopListItemGoodsVO goodsVO = new ShopListItemGoodsVO();
                        goodsVO.setGoodsId(goodsDto.getId());
                        goodsVO.setGoodsName(standardGoodsBasicDtoMap.get(goodsDto.getId()).getStandardGoods().getName());
                        GoodsPriceDTO goodsPriceDTO = priceMapTop.get(goodsDto.getId());
                        goodsVO.setPrice(goodsPriceDTO.getBuyPrice());
                        goodsVO.setPic(pictureUrlUtils.getGoodsPicUrl(goodsDto.getPic()));
                        GoodsPriceVO goodsPriceVO = new GoodsPriceVO();
                        goodsPriceVO.setBuyPrice(goodsPriceDTO.getBuyPrice());
                        goodsPriceVO.setLinePrice(goodsPriceDTO.getLinePrice());
                        goodsPriceVO.setIsShow(goodsPriceDTO.getIsShow());
                        goodsVO.setGoodsPriceVO(goodsPriceVO);

                        // 秒杀/特价商品
                        PromotionGoodsLimitDTO promotionGoodsLimitDTO = promotionMap.get(goodsDto.getId());
                        goodsVO.setHasSecKill(Objects.nonNull(promotionGoodsLimitDTO) && PromotionTypeEnum.isSecKill(promotionGoodsLimitDTO.getType()));
                        goodsVO.setHasSpecial(Objects.nonNull(promotionGoodsLimitDTO) && PromotionTypeEnum.isSpecialPrice(promotionGoodsLimitDTO.getType()));

                        return goodsVO;
                    }).collect(Collectors.toList());

                    shopListItemVO.setItemProductList(goodsVOList);
                }

                //获取商品种类
                Long goodsKind = b2bGoodsApi.countB2bGoodsByEids(ListUtil.toList(shopListItemVO.getShopEid()));
                shopListItemVO.setGoodsKind(goodsKind.intValue());

            });

        }
        return Result.success(page);
    }

    /**
     * 获取商品是否为特价/秒杀商品
     *
     * @param goodsIds
     * @param buyerEid
     * @return
     */
    private Map<Long, PromotionGoodsLimitDTO> getGoodsType(List<Long> goodsIds, Long buyerEid) {
        if (CollUtil.isEmpty(goodsIds)) {
            return MapUtil.newHashMap();
        }

        PromotionAppRequest promotionRequest = new PromotionAppRequest();
        promotionRequest.setGoodsIdList(goodsIds);
        promotionRequest.setPlatform(PlatformEnum.B2B.getCode());
        promotionRequest.setTypeList(ListUtil.toList(PromotionActivityTypeEnum.SPECIAL.getCode(), PromotionActivityTypeEnum.LIMIT.getCode()));
        promotionRequest.setBuyerEid(buyerEid);

        List<PromotionGoodsLimitDTO> promotionGoodsList = promotionActivityApi.queryGoodsPromotionInfo(promotionRequest);

        return promotionGoodsList.stream().collect(Collectors.toMap(PromotionGoodsLimitDTO::getGoodsId, Function.identity(), (k1, k2) -> k2));
    }

    @ApiOperation(value = "获取店铺详情")
    @GetMapping("/getShopDetail")
    public Result<ShopDetailVO> getShopDetail(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam("shopId") @ApiParam("店铺ID") Long shopId) {
        ShopListItemDTO shopListItemDto = Optional.ofNullable(shopApi.getShopDetail(shopId)).orElseThrow(() -> new BusinessException(UserErrorCode.SHOP_NOT_EXISTS));
        ShopDetailVO shopDetailVO = PojoUtils.map(shopListItemDto, ShopDetailVO.class);

        EnterpriseDTO enterpriseDTO = Optional.ofNullable(enterpriseApi.getById(shopDetailVO.getShopEid())).orElse(new EnterpriseDTO());
        shopDetailVO.setStatus(enterpriseDTO.getStatus());

        //获取商品种类
        Long goodsKind = b2bGoodsApi.countB2bGoodsByEids(ListUtil.toList(shopDetailVO.getShopEid()));
        shopDetailVO.setGoodsKind(goodsKind.intValue());

        // 获取月售量
        QueryBackOrderInfoRequest request = new QueryBackOrderInfoRequest();
        request.setSellerEid(shopDetailVO.getShopEid());
        request.setOrderStatusList(ListUtil.toList(OrderStatusEnum.RECEIVED.getCode(), OrderStatusEnum.FINISHED.getCode()));
        request.setOrderType(2);
        request.setStartCreateTime(DateUtil.beginOfMonth(new Date()));
        request.setEndCreateTime(new Date());
        Page<OrderDTO> backOrderPage = orderApi.getBackOrderPage(request);
        long total = backOrderPage.getTotal();
        shopDetailVO.setMonthSales((int) total);

        // 设置是否存在楼层
        boolean floorFlag = shopFloorApi.existFloorFlag(shopDetailVO.getShopEid());
        shopDetailVO.setExistFloor(floorFlag);

        // 是否为优质商家
        shopDetailVO.setHighQualitySupplierFlag(enterpriseHighQualitySupplierApi.getHighQualitySupplierFlag(shopDetailVO.getShopEid()));

        //获取采购关系
        boolean openFlag = enterpriseCustomerLineApi.getCustomerLineFlag(enterpriseDTO.getId(), staffInfo.getCurrentEid(), EnterpriseCustomerLineEnum.B2B);
        if (openFlag) {
            shopDetailVO.setPurchaseStatus(EnterprisePurchaseApplyStatusEnum.ESTABLISHED.getCode());
        } else {
            EnterprisePurchaseApplyBO purchaseApplyBO = enterprisePurchaseApplyApi.getByCustomerEid(staffInfo.getCurrentEid(), enterpriseDTO.getId());
            if (Objects.isNull(purchaseApplyBO)) {
                shopDetailVO.setPurchaseStatus(4);
            } else {
                shopDetailVO.setPurchaseStatus(purchaseApplyBO.getAuthStatus());
            }
        }

        return Result.success(shopDetailVO);
    }

    @ApiOperation(value = "获取企业资质")
    @GetMapping("/getEnterpriseCertificate")
    public Result<ShopCertificateVO> getEnterpriseCertificate(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam("shopEid") @ApiParam("企业ID") Long shopEid) {
        //企业信息
        EnterpriseDTO enterpriseDTO = Optional.ofNullable(enterpriseApi.getById(shopEid)).orElseThrow(() -> new BusinessException(UserErrorCode.ENTERPRISE_NOT_EXISTS));
        ShopCertificateVO shopCertificateVO = PojoUtils.map(enterpriseDTO, ShopCertificateVO.class);
        //店铺信息
        PojoUtils.map(shopApi.getShop(shopEid), shopCertificateVO);

        //获取商品种类
        Long goodsKind = b2bGoodsApi.countB2bGoodsByEids(ListUtil.toList(shopEid));
        shopCertificateVO.setGoodsKind(goodsKind.intValue());

        // 企业类型对应的资质列表
        List<EnterpriseCertificateTypeEnum> enterpriseCertificateTypeEnumList = Objects.requireNonNull(EnterpriseTypeEnum.getByCode(enterpriseDTO.getType())).getCertificateTypeEnumList();
        // 已上传的企业资质列表
        List<EnterpriseCertificateDTO> enterpriseCertificateDTOList = certificateApi.listByEid(shopEid);
        Map<Integer, EnterpriseCertificateDTO> enterpriseCertificateDtoMap = enterpriseCertificateDTOList.stream().collect(Collectors.toMap(EnterpriseCertificateDTO::getType, Function.identity()));

        List<EnterpriseCertificateVO> enterpriseCertificateVOList = CollUtil.newArrayList();
        enterpriseCertificateTypeEnumList.forEach(e -> {
            EnterpriseCertificateVO enterpriseCertificateVO = new EnterpriseCertificateVO();
            enterpriseCertificateVO.setType(e.getCode());
            enterpriseCertificateVO.setName(e.getName());
            enterpriseCertificateVO.setPeriodRequired(e.getCollectDate());
            enterpriseCertificateVO.setRequired(e.getMustExist());

            EnterpriseCertificateDTO enterpriseCertificateDTO = enterpriseCertificateDtoMap.get(e.getCode());
            if (enterpriseCertificateDTO != null) {
                enterpriseCertificateVO.setFileUrl(fileService.getUrl(enterpriseCertificateDTO.getFileKey(), FileTypeEnum.ENTERPRISE_CERTIFICATE));
                enterpriseCertificateVO.setFileKey(enterpriseCertificateDTO.getFileKey());
                enterpriseCertificateVO.setPeriodBegin(enterpriseCertificateDTO.getPeriodBegin());
                enterpriseCertificateVO.setPeriodEnd(enterpriseCertificateDTO.getPeriodEnd());
                enterpriseCertificateVO.setLongEffective(enterpriseCertificateDTO.getLongEffective());
            }
            enterpriseCertificateVOList.add(enterpriseCertificateVO);
        });
        shopCertificateVO.setCertificateVoList(enterpriseCertificateVOList);

        return Result.success(shopCertificateVO);
    }

    @ApiOperation(value = "获取优惠券分页列表")
    @PostMapping("/getCouponsPage")
    public Result<CollectionObject<CouponActivityHasGetVO>> getCouponsPage(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryShopCouponsPageForm form) {
        QueryCouponActivityCanReceivePageRequest request = PojoUtils.map(form, QueryCouponActivityCanReceivePageRequest.class);
        request.setEid(form.getEid());
        request.setCurrentEid(staffInfo.getCurrentEid());
        request.setPlatformType(CouponPlatformTypeEnum.B2B.getCode());
        List<CouponActivityHasGetDTO> listPage = couponActivityApi.getCouponActivityListPageByEid(request);
        return Result.success(new CollectionObject(listPage));
    }

    @ApiOperation(value = "领券中心列表")
    @PostMapping("/getCouponsCenter")
    public Result<Page<CouponActivityVO>> getCouponsCenter(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryShopCouponsPageForm form) {
        QueryCouponActivityCanReceivePageRequest request = PojoUtils.map(form, QueryCouponActivityCanReceivePageRequest.class);
        request.setEid(staffInfo.getCurrentEid());
        request.setCurrentEid(staffInfo.getCurrentEid());
        request.setPlatformType(CouponPlatformTypeEnum.B2B.getCode());
        Page<CouponActivityEnterpDTO> listPage = couponActivityApi.getCouponsCenter(request);
        log.info("getCouponsCenter result"+ JSONUtil.toJsonStr(listPage));
        return Result.success(PojoUtils.map(listPage, CouponActivityVO.class));
    }


    @ApiOperation(value = "会员券列表")
    @PostMapping("/getMemberCouponsCenter")
    public Result<CollectionObject<CouponActivityHasGetVO>> getMemberCouponsCenter(@CurrentUser CurrentStaffInfo staffInfo) {
        QueryCouponActivityCanReceivePageRequest request = new QueryCouponActivityCanReceivePageRequest();
        request.setEid(staffInfo.getCurrentEid());
        request.setCurrentEid(staffInfo.getCurrentEid());
        request.setPlatformType(CouponPlatformTypeEnum.B2B.getCode());
        List<CouponActivityHasGetDTO> listPage = couponActivityApi.getMemberCouponsCenter(request);
        if (CollectionUtils.isNotEmpty(listPage)) {
            listPage.forEach(item -> {
                transNumIntoChinese(item);
            });
        }
        CollectionObject collectionObject = new CollectionObject(listPage);
        return Result.success(collectionObject);
    }

    private void transNumIntoChinese(CouponActivityHasGetDTO activityHasGetDTO) {
        if(ObjectUtil.isNull(activityHasGetDTO)){
            return;
        }
        String payMethodDescribe = "";
        if (StringUtils.isEmpty(activityHasGetDTO.getPayMethodSelected())) {
            payMethodDescribe += Arrays.stream(PayMethodTypeEnum.values()).map(PayMethodTypeEnum::getName).collect(Collectors.joining("、"));
        }
        if (StringUtils.isNotEmpty(activityHasGetDTO.getPayMethodSelected())) {
            payMethodDescribe += Arrays.stream(activityHasGetDTO.getPayMethodSelected().split("\\,")).map(item -> PayMethodTypeEnum.getByCode(Integer.valueOf(item))).collect(Collectors.joining("、"));
        }
        activityHasGetDTO.setPayMethodSelected(payMethodDescribe);
    }

    @ApiOperation(value = "领取优惠券")
    @PostMapping("/receiveCoupons")
    public Result<GetCouponActivityResultVO> receiveCoupons(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid GetCouponsForm form) {
        log.info("2getCoupon"+staffInfo.getCurrentEid()+"form"+form);
        EnterpriseDTO enterpriseDTO = Optional.ofNullable(enterpriseApi.getById(staffInfo.getCurrentEid())).orElseThrow(() -> new BusinessException(UserErrorCode.ENTERPRISE_NOT_EXISTS));

        UserDTO userDTO = Optional.ofNullable(userApi.getById(staffInfo.getCurrentUserId())).orElse(new UserDTO());
        CurrentMemberForMarketingDTO member = Optional.ofNullable(memberApi.getCurrentMemberForMarketing(staffInfo.getCurrentEid())).orElse(new CurrentMemberForMarketingDTO());

        CouponActivityReceiveRequest request = new CouponActivityReceiveRequest().setCouponActivityId(form.getCouponActivityId()).setEid(staffInfo.getCurrentEid()).setEname(enterpriseDTO.getName()).setUserId(staffInfo.getCurrentUserId()).setUserName(userDTO.getName()).setEtype(enterpriseDTO.getType()).setCurrentMember(member.getCurrentMember()).setPlatformType(CouponPlatformTypeEnum.B2B.getCode());
        log.info("立即领取优惠券，调用优惠券接口请求入参：{}", JSONObject.toJSONString(request));
        GetCouponActivityResultDTO getCouponActivityResultDTO = couponActivityApi.receiveByCouponActivityIdForApp(request);
        getCouponActivityResultDTO.setId(form.getCouponActivityId());
        return Result.success(PojoUtils.map(getCouponActivityResultDTO, GetCouponActivityResultVO.class));
    }

    @ApiOperation(value = "获取优惠券列表-获取可领取的优惠券列表")
    @GetMapping("/getCouponsList")
    public Result<CollectionObject<CouponActivityHasGetVO>> getCouponsList(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam("eid") @ApiParam("选择的企业ID") Long eid) {

        QueryCouponActivityCanReceiveRequest request = new QueryCouponActivityCanReceiveRequest();
        request.setCurrentEid(staffInfo.getCurrentEid());
        request.setEid(eid);
        request.setLimit(10);
        request.setPlatformType(CouponPlatformTypeEnum.B2B.getCode());
        List<CouponActivityHasGetDTO> activityHasGetDtoList = couponActivityApi.getCouponActivityListByEid(request);

        List<CouponActivityHasGetVO> activityHasGetVOList = PojoUtils.map(activityHasGetDtoList, CouponActivityHasGetVO.class);

        return Result.success(new CollectionObject<>(activityHasGetVOList));
    }


    @ApiOperation(value = "获取店铺优质商品列表")
    @GetMapping("/getShopGoodsList")
    public Result<CollectionObject<ShopGreatGoodsVO>> getShopGoodsList(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam("shopEid") @ApiParam("企业ID") Long shopEid) {
        //根据企业ID，调用企业对应的优质商品（即销量为前30的商品）
        List<GoodsDTO> b2bGoodsSaleList = b2bGoodsApi.getB2bGoodsSaleTopLimit(ListUtil.toList(shopEid), 30);
        //获取商品定价系统
        List<Long> goodsIds = b2bGoodsSaleList.stream().map(GoodsDTO::getId).collect(Collectors.toList());
        QueryGoodsPriceRequest queryGoodsPriceRequest = new QueryGoodsPriceRequest();
        queryGoodsPriceRequest.setCustomerEid(staffInfo.getCurrentEid());
        queryGoodsPriceRequest.setGoodsIds(goodsIds);
        Map<Long, GoodsPriceDTO> priceMap = goodsPriceApi.queryB2bGoodsPriceInfoMap(queryGoodsPriceRequest);

        List<StandardGoodsBasicDTO> standardGoodsBasicDTOList = goodsApi.batchQueryStandardGoodsBasic(goodsIds);
        Map<Long, StandardGoodsBasicDTO> standardGoodsBasicDtoMap = standardGoodsBasicDTOList.stream().collect(Collectors.toMap(StandardGoodsBasicDTO::getId, Function.identity()));


        ShopDTO shopDTO = Optional.ofNullable(shopApi.getShop(shopEid)).orElseThrow(() -> new BusinessException(UserErrorCode.ENTERPRISE_NOT_EXISTS));
        //获取预售信息
        QueryPresaleInfoRequest presaleRequest = new QueryPresaleInfoRequest();
        presaleRequest.setBuyEid(staffInfo.getCurrentEid());
        presaleRequest.setGoodsId(goodsIds);
        presaleRequest.setPlatformSelected(1);
        List<PresaleActivityGoodsDTO> presaleInfoList = presaleActivityApi.getPresaleInfoByGoodsIdAndBuyEidNoNum(presaleRequest);
        Map<Long, PresaleActivityGoodsDTO> presaleInfoMap;
        if(CollectionUtil.isNotEmpty(presaleInfoList)){
            presaleInfoMap = presaleInfoList.stream().collect(Collectors.toMap(PresaleActivityGoodsDTO::getGoodsId, Function.identity(),(k1, k2)->k1));
        }else {
            presaleInfoMap = new HashMap<>();
        }
        List<ShopGreatGoodsVO> goodsVOList = b2bGoodsSaleList.stream().map(goodsInfoDTO -> {
            ShopGreatGoodsVO goodsVO = new ShopGreatGoodsVO();
            StandardGoodsBasicDTO standardGoodsBasicDTO = standardGoodsBasicDtoMap.get(goodsInfoDTO.getId());
            goodsVO.setGoodsId(goodsInfoDTO.getId());
            goodsVO.setGoodsName(standardGoodsBasicDTO.getStandardGoods().getName());
            GoodsPriceDTO goodsPriceDTO = priceMap.get(goodsInfoDTO.getId());
            goodsVO.setPrice(goodsPriceDTO.getBuyPrice());
            goodsVO.setPic(pictureUrlUtils.getGoodsPicUrl(goodsInfoDTO.getPic()));
            goodsVO.setShopName(shopDTO.getShopName());
            goodsVO.setEname(standardGoodsBasicDTO.getStandardGoods().getManufacturer());
            goodsVO.setSpecifications(standardGoodsBasicDTO.getSellSpecifications());
            GoodsPriceVO goodsPriceVO = new GoodsPriceVO();
            goodsPriceVO.setBuyPrice(goodsPriceDTO.getBuyPrice());
            goodsPriceVO.setIsShow(goodsPriceDTO.getIsShow());
            goodsVO.setGoodsPriceVO(goodsPriceVO);
            //预售
            goodsVO.setPresaleInfoVO(PojoUtils.map(presaleInfoMap.get(goodsInfoDTO.getId()), PresaleInfoVO.class));
            return goodsVO;
        }).collect(Collectors.toList());

        return Result.success(new CollectionObject<>(goodsVOList));
    }

    @ApiOperation(value = "获取楼层列表")
    @GetMapping("/getShopFloorList")
    public Result<CollectionObject<ShopFloorBasicVO>> getShopFloorList(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam("shopEid") @ApiParam("企业ID") Long shopEid) {
        List<ShopFloorDTO> shopFloorList = shopFloorApi.getShopFloorList(shopEid);
        List<ShopFloorBasicVO> basicVOList = PojoUtils.map(shopFloorList, ShopFloorBasicVO.class);
        return Result.success(new CollectionObject<>(basicVOList));
    }

    @ApiOperation(value = "获取楼层商品分页")
    @PostMapping("/queryFloorGoodsPage")
    public Result<Page<ShopGreatGoodsVO>> queryFloorGoodsPage(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryFloorGoodsPageForm form) {
        QueryFloorGoodsPageRequest request = PojoUtils.map(form, QueryFloorGoodsPageRequest.class);
        Page<ShopFloorGoodsDTO> floorGoodsDTOPage = shopFloorApi.queryFloorGoodsPage(request);

        Page<ShopGreatGoodsVO> voPage = PojoUtils.map(floorGoodsDTOPage, ShopGreatGoodsVO.class);
        // 查询商品信息
        List<Long> goodsIdList = floorGoodsDTOPage.getRecords().stream().map(ShopFloorGoodsDTO::getGoodsId).distinct().collect(Collectors.toList());
        if (CollUtil.isEmpty(goodsIdList)) {
            return Result.success(voPage);
        }

        // 获取店铺名称
        ShopFloorDTO shopFloorDTO = shopFloorApi.get(form.getFloorId());
        ShopDTO shopDTO = Optional.ofNullable(shopApi.getShop(shopFloorDTO.getEid())).orElseThrow(() -> new BusinessException(UserErrorCode.ENTERPRISE_NOT_EXISTS));

        //获取商品定价系统
        QueryGoodsPriceRequest queryGoodsPriceRequest = new QueryGoodsPriceRequest();
        queryGoodsPriceRequest.setGoodsIds(goodsIdList);
        queryGoodsPriceRequest.setCustomerEid(staffInfo.getCurrentEid());
        Map<Long, GoodsPriceDTO> priceMap = goodsPriceApi.queryB2bGoodsPriceInfoMap(queryGoodsPriceRequest);

        // 获取商品基础信息
        List<StandardGoodsBasicDTO> standardGoodsBasicDTOList = goodsApi.batchQueryStandardGoodsBasic(goodsIdList);
        Map<Long, StandardGoodsBasicDTO> standardGoodsBasicDtoMap = standardGoodsBasicDTOList.stream().collect(Collectors.toMap(StandardGoodsBasicDTO::getId, Function.identity()));

        //获取预售信息
        QueryPresaleInfoRequest presaleRequest = new QueryPresaleInfoRequest();
        presaleRequest.setBuyEid(staffInfo.getCurrentEid());
        presaleRequest.setGoodsId(goodsIdList);
        presaleRequest.setPlatformSelected(1);
        List<PresaleActivityGoodsDTO> presaleInfoList = presaleActivityApi.getPresaleInfoByGoodsIdAndBuyEidNoNum(presaleRequest);
        Map<Long, PresaleActivityGoodsDTO> presaleInfoMap = MapUtil.newHashMap();
        if(CollectionUtil.isNotEmpty(presaleInfoList)){
            presaleInfoMap = presaleInfoList.stream().collect(Collectors.toMap(PresaleActivityGoodsDTO::getGoodsId, Function.identity(), (k1, k2)->k1));
        }

        // 设置商品信息对象
        Map<Long, PresaleActivityGoodsDTO> finalPresaleInfoMap = presaleInfoMap;
        List<ShopGreatGoodsVO> goodsVOList = floorGoodsDTOPage.getRecords().stream().map(floorGoodsDTO -> {
            ShopGreatGoodsVO goodsVO = new ShopGreatGoodsVO();
            StandardGoodsBasicDTO standardGoodsBasicDTO = standardGoodsBasicDtoMap.get(floorGoodsDTO.getGoodsId());
            goodsVO.setGoodsId(floorGoodsDTO.getGoodsId());
            goodsVO.setGoodsName(standardGoodsBasicDTO.getStandardGoods().getName());
            goodsVO.setPic(pictureUrlUtils.getGoodsPicUrl(standardGoodsBasicDTO.getPic()));
            goodsVO.setShopName(shopDTO.getShopName());
            goodsVO.setEname(standardGoodsBasicDTO.getStandardGoods().getManufacturer());
            goodsVO.setSpecifications(standardGoodsBasicDTO.getSellSpecifications());
            GoodsPriceDTO goodsPriceDTO = priceMap.get(floorGoodsDTO.getGoodsId());
            GoodsPriceVO goodsPriceVO = new GoodsPriceVO();
            goodsPriceVO.setBuyPrice(goodsPriceDTO.getBuyPrice());
            goodsPriceVO.setIsShow(goodsPriceDTO.getIsShow());
            goodsVO.setGoodsPriceVO(goodsPriceVO);
            //预售
            goodsVO.setPresaleInfoVO(PojoUtils.map(finalPresaleInfoMap.get(floorGoodsDTO.getGoodsId()), PresaleInfoVO.class));
            return goodsVO;
        }).collect(Collectors.toList());
        voPage.setRecords(goodsVOList);

        return Result.success(voPage);
    }

}
