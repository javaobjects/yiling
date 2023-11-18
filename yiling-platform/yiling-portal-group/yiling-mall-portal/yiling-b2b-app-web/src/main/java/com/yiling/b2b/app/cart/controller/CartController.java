package com.yiling.b2b.app.cart.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yiling.b2b.app.cart.form.AddToCartForm;
import com.yiling.b2b.app.cart.form.CombinationAddCartForm;
import com.yiling.b2b.app.cart.form.RemoveCartGoodsForm;
import com.yiling.b2b.app.cart.form.SelectCartGoodsForm;
import com.yiling.b2b.app.cart.form.UpdateCartGoodsQuantityForm;
import com.yiling.b2b.app.cart.util.SimpleGoodInfoUtils;
import com.yiling.b2b.app.cart.util.StockUtils;
import com.yiling.b2b.app.cart.vo.CartDistributorVO;
import com.yiling.b2b.app.cart.vo.CartGoodsVO;
import com.yiling.b2b.app.cart.vo.CartListVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.annotations.UserAccessAuthentication;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.pojo.vo.SimpleGoodsVO;
import com.yiling.framework.common.redis.RedisDistributedLock;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.goods.inventory.api.InventoryApi;
import com.yiling.goods.inventory.dto.InventoryDTO;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsSkuDTO;
import com.yiling.goods.medicine.dto.GoodsSkuStandardBasicDTO;
import com.yiling.goods.medicine.enums.GoodsSkuStatusEnum;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.mall.agreement.api.AgreementBusinessApi;
import com.yiling.mall.agreement.enums.GoodsLimitStatusEnum;
import com.yiling.mall.cart.api.CartApi;
import com.yiling.mall.cart.dto.CartDTO;
import com.yiling.mall.cart.dto.request.AddToCartRequest;
import com.yiling.mall.cart.dto.request.BatchAddToCartRequest;
import com.yiling.mall.cart.dto.request.CombinationAddToCartRequest;
import com.yiling.mall.cart.dto.request.GetCartGoodsInfoRequest;
import com.yiling.mall.cart.dto.request.RemoveCartGoodsRequest;
import com.yiling.mall.cart.dto.request.SelectCartGoodsRequest;
import com.yiling.mall.cart.dto.request.UpdateCartGoodsQuantityRequest;
import com.yiling.mall.cart.enums.CartGoodsSourceEnum;
import com.yiling.mall.cart.enums.CartGoodsStatusEnum;
import com.yiling.mall.cart.enums.CartIncludeEnum;
import com.yiling.marketing.promotion.api.PromotionActivityApi;
import com.yiling.marketing.promotion.dto.PromotionAppListDTO;
import com.yiling.marketing.promotion.dto.PromotionDTO;
import com.yiling.marketing.promotion.dto.PromotionGoodsLimitDTO;
import com.yiling.marketing.promotion.dto.request.PromotionActivityRequest;
import com.yiling.marketing.promotion.dto.request.PromotionAppCartGoods;
import com.yiling.marketing.promotion.dto.request.PromotionAppCartRequest;
import com.yiling.marketing.promotion.dto.request.PromotionAppRequest;
import com.yiling.marketing.promotion.enums.PromotionTypeEnum;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderDetailApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDetailDTO;
import com.yiling.order.order.enums.OrderErrorCode;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.order.order.enums.PromotionActivityTypeEnum;
import com.yiling.pricing.goods.api.GoodsPriceApi;
import com.yiling.pricing.goods.dto.GoodsPriceDTO;
import com.yiling.pricing.goods.dto.request.QueryGoodsPriceRequest;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.shop.api.ShopApi;
import com.yiling.user.shop.dto.ShopDTO;
import com.yiling.user.system.bo.CurrentStaffInfo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.comparator.CompareUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

/**
 * 进货单相关接口
 *
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.sales.assistant.app.order.controller
 * @date: 2021/9/14
 */
@RestController
@RequestMapping("/cart")
@Api(tags = "进货单接口")
@Slf4j
public class CartController extends BaseController {
    @DubboReference
    CartApi              cartApi;
    @DubboReference
    EnterpriseApi        enterpriseApi;
    @DubboReference
    GoodsApi             goodsApi;
    @DubboReference
    GoodsPriceApi        goodsPriceApi;
    @DubboReference
    ShopApi              shopApi;
    @DubboReference
    PromotionActivityApi promotionActivityApi;
    @DubboReference
    AgreementBusinessApi agreementBusinessApi;
    @DubboReference
    OrderApi             orderApi;
    @DubboReference
    OrderDetailApi       orderDetailApi;
    @DubboReference
    InventoryApi         inventoryApi;
    @Autowired
    RedisDistributedLock redisDistributedLock;
    @Autowired
    FileService          fileService;

    @ApiOperation(value = "获取购物车商品数量及金额")
    @GetMapping("/getCartGoodsNum")
    @UserAccessAuthentication
    public Result<Integer> getCartGoodsNum(@CurrentUser CurrentStaffInfo staffInfo) {

        if (staffInfo == null) {
            return Result.success(0);
        }
        Integer cartNum = cartApi.getCartGoodsNum(staffInfo.getCurrentEid(), PlatformEnum.B2B, CartGoodsSourceEnum.B2B);

        return Result.success(cartNum);
    }


    @ApiOperation(value = "组合优惠商品添加")
    @ApiResponses({
            @ApiResponse(code = 121103, message = "商品库存不足"),
            @ApiResponse(code = 10343, message = "活动已失效"),
            @ApiResponse(code = 10344, message = "加购数量小于最小起订量"),
            @ApiResponse(code = 10362, message = "限购xxx包，已达限购数"),
            @ApiResponse(code = 10364, message = "组合包数量不足!")
    })
    @PostMapping("/combination/add")
    @UserAccessAuthentication(checkEnterpriseAuthStatus = true, checkEnterpriseStatus = true, checkUserEnterpriseRelation = true, checkUserStatus = true)
    public Result<String> addByCombinationProduct(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid CombinationAddCartForm form) {
        CombinationAddToCartRequest request = PojoUtils.map(form,CombinationAddToCartRequest.class);
        request.setBuyerEid(staffInfo.getCurrentEid());
        request.setPlatformEnum(PlatformEnum.B2B);
        request.setGoodsSourceEnum(CartGoodsSourceEnum.B2B);
        request.setOpUserId(staffInfo.getCurrentUserId());
        String lockName = RedisKey.generate("cart",PlatformEnum.B2B.getCode().toString(),"add",staffInfo.getCurrentEid().toString(),form.getPromotionActivityId().toString());
        String lockId = "";
        try {
            lockId = redisDistributedLock.lock2(lockName, 60, 60, TimeUnit.SECONDS);
            boolean effectResult = cartApi.addToCartByCombinationProduct(request);
            if (effectResult) {
                return Result.success("添加成功!");
            }
            return Result.failed("添加失败!");
        } finally {
            redisDistributedLock.releaseLock(lockName, lockId);
        }
    }

    /**
     * 校验接口加购类型(只允许添加，秒杀&特价以及普通商品)
     * @param promotionActivityType
     * @return
     */
    private boolean checkPromotionActivityType(Integer promotionActivityType) {
        PromotionActivityTypeEnum promotionActivityTypeEnum = PromotionActivityTypeEnum.getByCode(promotionActivityType);
        if (promotionActivityTypeEnum == null) {
            return false;
        }
        switch (promotionActivityTypeEnum) {
            case SPECIAL:
            case LIMIT:
            case NORMAL:
                return true;
            default: return false;
        }
    }

    /**
     *  获取以岭商品ID
     * @param goodsId 以岭商品Id
     * @param distributorGoodsId 配送商商品ID
     * @return 返回以岭商品Id
     */
    private Long getYilingGoodsId(Long goodsId,Long distributorGoodsId) {
        if (goodsId != null && !Long.valueOf(0).equals(goodsId)) {
            return goodsId;
        }
        Map<Long, Long> yilingGoodsMap = goodsApi.getYilingGoodsIdByGoodsIdAndYilingEids(ListUtil.toList(distributorGoodsId),enterpriseApi.listSubEids(Constants.YILING_EID));
        Long flag = Optional.ofNullable(yilingGoodsMap.get(distributorGoodsId)).orElse(0L);
        boolean notYl = Long.valueOf(0).equals(flag);
        if (notYl) {
            return distributorGoodsId;
        }
        return yilingGoodsMap.get(distributorGoodsId);
    }

    @ApiOperation(value = "添加商品到购物车(普通商品&特价&秒杀加购)")
    @PostMapping("/add")
    @ApiResponses({
            @ApiResponse(code = 10356, message = "限购XX件，已达限购数!"),
            @ApiResponse(code = 10357, message = "每天限购XXX件，已达限购数!"),
            @ApiResponse(code = 10358, message = "每周限购xx件，已达限购数!"),
            @ApiResponse(code = 10359, message = "每月限购xx件，已达限购数!"),
            @ApiResponse(code = 10360, message = "xx天内限购xx件，已达限购数!"),
    })
    @UserAccessAuthentication(checkEnterpriseAuthStatus = true, checkEnterpriseStatus = true, checkUserEnterpriseRelation = true, checkUserStatus = true)
    public Result<String> add(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid AddToCartForm form) {
        if (form.getPromotionActivityType() == null) {
            form.setPromotionActivityType(0);
        }
        if (!checkPromotionActivityType(form.getPromotionActivityType())) {
            return Result.failed("商品促销类型错误!");
        }
        AddToCartRequest request = PojoUtils.map(form, AddToCartRequest.class);
        request.setBuyerEid(staffInfo.getCurrentEid());
        request.setGoodsSourceEnum(CartGoodsSourceEnum.B2B);
        request.setPlatformEnum(PlatformEnum.B2B);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setPromotionActivityType(form.getPromotionActivityType());
        String lockName = RedisKey.generate("cart",PlatformEnum.B2B.getCode().toString(),"add",staffInfo.getCurrentEid().toString(),form.getGoodsSkuId().toString());
        String lockId = "";
        try {
            lockId = redisDistributedLock.lock2(lockName, 60, 60, TimeUnit.SECONDS);
            // 设置以岭goodsId
            request.setGoodsId(this.getYilingGoodsId(form.getGoodsId(),form.getDistributorGoodsId()));
            boolean isSpecialAdd = false;
            switch (PromotionActivityTypeEnum.getByCode(form.getPromotionActivityType())) {
                case LIMIT:
                case SPECIAL:
                    isSpecialAdd = true;
                    break;
                default: isSpecialAdd = false;break;
            }
            if (isSpecialAdd) {
                return this.addBySpecialProduct(request);
            }
            if (cartApi.addToCart(request)) {
                return Result.success("添加成功!");
            } else {
                return Result.failed("添加失败!");
            }
        } finally {
            redisDistributedLock.releaseLock(lockName, lockId);
        }
    }


