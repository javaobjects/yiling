package com.yiling.hmc.admin.order.form;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/3/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderPageForm extends QueryPageListForm {

    @ApiModelProperty("订单状态:1-预订单待支付/2-已取消/3-待自提/4-待发货/5-待收货/6-已收货/7-已完成/8-取消已退")
    private List<Integer> orderStatusList;

    @ApiModelProperty("订单状态:1-预订单待支付/2-已取消/3-待自提/4-待发货/5-待收货/6-已收货/7-已完成/8-取消已退")
    private Integer orderStatus;

    @ApiModelProperty("平台订单编号")
    private String orderNo;

    @ApiModelProperty("药品服务终端id")
    private Long eid;

    @ApiModelProperty("下单人")
    private Long orderUser;

    @ApiModelProperty("配送方式 1-自提 2-物流")
    private Integer deliverType;

    @ApiModelProperty("日期段-开始时间")
    private Date startTime;

    @ApiModelProperty("日期段-截止时间")
    private Date stopTime;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("收货人姓名")
    private String receiveUserName;

    @ApiModelProperty("收货人手机号")
    private String receiveUserTel;

    @ApiModelProperty("开方状态 1-已开，2-未开")
    private Integer prescriptionStatus;
}
