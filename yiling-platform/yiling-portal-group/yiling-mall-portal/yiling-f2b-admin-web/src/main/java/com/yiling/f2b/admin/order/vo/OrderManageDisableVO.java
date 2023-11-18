package com.yiling.f2b.admin.order.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2021/12/15
 */
@Data
public class OrderManageDisableVO {

    @ApiModelProperty("是否允许审核")
    private Boolean checkDisable;
}