    /**
     * 查询秒杀&特价促销活动信息
     * @param platformEnum 平台类型
     * @param buyerEid 买家EID
     * @param distributorGoodsIds 商品IDs
     * @return
     */
    private List<PromotionGoodsLimitDTO> selectSpecialGoodsActivity(PlatformEnum platformEnum,Long buyerEid,List<Long> distributorGoodsIds) {

        if (CollectionUtil.isEmpty(distributorGoodsIds)) {
            return Collections.emptyList();
        }

        PromotionAppRequest appRequest = new PromotionAppRequest();
        appRequest.setPlatform(platformEnum.getCode());
        appRequest.setBuyerEid(buyerEid);
        appRequest.setTypeList(ListUtil.toList(PromotionTypeEnum.SPECIAL_PRICE.getType(),PromotionTypeEnum.SECOND_KILL.getType()));
        appRequest.setGoodsIdList(ListUtil.toList(distributorGoodsIds));
        List<PromotionGoodsLimitDTO> promotionGoodsLimitDTOList = promotionActivityApi.queryGoodsPromotionInfo(appRequest);

        if (log.isDebugEnabled()) {

            log.debug("..selectSpecialGoodsActivity..request ->{}...,result->{}",JSON.toJSON(appRequest),JSON.toJSON(promotionGoodsLimitDTOList));
        }

        return CollectionUtil.isEmpty(promotionGoodsLimitDTOList) ? Collections.emptyList() : promotionGoodsLimitDTOList;

    }


    /**
     * 校验赠品数量是否满足购买规格包装
     * @param skuIdNumberMap
     */
    private void checkPackageNumber(Map<Long,Integer> skuIdNumberMap) {
        List<GoodsSkuDTO> skuDTOList =  goodsApi.getGoodsSkuByIds(new ArrayList(skuIdNumberMap.keySet()));
        if (CollectionUtil.isEmpty(skuDTOList)) {
            throw new BusinessException(OrderErrorCode.SKU_NOT_EXIST);
        }
        Map<Long,GoodsSkuDTO> skuDTOMap =  skuDTOList.stream().collect(Collectors.toMap(GoodsSkuDTO::getId,Function.identity()));
        for (Long skuId :skuIdNumberMap.keySet()) {
            GoodsSkuDTO skuDTO = skuDTOMap.get(skuId);
            if (skuDTO == null) {
                throw new BusinessException(OrderErrorCode.SKU_NOT_EXIST);
            }
            Integer number = skuIdNumberMap.get(skuId);
            if (number % skuDTO.getPackageNumber() != 0) {
                throw new BusinessException(OrderErrorCode.SKU_PACKAGE_NUMBER_EXIST);
            }
        }
    }

    /**
     * 添加特价商品信息
     * @param request
     * @return
     */
    private Result<String> addBySpecialProduct(AddToCartRequest request) {
        List<PromotionGoodsLimitDTO> promotionGoodsLimitDTOList = this.selectSpecialGoodsActivity(request.getPlatformEnum(),request.getBuyerEid(),ListUtil.toList(request.getDistributorGoodsId()));
        if (CollectionUtil.isEmpty(promotionGoodsLimitDTOList)) {
            return Result.failed("促销活动已失效,请确认!");
        }
        List<GoodsSkuDTO> skuDTOList =  goodsApi.getGoodsSkuByIds(ListUtil.toList(request.getGoodsSkuId()));
        GoodsSkuDTO goodsSkuDTO = skuDTOList.stream().findAny().get();
        Optional<PromotionGoodsLimitDTO> first = promotionGoodsLimitDTOList.stream().filter(t -> t.getType().equals(request.getPromotionActivityType())).findFirst();
        if (first == null) {
            return Result.failed("促销活动已失效,请确认!");
        }
        this.checkPackageNumber(Collections.singletonMap(request.getGoodsSkuId(),request.getQuantity()));
        PromotionGoodsLimitDTO promotionGoodsLimitDto = first.get(); ;
        Integer leftBuyCount = promotionGoodsLimitDto.getLeftBuyCount();
        GetCartGoodsInfoRequest getCartGoodsInfoRequest = new GetCartGoodsInfoRequest();
        getCartGoodsInfoRequest.setGoodsId(request.getDistributorGoodsId());
        getCartGoodsInfoRequest.setBuyerEid(request.getBuyerEid());
        getCartGoodsInfoRequest.setDistributorEid(request.getDistributorEid());
        getCartGoodsInfoRequest.setGoodsSourceEnum(request.getGoodsSourceEnum());
        getCartGoodsInfoRequest.setPlatformEnum(request.getPlatformEnum());
        getCartGoodsInfoRequest.setPromotionActivityTypeEnum(PromotionActivityTypeEnum.getByCode(request.getPromotionActivityType()));
        getCartGoodsInfoRequest.setPromotionActivityId(promotionGoodsLimitDto.getPromotionActivityId());
        Integer quantity = cartApi.sumGoodsQuantityByGoodId(getCartGoodsInfoRequest);
        // 可以秒杀数量
        Integer canUseLimitNumber = leftBuyCount - quantity;
        if (CompareUtil.compare(canUseLimitNumber,0) <= 0) {
            canUseLimitNumber = 0;
        } else {
            Long ratio  = canUseLimitNumber / goodsSkuDTO.getPackageNumber();
            canUseLimitNumber = ratio.intValue() * goodsSkuDTO.getPackageNumber().intValue();
        }
        // 如果加购秒杀数量小于可秒杀数量,将以页面加购秒杀数量为准
        if (CompareUtil.compare(request.getQuantity(),canUseLimitNumber) < 0) {
            canUseLimitNumber = request.getQuantity();
        }
        Integer diffCount = request.getQuantity() -  canUseLimitNumber;
        StringBuilder tipMsg = new StringBuilder("");
        // 表示为购买数量大于秒杀可购买数量
        if (CompareUtil.compare(diffCount,0) > 0 ) {
            AddToCartRequest addToCartRequest = new AddToCartRequest();
            addToCartRequest.setGoodsId(request.getGoodsId());
            addToCartRequest.setQuantity(diffCount);
            addToCartRequest.setDistributorEid(goodsSkuDTO.getEid());
            addToCartRequest.setDistributorGoodsId(request.getDistributorGoodsId());
            addToCartRequest.setGoodsSkuId(request.getGoodsSkuId());
            addToCartRequest.setBuyerEid(request.getBuyerEid());
            addToCartRequest.setPlatformEnum(request.getPlatformEnum());
            addToCartRequest.setPromotionActivityType(PromotionActivityTypeEnum.NORMAL.getCode());
            addToCartRequest.setGoodsSourceEnum(request.getGoodsSourceEnum());
            addToCartRequest.setOpUserId(request.getOpUserId());
            // 添加原价部分商品数量
            cartApi.addToCart(addToCartRequest);
            tipMsg = new StringBuilder("活动价只可购买");
            tipMsg.append(CompareUtil.compare(canUseLimitNumber,0) <= 0 ? 0 : canUseLimitNumber + "盒,");
            tipMsg.append("剩余" + Math.abs(diffCount)).append("盒以原价加入进货单");
        } else {
            tipMsg = tipMsg.append("添加成功!");
        }
        if (CompareUtil.compare(canUseLimitNumber,0) <= 0) {
            return Result.success("商品已达到活动上限,将以原价购买");
        }
        // 添加秒杀库存数量
        AddToCartRequest specialCartRequest = new AddToCartRequest();
        specialCartRequest.setGoodsId(request.getGoodsId());
        specialCartRequest.setQuantity(canUseLimitNumber);
        specialCartRequest.setDistributorEid(goodsSkuDTO.getEid());
        specialCartRequest.setDistributorGoodsId(request.getDistributorGoodsId());
        specialCartRequest.setGoodsSkuId(request.getGoodsSkuId());
        specialCartRequest.setBuyerEid(request.getBuyerEid());
        specialCartRequest.setPromotionActivityType(promotionGoodsLimitDto.getType());
        specialCartRequest.setPromotionActivityId(promotionGoodsLimitDto.getPromotionActivityId());
        specialCartRequest.setPlatformEnum(request.getPlatformEnum());
        specialCartRequest.setGoodsSourceEnum(request.getGoodsSourceEnum());
        specialCartRequest.setOpUserId(request.getOpUserId());
        boolean flag = cartApi.addToCartBySpecialProduct(specialCartRequest);
        if (!flag) {
            return Result.failed("添加失败!");
        }
        return Result.success(tipMsg.toString());
    }

