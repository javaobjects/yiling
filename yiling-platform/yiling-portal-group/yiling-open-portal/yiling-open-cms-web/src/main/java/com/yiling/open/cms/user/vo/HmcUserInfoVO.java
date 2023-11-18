package com.yiling.open.cms.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 登录信息
 *
 * @Description
 * @Author fan.shen
 * @Date 2022/8/26
 */
@Data
@Accessors(chain = true)
@Builder
@ApiModel("C端用户信息")
@AllArgsConstructor
@NoArgsConstructor
public class HmcUserInfoVO {

    /**
     * 昵称
     */
    @ApiModelProperty("昵称")
    private String nickName;

    /**
     * 用户ID
     */
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 头像路径
     */
    @ApiModelProperty("头像路径")
    private String avatarUrl;

    /**
     * 小程序appId
     */
    @ApiModelProperty(value = "小程序appId")
    private String appId;

    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String mobile;

    /**
     * 小程序openId
     */
    @ApiModelProperty("小程序openId")
    private String miniProgramOpenId;

    /**
     * 微信unionId
     */
    @ApiModelProperty("微信unionId")
    private String unionId;

    /**
     * 登录次数
     */
    @ApiModelProperty(value = "登录次数")
    private Integer loginCount;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "注册时间")
    private Date createTime;

}
