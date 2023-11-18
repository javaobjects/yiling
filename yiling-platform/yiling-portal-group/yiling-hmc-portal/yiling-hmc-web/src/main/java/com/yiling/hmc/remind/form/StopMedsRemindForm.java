package com.yiling.hmc.remind.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 停止提醒入参
 *
 * @Description
 * @Author fan.shen
 * @Date 2022/6/6
 */
@Data
@ToString
@ApiModel(value = "停止提醒", description = "停止提醒入参")
@Slf4j
public class StopMedsRemindForm implements Serializable {

    private static final long serialVersionUID = -7722430332896313642L;

    /**
     * id
     */
    @NotNull
    @ApiModelProperty(value = "id")
    private Long id;



}
