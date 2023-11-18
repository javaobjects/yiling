package com.yiling.b2b.app.order.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * b2b移动端查看
 * @author:wei.wang
 * @date:2021/11/1
 */
@Data
public class OrderB2BPageForm extends QueryPageListForm {
    /**
     * 类型:1-待付款 2-待发货 3-待收货 4-已完成 5-已取消
     */
    @ApiModelProperty(value = "类型:1-待付款 2-待发货 3-待收货 4-已完成 5-已取消", required = false)
    private Integer type;

    /**
     * 订单号或者供应商名称
     */
    @ApiModelProperty(value = "查询条件", required = false)
    private String condition;
}
