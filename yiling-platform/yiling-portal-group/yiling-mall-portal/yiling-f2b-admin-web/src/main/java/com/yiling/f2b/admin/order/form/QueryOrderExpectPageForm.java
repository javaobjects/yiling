package com.yiling.f2b.admin.order.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 预订单列表查询Form
 * @author:wei.wang
 * @date:2021/7/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryOrderExpectPageForm extends QueryPageListForm {
    /**
     * 供应商名称
     */
    @ApiModelProperty(value = "供应商名称")
    private String distributorEname;

    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String orderNo;

    /**
     * 订单状态：10-待审核 20-待发货 30-已发货 40-已收货 100-已完成 -10-已取消 -20-审核驳回
     */
    @ApiModelProperty(value = "审核状态： 1-未提交 2-待审核 3-审核通过 4-审核驳回")
    private Integer auditStatus;

    /**
     * 下单开始时间
     */
    @ApiModelProperty(value = "下单开始时间")
    private Date startCreatTime;

    /**
     * 下单结束时间
     */
    @ApiModelProperty(value = "下单结束时间")
    private Date endCreatTime;

    /**
     *订单ID
     */
    @ApiModelProperty(value = "订单ID")
    private Long id;
}
