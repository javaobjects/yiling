package com.yiling.b2b.admin.goods.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

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
public class QueryGoodsPriceLimitPageListForm extends QueryPageListForm {

    @ApiModelProperty("商品Id")
    private Long goodsId;

    @ApiModelProperty("客户类型")
    private Integer customerType;

    @ApiModelProperty("客户省份编码")
    private String provinceCode;

    @ApiModelProperty("客户城市编码")
    private String cityCode;

    @ApiModelProperty("客户区域编码")
    private String regionCode;
}
