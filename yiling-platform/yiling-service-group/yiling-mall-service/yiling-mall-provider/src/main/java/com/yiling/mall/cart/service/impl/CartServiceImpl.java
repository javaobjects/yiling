package com.yiling.mall.cart.service.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.goods.inventory.api.InventoryApi;
import com.yiling.goods.inventory.dto.InventoryDTO;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsSkuDTO;
import com.yiling.goods.medicine.dto.GoodsSkuFullDTO;
import com.yiling.goods.medicine.dto.GoodsSkuInfoDTO;
import com.yiling.goods.medicine.enums.GoodsErrorCode;
import com.yiling.goods.medicine.enums.GoodsSkuStatusEnum;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.goods.restriction.api.GoodsPurchaseRestrictionApi;
import com.yiling.goods.restriction.dto.GoodsPurchaseRestrictionDTO;
import com.yiling.goods.restriction.dto.request.QueryGoodsPurchaseRestrictionRequest;
import com.yiling.mall.agreement.api.AgreementBusinessApi;
import com.yiling.mall.agreement.enums.GoodsLimitStatusEnum;
import com.yiling.mall.cart.dao.CartMapper;
import com.yiling.mall.cart.dto.request.AddToCartRequest;
import com.yiling.mall.cart.dto.request.BatchAddToCartRequest;
import com.yiling.mall.cart.dto.request.CombinationAddToCartRequest;
import com.yiling.mall.cart.dto.request.GetCartGoodsInfoRequest;
import com.yiling.mall.cart.dto.request.ListCartRequest;
import com.yiling.mall.cart.dto.request.SelectCartGoodsRequest;
import com.yiling.mall.cart.dto.request.UpdateCartGoodsQuantityRequest;
import com.yiling.mall.cart.entity.CartDO;
import com.yiling.mall.cart.enums.CartGoodsSourceEnum;
import com.yiling.mall.cart.enums.CartIncludeEnum;
import com.yiling.mall.cart.service.CartService;
import com.yiling.marketing.promotion.api.PromotionActivityApi;
import com.yiling.marketing.promotion.dto.PromotionCombinationPackDTO;
import com.yiling.marketing.promotion.dto.PromotionDTO;
import com.yiling.marketing.promotion.dto.PromotionGoodsLimitDTO;
import com.yiling.marketing.promotion.dto.request.PromotionActivityRequest;
import com.yiling.marketing.promotion.dto.request.PromotionAppRequest;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderPromotionActivityApi;
import com.yiling.order.order.bo.CombinationBuyNumberBO;
import com.yiling.order.order.dto.request.QueryUserBuyNumberRequest;
import com.yiling.order.order.enums.OrderErrorCode;
import com.yiling.order.order.enums.PromotionActivityTypeEnum;
import com.yiling.user.enterprise.api.EnterprisePurchaseRelationApi;
import com.yiling.user.procrelation.api.ProcurementRelationGoodsApi;
import com.yiling.user.procrelation.dto.DistributorGoodsBO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.comparator.CompareUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 进货单信息 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-06-17
 */
@Service
@Slf4j
public class CartServiceImpl extends BaseServiceImpl<CartMapper, CartDO> implements CartService {

    @DubboReference
    GoodsApi                      goodsApi;
    @DubboReference
    InventoryApi                  inventoryApi;
    @DubboReference
    EnterprisePurchaseRelationApi enterprisePurchaseRelationApi;
    @DubboReference
    AgreementBusinessApi          agreementBusinessApi;
    @DubboReference
    PromotionActivityApi          promotionActivityApi;
    @DubboReference
    GoodsPurchaseRestrictionApi   goodsPurchaseRestrictionApi;
    @DubboReference
    OrderApi                      orderApi;
    @DubboReference
    OrderPromotionActivityApi     orderPromotionActivityApi;
    @DubboReference
    ProcurementRelationGoodsApi   procurementRelationGoodsApi;



