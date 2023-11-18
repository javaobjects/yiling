package com.yiling.b2b.app.order.controller;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yiling.b2b.app.cart.util.SimpleGoodInfoUtils;
import com.yiling.b2b.app.deliveryAddress.vo.DeliveryAddressVO;
import com.yiling.b2b.app.order.form.B2BOrderVerificationForm;
import com.yiling.b2b.app.order.form.CheckCouponForm;
import com.yiling.b2b.app.order.form.CheckPaymentActivityForm;
import com.yiling.b2b.app.order.form.OrderB2BAppPayAmountForm;
import com.yiling.b2b.app.order.form.OrderB2BPageForm;
import com.yiling.b2b.app.order.form.OrderSubmitForm;
import com.yiling.b2b.app.order.function.CouponDiscountFunction;
import com.yiling.b2b.app.order.function.PaymentDiscountFunction;
import com.yiling.b2b.app.order.vo.OrderAddressVO;
import com.yiling.b2b.app.order.vo.OrderDeliveryVO;
import com.yiling.b2b.app.order.vo.OrderDetailVO;
import com.yiling.b2b.app.order.vo.OrderDistributorVO;
import com.yiling.b2b.app.order.vo.OrderGoodsDetailVO;
import com.yiling.b2b.app.order.vo.OrderGoodsVO;
import com.yiling.b2b.app.order.vo.OrderListVO;
import com.yiling.b2b.app.order.vo.OrderLogVO;
import com.yiling.b2b.app.order.vo.OrderSettlementPageVO;
import com.yiling.b2b.app.order.vo.PaymentActivityShowTypeEnum;
import com.yiling.b2b.app.order.vo.SubmitResultVO;
import com.yiling.common.web.goods.utils.PictureUrlUtils;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.annotations.UserAccessAuthentication;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.redis.RedisDistributedLock;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.redis.service.RedisService;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.IPUtils;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.inventory.api.InventoryApi;
import com.yiling.goods.inventory.dto.InventoryDTO;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsSkuInfoDTO;
import com.yiling.goods.medicine.dto.GoodsSkuStandardBasicDTO;
import com.yiling.goods.medicine.enums.GoodsSkuStatusEnum;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.goods.restriction.api.GoodsPurchaseRestrictionApi;
import com.yiling.goods.restriction.dto.GoodsPurchaseRestrictionDTO;
import com.yiling.goods.restriction.dto.request.QueryGoodsPurchaseRestrictionRequest;
import com.yiling.mall.agreement.api.AgreementBusinessApi;
import com.yiling.mall.agreement.enums.GoodsLimitStatusEnum;
import com.yiling.mall.cart.api.CartApi;
import com.yiling.mall.cart.dto.CartDTO;
import com.yiling.mall.cart.dto.request.GetCartGoodsInfoRequest;
import com.yiling.mall.cart.enums.CartGoodsSourceEnum;
import com.yiling.mall.cart.enums.CartIncludeEnum;
import com.yiling.mall.order.api.OrderProcessApi;
import com.yiling.mall.order.bo.OrderSubmitBO;
import com.yiling.mall.order.dto.request.OrderSubmitRequest;
import com.yiling.marketing.promotion.api.PromotionActivityApi;
import com.yiling.marketing.promotion.dto.PromotionCombinationPackDTO;
import com.yiling.marketing.promotion.dto.PromotionDTO;
import com.yiling.marketing.promotion.dto.PromotionGoodsLimitDTO;
import com.yiling.marketing.promotion.dto.request.PromotionActivityRequest;
import com.yiling.marketing.promotion.dto.request.PromotionAppRequest;
import com.yiling.order.order.api.OrderAddressApi;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderB2BApi;
import com.yiling.order.order.api.OrderDeliveryApi;
import com.yiling.order.order.api.OrderDetailApi;
import com.yiling.order.order.api.OrderDetailChangeApi;
import com.yiling.order.order.api.OrderLogApi;
import com.yiling.order.order.api.OrderPromotionActivityApi;
import com.yiling.order.order.api.OrderReturnApi;
import com.yiling.order.order.api.OrderReturnDetailBatchApi;
import com.yiling.order.order.api.PresaleOrderApi;
import com.yiling.order.order.dto.OrderAddressDTO;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDeliveryDTO;
import com.yiling.order.order.dto.OrderDetailChangeDTO;
import com.yiling.order.order.dto.OrderDetailDTO;
import com.yiling.order.order.dto.OrderLogDTO;
import com.yiling.order.order.dto.OrderPromotionActivityDTO;
import com.yiling.order.order.dto.OrderReturnDetailBatchDTO;
import com.yiling.order.order.dto.PresaleOrderDTO;
import com.yiling.order.order.dto.request.OrderB2BPageRequest;
import com.yiling.order.order.dto.request.QueryUserBuyNumberRequest;
import com.yiling.order.order.enums.OrderCategoryEnum;
import com.yiling.order.order.enums.OrderErpPushStatusEnum;
import com.yiling.order.order.enums.OrderErrorCode;
import com.yiling.order.order.enums.OrderReturnStatusEnum;
import com.yiling.order.order.enums.OrderSourceEnum;
import com.yiling.order.order.enums.OrderStatusEnum;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.order.order.enums.PaymentMethodEnum;
import com.yiling.order.order.enums.PaymentStatusEnum;
import com.yiling.order.order.enums.PreSaleActivityTypeEnum;
import com.yiling.order.order.enums.PromotionActivityTypeEnum;
import com.yiling.order.payment.enums.PaymentErrorCode;
import com.yiling.payment.enums.TradeTypeEnum;
import com.yiling.payment.pay.api.PayApi;
import com.yiling.payment.pay.dto.request.CreatePayOrderRequest;
import com.yiling.pricing.goods.api.GoodsPriceApi;
import com.yiling.pricing.goods.dto.GoodsPriceDTO;
import com.yiling.pricing.goods.dto.request.QueryGoodsPriceRequest;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.enterprise.api.DeliveryAddressApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.api.PaymentMethodApi;
import com.yiling.user.enterprise.dto.DeliveryAddressDTO;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.enums.ErpSyncLevelEnum;
import com.yiling.user.payment.api.PaymentDaysAccountApi;
import com.yiling.user.payment.dto.PaymentDaysAccountDTO;
import com.yiling.user.payment.dto.PaymentDaysOrderDTO;
import com.yiling.user.shop.api.ShopApi;
import com.yiling.user.shop.dto.ShopDTO;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.dto.PaymentMethodDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.comparator.CompareUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;


/**
 * B2B订单controller
 *
 * @author:wei.wang
 * @date:2021/10/20
 */
@Slf4j
@RestController
@RequestMapping("/order")
@Api(tags = "订单模块")
public class OrderController extends BaseController {
    @DubboReference
    OrderB2BApi orderB2BApi;
    @DubboReference
    OrderDetailChangeApi orderDetailChangeApi;
    @DubboReference
    GoodsApi goodsApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    OrderLogApi orderLogApi;
    @DubboReference
    OrderAddressApi orderAddressApi;
    @DubboReference
    OrderDetailApi orderDetailApi;
    @DubboReference
    PaymentDaysAccountApi paymentDaysAccountApi;
    @DubboReference
    DeliveryAddressApi deliveryAddressApi;
    @DubboReference
    CartApi cartApi;
    @DubboReference
    InventoryApi inventoryApi;
    @DubboReference
    PaymentMethodApi paymentMethodApi;
    @DubboReference
    OrderProcessApi orderProcessApi;
    @DubboReference
    OrderDeliveryApi orderDeliveryApi;
    @DubboReference
    GoodsPriceApi goodsPriceApi;
    @DubboReference
    ShopApi shopApi;
    @DubboReference
    PayApi payApi;
    @DubboReference
    OrderReturnDetailBatchApi orderReturnDetailBatchApi;
    @DubboReference
    OrderReturnApi orderReturnApi;
    @DubboReference
    AgreementBusinessApi agreementBusinessApi;
    @DubboReference
    PromotionActivityApi promotionActivityApi;
    @DubboReference
    OrderPromotionActivityApi orderPromotionActivityApi;
    @DubboReference
    PresaleOrderApi presaleOrderApi;
    @DubboReference
    OrderApi   orderApi;
    @DubboReference
    GoodsPurchaseRestrictionApi goodsPurchaseRestrictionApi;
    @Autowired
    private CouponDiscountFunction couponDiscountFunction;
    @Autowired
    private PaymentDiscountFunction paymentDiscountFunction;
    @Autowired
    PictureUrlUtils pictureUrlUtils;
    @Autowired
    HttpServletRequest httpRequest;
    @Autowired
    RedisService redisService;
    @Autowired
    RedisDistributedLock redisDistributedLock;


    /**
     * 查询企业收货地址信息，
     *
     * @param eid 企业ID
     * @return
     */
    private DeliveryAddressVO selectDeliveryAddressInfoList(Long eid) {
        DeliveryAddressDTO addressDTO = deliveryAddressApi.getDefaultAddressByEid(eid);
        if (addressDTO != null && StringUtils.isBlank(addressDTO.getReceiver())) {
            addressDTO.setReceiver("未填写收货人");
        }
        return PojoUtils.map(addressDTO, DeliveryAddressVO.class);
    }

    /**
     * 设置订单支付方式
     *
     * @param paymentMethodDTOList
     * @param orderDistributorVO
     */
    private void setPaymentMethod(List<PaymentMethodDTO> paymentMethodDTOList, Long choosePaymentMethodId, OrderDistributorVO orderDistributorVO) {
        orderDistributorVO.setPaymentMethodList(ListUtil.empty());
        orderDistributorVO.setPaymentMethod(0l);
        List<OrderDistributorVO.PaymentMethodVO> paymentMethodVOList = Lists.newArrayList();
        for (PaymentMethodDTO e : paymentMethodDTOList) {
            OrderDistributorVO.PaymentMethodVO paymentMethodVO = new OrderDistributorVO.PaymentMethodVO();
            paymentMethodVO.setId(e.getCode());
            paymentMethodVO.setName(e.getName());
            paymentMethodVO.setEnabled(1 == e.getStatus());
            paymentMethodVO.setSelected(false);
            paymentMethodVO.setDiscountValueRules("");
            // 如果有企业方式，按照用户选择的企业方式
            if (choosePaymentMethodId != null && choosePaymentMethodId.equals(e.getCode().longValue())) {
                paymentMethodVO.setSelected(true);
                orderDistributorVO.setPaymentMethod(choosePaymentMethodId);
                paymentMethodVOList.add(paymentMethodVO);
                continue;
            }
            // 默认在线支付
            if (choosePaymentMethodId == null && PaymentMethodEnum.ONLINE == PaymentMethodEnum.getByCode(e.getCode())) {
                paymentMethodVO.setSelected(true);
                orderDistributorVO.setPaymentMethod(paymentMethodVO.getId());
                paymentMethodVOList.add(paymentMethodVO);
                continue;
            }
            paymentMethodVOList.add(paymentMethodVO);
        }
        // 如果企业不存线上支付方式,从新设置一个线下默认方式
        if (!paymentMethodVOList.stream().filter(t -> t.getSelected()).findFirst().isPresent()) {
            paymentMethodVOList.stream().findFirst().ifPresent(e -> e.setSelected(true));
        }
        orderDistributorVO.setPaymentMethodList(paymentMethodVOList);
    }


    /**
     * 查询商品秒杀&特价信息
     *
     * @param cartDTOList 购物车商品信息
     * @param buyerEid 购买企业
     * @return 秒杀&特价活动信息
     */
    private Map<Long, List<PromotionGoodsLimitDTO>> selectSpecialGoodsActivity(List<CartDTO> cartDTOList, Long buyerEid) {
        // 秒杀商品集合
        List<CartDTO> promotionCartList = cartDTOList.stream().filter(t -> PromotionActivityTypeEnum.getByCode(t.getPromotionActivityType()) == PromotionActivityTypeEnum.LIMIT || PromotionActivityTypeEnum.getByCode(t.getPromotionActivityType()) == PromotionActivityTypeEnum.SPECIAL).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(promotionCartList)) {
            return Collections.emptyMap();
        }
        List<Long> goodsIdList = promotionCartList.stream().map(z -> z.getDistributorGoodsId()).collect(Collectors.toList());

        PromotionAppRequest appRequest = new PromotionAppRequest();
        appRequest.setPlatform(PlatformEnum.B2B.getCode());
        appRequest.setBuyerEid(buyerEid);
        appRequest.setTypeList(ListUtil.toList(PromotionActivityTypeEnum.SPECIAL.getCode(), PromotionActivityTypeEnum.LIMIT.getCode()));
        appRequest.setGoodsIdList(goodsIdList);
        List<PromotionGoodsLimitDTO> promotionGoodsLimitDTOList = promotionActivityApi.queryGoodsPromotionInfo(appRequest);
        if (log.isDebugEnabled()) {
            log.debug("..selectSpecialGoodsActivity..request ->{}...,result->{}", JSON.toJSON(appRequest), JSON.toJSON(promotionGoodsLimitDTOList));
        }

