package com.yiling.b2b.admin.paymentdays.form;

import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 账期订单查询 Form
 *
 * @author: lun.yu
 * @date: 2021/10/29
 */
@Data
@Accessors(chain = true)
public class QueryPaymentDaysOrderForm extends QueryPageListForm {

    /**
     * 账户Id
     */
    @NotNull
    @Min(1)
    @ApiModelProperty(value = "账户Id", required = true)
    private Long accountId;

    /**
     * 还款状态
     */
    @ApiModelProperty(value = "还款状态", required = true)
    private Integer status;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    /**
     * 菜单类型：1.已使用订单，2.已还款订单 3.待还款订单
     */
    @ApiModelProperty(value = "菜单类型：1.已使用订单，2.已还款订单 3.待还款订单")
    private Integer type;
}
