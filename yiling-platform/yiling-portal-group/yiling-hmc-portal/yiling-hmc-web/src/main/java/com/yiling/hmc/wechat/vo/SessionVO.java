package com.yiling.hmc.wechat.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 登录信息
 *
 * @Description
 * @Author fan.shen
 * @Date 2022/3/25
 */
@Data
@Accessors(chain = true)
@Builder
@ApiModel("会话信息")
@AllArgsConstructor
@NoArgsConstructor
public class SessionVO {

    /**
     * uk
     */
    @ApiModelProperty("唯一标志")
    private String uk;

    /**
     * 登录信息
     */
    @ApiModelProperty("登录信息")
    private LoginInfoVO loginInfo;

}
