package com.yiling.mall.cart.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 购物车 DTO
 *
 * @author: xuan.zhou
 * @date: 2021/6/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CartDTO extends BaseDTO {

    /**
     * 买家企业ID
     */
    private Long buyerEid;

    /**
     * 卖家企业ID
     */
    private Long sellerEid;

    /**
     * 配送商企业ID
     */
    private Long distributorEid;

    /**
     * 配送商商品ID
     */
    private Long distributorGoodsId;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 商品SKUid
     */
    private Long goodsSkuId;

    /**
     * 购买数量
     */
    private Integer quantity;

    /**
     * 平台类型：1-POP 2-B2B
     */
    private Integer platformType;

    /**
     * 购物车来源：1-POP,2-B2B,3-ERP,4-互联网医院 @see GoodsSource
     */
    private Integer goodSource;

    /**
     * 促销活动类型
     * @see com.yiling.order.order.enums.PromotionActivityTypeEnum
     */
    private Integer promotionActivityType;

    /**
     * 促销活动ID
     */
    private Long promotionActivityId;

    /**
     * 是否选中：0-否 1-是
     */
    private Integer selectedFlag;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;
}
