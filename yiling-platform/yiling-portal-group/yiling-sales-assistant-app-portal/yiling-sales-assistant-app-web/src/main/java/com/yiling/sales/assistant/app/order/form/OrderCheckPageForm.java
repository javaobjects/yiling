package com.yiling.sales.assistant.app.order.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 销售助手B2B
 * @author:wei.wang
 * @date:2021/11/1
 */
@Data
public class OrderCheckPageForm extends QueryPageListForm {
    /**
     * 类型:1-待付款 2-待发货 3-待收货 4-已完成 5-已取消
     */
    @ApiModelProperty(value = "类型:1-待付款/待审核  2-待发货 3-待收货/已发货 4-已完成/已签收 5-已取消 6-部分发货", required = false)
    private Integer type;

    /**
     * 订单号
     */
    @ApiModelProperty(value = "查询条件", required = false)
    private String condition;

    /**
     * 采购商Eid
     */
    @ApiModelProperty(value = "采购商Eid", required = false)
    private Long buyerEid;
}
