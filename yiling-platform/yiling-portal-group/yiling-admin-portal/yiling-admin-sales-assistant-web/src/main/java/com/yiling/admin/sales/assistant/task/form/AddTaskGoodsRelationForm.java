package com.yiling.admin.sales.assistant.task.form;

import java.math.BigDecimal;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: ray
 * @date: 2021/9/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddTaskGoodsRelationForm extends BaseForm {
    @ApiModelProperty(value = "任务商品基价")
    private  BigDecimal salePrice;
    @ApiModelProperty(value = "商品主键")
    private Long goodsId;
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "佣金")
    private BigDecimal commission;
/*    @ApiModelProperty(value = "佣金比例")
    private String  commissionRate;*/

    @ApiModelProperty(value = "出货价")
    private BigDecimal outPrice;

    /**
     * 商销价
     */
    @ApiModelProperty(value = "商销价")
    private BigDecimal  sellPrice;
}