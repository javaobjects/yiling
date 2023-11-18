package com.yiling.b2b.admin.goods.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2021/6/8
 */
@Data
public class CustomerDisableVO {

    @ApiModelProperty(value = "客户控销是否已经选择")
    private Boolean controlDisable;

    @ApiModelProperty(value = "客户控销占用描述")
    private String controlDesc;
}
