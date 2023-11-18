package com.yiling.b2b.admin.goods.form;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 客户定价
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveGoodsPriceCustomerForm extends BaseForm {

    /**
     * 企业ID
     */
    @ApiModelProperty(value = "企业ID")
    @NotNull
    private Long eid;

    /**
     * 客户企业ID
     */
    @ApiModelProperty(value = "客户企业ID")
    @NotNull(message = "客户企业ID不能为空")
    private Long customerEid;

    /**
     * 商品ID
     */
    @ApiModelProperty(value = "商品ID")
    @NotNull(message = "商品ID不能为空")
    private Long goodsId;

    /**
     * 定价规则：1-浮动点位 2-具体价格
     */
    @ApiModelProperty(value = "定价规则：1-浮动点位 2-具体价格")
    @NotNull(message = "定价规则不能为空")
    private Integer priceRule;

    /**
     * 浮动点位/价格
     */
    @ApiModelProperty(value = "浮动点位/价格")
    @NotNull(message = "浮动点位/价格不能为空")
    private BigDecimal priceValue;

}
