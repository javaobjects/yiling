package com.yiling.mall.cart.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/** 预售快速购买DTO
 * @author zhigang.guo
 * @date: 2022/10/9
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QuickBuyGoodsDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

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
     * 预售活动Id
      */
    private Long promotionActivityId;

    /**
     * 促销活动类型：0正常：2特价,3秒杀 6预售
     */
    private Integer promotionActivityType;

    /**
     * 预售活动类型:1-定金预售 2-全额预售
     */
    private Integer presaleActivityType;

}
