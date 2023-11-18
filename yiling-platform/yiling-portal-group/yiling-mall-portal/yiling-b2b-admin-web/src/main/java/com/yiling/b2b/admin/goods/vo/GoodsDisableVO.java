package com.yiling.b2b.admin.goods.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2021/6/8
 */
@Data
public class GoodsDisableVO {

    @ApiModelProperty(value = "控价是否已经选择")
    private Boolean limitDisable;

    @ApiModelProperty(value = "控价商品占用描述")
    private String limitDesc;

    @ApiModelProperty(value = "是否允许被选择 0-可 1-不可")
    private Integer isAllowSelect;

}
