package com.yiling.hmc.welfare.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;

/**
 * 查询核销状态
 *
 * @Description
 * @Author fan.shen
 * @Date 2022-09-27
 */
@Data
@ToString
@ApiModel(value = "查询核销状态", description = "查询核销状态")
@Slf4j
public class QueryVerifyStatusForm {

    private static final long serialVersionUID = -7722430332896313642L;

    /**
     * 福利券码
     */
    @NotNull
    @ApiModelProperty(value = "福利券码")
    private String couponCode;

}
