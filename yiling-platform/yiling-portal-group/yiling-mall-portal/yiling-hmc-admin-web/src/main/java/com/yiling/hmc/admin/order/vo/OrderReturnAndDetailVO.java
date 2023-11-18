package com.yiling.hmc.admin.order.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2022/3/30
 */
@Data
public class OrderReturnAndDetailVO extends OrderReturnVO {

    @ApiModelProperty("退货单明细")
    private List<OrderReturnDetailVO> orderReturnDetailList;

    @ApiModelProperty("退货单日志")
    private List<OrderOperateVO> operateList;

    @ApiModelProperty("收货人姓名")
    private String receiveUserName;

    @ApiModelProperty("收货人手机号")
    private String receiveUserTel;
}
