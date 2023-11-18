package com.yiling.mall.cart.api;

import java.util.List;

import com.yiling.framework.common.enums.PlatformEnum;
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

/**
 * 进货单 API
 *
 * @author: xuan.zhou
 * @date: 2021/6/17
 */
public interface CartApi {

    /**
     * 获取购物车商品个数
     *
     * @param buyerEid 买家企业ID
     * @param platformEnum 平台枚举
     * @param goodsSourceEnum 商品来源类型
     * @return
     */
    Integer getCartGoodsNum(Long buyerEid, PlatformEnum platformEnum, CartGoodsSourceEnum goodsSourceEnum);

    /**
     * 添加商品到购物车
     *
     * @param request
     * @return
     */
    boolean addToCart(AddToCartRequest request);

    /**
     * 添加组合商品信息
     * @param request
     * @return
     */
    boolean addToCartByCombinationProduct(CombinationAddToCartRequest request);

    /**
     * 批量添加商品到购物车
     *
     * @param request
     * @return
     */
    boolean batchAddToCart(BatchAddToCartRequest request);

    /**
     * 特价&秒杀商品添加购物车
     * @param request
     * @return
     */
    boolean addToCartBySpecialProduct(AddToCartRequest request);
    /**
     * 移除购物车商品
     *
     * @param request
     * @return
     */
    boolean removeCartGoods(RemoveCartGoodsRequest request);

    /**
     * 修改购物车商品数量
     *
     * @param request
     * @return
     */
    boolean updateCartGoodsQuantity(UpdateCartGoodsQuantityRequest request);

    /**
     * 修改购物数量,无需处理任何校验逻辑(主要用于购物车自动刷新库存逻辑)
     * @param request
     * @return
     */
    boolean modifyCartGoodsQuantity(UpdateCartGoodsQuantityRequest request);

    /**
     * 勾选购物车商品
     *
     * @param request
     * @return
     */
    boolean selectCartGoods(SelectCartGoodsRequest request);

    /**
     * 获取买家购物车列表
     *
     * @param buyerEid 买家企业ID
     * @param platformEnum 平台枚举
     * @param goodsSourceEnum 商品来源
     * @param includeEnum 包含数据范围枚举
     * @return
     */
    List<CartDTO> listByBuyerEid(Long buyerEid, PlatformEnum platformEnum, CartGoodsSourceEnum goodsSourceEnum, CartIncludeEnum includeEnum);

    /**
     * 获取配送商的购物车列表
     * @param distributorEid 配送商企业ID
     * @param platformEnum 平台枚举
     * @param goodsSourceEnum 商品来源
     * @param includeEnum 包含数据范围枚举
     * @return
     */
    List<CartDTO> listByDistributorEid(Long distributorEid, PlatformEnum platformEnum, CartGoodsSourceEnum goodsSourceEnum, CartIncludeEnum includeEnum);

    /**
     * 查询购物车信息
     * @param listCartRequest 传递参数
     * @return
     */
    List<CartDTO> listByCreateUser(ListCartRequest listCartRequest);


    /**
     * 获取购物车数量
     * @param listCartRequest
     * @return
     */
    Integer getCartGoodsNumByCreateUser(ListCartRequest listCartRequest);


    /**
     * 获取购物车商品信息
     *
     * @param getCartGoodsInfoRequest 获取购物车参数
     * @return
     */
    CartDTO getCartGoodsInfo(GetCartGoodsInfoRequest getCartGoodsInfoRequest);


    /**
     * 统计商品数量
     * @param getCartGoodsInfoRequest
     * @return
     */
    Integer sumGoodsQuantityByGoodId(GetCartGoodsInfoRequest getCartGoodsInfoRequest);


    /**
     *  从客户购买历史中添加购物车商品信息
     * @param request
     * @return
     */
    boolean historyAddToCart(BatchAddToCartRequest request);

}
