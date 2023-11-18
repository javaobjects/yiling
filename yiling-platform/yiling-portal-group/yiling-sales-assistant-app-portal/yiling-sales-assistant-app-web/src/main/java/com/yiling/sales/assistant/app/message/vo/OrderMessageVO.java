package com.yiling.sales.assistant.app.message.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/1/19
 */
@Data
@Accessors(chain = true)
public class OrderMessageVO implements Serializable {

    @ApiModelProperty(value = "订单编号")
    private String  orderNo;

    @ApiModelProperty(value = "下单时间")
    private Date    createTime;

    @ApiModelProperty(value = "业务进度的消息节点：10-待付款 11-待审核 12-待发货 13-部分发货 14-已发货 15-已收货 16-已完成 17-已取消 20-客户信息认证失败")
    private Integer node;
}
