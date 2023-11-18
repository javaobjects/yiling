package com.yiling.f2b.admin.enterprise.form;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 账户临时额度 Form
 *
 * @author: tingwei.chen
 * @date: 2021/7/5
 */
@Data
@Accessors(chain = true)
public class QueryExpireDayOrderForm extends QueryPageListForm {
    /**
     * acountId
     */
    @NotNull
    @ApiModelProperty(value = "acountId", required = true)
    private Long acountId;

    /**
     * 采购商名称
     */
    @NotNull
    @ApiModelProperty(value = "采购商名称", required = true)
    private String buyerEname;

    /**
     * 还款状态
     */
    @NotNull
    @ApiModelProperty(value = "还款状态", required = true)
    private Integer repaymentStatus;


    /**
     * 供应商名称
     */
    @NotNull
    @ApiModelProperty(value = "供应商名称", required = true)
    private String sellerEname;

    /**
     * 开始时间
     */
    @NotNull
    @ApiModelProperty(value = "开始时间", required = true)
    private Date startTime;

    /**
     * 结束时间
     */
    @NotNull
    @ApiModelProperty(value = "结束时间", required = true)
    private Date endTime;
}
