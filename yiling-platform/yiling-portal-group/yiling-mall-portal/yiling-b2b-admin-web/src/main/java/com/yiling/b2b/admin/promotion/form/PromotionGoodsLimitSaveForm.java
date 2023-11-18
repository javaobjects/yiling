package com.yiling.b2b.admin.promotion.form;

import java.math.BigDecimal;

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
public class PromotionGoodsLimitSaveForm extends BaseForm {

    @ApiModelProperty(value = "商品所属企业ID")
    private Long eid;

    @ApiModelProperty(value = "商品所属企业名称")
    private String ename;

    @ApiModelProperty(value = "商品ID")
    private Long goodsId;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "满赠金额")
    private BigDecimal giftAmountLimit;
}
