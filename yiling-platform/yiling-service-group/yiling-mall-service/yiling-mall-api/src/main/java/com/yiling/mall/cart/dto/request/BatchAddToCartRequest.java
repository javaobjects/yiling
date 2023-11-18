package com.yiling.mall.cart.dto.request;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.mall.cart.enums.CartGoodsSourceEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 批量添加商品到购物车 Request
 *
 * @author: xuan.zhou
 * @date: 2021/6/30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class BatchAddToCartRequest extends BaseRequest {

    private static final long serialVersionUID = 5213503804217901414L;

    /**
     * 快速采购信息集合
     */
    @NotEmpty
    private List<QuickPurchaseInfoDTO> quickPurchaseInfoList;

    /**
     * 买家企业ID
     */
    @NotNull
    private Long buyerEid;

    /**
     * 平台类型
     */
    @NotNull
    private PlatformEnum platformEnum;

    /**
     * 商品来源
     */
    @NotNull
    private CartGoodsSourceEnum goodsSourceEnum;



    @Data
    public static class QuickPurchaseInfoDTO implements java.io.Serializable {

        private static final long serialVersionUID = 4313606147239475834L;

        /**
         * 商品ID
         */
        @NotNull
        private Long goodsId;

        /**
         * 购买数量
         */
        @NotNull
        private Integer quantity;

        /**
         * 配送商企业ID
         */
        @NotNull
        private Long distributorEid;

        /**
         * 配送商商品ID
         */
        @NotNull
        private Long distributorGoodsId;

        /**
         * 商品skuId
         */
        @NotNull
        private Long goodsSkuId;


    }
}
