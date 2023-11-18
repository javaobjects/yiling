package com.yiling.b2b.admin.promotion.form;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/11/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PromotionGoodsGiftLimitSaveForm extends BaseForm {

    @ApiModelProperty(value = "赠送商品ID")
    private Long goodsGiftId;

    @ApiModelProperty(value = "参与活动商品数量")
    private Integer activityQuantity;

    @ApiModelProperty(value = "已经参与活动数量")
    private Integer promotionQuantity;
}
