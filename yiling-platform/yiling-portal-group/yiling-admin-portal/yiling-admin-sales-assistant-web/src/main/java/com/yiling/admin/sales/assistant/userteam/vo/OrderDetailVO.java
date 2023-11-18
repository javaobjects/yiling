package com.yiling.admin.sales.assistant.userteam.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 销售助手后台-用户对应订单列表VO
 * @author lun.yu
 * @version V1.0
 * @date: 2021/9/30
 */
@Data
@ApiModel
public class OrderDetailVO implements Serializable {

    @ApiModelProperty("订单收货信息")
    private OrderReceiveInfoVO orderReceiveInfoVO;

    @ApiModelProperty("订单对应的商品信息")
    private List<OrderProductItemVO> productItemVoList;




}
