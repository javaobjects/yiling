package com.yiling.admin.b2b.goods.form;

import java.util.List;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/6/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class BatchUpdateGoodsStatusForm extends BaseForm {

    /**
     * 商品ID集合
     */
    @ApiModelProperty(value = "商品集合")
    private List<Long> goodsIds;
    /**
     * 商品状态
     */
    @ApiModelProperty(value = "商品状态")
    private Integer    goodsStatus;

}
