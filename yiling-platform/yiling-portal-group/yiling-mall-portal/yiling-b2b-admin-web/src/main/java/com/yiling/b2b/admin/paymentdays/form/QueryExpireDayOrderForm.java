package com.yiling.b2b.admin.paymentdays.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 账期到期提醒列表 Form
 *
 * @author: lun.yu
 * @date: 2021/10/29
 */
@Data
@Accessors(chain = true)
public class QueryExpireDayOrderForm extends QueryPageListForm {

    /**
     * 采购商名称
     */
    @ApiModelProperty(value = "采购商名称")
    private String buyerEname;

    /**
     * 还款状态
     */
    @ApiModelProperty(value = "还款状态")
    private Integer repaymentStatus;


    /**
     * 供应商名称
     */
    @ApiModelProperty(value = "供应商名称")
    private String sellerEname;

    /**
     * 发货开始时间
     */
    @ApiModelProperty(value = "发货开始时间")
    private Date deliveryStartTime;

    /**
     * 发货结束时间
     */
    @ApiModelProperty(value = "发货结束时间")
    private Date deliveryEndTime;


}
