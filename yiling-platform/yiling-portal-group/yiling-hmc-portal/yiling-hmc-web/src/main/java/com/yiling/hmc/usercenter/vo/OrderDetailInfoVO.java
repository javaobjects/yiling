package com.yiling.hmc.usercenter.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 订单详情VO
 * </p>
 *
 * @author fan.shen
 * @date 2022/4/12
 */
@Data
public class OrderDetailInfoVO {

    @ApiModelProperty("订单信息")
    private OrderVO orderVO;

    @ApiModelProperty("订单详细信息")
    private List<OrderDetailVO> orderDetailVOList;

    @ApiModelProperty("保单信息")
    private InsuranceRecordVO insuranceRecordVO;

    @ApiModelProperty("倒计时剩余时间")
    private Long countDownTime;

    @ApiModelProperty("互联网问诊地址")
    private String internetConsultationUrl;

    @ApiModelProperty("订单收货人")
    private OrderRelateUserVO receiveVO;


}
