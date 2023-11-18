package com.yiling.hmc.remind.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 用药详情入参
 *
 * @Description
 * @Author fan.shen
 * @Date 2022/6/7
 */
@Data
@ToString
@ApiModel(value = "用药详情", description = "用药详情入参")
@Slf4j
public class QueryMedsRemindForm implements Serializable {

    private static final long serialVersionUID = -7722430332896313642L;

    /**
     * id
     */
    @NotNull
    @ApiModelProperty(value = "id")
    private Long id;



}
