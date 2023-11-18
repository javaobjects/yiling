package com.yiling.mall.cart.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.mall.cart.api.CartApi;
import com.yiling.mall.cart.dto.CartDTO;
import com.yiling.mall.cart.dto.request.AddToCartRequest;
import com.yiling.mall.cart.dto.request.BatchAddToCartRequest;
import com.yiling.mall.cart.dto.request.CombinationAddToCartRequest;
import com.yiling.mall.cart.dto.request.GetCartGoodsInfoRequest;
import com.yiling.mall.cart.dto.request.ListCartRequest;
import com.yiling.mall.cart.dto.request.RemoveCartGoodsRequest;
import com.yiling.mall.cart.dto.request.SelectCartGoodsRequest;
import com.yiling.mall.cart.dto.request.UpdateCartGoodsQuantityRequest;
import com.yiling.mall.cart.enums.CartGoodsSourceEnum;
import com.yiling.mall.cart.enums.CartIncludeEnum;
import com.yiling.mall.cart.service.CartService;

import io.jsonwebtoken.lang.Assert;

/**
 * 进货单 API 实现
 *
 * @author: xuan.zhou
 * @date: 2021/6/17
 */
@DubboService
public class CartApiImpl implements CartApi {

    @Autowired
    CartService cartService;

    @Override
    public Integer getCartGoodsNum(Long buyerEid, PlatformEnum platformEnum, CartGoodsSourceEnum goodsSourceEnum) {
        return cartService.getCartGoodsNum(buyerEid, platformEnum,goodsSourceEnum);
    }

    @Override
    public boolean addToCart(AddToCartRequest request) {
        return cartService.addToCart(request);
    }

    @Override
    public boolean batchAddToCart(BatchAddToCartRequest request) {
        return cartService.batchAddToCart(request);
    }

    @Override
    public boolean removeCartGoods(RemoveCartGoodsRequest request) {
        return cartService.removeByIds(request.getIds());
    }

    @Override
    public boolean updateCartGoodsQuantity(UpdateCartGoodsQuantityRequest request) {
        return cartService.updateCartGoodsQuantity(request);
    }

    @Override
    public boolean modifyCartGoodsQuantity(UpdateCartGoodsQuantityRequest request) {

        return cartService.modifyCartGoodsQuantity(request);
    }

    @Override
    public boolean selectCartGoods(SelectCartGoodsRequest request) {
        return cartService.selectCartGoods(request);
    }

    @Override
    public List<CartDTO> listByBuyerEid(Long buyerEid, PlatformEnum platformEnum, CartGoodsSourceEnum goodsSourceEnum, CartIncludeEnum includeEnum) {
        Assert.notNull(buyerEid, "参数buyerEid不能为空");
        Assert.notNull(includeEnum, "参数includeEnum不能为空");
        return PojoUtils.map(cartService.listByBuyerEid(buyerEid, platformEnum, goodsSourceEnum,includeEnum), CartDTO.class);
    }


    @Override
    public List<CartDTO> listByDistributorEid(Long distributorEid, PlatformEnum platformEnum, CartGoodsSourceEnum goodsSourceEnum, CartIncludeEnum includeEnum) {
        Assert.notNull(distributorEid, "参数buyerEid不能为空");
        Assert.notNull(includeEnum, "参数includeEnum不能为空");


        return PojoUtils.map(cartService.listByDistributorEid(distributorEid, platformEnum, goodsSourceEnum,includeEnum), CartDTO.class);
    }

    @Override
    public CartDTO getCartGoodsInfo(GetCartGoodsInfoRequest getCartGoodsInfoRequest) {

        Assert.notNull(getCartGoodsInfoRequest.getGoodsSkuId(), "参数goodsSkuId不能为空");
        Assert.notNull(getCartGoodsInfoRequest.getGoodsId(), "参数goodsId不能为空");
        Assert.notNull(getCartGoodsInfoRequest.getBuyerEid(), "参数buyerEid不能为空");
        Assert.notNull(getCartGoodsInfoRequest.getDistributorEid(), "参数distributorEid不能为空");
        Assert.notNull(getCartGoodsInfoRequest.getPlatformEnum(), "参数platformEnum不能为空");

        return PojoUtils.map(cartService.getCartGoodsInfo(getCartGoodsInfoRequest), CartDTO.class);
    }


    @Override
    public Integer sumGoodsQuantityByGoodId(GetCartGoodsInfoRequest getCartGoodsInfoRequest) {
        Assert.notNull(getCartGoodsInfoRequest.getGoodsId(), "参数goodsId不能为空");
        Assert.notNull(getCartGoodsInfoRequest.getBuyerEid(), "参数buyerEid不能为空");
        Assert.notNull(getCartGoodsInfoRequest.getDistributorEid(), "参数distributorEid不能为空");
        Assert.notNull(getCartGoodsInfoRequest.getPlatformEnum(), "参数platformEnum不能为空");
        return cartService.sumGoodsQuantityByGoodId(getCartGoodsInfoRequest);
    }

    @Override
    public boolean addToCartBySpecialProduct(AddToCartRequest request) {

        return cartService.addToCartBySpecialProduct(request);
    }


    @Override
    public List<CartDTO> listByCreateUser(ListCartRequest listCartRequest) {

         return PojoUtils.map(cartService.listByCreateUser(listCartRequest), CartDTO.class);
    }

    @Override
    public Integer getCartGoodsNumByCreateUser(ListCartRequest listCartRequest) {

        return cartService.getCartGoodsNumByCreateUser(listCartRequest);
    }


    @Override
    public boolean addToCartByCombinationProduct(CombinationAddToCartRequest request) {

        return cartService.addToCartByCombinationProduct(request);
    }


    @Override
    public boolean historyAddToCart(BatchAddToCartRequest request) {


        return cartService.historyAddToCart(request);
    }
}
