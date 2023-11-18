package com.yiling.admin.hmc.settlement.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 同步订单到保司的返回数据
 *
 * @author: yong.zhang
 * @date: 2022/7/5
 */
@Data
public class SyncOrderResultVO implements Serializable {

    @ApiModelProperty("订单id")
    private Long orderId;

    @ApiModelProperty("订单编号")
    private String orderNo;

}
