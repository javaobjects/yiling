package com.yiling.f2b.web.order.form;

import java.util.Date;


import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrderListPageForm extends QueryPageListForm {
    /**
     * 供应商名称
     */
    @ApiModelProperty(value = "供应商名称")
    private String sellerEname;

    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    /**
     *订单ID
     */
    @ApiModelProperty(value = "订单ID")
    private Long id;

    /**
     * 下单开始时间
     */
    @ApiModelProperty(value = "下单开始时间")
    private Date startCreateTime;

    /**
     * 下单开始时间
     */
    @ApiModelProperty(value = "下单结束时间")
    private Date endCreateTime;

    /**
     * 下单开始时间
     */
    @ApiModelProperty(value = "类型 0-全部,1-未提交,2-待审核,3-待发货,4-部分发货,5-已发货,6-已收货,7-已取消,8-已驳回",required = true)
    private Integer type;
}
