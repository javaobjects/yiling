package com.yiling.admin.b2b.promotion.vo;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 促销活动赠品表
 * </p>
 *
 * @author: yong.zhang
 * @date: 2021/11/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PromotionGoodsGiftLimitVO extends BaseVO {
    
    @ApiModelProperty(value = "促销活动ID")
    private Long promotionActivityId;

    @ApiModelProperty(value = "满赠金额")
    private BigDecimal promotionAmount;

    @ApiModelProperty(value = "赠送商品ID")
    private Long goodsGiftId;

    @ApiModelProperty(value = "参与活动商品数量")
    private Integer promotionStock;

    @ApiModelProperty(value = "已经参与活动商品数量")
    private Integer usedStock;

    @ApiModelProperty(value = "赠品名称")
    private String giftName;

}