        return promotionGoodsLimitDTOList.stream().collect(Collectors.groupingBy(PromotionGoodsLimitDTO::getEid));
    }


    /**
     * 获取秒杀价格
     *
     * @param cartInfo
     * @param orderGoodsVO
     * @param promotionGoodsLimitDTOs
     * @return
     */
    private BigDecimal getPromotionPrice(CartDTO cartInfo, OrderGoodsVO orderGoodsVO, List<PromotionGoodsLimitDTO> promotionGoodsLimitDTOs) {
        if (CollectionUtil.isEmpty(promotionGoodsLimitDTOs)) {
            String failMsg = MessageFormat.format(OrderErrorCode.ORDER_PROMOTION_EXPIRED.getMessage(), orderGoodsVO.getName());
            throw new BusinessException(OrderErrorCode.ORDER_PROMOTION_EXPIRED, failMsg);
        }
        List<PromotionGoodsLimitDTO> goodsLimitDTOs = promotionGoodsLimitDTOs.stream().filter(t -> t.getPromotionActivityId().equals(cartInfo.getPromotionActivityId())).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(goodsLimitDTOs)) {
            String failMsg = MessageFormat.format(OrderErrorCode.ORDER_PROMOTION_EXPIRED.getMessage(), orderGoodsVO.getName());
            throw new BusinessException(OrderErrorCode.ORDER_PROMOTION_EXPIRED, failMsg);
        }
        PromotionGoodsLimitDTO promotionGoodsLimitDto = goodsLimitDTOs.stream().findFirst().get();
        GetCartGoodsInfoRequest getCartGoodsInfoRequest = new GetCartGoodsInfoRequest();
        getCartGoodsInfoRequest.setGoodsSkuId(cartInfo.getGoodsSkuId());
        getCartGoodsInfoRequest.setGoodsId(cartInfo.getDistributorGoodsId());
        getCartGoodsInfoRequest.setBuyerEid(cartInfo.getBuyerEid());
        getCartGoodsInfoRequest.setDistributorEid(cartInfo.getDistributorEid());
        getCartGoodsInfoRequest.setGoodsSourceEnum(CartGoodsSourceEnum.B2B);
        getCartGoodsInfoRequest.setPlatformEnum(PlatformEnum.B2B);
        getCartGoodsInfoRequest.setCartIncludeEnum(CartIncludeEnum.SELECTED);
        getCartGoodsInfoRequest.setPromotionActivityTypeEnum(PromotionActivityTypeEnum.getByCode(cartInfo.getPromotionActivityType()));
        Integer goodsQuantity = cartApi.sumGoodsQuantityByGoodId(getCartGoodsInfoRequest);
        if (CompareUtil.compare(cartInfo.getQuantity() + goodsQuantity, promotionGoodsLimitDto.getLeftBuyCount()) > 0) {
            String failMsg = MessageFormat.format(OrderErrorCode.ORDER_PROMOTION_LIMITD.getMessage(), orderGoodsVO.getName());
            throw new BusinessException(OrderErrorCode.ORDER_PROMOTION_LIMITD, failMsg);
        }
        return promotionGoodsLimitDto.getPromotionPrice();
    }

    /**
     * 设置组合促销活动信息
     *
     * @param orderGoodsVO
     * @param promotionDTO
     */
    private void setCombinationActivity(OrderGoodsVO orderGoodsVO, PromotionDTO promotionDTO) {
        orderGoodsVO.setPictureUrl(promotionDTO.getPromotionCombinationPackDTO().getPic());
        orderGoodsVO.setGoodPic(promotionDTO.getPromotionCombinationPackDTO().getPic());
        orderGoodsVO.setPackageName(promotionDTO.getPromotionCombinationPackDTO().getPackageName());
        orderGoodsVO.setPackageShortName(promotionDTO.getPromotionCombinationPackDTO().getPackageShortName());
        List<PromotionGoodsLimitDTO> promotionGoodsLimitDTOList = promotionDTO.getGoodsLimitDTOList();
        BigDecimal packageTotalPrice = BigDecimal.ZERO;
        for (PromotionGoodsLimitDTO promotionGoodsLimitDTO : promotionGoodsLimitDTOList) {
            packageTotalPrice = NumberUtil.add(packageTotalPrice, NumberUtil.mul(promotionGoodsLimitDTO.getAllowBuyCount(), promotionGoodsLimitDTO.getPromotionPrice()));
        }
        orderGoodsVO.setPrice(packageTotalPrice);
    }


    /**
     * 校验组合包商品限购逻辑
     * @param allCartDOList
     * @param comPromotionMap
     */
    private void validateCombinationOrderLimit(List<CartDTO> allCartDOList, Map<Long, PromotionDTO> comPromotionMap){

        List<CartDTO> combimationCartDOList = allCartDOList.stream().filter(t -> PromotionActivityTypeEnum.COMBINATION == PromotionActivityTypeEnum.getByCode(t.getPromotionActivityType())).collect(Collectors.toList());

        if (CollectionUtil.isEmpty(combimationCartDOList)) {

            return;
        }

        combimationCartDOList.forEach(cartDTO -> {
            PromotionDTO promotionDTO = comPromotionMap.get(cartDTO.getPromotionActivityId());
            // 校验活动是否已经失效
            if (promotionDTO == null || promotionDTO.getAvailable() != 1) {
                throw new BusinessException(OrderErrorCode.ORDER_PROMOTION_ERROR);
            }
            PromotionCombinationPackDTO promotionCombinationPackDTO = promotionDTO.getPromotionCombinationPackDTO();
            // 购买数量小于促销活动最小起订量
            if (CompareUtil.compare(cartDTO.getQuantity(), promotionCombinationPackDTO.getInitialNum()) < 0) {
                String failMsg = MessageFormat.format(OrderErrorCode.ORDER_PROMOTION_MINIMUM_NUM.getMessage(), promotionCombinationPackDTO.getInitialNum());
                throw new BusinessException(OrderErrorCode.ORDER_PROMOTION_MINIMUM_NUM, failMsg);
            }

        });

        List<CartDTO> limitBuyCartDTOList = combimationCartDOList.stream().filter(cartDTO -> {
            PromotionDTO  promotionDTO = comPromotionMap.get(cartDTO.getPromotionActivityId());
            if (ObjectUtil.isNull(promotionDTO)) {
                return false;
            }
            return  promotionDTO.getPromotionCombinationPackDTO().getTotalNum() != 0 ;
        }).collect(Collectors.toList());


        // 是否校验限购商品信息
        if (CollectionUtil.isEmpty(limitBuyCartDTOList)) {

            return;
        }

        // 组合包活动已购买数量
        List<Long> activityIds = limitBuyCartDTOList.stream().map(t -> t.getPromotionActivityId()).collect(Collectors.toList());
        Map<Long,Long> combinationBuyNumberBOMap = orderPromotionActivityApi.sumBatchCombinationActivityNumber(activityIds);

        if (log.isDebugEnabled()) {

            log.debug("查询用户已购买组合包数量入参：{},返回:[{}]",activityIds,JSON.toJSONString(combinationBuyNumberBOMap));
        }

        limitBuyCartDTOList.forEach(cartDTO -> {
            PromotionDTO promotionDTO = comPromotionMap.get(cartDTO.getPromotionActivityId());
            PromotionCombinationPackDTO promotionCombinationPackDTO = promotionDTO.getPromotionCombinationPackDTO();
            Long sumQty = combinationBuyNumberBOMap.getOrDefault(cartDTO.getPromotionActivityId(),0l);

            // 校验总购买数量
            if (CompareUtil.compare(cartDTO.getQuantity() + sumQty.intValue(),promotionCombinationPackDTO.getTotalNum()) > 0) {

                String failMsg = MessageFormat.format(OrderErrorCode.SUBMIT_COMBINATION_ORDER_LIMIT_ERROR.getMessage(), promotionCombinationPackDTO.getPackageName());
                throw new BusinessException(OrderErrorCode.SUBMIT_COMBINATION_ORDER_LIMIT_ERROR,failMsg);
            }
        });

    }

    /**
     * 校验库存是否充足
     *
     * @param allCartDOList
     */
    protected void validateGoodsInventory(List<Long> allGoodsSkuIds, List<CartDTO> allCartDOList, Map<Long, PromotionDTO> comPromotionMap) {
        Map<Long, Integer> skuNumberMap = Maps.newHashMap();
        Map<Long, InventoryDTO> distributorGoodsInventoryDTOMap = inventoryApi.getMapBySkuIds(allGoodsSkuIds);
        for (CartDTO cartDO : allCartDOList) {
            // 表示组合促销活动商品
            if (PromotionActivityTypeEnum.COMBINATION == PromotionActivityTypeEnum.getByCode(cartDO.getPromotionActivityType())) {
                PromotionDTO promotionDTO = comPromotionMap.get(cartDO.getPromotionActivityId());
                for (PromotionGoodsLimitDTO promotionGoodsLimitDTO : promotionDTO.getGoodsLimitDTOList()) {
                    Integer skuNumber = skuNumberMap.getOrDefault(promotionGoodsLimitDTO.getGoodsSkuId(), 0);
                    InventoryDTO inventoryDTO = distributorGoodsInventoryDTOMap.get(promotionGoodsLimitDTO.getGoodsSkuId());
                    // 组合购买数量
                    Integer buyNumber = cartDO.getQuantity() * promotionGoodsLimitDTO.getAllowBuyCount();
                    if (CompareUtil.compare(cartDO.getQuantity(), 0) <= 0) {
                        throw new BusinessException(OrderErrorCode.SUBMIT_GOODS_INVENTORY_NOT_ENOUGH);
                    }
                    if (inventoryDTO == null || inventoryDTO.getQty() - inventoryDTO.getFrozenQty() < buyNumber + skuNumber) {
                        throw new BusinessException(OrderErrorCode.SUBMIT_GOODS_INVENTORY_NOT_ENOUGH);
                    }
                    skuNumberMap.put(cartDO.getGoodsSkuId(), skuNumber + buyNumber);
                }
                // 非组合商品信息校验库存
            } else {
                Integer skuNumber = skuNumberMap.getOrDefault(cartDO.getGoodsSkuId(), 0);
                InventoryDTO inventoryDTO = distributorGoodsInventoryDTOMap.get(cartDO.getGoodsSkuId());
                if (CompareUtil.compare(cartDO.getQuantity(), 0) <= 0) {
                    throw new BusinessException(OrderErrorCode.SUBMIT_GOODS_INVENTORY_NOT_ENOUGH);
                }
                if (inventoryDTO == null || inventoryDTO.getQty() - inventoryDTO.getFrozenQty() < cartDO.getQuantity() + skuNumber) {
                    throw new BusinessException(OrderErrorCode.SUBMIT_GOODS_INVENTORY_NOT_ENOUGH);
                }
                skuNumberMap.put(cartDO.getGoodsSkuId(), skuNumber + cartDO.getQuantity());
            }

        }
    }


    /**
     * 获取店铺账期上浮浮动值
     * @param paymentMethodVO 支付方式
     * @param distributorEid 配送商(卖家)
     * @param buyerEid 买家(企业Eid)
     * @return
     */
    private BigDecimal getShopUpPoint (OrderDistributorVO.PaymentMethodVO paymentMethodVO,Long distributorEid,Long buyerEid) {

        BigDecimal upPoint = new BigDecimal(1);

        if (paymentMethodVO == null) {
            return upPoint;
        }

        if (PaymentMethodEnum.PAYMENT_DAYS != PaymentMethodEnum.getByCode(paymentMethodVO.getId().longValue())) {
            return upPoint;
        }

        PaymentDaysAccountDTO paymentDaysAccountDTO = paymentDaysAccountApi.getByCustomerEid(distributorEid, buyerEid);
        if (paymentDaysAccountDTO == null) {
            throw new BusinessException(UserErrorCode.PAYMENT_DAYS_ACCOUNT_NOT_EXISTS);
        }

        if (paymentDaysAccountDTO.getUpPoint() != null) {
            upPoint = NumberUtil.add(upPoint, NumberUtil.div(paymentDaysAccountDTO.getUpPoint(), 100));
        }

        return upPoint;
    }


    /**
     * @param distributorEid 配送商ID
     * @param distributorName 配送商名称
     * @param distributorCartDTOList 配送商对应的购物车ID
     * @param allDistributorGoodsDTOMap 商品库map
     * @param paymentMethodDTOList 支付集合
     * @param shopCustomerCouponId 店铺优惠劵ID
     * @param pictureMap 图片地址
     * @param yilingSubEids 以岭企业ID
     * @param goodsPriceMap 价格
     * @param promotionGoodsLimitDTOS 秒杀信息
     * @return
     */
    private OrderDistributorVO buildOrderDistributorVo(Long buyerEid, Long distributorEid, String distributorName, List<CartDTO> distributorCartDTOList, Map<Long, GoodsSkuStandardBasicDTO> allDistributorGoodsDTOMap, List<PaymentMethodDTO> paymentMethodDTOList, Map<Long, String> pictureMap, List<Long> yilingSubEids, Integer getPaymentMethod, Long shopCustomerCouponId, Map<Long, GoodsPriceDTO> goodsPriceMap, ShopDTO shopOne, String buyerMessage, List<PromotionGoodsLimitDTO> promotionGoodsLimitDTOS, Map<Long, PromotionDTO> comPromotionMap

    ) {
        // 校验支付方式
        if (CollUtil.isEmpty(paymentMethodDTOList)) {
            // 如果未查询到支付方式，默认为在线支付
            PaymentMethodDTO paymentMethodDTO = new PaymentMethodDTO();
            paymentMethodDTO.setCode(4l);
            paymentMethodDTO.setName("在线支付");
            paymentMethodDTO.setStatus(1);
            paymentMethodDTO.setType(1);
            paymentMethodDTOList = Collections.singletonList(paymentMethodDTO);
        } else {
            boolean result = paymentMethodDTOList.stream().anyMatch(t -> PaymentMethodEnum.ONLINE == PaymentMethodEnum.getByCode(t.getCode()));

            if (!result) {
                PaymentMethodDTO paymentMethodDTO = new PaymentMethodDTO();
                paymentMethodDTO.setCode(4l);
                paymentMethodDTO.setName("在线支付");
                paymentMethodDTO.setStatus(1);
                paymentMethodDTO.setType(1);
                paymentMethodDTOList.add(paymentMethodDTO);
            }
        }
        // 促销价格集合
        Map<Long, List<PromotionGoodsLimitDTO>> promotionGoodsLimitDTOMap = Maps.newHashMap();
        if (CollectionUtil.isNotEmpty(promotionGoodsLimitDTOS)) {
            promotionGoodsLimitDTOMap = promotionGoodsLimitDTOS.stream().collect(Collectors.groupingBy(PromotionGoodsLimitDTO::getGoodsId));
        }
        // 校验选择的支付方式
        if (getPaymentMethod != null) {
            this.checkPaymentMethod(paymentMethodDTOList, getPaymentMethod.longValue());
        }
        OrderDistributorVO orderDistributorVO = new OrderDistributorVO();
        // 设置支付方式
        this.setPaymentMethod(paymentMethodDTOList, Optional.ofNullable(getPaymentMethod).map(e -> e.longValue()).orElse(null), orderDistributorVO);
        // 当前支付方式
        OrderDistributorVO.PaymentMethodVO currentPaymentPayment = orderDistributorVO.getPaymentMethodList().stream().filter(t -> t.getSelected()).findFirst().get();
        // 获取账期浮动值
        BigDecimal upPoint = this.getShopUpPoint(currentPaymentPayment,distributorEid,buyerEid);
        List<OrderGoodsVO> orderGoodsVOList = CollUtil.newArrayList();
        BigDecimal amount = BigDecimal.ZERO;
        for (CartDTO cartInfo : distributorCartDTOList) {
            GoodsSkuStandardBasicDTO standardGoodsBasicDTO = allDistributorGoodsDTOMap.get(cartInfo.getGoodsSkuId());
            OrderGoodsVO orderGoodsVO = new OrderGoodsVO();
            orderGoodsVO.setCouponDiscountMoney(BigDecimal.ZERO);
            orderGoodsVO.setPromotionActivityId(cartInfo.getPromotionActivityId());
            // 非组合商品信息拷贝
            if (PromotionActivityTypeEnum.COMBINATION != PromotionActivityTypeEnum.getByCode(cartInfo.getPromotionActivityType())) {
                // 拷贝商品标准库信息
                PojoUtils.map(SimpleGoodInfoUtils.toSimpleGoodsVO(standardGoodsBasicDTO.getStandardGoodsBasic()), orderGoodsVO);
                orderGoodsVO.setPrice(goodsPriceMap.get(cartInfo.getDistributorGoodsId()).getLinePrice());
            }
            orderGoodsVO.setGoodsSkuId(cartInfo.getGoodsSkuId());
            //设置配送商商品ID
            orderGoodsVO.setGoodsId(cartInfo.getDistributorGoodsId());
            orderGoodsVO.setGoodPic(pictureUrlUtils.getGoodsPicUrl(pictureMap.get(cartInfo.getGoodsId())));
            orderGoodsVO.setGoodsQuantity(cartInfo.getQuantity());
            orderGoodsVO.setCartId(cartInfo.getId());
            // 促销活动类型
            orderGoodsVO.setPromotionActivityType(cartInfo.getPromotionActivityType() == null ? PromotionActivityTypeEnum.NORMAL.getCode() : cartInfo.getPromotionActivityType());
            // 秒杀，特价商品价格
            EnumSet<PromotionActivityTypeEnum> promotionActivityTypeEnumEnumSet = EnumSet.of(PromotionActivityTypeEnum.LIMIT, PromotionActivityTypeEnum.SPECIAL);
            if (promotionActivityTypeEnumEnumSet.contains(PromotionActivityTypeEnum.getByCode(cartInfo.getPromotionActivityType()))) {
                // 设置秒杀价格
                orderGoodsVO.setPrice(this.getPromotionPrice(cartInfo, orderGoodsVO, promotionGoodsLimitDTOMap.get(cartInfo.getDistributorGoodsId())));
            } else if (PromotionActivityTypeEnum.COMBINATION == PromotionActivityTypeEnum.getByCode(cartInfo.getPromotionActivityType())) {
                // 设置组合促销活动信息
                this.setCombinationActivity(orderGoodsVO, comPromotionMap.get(cartInfo.getPromotionActivityId()));
            } else if (currentPaymentPayment.getId() != null && PaymentMethodEnum.PAYMENT_DAYS == PaymentMethodEnum.getByCode(currentPaymentPayment.getId()) && cartInfo.getDistributorGoodsId().equals(cartInfo.getGoodsId())) {
                // 账期浮动商品价格
                orderGoodsVO.setPrice(NumberUtil.round(NumberUtil.mul(goodsPriceMap.get(cartInfo.getDistributorGoodsId()).getLinePrice(), upPoint), 2));
            }
            orderGoodsVO.setAmount(NumberUtil.round(NumberUtil.mul(orderGoodsVO.getPrice(), BigDecimal.valueOf(cartInfo.getQuantity())), 2));
            amount = amount.add(orderGoodsVO.getAmount());
            orderGoodsVOList.add(orderGoodsVO);
        }
        if (shopOne != null && shopOne.getStartAmount().compareTo(amount) > 0) {
            // 部分企业未满足起配金额，请满足后重新提交
            throw new BusinessException(OrderErrorCode.ORDER_SHOP_START_ERROR);
        }
        orderDistributorVO.setDistributorEid(distributorEid);
        orderDistributorVO.setDistributorName(distributorName);
        orderDistributorVO.setOrderGoodsList(orderGoodsVOList);
        orderDistributorVO.setGoodsSpeciesNum(orderGoodsVOList.stream().count());
        orderDistributorVO.setGoodsNum(orderGoodsVOList.stream().mapToLong(OrderGoodsVO::getGoodsQuantity).sum());
        orderDistributorVO.setTotalAmount(amount);
        orderDistributorVO.setFreightAmount(BigDecimal.ZERO);
        orderDistributorVO.setPaymentAmount(NumberUtil.sub(orderDistributorVO.getTotalAmount(), orderDistributorVO.getFreightAmount()));
        orderDistributorVO.setYilingFlag(yilingSubEids.contains(distributorEid));
        orderDistributorVO.setShopCouponCount(0);
        orderDistributorVO.setIsUseShopCoupon(false);
        orderDistributorVO.setCustomerShopCouponId(shopCustomerCouponId);
        orderDistributorVO.setShopCouponActivity(Collections.emptyList());
        orderDistributorVO.setBuyerMessage(buyerMessage);
        orderDistributorVO.setShopPaymentActivity(Collections.emptyList());
        orderDistributorVO.setPaymentDiscountMoney(BigDecimal.ZERO);
        orderDistributorVO.setCouponDiscountMoney(BigDecimal.ZERO);
        orderDistributorVO.setPlatformCouponDiscountMoney(BigDecimal.ZERO);
        orderDistributorVO.setPaymentActivityShowType(PaymentActivityShowTypeEnum.hidden);

        return orderDistributorVO;
    }

    /**
     * 校验订单支付方式
     *
     * @param paymentMethodDTOList
     * @param paymentMethodId
     */
    private void checkPaymentMethod(List<PaymentMethodDTO> paymentMethodDTOList, Long paymentMethodId) {

        List<Long> paymentMethodIds = paymentMethodDTOList.stream().map(PaymentMethodDTO::getCode).collect(Collectors.toList());
        if (!paymentMethodIds.contains(paymentMethodId)) {
            throw new BusinessException(PaymentErrorCode.PAYMENT_METHOD_UNUSABLE);
        }
    }


    /**
     * 构建前端传递参数
     *
     * @param checkCouponForm
     * @param shopCustomerCouponMap
     * @param buyerMessageMap
     * @param paymentMethodMap
     */
    private void buildCustomerParam(CheckPaymentActivityForm checkCouponForm, Map<Long, Long> shopCustomerCouponMap, Map<Long, String> buyerMessageMap, Map<Long, Integer> paymentMethodMap, Map<Long, Long> shopPaymentActivityMap) {
        if (checkCouponForm == null) {
            return;
        }
        if (CollectionUtil.isEmpty(checkCouponForm.getDistributorOrderList())) {
            return;
        }
        for (CheckPaymentActivityForm.DistributorForm distributorForm : checkCouponForm.getDistributorOrderList()) {
            if (distributorForm.getCustomerShopCouponId() != null && distributorForm.getCustomerShopCouponId() != 0l) {
                shopCustomerCouponMap.put(distributorForm.getDistributorEid(), distributorForm.getCustomerShopCouponId());
            }

            if (distributorForm.getShopPaymentActivityId() != null) {
                shopPaymentActivityMap.put(distributorForm.getDistributorEid(), distributorForm.getShopPaymentActivityId());
            }
            if (StringUtils.isNotBlank(distributorForm.getBuyerMessage())) {
                buyerMessageMap.put(distributorForm.getDistributorEid(), distributorForm.getBuyerMessage());
            }

            if (distributorForm.getPaymentMethod() != null) {
                paymentMethodMap.put(distributorForm.getDistributorEid(), distributorForm.getPaymentMethod());
            }
        }
    }



    @ApiOperation(value = "去结算页面-完善订单信息")
    @UserAccessAuthentication(checkEnterpriseAuthStatus = true, checkEnterpriseStatus = true, checkUserEnterpriseRelation = true, checkUserStatus = true)
    @RequestMapping(path = "/settlement", method = { org.springframework.web.bind.annotation.RequestMethod.POST, RequestMethod.GET })
    public Result<OrderSettlementPageVO> settlement(@CurrentUser CurrentStaffInfo staffInfo) {

        return this.check(staffInfo, null);
    }

    @ApiOperation(value = "择支付方式\"用户择优惠劵")
    @RequestMapping(path = "/check", method = { org.springframework.web.bind.annotation.RequestMethod.POST })
    public Result<OrderSettlementPageVO> check(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid CheckCouponForm checkCouponForm) {
        CheckPaymentActivityForm checkPaymentActivityForm = PojoUtils.map(checkCouponForm, CheckPaymentActivityForm.class);

        return this.checkPaymentActivity(staffInfo, checkPaymentActivityForm);
    }

    @ApiOperation(value = "择支付促销活动")
    @RequestMapping(path = "/check/paymentActivity", method = { org.springframework.web.bind.annotation.RequestMethod.POST })
    public Result<OrderSettlementPageVO> checkPaymentActivity(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid CheckPaymentActivityForm checkCouponForm) {
        List<CartDTO> cartDTOList = cartApi.listByBuyerEid(staffInfo.getCurrentEid(), PlatformEnum.B2B, CartGoodsSourceEnum.B2B, CartIncludeEnum.SELECTED);
        if (CollUtil.isEmpty(cartDTOList)) {

            return Result.failed(ResultCode.FAILED.getCode(),"进货单中无选中商品",OrderSettlementPageVO.empty());
        }
        // 校验采购关系
        List<Long> distributorEids = cartDTOList.stream().map(CartDTO::getDistributorEid).distinct().collect(Collectors.toList());
        // 校验商品采购权限
        Map<Long, List<CartDTO>> distributorCartDTOMap = cartDTOList.stream().collect(Collectors.groupingBy(CartDTO::getDistributorEid));
        // 组合商品skuId信息
        List<Long> comGoodsSkuIds = Lists.newArrayList();
        // 组合商品goodId信息
        List<Long> comGoodsIds = Lists.newArrayList();
        // 查询组合商品信息
        Map<Long, PromotionDTO> comPromotionMap  = this.getCombinationPromotionDTOMap(staffInfo, comGoodsSkuIds, comGoodsIds, cartDTOList);
        // 校验商品状态
        List<Long> normalGoodsSkuIds = cartDTOList.stream().filter(t -> PromotionActivityTypeEnum.COMBINATION != PromotionActivityTypeEnum.getByCode(t.getPromotionActivityType())).map(CartDTO::getGoodsSkuId).distinct().collect(Collectors.toList());
        List<Long> allGoodsSkuIds = Stream.of(comGoodsSkuIds, normalGoodsSkuIds).flatMap(Collection::stream).distinct().collect(Collectors.toList());
        List<GoodsSkuStandardBasicDTO> allDistributorGoodsDTOList = goodsApi.batchQueryStandardGoodsBasicBySkuIds(allGoodsSkuIds);

        if (allGoodsSkuIds.size() != allDistributorGoodsDTOList.size()) {
            log.error("{}:商品信息查询不全!",allGoodsSkuIds);
        }
        // 商品信息字典
        Map<Long, GoodsSkuStandardBasicDTO> allDistributorGoodsDTOMap = allDistributorGoodsDTOList.stream().collect(Collectors.toMap(GoodsSkuStandardBasicDTO::getId, Function.identity()));

        //获取购物车最新的配送商商品Id
        cartDTOList.stream().forEach(cartDTO -> {
            GoodsSkuStandardBasicDTO goodsSkuStandardBasicDTO = allDistributorGoodsDTOMap.get(cartDTO.getGoodsSkuId());
            Long goodId = Optional.ofNullable(goodsSkuStandardBasicDTO).map(t -> t.getGoodsId()).orElse(cartDTO.getDistributorGoodsId());
            cartDTO.setDistributorGoodsId(goodId);
        });

        // 秒杀商品集合
        Map<Long, List<PromotionGoodsLimitDTO>> promotionGoodsMap = this.selectSpecialGoodsActivity(cartDTOList, staffInfo.getCurrentEid());
        // 商家优惠劵map
        Map<Long, Long> shopCustomerCouponMap = Maps.newHashMap();
        // 卖家留言
        Map<Long, String> buyerMessageMap = Maps.newHashMap();
        // 支付方式map
        Map<Long, Integer> paymentMethodMap = Maps.newHashMap();
        // 商家支付促销活动map
        Map<Long, Long> shopPaymentActivityMap = Maps.newHashMap();
        // 构建前端传递参数
        this.buildCustomerParam(checkCouponForm, shopCustomerCouponMap, buyerMessageMap, paymentMethodMap,shopPaymentActivityMap);
        // 平台优惠劵
        Long customerPlatformCouponId = Optional.ofNullable(checkCouponForm).map(e -> e.getCustomerPlatformCouponId()).orElse(null);
        // 平台支付促销活动ID
        Long platformPaymentActivityId = Optional.ofNullable(checkCouponForm).map(e -> e.getPlatformPaymentActivityId() ).orElse(null);
        // 配送商字典
        List<EnterpriseDTO> distributorDTOList = enterpriseApi.listByIds(distributorEids);
        Map<Long, EnterpriseDTO> distributorDTOMap = distributorDTOList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, Function.identity()));
        // 以岭企业列表
        List<Long> yilingSubEids = enterpriseApi.listSubEids(Constants.YILING_EID);
        //商品信息
        List<Long> goodsIds = cartDTOList.stream().map(e -> e.getGoodsId()).distinct().collect(Collectors.toList());
        List<Long> distributorGoodsIds = cartDTOList.stream().filter(t -> PromotionActivityTypeEnum.COMBINATION != PromotionActivityTypeEnum.getByCode(t.getPromotionActivityType())).map(e -> e.getDistributorGoodsId()).distinct().collect(Collectors.toList());
        // 组合数据合并
        distributorGoodsIds = Stream.of(distributorGoodsIds, comGoodsIds).flatMap(Collection::stream).distinct().collect(Collectors.toList());
        // 图片地址
        Map<Long, String> pictureMap = goodsApi.getPictureUrlMapByGoodsIds(goodsIds);
        //满配信息
        List<ShopDTO> shopList = shopApi.listShopByEidList(distributorEids);
        Map<Long, ShopDTO> shopMap = shopList.stream().collect(Collectors.toMap(ShopDTO::getShopEid, o -> o, (k1, k2) -> k1));
        // 商品价格字典
        QueryGoodsPriceRequest request = new QueryGoodsPriceRequest();
        request.setCustomerEid(staffInfo.getCurrentEid());
        request.setGoodsIds(distributorGoodsIds);

        Map<Long, GoodsPriceDTO> goodsPriceMap = goodsPriceApi.queryB2bGoodsPriceInfoMap(request);

        if (MapUtil.isEmpty(goodsPriceMap)) {

            return Result.failed("获取B2B价格异常!");
        }

        List<Long> sellerEids = cartDTOList.stream().map(CartDTO::getSellerEid).distinct().collect(Collectors.toList());
        Map<Long, List<PaymentMethodDTO>> paymentMethodDTOMap = paymentMethodApi.listByCustomerEidAndEids(staffInfo.getCurrentEid(), sellerEids, PlatformEnum.B2B);

        // 构建配送商的商品信息全集
        List<OrderDistributorVO> orderDistributorVOList = distributorCartDTOMap.keySet().stream().map(distributorEid -> {
            String distributorName = distributorDTOMap.get(distributorEid).getName();
            List<CartDTO> distributorCartDTOList = distributorCartDTOMap.get(distributorEid);
            List<PaymentMethodDTO> paymentMethodDTOList = paymentMethodDTOMap.get(distributorEid);
            ShopDTO shopOne = shopMap.get(distributorEid);
            Integer choosePaymentMethod = paymentMethodMap.get(distributorEid);
            Long shopCustomerCouponId = shopCustomerCouponMap.get(distributorEid);
            String buyerMessage = buyerMessageMap.get(distributorEid);
            List<PromotionGoodsLimitDTO> promotionGoodsLimitDTOList = promotionGoodsMap.get(distributorEid);

            return this.buildOrderDistributorVo(staffInfo.getCurrentEid(), distributorEid, distributorName, distributorCartDTOList, allDistributorGoodsDTOMap, paymentMethodDTOList, pictureMap, yilingSubEids, choosePaymentMethod, shopCustomerCouponId, goodsPriceMap, shopOne, buyerMessage,promotionGoodsLimitDTOList, comPromotionMap);

        }).collect(Collectors.toList());

        return buildOrderSettlementPageVO(orderDistributorVOList,customerPlatformCouponId,shopCustomerCouponMap,platformPaymentActivityId,shopPaymentActivityMap,comPromotionMap,staffInfo.getCurrentEid(),ObjectUtil.isNull(checkCouponForm));
    }


    private Result<OrderSettlementPageVO> buildOrderSettlementPageVO (List<OrderDistributorVO> orderDistributorVOList,Long customerPlatformCouponId, Map<Long, Long> shopCustomerCouponMap,Long platformPaymentActivityId,Map<Long,Long> shopPaymentActivityMap, Map<Long, PromotionDTO> comPromotionMap,Long currentEid,Boolean isBestCoupon) {

        OrderSettlementPageVO pageVO = new OrderSettlementPageVO();
        pageVO.setPlatfromCouponActivity(Collections.emptyList());
        pageVO.setPlatformCouponCount(0);
        pageVO.setIsUsePlatformCoupon(false);
        pageVO.setPlatformCouponDiscountAmount(BigDecimal.ZERO);
        pageVO.setShopCouponDiscountAmount(BigDecimal.ZERO);
        pageVO.setPlatformPaymentDiscountAmount(BigDecimal.ZERO);
        pageVO.setShopPaymentDiscountAmount(BigDecimal.ZERO);
        pageVO.setPlatformPaymentActivity(Collections.emptyList());
        pageVO.setCustomerPlatformCouponId(customerPlatformCouponId);
        // 配送商商品信息
        pageVO.setOrderDistributorList(orderDistributorVOList);
        // 商品种数
        pageVO.setGoodsSpeciesNum(orderDistributorVOList.stream().mapToLong(OrderDistributorVO::getGoodsSpeciesNum).sum()); // 商品种数
        // 商品件数
        pageVO.setGoodsNum(orderDistributorVOList.stream().mapToLong(OrderDistributorVO::getGoodsNum).sum()); // 商品件数
        // 运费
        pageVO.setFreightAmount(orderDistributorVOList.stream().map(OrderDistributorVO::getFreightAmount).reduce(BigDecimal::add).orElse(BigDecimal.ZERO));
        // 订单总金额
        pageVO.setTotalAmount(orderDistributorVOList.stream().map(OrderDistributorVO::getTotalAmount).reduce(BigDecimal::add).orElse(BigDecimal.ZERO));
        // 设置优惠劵信息
        couponDiscountFunction.calOrderCouponDiscount(currentEid, pageVO,shopCustomerCouponMap, customerPlatformCouponId,isBestCoupon);
        // 设置支付促销活动信息
        paymentDiscountFunction.calPaymentDiscount(currentEid,pageVO,platformPaymentActivityId,shopPaymentActivityMap,comPromotionMap);
        // 取出所有商家优惠金额减平台优惠金额
        pageVO.setPaymentAmount(orderDistributorVOList.stream().map(OrderDistributorVO::getPaymentAmount).reduce(BigDecimal::add).orElse(BigDecimal.ZERO));
        // 减去所有的平台优惠金额
        pageVO.setPaymentAmount(pageVO.getPaymentAmount().subtract(pageVO.getPlatformCouponDiscountAmount()).subtract(pageVO.getPlatformPaymentDiscountAmount()));
        // 用户下单地址信息
        pageVO.setDeliveryAddressVO(this.selectDeliveryAddressInfoList(currentEid));  // 配送地址
        // 校验账期支付的配送订单
        Result checkPaymentDaysOrder = this.checkPaymentDaysOrder(currentEid, orderDistributorVOList);

        if (HttpStatus.HTTP_OK == checkPaymentDaysOrder.getCode()) {

            return Result.success(pageVO);
        }

        return Result.failed(checkPaymentDaysOrder.getMessage());

    }


    /**
     * 校验可控商品
     *
     * @param buyerEid
     * @param gidList
     */
    private void checkB2BGoodsByGids(Long buyerEid, List<Long> gidList) {
        log.info("checkB2BGoodsByGids request->{}", JSON.toJSON(gidList));
        Map<Long, Integer> goodsListResult = agreementBusinessApi.getB2bGoodsLimitByGids(gidList, buyerEid);
        log.info("checkB2BGoodsByGids result->{}", JSON.toJSON(goodsListResult));
        // 控销商品无法结算
        if (MapUtil.isEmpty(goodsListResult)) {
            throw new BusinessException(OrderErrorCode.LIMIT_GOODS_SALE_ERROR);
        }
        // 是否管控商品
        if (goodsListResult.values().stream().anyMatch(value -> GoodsLimitStatusEnum.CONTROL_GOODS == GoodsLimitStatusEnum.getByCode(value))) {
            throw new BusinessException(OrderErrorCode.LIMIT_GOODS_SALE_ERROR);
        }
        // 是否建立采购关系
        if (goodsListResult.values().stream().anyMatch(value -> GoodsLimitStatusEnum.NOT_RELATION_SHIP == GoodsLimitStatusEnum.getByCode(value))) {
            throw new BusinessException(OrderErrorCode.NOT_RELATION_SHIP_ERROR);
        }
    }

    /**
     * 校验账期支付订单，企业账户，账期是否满足
     * @param currentEid 当前企业ID
     * @param orderDistributorVOList 配送商订单信息
     * @return
     */
    private Result  checkPaymentDaysOrder(Long currentEid,List<OrderDistributorVO> orderDistributorVOList) {

        List<OrderDistributorVO> paymentDaysOrders =  orderDistributorVOList.stream().filter(e -> {
            if (CollectionUtil.isEmpty(e.getPaymentMethodList())) {
                return false;
            }
            return e.getPaymentMethodList().stream().filter(t -> t.getSelected() && PaymentMethodEnum.PAYMENT_DAYS == PaymentMethodEnum.getByCode(t.getId().longValue())).findFirst().isPresent();

        }).collect(Collectors.toList());


        if (CollectionUtil.isEmpty(paymentDaysOrders)) {

            return Result.success();
        }

        // 校验账期支付的，账期支付金额是否充足
        for (OrderDistributorVO t : paymentDaysOrders) {

            BigDecimal paymentDaysAvailableAmount = paymentDaysAccountApi.getB2bAvailableAmountByCustomerEid(t.getDistributorEid(), currentEid);

            // 订单应付金额
            if (paymentDaysAvailableAmount.compareTo(NumberUtil.sub(t.getPaymentAmount(),t.getPlatformCouponDiscountMoney())) == -1) {

                return Result.failed("采购商可以选择其它支付方式或者补足额度后再选择该支付方式");
            }
        }

        return Result.success();

    }


    @ApiOperation(value = "订单列表")
    @PostMapping("/list")
    public Result<Page<OrderListVO>> getB2BAppOrder(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid OrderB2BPageForm form) {
        OrderB2BPageRequest request = PojoUtils.map(form, OrderB2BPageRequest.class);
        request.setEid(staffInfo.getCurrentEid());
        Page<OrderDTO> orderList = orderB2BApi.getB2BAppOrder(request);
        Page<OrderListVO> resultPage = PojoUtils.map(orderList, OrderListVO.class);
        List<OrderListVO> result = new ArrayList<>();
        //商品id集合
        List<Long> goodIds = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(orderList.getRecords())) {
            List<Long> ids = orderList.getRecords().stream().map(order -> order.getId()).collect(Collectors.toList());
            List<Long> sellerEid = orderList.getRecords().stream().map(order -> order.getSellerEid()).collect(Collectors.toList());
            List<EnterpriseDTO> enterpriseList = enterpriseApi.listByIds(sellerEid);
            Map<Long, EnterpriseDTO> enterpriseMap = enterpriseList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, o -> o, (k1, k2) -> k1));
            List<OrderDetailDTO> orderDetailList = orderDetailApi.getOrderDetailByOrderIds(ids);
            Map<Long, OrderDetailDTO> detailMap = orderDetailList.stream().collect(Collectors.toMap(OrderDetailDTO::getId, o -> o, (k1, k2) -> k1));
            List<Long> goodsSkuList = orderDetailList.stream().map(o -> o.getGoodsSkuId()).collect(Collectors.toList());
            List<GoodsSkuInfoDTO> goodsSkuInfoList = goodsApi.batchQueryInfoBySkuIds(goodsSkuList);
            Map<Long, String> sellUnitMap = new HashMap<>();
            if (CollectionUtil.isNotEmpty(goodsSkuInfoList)) {
                sellUnitMap = goodsSkuInfoList.stream().collect(Collectors.toMap(GoodsSkuInfoDTO::getId, o -> o.getGoodsInfo().getSellUnit(), (k1, k2) -> k1));

            }

            List<OrderDetailChangeDTO> orderDetailChangeList = orderDetailChangeApi.listByOrderIds(ids);
            Map<Long, List<OrderDetailChangeDTO>> detailChangeMap = new HashMap<>();
            for (OrderDetailChangeDTO changeOne : orderDetailChangeList) {
                if (detailChangeMap.containsKey(changeOne.getOrderId())) {
                    List<OrderDetailChangeDTO> changeList = detailChangeMap.get(changeOne.getOrderId());
                    changeList.add(changeOne);
                } else {
                    detailChangeMap.put(changeOne.getOrderId(), new ArrayList<OrderDetailChangeDTO>() {{
                        add(changeOne);
                    }});
                }
            }
            for (OrderDTO one : orderList.getRecords()) {
                OrderListVO orderResult = getOrderResult(detailChangeMap, one, goodIds, enterpriseMap, detailMap, sellUnitMap);

                Date createTime = one.getCreateTime();
                int createYear = DateUtil.year(createTime);
                int nowYear = DateUtil.year(new Date());
                if (createYear != nowYear) {
                    orderResult.setIsAllowReturn(1);
                    orderResult.setRefuseReturnReason(OrderErrorCode.ORDER_RETURN_OTHER_YEAR.getMessage());
                }
                // 2.该订单存在未审核的退货单，不允许申请退货
                int count = orderReturnApi.countByOrderIdAndStatus(one.getId(), OrderReturnStatusEnum.ORDER_RETURN_PENDING.getCode());
                if (count > 0) {
                    orderResult.setIsAllowReturn(1);
                    orderResult.setRefuseReturnReason(OrderErrorCode.ORDER_RETURN_STATUS_IS_AUDIT_EXIST.getMessage());
                }
                result.add(orderResult);
            }
            Map<Long, String> map = goodsApi.getPictureUrlMapByGoodsIds(goodIds);
            for (OrderListVO one : result) {
                for (OrderGoodsVO goods : one.getGoodsList()) {
                    goods.setGoodPic(pictureUrlUtils.getGoodsPicUrl(map.get(goods.getGoodsId())));
                }
            }
        }
        resultPage.setRecords(result);
        return Result.success(resultPage);
    }

    @ApiOperation(value = "订单详情")
    @GetMapping("/detail")
    public Result<OrderDetailVO> getB2BAppOrder(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam(value = "orderId", required = true) Long orderId) {
        OrderDetailVO result = new OrderDetailVO();
        OrderDTO orderOne = orderB2BApi.getB2BOrderOne(orderId);
        EnterpriseDTO enterpriseDTO = enterpriseApi.getById(orderOne.getSellerEid());
        //待付款设置时间
        if (PaymentMethodEnum.getByCode(Long.valueOf(orderOne.getPaymentMethod())) == PaymentMethodEnum.ONLINE && PaymentStatusEnum.UNPAID == PaymentStatusEnum.getByCode(orderOne.getPaymentStatus())) {
            Date createTime = orderOne.getCreateTime();
            long startTime = createTime.getTime();
            Calendar nowTime = Calendar.getInstance();
            nowTime.add(Calendar.MINUTE, -15);
            long endTime = nowTime.getTime().getTime();
            if (startTime <= endTime) {
                result.setRemainTime("0分钟0秒");
            } else {
                int m = (int) (((startTime - endTime) % 86400000) % 3600000) / 60000;
                int s = (int) ((((startTime - endTime) % 86400000) % 3600000) % 60000) / 1000;
                result.setRemainTime(m + "分钟" + s + "秒");
            }
        }
        if (OrderStatusEnum.DELIVERED == OrderStatusEnum.getByCode(orderOne.getOrderStatus())) {
            long day = DateUtil.betweenDay(orderOne.getDeliveryTime(), new Date(), true);
            if (NumberUtil.compare(7L, day) >= 0) {
                result.setRemainReceiveDay(7L - day);
            } else {
                result.setRemainReceiveDay(0L);
            }
        }
        //地址信息
        OrderAddressDTO addressInfo = orderAddressApi.getOrderAddressInfo(orderId);

        //流转信息
        List<OrderLogDTO> orderLogInfo = orderLogApi.getOrderLogInfo(orderId);

        List<OrderDetailChangeDTO> changeList = orderDetailChangeApi.listByOrderId(orderId);
        List<OrderDetailDTO> orderDetail = orderDetailApi.getOrderDetailInfo(orderId);
        Map<Long, OrderDetailDTO> detailMap = orderDetail.stream().collect(Collectors.toMap(OrderDetailDTO::getId, o -> o, (k1, k2) -> k1));

        List<Long> goodsSkuList = orderDetail.stream().map(o -> o.getGoodsSkuId()).collect(Collectors.toList());
        List<GoodsSkuInfoDTO> goodsSkuInfoList = goodsApi.batchQueryInfoBySkuIds(goodsSkuList);
        Map<Long, String> sellUnitMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(goodsSkuInfoList)) {
            sellUnitMap = goodsSkuInfoList.stream().collect(Collectors.toMap(GoodsSkuInfoDTO::getId, o -> o.getGoodsInfo().getSellUnit(), (k1, k2) -> k1));

        }


        List<OrderDeliveryDTO> orderDeliveryList = orderDeliveryApi.getOrderDeliveryList(orderId);
        Map<Long, List<OrderDeliveryDTO>> deliveryMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(orderDeliveryList)) {
            for (OrderDeliveryDTO one : orderDeliveryList) {
                if (deliveryMap.containsKey(one.getDetailId())) {
                    List<OrderDeliveryDTO> list = deliveryMap.get(one.getDetailId());
                    list.add(one);
                } else {
                    deliveryMap.put(one.getDetailId(), new ArrayList<OrderDeliveryDTO>() {{
                        add(one);
                    }});
                }
            }
        }
        //商品明细信息
        List<OrderGoodsDetailVO> goodsDetailList = new ArrayList<>();
        //实退金额
        BigDecimal realReturnAmount = BigDecimal.ZERO;

        //商品结合id
        List<Long> goodList = new ArrayList<>();
        //商品列表信息
        List<OrderGoodsVO> goodsList = new ArrayList<>();

        //预售支付按钮类型
        Integer paymentNameType = 0;
        if (OrderCategoryEnum.PRESALE == OrderCategoryEnum.getByCode(orderOne.getOrderCategory())) {
            if (PaymentStatusEnum.UNPAID == PaymentStatusEnum.getByCode(orderOne.getPaymentStatus())) {
                paymentNameType = 1;
            } else if (PaymentStatusEnum.PARTPAID == PaymentStatusEnum.getByCode(orderOne.getPaymentStatus())) {
                paymentNameType = 2;
            }
        }

        //支付尾款控制按钮
        Boolean presaleButtonFlag = false;

        //数量
        Integer goodsNumber = 0;
        //组合包数量
        Map<Long, Integer> promotionGoodsMap = new HashMap<>();
        OrderPromotionActivityDTO activityDTO = orderPromotionActivityApi.getOneByOrderIds(orderId);
        if (activityDTO != null) {
            if (PromotionActivityTypeEnum.COMBINATION == PromotionActivityTypeEnum.getByCode(activityDTO.getActivityType())) {
                List<PromotionGoodsLimitDTO> promotionGoodsLimitList = promotionActivityApi.queryGoodsByActivityId(activityDTO.getActivityId());
                //促销的组合包允许购买数量
                promotionGoodsMap = promotionGoodsLimitList.stream().collect(Collectors.toMap(PromotionGoodsLimitDTO::getGoodsSkuId, PromotionGoodsLimitDTO::getAllowBuyCount, (k1, k2) -> k1));
            } else if (PromotionActivityTypeEnum.PRESALE == PromotionActivityTypeEnum.getByCode(activityDTO.getActivityType())) {
                PresaleOrderDTO orderInfo = presaleOrderApi.getOrderInfo(orderId);
                if (orderInfo != null) {
                    result.setDepositAmount(orderInfo.getDepositAmount());
                    result.setBalanceAmount(orderInfo.getBalanceAmount());
                    if (PreSaleActivityTypeEnum.getByCode(orderInfo.getActivityType()) == PreSaleActivityTypeEnum.FULL) {
                        paymentNameType = 2;
                        presaleButtonFlag = true;
                    } else if (PreSaleActivityTypeEnum.getByCode(orderInfo.getActivityType()) == PreSaleActivityTypeEnum.DEPOSIT) {
                        long now = DateUtil.date().getTime();
                        //
                        if ((orderInfo.getBalanceStartTime().getTime() <= now) && (now <= orderInfo.getBalanceEndTime().getTime())) {
                            presaleButtonFlag = true;
                        } else {
                            presaleButtonFlag = false;
                        }
                        if(now <= orderInfo.getBalanceEndTime().getTime()){
                            int d = (int) (((orderInfo.getBalanceEndTime().getTime() - now) / 86400000));
                            int h = (int) (((orderInfo.getBalanceEndTime().getTime() - now) % 86400000) / 3600000);
                            int m = (int) (((orderInfo.getBalanceEndTime().getTime() - now) % 86400000) % 3600000) / 60000;
                            int s = (int) ((((orderInfo.getBalanceEndTime().getTime() - now) % 86400000) % 3600000) % 60000) / 1000;

                            result.setPresaleRemainTime(d+"天"+h+"小时"+m + "分钟" + s + "秒");
                        }else{
                            result.setPresaleRemainTime(0+"天"+0+"小时"+0 + "分钟" + 0 + "秒");
                        }
                    }

                }
            }
        }

        for (OrderDetailChangeDTO changeOne : changeList) {
            OrderDetailDTO detailOne = detailMap.get(changeOne.getDetailId());
            OrderGoodsVO goodsVO = new OrderGoodsVO();
            goodsNumber = goodsNumber + changeOne.getGoodsQuantity();
            goodsVO.setGoodsSkuId(changeOne.getGoodsSkuId());
            goodsVO.setGoodsId(changeOne.getGoodsId());
            goodsVO.setGoodsQuantity(changeOne.getGoodsQuantity());
            goodsVO.setPromotionActivityType(detailOne.getPromotionActivityType());
            goodsVO.setPromotionNumber(promotionGoodsMap.get(detailOne.getGoodsSkuId()));
            goodsVO.setUnit(sellUnitMap.get(changeOne.getGoodsSkuId()));
            goodsList.add(goodsVO);

            OrderGoodsDetailVO goodOne = getOrderGoodsDetailVO(changeOne, detailOne, promotionGoodsMap);
            List<OrderDeliveryDTO> deliveryList = deliveryMap.get(changeOne.getDetailId());
            Integer deliveryAllQuantity = 0;
            List<OrderDeliveryVO> deliveryVoList = new ArrayList<>();
            if (CollectionUtil.isNotEmpty(deliveryList)) {
                for (OrderDeliveryDTO one : deliveryList) {
                    deliveryAllQuantity = deliveryAllQuantity + one.getDeliveryQuantity();
                    OrderDeliveryVO vo = PojoUtils.map(one, OrderDeliveryVO.class);
                    List<OrderReturnDetailBatchDTO> orderReturnDetailBatchList = orderReturnDetailBatchApi.queryByDetailIdAndBatchNoAndType(one.getDetailId(), one.getBatchNo(), null);
                    Integer returnQuantity = 0;
                    if (CollectionUtil.isNotEmpty(orderReturnDetailBatchList)) {
                        for (OrderReturnDetailBatchDTO returnOne : orderReturnDetailBatchList) {
                            returnQuantity = returnQuantity + returnOne.getReturnQuantity();
                        }
                    }
                    vo.setReturnQuantity(returnQuantity);
                    deliveryVoList.add(vo);
                }
            }
            goodOne.setUnit(sellUnitMap.get(changeOne.getGoodsSkuId()));
            goodOne.setDeliveryList(deliveryVoList);
            goodOne.setDeliveryAllQuantity(deliveryAllQuantity);
            goodsDetailList.add(goodOne);

            goodList.add(detailOne.getGoodsId());
            realReturnAmount = changeOne.getReturnAmount().add(changeOne.getSellerReturnAmount())
                    .subtract(changeOne.getSellerPlatformCouponDiscountAmount()).subtract(changeOne.getSellerCouponDiscountAmount())
                    .subtract(changeOne.getReturnPlatformCouponDiscountAmount()).subtract(changeOne.getReturnCouponDiscountAmount())
                    .subtract(changeOne.getSellerPlatformPaymentDiscountAmount()).subtract(changeOne.getSellerShopPaymentDiscountAmount())
                    .subtract(changeOne.getReturnPlatformPaymentDiscountAmount()).subtract(changeOne.getReturnShopPaymentDiscountAmount());
        }

        Map<Long, String> map = goodsApi.getPictureUrlMapByGoodsIds(goodList);

        for (OrderGoodsDetailVO one : goodsDetailList) {
            one.setGoodPic(pictureUrlUtils.getGoodsPicUrl(map.get(one.getGoodsId())));
        }

        for (OrderGoodsVO one : goodsList) {
            one.setGoodPic(pictureUrlUtils.getGoodsPicUrl(map.get(one.getGoodsId())));
        }

        if (OrderStatusEnum.RECEIVED == OrderStatusEnum.getByCode(orderOne.getOrderStatus()) && PaymentMethodEnum.PAYMENT_DAYS == PaymentMethodEnum.getByCode(Long.valueOf(orderOne.getPaymentMethod()))) {
            PaymentDaysOrderDTO paymentAccountDetailOne = paymentDaysAccountApi.getPaymentDaysOrderOne(orderOne.getId());
            if (paymentAccountDetailOne != null) {
                result.setRepaymentStatus(orderOne.getPaymentStatus());
                result.setRepaymentAmount(paymentAccountDetailOne.getRepaymentAmount());
                result.setRepaymentTime(paymentAccountDetailOne.getRepaymentTime());
                result.setStayPaymentAmount(paymentAccountDetailOne.getUsedAmount().subtract(paymentAccountDetailOne.getReturnAmount()));
                result.setRepaymentTime(paymentAccountDetailOne.getRepaymentTime());
            }
        }

        Date createTime = orderOne.getCreateTime();
        int createYear = DateUtil.year(createTime);
        int nowYear = DateUtil.year(new Date());
        if (createYear != nowYear) {
            result.setIsAllowReturn(1);
            result.setRefuseReturnReason(OrderErrorCode.ORDER_RETURN_OTHER_YEAR.getMessage());
        }
        // 2.该订单存在未审核的退货单，不允许申请退货
        int count = orderReturnApi.countByOrderIdAndStatus(orderOne.getId(), OrderReturnStatusEnum.ORDER_RETURN_PENDING.getCode());
        if (count > 0) {
            result.setIsAllowReturn(1);
            result.setRefuseReturnReason(OrderErrorCode.ORDER_RETURN_STATUS_IS_AUDIT_EXIST.getMessage());
        }
        result.setGoodsType(changeList.size());
        result.setGoodsNumber(goodsNumber);
        result.setOrderLogInfo(PojoUtils.map(orderLogInfo, OrderLogVO.class));
        result.setOrderAddressVO(PojoUtils.map(addressInfo, OrderAddressVO.class));
        result.setOrderNote(orderOne.getOrderNote());
        result.setCouponDiscountAmount(orderOne.getCouponDiscountAmount());
        result.setPlatformCouponDiscountAmount(orderOne.getPlatformCouponDiscountAmount());
        result.setPresaleDiscountAmount(orderOne.getPresaleDiscountAmount());
        result.setShopPaymentDiscountAmount(orderOne.getShopPaymentDiscountAmount());
        result.setPlatformPaymentDiscountAmount(orderOne.getPlatformPaymentDiscountAmount());
        result.setTotalAmount(orderOne.getTotalAmount());
        result.setPayAmount(orderOne.getPaymentAmount());
        result.setGoodsDetailList(goodsDetailList);
        result.setRealReturnAmount(realReturnAmount);
        result.setGoodsList(goodsList);
        result.setCreateTime(orderOne.getCreateTime());
        result.setOrderNo(orderOne.getOrderNo());
        result.setSellerEid(orderOne.getSellerEid());
        result.setSellerEname(orderOne.getSellerEname());
        orderOne.setBuyerEname(orderOne.getBuyerEname());
        orderOne.setBuyerEname(orderOne.getBuyerEname());
        Integer status = getOrderStatus(orderOne);
        Boolean cancelButtonFlag = false;
        if (status.compareTo(1) == 0) {
            if(PaymentStatusEnum.PARTPAID == PaymentStatusEnum.getByCode(orderOne.getPaymentStatus())){
                cancelButtonFlag = false;
            }else{
                cancelButtonFlag = true;
            }

        } else if (status.compareTo(2) == 0 && OrderStatusEnum.UNDELIVERED == OrderStatusEnum.getByCode(orderOne.getOrderStatus()) ) {
            if (enterpriseDTO.getErpSyncLevel().compareTo(ErpSyncLevelEnum.BASED_DOCKING.getCode()) <= 0
                    && (OrderErpPushStatusEnum.getByCode(orderOne.getErpPushStatus()) == OrderErpPushStatusEnum.NOT_PUSH || OrderErpPushStatusEnum.getByCode(orderOne.getErpPushStatus()) == OrderErpPushStatusEnum.PUSH_FAIL)&&
                    OrderCategoryEnum.NORMAL == OrderCategoryEnum.getByCode(orderOne.getOrderCategory())) {

                cancelButtonFlag = true;
            }

        }
        result.setOrderCategory(orderOne.getOrderCategory());
        result.setPresaleButtonFlag(presaleButtonFlag);
        result.setPaymentNameType(paymentNameType);
        result.setStatus(status);
        result.setCancelButtonFlag(cancelButtonFlag);
        result.setAgainBuyButtonFlag(getAgainBuyButtonFlag(orderOne));

        return Result.success(result);
    }


    /**
     * 判断订单是是否支持再购买的权限按钮
     * @param orderOne
     * @return
     */
    private Boolean getAgainBuyButtonFlag (OrderDTO orderOne) {

        // 是否展示再次购买显示按钮
        EnumSet<OrderStatusEnum>  againBuyOrderStatus = EnumSet.of(OrderStatusEnum.CANCELED,OrderStatusEnum.FINISHED,OrderStatusEnum.DELIVERED,OrderStatusEnum.PARTDELIVERED,OrderStatusEnum.RECEIVED);

        if (againBuyOrderStatus.contains(OrderStatusEnum.getByCode(orderOne.getOrderStatus()))) {
            return true;
        }

        return false;
    }

    private Integer getOrderStatus(OrderDTO orderOne) {
        Integer status = 0;
        if (PaymentMethodEnum.getByCode(Long.valueOf(orderOne.getPaymentMethod())) == PaymentMethodEnum.ONLINE && PaymentStatusEnum.PAID != PaymentStatusEnum.getByCode(orderOne.getPaymentStatus()) && OrderStatusEnum.UNAUDITED == OrderStatusEnum.getByCode(orderOne.getOrderStatus())) {
            status = 1;
        } else if (OrderStatusEnum.UNDELIVERED == OrderStatusEnum.getByCode(orderOne.getOrderStatus())) {

            status = 2;
        } else if (OrderStatusEnum.DELIVERED == OrderStatusEnum.getByCode(orderOne.getOrderStatus())) {
            status = 3;
        } else if (orderOne.getOrderStatus() >= OrderStatusEnum.RECEIVED.getCode()) {
            status = 4;
        } else if (OrderStatusEnum.CANCELED == OrderStatusEnum.getByCode(orderOne.getOrderStatus())) {
            status = 5;
        } else if(OrderStatusEnum.PARTDELIVERED == OrderStatusEnum.getByCode(orderOne.getOrderStatus())){
            status = 2;
        }
        return status;
    }

    private OrderGoodsDetailVO getOrderGoodsDetailVO(OrderDetailChangeDTO changeOne, OrderDetailDTO detailOne, Map<Long, Integer> promotionGoodsMap) {

        OrderGoodsDetailVO goodOne = new OrderGoodsDetailVO();
        goodOne.setGoodsName(detailOne.getGoodsName());
        goodOne.setGoodsLicenseNo(detailOne.getGoodsLicenseNo());
        goodOne.setGoodsSpecification(detailOne.getGoodsSpecification());
        goodOne.setGoodsManufacturer(detailOne.getGoodsManufacturer());
        goodOne.setGoodsPrice(changeOne.getGoodsPrice());
        goodOne.setGoodsQuantity(changeOne.getGoodsQuantity());
        goodOne.setGoodsSkuId(changeOne.getGoodsSkuId());
        goodOne.setGoodsId(detailOne.getGoodsId());
        goodOne.setDetailId(detailOne.getId());
        goodOne.setPromotionNumber(promotionGoodsMap.get(changeOne.getGoodsSkuId()));
        goodOne.setPromotionActivityType(detailOne.getPromotionActivityType());
        return goodOne;
    }

    @ApiOperation(value = "取消订单")
    @GetMapping("/cancel")
    public Result cancel(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam(value = "orderId") Long orderId) {

        Boolean result = orderProcessApi.cancel(orderId, staffInfo.getCurrentUserId());
        return Result.success(result);
    }

    @ApiOperation(value = "收货")
    @GetMapping("/receive")
    public Result receive(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam(value = "orderId") Long orderId) {

        Boolean result = orderProcessApi.B2BReceive(orderId, staffInfo.getCurrentUserId(), staffInfo.getCurrentEid());
        return Result.success(result);
    }

    @ApiOperation(value = "删除订单")
    @GetMapping("/hide")
    public Result hide(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam(value = "orderId") Long orderId) {
        Boolean result = orderApi.hideB2BOrder(orderId);
        return Result.success(result);
    }

    private OrderListVO getOrderResult(Map<Long, List<OrderDetailChangeDTO>> detailChangeMap, OrderDTO one, List<Long> goodIds, Map<Long, EnterpriseDTO> enterpriseMap, Map<Long, OrderDetailDTO> detailMap, Map<Long, String> sellUnitMap) {

        EnterpriseDTO enterpriseDTO = enterpriseMap.get(one.getSellerEid());
        List<OrderDetailChangeDTO> changeList = detailChangeMap.get(one.getId());
        OrderListVO orderOne = new OrderListVO();
        orderOne.setOrderNo(one.getOrderNo());
        Boolean cancelButtonFlag = false;
        //状态
        Integer status = 0;
        if (OrderStatusEnum.UNAUDITED == OrderStatusEnum.getByCode(one.getOrderStatus()) && PaymentMethodEnum.getByCode(Long.valueOf(one.getPaymentMethod())) == PaymentMethodEnum.ONLINE && PaymentStatusEnum.PAID != PaymentStatusEnum.getByCode(one.getPaymentStatus())) {
            status = 1;
            if( PaymentStatusEnum.UNPAID == PaymentStatusEnum.getByCode(one.getPaymentStatus())){
                cancelButtonFlag = true;
            }else{
                cancelButtonFlag = false;
            }


        } else if (OrderStatusEnum.UNDELIVERED == OrderStatusEnum.getByCode(one.getOrderStatus())) {

            if (enterpriseDTO != null && (enterpriseDTO.getErpSyncLevel().compareTo(ErpSyncLevelEnum.BASED_DOCKING.getCode()) <= 0) &&
                    (OrderErpPushStatusEnum.getByCode(one.getErpPushStatus()) == OrderErpPushStatusEnum.NOT_PUSH || OrderErpPushStatusEnum.getByCode(one.getErpPushStatus()) == OrderErpPushStatusEnum.PUSH_FAIL) &&
                    OrderCategoryEnum.NORMAL == OrderCategoryEnum.getByCode(one.getOrderCategory())
            ) {
                cancelButtonFlag = true;

            }

            status = 2;
        } else if (OrderStatusEnum.DELIVERED == OrderStatusEnum.getByCode(one.getOrderStatus())) {
            status = 3;
        } else if (one.getOrderStatus() >= OrderStatusEnum.RECEIVED.getCode()) {
            status = 4;
        } else if (OrderStatusEnum.CANCELED == OrderStatusEnum.getByCode(one.getOrderStatus())) {
            status = 5;
        } else if(OrderStatusEnum.PARTDELIVERED == OrderStatusEnum.getByCode(one.getOrderStatus())){
            status = 2;
        }
        //数量
        Integer goodsNumber = 0;
        List<OrderGoodsVO> goodsList = new ArrayList<>();
        OrderPromotionActivityDTO activityDTO = orderPromotionActivityApi.getOneByOrderIds(one.getId());

        //预售支付按钮类型
        Integer paymentNameType = 0;
        if (OrderCategoryEnum.PRESALE == OrderCategoryEnum.getByCode(one.getOrderCategory())) {
            if (PaymentStatusEnum.UNPAID == PaymentStatusEnum.getByCode(one.getPaymentStatus())) {
                paymentNameType = 1;
            } else if (PaymentStatusEnum.PARTPAID == PaymentStatusEnum.getByCode(one.getPaymentStatus())) {
                paymentNameType = 2;
            }
        }

        //支付尾款控制按钮
        Boolean presaleButtonFlag = false;

        Map<Long, Integer> promotionGoodsMap = new HashMap<>();
        if (activityDTO != null) {
            if (PromotionActivityTypeEnum.COMBINATION == PromotionActivityTypeEnum.getByCode(activityDTO.getActivityType())) {
                List<PromotionGoodsLimitDTO> promotionGoodsLimitList = promotionActivityApi.queryGoodsByActivityId(activityDTO.getActivityId());
                //促销的组合包允许购买数量
                promotionGoodsMap = promotionGoodsLimitList.stream().collect(Collectors.toMap(PromotionGoodsLimitDTO::getGoodsSkuId, PromotionGoodsLimitDTO::getAllowBuyCount, (k1, k2) -> k1));
            } else if (PromotionActivityTypeEnum.PRESALE == PromotionActivityTypeEnum.getByCode(activityDTO.getActivityType())) {
                PresaleOrderDTO presaleOrder = presaleOrderApi.getOrderInfo(one.getId());
                if (presaleOrder != null) {
                    //判断类型为全款预售
                    orderOne.setDepositAmount(presaleOrder.getDepositAmount());
                    orderOne.setBalanceAmount(presaleOrder.getBalanceAmount());
                    if (PreSaleActivityTypeEnum.getByCode(presaleOrder.getActivityType()) == PreSaleActivityTypeEnum.FULL) {
                        paymentNameType = 2;
                        presaleButtonFlag = true;
                    } else if (PreSaleActivityTypeEnum.getByCode(presaleOrder.getActivityType()) == PreSaleActivityTypeEnum.DEPOSIT) {
                        long now = System.currentTimeMillis();
                        //
                        if ((presaleOrder.getBalanceStartTime().getTime() <= now) && (now <= presaleOrder.getBalanceEndTime().getTime())) {
                            presaleButtonFlag = true;
                        } else {
                            presaleButtonFlag = false;
                        }
                    }
                }
            }

        }
        for (OrderDetailChangeDTO changeOne : changeList) {
            goodsNumber = goodsNumber + changeOne.getGoodsQuantity();
            OrderDetailDTO detailDTO = detailMap.get(changeOne.getDetailId());
            OrderGoodsVO goodOne = new OrderGoodsVO();
            goodOne.setGoodsSkuId(changeOne.getGoodsSkuId());
            goodOne.setPromotionNumber(promotionGoodsMap.get(changeOne.getGoodsSkuId()));
            goodOne.setGoodsId(changeOne.getGoodsId());
            goodOne.setGoodsQuantity(changeOne.getGoodsQuantity());
            goodOne.setPromotionActivityType(detailDTO.getPromotionActivityType());
            goodOne.setUnit(sellUnitMap.get(changeOne.getGoodsSkuId()));
            goodsList.add(goodOne);
            goodIds.add(changeOne.getGoodsId());
        }


        orderOne.setStatus(status);
        orderOne.setSellerEname(one.getSellerEname());
        orderOne.setCreateTime(one.getCreateTime());
        orderOne.setPayAmount(one.getPaymentAmount());
        orderOne.setGoodsType(changeList.size());
        orderOne.setGoodsNumber(goodsNumber);
        orderOne.setGoodsList(goodsList);
        orderOne.setCancelButtonFlag(cancelButtonFlag);
        orderOne.setId(one.getId());
        orderOne.setBuyerEid(one.getBuyerEid());
        orderOne.setSellerEid(one.getSellerEid());
        orderOne.setBuyerEname(one.getBuyerEname());
        orderOne.setSellerEname(one.getSellerEname());
        orderOne.setPaymentNameType(paymentNameType);
        orderOne.setPresaleButtonFlag(presaleButtonFlag);
        orderOne.setOrderCategory(one.getOrderCategory());
        orderOne.setAgainBuyButtonFlag(getAgainBuyButtonFlag(one));

        return orderOne;
    }


    /**
     * 订单购物车确认是否有赠品
     * @param request
     */
    private void isOrderGift(OrderSubmitRequest request) {

        String userSessionId = StringUtils.join("order:submit:session:",request.getBuyerEid());
        Object userSessionInfo = redisService.get(userSessionId);
        if (ObjectUtil.isNull(userSessionInfo)) {
            return;
        }
        List<String> distributorStrList =  ListUtil.toList(StrUtil.split(userSessionInfo.toString(),Constants.SEPARATOR_UNDERLINE));
        request.getDistributorOrderList().forEach(t -> {
            if (distributorStrList.contains(t.getDistributorEid().toString())) {
                t.setIsHasGift(false);
            } else {
                t.setIsHasGift(true);
            }
        });

    }

    @ApiOperation(value = "用户提交订单")
    @PostMapping("/submit")
    @UserAccessAuthentication(checkEnterpriseAuthStatus = true, checkEnterpriseStatus = true, checkUserEnterpriseRelation = true, checkUserStatus = true)
    public Result<SubmitResultVO> submit(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid OrderSubmitForm form) {

        // 老版本字段名字去错,换成新的名字,并兼容
        Long platformCustomerCouponId = form.getPlatformCustomerCouponId() != null && form.getPlatformCustomerCouponId()  != 0 ? form.getPlatformCustomerCouponId() : form.getPlatfromCustomerCouponId();

        UserAgent userAgent = UserAgentUtil.parse(httpRequest.getHeader("User-Agent"));
        OrderSubmitRequest request = PojoUtils.map(form, OrderSubmitRequest.class);
        request.setOrderTypeEnum(OrderTypeEnum.B2B);
        request.setOrderSourceEnum(OrderSourceEnum.B2B_APP);
        request.setBuyerEid(staffInfo.getCurrentEid());
        request.setOpUserId(staffInfo.getCurrentUserId());
        //   request.setContacterId(staffInfo.getCurrentUserId());
        request.setPlatformCustomerCouponId(platformCustomerCouponId);
        request.setIpAddress(IPUtils.getIp(httpRequest));
        request.setUserAgent(JSONObject.toJSONString(userAgent));
        isOrderGift(request);
        String lockName = RedisKey.generate("order", "submit",request.getBuyerEid().toString());
        String lockId = "";
        OrderSubmitBO submitBO = null;
        try {
            lockId = redisDistributedLock.lock2(lockName, 60, 60, TimeUnit.SECONDS);
            submitBO = orderProcessApi.submit(request);
        } finally {
            redisDistributedLock.releaseLock(lockName,lockId);
        }
        // 在线支付订单
        List<OrderDTO> onlineOrderList = submitBO.getOrderDTOList().stream().filter(e -> PaymentStatusEnum.getByCode(e.getPaymentStatus()) == PaymentStatusEnum.UNPAID && PaymentMethodEnum.ONLINE == PaymentMethodEnum.getByCode(e.getPaymentMethod().longValue())).collect(Collectors.toList());

        Boolean hasOnlinePay = false;
        BigDecimal payMoney = BigDecimal.ZERO;

        if (CollectionUtil.isNotEmpty(onlineOrderList)) {
            hasOnlinePay = true;
            payMoney = onlineOrderList.stream().map(OrderDTO::getPaymentAmount).reduce(BigDecimal::add).get();
        }

        List<String> orderNos = submitBO.getOrderDTOList().stream().map(OrderDTO::getOrderNo).collect(Collectors.toList());
        SubmitResultVO resultVO = SubmitResultVO.builder().orderCodeList(orderNos).payMoney(payMoney).hasOnlinePay(hasOnlinePay).payId(submitBO.getPayId()).build();

        return Result.success(resultVO);
    }


    @Deprecated
    @ApiOperation(value = "去支付-已作废(迁移到支付平台接口文档)")
    @PostMapping("/pay")
    @UserAccessAuthentication(checkEnterpriseAuthStatus = true, checkEnterpriseStatus = true, checkUserEnterpriseRelation = true, checkUserStatus = true)
    public Result<String> repaymentBatch(@CurrentUser CurrentStaffInfo staffInfo, @Valid @RequestBody OrderB2BAppPayAmountForm form) {

        StringBuilder builder = new StringBuilder();
        builder.append("订单包含");
        builder.append("1笔在线支付 ");
        CreatePayOrderRequest payOrderRequest = new CreatePayOrderRequest();
        List<CreatePayOrderRequest.appOrderRequest> appOrderList = new ArrayList<>();
        CreatePayOrderRequest.appOrderRequest request = new CreatePayOrderRequest.appOrderRequest();
        request.setUserId(staffInfo.getCurrentUserId());
        request.setAppOrderId(form.getOrderId());
        OrderDTO order = orderB2BApi.getB2BOrderOne(form.getOrderId());
        if (order == null) {
            throw new BusinessException(OrderErrorCode.ORDER_NOT_EXISTS);
        }
        if( OrderCategoryEnum.PRESALE ==  OrderCategoryEnum.getByCode(order.getOrderCategory())){
            throw new BusinessException(OrderErrorCode.ORDER_CATEGORY_NOT_PAY);
        }
        request.setAppOrderNo(order.getOrderNo());
        request.setAmount(order.getPaymentAmount());
        request.setBuyerEid(staffInfo.getCurrentEid());
        request.setSellerEid(form.getSellerEid());
        appOrderList.add(request);
        payOrderRequest.setAppOrderList(appOrderList);
        payOrderRequest.setTradeType(TradeTypeEnum.PAY);
        payOrderRequest.setContent(builder.toString());
        payOrderRequest.setAppOrderList(appOrderList);
        payOrderRequest.setOpUserId(staffInfo.getCurrentUserId());
        Result<String> createResult = payApi.createPayOrder(payOrderRequest);
        return createResult;
    }


    @ApiOperation(value = "购物车生成结算页信息")
    @UserAccessAuthentication(checkEnterpriseAuthStatus = true, checkEnterpriseStatus = true, checkUserEnterpriseRelation = true, checkUserStatus = true)
    @ApiResponses({
            @ApiResponse(code = 10361, message = "XX商品超出限购!!"),
            @ApiResponse(code = 10363, message = "组合包XX超出限购")
    })
    @RequestMapping(path = "verification", method = { org.springframework.web.bind.annotation.RequestMethod.POST })
    public Result<Void> verification(@CurrentUser CurrentStaffInfo staffInfo,@Valid @RequestBody B2BOrderVerificationForm form ) {
        List<CartDTO> cartDTOList = cartApi.listByBuyerEid(staffInfo.getCurrentEid(), PlatformEnum.B2B, CartGoodsSourceEnum.B2B, CartIncludeEnum.SELECTED);

        if (CollUtil.isEmpty(cartDTOList)) {

            return Result.failed("进货单中无选中商品");
        }

        // 组合商品skuId信息
        List<Long> comGoodsSkuIds = Lists.newArrayList();
        // 组合商品goodId信息
        List<Long> comGoodsIds = Lists.newArrayList();
        // 查询组合商品信息
        Map<Long, PromotionDTO> comPromotionMap = this.getCombinationPromotionDTOMap(staffInfo, comGoodsSkuIds, comGoodsIds, cartDTOList);
        // 验证组合包状态
        this.validateCombinationOrderLimit(cartDTOList, comPromotionMap);
        // 校验商品状态
        List<Long> normalGoodsSkuIds = cartDTOList.stream().filter(t -> PromotionActivityTypeEnum.COMBINATION != PromotionActivityTypeEnum.getByCode(t.getPromotionActivityType())).map(CartDTO::getGoodsSkuId).distinct().collect(Collectors.toList());
        List<Long> allGoodsSkuIds = Stream.of(comGoodsSkuIds, normalGoodsSkuIds).flatMap(Collection::stream).distinct().collect(Collectors.toList());
        List<GoodsSkuStandardBasicDTO> allDistributorGoodsDTOList = goodsApi.batchQueryStandardGoodsBasicBySkuIds(allGoodsSkuIds);
        Optional<GoodsSkuStandardBasicDTO> optional = allDistributorGoodsDTOList.stream().filter(e -> GoodsStatusEnum.getByCode(e.getStandardGoodsBasic().getGoodsStatus()) != GoodsStatusEnum.UP_SHELF).findAny();

        if (optional.isPresent()) {
            return Result.failed(OrderErrorCode.SUBMIT_GOODS_OFF_SHELF);
        }

        Optional<GoodsSkuStandardBasicDTO> checkDisable = allDistributorGoodsDTOList.stream().filter(e -> !GoodsSkuStatusEnum.NORMAL.getCode().equals(e.getStatus())).findAny();
        if (checkDisable.isPresent()) {
            return Result.failed(OrderErrorCode.SUBMIT_GOODS_DISABLE);
        }
        if (allGoodsSkuIds.size() != allDistributorGoodsDTOList.size()) {
            log.error("{}:商品信息查询不全!",allGoodsSkuIds);
        }
        // 商品信息字典
        Map<Long, GoodsSkuStandardBasicDTO> allDistributorGoodsDTOMap = allDistributorGoodsDTOList.stream().collect(Collectors.toMap(GoodsSkuStandardBasicDTO::getId, Function.identity()));
        //获取购物车最新的配送商商品Id
        cartDTOList.stream().forEach(cartDTO -> {
            GoodsSkuStandardBasicDTO goodsSkuStandardBasicDTO = allDistributorGoodsDTOMap.get(cartDTO.getGoodsSkuId());
            Long goodId = Optional.ofNullable(goodsSkuStandardBasicDTO).map(t -> t.getGoodsId()).orElse(cartDTO.getDistributorGoodsId());
            cartDTO.setDistributorGoodsId(goodId);
        });

        List<Long> distributorGoodsIds = cartDTOList.stream().filter(t -> PromotionActivityTypeEnum.COMBINATION != PromotionActivityTypeEnum.getByCode(t.getPromotionActivityType())).map(e -> e.getDistributorGoodsId()).distinct().collect(Collectors.toList());
        // 组合数据合并
        distributorGoodsIds = Stream.of(distributorGoodsIds, comGoodsIds).flatMap(Collection::stream).distinct().collect(Collectors.toList());
        // 校验库存是否充足
        this.validateGoodsInventory(allGoodsSkuIds, cartDTOList, comPromotionMap);
        // 校验商品限购约束
        this.validateGoodsLimit(staffInfo.getCurrentEid(),cartDTOList,allDistributorGoodsDTOList);
        // 校验管控商品
        this.checkB2BGoodsByGids(staffInfo.getCurrentEid(), distributorGoodsIds);

        // 没有赠品的会话信息
        String userSessionInfo = form.getDistributorList().stream().filter(t -> !t.getHasGift()).map(t -> t.getDistributorEid().toString()).collect(Collectors.joining(Constants.SEPARATOR_UNDERLINE));

        if (StringUtils.isNotBlank(userSessionInfo)) {
            String userSessionId = StringUtils.join("order:submit:session:",staffInfo.getCurrentEid());
            // 设置用户会话信息 60分钟
            redisService.set(userSessionId, userSessionInfo,60 * 60 );
        }

        return Result.success();
    }


    /**
     * 获取组合包活动信息
     * @param staffInfo
     * @param comGoodsSkuIds
     * @param comGoodsIds
     * @param cartDTOList
     * @return
     */
    private Map<Long, PromotionDTO> getCombinationPromotionDTOMap(@CurrentUser CurrentStaffInfo staffInfo, List<Long> comGoodsSkuIds, List<Long> comGoodsIds, List<CartDTO> cartDTOList) {

        List<CartDTO> combinationCartList = cartDTOList.stream().filter(t -> PromotionActivityTypeEnum.COMBINATION == PromotionActivityTypeEnum.getByCode(t.getPromotionActivityType())).collect(Collectors.toList());

        if (CollectionUtil.isEmpty(combinationCartList)) {

            return MapUtil.empty();
        }

        // 查询组合商品信息
        PromotionActivityRequest promotionActivityRequest = new PromotionActivityRequest();
        promotionActivityRequest.setBuyerEid(staffInfo.getCurrentEid());
        promotionActivityRequest.setGoodsPromotionActivityIdList(ListUtil.toList(combinationCartList.stream().map(t -> t.getPromotionActivityId()).collect(Collectors.toList())));
        List<PromotionDTO> promotionDTOS = promotionActivityApi.queryPromotionInfoByActivityAndBuyerId(promotionActivityRequest);

        if (log.isDebugEnabled()) {

            log.debug("查询组合促销活动信息,入参:{},返回参数:{}",promotionActivityRequest,promotionDTOS);
        }

        if (CollectionUtil.isEmpty(promotionDTOS)) {

            log.warn("未查询到组合促销商品信息");

            return MapUtil.empty();
        }

        return  promotionDTOS.stream().peek(t -> {
            comGoodsSkuIds.addAll(t.getGoodsLimitDTOList().stream().map(z -> z.getGoodsSkuId()).distinct().collect(Collectors.toList()));
            comGoodsIds.addAll(t.getGoodsLimitDTOList().stream().map(z -> z.getGoodsId()).distinct().collect(Collectors.toList()));
        }).collect(Collectors.toMap(t -> t.getPromotionCombinationPackDTO().getPromotionActivityId(), Function.identity()));
    }


    /**
     * 判断用户限制规则
     * @param buyerEid
     * @param allCartDOList
     */
    private void validateGoodsLimit(Long buyerEid,List<CartDTO> allCartDOList,List<GoodsSkuStandardBasicDTO> allGoodsDTOList) {
        List<CartDTO> normalCartDoList = allCartDOList.stream().filter(t -> PromotionActivityTypeEnum.NORMAL == PromotionActivityTypeEnum.getByCode(t.getPromotionActivityType())).collect(Collectors.toList());

        if (CollectionUtil.isEmpty(normalCartDoList)) {
            return;
        }

        long start1 = System.currentTimeMillis();
        QueryGoodsPurchaseRestrictionRequest restrictionRequest = new QueryGoodsPurchaseRestrictionRequest();
        restrictionRequest.setCustomerEid(buyerEid);
        restrictionRequest.setGoodsIdList(normalCartDoList.stream().map(t -> t.getDistributorGoodsId()).collect(Collectors.toList()));
        List<GoodsPurchaseRestrictionDTO> goodsPurchaseRestrictionDTOS = goodsPurchaseRestrictionApi.queryValidPurchaseRestriction(restrictionRequest);
        if (log.isDebugEnabled()) {
            log.debug("调用商品接口查询商品限购规则耗时：{}，入参：{},返回:[{}]",(System.currentTimeMillis() - start1),restrictionRequest,JSON.toJSONString(goodsPurchaseRestrictionDTOS));
        }
        if (CollectionUtil.isEmpty(goodsPurchaseRestrictionDTOS)) {
            return;
        }
        Set<Long> limitGoodSet = goodsPurchaseRestrictionDTOS.stream().map(t -> t.getGoodsId()).collect(Collectors.toSet());
        Map<Long, GoodsPurchaseRestrictionDTO> purchaseRestrictionDTOMap = goodsPurchaseRestrictionDTOS.stream().collect(Collectors.toMap(t -> t.getGoodsId(), Function.identity()));
        Map<Long, List<GoodsSkuStandardBasicDTO>> goodsInfoMap = allGoodsDTOList.stream().collect(Collectors.groupingBy(t -> t.getGoodsId()));
        Map<Long, Integer> distributorGoodsQtyMap = normalCartDoList.stream().filter(t -> limitGoodSet.contains(t.getDistributorGoodsId())).collect(Collectors.groupingBy(t -> t.getDistributorGoodsId(), Collectors.summingInt(t -> t.getQuantity())));

        // 校验订单,按照单购买是否符合限制规则
        for (Map.Entry<Long, Integer>  entry : distributorGoodsQtyMap.entrySet()) {
            GoodsPurchaseRestrictionDTO goodsPurchaseRestrictionDTO = purchaseRestrictionDTOMap.get(entry.getKey());
            if (goodsPurchaseRestrictionDTO.getOrderRestrictionQuantity() != 0 && CompareUtil.compare(goodsPurchaseRestrictionDTO.getOrderRestrictionQuantity(),entry.getValue().longValue()) < 0) {

                throw limitException(goodsInfoMap,entry.getKey());
            }
        }

        // 过滤出有限制条件的,如果全部都没有限制,无需校验限制规则
        List<QueryUserBuyNumberRequest> queryUserBuyNumberRequests = goodsPurchaseRestrictionDTOS.stream().filter(t -> t.getTimeRestrictionQuantity() != 0 ).map(t -> {
            QueryUserBuyNumberRequest queryUserBuyNumberRequest = new QueryUserBuyNumberRequest();
            queryUserBuyNumberRequest.setGoodId(t.getGoodsId());
            queryUserBuyNumberRequest.setBuyerEid(buyerEid);
            queryUserBuyNumberRequest.setStartTime(t.getStartTime());
            queryUserBuyNumberRequest.setEndTime(t.getEndTime());
            queryUserBuyNumberRequest.setSelectRuleEnum(QueryUserBuyNumberRequest.SelectRuleEnum.getByCode(t.getTimeType()));
            return queryUserBuyNumberRequest;

        }).collect(Collectors.toList());

        // 没有需要校验,时间段限购的规则
        if (CollectionUtil.isEmpty(queryUserBuyNumberRequests)) {
            return;
        }

        long start2 = System.currentTimeMillis();

        Map<Long, Long> userBuyNumberMap =  orderApi.getUserBuyNumber(queryUserBuyNumberRequests);

        if (log.isDebugEnabled()) {

            log.debug("判断用户限购规则查询用户已购数量耗时:{},返回参数:{}",(System.currentTimeMillis() - start2),userBuyNumberMap);
        }

        // 校验用户自定义时间段限制,是否满足匹配的限制
        for (Map.Entry<Long, Integer>  entry : distributorGoodsQtyMap.entrySet()) {
            GoodsPurchaseRestrictionDTO goodsPurchaseRestrictionDTO = purchaseRestrictionDTOMap.get(entry.getKey());
            Long allBuyNumber = userBuyNumberMap.getOrDefault(entry.getKey(),0l) + entry.getValue().longValue();
            if (goodsPurchaseRestrictionDTO.getTimeRestrictionQuantity() != 0 && CompareUtil.compare(allBuyNumber,goodsPurchaseRestrictionDTO.getTimeRestrictionQuantity()) > 0) {
                throw limitException(goodsInfoMap,entry.getKey());
            }
        }

        log.info("判断用户限购规则耗时：" + (System.currentTimeMillis() - start1));
    }


    /**
     * 商品限价异常信息
     * @param goodsInfoMap
     * @param goodsId
     * @return
     */
    private BusinessException limitException(Map<Long, List<GoodsSkuStandardBasicDTO>> goodsInfoMap,Long goodsId) {

        List<GoodsSkuStandardBasicDTO> goodsSkuInfoDTOS = goodsInfoMap.get(goodsId);
        String goodsName = "**商品";
        if (CollectionUtil.isNotEmpty(goodsSkuInfoDTOS)) {
            goodsName = goodsSkuInfoDTOS.stream().findFirst().get().getStandardGoodsBasic().getName();
        }

        String failMsg = MessageFormat.format(OrderErrorCode.SUBMIT_GOODS_LIMIT_ERROR.getMessage(), goodsName);

        return new BusinessException(OrderErrorCode.SUBMIT_GOODS_LIMIT_ERROR,failMsg);
    }
}
