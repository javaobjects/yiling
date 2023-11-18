package com.yiling.hmc.wechat.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 登录信息
 *
 * @Description
 * @Author fan.shen
 * @Date 2022/3/23
 */
@Data
@Accessors(chain = true)
@Builder
@ApiModel("登录信息")
public class LoginInfoVO {

    /**
     * 用户ID
     */
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 昵称
     */
    @ApiModelProperty("昵称")
    private String nickName;

    /**
     * 头像路径
     */
    @ApiModelProperty("头像路径")
    private String avatarUrl;

    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String mobile;

    /**
     * 微信应用AppID
     */
    @ApiModelProperty("微信应用AppID")
    private String appId;

    /**
     * 微信用户OpenID
     */
    @ApiModelProperty("微信用户OpenID")
    private String openId;

    /**
     * 小程序openId（已废弃）
     */
    @ApiModelProperty("小程序openId（已废弃）")
    private String miniProgramOpenId;

    /**
     * 微信用户UnionID
     */
    @ApiModelProperty("微信用户UnionID")
    private String unionId;

    /**
     * token
     */
    @ApiModelProperty("token")
    private String token;

    /**
     * 邀请人id
     */
    @ApiModelProperty("邀请人id")
    private Long inviteUserId;

    /**
     * 腾讯IM通讯密码
     */
    @ApiModelProperty("腾讯IM通讯密码")
    private String tencentIMUserSig;

}