    /**
     * 校验商品状态
     */
    private void validateGoodsStatus(List<Long> GoodsSkuIds) {

        List<GoodsSkuInfoDTO> allGoodsDTOList = goodsApi.batchQueryInfoBySkuIds(GoodsSkuIds);

        if (GoodsSkuIds.size() != allGoodsDTOList.size()) {

            throw new BusinessException(GoodsErrorCode.REMOVED);
        }

        Map<Long, GoodsSkuInfoDTO> allGoodsDTOMap = allGoodsDTOList.stream().collect(Collectors.toMap(GoodsSkuInfoDTO::getId, Function.identity()));
        for (Long goodsSkuIdsId : GoodsSkuIds) {
            GoodsSkuInfoDTO goodsDTO = allGoodsDTOMap.get(goodsSkuIdsId);
            if (GoodsStatusEnum.getByCode(goodsDTO.getGoodsInfo().getGoodsStatus()) != GoodsStatusEnum.UP_SHELF) {
                throw new BusinessException(OrderErrorCode.SUBMIT_GOODS_OFF_SHELF);
            }
            if (!GoodsSkuStatusEnum.NORMAL.getCode().equals(goodsDTO.getStatus())){
                throw new BusinessException(OrderErrorCode.SUBMIT_GOODS_DISABLE);
            }
            // 校验是否审核通过
            if (!GoodsStatusEnum.AUDIT_PASS.getCode().equals(goodsDTO.getGoodsInfo().getAuditStatus())){
                throw new BusinessException(OrderErrorCode.SUBMIT_GOODS_DISABLE);
            }
        }
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
            if (!GoodsSkuStatusEnum.NORMAL.getCode().equals(skuDTO.getStatus())){
                throw new BusinessException(OrderErrorCode.SUBMIT_GOODS_DISABLE);
            }
            Integer number = skuIdNumberMap.get(skuId);
            if (number % skuDTO.getPackageNumber() != 0) {
                throw new BusinessException(OrderErrorCode.SKU_PACKAGE_NUMBER_EXIST);
            }
        }
    }

    /**
     * 校验采购关系
     */
    private void validatePurchaseRelation(List<Long> distributorEids, Long buyerEid) {
        boolean checkPurchaseRelationResult = enterprisePurchaseRelationApi.checkPurchaseRelation(buyerEid, distributorEids);
        if (!checkPurchaseRelationResult) {
            throw new BusinessException(OrderErrorCode.SUBMIT_NO_PURCHASE_RELATION);
        }
    }

    /**
     * 校验采购权限
     */
    private void validatePurchaseAuthority(Map<Long,List<Long>> goodIdMap, Long buyerEid) {

        List<Long> goodIdList = goodIdMap.entrySet().stream().map(t -> t.getKey()).collect(Collectors.toList());

        Map<Long, List<DistributorGoodsBO>> resultMap = procurementRelationGoodsApi.getDistributorByYlGoodsIdAndBuyer(goodIdList, buyerEid);

        if (MapUtil.isEmpty(resultMap)) {

            throw new BusinessException(OrderErrorCode.SUBMIT_NO_PURCHASE_AUTHORITY);
        }

        goodIdMap.entrySet().forEach(t -> {

            List<DistributorGoodsBO> distributorGoodsBOS = resultMap.get(t.getKey());

            if (CollectionUtil.isEmpty(distributorGoodsBOS)) {

                throw new BusinessException(OrderErrorCode.SUBMIT_NO_PURCHASE_AUTHORITY);
            }

            List<Long> distributorEidList = distributorGoodsBOS.stream().map(z -> z.getDistributorEid()).collect(Collectors.toList());

            if (!distributorEidList.containsAll(t.getValue())) {

                throw new BusinessException(OrderErrorCode.SUBMIT_NO_PURCHASE_AUTHORITY);
            }
        });
    }

    /**
     * 校验基本库存
     *
     * @param allGoodsSkuIds    配送商品ID
     * @param skuGoodQuantityMap 商品Map
     */
    private void validateInventory(List<Long> allGoodsSkuIds, Map<Long, Integer> skuGoodQuantityMap) {

        List<List<Long>> subList = Lists.partition(allGoodsSkuIds, 50);

        Map<Long, InventoryDTO> inventoryDTOMap = MapUtil.newHashMap();
        subList.forEach(e -> {

            Map<Long, InventoryDTO> inventoryMap = inventoryApi.getMapBySkuIds(e);

            inventoryDTOMap.putAll(Optional.ofNullable(inventoryMap).orElse(Collections.EMPTY_MAP));
        });

        if (CollectionUtil.isEmpty(inventoryDTOMap)) {

            throw new BusinessException(GoodsErrorCode.INVENTORY_NOT_ENOUGH);
        }

        for (Long skuGoodId : allGoodsSkuIds) {

            InventoryDTO inventoryDTO = inventoryDTOMap.get(skuGoodId);
            Integer quantity = skuGoodQuantityMap.get(skuGoodId);
            // 超卖商品无需校验库存
            if (ObjectUtil.equal(1,inventoryDTO.getOverSoldType())) {

                continue;
            }
            if (inventoryDTO == null || inventoryDTO.getQty() - inventoryDTO.getFrozenQty() < quantity) {

                throw new BusinessException(GoodsErrorCode.INVENTORY_NOT_ENOUGH);
            }
        }

    }

    /**
     * 组合商品限购
     * @param request
     * @param buyQty
     * @param promotionDTO
     */
    private void validateCombinationOrderLimit(GetCartGoodsInfoRequest request,Integer buyQty,PromotionDTO promotionDTO,Boolean isUpdate){

        if (CartGoodsSourceEnum.B2B != request.getGoodsSourceEnum()) {
            return;
        }

        // 只校验正组合商品
        if (PromotionActivityTypeEnum.COMBINATION != request.getPromotionActivityTypeEnum()) {
            return;
        }

        // 表示促销活动已失效
        if ( 1 != promotionDTO.getAvailable()) {
            throw new BusinessException(OrderErrorCode.ORDER_PROMOTION_ERROR);
        }

        PromotionCombinationPackDTO promotionCombinationPackDTO = promotionDTO.getPromotionCombinationPackDTO();
        // 购买数量小于促销活动最小起订量
        if (CompareUtil.compare(buyQty,promotionCombinationPackDTO.getInitialNum()) < 0 ) {
            String failMsg = MessageFormat.format(OrderErrorCode.ORDER_PROMOTION_MINIMUM_NUM.getMessage(), promotionCombinationPackDTO.getInitialNum());
            throw new BusinessException(OrderErrorCode.ORDER_PROMOTION_MINIMUM_NUM, failMsg);
        }

        // 是否校验限购商品信息
        Boolean isCheckNumberLimit =  promotionCombinationPackDTO.getPerDayNum() != 0 ||  promotionCombinationPackDTO.getPerPersonNum() != 0
                || promotionCombinationPackDTO.getTotalNum() != 0;

        if (!isCheckNumberLimit) {

            return;
        }

        // 购物车已加购数量
        Integer cartNumber = isUpdate ? buyQty : this.sumGoodsQuantityByGoodId(request) + buyQty;

        // 组合包活动已购买数量
        CombinationBuyNumberBO combinationBuyNumberBO = orderPromotionActivityApi.sumCombinationActivityNumber(request.getBuyerEid(), request.getPromotionActivityId());

        if (log.isDebugEnabled()) {
            log.debug("查询用户已购买组合包数量入参：{}，{},返回:[{}]",request.getPromotionActivityId(),request.getBuyerEid(),JSON.toJSONString(combinationBuyNumberBO));
        }

        // 校验每人可购买数量
        if (promotionCombinationPackDTO.getPerPersonNum() != 0 && CompareUtil.compare(cartNumber + combinationBuyNumberBO.getBuyerQty().intValue(),promotionCombinationPackDTO.getPerPersonNum()) > 0) {
            if (isUpdate) {
                String failMsg = MessageFormat.format(OrderErrorCode.SUBMIT_COMBINATION_GOODS_LIMIT_ERROR.getMessage(),promotionCombinationPackDTO.getPackageName(),promotionCombinationPackDTO.getPerPersonNum());
                throw new BusinessException(OrderErrorCode.SUBMIT_COMBINATION_GOODS_LIMIT_ERROR,failMsg);
            } else {
                String failMsg = MessageFormat.format(OrderErrorCode.CART_COMBINATION_GOODS_LIMIT_ERROR.getMessage(), promotionCombinationPackDTO.getPerPersonNum());
                throw new BusinessException(OrderErrorCode.CART_COMBINATION_GOODS_LIMIT_ERROR,failMsg);
            }
        }
        // 校验每人每天
        if (promotionCombinationPackDTO.getPerDayNum() != 0 && CompareUtil.compare(cartNumber + combinationBuyNumberBO.getBuyerDayQty().intValue(),promotionCombinationPackDTO.getPerDayNum()) > 0) {
            if (isUpdate) {
                String failMsg = MessageFormat.format(OrderErrorCode.SUBMIT_COMBINATION_GOODS_LIMIT_ERROR.getMessage(),promotionCombinationPackDTO.getPackageName(),promotionCombinationPackDTO.getPerDayNum());
                throw new BusinessException(OrderErrorCode.SUBMIT_COMBINATION_GOODS_LIMIT_ERROR,failMsg);
            } else {
                String failMsg = MessageFormat.format(OrderErrorCode.CART_COMBINATION_GOODS_LIMIT_ERROR.getMessage(), promotionCombinationPackDTO.getPerDayNum());
                throw new BusinessException(OrderErrorCode.CART_COMBINATION_GOODS_LIMIT_ERROR,failMsg);
            }
        }
        // 校验总购买数量
        if (promotionCombinationPackDTO.getTotalNum() != 0 && CompareUtil.compare(cartNumber + combinationBuyNumberBO.getSumQty().intValue(),promotionCombinationPackDTO.getTotalNum()) > 0) {
            if (isUpdate) {
                String failMsg = MessageFormat.format(OrderErrorCode.SUBMIT_COMBINATION_ORDER_LIMIT_ERROR.getMessage(), promotionCombinationPackDTO.getPackageName());
                throw new BusinessException(OrderErrorCode.SUBMIT_COMBINATION_ORDER_LIMIT_ERROR,failMsg);
            } else {
                throw new BusinessException(OrderErrorCode.CART_COMBINATION_ORDER_LIMIT_ERROR);
            }
        }

    }


    /**
     * 正常品限购逻辑 验证商品限购商品信息,目前需要校验正常品,活动商品不拉入商品限购规则
     * @param request
     * @param buyQty
     */
    private void validateNormalOrderLimit(GetCartGoodsInfoRequest request,Integer buyQty) {

        if (CartGoodsSourceEnum.B2B != request.getGoodsSourceEnum()) {
            return;
        }
        // 只校验大运河
        if (PlatformEnum.B2B != request.getPlatformEnum()) {
            return;
        }

        // 组合商品走单独限购逻辑
        if (PromotionActivityTypeEnum.NORMAL != request.getPromotionActivityTypeEnum()) {
            return;
        }

        QueryGoodsPurchaseRestrictionRequest queryGoodsPurchaseRestrictionRequest = new QueryGoodsPurchaseRestrictionRequest();
        queryGoodsPurchaseRestrictionRequest.setCustomerEid(request.getBuyerEid()).setGoodsIdList(ListUtil.toList(request.getGoodsId()));
        List<GoodsPurchaseRestrictionDTO> goodsPurchaseRestrictionDTOS = goodsPurchaseRestrictionApi.queryValidPurchaseRestriction(queryGoodsPurchaseRestrictionRequest);
        if (log.isDebugEnabled()) {

            log.debug("调用商品接口查询商品限购规则入参：{},返回:[{}]",queryGoodsPurchaseRestrictionRequest,JSON.toJSONString(goodsPurchaseRestrictionDTOS));
        }

        if (CollectionUtil.isEmpty(goodsPurchaseRestrictionDTOS)) {
            return;
        }

        GoodsPurchaseRestrictionDTO goodsPurchaseRestrictionDTO = goodsPurchaseRestrictionDTOS.stream().findFirst().get();

        if (goodsPurchaseRestrictionDTO.getTimeRestrictionQuantity() == 0 && goodsPurchaseRestrictionDTO.getOrderRestrictionQuantity() == 0) {
            return;
        }
        // 购物车已加购数量
        Integer cartNumber = this.sumGoodsQuantityByGoodId(request) + buyQty;
        // 每单数量是否超出,每单限制最大数量
        if (goodsPurchaseRestrictionDTO.getOrderRestrictionQuantity() != 0 && CompareUtil.compare(cartNumber.longValue(),goodsPurchaseRestrictionDTO.getOrderRestrictionQuantity()) > 0 ) {
            String failMsg = MessageFormat.format(OrderErrorCode.SUBMIT_GOODS_LIMIT_ONE_ERROR.getMessage(), goodsPurchaseRestrictionDTO.getOrderRestrictionQuantity());
            throw new BusinessException(OrderErrorCode.SUBMIT_GOODS_LIMIT_ONE_ERROR,failMsg);
        }

        if (goodsPurchaseRestrictionDTO.getTimeRestrictionQuantity() == 0) {
            return;
        }

        QueryUserBuyNumberRequest queryUserBuyNumberRequest = new QueryUserBuyNumberRequest();
        queryUserBuyNumberRequest.setGoodId(goodsPurchaseRestrictionDTO.getGoodsId());
        queryUserBuyNumberRequest.setBuyerEid(request.getBuyerEid());
        queryUserBuyNumberRequest.setSelectRuleEnum(QueryUserBuyNumberRequest.SelectRuleEnum.getByCode(goodsPurchaseRestrictionDTO.getTimeType()));
        queryUserBuyNumberRequest.setStartTime(goodsPurchaseRestrictionDTO.getStartTime());
        queryUserBuyNumberRequest.setEndTime(goodsPurchaseRestrictionDTO.getEndTime());
        // 用户购买数量
        Long userBuyNumber = orderApi.getUserBuyNumber(queryUserBuyNumberRequest) + cartNumber;

        // 校验是否超出自定义商品购买数量
        if (CompareUtil.compare(userBuyNumber,goodsPurchaseRestrictionDTO.getTimeRestrictionQuantity()) > 0) {

            throw checkLimitExceptionMsg(queryUserBuyNumberRequest,goodsPurchaseRestrictionDTO);
        }

    }



    /**
     * 获取商品限制的异常信息
     * @param queryUserBuyNumberRequest
     * @param goodsPurchaseRestrictionDTO
     * @return
     */
    private BusinessException checkLimitExceptionMsg(QueryUserBuyNumberRequest queryUserBuyNumberRequest ,GoodsPurchaseRestrictionDTO goodsPurchaseRestrictionDTO ) {
        BusinessException businessException = null;
        String failMsg = "";
        // 如果没有每单限制,只有时间内限制，提示成每单限制提示
        if (goodsPurchaseRestrictionDTO.getOrderRestrictionQuantity() == 0) {
            failMsg = MessageFormat.format(OrderErrorCode.SUBMIT_GOODS_LIMIT_ONE_ERROR.getMessage(), goodsPurchaseRestrictionDTO.getTimeRestrictionQuantity());
            businessException =  new BusinessException(OrderErrorCode.SUBMIT_GOODS_LIMIT_ONE_ERROR,failMsg);
            return businessException;
        }
        switch (queryUserBuyNumberRequest.getSelectRuleEnum()) {
            case DAY:
                failMsg = MessageFormat.format(OrderErrorCode.SUBMIT_GOODS_LIMIT_DAY_ERROR.getMessage(), goodsPurchaseRestrictionDTO.getTimeRestrictionQuantity());
                businessException =  new BusinessException(OrderErrorCode.SUBMIT_GOODS_LIMIT_DAY_ERROR,failMsg);
                break;
            case WEEK:
                failMsg = MessageFormat.format(OrderErrorCode.SUBMIT_GOODS_LIMIT_WEEK_ERROR.getMessage(), goodsPurchaseRestrictionDTO.getTimeRestrictionQuantity());
                businessException =  new BusinessException(OrderErrorCode.SUBMIT_GOODS_LIMIT_WEEK_ERROR,failMsg);
                break;
            case MONTH:
                failMsg = MessageFormat.format(OrderErrorCode.SUBMIT_GOODS_LIMIT_MONTH_ERROR.getMessage(), goodsPurchaseRestrictionDTO.getTimeRestrictionQuantity());
                businessException =  new BusinessException(OrderErrorCode.SUBMIT_GOODS_LIMIT_MONTH_ERROR,failMsg);
                break;
            case CUSTOMIZE:
                Long dayValue = DateUtil.between(queryUserBuyNumberRequest.getStartTime(),queryUserBuyNumberRequest.getEndTime(), DateUnit.DAY) + 1;
                failMsg = MessageFormat.format(OrderErrorCode.SUBMIT_GOODS_LIMIT_PERIOD_ERROR.getMessage(), dayValue,goodsPurchaseRestrictionDTO.getTimeRestrictionQuantity());
                businessException = new  BusinessException(OrderErrorCode.SUBMIT_GOODS_LIMIT_PERIOD_ERROR,failMsg);
            default: break;
        }
        return businessException;
    }

    @Override
    public Integer getCartGoodsNum(Long buyerEid, PlatformEnum platformEnum, CartGoodsSourceEnum goodsSourceEnum) {
        QueryWrapper<CartDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CartDO::getBuyerEid, buyerEid);

        if (ObjectUtil.isNotNull(platformEnum)) {
            queryWrapper.lambda().eq(CartDO::getPlatformType, platformEnum.getCode());
        }

        if (ObjectUtil.isNotNull(goodsSourceEnum)) {
            queryWrapper.lambda().eq(CartDO::getGoodSource, goodsSourceEnum.getCode());
        }
        return this.count(queryWrapper);
    }

    @Override
    public boolean addToCart(AddToCartRequest request) {
        // 兼容购物车老数据，可能没有促销类型数据
        if (request.getPromotionActivityType() == null || 0 == request.getPromotionActivityType()) {
            request.setPromotionActivityType(PromotionActivityTypeEnum.NORMAL.getCode());
        }
        // 购物车校验
        this.cartAddValidate(request);

        return this.saveOrUpdate(request.getGoodsSkuId(), request.getGoodsId(), request.getQuantity(), request.getDistributorEid(), request.getDistributorGoodsId(), request.getBuyerEid(), request.getPlatformEnum(), request.getGoodsSourceEnum(),request.getPromotionActivityType(),request.getOpUserId());
    }


    /**
     * 购物车添加校验
     * @param request
     */
    private void cartAddValidate(AddToCartRequest request) {

        Long goodsId = request.getGoodsId();
        Long buyerEid = request.getBuyerEid();
        Long distributorEid = request.getDistributorEid();
        Long distributorGoodsId = request.getDistributorGoodsId();
        Long goodsSkuId = request.getGoodsSkuId();
        Integer quantity = request.getQuantity();
        CartGoodsSourceEnum goodsSourceEnum = request.getGoodsSourceEnum();
        PlatformEnum platformEnum = request.getPlatformEnum();
        // 校验卖家规格信息
        if (CartGoodsSourceEnum.B2B == goodsSourceEnum) {
            this.checkB2BGoodsByGids(request.getBuyerEid(),Collections.singletonList(distributorGoodsId));
        }
        EnumSet<CartGoodsSourceEnum> checkPurchaseRelationSet = EnumSet.of(CartGoodsSourceEnum.POP);
        // 校验包装规格数量
        this.checkPackageNumber(Collections.singletonMap(goodsSkuId,quantity));
        // 校验商品销售规格基本状态
        this.validateGoodsStatus(Collections.singletonList(goodsSkuId));
        // 如果是pop订单需要校验采购关系
        if (checkPurchaseRelationSet.contains(goodsSourceEnum)) {
            // 校验采购关系
            this.validatePurchaseRelation(Collections.singletonList(distributorEid), buyerEid);
            // 校验采购权限关系
            this.validatePurchaseAuthority(MapUtil.of(goodsId,ListUtil.toList(distributorEid)), buyerEid);
        }
        GetCartGoodsInfoRequest skuRequest = new GetCartGoodsInfoRequest();
        skuRequest.setGoodsSkuId(goodsSkuId);
        skuRequest.setGoodsId(distributorGoodsId);
        skuRequest.setBuyerEid(request.getBuyerEid());
        skuRequest.setDistributorEid(request.getDistributorEid());
        skuRequest.setGoodsSourceEnum(request.getGoodsSourceEnum());
        skuRequest.setPlatformEnum(platformEnum);
        skuRequest.setCartIncludeEnum(CartIncludeEnum.SELECTED);
        if (PlatformEnum.SALES_ASSIST == skuRequest.getPlatformEnum()) {
            skuRequest.setOpUserId(request.getOpUserId());
        }
        // 购物车已加购数量
        Integer cartNumber = this.sumGoodsQuantityByGoodSkuId(skuRequest);

        // 校验商品基本库存是否满足
        this.validateInventory(Collections.singletonList(goodsSkuId), Collections.singletonMap(goodsSkuId, quantity + cartNumber));

        // 验证限购商品
        GetCartGoodsInfoRequest limitRequest = new GetCartGoodsInfoRequest();
        limitRequest.setGoodsId(request.getDistributorGoodsId());
        limitRequest.setBuyerEid(request.getBuyerEid());
        limitRequest.setDistributorEid(request.getDistributorEid());
        limitRequest.setGoodsSourceEnum(request.getGoodsSourceEnum());
        limitRequest.setPlatformEnum(request.getPlatformEnum());
        limitRequest.setPromotionActivityTypeEnum(PromotionActivityTypeEnum.getByCode(request.getPromotionActivityType()));

        this.validateNormalOrderLimit(limitRequest,request.getQuantity());
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addToCartBySpecialProduct(AddToCartRequest request) {
        if (PromotionActivityTypeEnum.NORMAL == PromotionActivityTypeEnum.getByCode(request.getPromotionActivityType())) {
            return this.addToCart(request);
        }
        Long goodsId = request.getGoodsId();
        Long buyerEid = request.getBuyerEid();
        Long distributorEid = request.getDistributorEid();
        Long distributorGoodsId = request.getDistributorGoodsId();
        Long goodsSkuId = request.getGoodsSkuId();
        Integer quantity = request.getQuantity();
        PlatformEnum platformEnum = request.getPlatformEnum();
        CartGoodsSourceEnum goodsSourceEnum = request.getGoodsSourceEnum();
        PromotionActivityTypeEnum promotionActivityTypeEnum = PromotionActivityTypeEnum.getByCode(request.getPromotionActivityType());

        // 购物车校验
        this.cartAddValidate(request);

        GetCartGoodsInfoRequest getCartGoodsInfoRequest = new GetCartGoodsInfoRequest();
        getCartGoodsInfoRequest.setGoodsSkuId(goodsSkuId);
        // getCartGoodsInfoRequest.setGoodsId(goodsId);
        getCartGoodsInfoRequest.setDistributorEid(distributorEid);
        getCartGoodsInfoRequest.setBuyerEid(buyerEid);
        getCartGoodsInfoRequest.setPlatformEnum(platformEnum);
        getCartGoodsInfoRequest.setGoodsSourceEnum(goodsSourceEnum);
        getCartGoodsInfoRequest.setPromotionActivityTypeEnum(promotionActivityTypeEnum);

        CartDO entity = this.getCartGoodsInfo(getCartGoodsInfoRequest);

        if (entity == null) {
            entity = new CartDO();
            entity.setBuyerEid(buyerEid);
            entity.setSellerEid(distributorEid);
            entity.setDistributorEid(distributorEid);
            entity.setDistributorGoodsId(distributorGoodsId);
            entity.setGoodSource(goodsSourceEnum.getCode());
            entity.setGoodsSkuId(goodsSkuId);
            entity.setGoodsId(goodsId);
            entity.setQuantity(quantity);
            entity.setPlatformType(platformEnum.getCode());
            entity.setSelectedFlag(1);
            entity.setOpUserId(request.getOpUserId());
            entity.setPromotionActivityType(promotionActivityTypeEnum.getCode());
            entity.setPromotionActivityId(request.getPromotionActivityId());
            return this.save(entity);
        }
        // 表示秒杀或者特价活动为同一个活动，修改数量
        if (request.getPromotionActivityId().equals(entity.getPromotionActivityId())) {
            entity.setQuantity(entity.getQuantity() + quantity);
            entity.setPromotionActivityId(request.getPromotionActivityId());
            entity.setSelectedFlag(1);
            entity.setGoodsId(goodsId);
            entity.setOpUserId(request.getOpUserId());
            return this.updateById(entity);
        } else {
            // 直接覆盖原有活动以及数量,表示活动变了，直接加购成为信息的活动信息
            entity.setQuantity(quantity);
            entity.setPromotionActivityId(request.getPromotionActivityId());
            entity.setGoodsId(goodsId);
            entity.setSelectedFlag(1);
            entity.setOpUserId(request.getOpUserId());
            return this.updateById(entity);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchAddToCart(BatchAddToCartRequest request) {
        List<BatchAddToCartRequest.QuickPurchaseInfoDTO> quickPurchaseInfoList = request.getQuickPurchaseInfoList();
        if (CollUtil.isEmpty(quickPurchaseInfoList)) {
            return false;
        }
        // 配送商企业ID
        List<Long> distributorEids = quickPurchaseInfoList.stream().map(BatchAddToCartRequest.QuickPurchaseInfoDTO::getDistributorEid).distinct().collect(Collectors.toList());
        // 所有配送商商品ID集合
        List<Long> allGoodsSkuIds = quickPurchaseInfoList.stream().map(BatchAddToCartRequest.QuickPurchaseInfoDTO::getGoodsSkuId).distinct().collect(Collectors.toList());
        // 所有以岭商品ID集合
        List<Long> allGoodsIds = quickPurchaseInfoList.stream().map(BatchAddToCartRequest.QuickPurchaseInfoDTO::getGoodsId).distinct().collect(Collectors.toList());
        // 商品购买数量
        Map<Long, Integer> skuGoodQuantityMap = quickPurchaseInfoList.stream().collect(Collectors.toMap(BatchAddToCartRequest.QuickPurchaseInfoDTO::getGoodsSkuId, BatchAddToCartRequest.QuickPurchaseInfoDTO::getQuantity));



        // 校验包装规格数量
        this.checkPackageNumber(skuGoodQuantityMap);
        //校验商品状态
        this.validateGoodsStatus(allGoodsSkuIds);
        EnumSet<CartGoodsSourceEnum> checkPurchaseRelationSet = EnumSet.of(CartGoodsSourceEnum.POP);

        if (checkPurchaseRelationSet.contains(request.getGoodsSourceEnum())) {
            Map<Long, List<Long>> quickPurchaseInfoMap = quickPurchaseInfoList.stream().collect(Collectors.groupingBy(BatchAddToCartRequest.QuickPurchaseInfoDTO::getGoodsId, Collectors.mapping(BatchAddToCartRequest.QuickPurchaseInfoDTO::getDistributorEid, Collectors.toList())));
            // 校验采购关系
            this.validatePurchaseRelation(distributorEids, request.getBuyerEid());
            // 校验采购权限
            this.validatePurchaseAuthority(quickPurchaseInfoMap, request.getBuyerEid());
        }
        // 校验商品基本库存是否满足
        this.validateInventory(allGoodsSkuIds, skuGoodQuantityMap);

        quickPurchaseInfoList.forEach(e -> {
            this.saveOrUpdate(e.getGoodsSkuId(), e.getGoodsId(), e.getQuantity(), e.getDistributorEid(), e.getDistributorGoodsId(),
                    request.getBuyerEid(), request.getPlatformEnum(), request.getGoodsSourceEnum(),PromotionActivityTypeEnum.NORMAL.getCode(),request.getOpUserId());
        });

        return true;
    }

    /**
     * 保存购物车数据
     * @param goodsSkuId
     * @param goodsId
     * @param quantity
     * @param distributorEid
     * @param distributorGoodsId
     * @param buyerEid
     * @param platformEnum
     * @param goodsSourceEnum
     * @param promotionActivityType
     * @param opUserId
     * @return
     */
    private boolean saveOrUpdate(Long goodsSkuId, Long goodsId, Integer quantity, Long distributorEid, Long distributorGoodsId, Long buyerEid, PlatformEnum platformEnum, CartGoodsSourceEnum goodsSourceEnum,Integer promotionActivityType,Long opUserId) {

        GetCartGoodsInfoRequest request = new GetCartGoodsInfoRequest();
        request.setGoodsSkuId(goodsSkuId);
        // request.setGoodsId(goodsId);
        request.setDistributorEid(distributorEid);
        request.setBuyerEid(buyerEid);
        request.setPlatformEnum(platformEnum);
        request.setGoodsSourceEnum(goodsSourceEnum);
        request.setPromotionActivityTypeEnum(PromotionActivityTypeEnum.getByCode(promotionActivityType));
        request.setOpUserId(opUserId);

        CartDO entity = this.getCartGoodsInfo(request);

        if (entity == null) {
            entity = new CartDO();
            entity.setBuyerEid(buyerEid);
            entity.setSellerEid(distributorEid);
            entity.setDistributorEid(distributorEid);
            entity.setDistributorGoodsId(distributorGoodsId);
            entity.setGoodSource(goodsSourceEnum.getCode());
            entity.setGoodsSkuId(goodsSkuId);
            entity.setGoodsId(goodsId);
            entity.setQuantity(quantity);
            entity.setPlatformType(platformEnum.getCode());
            entity.setSelectedFlag(1);
            entity.setOpUserId(opUserId);
            return this.save(entity);
        } else {
            entity.setQuantity(entity.getQuantity() + quantity);
            entity.setGoodsId(goodsId);
            entity.setSelectedFlag(1);
            entity.setOpUserId(opUserId);
            return this.updateById(entity);
        }
    }


    /**
     * 购物车基本修改
     * @param cartDO
     * @param opUserId
     */
    private void cartUpdateValidate(CartDO cartDO,Integer quantity,Long opUserId) {

        Long goodsSkuId = cartDO.getGoodsSkuId();
        GetCartGoodsInfoRequest skuRequest = new GetCartGoodsInfoRequest();
        skuRequest.setGoodsSkuId(cartDO.getGoodsSkuId());
        skuRequest.setGoodsId(cartDO.getDistributorGoodsId());
        skuRequest.setBuyerEid(cartDO.getBuyerEid());
        skuRequest.setDistributorEid(cartDO.getDistributorEid());
        skuRequest.setGoodsSourceEnum(CartGoodsSourceEnum.getByCode(cartDO.getGoodSource()));
        skuRequest.setPlatformEnum(PlatformEnum.getByCode(cartDO.getPlatformType()));
        skuRequest.setCartIncludeEnum(CartIncludeEnum.SELECTED);

        if (PlatformEnum.SALES_ASSIST == skuRequest.getPlatformEnum()) {
            skuRequest.setOpUserId(opUserId);
        }
        // 购物车已加购数量
        Integer cartNumber = this.sumGoodsQuantityByGoodSkuId(skuRequest);
        if (cartDO.getSelectedFlag() == 1) {
            cartNumber = cartNumber - cartDO.getQuantity();
        }
        Map<Long, InventoryDTO> inventoryMap = inventoryApi.getMapBySkuIds(Arrays.asList(goodsSkuId));
        InventoryDTO inventoryDTO = inventoryMap.get(goodsSkuId);
        // 超卖库存无需校验库存
        if (inventoryDTO == null || (ObjectUtil.equal(0,inventoryDTO.getOverSoldType()) && (inventoryDTO.getQty() - inventoryDTO.getFrozenQty() < quantity + cartNumber ))) {
            throw new BusinessException(GoodsErrorCode.INVENTORY_NOT_ENOUGH);
        }
    }

    /**
     * 特价商品修改
     * @param request
     * @param cartDO
     * @return
     */
    private  boolean updateSpecialCartGoodsQuantity(UpdateCartGoodsQuantityRequest request,CartDO cartDO) {
        Long goodsSkuId = cartDO.getGoodsSkuId();
        GoodsSkuFullDTO goodsDTO = goodsApi.queryFullInfoBySkuId(goodsSkuId);
        if (goodsDTO == null) {
            throw new BusinessException(GoodsErrorCode.REMOVED);
        } else if (GoodsStatusEnum.getByCode(goodsDTO.getGoodsInfo().getGoodsStatus()) != GoodsStatusEnum.UP_SHELF) {
            throw new BusinessException(GoodsErrorCode.OFF_SHELF);
        }
        this.checkPackageNumber(Collections.singletonMap(goodsSkuId,request.getQuantity()));
        PromotionAppRequest promotionAppRequest = new PromotionAppRequest();
        promotionAppRequest.setBuyerEid(cartDO.getBuyerEid());
        promotionAppRequest.setPlatform(cartDO.getPlatformType());
        promotionAppRequest.setTypeList(ListUtil.toList(cartDO.getPromotionActivityType()));
        promotionAppRequest.setGoodsIdList(ListUtil.toList(cartDO.getDistributorGoodsId()));
        List<PromotionGoodsLimitDTO> promotionGoodsLimitDTOList = promotionActivityApi.queryGoodsPromotionInfo(promotionAppRequest);
        if (CollectionUtil.isEmpty(promotionGoodsLimitDTOList)) {
            String failMsg = MessageFormat.format(OrderErrorCode.ORDER_PROMOTION_EXPIRED.getMessage(), goodsDTO.getGoodsInfo().getName());
            throw new BusinessException(OrderErrorCode.ORDER_PROMOTION_EXPIRED,failMsg);
        }
        promotionGoodsLimitDTOList = promotionGoodsLimitDTOList.stream().filter(t -> t.getPromotionActivityId().equals(cartDO.getPromotionActivityId())).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(promotionGoodsLimitDTOList)) {
            String failMsg = MessageFormat.format(OrderErrorCode.ORDER_PROMOTION_EXPIRED.getMessage(), goodsDTO.getGoodsInfo().getName());
            throw new BusinessException(OrderErrorCode.ORDER_PROMOTION_EXPIRED,failMsg);
        }
        // 查询其他规格的限购数量
        GetCartGoodsInfoRequest getCartGoodsInfoRequest = new GetCartGoodsInfoRequest();
        getCartGoodsInfoRequest.setGoodsSkuId(goodsSkuId);
        getCartGoodsInfoRequest.setGoodsId(cartDO.getDistributorGoodsId());
        getCartGoodsInfoRequest.setBuyerEid(cartDO.getBuyerEid());
        getCartGoodsInfoRequest.setDistributorEid(cartDO.getDistributorEid());
        getCartGoodsInfoRequest.setGoodsSourceEnum(CartGoodsSourceEnum.B2B);
        getCartGoodsInfoRequest.setPlatformEnum(PlatformEnum.getByCode(cartDO.getPlatformType()));
        getCartGoodsInfoRequest.setPromotionActivityTypeEnum(PromotionActivityTypeEnum.getByCode(cartDO.getPromotionActivityType()));
        getCartGoodsInfoRequest.setPromotionActivityId(cartDO.getPromotionActivityId());
        Integer goodsQuantity = this.sumGoodsQuantityByGoodId(getCartGoodsInfoRequest);
        // 校验库存数量
        PromotionGoodsLimitDTO goodsLimitDTO = promotionGoodsLimitDTOList.stream().findAny().get();
        if (CompareUtil.compare(request.getQuantity() + goodsQuantity,goodsLimitDTO.getLeftBuyCount()) > 0) {
            String failMsg = MessageFormat.format(OrderErrorCode.ORDER_PROMOTION_LIMITD.getMessage(), goodsDTO.getGoodsInfo().getStandardGoodsAllInfo().getBaseInfo().getName());
            throw new BusinessException(OrderErrorCode.ORDER_PROMOTION_LIMITD,failMsg);
        }
        // 购物车库存校验
        cartUpdateValidate(cartDO,cartDO.getQuantity(),request.getOpUserId());
        CartDO entity = new CartDO();
        entity.setId(request.getId());
        entity.setQuantity(request.getQuantity());
        entity.setOpUserId(request.getOpUserId());

        return this.updateById(entity);
    }

    /**
     * 修改组合商品购物车
     * @param request
     * @param cartDO
     * @return
     */
    private  boolean updateCombinationCartGoodsQuantity(UpdateCartGoodsQuantityRequest request,CartDO cartDO) {
        // 活动ID
        Long promotionActivityId = cartDO.getPromotionActivityId();
        PromotionActivityRequest promotionActivityRequest = new PromotionActivityRequest();
        promotionActivityRequest.setBuyerEid(cartDO.getBuyerEid());
        promotionActivityRequest.setGoodsPromotionActivityIdList(ListUtil.toList(promotionActivityId));
        List<PromotionDTO>  promotionDTOS = promotionActivityApi.queryPromotionInfoByActivityAndBuyerId(promotionActivityRequest);
        if (CollectionUtil.isEmpty(promotionDTOS)) {
            throw new BusinessException(OrderErrorCode.ORDER_PROMOTION_ERROR);
        }
        PromotionDTO promotionDTO = promotionDTOS.get(0);

        GetCartGoodsInfoRequest limitRequest = new GetCartGoodsInfoRequest();
        limitRequest.setGoodsId(0l);
        limitRequest.setBuyerEid(cartDO.getBuyerEid());
        limitRequest.setDistributorEid(cartDO.getDistributorEid());
        limitRequest.setGoodsSourceEnum(CartGoodsSourceEnum.getByCode(cartDO.getGoodSource()));
        limitRequest.setPlatformEnum(PlatformEnum.getByCode(cartDO.getPlatformType()));
        limitRequest.setPromotionActivityTypeEnum(PromotionActivityTypeEnum.COMBINATION);
        limitRequest.setPromotionActivityId(cartDO.getPromotionActivityId());
        limitRequest.setGoodsSkuId(cartDO.getGoodsSkuId());

        // 组合商品校验
        this.validateCombinationOrderLimit(limitRequest,request.getQuantity(),promotionDTO,true);

        // 校验商品库存以及状态
        promotionDTO.getGoodsLimitDTOList().forEach(t -> {
            CartDO updateCartDo = new CartDO();
            updateCartDo.setPromotionActivityType(PromotionActivityTypeEnum.COMBINATION.getCode());
            updateCartDo.setOpUserId(request.getOpUserId());
            updateCartDo.setGoodsSkuId(t.getGoodsSkuId());
            updateCartDo.setDistributorGoodsId(t.getGoodsId());
            updateCartDo.setDistributorEid(t.getEid());
            updateCartDo.setGoodSource(cartDO.getGoodSource());
            updateCartDo.setPlatformType(cartDO.getPlatformType());
            updateCartDo.setPromotionActivityId(cartDO.getPromotionActivityId());
            updateCartDo.setSelectedFlag(cartDO.getSelectedFlag());
            updateCartDo.setBuyerEid(cartDO.getBuyerEid());
            updateCartDo.setQuantity(0);
            cartUpdateValidate(updateCartDo,request.getQuantity() * t.getAllowBuyCount(),request.getOpUserId());
        });
        CartDO entity = new CartDO();
        entity.setId(request.getId());
        entity.setQuantity(request.getQuantity());
        entity.setOpUserId(request.getOpUserId());

        return this.updateById(entity);
    }

    /**
     * 普通品购物车修改
     * @param request
     * @param cartDO
     * @return
     */
    private boolean updateNormalCartGoodsQuantity(UpdateCartGoodsQuantityRequest request,CartDO cartDO) {
        Long goodsSkuId = cartDO.getGoodsSkuId();
        GoodsSkuFullDTO goodsDTO = goodsApi.queryFullInfoBySkuId(goodsSkuId);
        if (goodsDTO == null) {
            throw new BusinessException(GoodsErrorCode.REMOVED);
        } else if (GoodsStatusEnum.getByCode(goodsDTO.getGoodsInfo().getGoodsStatus()) != GoodsStatusEnum.UP_SHELF) {
            throw new BusinessException(GoodsErrorCode.OFF_SHELF);
        }
        this.checkPackageNumber(Collections.singletonMap(goodsSkuId,request.getQuantity()));
        // 购物车库存校验
        cartUpdateValidate(cartDO,request.getQuantity(),request.getOpUserId());

        // 验证限购商品
        GetCartGoodsInfoRequest limitRequest = new GetCartGoodsInfoRequest();
        limitRequest.setGoodsId(cartDO.getDistributorGoodsId());
        limitRequest.setBuyerEid(cartDO.getBuyerEid());
        limitRequest.setDistributorEid(cartDO.getDistributorEid());
        limitRequest.setGoodsSourceEnum(CartGoodsSourceEnum.getByCode(cartDO.getGoodSource()));
        limitRequest.setPlatformEnum(PlatformEnum.getByCode(cartDO.getPlatformType()));
        limitRequest.setPromotionActivityTypeEnum(PromotionActivityTypeEnum.NORMAL);
        limitRequest.setGoodsSkuId(cartDO.getGoodsSkuId());

        this.validateNormalOrderLimit(limitRequest,request.getQuantity());

        CartDO entity = new CartDO();
        entity.setId(request.getId());
        entity.setQuantity(request.getQuantity());
        entity.setOpUserId(request.getOpUserId());
        return this.updateById(entity);
    }

    @Override
    public boolean updateCartGoodsQuantity(UpdateCartGoodsQuantityRequest request) {
        CartDO cartDO = Optional.ofNullable(this.getById(request.getId())).orElseThrow(() -> new BusinessException(OrderErrorCode.CART_NOT_EXIST));
        // 秒杀特价，校验秒杀库存
        switch (PromotionActivityTypeEnum.getByCode(cartDO.getPromotionActivityType())) {
            case LIMIT:
            case SPECIAL:
                return this.updateSpecialCartGoodsQuantity(request,cartDO);
            case COMBINATION:
                return this.updateCombinationCartGoodsQuantity(request,cartDO);
            default:
                return updateNormalCartGoodsQuantity(request,cartDO);
        }
    }


    @Override
    public boolean modifyCartGoodsQuantity(UpdateCartGoodsQuantityRequest request) {

        CartDO cartDO = Optional.ofNullable(this.getById(request.getId())).orElseThrow(() -> new BusinessException(OrderErrorCode.CART_NOT_EXIST));

        CartDO entity = new CartDO();
        entity.setId(cartDO.getId());
        entity.setQuantity(request.getQuantity());
        entity.setOpUserId(request.getOpUserId());

        return this.updateById(entity);
    }

    @Override
    public boolean selectCartGoods(SelectCartGoodsRequest request) {
        QueryWrapper<CartDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(CartDO::getId, request.getIds());

        CartDO entity = new CartDO();
        entity.setSelectedFlag(request.getSelected() ? 1 : 0);
        entity.setOpUserId(request.getOpUserId());
        return this.update(entity, queryWrapper);
    }

    @Override
    public List<CartDO> listByBuyerEid(Long buyerEid, PlatformEnum platformEnum, CartGoodsSourceEnum goodsSourceEnum, CartIncludeEnum includeEnum) {
        QueryWrapper<CartDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(CartDO::getBuyerEid, buyerEid);

        if (ObjectUtil.isNotNull(platformEnum)) {
            queryWrapper.lambda().eq(CartDO::getPlatformType, platformEnum.getCode());
        }
        if (ObjectUtil.isNotNull(goodsSourceEnum)) {
            queryWrapper.lambda().eq(CartDO::getGoodSource, goodsSourceEnum.getCode());
        }

        if (includeEnum == CartIncludeEnum.SELECTED) {
            queryWrapper.lambda().eq(CartDO::getSelectedFlag, 1);
        } else if (includeEnum == CartIncludeEnum.UNSELECTED) {
            queryWrapper.lambda().eq(CartDO::getSelectedFlag, 0);
        }

        List<CartDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return list;
    }

    @Override
    public List<CartDO> listByDistributorEid(Long distributorEid, PlatformEnum platformEnum, CartGoodsSourceEnum goodsSourceEnum, CartIncludeEnum includeEnum) {
        QueryWrapper<CartDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(CartDO::getDistributorEid, distributorEid);

        if (ObjectUtil.isNotNull(platformEnum)) {
            queryWrapper.lambda().eq(CartDO::getPlatformType, platformEnum.getCode());
        }
        if (ObjectUtil.isNotNull(goodsSourceEnum)) {
            queryWrapper.lambda().eq(CartDO::getGoodSource, goodsSourceEnum.getCode());
        }

        if (includeEnum == CartIncludeEnum.SELECTED) {
            queryWrapper.lambda().eq(CartDO::getSelectedFlag, 1);
        } else if (includeEnum == CartIncludeEnum.UNSELECTED) {
            queryWrapper.lambda().eq(CartDO::getSelectedFlag, 0);
        }

        List<CartDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return list;
    }

    @Override
    public CartDO getCartGoodsInfo(GetCartGoodsInfoRequest getCartGoodsInfoRequest) {
        QueryWrapper<CartDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(CartDO::getBuyerEid, getCartGoodsInfoRequest.getBuyerEid())
                .eq(CartDO::getPlatformType, getCartGoodsInfoRequest.getPlatformEnum().getCode())
                .eq(CartDO::getGoodSource, getCartGoodsInfoRequest.getGoodsSourceEnum().getCode())
                .last("limit 1");
        if (getCartGoodsInfoRequest.getGoodsSkuId() != null && getCartGoodsInfoRequest.getGoodsSkuId() != 0l) {
            queryWrapper.lambda().eq(CartDO::getGoodsSkuId, getCartGoodsInfoRequest.getGoodsSkuId());
        }
        if (getCartGoodsInfoRequest.getGoodsId() != null && getCartGoodsInfoRequest.getGoodsId() != 0l) {
            queryWrapper.lambda().eq(CartDO::getGoodsId, getCartGoodsInfoRequest.getGoodsId());
        }
        if (getCartGoodsInfoRequest.getDistributorEid() != null && getCartGoodsInfoRequest.getDistributorEid() != 0L) {
            queryWrapper.lambda().eq(CartDO::getDistributorEid, getCartGoodsInfoRequest.getDistributorEid());
        }
        if (getCartGoodsInfoRequest.getPromotionActivityId() != null && getCartGoodsInfoRequest.getPromotionActivityId() != 0L) {
            queryWrapper.lambda().eq(CartDO::getPromotionActivityId, getCartGoodsInfoRequest.getPromotionActivityId());
        }
        if (getCartGoodsInfoRequest.getPromotionActivityTypeEnum() != null ) {
            queryWrapper.lambda().eq(CartDO::getPromotionActivityType, getCartGoodsInfoRequest.getPromotionActivityTypeEnum().getCode());
        }
        if (PlatformEnum.SALES_ASSIST == getCartGoodsInfoRequest.getPlatformEnum() && getCartGoodsInfoRequest.getOpUserId() != null) {
            queryWrapper.lambda().eq(CartDO::getCreateUser, getCartGoodsInfoRequest.getOpUserId());
        }
        return this.getOne(queryWrapper);
    }


    @Override
    public Integer sumGoodsQuantityByGoodId(GetCartGoodsInfoRequest getCartGoodsInfoRequest) {
        QueryWrapper<CartDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(CartDO::getDistributorGoodsId, getCartGoodsInfoRequest.getGoodsId())
                .eq(CartDO::getBuyerEid, getCartGoodsInfoRequest.getBuyerEid())
                .eq(CartDO::getPlatformType, getCartGoodsInfoRequest.getPlatformEnum().getCode())
                .eq(CartDO::getGoodSource, getCartGoodsInfoRequest.getGoodsSourceEnum().getCode());

        if (getCartGoodsInfoRequest.getDistributorEid() != null && getCartGoodsInfoRequest.getDistributorEid() != 0L) {
            queryWrapper.lambda().eq(CartDO::getDistributorEid, getCartGoodsInfoRequest.getDistributorEid());
        }

        if (getCartGoodsInfoRequest.getPromotionActivityTypeEnum() != null ) {
            queryWrapper.lambda().eq(CartDO::getPromotionActivityType, getCartGoodsInfoRequest.getPromotionActivityTypeEnum().getCode());
        }

        if (getCartGoodsInfoRequest.getCartIncludeEnum() != null ) {
            CartIncludeEnum includeEnum = getCartGoodsInfoRequest.getCartIncludeEnum();
            if (includeEnum == CartIncludeEnum.SELECTED) {
                queryWrapper.lambda().eq(CartDO::getSelectedFlag, 1);
            } else if (includeEnum == CartIncludeEnum.UNSELECTED) {
                queryWrapper.lambda().eq(CartDO::getSelectedFlag, 0);
            }
        }
        if (getCartGoodsInfoRequest.getGoodsSkuId() != null) {
            queryWrapper.lambda().ne(CartDO::getGoodsSkuId, getCartGoodsInfoRequest.getGoodsSkuId());
        }
        if (getCartGoodsInfoRequest.getPromotionActivityId() != null && getCartGoodsInfoRequest.getPromotionActivityId() != 0L) {
            queryWrapper.lambda().eq(CartDO::getPromotionActivityId, getCartGoodsInfoRequest.getPromotionActivityId());
        }
        List<CartDO> cartDOList = this.getBaseMapper().selectList(queryWrapper);
        if (CollectionUtil.isEmpty(cartDOList)) {
            return 0;
        }
        return cartDOList.stream().collect(Collectors.summingInt(CartDO::getQuantity));
    }


    @Override
    public Integer sumGoodsQuantityByGoodSkuId(GetCartGoodsInfoRequest getCartGoodsInfoRequest) {
        QueryWrapper<CartDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(CartDO::getDistributorGoodsId, getCartGoodsInfoRequest.getGoodsId())
                .eq(CartDO::getBuyerEid, getCartGoodsInfoRequest.getBuyerEid())
                .eq(CartDO::getPlatformType, getCartGoodsInfoRequest.getPlatformEnum().getCode())
                .eq(CartDO::getGoodSource, getCartGoodsInfoRequest.getGoodsSourceEnum().getCode());

        if (getCartGoodsInfoRequest.getDistributorEid() != null && getCartGoodsInfoRequest.getDistributorEid() != 0L) {
            queryWrapper.lambda().eq(CartDO::getDistributorEid, getCartGoodsInfoRequest.getDistributorEid());
        }

        if (getCartGoodsInfoRequest.getDistributorEid() != null && getCartGoodsInfoRequest.getDistributorEid() != 0L) {
            queryWrapper.lambda().eq(CartDO::getDistributorEid, getCartGoodsInfoRequest.getDistributorEid());
        }

        if (getCartGoodsInfoRequest.getPromotionActivityTypeEnum() != null ) {
            queryWrapper.lambda().eq(CartDO::getPromotionActivityType, getCartGoodsInfoRequest.getPromotionActivityTypeEnum().getCode());
        }

        if (getCartGoodsInfoRequest.getPromotionActivityId() != null && getCartGoodsInfoRequest.getPromotionActivityId() != 0L) {
            queryWrapper.lambda().eq(CartDO::getPromotionActivityId, getCartGoodsInfoRequest.getPromotionActivityId());
        }

        if (getCartGoodsInfoRequest.getCartIncludeEnum() != null ) {
            CartIncludeEnum includeEnum = getCartGoodsInfoRequest.getCartIncludeEnum();
            if (includeEnum == CartIncludeEnum.SELECTED) {
                queryWrapper.lambda().eq(CartDO::getSelectedFlag, 1);
            } else if (includeEnum == CartIncludeEnum.UNSELECTED) {
                queryWrapper.lambda().eq(CartDO::getSelectedFlag, 0);
            }
        }
        if (getCartGoodsInfoRequest.getGoodsSkuId() != null) {
            queryWrapper.lambda().eq(CartDO::getGoodsSkuId, getCartGoodsInfoRequest.getGoodsSkuId());
        }
        if (getCartGoodsInfoRequest.getOpUserId() != null) {
            queryWrapper.lambda().eq(CartDO::getCreateUser, getCartGoodsInfoRequest.getOpUserId());
        }
        List<CartDO> cartDOList = this.getBaseMapper().selectList(queryWrapper);
        if (CollectionUtil.isEmpty(cartDOList)) {
            return 0;
        }
        return cartDOList.stream().collect(Collectors.summingInt(CartDO::getQuantity));
    }


    @Override
    public List<CartDO> listByCreateUser(ListCartRequest listCartRequest) {
        QueryWrapper<CartDO> queryWrapper = new QueryWrapper<>();

        if (listCartRequest.getBuyerEid() != null && listCartRequest.getBuyerEid() != 0) {
            queryWrapper.lambda().eq(CartDO::getBuyerEid, listCartRequest.getBuyerEid());
        }
        if (listCartRequest.getDistributorEid() != null && listCartRequest.getDistributorEid() != 0) {
            queryWrapper.lambda().eq(CartDO::getDistributorEid, listCartRequest.getDistributorEid());
        }
        if (listCartRequest.getPlatformEnum() != null) {
            queryWrapper.lambda().eq(CartDO::getPlatformType, listCartRequest.getPlatformEnum().getCode());
        }
        if (listCartRequest.getGoodsSourceEnum() != null) {
            queryWrapper.lambda().eq(CartDO::getGoodSource, listCartRequest.getGoodsSourceEnum().getCode());
        }
        if (listCartRequest.getCartIncludeEnum() != null) {
            if (listCartRequest.getCartIncludeEnum() == CartIncludeEnum.SELECTED) {
                queryWrapper.lambda().eq(CartDO::getSelectedFlag, 1);
            } else if (listCartRequest.getCartIncludeEnum() == CartIncludeEnum.UNSELECTED) {
                queryWrapper.lambda().eq(CartDO::getSelectedFlag, 0);
            }
        }
        if (listCartRequest.getPromotionActivityTypeEnum() != null) {
            queryWrapper.lambda().eq(CartDO::getPromotionActivityType, listCartRequest.getPromotionActivityTypeEnum().getCode());
        }
        if (listCartRequest.getCreateUser() != null) {
            queryWrapper.lambda().eq(CartDO::getCreateUser, listCartRequest.getCreateUser());
        }
        List<CartDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return list;
    }


    @Override
    public Integer getCartGoodsNumByCreateUser(ListCartRequest listCartRequest) {
        QueryWrapper<CartDO> queryWrapper = new QueryWrapper<>();

        if (listCartRequest.getBuyerEid() != null && listCartRequest.getBuyerEid() != 0) {
            queryWrapper.lambda().eq(CartDO::getBuyerEid, listCartRequest.getBuyerEid());
        }
        if (listCartRequest.getDistributorEid() != null && listCartRequest.getDistributorEid() != 0) {
            queryWrapper.lambda().eq(CartDO::getDistributorEid, listCartRequest.getDistributorEid());
        }
        if (listCartRequest.getPlatformEnum() != null) {
            queryWrapper.lambda().eq(CartDO::getPlatformType, listCartRequest.getPlatformEnum().getCode());
        }
        if (listCartRequest.getGoodsSourceEnum() != null) {
            queryWrapper.lambda().eq(CartDO::getGoodSource, listCartRequest.getGoodsSourceEnum().getCode());
        }
        if (listCartRequest.getCartIncludeEnum() != null) {
            if (listCartRequest.getCartIncludeEnum() == CartIncludeEnum.SELECTED) {
                queryWrapper.lambda().eq(CartDO::getSelectedFlag, 1);
            } else if (listCartRequest.getCartIncludeEnum() == CartIncludeEnum.UNSELECTED) {
                queryWrapper.lambda().eq(CartDO::getSelectedFlag, 0);
            }
        }
        if (listCartRequest.getPromotionActivityTypeEnum() != null) {
            queryWrapper.lambda().eq(CartDO::getPromotionActivityType, listCartRequest.getPromotionActivityTypeEnum().getCode());
        }
        if (listCartRequest.getCreateUser() != null) {
            queryWrapper.lambda().eq(CartDO::getCreateUser, listCartRequest.getCreateUser());
        }

        return this.count(queryWrapper);
    }
    /**
     * 校验可控商品
     * @param buyerEid
     * @param gidList
     */
    private void checkB2BGoodsByGids(Long buyerEid,List<Long> gidList) {
        log.info("checkB2BGoodsByGids request->{}", JSON.toJSON(gidList));
        Map<Long, Integer> goodsListResult = agreementBusinessApi.getB2bGoodsLimitByGids(gidList,buyerEid);
        log.info("checkB2BGoodsByGids result->{}",JSON.toJSON(goodsListResult));
        // 控销商品无法结算
        if (MapUtil.isEmpty(goodsListResult)) {
            throw new BusinessException(OrderErrorCode.LIMIT_GOODS_SALE_ERROR);
        }
        // 是否管控商品
        if (goodsListResult.values().stream().anyMatch(value -> GoodsLimitStatusEnum.CONTROL_GOODS == GoodsLimitStatusEnum.getByCode(value))){
            throw new BusinessException(OrderErrorCode.LIMIT_GOODS_SALE_ERROR);
        }
        // 是否建立采购关系
        if (goodsListResult.values().stream().anyMatch(value -> GoodsLimitStatusEnum.NOT_RELATION_SHIP == GoodsLimitStatusEnum.getByCode(value))) {
            throw new BusinessException(OrderErrorCode.NOT_RELATION_SHIP_ERROR);
        }
    }

    @Override
    public boolean addToCartByCombinationProduct(CombinationAddToCartRequest request) {
        GetCartGoodsInfoRequest getCartGoodsInfoRequest = new GetCartGoodsInfoRequest();
        getCartGoodsInfoRequest.setBuyerEid(request.getBuyerEid());
        getCartGoodsInfoRequest.setPlatformEnum(request.getPlatformEnum());
        getCartGoodsInfoRequest.setGoodsSourceEnum(request.getGoodsSourceEnum());
        getCartGoodsInfoRequest.setPromotionActivityTypeEnum(PromotionActivityTypeEnum.COMBINATION);
        getCartGoodsInfoRequest.setPromotionActivityId(request.getPromotionActivityId());
        getCartGoodsInfoRequest.setOpUserId(request.getOpUserId());
        // 查询出购物车信息数据
        CartDO  entity = this.getCartGoodsInfo(getCartGoodsInfoRequest);
        PromotionActivityRequest promotionActivityRequest = new PromotionActivityRequest();
        promotionActivityRequest.setBuyerEid(request.getBuyerEid());
        promotionActivityRequest.setGoodsPromotionActivityIdList(ListUtil.toList(request.getPromotionActivityId()));
        List<PromotionDTO>  promotionDTOS = promotionActivityApi.queryPromotionInfoByActivityAndBuyerId(promotionActivityRequest);
        if (CollectionUtil.isEmpty(promotionDTOS)) {
            throw new BusinessException(OrderErrorCode.ORDER_PROMOTION_ERROR);
        }

        PromotionDTO promotionDTO = promotionDTOS.get(0);

        GetCartGoodsInfoRequest limitRequest = new GetCartGoodsInfoRequest();
        limitRequest.setGoodsId(0l);
        limitRequest.setBuyerEid(request.getBuyerEid());
        limitRequest.setDistributorEid(request.getDistributorEid());
        limitRequest.setGoodsSourceEnum(request.getGoodsSourceEnum());
        limitRequest.setPlatformEnum(request.getPlatformEnum());
        limitRequest.setPromotionActivityTypeEnum(PromotionActivityTypeEnum.COMBINATION);
        limitRequest.setPromotionActivityId(request.getPromotionActivityId());

        // 组合商品校验
        this.validateCombinationOrderLimit(limitRequest,request.getQuantity(),promotionDTO,false);

        // 校验商品库存以及状态
        for (PromotionGoodsLimitDTO t : promotionDTO.getGoodsLimitDTOList()) {
            AddToCartRequest addToCartRequest = new AddToCartRequest();
            addToCartRequest.setPromotionActivityType(PromotionActivityTypeEnum.COMBINATION.getCode());
            addToCartRequest.setOpUserId(request.getOpUserId());
            addToCartRequest.setGoodsSkuId(t.getGoodsSkuId());
            addToCartRequest.setDistributorGoodsId(t.getGoodsId());
            addToCartRequest.setDistributorEid(t.getEid());
            addToCartRequest.setGoodsSourceEnum(request.getGoodsSourceEnum());
            addToCartRequest.setPlatformEnum(request.getPlatformEnum());
            addToCartRequest.setPromotionActivityId(request.getPromotionActivityId());
            addToCartRequest.setBuyerEid(request.getBuyerEid());
            addToCartRequest.setQuantity(entity != null ? (entity.getQuantity() + request.getQuantity()) * t.getAllowBuyCount() : request.getQuantity() * t.getAllowBuyCount());
            this.cartAddValidate(addToCartRequest);
        }
        if (entity == null) {
            entity = new CartDO();
            entity.setBuyerEid(request.getBuyerEid());
            entity.setSellerEid(request.getDistributorEid());
            entity.setDistributorEid(request.getDistributorEid());
            entity.setGoodSource(request.getGoodsSourceEnum().getCode());
            entity.setQuantity(request.getQuantity());
            entity.setPlatformType(request.getPlatformEnum().getCode());
            entity.setPromotionActivityId(request.getPromotionActivityId());
            entity.setPromotionActivityType(PromotionActivityTypeEnum.COMBINATION.getCode());
            entity.setSelectedFlag(1);
            entity.setOpUserId(request.getOpUserId());
            return this.save(entity);
        } else {
            entity.setQuantity(entity.getQuantity() + request.getQuantity());
            entity.setSelectedFlag(1);
            entity.setOpUserId(request.getOpUserId());
            return this.updateById(entity);
        }
    }


    /**
     * 从订单历史中添加购物车商品信息,上层已经做了基本校验,无需处理校验，直接加入到购物车中
     * @param request
     * @return
     */
    @Override
    public boolean historyAddToCart(BatchAddToCartRequest request) {

        List<BatchAddToCartRequest.QuickPurchaseInfoDTO> quickPurchaseInfoList = request.getQuickPurchaseInfoList();

        if (CollUtil.isEmpty(quickPurchaseInfoList)) {

            return false;
        }

        quickPurchaseInfoList.forEach(e -> {
            this.saveOrUpdate(e.getGoodsSkuId(), e.getGoodsId(), e.getQuantity(), e.getDistributorEid(), e.getDistributorGoodsId(),
                    request.getBuyerEid(), request.getPlatformEnum(), request.getGoodsSourceEnum(),PromotionActivityTypeEnum.NORMAL.getCode(),request.getOpUserId());
        });

        return true;
    }
}
