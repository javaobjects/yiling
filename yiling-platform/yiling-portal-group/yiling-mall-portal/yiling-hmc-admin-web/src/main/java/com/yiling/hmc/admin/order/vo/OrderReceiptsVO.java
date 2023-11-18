package com.yiling.hmc.admin.order.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 方便前端修改图片时图片的处理
 *
 * @author: yong.zhang
 * @date: 2022/7/12
 */
@Data
public class OrderReceiptsVO {

    @ApiModelProperty("票据图片的key值")
    private String orderReceiptsKey;

    @ApiModelProperty("对应票据图片的url地址")
    private String orderReceiptsUrl;
}
