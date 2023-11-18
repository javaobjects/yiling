package com.yiling.hmc.remind.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 设为已用/未用入参
 *
 * @Description
 * @Author fan.shen
 * @Date 2022/5/31
 */
@Data
@ToString
@ApiModel(value = "设为已用/未用", description = "设为已用/未用入参")
@Slf4j
public class CheckMedsRemindForm implements Serializable {

    private static final long serialVersionUID = -7722430332896313642L;

    /**
     * id
     */
    @NotNull
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 确认状态 1-已确认，2-未确认
     */
    @NotNull
    @ApiModelProperty(value = "确认状态 1-已确认，2-未确认")
    private Integer confirmStatus;


    public void check() {

    }


}
