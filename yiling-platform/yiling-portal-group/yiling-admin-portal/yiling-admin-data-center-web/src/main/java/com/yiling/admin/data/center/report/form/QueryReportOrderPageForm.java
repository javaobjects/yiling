package com.yiling.admin.data.center.report.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2022-05-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryReportOrderPageForm extends QueryPageListForm {

    /**
     * 报表id
     */
    @NotNull
    @ApiModelProperty(value = "报表id")
    private  Long reportId;

    /**
     * 订单id
     */
    @NotNull
    @ApiModelProperty(value = "订单id")
    private  Long orderId;
}
