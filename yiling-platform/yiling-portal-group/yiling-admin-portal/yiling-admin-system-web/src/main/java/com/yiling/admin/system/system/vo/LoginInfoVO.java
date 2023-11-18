package com.yiling.admin.system.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 登录用户信息 VO
 *
 * @author: xuan.zhou
 * @date: 2021/5/13
 */
@Data
@ApiModel
public class LoginInfoVO {

    /**
     * 用户信息
     */
    @ApiModelProperty("用户信息")
    private AdminVO userInfo;


    /**
     * token
     */
    @ApiModelProperty("token")
    private String token;
}