    /**
     * 构建配送商满赠商品集合
     * @param distributorEid  配送商Id
     * @param distributorName 配送商名称
     * @param cartGoodsVOList 商品结果集合
     * @return
     */
    private CartDistributorVO buildCartDistributorVo(
            Long distributorEid,
            String distributorName,
            List<CartGoodsVO> cartGoodsVOList,
            ShopDTO shopOne,
            List<PromotionAppListDTO> promotionAppList
    ) {

        CartDistributorVO cartDistributorVO = new CartDistributorVO();
        cartDistributorVO.setDistributorEid(distributorEid);
        cartDistributorVO.setDistributorName(distributorName);
        Optional optional = cartGoodsVOList.stream().filter(e -> e.getSelected()).findAny();
        if (optional.isPresent()) {
            cartDistributorVO.setSelectedGoodsNum(cartGoodsVOList.stream().filter(e -> e.getSelected()).mapToLong(CartGoodsVO::getQuantity).sum());
            cartDistributorVO.setSelectedGoodsAmount(cartGoodsVOList.stream().filter(e -> e.getSelected()).map(CartGoodsVO::getAmount).reduce(BigDecimal::add).get());
        } else {
            cartDistributorVO.setSelectedGoodsNum(0L);
            cartDistributorVO.setSelectedGoodsAmount(BigDecimal.ZERO);
        }
        cartDistributorVO.setIsMatchShopDistribute(false);
        //设置起配金额
        if (shopOne == null){
            cartDistributorVO.setStartPassAmount(BigDecimal.ZERO);
            cartDistributorVO.setPassRemainAmount(BigDecimal.ZERO);
        } else {
            cartDistributorVO.setStartPassAmount(shopOne.getStartAmount());
            cartDistributorVO.setPassRemainAmount(shopOne.getStartAmount().compareTo(cartDistributorVO.getSelectedGoodsAmount()) > 0 ? shopOne.getStartAmount().subtract(cartDistributorVO.getSelectedGoodsAmount()) : BigDecimal.ZERO);
        }

        if (CompareUtil.compare(cartDistributorVO.getPassRemainAmount(),BigDecimal.ZERO) == 0) {
            cartDistributorVO.setIsMatchShopDistribute(true);
        }

        cartDistributorVO.setCartGoodsList(cartGoodsVOList);
        cartDistributorVO.setSelectEnabled(cartGoodsVOList.stream().filter(e -> e.getSelectEnabled()).findAny().isPresent());
        cartDistributorVO.setSelected(!cartGoodsVOList.stream().filter(e -> !e.getSelected()).findAny().isPresent());
        List<CartDistributorVO.PromotionActivity> promotionList = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(promotionAppList)){
            Map<Long,List<PromotionAppListDTO>> promotionAppListMap = promotionAppList.stream().collect(Collectors.groupingBy(PromotionAppListDTO::getPromotionActivityId));
            for (Long promotionActivityId : promotionAppListMap.keySet()) {
                List<PromotionAppListDTO> promotionAppListDTOS = promotionAppListMap.get(promotionActivityId);
                PromotionAppListDTO one = promotionAppListDTOS.get(0);
                CartDistributorVO.PromotionActivity vo = new CartDistributorVO.PromotionActivity();
                vo.setActivityId(promotionActivityId);
                vo.setGiftName(one.getGiftName());
                vo.setGiftNum(1);
                vo.setPictureUrl(fileService.getUrl(one.getPictureUrl(), FileTypeEnum.MARKETING_GOODS_GIFT_PICTURE));
                vo.setGiftAmountLimit(one.getGiftAmountLimit());
                vo.setActivityGoodList(one.getGoodsIdList());
                vo.setPassRemainGifAmount(one.getDiff());
                vo.setIsMatchGift(true);
                if (CompareUtil.compare(one.getDiff(),BigDecimal.ZERO) > 0) {
                    vo.setIsMatchGift(false);
                }
                promotionList.add(vo);
            }
        }
        cartDistributorVO.setPromotionActivityList(promotionList);
        if (CollectionUtil.isEmpty(cartDistributorVO.getPromotionActivityList())) {
            cartDistributorVO.setHasGift(false);
        } else {
            cartDistributorVO.setHasGift(cartDistributorVO.getPromotionActivityList().stream().anyMatch(t -> t.getIsMatchGift() == Boolean.TRUE));
        }
        Boolean isSpecialDistributor = cartDistributorVO.getCartGoodsList().stream().anyMatch(t -> PromotionActivityTypeEnum.SPECIAL == PromotionActivityTypeEnum.getByCode(t.getPromotionActivityType())
                || PromotionActivityTypeEnum.LIMIT == PromotionActivityTypeEnum.getByCode(t.getPromotionActivityType()));
        // 表示秒杀&特价商品，排序需要排序到正常商品前面
        if (isSpecialDistributor) {
            List<CartGoodsVO> cartGoodsList = cartDistributorVO.getCartGoodsList().stream().sorted(Comparator.comparing(CartGoodsVO::getDistributorGoodsId).thenComparing(CartGoodsVO::getPromotionActivityType,Comparator.reverseOrder())).collect(Collectors.toList());
            cartDistributorVO.setCartGoodsList(cartGoodsList);
        }
        return cartDistributorVO;

    }


    /**
     * 设置特价&秒杀商品价格信息
     * @param cartGoodsVO
     * @param promotionGoodsLimitDtos
     */
    private void setSpecialCartGoods (
            CartGoodsVO cartGoodsVO,
            List<PromotionGoodsLimitDTO> promotionGoodsLimitDtos,
            Map<Long,Integer> goodsQuantityMap
    ) {
        Integer goodsQuantity = goodsQuantityMap.getOrDefault(cartGoodsVO.getDistributorGoodsId(),0);
        // 不是同一个活动，促销活动失效
        if (CollectionUtil.isEmpty(promotionGoodsLimitDtos)) {
            cartGoodsVO.setStatus(CartGoodsStatusEnum.INVALID.getCode());
            cartGoodsVO.setStatusText(CartGoodsStatusEnum.INVALID.getName());
            cartGoodsVO.setSelectEnabled(false);
            cartGoodsVO.setSelected(false);
            return;
        }
        CartGoodsVO.GoodsPromotionActivity promotionActivity = cartGoodsVO.getPromotionActivity();
        List<PromotionGoodsLimitDTO> goodsLimitDtos = promotionGoodsLimitDtos.stream().filter(t -> t.getPromotionActivityId().equals(promotionActivity.getPromotionActivityId())).collect(Collectors.toList());
        // 不是同一个活动，促销活动失效
        if (CollectionUtil.isEmpty(goodsLimitDtos)) {
            cartGoodsVO.setStatus(CartGoodsStatusEnum.INVALID.getCode());
            cartGoodsVO.setStatusText(CartGoodsStatusEnum.INVALID.getName());
            cartGoodsVO.setSelectEnabled(false);
            cartGoodsVO.setSelected(false);
            return;
        }
        PromotionGoodsLimitDTO promotionGoodsLimitDto = goodsLimitDtos.stream().findFirst().get();
        goodsQuantityMap.put(cartGoodsVO.getDistributorGoodsId(),goodsQuantity + cartGoodsVO.getQuantity());
        // 设置促销价格
        cartGoodsVO.setPrice(promotionGoodsLimitDto.getPromotionPrice());
        cartGoodsVO.setAmount(NumberUtil.round(NumberUtil.mul(cartGoodsVO.getPrice(), cartGoodsVO.getQuantity()), 2));
        promotionActivity.setPromotionActivityName(promotionGoodsLimitDto.getPromotionName());
        promotionActivity.setLeftBuyCount(promotionGoodsLimitDto.getLeftBuyCount());
        promotionActivity.setTips("限购"+ promotionGoodsLimitDto.getAllowBuyCount() + ",已购买" + (promotionGoodsLimitDto.getAllowBuyCount() - promotionGoodsLimitDto.getLeftBuyCount()));
        if (CompareUtil.compare(promotionActivity.getLeftBuyCount(),cartGoodsVO.getQuantity() + goodsQuantity + cartGoodsVO.getPackageNumber().intValue()) > 0 ) {
            promotionActivity.setIsAllowPromotion(true);
        }
        if (CompareUtil.compare(promotionActivity.getLeftBuyCount(),cartGoodsVO.getQuantity() + goodsQuantity) < 0) {
            // 库存不足失效
            cartGoodsVO.setStatus(CartGoodsStatusEnum.NO_STOCK.getCode());
            cartGoodsVO.setStatusText(CartGoodsStatusEnum.NO_STOCK.getName());
            cartGoodsVO.setStockText("无货");
            cartGoodsVO.setSelectEnabled(false);
            cartGoodsVO.setSelected(false);
        }
    }


    /**
     * 非组合商品展示
     * @param cartGoodsVO
     * @param cartInfo
     * @param skuQuantityMap
     * @param goodSkuDtoMap
     * @param goodsListResult
     */
    private void notCombinationCartInfoList(
            Long currentUseId,
            Map<Long,List<PromotionGoodsLimitDTO>> promotionGoodsLimitDTOMap,
            Map<Long, GoodsPriceDTO> goodsPriceMap,
            Map<Long, GoodsSkuStandardBasicDTO> allDistributorGoodsDTOMap,
            CartGoodsVO cartGoodsVO,
            CartDTO cartInfo,
            Map<Long,Integer> skuQuantityMap,
            Map<Long,Integer> goodsQuantityMap,
            Map<Long, GoodsSkuDTO> goodSkuDtoMap,
            Map<Long, Integer> goodsListResult,
            List<CartGoodsVO> cartGoodsVOList,
            List<Long> cancelSelectCartIds
    ) {

        // 特价&秒杀商品信息
        EnumSet<PromotionActivityTypeEnum>  promotionActivityTypeEnumEnumSet = EnumSet.of(PromotionActivityTypeEnum.SPECIAL,PromotionActivityTypeEnum.LIMIT);

        Integer skuNumber = skuQuantityMap.getOrDefault(cartInfo.getGoodsSkuId(),0);
        GoodsSkuDTO goodsSkuDTO = goodSkuDtoMap.get(cartInfo.getGoodsSkuId());
        // 商品信息
        GoodsSkuStandardBasicDTO standardGoodsBasicDTO = allDistributorGoodsDTOMap.get(cartInfo.getGoodsSkuId());
        // 拷贝商品标准库信息
        PojoUtils.map(SimpleGoodInfoUtils.toSimpleGoodsVO(standardGoodsBasicDTO.getStandardGoodsBasic()), cartGoodsVO);
        cartGoodsVO.setPackageName("");
        cartGoodsVO.setPackageShortName("");
        cartGoodsVO.setPakageInitialNum(0);
        cartGoodsVO.setCartId(cartInfo.getId());
        cartGoodsVO.setDistributorGoodsId(cartInfo.getDistributorGoodsId());
        cartGoodsVO.setBigPackage(standardGoodsBasicDTO.getPackageNumber().intValue());
        cartGoodsVO.setPackageNumber(standardGoodsBasicDTO.getPackageNumber());
        cartGoodsVO.setPrice(goodsPriceMap.get(cartInfo.getDistributorGoodsId()).getLinePrice());

        cartGoodsVO.setQuantity(cartInfo.getQuantity());
        cartGoodsVO.setSelectEnabled(true);
        cartGoodsVO.setSelected(cartInfo.getSelectedFlag() == 1);
        cartGoodsVO.setStatus(CartGoodsStatusEnum.NORMAL.getCode());
        cartGoodsVO.setStatusText(CartGoodsStatusEnum.NORMAL.getName());
        cartGoodsVO.setRemark(goodsSkuDTO != null ? goodsSkuDTO.getRemark(): "");
        cartGoodsVO.setPromotionActivityType(cartInfo.getPromotionActivityType() == null ? 0 : cartInfo.getPromotionActivityType());
        CartGoodsVO.GoodsPromotionActivity goodsPromotionActivity = new CartGoodsVO.GoodsPromotionActivity();
        goodsPromotionActivity.setLeftBuyCount(0);
        goodsPromotionActivity.setIsAllowPromotion(false);
        goodsPromotionActivity.setPromotionActivityId(cartInfo.getPromotionActivityId());
        goodsPromotionActivity.setTips("");
        goodsPromotionActivity.setPromotionActivityName("");
        cartGoodsVO.setPromotionActivity(goodsPromotionActivity);

        // 判断基本库存是否大于冻结库存
        Long stockNum = goodsSkuDTO != null && CompareUtil.compare(goodsSkuDTO.getQty(), goodsSkuDTO.getFrozenQty()) > 0 ? goodsSkuDTO.getQty() - goodsSkuDTO.getFrozenQty() - skuNumber : 0L;
        if (CompareUtil.compare(stockNum,0l) < 0) {
            stockNum = 0l;
        } else {
            Integer rate = stockNum.intValue() / goodsSkuDTO.getPackageNumber().intValue();
            stockNum = rate * goodsSkuDTO.getPackageNumber();
        }
        cartGoodsVO.setStockText(StockUtils.getStockText(stockNum));
        cartGoodsVO.setStockNum(stockNum);
        Integer goodsLimitStatus = goodsListResult.get(cartInfo.getDistributorGoodsId());

        /****优先级 失效->下架->无货****/
        if (goodsSkuDTO != null && GoodsSkuStatusEnum.DISABLE.getCode().equals(goodsSkuDTO.getStatus())){
            cartGoodsVO.setStatus(CartGoodsStatusEnum.INVALID.getCode());
            cartGoodsVO.setStatusText(CartGoodsStatusEnum.INVALID.getName());
            cartGoodsVO.setSelectEnabled(false);
            cartGoodsVO.setSelected(false);
            // 如果原来是选中状态，现在就要取消选中
            if (cartGoodsVO.getSelected()) {
                cancelSelectCartIds.add(cartGoodsVO.getCartId());
            }
        } else if ( goodsLimitStatus != null &&  (GoodsLimitStatusEnum.CONTROL_GOODS == GoodsLimitStatusEnum.getByCode(goodsLimitStatus)
                || GoodsLimitStatusEnum.INVALID_GOODS == GoodsLimitStatusEnum.getByCode(goodsLimitStatus))) {
            cartGoodsVO.setStatus(CartGoodsStatusEnum.INVALID.getCode());
            cartGoodsVO.setStatusText(CartGoodsStatusEnum.INVALID.getName());
            cartGoodsVO.setSelectEnabled(false);
            // 如果原来是选中状态，现在就要取消选中
            if (cartGoodsVO.getSelected()) {
                cancelSelectCartIds.add(cartGoodsVO.getCartId());
            }
            cartGoodsVO.setSelected(false);
        } else if (GoodsStatusEnum.getByCode(standardGoodsBasicDTO.getStandardGoodsBasic().getGoodsStatus()) != GoodsStatusEnum.UP_SHELF) {
            // 如果原来是选中状态，现在就要取消选中
            if (cartGoodsVO.getSelected()) {
                cancelSelectCartIds.add(cartGoodsVO.getCartId());
            }
            cartGoodsVO.setSelectEnabled(false);
            cartGoodsVO.setSelected(false);
            cartGoodsVO.setStatus(CartGoodsStatusEnum.UN_SHELF.getCode());
            cartGoodsVO.setStatusText(CartGoodsStatusEnum.UN_SHELF.getName());
        } else if (stockNum == 0L) {
            // 如果原来是选中状态，现在就要取消选中
            if (cartGoodsVO.getSelected()) {
                cancelSelectCartIds.add(cartGoodsVO.getCartId());
            }
            cartGoodsVO.setSelectEnabled(false);
            cartGoodsVO.setSelected(false);
            cartGoodsVO.setStatus(CartGoodsStatusEnum.NO_STOCK.getCode());
            cartGoodsVO.setStatusText(CartGoodsStatusEnum.NO_STOCK.getName());
        } else if (promotionActivityTypeEnumEnumSet.contains(PromotionActivityTypeEnum.getByCode(cartGoodsVO.getPromotionActivityType()))){
            // 设置特价商品信息
            boolean isSelected =  cartGoodsVO.getSelected();
            this.setSpecialCartGoods(cartGoodsVO,promotionGoodsLimitDTOMap.get(cartGoodsVO.getDistributorGoodsId()),goodsQuantityMap);
            // 表示之前选中，经过特价后，取消选中
            if (!cartGoodsVO.getSelected() && isSelected) {
                cancelSelectCartIds.add(cartGoodsVO.getCartId());
            }
        } else if (stockNum < cartGoodsVO.getQuantity() &&  !(promotionActivityTypeEnumEnumSet.contains(PromotionActivityTypeEnum.getByCode(cartGoodsVO.getPromotionActivityType())))) {
            // 当库存数量大于0，且小于购买数量时，修改购物车商品数量为库存数量
            cartGoodsVO.setQuantity(stockNum.intValue());
            if (GoodsStatusEnum.getByCode(standardGoodsBasicDTO.getStandardGoodsBasic().getGoodsStatus()) == GoodsStatusEnum.UP_SHELF) {
                UpdateCartGoodsQuantityRequest updateCartGoodsQuantityRequest = new UpdateCartGoodsQuantityRequest();
                updateCartGoodsQuantityRequest.setId(cartGoodsVO.getCartId());
                updateCartGoodsQuantityRequest.setQuantity(cartGoodsVO.getQuantity());
                updateCartGoodsQuantityRequest.setOpUserId(currentUseId);
                cartApi.updateCartGoodsQuantity(updateCartGoodsQuantityRequest);
            }
        }
        cartGoodsVO.setAmount(NumberUtil.round(NumberUtil.mul(cartGoodsVO.getPrice(), cartGoodsVO.getQuantity()), 2));
        cartGoodsVOList.add(cartGoodsVO);

        if (cartGoodsVO.getSelected()) {
            skuQuantityMap.put(cartInfo.getGoodsSkuId(),skuNumber + cartGoodsVO.getQuantity());
        }


    }


    /**
     *  组合商品信息展示
     * @param cartGoodsVO
     * @param cartInfo
     * @param skuQuantityMap
     */
    private void combinationCartInfoList(PromotionDTO promotionDTO,CartGoodsVO cartGoodsVO,CartDTO cartInfo,Map<Long,Integer> skuQuantityMap,  Map<Long, GoodsSkuDTO> goodSkuDtoMap,Map<Long, Integer> goodsListResult) {
        cartGoodsVO.setCartId(cartInfo.getId());
        cartGoodsVO.setBigPackage(1);
        cartGoodsVO.setPackageNumber(1l);
        cartGoodsVO.setPrice(BigDecimal.ZERO);
        cartGoodsVO.setQuantity(cartInfo.getQuantity());
        cartGoodsVO.setStockNum(0l);
        cartGoodsVO.setDistributorGoodsId(0l);
        cartGoodsVO.setOverSoldType(0);
        cartGoodsVO.setSelectEnabled(true);
        cartGoodsVO.setSelected(cartInfo.getSelectedFlag() == 1);
        cartGoodsVO.setStatus(CartGoodsStatusEnum.NORMAL.getCode());
        cartGoodsVO.setStatusText(CartGoodsStatusEnum.NORMAL.getName());
        cartGoodsVO.setRemark("");
        cartGoodsVO.setPromotionActivityType(cartInfo.getPromotionActivityType() == null ? 0 : cartInfo.getPromotionActivityType());
        CartGoodsVO.GoodsPromotionActivity goodsPromotionActivity = new CartGoodsVO.GoodsPromotionActivity();
        goodsPromotionActivity.setLeftBuyCount(0);
        goodsPromotionActivity.setIsAllowPromotion(true);
        goodsPromotionActivity.setPromotionActivityId(cartInfo.getPromotionActivityId());
        goodsPromotionActivity.setTips("");
        goodsPromotionActivity.setPromotionActivityName("");
        cartGoodsVO.setPromotionActivity(goodsPromotionActivity);
        cartGoodsVO.setAmount(NumberUtil.round(NumberUtil.mul(cartGoodsVO.getPrice(), cartGoodsVO.getQuantity()), 2));
        if (promotionDTO == null ) {
            cartGoodsVO.setStatus(CartGoodsStatusEnum.INVALID.getCode());
            cartGoodsVO.setStatusText(CartGoodsStatusEnum.INVALID.getName());
            cartGoodsVO.setSelectEnabled(false);
            cartGoodsVO.setSelected(false);
            return;
        }
        cartGoodsVO.setPictureUrl(promotionDTO.getPromotionCombinationPackDTO().getPic());
        cartGoodsVO.setPackageName(promotionDTO.getPromotionCombinationPackDTO().getPackageName());
        cartGoodsVO.setPackageShortName(promotionDTO.getPromotionCombinationPackDTO().getPackageShortName());
        cartGoodsVO.setPakageInitialNum(promotionDTO.getPromotionCombinationPackDTO().getInitialNum());
        cartGoodsVO.setName(promotionDTO.getPromotionCombinationPackDTO().getPackageName());
        goodsPromotionActivity.setPromotionActivityName(promotionDTO.getPromotionActivityDTO().getName());
        List<PromotionGoodsLimitDTO> promotionGoodsLimitDTOList = promotionDTO.getGoodsLimitDTOList();
        BigDecimal packageTotalPrice = BigDecimal.ZERO;
        for (PromotionGoodsLimitDTO promotionGoodsLimitDTO : promotionGoodsLimitDTOList) {
            packageTotalPrice = NumberUtil.add(packageTotalPrice,NumberUtil.mul(promotionGoodsLimitDTO.getAllowBuyCount(),promotionGoodsLimitDTO.getPromotionPrice()));
            GoodsSkuDTO  goodsSkuDTO = goodSkuDtoMap.get(promotionGoodsLimitDTO.getGoodsSkuId());
            Integer skuNumber = skuQuantityMap.getOrDefault(cartInfo.getGoodsSkuId(),0);
            Long stockNum = goodsSkuDTO != null && CompareUtil.compare(goodsSkuDTO.getQty(), goodsSkuDTO.getFrozenQty()) > 0 ? goodsSkuDTO.getQty() - goodsSkuDTO.getFrozenQty() - skuNumber : 0L;
            if (CompareUtil.compare(stockNum,0l) < 0) {
                stockNum = 0l;
            } else {
                Integer rate = stockNum.intValue() / promotionGoodsLimitDTO.getAllowBuyCount().intValue();
                stockNum = rate * promotionGoodsLimitDTO.getAllowBuyCount().longValue();
            }
            // 判断最小购买库存
            if (CompareUtil.compare(0l,cartGoodsVO.getStockNum()) == 0 || CompareUtil.compare(stockNum,cartGoodsVO.getStockNum()) < 0) {
                cartGoodsVO.setStockText(StockUtils.getStockText(stockNum));
                cartGoodsVO.setStockNum(stockNum);
            }
            /****优先级 失效->下架->无货****/
            Integer goodsLimitStatus = goodsListResult.get(cartInfo.getDistributorGoodsId());
            if (goodsSkuDTO != null && (!GoodsSkuStatusEnum.NORMAL.getCode().equals(goodsSkuDTO.getStatus()) || promotionDTO.getAvailable() != 1)){
                cartGoodsVO.setStatus(CartGoodsStatusEnum.INVALID.getCode());
                cartGoodsVO.setStatusText(CartGoodsStatusEnum.INVALID.getName());
                cartGoodsVO.setSelectEnabled(false);
                cartGoodsVO.setSelected(false);
            } else if ( goodsLimitStatus != null &&  (GoodsLimitStatusEnum.CONTROL_GOODS == GoodsLimitStatusEnum.getByCode(goodsLimitStatus)
                    || GoodsLimitStatusEnum.INVALID_GOODS == GoodsLimitStatusEnum.getByCode(goodsLimitStatus))) {
                cartGoodsVO.setStatus(CartGoodsStatusEnum.INVALID.getCode());
                cartGoodsVO.setStatusText(CartGoodsStatusEnum.INVALID.getName());
                cartGoodsVO.setSelectEnabled(false);
                cartGoodsVO.setSelected(false);
            } else if ((CartGoodsStatusEnum.NORMAL.getCode().equals(goodsSkuDTO.getStatus()) || CartGoodsStatusEnum.UN_SHELF.getCode().equals(goodsSkuDTO.getStatus())) && goodsLimitStatus != null && GoodsLimitStatusEnum.UN_SHELF == GoodsLimitStatusEnum.getByCode(goodsLimitStatus)) {
                cartGoodsVO.setSelectEnabled(false);
                cartGoodsVO.setSelected(false);
                cartGoodsVO.setStatus(CartGoodsStatusEnum.UN_SHELF.getCode());
                cartGoodsVO.setStatusText(CartGoodsStatusEnum.UN_SHELF.getName());
            } else if (CartGoodsStatusEnum.NORMAL.getCode().equals(goodsSkuDTO.getStatus()) && stockNum == 0L) {
                cartGoodsVO.setSelectEnabled(false);
                cartGoodsVO.setSelected(false);
                cartGoodsVO.setStatus(CartGoodsStatusEnum.NO_STOCK.getCode());
                cartGoodsVO.setStatusText(CartGoodsStatusEnum.NO_STOCK.getName());
            }
        }
        cartGoodsVO.setPrice(packageTotalPrice);
        cartGoodsVO.setAmount(NumberUtil.round(NumberUtil.mul(cartGoodsVO.getPrice(), cartGoodsVO.getQuantity()), 2));
        goodsPromotionActivity.setLeftBuyCount(cartGoodsVO.getStockNum().intValue());
        cartGoodsVO.setPromotionActivity(goodsPromotionActivity);
    }



    /**
     *  获取促销赠品信息
     * @param cartGoodsVOList
     * @return
     */
    private Map<Long, List<PromotionAppListDTO>> getGoodPromotionGifts(List<CartGoodsVO> cartGoodsVOList) {

        /***************************目前产品定义只有正常商品才可以参与赠品活动******************************/
        EnumSet<PromotionActivityTypeEnum> promotionActivityTypeEnumEnumSet = EnumSet.of(PromotionActivityTypeEnum.NORMAL);

        // 取出正常品,按照商品Id合并不同sku的商品金额
        Map<Long,BigDecimal> goodsAmountMap = cartGoodsVOList.stream().filter(t -> t.getSelected() && promotionActivityTypeEnumEnumSet.contains(PromotionActivityTypeEnum.getByCode(t.getPromotionActivityType()))
        ).collect(Collectors.groupingBy(CartGoodsVO::getDistributorGoodsId, Collectors.reducing(BigDecimal.ZERO,CartGoodsVO::getAmount,BigDecimal::add)));

        // 查询赠品活动,取正常赠品活动
        List<Long> distributorGoodsIds = cartGoodsVOList.stream().filter(z -> promotionActivityTypeEnumEnumSet.contains(PromotionActivityTypeEnum.getByCode(z.getPromotionActivityType()))).map(t -> t.getDistributorGoodsId()).distinct().collect(Collectors.toList());
        if (CollectionUtil.isEmpty(distributorGoodsIds)) {
            return MapUtil.empty();
        }

        // 创建获取请求赠品信息的请求request
        List<PromotionAppCartGoods> requestList =  distributorGoodsIds.stream().map(distributorGoodsId -> {
            PromotionAppCartGoods cartGood = new PromotionAppCartGoods();
            cartGood.setGoodsId(distributorGoodsId);
            cartGood.setAmount(goodsAmountMap.getOrDefault(distributorGoodsId,BigDecimal.ZERO));
            return cartGood;
        }).collect(Collectors.toList());

        PromotionAppCartRequest cartRequest = new PromotionAppCartRequest();
        cartRequest.setPlatform(PlatformEnum.B2B.getCode());
        cartRequest.setCartGoodsList(requestList);
        List<PromotionAppListDTO> promotionAppListList = promotionActivityApi.queryAppCartPromotion(cartRequest);

        if (log.isDebugEnabled()) {
            log.debug("queryAppGoodsListPromotion...request ->{}...result ->{}",JSON.toJSON(cartRequest),JSON.toJSON(promotionAppListList));
        }

        if (CollectionUtil.isEmpty(promotionAppListList)) {

            return MapUtil.empty();
        }
        return promotionAppListList.stream().collect(Collectors.groupingBy(PromotionAppListDTO::getEid));
    }


    @ApiOperation(value = "获取购物车列表")
    @PostMapping("/list")
    @UserAccessAuthentication
    public Result<CartListVO> list(@CurrentUser CurrentStaffInfo staffInfo) {
        List<CartDTO> cartDTOList = cartApi.listByBuyerEid(staffInfo.getCurrentEid(),PlatformEnum.B2B, CartGoodsSourceEnum.B2B, CartIncludeEnum.ALL);

        if (CollUtil.isEmpty(cartDTOList)) {
            return Result.success(CartListVO.empty());
        }

        // 配送商字典
        List<Long> distributorEids = cartDTOList.stream().map(CartDTO::getDistributorEid).distinct().collect(Collectors.toList());
        List<EnterpriseDTO> distributorDTOList = enterpriseApi.listByIds(distributorEids);
        Map<Long, EnterpriseDTO> distributorDTOMap = distributorDTOList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, Function.identity()));
        // 组合促销活动id信息
        List<Long> comActivityIds = cartDTOList.stream().filter(t -> PromotionActivityTypeEnum.COMBINATION == PromotionActivityTypeEnum.getByCode(t.getPromotionActivityType())).map(CartDTO::getPromotionActivityId).distinct().collect(Collectors.toList());
        // 商品Sku信息
        List<Long> normalGoodsSkuIds = cartDTOList.stream().filter(t -> PromotionActivityTypeEnum.COMBINATION != PromotionActivityTypeEnum.getByCode(t.getPromotionActivityType())).map(CartDTO::getGoodsSkuId).distinct().collect(Collectors.toList());
        // 组合商品skuId信息
        List<Long> comGoodsSkuIds = Lists.newArrayList();
        Map<Long,PromotionDTO> comPromotionMap = Maps.newHashMap();
        // 促销活动信息
        if (CollectionUtil.isNotEmpty(comActivityIds)) {
            PromotionActivityRequest promotionActivityRequest = new PromotionActivityRequest();
            promotionActivityRequest.setBuyerEid(staffInfo.getCurrentEid());
            promotionActivityRequest.setGoodsPromotionActivityIdList(ListUtil.toList(comActivityIds));
            List<PromotionDTO>  promotionDTOS = promotionActivityApi.queryPromotionInfoByActivityAndBuyerId(promotionActivityRequest);
            if (CollectionUtil.isNotEmpty(promotionDTOS)) {
                comPromotionMap =  promotionDTOS.stream().collect(Collectors.toMap(t -> t.getPromotionCombinationPackDTO().getPromotionActivityId(),Function.identity()));
                promotionDTOS.forEach(t -> comGoodsSkuIds.addAll(t.getGoodsLimitDTOList().stream().map(z -> z.getGoodsSkuId()).distinct().collect(Collectors.toList())));
            }

        }
        List<Long> allGoodsSkuIds  = Stream.of(comGoodsSkuIds, normalGoodsSkuIds).flatMap(Collection::stream).distinct().collect(Collectors.toList());
        List<GoodsSkuStandardBasicDTO> allSkuGoodsList = goodsApi.batchQueryStandardGoodsBasicBySkuIds(allGoodsSkuIds);

        if (allSkuGoodsList.size() != allGoodsSkuIds.size()) {
            log.error("{}:商品信息查询不全!",allGoodsSkuIds);
        }
        // 商品Id信息
        List<Long> allGoodsIds = allSkuGoodsList.stream().map(t -> t.getGoodsId()).collect(Collectors.toList());
        Map<Long, GoodsSkuStandardBasicDTO> allDistributorGoodsDTOMap =  allSkuGoodsList.stream().collect(Collectors.toMap(GoodsSkuStandardBasicDTO::getId, Function.identity()));

        cartDTOList.forEach(cartDTO -> {
            Long distributorGoodsId = Optional.ofNullable(allDistributorGoodsDTOMap.get(cartDTO.getGoodsSkuId())).map(t -> t.getGoodsId()).orElse(cartDTO.getDistributorGoodsId());
            cartDTO.setDistributorGoodsId(distributorGoodsId);
        });

        Map<Long, GoodsSkuDTO> goodSkuDtoMap = goodsApi.getGoodsSkuByIds(allGoodsSkuIds).stream().collect(Collectors.toMap(GoodsSkuDTO::getId,Function.identity()));
        // 获取商品价格信息
        QueryGoodsPriceRequest request = new QueryGoodsPriceRequest();
        request.setCustomerEid(staffInfo.getCurrentEid());
        request.setGoodsIds(allGoodsIds);
        Map<Long, GoodsPriceDTO> goodsPriceMap = goodsPriceApi.queryB2bGoodsPriceInfoMap(request);
        Map<Long, Integer> goodsListResult = agreementBusinessApi.getB2bGoodsLimitByGids(allGoodsIds, staffInfo.getCurrentEid());
        //满配信息
        List<ShopDTO> shopList = shopApi.listShopByEidList(distributorEids);
        Map<Long, ShopDTO> shopMap = shopList.stream().collect(Collectors.toMap(ShopDTO::getShopEid, o -> o, (k1, k2) -> k1));
        List<CartDistributorVO> cartDistributorVOList = CollUtil.newArrayList();
        Map<Long, List<CartDTO>> distributorCartDTOMap = cartDTOList.stream().collect(Collectors.groupingBy(CartDTO::getDistributorEid));
        // 特价&秒杀商品信息
        EnumSet<PromotionActivityTypeEnum>  promotionActivityTypeEnumEnumSet = EnumSet.of(PromotionActivityTypeEnum.SPECIAL,PromotionActivityTypeEnum.LIMIT);
        List<CartDTO> specialCartList = cartDTOList.stream().filter(z -> promotionActivityTypeEnumEnumSet.contains(PromotionActivityTypeEnum.getByCode(z.getPromotionActivityType()))).collect(Collectors.toList());
        // 特价&秒杀商品信息
        Map<Long,List<PromotionGoodsLimitDTO>> specialGoodsPromotionMap = CollectionUtil.isEmpty(specialCartList) ? MapUtil.newHashMap() : Optional.ofNullable(specialCartList).map(s -> {
            List<Long> goodsIdList = s.stream().map(t -> t.getDistributorGoodsId()).distinct().collect(Collectors.toList());
            // 查询商品特价活动信息
            return this.selectSpecialGoodsActivity(PlatformEnum.B2B,staffInfo.getCurrentEid(),goodsIdList).stream().collect(Collectors.groupingBy(PromotionGoodsLimitDTO::getEid));

        }).orElse(MapUtil.newHashMap());

        // 需要取消选中的购物车ID集合
        List<Long> cancelSelectCartIds = CollUtil.newArrayList();
        Map<Long,List<CartGoodsVO>> cartDistributorMap = Maps.newHashMap();
        List<CartGoodsVO> allCartGoodsVOList = Lists.newArrayList();
        for (Long distributorEid : distributorCartDTOMap.keySet()) {
            List<CartDTO> distributorCartDTOList = distributorCartDTOMap.get(distributorEid);
            List<CartGoodsVO> cartGoodsVOList = CollUtil.newArrayList();
            // 秒杀&特价促销价格集合
            Map<Long,List<PromotionGoodsLimitDTO>> promotionGoodsLimitDTOMap = specialGoodsPromotionMap.getOrDefault(distributorEid,new ArrayList<>()).stream().collect(Collectors.groupingBy(PromotionGoodsLimitDTO::getGoodsId));
            distributorCartDTOList = distributorCartDTOList.stream().sorted(Comparator.comparing(CartDTO::getDistributorGoodsId).thenComparing(CartDTO::getPromotionActivityType,Comparator.reverseOrder())).collect(Collectors.toList());
            // 商品数量Map
            Map<Long,Integer> goodsQuantityMap = Maps.newHashMap();
            Map<Long,Integer> skuQuantityMap = Maps.newHashMap();
            for (CartDTO cartInfo : distributorCartDTOList) {
                CartGoodsVO cartGoodsVO = new CartGoodsVO();
                // 组合商品信息
                if (PromotionActivityTypeEnum.COMBINATION == PromotionActivityTypeEnum.getByCode(cartInfo.getPromotionActivityType())) {
                    boolean isSelected =  cartInfo.getSelectedFlag() == 1;
                    // 设置组合商品信息
                    this.combinationCartInfoList(comPromotionMap.get(cartInfo.getPromotionActivityId()),cartGoodsVO,cartInfo,skuQuantityMap,goodSkuDtoMap,goodsListResult);
                    // 表示之前选中，经过特价后，取消选中
                    if (!cartGoodsVO.getSelected() && isSelected) {
                        cancelSelectCartIds.add(cartGoodsVO.getCartId());
                    }
                    cartGoodsVOList.add(cartGoodsVO);
                    continue;
                }
                Integer skuNumber = skuQuantityMap.getOrDefault(cartInfo.getGoodsSkuId(),0);
                GoodsSkuDTO goodsSkuDTO = goodSkuDtoMap.get(cartInfo.getGoodsSkuId());
                // 商品信息
                GoodsSkuStandardBasicDTO standardGoodsBasicDTO = allDistributorGoodsDTOMap.get(cartInfo.getGoodsSkuId());
                // 拷贝商品标准库信息
                PojoUtils.map(SimpleGoodInfoUtils.toSimpleGoodsVO(standardGoodsBasicDTO.getStandardGoodsBasic()), cartGoodsVO);
                cartGoodsVO.setPackageName("");
                cartGoodsVO.setPackageShortName("");
                cartGoodsVO.setPakageInitialNum(0);
                cartGoodsVO.setCartId(cartInfo.getId());
                cartGoodsVO.setDistributorGoodsId(cartInfo.getDistributorGoodsId());
                cartGoodsVO.setBigPackage(standardGoodsBasicDTO.getPackageNumber().intValue());
                cartGoodsVO.setPackageNumber(standardGoodsBasicDTO.getPackageNumber());
                cartGoodsVO.setPrice(goodsPriceMap.get(cartInfo.getDistributorGoodsId()).getLinePrice());

                cartGoodsVO.setQuantity(cartInfo.getQuantity());
                cartGoodsVO.setSelectEnabled(true);
                cartGoodsVO.setSelected(cartInfo.getSelectedFlag() == 1);
                cartGoodsVO.setStatus(CartGoodsStatusEnum.NORMAL.getCode());
                cartGoodsVO.setStatusText(CartGoodsStatusEnum.NORMAL.getName());
                cartGoodsVO.setRemark(goodsSkuDTO != null ? goodsSkuDTO.getRemark(): "");
                cartGoodsVO.setPromotionActivityType(cartInfo.getPromotionActivityType() == null ? 0 : cartInfo.getPromotionActivityType());
                CartGoodsVO.GoodsPromotionActivity goodsPromotionActivity = new CartGoodsVO.GoodsPromotionActivity();
                goodsPromotionActivity.setLeftBuyCount(0);
                goodsPromotionActivity.setIsAllowPromotion(false);
                goodsPromotionActivity.setPromotionActivityId(cartInfo.getPromotionActivityId());
                goodsPromotionActivity.setTips("");
                goodsPromotionActivity.setPromotionActivityName("");
                cartGoodsVO.setPromotionActivity(goodsPromotionActivity);

                Long stockNum = goodsSkuDTO != null && CompareUtil.compare(goodsSkuDTO.getQty(), goodsSkuDTO.getFrozenQty()) > 0 ? goodsSkuDTO.getQty() - goodsSkuDTO.getFrozenQty() - skuNumber : 0L;
                if (CompareUtil.compare(stockNum,0l) < 0) {
                    stockNum = 0l;
                } else {
                    Long rate = stockNum / goodsSkuDTO.getPackageNumber();
                    stockNum = rate * goodsSkuDTO.getPackageNumber();
                }
                cartGoodsVO.setStockText(StockUtils.getStockText(stockNum));
                cartGoodsVO.setStockNum(stockNum);
                Integer goodsLimitStatus = goodsListResult.get(cartInfo.getDistributorGoodsId());
                if (goodsSkuDTO != null && !GoodsSkuStatusEnum.NORMAL.getCode().equals(goodsSkuDTO.getStatus())){
                    cartGoodsVO.setStatus(CartGoodsStatusEnum.INVALID.getCode());
                    cartGoodsVO.setStatusText(CartGoodsStatusEnum.INVALID.getName());
                    // 如果原来是选中状态，现在就要取消选中
                    if (cartGoodsVO.getSelected()) {
                        cancelSelectCartIds.add(cartGoodsVO.getCartId());
                    }
                    cartGoodsVO.setSelectEnabled(false);
                    cartGoodsVO.setSelected(false);
                } else if ( goodsLimitStatus != null &&  (GoodsLimitStatusEnum.CONTROL_GOODS == GoodsLimitStatusEnum.getByCode(goodsLimitStatus)
                        || GoodsLimitStatusEnum.INVALID_GOODS == GoodsLimitStatusEnum.getByCode(goodsLimitStatus))) {
                    cartGoodsVO.setStatus(CartGoodsStatusEnum.INVALID.getCode());
                    cartGoodsVO.setStatusText(CartGoodsStatusEnum.INVALID.getName());
                    cartGoodsVO.setSelectEnabled(false);
                    // 如果原来是选中状态，现在就要取消选中
                    if (cartGoodsVO.getSelected()) {
                        cancelSelectCartIds.add(cartGoodsVO.getCartId());
                    }
                    cartGoodsVO.setSelected(false);
                } else if (GoodsStatusEnum.getByCode(standardGoodsBasicDTO.getStandardGoodsBasic().getGoodsStatus()) != GoodsStatusEnum.UP_SHELF) {
                    // 如果原来是选中状态，现在就要取消选中
                    if (cartGoodsVO.getSelected()) {
                        cancelSelectCartIds.add(cartGoodsVO.getCartId());
                    }
                    cartGoodsVO.setSelectEnabled(false);
                    cartGoodsVO.setSelected(false);
                    cartGoodsVO.setStatus(CartGoodsStatusEnum.UN_SHELF.getCode());
                    cartGoodsVO.setStatusText(CartGoodsStatusEnum.UN_SHELF.getName());
                } else if (stockNum == 0L) {
                    // 如果原来是选中状态，现在就要取消选中
                    if (cartGoodsVO.getSelected()) {
                        cancelSelectCartIds.add(cartGoodsVO.getCartId());
                    }
                    cartGoodsVO.setSelectEnabled(false);
                    cartGoodsVO.setSelected(false);
                    cartGoodsVO.setStatus(CartGoodsStatusEnum.NO_STOCK.getCode());
                    cartGoodsVO.setStatusText(CartGoodsStatusEnum.NO_STOCK.getName());
                } else if (promotionActivityTypeEnumEnumSet.contains(PromotionActivityTypeEnum.getByCode(cartGoodsVO.getPromotionActivityType()))){
                    // 设置特价商品信息
                    boolean isSelected =  cartGoodsVO.getSelected();
                    this.setSpecialCartGoods(cartGoodsVO,promotionGoodsLimitDTOMap.get(cartGoodsVO.getDistributorGoodsId()),goodsQuantityMap);
                    // 表示之前选中，经过特价后，取消选中
                    if (!cartGoodsVO.getSelected() && isSelected) {
                        cancelSelectCartIds.add(cartGoodsVO.getCartId());
                    }
                } else if (stockNum < cartGoodsVO.getQuantity() &&  !(promotionActivityTypeEnumEnumSet.contains(PromotionActivityTypeEnum.getByCode(cartGoodsVO.getPromotionActivityType())))) {
                    // 当库存数量大于0，且小于购买数量时，修改购物车商品数量为库存数量
                    cartGoodsVO.setQuantity(stockNum.intValue());
                    if (GoodsStatusEnum.getByCode(standardGoodsBasicDTO.getStandardGoodsBasic().getGoodsStatus()) == GoodsStatusEnum.UP_SHELF) {
                        UpdateCartGoodsQuantityRequest updateCartGoodsQuantityRequest = new UpdateCartGoodsQuantityRequest();
                        updateCartGoodsQuantityRequest.setId(cartGoodsVO.getCartId());
                        updateCartGoodsQuantityRequest.setQuantity(cartGoodsVO.getQuantity());
                        updateCartGoodsQuantityRequest.setOpUserId(staffInfo.getCurrentUserId());
                        cartApi.modifyCartGoodsQuantity(updateCartGoodsQuantityRequest);
                    }
                }
                cartGoodsVO.setAmount(NumberUtil.round(NumberUtil.mul(cartGoodsVO.getPrice(), cartGoodsVO.getQuantity()), 2));
                cartGoodsVOList.add(cartGoodsVO);
                if (cartGoodsVO.getSelected()) {
                    skuQuantityMap.put(cartInfo.getGoodsSkuId(),skuNumber + cartGoodsVO.getQuantity());
                }
            }
            allCartGoodsVOList.addAll(cartGoodsVOList);
            cartDistributorMap.put(distributorEid,cartGoodsVOList);
        }

        // 获取促销赠品信息
        Map<Long, List<PromotionAppListDTO>> promotionMap = this.getGoodPromotionGifts(allCartGoodsVOList);
        for (Long distributorEid : cartDistributorMap.keySet()) {
            //满配金额
            ShopDTO shopOne = shopMap.get(distributorEid);
            //赠品活动信息
            List<PromotionAppListDTO> promotionAppList = promotionMap.get(distributorEid);
            // 构建每个店铺的商品信息
            CartDistributorVO cartDistributorVO = this.buildCartDistributorVo(distributorEid, distributorDTOMap.get(distributorEid).getName(), cartDistributorMap.get(distributorEid),shopOne,promotionAppList);
            cartDistributorVOList.add(cartDistributorVO);
        }

        CartListVO cartListVO = new CartListVO();
        cartListVO.setCartDistributorList(cartDistributorVOList);
        cartListVO.setSelectedGoodsNum(cartDistributorVOList.stream().mapToLong(CartDistributorVO::getSelectedGoodsNum).sum());
        cartListVO.setSelectedGoodsAmount(cartDistributorVOList.stream().map(CartDistributorVO::getSelectedGoodsAmount).reduce(BigDecimal::add).get());
        cartListVO.setSelectEnabled(cartDistributorVOList.stream().filter(e -> e.getSelectEnabled()).findAny().isPresent());
        cartListVO.setSelected(!cartDistributorVOList.stream().filter(e -> !e.getSelected()).findAny().isPresent());
        cartListVO.setCartGoodsNum(Long.valueOf(cartDTOList.size()));

        if (CollUtil.isEmpty(cancelSelectCartIds)) {

            return Result.success(cartListVO);
        }

        SelectCartGoodsRequest selectCartGoodsRequest = new SelectCartGoodsRequest();
        selectCartGoodsRequest.setIds(cancelSelectCartIds);
        selectCartGoodsRequest.setSelected(false);
        selectCartGoodsRequest.setOpUserId(staffInfo.getCurrentUserId());
        // 去除未选中的商品信息
        cartApi.selectCartGoods(selectCartGoodsRequest);

        return Result.success(cartListVO);
    }


    /**
     *  获取组合促销活动信息
     * @param staffInfo 登录人信息
     * @param comActivityIds 组合包活动Id
     * @param comGoodsSkuIds 返回参与组合促销活动的skuId
     * @param comGoodsIds  返回参与组合促销的商品Id
     * @return
     */
    private Map<Long, PromotionDTO> getCombinationPromotionDTOMap(@CurrentUser CurrentStaffInfo staffInfo, List<Long> comActivityIds, List<Long> comGoodsSkuIds, List<Long> comGoodsIds) {

        if (CollectionUtil.isEmpty(comActivityIds)) {
            return MapUtil.empty();
        }

        PromotionActivityRequest promotionActivityRequest = new PromotionActivityRequest();
        promotionActivityRequest.setBuyerEid(staffInfo.getCurrentEid());
        promotionActivityRequest.setGoodsPromotionActivityIdList(ListUtil.toList(comActivityIds));
        List<PromotionDTO>  promotionDTOS = promotionActivityApi.queryPromotionInfoByActivityAndBuyerId(promotionActivityRequest);

        if (log.isDebugEnabled()) {

            log.debug("调用营销接口查询组合促销活动信息,入参:[{}],返回参数:[{}]",promotionActivityRequest,promotionDTOS);
        }

        if (CollectionUtil.isEmpty(promotionDTOS)) {
            return MapUtil.empty();
        }

        Map<Long, PromotionDTO> comPromotionMap  =  promotionDTOS.stream().collect(Collectors.toMap(t -> t.getPromotionCombinationPackDTO().getPromotionActivityId(), Function.identity()));

        promotionDTOS.forEach(t -> {
            comGoodsSkuIds.addAll(t.getGoodsLimitDTOList().stream().map(z -> z.getGoodsSkuId()).distinct().collect(Collectors.toList()));
            comGoodsIds.addAll(t.getGoodsLimitDTOList().stream().map(z -> z.getGoodsId()).distinct().collect(Collectors.toList()));
        });

        return comPromotionMap;
    }

    @ApiOperation(value = "移除购物车商品")
    @PostMapping("/remove")
    @UserAccessAuthentication(checkEnterpriseAuthStatus = true, checkEnterpriseStatus = true, checkUserEnterpriseRelation = true, checkUserStatus = true)
    public Result<CartListVO> remove(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid RemoveCartGoodsForm form) {
        RemoveCartGoodsRequest request = PojoUtils.map(form, RemoveCartGoodsRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());

        cartApi.removeCartGoods(request);

        return this.list(staffInfo);
    }

    @ApiOperation(value = "修改购物车商品数量")
    @PostMapping("/updateQuantity")
    @ApiResponses({
            @ApiResponse(code = 10356, message = "限购XXX件，已达限购数!"),
            @ApiResponse(code = 10357, message = "每天限购XXX件，已达限购数!"),
            @ApiResponse(code = 10358, message = "每周限购xx件，已达限购数!"),
            @ApiResponse(code = 10359, message = "每月限购xx件，已达限购数!"),
            @ApiResponse(code = 10360, message = "xx天内限购xx件，已达限购数!"),
            @ApiResponse(code = 10363, message = "组合包XX限购XX包,已达限购数"),
            @ApiResponse(code = 10365, message = "组合包XXX数量不足!")
    })
    @UserAccessAuthentication(checkEnterpriseAuthStatus = true, checkEnterpriseStatus = true, checkUserEnterpriseRelation = true, checkUserStatus = true)
    public Result<CartListVO> updateQuantity(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid UpdateCartGoodsQuantityForm form) {
        UpdateCartGoodsQuantityRequest request = PojoUtils.map(form, UpdateCartGoodsQuantityRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());

        cartApi.updateCartGoodsQuantity(request);
        return this.list(staffInfo);
    }

    @ApiOperation(value = "勾选购物车商品")
    @PostMapping("/select")
    @UserAccessAuthentication(checkEnterpriseAuthStatus = true, checkEnterpriseStatus = true, checkUserEnterpriseRelation = true, checkUserStatus = true)
    public Result<CartListVO> select(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid SelectCartGoodsForm form) {
        SelectCartGoodsRequest request = PojoUtils.map(form, SelectCartGoodsRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());

        cartApi.selectCartGoods(request);
        return this.list(staffInfo);
    }

    /**
     * 通过配送商ID,查询已选中商品的
     *
     * @param staffInfo
     * @param distributorEid
     * @return
     */
    @ApiOperation(value = "卖家商品详情")
    @GetMapping("/distributor/goods")
    @UserAccessAuthentication
    public Result<CollectionObject<CartGoodsVO>> listDistributorGoods(@CurrentUser CurrentStaffInfo staffInfo, @Param("distributorEid") Long distributorEid) {

        List<CartDTO> cartDTOList = cartApi.listByDistributorEid(distributorEid, PlatformEnum.B2B, CartGoodsSourceEnum.B2B, CartIncludeEnum.SELECTED);

        if (CollUtil.isEmpty(cartDTOList)) {

            return Result.success(new CollectionObject(Collections.emptyList()));
        }
        // 过滤当前人卖家详情数据
        cartDTOList = cartDTOList.stream().filter(t -> staffInfo.getCurrentEid().equals(t.getBuyerEid())).collect(Collectors.toList());

        List<Long> allGoodsSkuIds = cartDTOList.stream().map(CartDTO::getGoodsSkuId).distinct().collect(Collectors.toList());
        List<GoodsSkuStandardBasicDTO> allSkuGoodsList = goodsApi.batchQueryStandardGoodsBasicBySkuIds(allGoodsSkuIds);

        if (allSkuGoodsList.size() != allGoodsSkuIds.size()) {
            log.error("{}:商品信息查询不全!",allGoodsSkuIds);
        }

        Map<Long, GoodsSkuStandardBasicDTO> allSkuGoodsDTOMap = allSkuGoodsList.stream().collect(Collectors.toMap(GoodsSkuStandardBasicDTO::getId, Function.identity()));
        List<Long> goodsIds = allSkuGoodsList.stream().map(e -> e.getGoodsId()).distinct().collect(Collectors.toList());
        QueryGoodsPriceRequest request = new QueryGoodsPriceRequest();
        request.setCustomerEid(staffInfo.getCurrentEid());
        request.setGoodsIds(goodsIds);
        Map<Long, GoodsPriceDTO> goodsPriceMap = goodsPriceApi.queryB2bGoodsPriceInfoMap(request);

        List<CartGoodsVO> cartGoodsVOList = cartDTOList.stream().map(e -> {

            CartGoodsVO cartGoodsVO = new CartGoodsVO();
            GoodsSkuStandardBasicDTO standardGoodsBasicDTO = allSkuGoodsDTOMap.get(e.getGoodsSkuId());
            // 商品信息
            // 拷贝商品标准库信息e
            PojoUtils.map(SimpleGoodInfoUtils.toSimpleGoodsVO(standardGoodsBasicDTO.getStandardGoodsBasic()), cartGoodsVO);
            cartGoodsVO.setCartId(e.getId());
            cartGoodsVO.setDistributorGoodsId(standardGoodsBasicDTO.getGoodsId());
            cartGoodsVO.setBigPackage(standardGoodsBasicDTO.getStandardGoodsBasic().getBigPackage());

            cartGoodsVO.setPrice(goodsPriceMap.get(e.getDistributorGoodsId()).getLinePrice());

            cartGoodsVO.setQuantity(e.getQuantity());
            cartGoodsVO.setAmount(NumberUtil.round(NumberUtil.mul(cartGoodsVO.getPrice(), cartGoodsVO.getQuantity()), 2));
            cartGoodsVO.setSelectEnabled(true);
            cartGoodsVO.setSelected(e.getSelectedFlag() == 1);
            cartGoodsVO.setStatus(CartGoodsStatusEnum.NORMAL.getCode());
            cartGoodsVO.setStatusText(CartGoodsStatusEnum.NORMAL.getName());
            return cartGoodsVO;
        }).collect(Collectors.toList());


        return Result.success(new CollectionObject(cartGoodsVOList));
    }




    /**
     * 过滤出库存充足的商品数据
     * @param allGoodsDTOList sku商品信息
     * @param skuGoodQuantityMap 商品数量包含购车中已选中的
     * @return
     */
    private List<Long> filterEnoughSkuData(Long buyerEid,List<GoodsSkuStandardBasicDTO> allGoodsDTOList, Map<Long, Long> skuGoodQuantityMap) {

        List<Long> allGoodsSkuIds = allGoodsDTOList.stream().map(t -> t.getId()).collect(Collectors.toList());
        List<List<Long>> subList = Lists.partition(allGoodsSkuIds, 50);
        Map<Long, InventoryDTO> inventoryDTOMap = MapUtil.newHashMap();

        subList.forEach(e -> {

            Map<Long, InventoryDTO> inventoryMap = inventoryApi.getMapBySkuIds(e);

            inventoryDTOMap.putAll(Optional.ofNullable(inventoryMap).orElse(Collections.EMPTY_MAP));
        });

        if (CollectionUtil.isEmpty(inventoryDTOMap)) {

            return Collections.emptyList();
        }

        // 查询出购物车中已经选中的商品信息,用于校验库存是否充足
        List<CartDTO> cartDTOList = cartApi.listByBuyerEid(buyerEid, PlatformEnum.B2B, CartGoodsSourceEnum.B2B, CartIncludeEnum.ALL);
        // 已加入购物车的商品数量信息
        Map<Long, Long> cartDTOMap = cartDTOList.stream().filter(t -> PromotionActivityTypeEnum.NORMAL == PromotionActivityTypeEnum.getByCode(t.getPromotionActivityType())).collect(Collectors.groupingBy(CartDTO::getGoodsSkuId, Collectors.summingLong(CartDTO::getQuantity)));
        Map<Long, GoodsSkuStandardBasicDTO> allGoodsDTOMap = allGoodsDTOList.stream().collect(Collectors.toMap(GoodsSkuStandardBasicDTO::getId, Function.identity()));

        return  allGoodsSkuIds.stream().filter(skuGoodId -> {

            /****************************过滤商品状态是否符合**********************************/

            GoodsSkuStandardBasicDTO goodsDTO = allGoodsDTOMap.get(skuGoodId);

            if (GoodsStatusEnum.getByCode(goodsDTO.getStandardGoodsBasic().getGoodsStatus()) != GoodsStatusEnum.UP_SHELF) {
                return false;
            }
            if (!GoodsSkuStatusEnum.NORMAL.getCode().equals(goodsDTO.getStatus())){
                return false;
            }
            // 校验是否审核通过
            if (!GoodsStatusEnum.AUDIT_PASS.getCode().equals(goodsDTO.getStandardGoodsBasic().getAuditStatus())){
                return false;
            }
            return true;

        }).filter(skuGoodId -> {

            /****************************过滤商品库存是否符合要求**********************************/

            InventoryDTO inventoryDTO = inventoryDTOMap.get(skuGoodId);
            GoodsSkuStandardBasicDTO goodsDTO = allGoodsDTOMap.get(skuGoodId);
            Long quantity = skuGoodQuantityMap.get(skuGoodId);
            // 购物车原有数量
            Long cartNumber = cartDTOMap.getOrDefault(skuGoodId,0l);

            // 无库存
            if (inventoryDTO == null) {

                return false;
            }

            // 如果不满足包装规格,按照
            if (quantity % goodsDTO.getPackageNumber() != 0) {

                quantity = goodsDTO.getPackageNumber();
            }

            // 库存存在,但是购买数量大于可购买数量时
            Long availableQty = inventoryDTO.getQty() - inventoryDTO.getFrozenQty() - quantity - cartNumber;
            // 可购买的最大数量
            Long canBuyQty = inventoryDTO.getQty() - inventoryDTO.getFrozenQty() - cartNumber;

            if (CompareUtil.compare(new BigDecimal(availableQty),BigDecimal.ZERO) < 0 ) {
                // 如果本来加入购物车的数量就已经超出,无需加购
                if (CompareUtil.compare(new BigDecimal(canBuyQty),BigDecimal.ZERO) < 0) {

                    return false;
                }
                // 如果可购买数量小于商品最小包装规格数量,无需加购
                if (CompareUtil.compare(new BigDecimal(canBuyQty),new BigDecimal(goodsDTO.getPackageNumber())) < 0) {

                    return false;
                }
            }

            Long rate = canBuyQty / goodsDTO.getPackageNumber();
            Long stockNum = rate * goodsDTO.getPackageNumber();
            Long userNumber = cartNumber + quantity;
            Long rate1 = userNumber / goodsDTO.getPackageNumber();
            Long maxBuyNumber = rate1 * goodsDTO.getPackageNumber();

            if (userNumber % goodsDTO.getPackageNumber() != 0) {
                if (cartNumber > maxBuyNumber) {
                    return false;
                } else  {
                    quantity = maxBuyNumber - cartNumber;
                }
            }

            // 判断数量是否大于库存最大数量
            if (quantity > stockNum) {

                 quantity = stockNum;
            }

            // 设置为最最大购买数量
            skuGoodQuantityMap.put(skuGoodId,quantity);

            return true;

        }).collect(Collectors.toList());

    }


    @ApiOperation(value = "再次购买")
    @PostMapping("/history/add")
    @ApiResponses({
            @ApiResponse(code = 10350, message = "商品全部卖光,看看其他品种!"),
            @ApiResponse(code = 10351, message = "部分商品已卖光,具体卖光的商品信息,data里面返回!"),
    })
    @UserAccessAuthentication(checkEnterpriseAuthStatus = true, checkEnterpriseStatus = true, checkUserEnterpriseRelation = true, checkUserStatus = true)
    public Result<CollectionObject<SimpleGoodsVO>> historyAdd(@CurrentUser CurrentStaffInfo staffInfo, @ApiParam(value = "订单ID", required = true) @RequestParam("orderId") Long orderId) {

        List<OrderDetailDTO> orderDetailDTOList = orderDetailApi.getOrderDetailInfo(orderId);

        if (CollectionUtil.isEmpty(orderDetailDTOList)) {

            return Result.failed("未查询到订单信息!");
        }

        OrderDTO orderDTO = orderApi.getOrderInfo(orderId);

        if (orderDTO == null) {

            return Result.failed("未查询到订单信息!");
        }

        if (OrderTypeEnum.B2B != OrderTypeEnum.getByCode(orderDTO.getOrderType())) {

            return Result.failed("订单类型不匹配!");
        }

        // 所有的sku信息
        List<Long> allSkuGoodIds = orderDetailDTOList.stream().map(t -> t.getGoodsSkuId()).collect(Collectors.toList());
        // 订单明细购买库存数量
        Map<Long, Long> skuGoodQuantityMap = orderDetailDTOList.stream().collect(Collectors.groupingBy(OrderDetailDTO::getGoodsSkuId, Collectors.summingLong(OrderDetailDTO::getGoodsQuantity)));
        // 查询出所有的商品信息
        List<GoodsSkuStandardBasicDTO> allGoodsDTOList = goodsApi.batchQueryStandardGoodsBasicBySkuIds(allSkuGoodIds);
        // 过滤符合商品标准信息的
        List<Long> canAddSkuData = this.filterEnoughSkuData(orderDTO.getBuyerEid(),allGoodsDTOList,skuGoodQuantityMap);

        if (log.isDebugEnabled()) {

            log.debug("再来一单,符合加购标准的商品信息[{}]",canAddSkuData);
        }

        if (CollectionUtil.isEmpty(canAddSkuData)) {

            return Result.failed(10350,"商品全部卖光,看看其他品种!");
        }

        BatchAddToCartRequest request = new BatchAddToCartRequest();
        request.setBuyerEid(orderDTO.getBuyerEid());
        request.setPlatformEnum(PlatformEnum.B2B);
        request.setGoodsSourceEnum(CartGoodsSourceEnum.B2B);
        request.setOpUserId(staffInfo.getCurrentUserId());

        List<BatchAddToCartRequest.QuickPurchaseInfoDTO> quickPurchaseInfoList = orderDetailDTOList.stream().filter(t -> canAddSkuData.contains(t.getGoodsSkuId())).map(t -> {

            BatchAddToCartRequest.QuickPurchaseInfoDTO quickPurchaseInfoDTO = new BatchAddToCartRequest.QuickPurchaseInfoDTO();
            quickPurchaseInfoDTO.setGoodsId(t.getGoodsId());
            quickPurchaseInfoDTO.setDistributorGoodsId(t.getDistributorGoodsId());
            quickPurchaseInfoDTO.setDistributorEid(orderDTO.getDistributorEid());
            quickPurchaseInfoDTO.setGoodsSkuId(t.getGoodsSkuId());
            quickPurchaseInfoDTO.setQuantity(skuGoodQuantityMap.get(t.getGoodsSkuId()).intValue());

            return quickPurchaseInfoDTO;

        }).collect(Collectors.toList());

        request.setQuickPurchaseInfoList(quickPurchaseInfoList);

        Boolean result = cartApi.historyAddToCart(request);

        if (!result) {

            return Result.failed("再次购买添加失败!");
        }

        // 不满足的sku信息
        List<GoodsSkuStandardBasicDTO> notEngoughSkuGoodInfos = ListUtil.filter(allGoodsDTOList, (t) -> { if ( canAddSkuData.contains(t.getId())) { return null; } return t; });

        if (CollectionUtil.isEmpty(notEngoughSkuGoodInfos)) {

            return Result.success();
        }

        List<SimpleGoodsVO>  goodsVOList = notEngoughSkuGoodInfos.stream().map(t -> {
            SimpleGoodsVO simpleGoodsVO = new SimpleGoodsVO();
            PojoUtils.map(SimpleGoodInfoUtils.toSimpleGoodsVO(t.getStandardGoodsBasic()), simpleGoodsVO);
            return simpleGoodsVO;
        }).collect(Collectors.toList());

        return Result.failed(10351,"部分商品已卖光!",new CollectionObject<>(goodsVOList));
    }

}