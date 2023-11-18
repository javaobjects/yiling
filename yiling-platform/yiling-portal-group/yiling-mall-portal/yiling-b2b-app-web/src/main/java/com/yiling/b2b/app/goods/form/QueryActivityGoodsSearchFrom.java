package com.yiling.b2b.app.goods.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/11/9
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryActivityGoodsSearchFrom extends QueryPageListForm {
    /**
     * 搜索词
     */
    @ApiModelProperty(value = "搜索词")
    private String key;

    /**
     * 商品Id
     */
    @ApiModelProperty(value = "商品Id")
    private Long goodsId;

    /**
     * 店铺eid
     */
    @ApiModelProperty(value = "店铺eid")
    private Long eid;

}
