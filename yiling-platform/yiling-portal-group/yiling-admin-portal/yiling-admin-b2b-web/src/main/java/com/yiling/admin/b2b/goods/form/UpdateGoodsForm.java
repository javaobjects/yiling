package com.yiling.admin.b2b.goods.form;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-05-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateGoodsForm extends BaseForm {

    @NotEmpty
    @ApiModelProperty(value = "商品ID", example = "1111")
    private Long goodsId;

    @NotEmpty
    @ApiModelProperty(value = "基价", example = "1111")
    private BigDecimal price;

    @NotEmpty
    @ApiModelProperty(value = "上下架", example = "1111")
    private Integer goodsStatus;

    @NotEmpty
    @ApiModelProperty(value = "包装集合", example = "1111")
    private List<UpdateGoodsSkuForm> goodsSkuList;
}
