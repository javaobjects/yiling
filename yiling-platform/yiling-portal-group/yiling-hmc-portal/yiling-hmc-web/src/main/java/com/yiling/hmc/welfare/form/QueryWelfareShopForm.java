package com.yiling.hmc.welfare.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 查询承接药店入参
 *
 * @Description
 * @Author fan.shen
 * @Date 2022-09-27
 */
@Data
@ToString
@ApiModel(value = "查询承接药店入参", description = "查询承接药店入参")
@Slf4j
public class QueryWelfareShopForm implements Serializable {

    private static final long serialVersionUID = -7722430332896313642L;

    /**
     * 福利计划id
     */
    @NotNull
    @ApiModelProperty(value = "福利计划id")
    private Long welfareId;

}
