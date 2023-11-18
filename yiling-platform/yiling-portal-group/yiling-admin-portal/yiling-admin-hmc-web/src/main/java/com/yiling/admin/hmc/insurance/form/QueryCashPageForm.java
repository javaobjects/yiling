package com.yiling.admin.hmc.insurance.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询保单兑付记录
 * @author: gxl
 * @date: 2022/4/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCashPageForm extends QueryPageListRequest {

    private static final long serialVersionUID = 9022136738238303731L;

    /**
     * 购买保险id
     */
    @NotNull
    @ApiModelProperty(value = "保险销售记录id")
    private Long insuranceRecordId;
}