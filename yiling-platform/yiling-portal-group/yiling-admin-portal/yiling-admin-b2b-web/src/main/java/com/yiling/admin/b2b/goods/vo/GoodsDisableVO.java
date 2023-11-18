package com.yiling.admin.b2b.goods.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2021/6/8
 */
@Data
public class GoodsDisableVO {

//    @ApiModelProperty(value = "协议商品是否已经选择")
//    private Boolean agreementDisable;
//
//    @ApiModelProperty(value = "协议商品占用描述")
//    private String agreementDesc;

    @ApiModelProperty(value = "是否允许被选择 0-可 1-不可")
    private Integer isAllowSelect;

    @ApiModelProperty(value = "控价是否已经选择")
    private Boolean limitDisable;
}
