package com.yiling.admin.pop.goods.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2021/6/8
 */
@Data
public class GoodsDisableVO {

    @ApiModelProperty(value = "协议商品是否已经选择")
    private Boolean agreementDisable;

    @ApiModelProperty(value = "协议商品占用描述")
    private String agreementDesc;
}
