package com.yiling.admin.b2b.settlement.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2023-04-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryYeeSettlePageListForm extends QueryPageListForm {

    /**
     * 结算订单号
     */
    @ApiModelProperty("结算单号")
    private String summaryNo;

    /**
     * 创建开始时间
     */
    @ApiModelProperty("创建开始时间")
    private Date createTimeBegin;

    /**
     * 创建开始时间
     */
    @ApiModelProperty("创建开始时间")
    private Date createTimeEnd;
}
