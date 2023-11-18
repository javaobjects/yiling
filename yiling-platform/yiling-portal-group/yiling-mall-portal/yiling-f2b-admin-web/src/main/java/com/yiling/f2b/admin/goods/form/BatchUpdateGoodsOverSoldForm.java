package com.yiling.f2b.admin.goods.form;

import java.util.List;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: chen.shi
 * @date: 2021/12/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class BatchUpdateGoodsOverSoldForm extends BaseForm {

    /**
     * 商品ID集合
     */
    @ApiModelProperty(value = "商品集合")
    private List<Long> goodsIds;

    /**
     * 是否超卖
     */
    @ApiModelProperty(value = "是否超卖 0-非超卖  1-超卖", example = "1")
    private Integer overSoldType;
}
