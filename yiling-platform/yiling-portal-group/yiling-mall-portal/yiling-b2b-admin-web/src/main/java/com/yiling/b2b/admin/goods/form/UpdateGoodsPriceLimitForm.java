package com.yiling.b2b.admin.goods.form;

import java.math.BigDecimal;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/10/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateGoodsPriceLimitForm extends BaseForm {

    @ApiModelProperty(value = "商品限价条件Id", example = "11")
    private Long id;

    @ApiModelProperty(value = "客户类型", example = "11")
    private Integer customerType;

    /**
     * 省
     */
    @ApiModelProperty(value = "省", example = "11")
    private String provinceCode;

    /**
     * 市
     */
    @ApiModelProperty(value = "市", example = "11")
    private String cityCode;

    /**
     * 区
     */
    @ApiModelProperty(value = "区", example = "11")
    private String regionCode;

    /**
     * 价格
     */
    @ApiModelProperty(value = "价格", example = "11")
    private BigDecimal price;
}
