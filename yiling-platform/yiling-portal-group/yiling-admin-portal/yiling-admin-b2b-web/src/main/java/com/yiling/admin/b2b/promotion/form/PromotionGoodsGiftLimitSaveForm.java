package com.yiling.admin.b2b.promotion.form;

import java.math.BigDecimal;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 促销活动-赠品
 * @author: yong.zhang
 * @date: 2021/11/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "PromotionGoodsGiftLimitSaveForm", description = "促销活动-赠品")
public class PromotionGoodsGiftLimitSaveForm extends BaseForm {

    @ApiModelProperty(value = "满赠金额")
    private BigDecimal promotionAmount;

    @ApiModelProperty(value = "赠送商品ID")
    private Long       goodsGiftId;

    @ApiModelProperty(value = "活动库存")
    private Integer    promotionStock;

}
