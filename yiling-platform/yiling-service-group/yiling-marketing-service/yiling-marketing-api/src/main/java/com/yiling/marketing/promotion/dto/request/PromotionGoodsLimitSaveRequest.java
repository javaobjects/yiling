package com.yiling.marketing.promotion.dto.request;

import java.io.Serializable;
import java.math.BigDecimal;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/11/4
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PromotionGoodsLimitSaveRequest extends BaseRequest implements Serializable {

    /**
     * 商品所属企业ID
     */
    private Long       eid;

    /**
     * 商品所属企业名称
     */
    private String     ename;

    /**
     * 商品ID
     */
    private Long       goodsId;

    /**
     * 商品名称
     */
    private String     goodsName;

    /**
     * 满赠金额
     */
    private BigDecimal giftAmountLimit;

    /**
     * 销售价格
     */
    private BigDecimal price;
    /**
     * 活动价格
     */
    private BigDecimal promotionPrice;

    /**
     * 允许购买数量
     */
    private Integer    allowBuyCount;

    /**
     * 活动库存
     */
    private Integer    promotionStock;

    /**
     * 组合包总价
     */
    private BigDecimal    packageTotalPrice;

    /**
     * 商品关联skuid
     */
    private Long goodsSkuId;
}
