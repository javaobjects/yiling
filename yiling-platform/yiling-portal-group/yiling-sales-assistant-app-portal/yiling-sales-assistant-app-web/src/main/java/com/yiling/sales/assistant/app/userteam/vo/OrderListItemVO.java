package com.yiling.sales.assistant.app.userteam.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 用户对应订单列表VO
 * @author lun.yu
 * @version V1.0
 * @date: 2021/9/26
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderListItemVO extends BaseVO {

    /**
     * 订单ID
     */
    @ApiModelProperty("订单ID")
    private Long orderId;

    /**
     * 企业名称
     */
    @ApiModelProperty("企业名称")
    private String customerName;

    /**
     * 下单时间
     */
    @ApiModelProperty("下单时间")
    private Date createTime;

    /**
     * 订单金额
     */
    @ApiModelProperty("订单金额")
    private BigDecimal orderAmount;

    /**
     * 订单号
     */
    @ApiModelProperty("订单号")
    private String orderNo;


}
