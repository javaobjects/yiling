package com.yiling.sales.assistant.app.order.vo;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.framework.common.pojo.vo.FileInfoVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.sales.assistant.app.order.vo
 * @date: 2022/1/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PopOrderInfoVO extends OrderFullDetailInfoVO{

    @ApiModelProperty(value = "收货地址信息")
    private OrderAddressVO orderAddress;

    /**
     * 购销合同信息
     */
    @ApiModelProperty(value = "购销合同信息")
    private List<FileInfoVO> contractList;

    /**
     * 票折单号
     */
    @ApiModelProperty(value = "票折单号")
    private String ticketDiscountNo;

    /**
     *票折金额
     */
    @ApiModelProperty(value = "票折金额")
    private BigDecimal ticketDiscountAmount;

    /**
     * 现折金额
     */
    @ApiModelProperty(value = "现折金额")
    private BigDecimal cashDiscountAmount;


    @ApiModelProperty(value = "票折信息")
    private List<OrderTicketDiscountVO> orderTicketDiscountVoList;


}
