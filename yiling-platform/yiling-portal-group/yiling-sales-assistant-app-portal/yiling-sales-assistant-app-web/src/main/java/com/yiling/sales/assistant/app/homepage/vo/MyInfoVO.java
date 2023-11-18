package com.yiling.sales.assistant.app.homepage.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 我的个人信息VO
 * @author lun.yu
 * @version V1.0
 * @date: 2021/9/27
 */
@Data
@ApiModel
@Accessors(chain = true)
public class MyInfoVO {

    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    private Long userId;

    /**
     * 用户姓名
     */
    @ApiModelProperty("用户姓名")
    private String name;

    /**
     * 当前登录人手机号
     */
    @ApiModelProperty("当前登录人手机号")
    private String mobile;

}
