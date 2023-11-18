package com.yiling.f2b.admin.goods.form;

import java.math.BigDecimal;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 SaveOrUpdateLimitPriceForm
 * @描述
 * @创建时间 2023/1/4
 * @修改人 shichen
 * @修改时间 2023/1/4
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveOrUpdateLimitPriceForm extends BaseForm {

    @ApiModelProperty(value = "限价ID", example = "1")
    private Long id;

    @ApiModelProperty(value = "商品ID", example = "1")
    private Long goodsId;

    /**
     * 价格上限
     */
    @ApiModelProperty(value = "价格上限", example = "9.99")
    private BigDecimal upperLimitPrice;

    /**
     * 价格下限
     */
    @ApiModelProperty(value = "价格下限", example = "9.99")
    private BigDecimal lowerLimitPrice;
}
