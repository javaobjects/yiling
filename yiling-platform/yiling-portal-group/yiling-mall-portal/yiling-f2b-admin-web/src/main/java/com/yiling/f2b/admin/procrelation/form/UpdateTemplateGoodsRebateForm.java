package com.yiling.f2b.admin.procrelation.form;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2023/6/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateTemplateGoodsRebateForm extends BaseForm {

    /**
     * 商品优惠折扣
     */
    @ApiModelProperty(value = "商品优惠折扣")
    private BigDecimal rebate;

    /**
     * 采购关系商品id
     */
    @ApiModelProperty(value = "id列表")
    private List<Long> idList;
}
