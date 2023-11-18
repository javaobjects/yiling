package com.yiling.admin.b2b.promotion.form;

import java.math.BigDecimal;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 促销活动-商品
 * @author: yong.zhang
 * @date: 2021/11/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "PromotionGoodsLimitSaveForm", description = "促销活动商品信息")
public class PromotionGoodsLimitSaveForm extends BaseForm {

    @ApiModelProperty(value = "商品所属企业ID")
    private Long       eid;

    @ApiModelProperty(value = "商品所属企业名称")
    private String     ename;

    @ApiModelProperty(value = "商品ID")
    private Long       goodsId;

    @ApiModelProperty(value = "商品名称")
    private String     goodsName;

    @ApiModelProperty(value = "销售价格")
    private BigDecimal price;

    @ApiModelProperty(value = "活动价格")
    private BigDecimal promotionPrice;

    @ApiModelProperty(value = "允许购买数量")
    private Integer    allowBuyCount;

    @ApiModelProperty(value = "活动库存")
    private Integer    promotionStock;

    @ApiModelProperty(value = "组合包商品总价")
    private BigDecimal    packageTotalPrice;

    @ApiModelProperty(value = "商品关联skuid")
    private Long goodsSkuId;
}
