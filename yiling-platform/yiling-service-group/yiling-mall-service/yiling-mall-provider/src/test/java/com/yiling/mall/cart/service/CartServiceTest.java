package com.yiling.mall.cart.service;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.mall.BaseTest;
import com.yiling.mall.cart.dto.request.AddToCartRequest;
import com.yiling.mall.cart.dto.request.CombinationAddToCartRequest;
import com.yiling.mall.cart.dto.request.ListCartRequest;
import com.yiling.mall.cart.dto.request.UpdateCartGoodsQuantityRequest;
import com.yiling.mall.cart.enums.CartGoodsSourceEnum;
import com.yiling.mall.cart.enums.CartIncludeEnum;

/**
 * @author: xuan.zhou
 * @date: 2021/6/18
 */
public class CartServiceTest extends BaseTest {

    @Autowired
    CartService cartService;

    /* @Test
     public void addToCart() {
         AddToCartRequest request = new AddToCartRequest();
         request.setGoodsId(1L);
         request.setQuantity(1);
         request.setDistributorEid(2L);
         request.setBuyerEid(3L);
         request.setPlatformEnum(PlatformEnum.POP);
         request.setOpUserId(1L);

         boolean result = cartService.addToCart(request);
         Assert.assertTrue(result);
     }*/
 /*   @Test
    public void getCartGoodsNumByCreateUser() {
        ListCartRequest request = new ListCartRequest();
        request.setBuyerEid(316l);
        request.setPlatformEnum(PlatformEnum.SALES_ASSIST);
        request.setGoodsSourceEnum(CartGoodsSourceEnum.B2B);
        request.setCreateUser(0l);
        request.setCartIncludeEnum(CartIncludeEnum.SELECTED);

        Integer cartNum = cartService.getCartGoodsNumByCreateUser(request);

        System.out.println(cartNum + "..");
    }
*/
    @Test
    public void addToCartByCombinationProduct() {
        CombinationAddToCartRequest request = new CombinationAddToCartRequest();
        request.setBuyerEid(54l);
        request.setPlatformEnum(PlatformEnum.B2B);
        request.setGoodsSourceEnum(CartGoodsSourceEnum.B2B);
        request.setDistributorEid(35l);
        request.setQuantity(7);
        request.setOpUserId(1L);
        request.setPromotionActivityId(84l);
        boolean result = cartService.addToCartByCombinationProduct(request);
        Assert.assertTrue(result);
    }

    @Test
    public void updateCart() {
        UpdateCartGoodsQuantityRequest request = new UpdateCartGoodsQuantityRequest();
        request.setId(346l);
        request.setQuantity(8);

        boolean result = cartService.updateCartGoodsQuantity(request);
        Assert.assertTrue(result);
    }

}
